package ru.rt.fsom.wfc.wfcservice.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import ru.rt.fsom.wfc.wfcservice.config.Conf;
import ru.rt.fsom.wfc.wfcservice.dict.SysParams;

public abstract class BaseFacade<T extends BaseData> {
    protected static final Logger LOGGER = Logger.getLogger(SysParams.LOGGER_NAME);
    protected final Class<T> itemClass;
    
    @EJB protected Conf conf; 
    
    @PersistenceContext(unitName = "FSOM_PU")
    protected EntityManager em;
    
    public BaseFacade(Class<T> itemClass) {
        this.itemClass = itemClass;
    }
	
    protected Gson getGson(){
	return new GsonBuilder().registerTypeAdapter(Date.class, new JsonDeserializer<Date>() { 
	    @Override
	    public Date deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext context) throws JsonParseException {
		  return new Date(jsonElement.getAsJsonPrimitive().getAsLong()); 
	    } 
	}).create();
    } 
     
    synchronized protected void create(T entity) {
        em.persist(entity);
    }

    synchronized protected void edit(T entity) {
        em.merge(entity);
    }

    synchronized public void remove(T entity){
        em.remove(entity);
    }
     
    /**
     * Отбор всех записей
     * @return 
     */
    protected List<T> findAll(){
        em.getEntityManagerFactory().getCache().evict(itemClass);
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<T> cq = builder.createQuery(itemClass);
        Root<T> c = cq.from(itemClass);        
        cq.select(c).orderBy(orderBuilder(builder, c));
        Query q = em.createQuery(cq);
        return q.getResultList();
    }
    
    /**
     * Поиск объекта по идентификатору
     * @param id
     * @return 
     */
    public T findById(int id){
        em.getEntityManagerFactory().getCache().evict(itemClass);
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<T> cq = builder.createQuery(itemClass);
        Root<T> root = cq.from(itemClass);
        Predicate crit1 = builder.equal(root.get("id"), id);
        cq.select(root).where(builder.and(crit1));        
        List<T> results = em.createQuery(cq).getResultList();
	if (results.isEmpty()) return null;
	return results.get(0);
    }    
    
    /**
     * Определяет дефолтный порядок сортировки данных в запросах
     * @param builder
     * @param root
     * @return
     */
    protected List<Order> orderBuilder(CriteriaBuilder builder, Root root){
        List<Order> orderList = new ArrayList<>();
        orderList.add(builder.asc(root.get("name")));
        return orderList;
    }
}