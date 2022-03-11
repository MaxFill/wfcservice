package ru.rt.fsom.wfc.wfcservice.config;

import com.comptel.mds.sas5.taskengine.rmi_fifo.RMIFifoClient;
import com.comptel.mds.sas5.taskengine.rmi_fifo.exceptions.ClientAlreadyRegisteredException;
import com.comptel.mds.sas5.taskengine.rmi_fifo.exceptions.ClientNotAuthorisedException;
import com.comptel.mds.sas5.taskengine.rmi_fifo.exceptions.InvalidModeException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.crypto.SecretKey;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import org.apache.commons.lang3.StringUtils;
import ru.rt.fsom.wfc.wfcservice.service.RMIFifo.RMICallbackHandler;
import ru.rt.fsom.wfc.wfcservice.dict.SysParams;

/*
	resources.add(ru.rt.fsom.wfc.wfcservice.service.users.UserService.class);
	resources.add(ru.rt.fsom.wfc.wfcservice.service.ticket.attached.AttachedService.class);
	resources.add(ru.rt.fsom.wfc.wfcservice.service.ticket.group.GroupService.class);
	resources.add(ru.rt.fsom.wfc.wfcservice.service.ticket.TicketService.class);
*/

@Singleton
@Startup
public class Conf {    
    protected static final Logger LOGGER = Logger.getLogger(SysParams.LOGGER_NAME);
    
    private static final int ERR_MIN_LENGHT = 1024;
    private static final String DATA_SOURCE_NAME = "java:jboss/datasources/PostgresDS";
    private final static String UNREGISTR_EXCEPTION = "Client unregister exception";            
    private final static String DEFAULT_FIREBASE_JSON = "/opt/wildfly/standalone/configuration/fsom-carm-firebase-adminsdk-bo7zt-c6138018d5.json";
    private final static int DEFAULT_UPLOAD_MAX_SIZE = 1000000;
    
    private RMIFifoClient client;    
    private ResourceBundle properties;
    private DataSource ds;            
    private String versionInfo;
    private String secretKey;
    
    @PostConstruct
    private void init() {	
	initConfFile();
	initConnectionPool();
	loadVersionInfo();
	initRmiFifoClient();
	//initFireBaseApp();
	LOGGER.log(Level.INFO, "Now start liquibase for check and update database. Please wait!");
    }
    
    public Connection getJdbcConnection() throws SQLException{
	Connection connection = null;
	if (ds != null){ 
	    connection = ds.getConnection();
	} else {
	    throw new SQLException("DataSource is not initialized!");
	}
	return connection;
    }
    
    public RMIFifoClient getRMIFifoClient() throws RemoteException, ClientAlreadyRegisteredException, InvalidModeException, ClientNotAuthorisedException{
	if (client == null){
	    initRmiFifoClient();
	}
	if (client != null && !client.isRegistered()){
	    client.register();
	}
	return client;
    }
    
    public void resetRMIFifoClient(){
	rmiFifoClientUnregistr();
    }
    
    public void rmiFifoClientUnregistr(){
	LOGGER.log(Level.INFO, "close RMI FIFO client...");
	if (client == null) return;
	try {
	    if (client.isRegistered()){
		client.unregister();
	    }
	    client = null;
	    LOGGER.log(Level.INFO, "RMI FIFO client unregistered successfuly!");
	} catch(RemoteException ex){
	    LOGGER.log(Level.SEVERE, UNREGISTR_EXCEPTION, ex);
	}
    }     
    
    public String getVersionInfo() {
	return versionInfo;
    }
    
    private int getErrMinLenght(){
	return ERR_MIN_LENGHT;
    }        

    public String getFireBaseJsonFile(){	
	return getStrPropertyByKey("firebase_json_file", DEFAULT_FIREBASE_JSON);
    }
    
    public int getMaxUploadFileSize(){
	return getIntPropertyByKey("upload_max_size", DEFAULT_UPLOAD_MAX_SIZE);
    }
    
    public String getSecretKey(){
	if (StringUtils.isBlank(secretKey)){
	    SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
	    secretKey = Encoders.BASE64.encode(key.getEncoded());
	}
	return secretKey;
    }
     
    /* *** privates *** */
    /*
    private void initFireBaseApp(){
	FileInputStream serviceAccount = null;
	try {
	    //LOGGER.log(Level.INFO, "start init Firebase ");
	    serviceAccount = new FileInputStream(getFireBaseJsonFile());	    
	    FirebaseOptions options = FirebaseOptions.builder()
		.setCredentials(GoogleCredentials.fromStream(serviceAccount))
		.setServiceAccountId("firebase-adminsdk-bo7zt@fsom-carm.iam.gserviceaccount.com")
		.build();
	    //LOGGER.log(Level.INFO, "initialize Firebase ...");
	    FirebaseApp.initializeApp(options);
	    //LOGGER.log(Level.INFO, "initialize FirebaseApp complete!");
	} catch (IOException ex) {
	    LOGGER.log(Level.SEVERE, null, ex);
	} finally {
	    try {
		if (serviceAccount != null){
		    serviceAccount.close();
		}
	    } catch (IOException ex) {
		Logger.getLogger(Conf.class.getName()).log(Level.SEVERE, null, ex);
	    }
	}
    }   
    */
    private void loadVersionInfo() {	
	LOGGER.log(Level.INFO, "init version info ...");
	Enumeration resEnum;
	try {
	    resEnum = Thread.currentThread().getContextClassLoader().getResources(JarFile.MANIFEST_NAME);
	    while (resEnum.hasMoreElements()) {
		try {
		    URL url = (URL)resEnum.nextElement();
		    InputStream is = url.openStream();
		    if (is != null) {
			Manifest manifest = new Manifest(is);
			Attributes attrs = manifest.getMainAttributes();
			if (attrs.getValue("build_name") != null){
			    StringBuilder sb = new StringBuilder();
			    sb.
				append("{").
				append("name:'").append(attrs.getValue("build_name")).append("', ").
				append("version:'").append(attrs.getValue("build_version")).append("', ").
				append("date:'").append(attrs.getValue("build_date")).append("', ").
				append("specification:'").append(attrs.getValue("build_specif")).append("'").
				append("}");
			    LOGGER.log(Level.INFO, "Version info loaded: {0}", sb.toString());
			    versionInfo = sb.toString();			    
			}
		    }		    
		}
		catch (IOException ex) {
		    LOGGER.log(Level.SEVERE, null, ex);
		}
	    }
	} catch (IOException ex){
	    LOGGER.log(Level.SEVERE, null, ex);
	}
    }
    	
    private void initRmiFifoClient() {	
	LOGGER.log(Level.INFO, "init RMIFIFO client ...");
	int port = getIntPropertyByKey("port");
	final String hostname = getStrPropertyByKey("host");	
	final String login = getStrPropertyByKey("user");
	final String pwl = getStrPropertyByKey("password");
	LOGGER.log(Level.INFO, "init RMIFIFO client: {0}, {1}, {2}, {3}, {4}", new Object[]{login, login, pwl, hostname, port});
	client = new RMIFifoClient(login, login, pwl, hostname, port, new RMICallbackHandler());
	LOGGER.log(Level.INFO, "init RMIFIFO client ok!");
    }    
    
    private void initConnectionPool(){
	try {
	    LOGGER.log(Level.INFO, "init db connection poll ...");
	    InitialContext initContext = new InitialContext();
	    ds = (DataSource) initContext.lookup(DATA_SOURCE_NAME);
	    LOGGER.log(Level.INFO, "init db connection poll ok!");
	} catch (NamingException ex) {
	    LOGGER.log(Level.SEVERE, "error init connection pool! ", ex);
	}
    }
    
    private void initConfFile(){
        try {
	    LOGGER.log(Level.INFO, "init config file ...");
            File props_path = new File(System.getProperty("jboss.server.config.dir"));
            URL[] urls = {props_path.toURI().toURL()};
            ClassLoader loader = new URLClassLoader(urls);
            properties = ResourceBundle.getBundle("FSOM", Locale.ROOT, loader);
	    LOGGER.log(Level.INFO, "init config file ok!");
        } catch (MalformedURLException ex) {
            LOGGER.log(Level.SEVERE, "error loading config file!", ex);
        }
    }
    
    private Integer getIntPropertyByKey(final String key){        
        if (null != properties){
            try { 
               return Integer.valueOf(properties.getString(key));
            }
            catch (MissingResourceException | ClassCastException ex){
		LOGGER.log(Level.SEVERE, "A required configuration parameter value is missing ={0}", key);
	    }
        }
        return null;
    }
	
    private String getStrPropertyByKey(final String key){       
        if (null != properties){
            try { 
               return properties.getString(key);
            }
            catch (MissingResourceException | ClassCastException ex){
		LOGGER.log(Level.SEVERE, "A required configuration parameter value is missing ={0}", key);
	    }
        }
        return null;
    } 
    
    private String getStrPropertyByKey(final String key, final String defaultValue){       
        String result = defaultValue;
        if (null != properties){
            try { 
               result = properties.getString(key);
            }
            catch (MissingResourceException | ClassCastException ignoreIt){}//ignore this and return default value
        }
        return result;
    }  
    
    private Integer getIntPropertyByKey(final String key, final int defaultValue){       
        Integer result = defaultValue;
        if (null != properties){
            try { 
               result = Integer.valueOf(properties.getString(key));
            }
            catch (MissingResourceException | ClassCastException ignoreIt){}//ignore this and return default value
        }
        return result;
    } 
    
    private String getShortMsg(String longMsg){
	String shortMsg = "";
	if (longMsg != null){
	    shortMsg = longMsg.substring(0, Math.min(getErrMinLenght(), longMsg.length()));
	}
	return shortMsg;
    }
}
