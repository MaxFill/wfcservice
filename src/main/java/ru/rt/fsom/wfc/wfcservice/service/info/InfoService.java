package ru.rt.fsom.wfc.wfcservice.service.info;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import ru.rt.fsom.wfc.wfcservice.dict.SysParams;

/**
 * REST API FSOM WFC
 * @author Maksim.Filatov
 */
@Path("/info")
@Stateless
public class InfoService {
    protected static final Logger LOGGER = Logger.getLogger(SysParams.LOGGER_NAME);

    @EJB private InfoFacade facade;
    
    public InfoService() {
    }
    
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    @Path("/dbstatus")
    public Response getDbStatus(){
	if (facade.checkDateBase()){
	    return Response.ok().build();
	}
	return Response.serverError().build();
    }
}
