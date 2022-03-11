package ru.rt.fsom.wfc.wfcservice.service.ticket.attached;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import ru.rt.fsom.wfc.wfcservice.data.tickets.TicketAttaches;
import ru.rt.fsom.wfc.wfcservice.service.BaseFacade;

@Stateless
public class AttachedFacade extends BaseFacade<TicketAttaches>{         

    public AttachedFacade() {
	super(TicketAttaches.class);
    }       
    
    public TicketAttaches createAttaches(TicketAttaches source){
	TicketAttaches attached = new TicketAttaches();
	attached.setAuthor(source.getAuthor());
	attached.setName(source.getName());
	attached.setDateCreate(source.getDateCreate());
	attached.setExtension(source.getExtension());
	attached.setFileSize(source.getFileSize());
	attached.setGuid(source.getGuid());
	attached.setTicketId(source.getTicketId());
	attached.setType(source.getType());
	create(attached);
	return attached;
    }  
    
    public List<TicketAttaches> findAttachedByTicketId(String ticketId){
        em.getEntityManagerFactory().getCache().evict(itemClass);
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<TicketAttaches> cq = builder.createQuery(itemClass);
        Root<TicketAttaches> root = cq.from(itemClass);
        Predicate crit1 = builder.equal(root.get("ticketId"), ticketId);
        cq.select(root).where(builder.and(crit1));        
        Query q = em.createQuery(cq);
        return q.getResultList();
    }
    
    public TicketAttaches findAttachedById(int id){
	return findById(id);
    }         
 
    public void deleteAttachedById(int id){
	TicketAttaches attached = em.getReference(itemClass, id);
	remove(attached);
    }
    
}