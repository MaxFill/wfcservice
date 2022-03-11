package ru.rt.fsom.wfc.wfcservice.service.ticket.attached;

import com.google.gson.Gson;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.apache.commons.lang3.StringUtils;
import ru.rt.fsom.wfc.wfcservice.data.tickets.TicketAttaches;
import ru.rt.fsom.wfc.wfcservice.dict.SysParams;
import ru.rt.fsom.wfc.wfcservice.service.security.SecurityFacade;

/**
 * REST API FSOM WFC
 * @author Maksim.Filatov
 */
@Path("/attached")
@Stateless
public class AttachedService {
    protected static final Logger LOGGER = Logger.getLogger(SysParams.LOGGER_NAME);
    
    @EJB private AttachedFacade facade;      
    @EJB private SecurityFacade securityFacade;
	
    public AttachedService() {
    }
      
    /**
     * Выполняет поиск вложения по идентификатору вложения
     * @param attacheId
     * @param token
     * @return Attached
     */
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    @Path("/id")
    public Response getAttachedById(@QueryParam("id") String attacheId, @QueryParam("token") String token){
	//LOGGER.log(Level.INFO, "getAttachedById = {0}", attacheId);
	if (!securityFacade.checkJWT(token)) return Response.status(Response.Status.UNAUTHORIZED).build();
	Integer id = null;
	try {
	    id = Integer.valueOf(attacheId);
	} catch (NumberFormatException ex) {
	    LOGGER.log(Level.SEVERE, "NumberFormatException: {0}", ex.getMessage());
	}
	if (id == null){
	    return Response.status(Response.Status.BAD_REQUEST).build();
	}
	TicketAttaches result = facade.findAttachedById(id);
	if (result == null){
	    return Response.status(Response.Status.NOT_FOUND).build();
	}
	return Response.ok(result).build();
    }
    
    /**
     * Выполняет отбор вложений по id тикета
     * @param ticketId
     * @param token
     * @return List TicketAttaches
     */
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    @Path("/ticket")
    public Response getAttachedByTicketId(@QueryParam("ticketId") String ticketId, @QueryParam("token") String token){
	//LOGGER.log(Level.INFO, "getAttachedByTicketId ticketId = {0}", ticketId);
	if (!securityFacade.checkJWT(token)) return Response.status(Response.Status.UNAUTHORIZED).build();
	if (StringUtils.isBlank(ticketId)){
	    return Response.status(Response.Status.BAD_REQUEST).build();
	}	
	List<TicketAttaches> result = facade.findAttachedByTicketId(ticketId);
	if (result == null){
	    return Response.status(Response.Status.NOT_FOUND).build();
	}
	return Response.ok(result).build();
    }        
    
    /**
     * Выполняет создание нового вложения
     * @param gsonStr
     * @param token
     * @return 
     */
    @Produces(MediaType.APPLICATION_JSON)
    @PUT
    @Path("/add")
    public Response addAttached(String gsonStr, @QueryParam("token") String token){
	LOGGER.log(Level.INFO, "addAttached from json = {0}", gsonStr);	
	if (!securityFacade.checkJWT(token)) return Response.status(Response.Status.UNAUTHORIZED).build();
	Gson gson = new Gson();
	TicketAttaches attached = gson.fromJson(gsonStr, TicketAttaches.class);
	TicketAttaches result = facade.createAttaches(attached);
	if (result == null){
	    return Response.status(Response.Status.NOT_FOUND).build();
	}
	LOGGER.log(Level.INFO, "sucessfully created! id = {0}", result.getId());
	return Response.ok(result).build();
    }
 
    /**
     * Выполняет удаление вложения по id вложения
     * @param attacheId
     * @param token
     * @return 
     */
    @Produces(MediaType.APPLICATION_JSON)    
    @DELETE
    @Path("delete")
    public Response deleteAttachedById(@QueryParam("id") String attacheId, @QueryParam("token") String token){
	if (!securityFacade.checkJWT(token)) return Response.status(Response.Status.UNAUTHORIZED).build();
	Integer id = null;
	try {
	    id = Integer.valueOf(attacheId);
	} catch (NumberFormatException ex) {
	    LOGGER.log(Level.SEVERE, "NumberFormatException: {0}", ex.getMessage());
	}
	if (id == null){
	    return Response.status(Response.Status.BAD_REQUEST).build();
	}
	TicketAttaches forDelete = facade.findAttachedById(id);
	if (forDelete == null){
	    return Response.status(Response.Status.NOT_FOUND).build();
	}
	facade.remove(forDelete);
	return Response.ok().build();
    }
}