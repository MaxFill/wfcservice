/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.rt.fsom.wfc.wfcservice.service.info;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import ru.rt.fsom.wfc.wfcservice.config.Conf;
import ru.rt.fsom.wfc.wfcservice.dict.SysParams;

/**
 *
 * @author Maksim.Filatov
 */
@Stateless
public class InfoFacade {
    protected static final Logger LOGGER = Logger.getLogger(SysParams.LOGGER_NAME);
    
    @EJB protected Conf conf; 
    
    public boolean checkDateBase(){
	final String sql = "SELECT id FROM ilink.caas_role WHERE role_name = 'admin'";
	//LOGGER.log(Level.INFO, "SQL = {0}", new Object[]{sql});  
	boolean result = false;
	try(Connection conn = conf.getJdbcConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {	    
	    ResultSet resultSet = ps.executeQuery();
	    if (resultSet.next()){
		result = true;
	    }
	} catch (SQLException ex) {
	    LOGGER.log(Level.SEVERE, "SQL State {0} error: {1}", new Object[]{ex.getSQLState(), ex.getMessage()});          
	}
	return result;
    }
}