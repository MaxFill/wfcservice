package ru.rt.fsom.wfc.wfcservice.service.ticket.group;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import javax.ejb.Stateless;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import ru.rt.fsom.wfc.wfcservice.data.tickets.TicketGroup;
import ru.rt.fsom.wfc.wfcservice.service.BaseFacade;

@Stateless
public class GroupFacade extends BaseFacade<TicketGroup>{         

    public GroupFacade() {
	super(TicketGroup.class);
    }       
    
    public TicketGroup createGroup(int id, String name){
	TicketGroup group = new TicketGroup(id, name);
	create(group);
	return group;
    }  
    
    /**
     * Поиск группы по его имени в поле name
     * @param name
     * @return 
     */
    public TicketGroup findGroupByName(String name){
        em.getEntityManagerFactory().getCache().evict(itemClass);
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<TicketGroup> cq = builder.createQuery(itemClass);
        Root<TicketGroup> root = cq.from(itemClass);
        Predicate crit1 = builder.equal(root.get("name"), name);
        cq.select(root).where(builder.and(crit1));        
        Query q = em.createQuery(cq);
        List<TicketGroup> results = q.getResultList();
	if (results.isEmpty()) return null;
	return results.get(0);
    }
    
    public TicketGroup findGroupById(Integer id){
	return findById(id);
    }     
    
    public List<TicketGroup> findAllGroups(){	
	return findAll();
    } 
 
    /**
     * Отбор списка id тикетов по id группы
     * @param groupId
     * @return 
     */
    public List<Integer> findTicketsIds(int groupId){
	final String sql = "SELECT ticket_id  FROM ilink.wfc_ticket_group_of_ticket WHERE ticket_group_id = ?";
	//LOGGER.log(Level.INFO, "SQL = {0}", new Object[]{sql});  
	List<Integer> results = new ArrayList<>();
	try(Connection conn = conf.getJdbcConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {	    
	    ps.setInt(1, groupId);
	    ResultSet resultSet = ps.executeQuery();
	    while (resultSet.next()){
		results.add(resultSet.getInt(1));
	    }
	} catch (SQLException ex) {
	    LOGGER.log(Level.SEVERE, "SQL State {0} error: {1}", new Object[]{ex.getSQLState(), ex.getMessage()});          
	}
	return results;
    }
    
    /**
     * Поиск групп по id тикета
     * @param ticketId
     * @return 
     */
    public List<TicketGroup> findGroupByTicketId(int ticketId){	
	final String sql = "SELECT GR.id, GR.ticket_group_name \n" +
		"FROM ilink.wfc_ticket_group_of_ticket AS MN \n" +
		"INNER JOIN ilink.wfc_ticket_group AS GR ON MN.ticket_group_id = GR.id \n" +
		"WHERE MN.ticket_id = ?";
	//LOGGER.log(Level.INFO, "SQL = {0}", new Object[]{sql});  
	List<TicketGroup> results = new ArrayList<>();
	try(Connection conn = conf.getJdbcConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {	    
	    ps.setInt(1, ticketId);
	    ResultSet resultSet = ps.executeQuery();
	    while (resultSet.next()){
		TicketGroup group = new TicketGroup();
		group.setId(resultSet.getInt(1));		
		group.setName(resultSet.getString(2));
		results.add(group);
	    }
	} catch (SQLException ex) {
	    LOGGER.log(Level.SEVERE, "SQL State {0} error: {1}", new Object[]{ex.getSQLState(), ex.getMessage()});          
	}
	return results;
    } 
}