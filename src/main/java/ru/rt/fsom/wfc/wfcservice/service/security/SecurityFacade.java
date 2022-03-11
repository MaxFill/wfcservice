package ru.rt.fsom.wfc.wfcservice.service.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.spec.SecretKeySpec;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import ru.rt.fsom.wfc.wfcservice.config.Conf;
import ru.rt.fsom.wfc.wfcservice.dict.SysParams;
import ru.rt.fsom.wfc.wfcservice.service.users.User;

/**
 *
 * @author Maksim.Filatov
 */
@Stateless
public class SecurityFacade {
    private static final Logger LOGGER = Logger.getLogger(SysParams.LOGGER_NAME);
    private static final long DELTA_EXPIRE_JWT_MILS = 86400000;
    
    @EJB protected Conf conf;
    
    public boolean checkJWT(String token){   
	return decodeJWT(token) != null;        
    }
   
    /*
    public boolean checkFireBaseJWT(String tokenId){   
	return decodeFireBaseJWT(tokenId) != null;
    }
    */
    public CarmTocken createJWT(User user){
	String tocken = makeJWT(user.getUserName(), user.getPassword());	
	CarmTocken carmTocken = new CarmTocken(user.getId(), tocken);
	return carmTocken;
    }
	
    /*
    public FireBaseToken createFireBaseToken(User user){
	String token = makeFireBaseToken();
	if (token == null) return null;
	return new FireBaseToken(token);	
    }
    */
    
    /* privates */            
    private Claims decodeJWT(String jwt){
	try {
	    Claims claims = Jwts.parser()
		.setSigningKey(Base64.getDecoder().decode(conf.getSecretKey()))
		.parseClaimsJws(jwt).getBody();
	    //String cred = claims.getIssuer() + ":" +  claims.getSubject();
	    //LOGGER.log(Level.INFO, "result decoded JWT token is ok!");
            return claims;
	} catch(MalformedJwtException | SignatureException ex){	    
	    LOGGER.log(Level.SEVERE, "Your JWT token incorrect! {0}", ex.getMessage());
	} catch(ExpiredJwtException ex){
	    LOGGER.log(Level.SEVERE, "Your JWT token has expired! {0}", ex.getMessage());
	}
	return null;
    }
    
    private String makeJWT(String issuer, String subject){
	SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;	
	long nowMillis = System.currentTimeMillis();
	byte[] apiKeySecretBytes = Base64.getDecoder().decode(conf.getSecretKey());
	Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());        	
	Date dateExp = new Date(nowMillis + DELTA_EXPIRE_JWT_MILS);
	JwtBuilder builder = Jwts.builder()
		.setId(UUID.randomUUID().toString())
		.setIssuedAt(new Date(nowMillis))
		.setSubject(subject)
		.setIssuer(issuer)
		.setExpiration(dateExp)
		.signWith(signatureAlgorithm, signingKey);
	return builder.compact();
    }
    
    /*
    private String makeFireBaseToken() {	
	try {	    
	    String customToken = FirebaseAuth.getInstance().createCustomToken(UUID.randomUUID().toString());
	    return customToken;
	} catch (FirebaseAuthException ex) {
	    LOGGER.log(Level.SEVERE, null, ex);
	}
	return null;
    }
    
    private String decodeFireBaseJWT(String tokenId){   
	try{
	    FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(tokenId);
	    return decodedToken.getUid();
	} catch (FirebaseAuthException ex) {
	    LOGGER.log(Level.SEVERE, ex.getMessage());
	}
	return null;
    }
    */
}