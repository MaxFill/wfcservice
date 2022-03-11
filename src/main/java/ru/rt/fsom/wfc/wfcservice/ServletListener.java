package ru.rt.fsom.wfc.wfcservice;

import javax.ejb.EJB;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import ru.rt.fsom.wfc.wfcservice.config.Conf;

public class ServletListener implements ServletContextListener {
@EJB private Conf conf;

@Override
public void contextInitialized(ServletContextEvent sce) {
    //Params.LOGGER.log(Level.INFO, "ServletContextEvent contextInitialized!");
}

@Override
public void contextDestroyed(ServletContextEvent sce) {
        conf.rmiFifoClientUnregistr();
    }
}