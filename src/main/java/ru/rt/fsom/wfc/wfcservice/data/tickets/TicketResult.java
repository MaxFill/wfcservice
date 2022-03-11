package ru.rt.fsom.wfc.wfcservice.data.tickets;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.io.Serializable;
import java.util.Objects;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class TicketResult implements Serializable{
    private static final long serialVersionUID = 1L;

    private String smessageId;
    private String smessage;    	 
    private int taskStatus;
    
    @JsonIgnore
    public final static TicketResult OK_SMES_WCLI200 = new TicketResult("WCLI200", "Work item or ticket sent and processed successfully.", 0);
    @JsonIgnore
    public final static TicketResult EXP_SMES_WCLI201 = new TicketResult("WCLI201", "Work item or ticket expired.", 0);
    @JsonIgnore
    public final static TicketResult WAIT_SMES_WCLI202 = new TicketResult("WCLI202", "Triggerable ticket was triggered.", 0);
    @JsonIgnore
    public final static TicketResult WAIT_SMES_WCLI203 = new TicketResult("WCLI203", "Triggering ticket processed.", 0);
    @JsonIgnore
    public final static TicketResult FAIL_SMES_WCLI901 = new TicketResult("WCLI901", "Work item or ticket failed by a user, task status is failed.", 0);
    @JsonIgnore
    public final static TicketResult FAIL_SMES_WCLI902 = new TicketResult("WCLI902", "Work item or ticket is failed when it is assigned to a non-existing ticket group", 0);

    public TicketResult() {
    }
    
    public TicketResult(String smessageId, String smessage, int taskStatus) {
	this.smessageId = smessageId;
	this.smessage = smessage;
	this.taskStatus = taskStatus;
    }
    
    public String getSmessageId() {
	return smessageId;
    }
    public void setSmessageId(String smessageId) {
	this.smessageId = smessageId;
    }

    public String getSmessage() {
	return smessage;
    }
    public void setSmessage(String smessage) {
	this.smessage = smessage;
    }

    public int getTaskStatus() {
	return taskStatus;
    }
    public void setTaskStatus(int taskStatus) {
	this.taskStatus = taskStatus;
    }
    
    @Override
    public int hashCode() {
	int hash = 7;
	hash = 31 * hash + Objects.hashCode(this.smessageId);
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
	final TicketResult other = (TicketResult) obj;
	if (!Objects.equals(this.smessageId, other.smessageId)) {
	    return false;
	}
	return true;
    }

    @Override
    public String toString() {
	return "TicketResult{" + "smessageId=" + smessageId + ", taskStatus=" + taskStatus + '}';
    }
    
}
