package ru.rt.fsom.wfc.wfcservice.service.ticket;

import com.comptel.mds.sas5.taskengine.rmi_fifo.RMIFifoClient;
import com.comptel.mds.sas5.taskengine.rmi_fifo.exceptions.ClientAlreadyRegisteredException;
import com.comptel.mds.sas5.taskengine.rmi_fifo.exceptions.ClientNotAuthorisedException;
import com.comptel.mds.sas5.taskengine.rmi_fifo.exceptions.InvalidModeException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.logging.Level;
import java.util.stream.Collectors;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.apache.commons.lang3.StringUtils;
import ru.rt.fsom.wfc.wfcservice.data.tickets.TicketData;
import ru.rt.fsom.wfc.wfcservice.data.tickets.TicketFilter;
import ru.rt.fsom.wfc.wfcservice.data.tickets.TicketParam;
import ru.rt.fsom.wfc.wfcservice.data.tickets.TicketStatus;
import ru.rt.fsom.wfc.wfcservice.service.BaseFacade;
import ru.rt.fsom.wfc.wfcservice.service.RMIFifo.RMIFIFOService;
import ru.rt.fsom.wfc.wfcservice.service.ticket.group.GroupFacade;
import ru.rt.fsom.wfc.wfcservice.service.ticket.param.ParamFacade;

@Stateless
public class TicketFacade extends BaseFacade<TicketData>{
      
    @EJB private RMIFIFOService fifoService;
    @EJB private ParamFacade paramFacade; 
    @EJB private GroupFacade groupFacade;
    
    public TicketFacade() {
	super(TicketData.class);
    }        
    
    public void saveTicket(TicketData ticket){
	//ToDo!
    }       
    
    public TicketData ticketFindById(Integer id){	
	return findById(id);
    }
    
    @Override
    public TicketData findById(int id){
        //em.getEntityManagerFactory().getCache().evict(itemClass);
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<TicketData> cq = builder.createQuery(itemClass);
        Root<TicketData> root = cq.from(itemClass);
        Predicate crit1 = builder.equal(root.get("ticketId"), id);
	cq.select(root).where(builder.and(crit1));        
        List<TicketData> results = em.createQuery(cq).getResultList();
	if (results.isEmpty()) return null;
	return results.get(0);
    }        
	
    public List<TicketData> findByFilter(TicketFilter filter){
	CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<TicketData> cq = builder.createQuery(itemClass);
        Root<TicketData> root = cq.from(itemClass);
	Predicate[] predicates = makePredicates(filter, root, builder);
	if (predicates.length == 0) return new ArrayList<>();
	cq.select(root).where(builder.and(predicates)); 	
        //LOGGER.log(Level.INFO, "findByFilter set maxResult ={0}", filter.getPositionEnd());
	return em.createQuery(cq)
	    .setFirstResult(filter.getPositionStart())
	    .setMaxResults(filter.getPositionEnd())
	    .getResultList();
    }
    
    public String changeTicketStatus(int ticketId, int statusId, String userName){
	String errMsg = null;
	TicketData ticket = ticketFindById(ticketId);
	if (ticket == null) return "Ticket by id=" + ticketId + " not found!";
	TicketStatus ticketStatus = TicketStatus.STATUSES.stream()
	    .filter(s->Objects.equals(statusId, s.getStatusId()))
	    .findFirst()
	    .orElse(null);
	if (ticketStatus == null) return "Ticket Status id=" + statusId + " not found!";
	try {
	    RMIFifoClient fifoClient = conf.getRMIFifoClient();
	    Map<String, String> params = paramFacade.findTicketParams(ticketId).stream().collect(Collectors.toMap(TicketParam::getName, TicketParam::getValue));
	    errMsg = fifoService.sendToIL(ticket, ticketStatus, params, fifoClient);
	    if (errMsg == null){
		ticket.setStatus(statusId);
		ticket.setModifyingUser(userName);
		ticket.setDateProcessed(new Date());
		edit(ticket);
	    }
	} catch (RemoteException | ClientAlreadyRegisteredException | InvalidModeException | ClientNotAuthorisedException ex) {
	    LOGGER.log(Level.SEVERE, null, ex);
	}
	return errMsg;
    }

    /* *** privates *** */
	
    private Predicate[] makePredicates(TicketFilter filter, Root root, CriteriaBuilder cb){
	List<Predicate> crits = new ArrayList<>();
	addIntCrit(root.get("ticketId"), filter.getTicketId(), cb).ifPresent(v->crits.add(v));
	//addIntCrit(root.get("ticketType"), filter.getTicketType(), cb).ifPresent(v->crits.add(v));
	addIntCrit(root.get("requestId"), filter.getRequestId(), cb).ifPresent(v->crits.add(v));
	//addStrCrit(root.get("taskId"), filter.getTicket().getTaskId(), cb).ifPresent(v->crits.add(v));
	addStrCrit(root.get("userName"), filter.getUserName(), cb).ifPresent(v->crits.add(v));
	addLikeCrit(root.<String>get("ticketInfo"), filter.getTicketInfo(), cb).ifPresent(v->crits.add(v));
	if (filter.getStatuses() != null){
	    List<Integer> statusIds = filter.getStatuses().stream().map(st->st.getStatusId()).collect(Collectors.toList());
	    addInListCrit(root.get("status"), statusIds).ifPresent(v->crits.add(v));
	}
	if (filter.getGroup() != null){
	    //LOGGER.log(Level.INFO, "filter.getGroup() id ={0}", filter.getGroup().getId());
	    List<Integer> ticketIds = groupFacade.findTicketsIds(filter.getGroup().getId());
	    addInListCrit(root.get("ticketId"), ticketIds).ifPresent(v->crits.add(v));	
	}
	addDateLessCrit(root.get("dateIssue"), filter.getDateIssueTo(), cb).ifPresent(v->crits.add(v));
	addDateGreaterCrit(root.get("dateIssue"), filter.getDateIssueFrom(), cb).ifPresent(v->crits.add(v));
	addDateLessCrit(root.get("dateJeopardy"), filter.getDateJeopardyTo(), cb).ifPresent(v->crits.add(v));
	addDateGreaterCrit(root.get("dateJeopardy"), filter.getDateJeopardyFrom(), cb).ifPresent(v->crits.add(v));
	Predicate[] predicates = new Predicate[crits.size()];
	return crits.toArray(predicates);
    }
 
    private Optional<Predicate> addInListCrit(Path field, List<Integer> values){
	if (values == null || values.isEmpty()) return Optional.empty();	
	//LOGGER.log(Level.INFO, "addInListCrit={0}", values.size());
	return Optional.of(field.in(values));
    }
    
    private Optional<Predicate> addDateGreaterCrit(Path field, Date value, CriteriaBuilder builder){
	if (value == null) return Optional.empty();
	//LOGGER.log(Level.INFO, "addDateGreaterCrit={0}", value);
	return Optional.of(builder.greaterThanOrEqualTo(field, value));
    }
    
    private Optional<Predicate> addDateLessCrit(Path field, Date value, CriteriaBuilder builder){
	if (value == null) return Optional.empty();
	//LOGGER.log(Level.INFO, "addDateLessCrit={0}", value);
	return Optional.of(builder.lessThanOrEqualTo(field, value));
    }
	
    private Optional<Predicate> addLikeCrit(Path field, String value, CriteriaBuilder builder){
	if (StringUtils.isEmpty(value)) return Optional.empty();
	//LOGGER.log(Level.INFO, "addLikeCrit={0}", field.toString());
	return Optional.of(builder.like(field, "%" + value + "%"));
    }
	
    private Optional<Predicate> addStrCrit(Path field, String value, CriteriaBuilder builder){
	if (StringUtils.isEmpty(value)) return Optional.empty();
	//LOGGER.log(Level.INFO, "addStrCrit={0}", field.toString());
	return Optional.of(builder.equal(field, value));
    }
    
    private Optional<Predicate> addIntCrit(Path field, Integer value, CriteriaBuilder builder){
	if (value == null) return Optional.empty();
	//LOGGER.log(Level.INFO, "addIntCrit={0}", field.toString());
	return Optional.of(builder.equal(field, value));
    }        
    
}