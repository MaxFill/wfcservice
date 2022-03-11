package ru.rt.fsom.wfc.wfcservice.service.users;

import java.util.List;
import java.util.logging.Level;
import javax.ejb.Stateless;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import ru.rt.fsom.wfc.wfcservice.service.BaseFacade;

@Stateless
public class UserFacade extends BaseFacade<User>{
    
    public UserFacade() {
	super(User.class);
    }
    
    public User checkLogin(String userName, String pwd){
	//LOGGER.log(Level.INFO, "user checkLogin ...");
	User user = findByName(userName);	
	if (user == null){
	    LOGGER.log(Level.INFO, "user for name [{0}] not found!", userName);
	    return null;
	}
	//Todo need check password!
	/*
	byte[] bytePwd = Cryptography.dehexify(user.getPassword().getBytes());
        try {
            byte[] bytePwd2 = Cryptography.decrypt(bytePwd, CryptAlgorithms.DESEDE, CryptAlgorithms.getKey(CryptAlgorithms.DESEDE));
            String str = new String(bytePwd2);
            ...
        } catch (CryptException e) {
            e.printStackTrace();
        }
	*/
	return user;
    }
    
    public User findByName(String userName){
        em.getEntityManagerFactory().getCache().evict(itemClass);
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<User> cq = builder.createQuery(itemClass);
        Root<User> root = cq.from(itemClass);
        Predicate crit1 = builder.equal(root.get("userName"), userName);
        cq.select(root).where(builder.and(crit1));
        List<User> results = em.createQuery(cq).getResultList();
	if (results.isEmpty()) return null;
	return results.get(0);
    }
 
}
