package ru.rt.fsom.wfc.wfcservice.service.RMIFifo;

import com.comptel.mds.sas5.taskengine.rmi_fifo.RMIClientCallbackHandler;
import java.rmi.RemoteException;

public class RMICallbackHandler implements RMIClientCallbackHandler{

    @Override
    public boolean disconnect() throws RemoteException {	
	    return true;
    }
    
}