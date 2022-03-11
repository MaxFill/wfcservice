package ru.rt.fsom.wfc.wfcservice.service.users;

import com.google.gson.Gson;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import ru.rt.fsom.wfc.wfcservice.data.tickets.TicketFilter;
import ru.rt.fsom.wfc.wfcservice.dict.SysParams;
import ru.rt.fsom.wfc.wfcservice.service.security.CarmTocken;
import ru.rt.fsom.wfc.wfcservice.service.security.SecurityFacade;

@Path("/user")
@Stateless
public class UserService {
    protected static final Logger LOGGER = Logger.getLogger(SysParams.LOGGER_NAME);
    
    @EJB private UserSettingsFacade settingsFacade;
    @EJB private UserFacade userFacade;
    @EJB private SecurityFacade securityFacade;
    
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    @Path("/login")
    public Response checkLogin(@QueryParam("name") String userName, @QueryParam("pwd") String pwd){
	//LOGGER.log(Level.INFO, "checkLogin name = {0} pwd={1}", new Object[]{userName, pwd});
	User user = userFacade.checkLogin(userName, pwd);	
	if (user == null) return Response.status(Response.Status.NOT_FOUND).build();	
        CarmTocken tocken = securityFacade.createJWT(user);
	if (tocken == null) return Response.serverError().build();
        return Response.ok(tocken).build();
    }         
        
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @PUT
    @Path("/filter/save")
    public Response saveFilter(String gsonStr, @QueryParam("token") String token){
	//LOGGER.log(Level.INFO, "saveFilter from json = {0}", gsonStr);	
	if (!securityFacade.checkJWT(token)) return Response.status(Response.Status.UNAUTHORIZED).build();
        Gson gson = new Gson();
	TicketFilter result = settingsFacade.saveFilter(gson.fromJson(gsonStr, TicketFilter.class));
	//LOGGER.log(Level.INFO, "sucessfully saved! filter = {0}", result.getName());
	return Response.ok(result).build();
    }
    
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    @Path("/filters")
    public Response loadFilters(@QueryParam("userId") String userId, @QueryParam("token") String token){
	//LOGGER.log(Level.INFO, "loadFilters userId = {0}", userId);
	if (!securityFacade.checkJWT(token)) return Response.status(Response.Status.UNAUTHORIZED).build();

        Integer id = null;
	try {
	    id = Integer.valueOf(userId);
	} catch (NumberFormatException ex) {
	    LOGGER.log(Level.SEVERE, "NumberFormatException: {0}", ex.getMessage());
	}
	if (id == null){
	    return Response.status(Response.Status.BAD_REQUEST).build();
	}
	List<TicketFilter> results = settingsFacade.loadFiltersByUserId(id);	
	if (results == null){
	    return Response.status(Response.Status.NOT_FOUND).build();
	}
	return Response.ok(results).build();
    } 
    
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @PUT
    @Path("/filter/delete")
    public Response deleteFilter(String gsonStr, @QueryParam("token") String token){
	//LOGGER.log(Level.INFO, "deleteFilter from json = {0}", gsonStr);	
	if (!securityFacade.checkJWT(token)) return Response.status(Response.Status.UNAUTHORIZED).build();

        Gson gson = new Gson();
	TicketFilter result = settingsFacade.deleteFilter(gson.fromJson(gsonStr, TicketFilter.class));
	//LOGGER.log(Level.INFO, "sucessfully deleteFilter = {0}", result.getName());
	return Response.ok(result).build();
    }
}