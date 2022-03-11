package ru.rt.fsom.wfc.wfcservice.service.ticket.param;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import ru.rt.fsom.wfc.wfcservice.data.tickets.TicketParam;
import ru.rt.fsom.wfc.wfcservice.service.BaseFacade;

@Stateless
public class ParamFacade extends BaseFacade<TicketParam>{        

    public ParamFacade() {
	super(TicketParam.class);
    }
    
    public TicketParam findTicketParamByName(int ticketId, String paramName){
	CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<TicketParam> cq = builder.createQuery(itemClass);
        Root<TicketParam> root = cq.from(itemClass);
        Predicate crit1 = builder.equal(root.get("ticketId"), ticketId);
	Predicate crit2 = builder.equal(root.get("name"), paramName);
	cq.select(root).where(builder.and(crit1, crit2));        
        List<TicketParam> results = em.createQuery(cq).getResultList();	
	if (results.isEmpty()) return null;
	return results.get(0);
    }    
         
    public List<TicketParam> findTicketParams(Integer ticketId){	
	CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<TicketParam> cq = builder.createQuery(itemClass);
        Root<TicketParam> root = cq.from(itemClass);
        Predicate crit1 = builder.equal(root.get("ticketId"), ticketId);
	cq.select(root).where(builder.and(crit1));        
        return em.createQuery(cq).getResultList();
    }    
}