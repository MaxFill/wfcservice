package ru.rt.fsom.wfc.wfcservice.service.users;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.ejb.Stateless;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.apache.commons.lang3.StringUtils;
import ru.rt.fsom.wfc.wfcservice.data.tickets.TicketFilter;
import ru.rt.fsom.wfc.wfcservice.service.BaseFacade;

@Stateless
public class UserSettingsFacade extends BaseFacade<UserSettings>{

    public UserSettingsFacade() {
	super(UserSettings.class);
    }
 
    public TicketFilter saveFilter(TicketFilter filter){
	UserSettings settings = findByUserId(filter.getUserId());	
	Gson gson = new Gson();		
	if (settings == null){
	    settings = new UserSettings(filter.getUserId());
	    List<TicketFilter> filters = new ArrayList<>();
	    filters.add(filter);
	    settings.setFilters(gson.toJson(filters, new TypeToken<ArrayList<TicketFilter>>(){}.getType()));
	    create(settings);
	    return filter;
	} 
	String gsonFilters = settings.getFilters();
	Set<TicketFilter> filters = new HashSet<>();
	if (StringUtils.isNoneBlank(gsonFilters)){
	    filters = gson.fromJson(gsonFilters, new TypeToken<HashSet<TicketFilter>>(){}.getType());	    	    
	}
	//LOGGER.log(Level.INFO, "save filter statuses size={0}", filter.getStatuses().size());
	filters.remove(filter);
	filters.add(filter);
	settings.setFilters(gson.toJson(filters, new TypeToken<HashSet<TicketFilter>>(){}.getType()));
	//LOGGER.log(Level.INFO, "start save filters..");
	edit(settings);	
	//LOGGER.log(Level.INFO, "filters saved!");
	return filter;
    }
    
    public TicketFilter deleteFilter(TicketFilter filter){
	UserSettings settings = findByUserId(filter.getUserId());
	if (settings == null) return null;
	String gsonFilters = settings.getFilters();
	Set<TicketFilter> filters = new HashSet<>();
	Gson gson = new Gson();
	if (StringUtils.isNoneBlank(gsonFilters)){
	    filters = gson.fromJson(gsonFilters, new TypeToken<HashSet<TicketFilter>>(){}.getType());
	}
	filters.remove(filter);
	settings.setFilters(gson.toJson(filters, new TypeToken<HashSet<TicketFilter>>(){}.getType()));
	edit(settings);	
	return filter;
    }
    
    public List<TicketFilter> loadFiltersByUserId(int userId){
	UserSettings settings = findByUserId(userId);
	//LOGGER.log(Level.INFO, "settings for userid={0} is find!", userId);
	List<TicketFilter> filters = null;
	if (settings != null){
	    String gsonFilters = settings.getFilters();	    
	    if (StringUtils.isNoneBlank(gsonFilters)){
		Gson gson = new Gson();
		filters = gson.fromJson(gsonFilters, new TypeToken<ArrayList<TicketFilter>>(){}.getType());	
	    }
	}
	if (filters == null){
	    filters = new ArrayList<>();
	}
	return filters;
    }
    
    public UserSettings findByUserId(int userId){
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<UserSettings> cq = builder.createQuery(itemClass);
        Root<UserSettings> root = cq.from(itemClass);
        Predicate crit1 = builder.equal(root.get("userId"), userId);
	cq.select(root).where(builder.and(crit1));        
        List<UserSettings> results = em.createQuery(cq).getResultList();
	if (results.isEmpty()) return null;
	return results.get(0);
    }  
}
