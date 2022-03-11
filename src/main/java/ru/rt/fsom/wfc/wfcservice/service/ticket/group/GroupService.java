package ru.rt.fsom.wfc.wfcservice.service.ticket.group;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.apache.commons.lang3.StringUtils;
import ru.rt.fsom.wfc.wfcservice.data.tickets.TicketGroup;
import ru.rt.fsom.wfc.wfcservice.dict.SysParams;
import ru.rt.fsom.wfc.wfcservice.service.security.SecurityFacade;

/**
 * REST API FSOM WFC
 * @author Maksim.Filatov
 */
@Path("/groups")
@Stateless
public class GroupService {
    protected static final Logger LOGGER = Logger.getLogger(SysParams.LOGGER_NAME);
    
    @EJB private GroupFacade facade;      
    @EJB private SecurityFacade securityFacade;
	
    public GroupService() {
    }
      
    /**
     * Выполняет поиск группы по идентификатору 
     * @param groupId
     * @param token
     * @return группа тикета
     */
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    @Path("/id")
    public Response getGroupById(@QueryParam("id") String groupId, @QueryParam("token") String token){
	//LOGGER.log(Level.INFO, "getGroupById ticketId = {0}", ticketId);
	if (!securityFacade.checkJWT(token)) return Response.status(Response.Status.UNAUTHORIZED).build();
	Integer id = null;
	try {
	    id = Integer.valueOf(groupId);
	} catch (NumberFormatException ex) {
	    LOGGER.log(Level.SEVERE, "NumberFormatException: {0}", ex.getMessage());
	}
	if (id == null){
	    return Response.status(Response.Status.BAD_REQUEST).build();
	}
	TicketGroup result = facade.findGroupById(id);
	if (result == null){
	    return Response.status(Response.Status.NOT_FOUND).build();
	}
	return Response.ok(result).build();
    }
    
    /**
     * Выполняет поиск групп по имени
     * @param groupName
     * @param token
     * @return группа тикета
     */
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    @Path("/name")
    public Response getGroupByName(@QueryParam("name") String groupName, @QueryParam("token") String token){
	//LOGGER.log(Level.INFO, "getGroupByName name = {0}", groupName);
	if (!securityFacade.checkJWT(token)) return Response.status(Response.Status.UNAUTHORIZED).build();
	if (StringUtils.isBlank(groupName)){
	    return Response.status(Response.Status.BAD_REQUEST).build();
	}	
	TicketGroup result = facade.findGroupByName(groupName);
	if (result == null){
	    return Response.status(Response.Status.NOT_FOUND).build();
	}
	return Response.ok(result).build();
    }
    
    /**
     * Выполняет отбор всех групп
     * @param token
     * @return список групп
     */
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    public Response getAllGroups(@QueryParam("token") String token){
	if (!securityFacade.checkJWT(token)) return Response.status(Response.Status.UNAUTHORIZED).build();
	List<TicketGroup> results = facade.findAllGroups();
	//LOGGER.log(Level.INFO, "getAllGroups size={0}", results.size());
	if (results.isEmpty()){
	    return Response.status(Response.Status.NOT_FOUND).build();
	}
	return Response.ok(results).build();
    }
    
    /**
     * Выполняет создание новой группы
     * @param id
     * @param name
     * @param token
     * @return 
     */
    @Produces(MediaType.APPLICATION_JSON)
    @PUT
    @Path("/add")
    public Response addGroup(@QueryParam("id") int id, @QueryParam("name") String name, @QueryParam("token") String token){
	if (!securityFacade.checkJWT(token)) return Response.status(Response.Status.UNAUTHORIZED).build();	
	//LOGGER.log(Level.INFO, "addGroup name = {0}", name);	
	TicketGroup result = facade.createGroup(id, name);
	if (result == null){
	    return Response.status(Response.Status.NOT_FOUND).build();
	}
	//LOGGER.log(Level.INFO, "sucessfully addGroup name = {0}", name);
	return Response.ok(result).build();
    }
 
    /**
     * Выполняет отбор групп, к котором относится данный тикет ticketId
     * @param ticketId
     * @param token
     * @return список групп
     */
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    @Path("/ticket")
    public Response getGroupByTicketId(@QueryParam("ticketId") String ticketId, @QueryParam("token") String token){	
	if (!securityFacade.checkJWT(token)) return Response.status(Response.Status.UNAUTHORIZED).build();
	Integer id = null;
	try {
	    id = Integer.valueOf(ticketId);
	} catch (NumberFormatException ex) {
	    LOGGER.log(Level.SEVERE, "NumberFormatException: {0}", ex.getMessage());
	}
	if (id == null){
	    return Response.status(Response.Status.BAD_REQUEST).build();
	}
	List<TicketGroup> results = facade.findGroupByTicketId(id);
	if (results.isEmpty()){
	    return Response.status(Response.Status.NOT_FOUND).build();
	}
	return Response.ok(results).build();
    } 
}