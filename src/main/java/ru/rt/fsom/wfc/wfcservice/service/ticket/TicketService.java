package ru.rt.fsom.wfc.wfcservice.service.ticket;

import com.google.gson.Gson;
import java.util.Date;
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
import org.apache.commons.lang3.StringUtils;
import ru.rt.fsom.wfc.wfcservice.data.tickets.TicketData;
import ru.rt.fsom.wfc.wfcservice.data.tickets.TicketFilter;
import ru.rt.fsom.wfc.wfcservice.data.tickets.TicketParam;
import ru.rt.fsom.wfc.wfcservice.dict.SysParams;
import ru.rt.fsom.wfc.wfcservice.service.security.SecurityFacade;
import ru.rt.fsom.wfc.wfcservice.service.ticket.param.ParamFacade;

/**
 * REST API FSOM WFC
 * @author Maksim.Filatov
 */
@Path("/ticket")
@Stateless
public class TicketService {
    protected static final Logger LOGGER = Logger.getLogger(SysParams.LOGGER_NAME);
    
    @EJB private TicketFacade ticketFacade;      
    @EJB private ParamFacade paramFacade; 
    @EJB private SecurityFacade securityFacade;
    
    public TicketService() {
    }
        
    /**
     * Выполняет поиск тикета по идентификатору
     * @param ticketId 
     * @param token 
     * @return тикет
     */
    @Produces(MediaType.APPLICATION_JSON)
    @GET    
    public Response getTicketById(@QueryParam("id") String ticketId, @QueryParam("token") String token){	
	//LOGGER.log(Level.INFO, "start get ticket by id = {0}", ticketId);
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
	TicketData results = ticketFacade.ticketFindById(id);	
	if (results == null){
	    return Response.status(Response.Status.NOT_FOUND).build();
	}
	//Date dateStart = new Date();
	//Date dateEnd = new Date();
	//long diff = dateEnd.getTime() - dateStart.getTime();
	//LOGGER.log(Level.INFO, "finish get ticket by id = {0}, duration={1}ms", new Object[]{ticketId, diff});
	return Response.ok(results).build();
    }
    
    /**
     * Выполняет поиск параметра тикета по идентификатору тикета и названию параметра
     * @param id
     * @param paramName
     * @param token
     * @return парамтер тикета
     */
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    @Path("/param")
    public Response getTicketParamByName(@QueryParam("id") String id, @QueryParam("name") String paramName, @QueryParam("token") String token){
	if (!securityFacade.checkJWT(token)) return Response.status(Response.Status.UNAUTHORIZED).build();
	Integer ticketId = null;
	try {
	    ticketId = Integer.valueOf(id);
	} catch (NumberFormatException ex) {
	    LOGGER.log(Level.SEVERE, "NumberFormatException: {0}", ex.getMessage());
	}
	if (StringUtils.isBlank(paramName)){
	    return Response.status(Response.Status.BAD_REQUEST).build();
	}
	TicketParam results = paramFacade.findTicketParamByName(ticketId, paramName);
	if (results == null){
	    return Response.status(Response.Status.NOT_FOUND).build();
	}
	return Response.ok(results).build();
    }
    
    /**
     * Выполняет отбор всех параметров тикета по идентификатору тикета
     * @param id
     * @param token
     * @return список параметров тикета
     */
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    @Path("/params")
    public Response getTicketParams(@QueryParam("id") String id, @QueryParam("token") String token){
	if (!securityFacade.checkJWT(token)) return Response.status(Response.Status.UNAUTHORIZED).build();
	Integer ticketId = null;
	try {
	    ticketId = Integer.valueOf(id);
	} catch (NumberFormatException ex) {
	    LOGGER.log(Level.SEVERE, "NumberFormatException: {0}", ex.getMessage());
	}
	List<TicketParam> results = paramFacade.findTicketParams(ticketId);
	if (results ==  null){
	    return Response.status(Response.Status.NOT_FOUND).build();
	}
	return Response.ok(results).build();
    }
    
    /**
     * Изменение статуса тикета на значение из параметра status
     * с передачей информации в IL посредством вызова RMI FIFO для изменения статуса задачи
     * @param id
     * @param statusId
     * @param userName
     * @return текст ошибки или ничего если выполнилось успешно
     */
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    @Path("/status/change")
    public Response changeTicketStatus(@QueryParam("id") String id, @QueryParam("status") int statusId, @QueryParam("user") String userName, @QueryParam("token") String token){
	if (!securityFacade.checkJWT(token)) return Response.status(Response.Status.UNAUTHORIZED).build();
	Integer ticketId = null;
	try {
	    ticketId = Integer.valueOf(id);
	} catch (NumberFormatException ex) {
	    LOGGER.log(Level.SEVERE, "NumberFormatException: {0}", ex.getMessage());
	}
	String results = ticketFacade.changeTicketStatus(ticketId, statusId, userName);
	if (results != null){
	    return Response.accepted(results).status(Response.Status.INTERNAL_SERVER_ERROR).build();
	}
	return Response.ok(results).build();
    }
    
    /**
     * Выполняет поиск тикетов по критериям поиска, переданных в структуре TicketFilter
     * @param gsonStr
     * @param token
     * @return список тикетов
     */
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @PUT
    @Path("/find")
    public Response findTikets(String gsonStr, @QueryParam("token") String token){
	//LOGGER.log(Level.INFO, "PUT request - start find tickets...");	
	if (!securityFacade.checkJWT(token)) return Response.status(Response.Status.UNAUTHORIZED).build();
	Gson gson = new Gson();
	//LOGGER.log(Level.INFO, "load ticket filter from json={0}", gsonStr);
	TicketFilter filter = gson.fromJson(gsonStr, TicketFilter.class);
	if (filter == null){
	    return Response.status(Response.Status.BAD_REQUEST).build();
	}
	//LOGGER.log(Level.INFO, "start find tikets ...");
	List<TicketData> results = ticketFacade.findByFilter(filter);
	Date dateStart = new Date();
	Date dateEnd = new Date();
	long diff = dateEnd.getTime() - dateStart.getTime();
	LOGGER.log(Level.INFO, "finish find tikets result size={0}, duration={1}ms", new Object[]{results.size(), diff});
	/*
	if (results.isEmpty()){
	    return Response.status(Response.Status.NOT_FOUND).build();
	}
	*/
	return Response.ok(results).build();
    }
      
}