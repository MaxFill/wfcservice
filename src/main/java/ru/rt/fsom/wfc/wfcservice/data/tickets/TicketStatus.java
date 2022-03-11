package ru.rt.fsom.wfc.wfcservice.data.tickets;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class TicketStatus implements Serializable{
    private static final long serialVersionUID = 1L;
    
    private static final int WAITING_CODE = 0;
    private static final int RESERVED_CODE = 1;
    private static final int SENT_CODE_OK = 2;
    private static final int SENT_CODE_EXPIRED = 3;
    private static final int SENT_CODE_FAIL = 4;
    private static final int RESEND_CODE = 5; 
    private static final int CANCELLED_CODE = 6;
    private static final int TRIGGERABLE_CODE = 7;
    
    @JsonIgnore
    public static final TicketStatus STATUS_WAITING = new TicketStatus(TicketStatus.WAITING_CODE, "Waiting", "clock", TicketResult.WAIT_SMES_WCLI203);
    @JsonIgnore
    public static final TicketStatus STATUS_RESERVED = new TicketStatus(TicketStatus.RESERVED_CODE, "Reserved", "service", TicketResult.WAIT_SMES_WCLI203);
    @JsonIgnore
    public static final TicketStatus STATUS_SENT_EXP = new TicketStatus(TicketStatus.SENT_CODE_EXPIRED, "Sent [EXPIRED]", "done", TicketResult.EXP_SMES_WCLI201);
    @JsonIgnore
    public static final TicketStatus STATUS_SENT_OK = new TicketStatus(TicketStatus.SENT_CODE_OK, "Sent [OK]", "done", TicketResult.OK_SMES_WCLI200);
    @JsonIgnore
    public static final TicketStatus STATUS_SENT_FAIL = new TicketStatus(TicketStatus.SENT_CODE_FAIL, "Sent [FAIL]", "done", TicketResult.FAIL_SMES_WCLI901);
    @JsonIgnore
    public static final TicketStatus STATUS_CANCELLED = new TicketStatus(TicketStatus.CANCELLED_CODE, "Cancelled", "stop", TicketResult.FAIL_SMES_WCLI901);
    @JsonIgnore
    public static final TicketStatus STATUS_RESEND = new TicketStatus(TicketStatus.RESEND_CODE, "Resend", "stop", TicketResult.OK_SMES_WCLI200);
    @JsonIgnore
    public static final TicketStatus STATUS_TRIGGERABLE = new TicketStatus(TicketStatus.TRIGGERABLE_CODE, "Triggerable", "importance", TicketResult.WAIT_SMES_WCLI203);
    
    private int statusId;
    private String name;
    private String icon;
    private TicketResult result;
    
    @JsonIgnore
    public static final List<TicketStatus> STATUSES = new ArrayList<TicketStatus>() {{	
	add(STATUS_WAITING);
	add(STATUS_RESERVED);
	add(STATUS_SENT_EXP);
	add(STATUS_SENT_OK);
	add(STATUS_SENT_FAIL);	
	add(STATUS_RESEND);
	add(STATUS_CANCELLED);
	add(STATUS_TRIGGERABLE);
    }};    

    public TicketStatus() {
    }
    
    public TicketStatus(int statusId, String name, String icon, TicketResult result) {
	this.statusId = statusId;
	this.name = name;
	this.icon = icon;
	this.result = result;
    }
    
    public int getStatusId() {
	return statusId;
    }
    public void setStatusId(int statusId) {
	this.statusId = statusId;
    }

    public String getName() {
	return name;
    }
    public void setName(String name) {
	this.name = name;
    }  

    public TicketResult getResult() {
	return result;
    }
    public void setResult(TicketResult result) {
	this.result = result;
    }
    
    @JsonIgnore
    public String getIcon16() {
	return icon + "-16";
    }  
        
    @Override
    public int hashCode() {
	int hash = 7;
	hash = 97 * hash + this.statusId;
	return hash;
    }

    @Override
    public boolean equals(Object obj) {
	if (this == obj) {
	    return true;
	}
	if (obj == null) {
	    return false;
	}
	if (getClass() != obj.getClass()) {
	    return false;
	}
	final TicketStatus other = (TicketStatus) obj;
	if (this.statusId != other.statusId) {
	    return false;
	}
	return true;
    }

    @Override
    public String toString() {
	return "TicketStatus{" + "statusId=" + statusId + ", name=" + name + '}';
    }
        
}
