package ru.rt.fsom.wfc.wfcservice.service.RMIFifo;

import com.comptel.mds.sas5.taskengine.rmi_fifo.RMIFifoClient;
import com.comptel.mds.sas5.taskengine.rmi_fifo.RMIResponseObject;
import com.comptel.mds.sas5.taskengine.rmi_fifo.exceptions.ClientNotRegisteredException;
import com.comptel.mds.sas5.taskengine.rmi_fifo.exceptions.InvalidModeException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import org.apache.commons.lang3.StringUtils;
import ru.rt.fsom.wfc.wfcservice.config.Conf;
import ru.rt.fsom.wfc.wfcservice.data.tickets.TicketData;
import ru.rt.fsom.wfc.wfcservice.data.tickets.TicketStatus;
import ru.rt.fsom.wfc.wfcservice.dict.SysParams;

/**
 * Клиент RMI FIFO InstantLink
 * @author Maksim.Filatov
 */
@Stateless
public class RMIFIFOService { 
    private static final Logger LOGGER = Logger.getLogger(SysParams.LOGGER_NAME);
     
    @EJB protected Conf conf; 
     
    private final static String RMIFIFO_ERR = "RMI_FIFO Error:";
    private final static String REMOTE_EXCEPTION = RMIFIFO_ERR + "RemoteException: there is a communication problem. Most probably something wrong with the physical connection OR TE has been shutdown";
    private final static String ILLEGAL_ARGUMENT_EXCEPTION = RMIFIFO_ERR + "IllegalArgumentException: the response contains some invalid data...";    
    private final static String CLIENT_NOTREGISTERED_EXCEPTION = RMIFIFO_ERR + "ClientNotRegisteredException: TE does not know us ' must re-register";
    private final static String INVALID_MODE_EXCEPTION = RMIFIFO_ERR + "InvalidModeException: TE is going down... do something with the response at";
    private final static String NE_TASK_TYPE = "create";
    private final static String NE_TYPE = "WFC";      
       
    synchronized public String sendToIL(TicketData ticket, TicketStatus status, Map<String, String> params, RMIFifoClient fifoClient) {
	String msg = null;
	try{
	    RMIResponseObject response = makeRMIResponse(ticket, status, params);
	    final String taskId = response.requestId + "_" + response.taskId;
	    final String ticketId = String.valueOf(ticket.getTicketId());
	    LOGGER.log(Level.INFO, "Старт отправки данных в IL. ticketId={0}, taskId={1}", new Object[]{ticketId, taskId});
	    fifoClient.sendResponse(response); //отправка в IL
	    LOGGER.log(Level.INFO, "Данные успешно отправлены в IL. ticketId={0}, taskId={1}", new Object[]{ticketId, taskId});	
	} catch(RemoteException ex){
	    LOGGER.log(Level.SEVERE, REMOTE_EXCEPTION, "");
	    msg = REMOTE_EXCEPTION;
	} catch(ClientNotRegisteredException ex){
	    LOGGER.log(Level.SEVERE, CLIENT_NOTREGISTERED_EXCEPTION, "");
	    msg = CLIENT_NOTREGISTERED_EXCEPTION;
	} catch(InvalidModeException ime){
	    LOGGER.log(Level.SEVERE, INVALID_MODE_EXCEPTION, ime.getMessage());	    
	    msg = INVALID_MODE_EXCEPTION;
	} catch(IllegalArgumentException ex){
	    LOGGER.log(Level.SEVERE, ILLEGAL_ARGUMENT_EXCEPTION, ex.getMessage());
	    msg = ILLEGAL_ARGUMENT_EXCEPTION;
	} catch(EJBException ex){
	    LOGGER.log(Level.SEVERE, ex.getMessage());
	    msg = "Internal server error:" + ex.getMessage();
	}
	return msg;
    }
    
    /* *** privates *** */ 
    
    private RMIResponseObject makeRMIResponse(TicketData ticket, TicketStatus status, Map<String, String> params){		
	final String smessageId = status.getResult().getSmessageId();
	final String smessage = status.getResult().getSmessage();
	final int taskStatus = status.getResult().getTaskStatus();
	final int reqId = Integer.valueOf(ticket.getRequestId());
	final int taskId = Integer.valueOf(ticket.getTaskId());
	Map<String, String> newParams = new HashMap<>();	
	params.entrySet().stream()
	    .filter(entry->StringUtils.isNoneBlank(entry.getValue())) //в IL нельзя отправлять пустые значения параметры!!! 
	    .forEach(entry->newParams.put(entry.getKey(), entry.getValue()));
	List phaseMessages = new ArrayList();
	LOGGER.log(Level.INFO, "RMIResponseObject: reqId={0}, taskId={1}, neType={2}, taskType={3}, smessageId={4}, smessage={5}", new Object[]{reqId, taskId, NE_TYPE, NE_TASK_TYPE, smessageId, smessage});
	return new RMIResponseObject(reqId, taskId, NE_TYPE, NE_TASK_TYPE, taskStatus, newParams, new HashMap<>(), phaseMessages, smessageId, smessage);
    }
}