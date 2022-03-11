package ru.rt.fsom.wfc.wfcservice.data.tickets;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import ru.rt.fsom.wfc.wfcservice.service.BaseData;

@Entity
@Table(name="ilink.wfc_ticket")
@JsonInclude(Include.NON_NULL)
public class TicketData extends BaseData implements Serializable{
    private static final long serialVersionUID = 1L;
    
    @Id
    @Column(name = "id") 
    private Integer ticketId;
    
    @Column(name = "task_id")
    private String taskId;
    
    @Column(name = "row_version")
    private Integer rowVersion;
	
    @Column(name = "request_id")
    private Integer requestId;    
    
    @Column(name = "modifying_user")
    private String modifyingUser;
    
    @Column(name = "user_name")
    private String userName;
    
    @Column(name = "ticket_info")
    private String ticketInfo;
    
    @Column(name = "trigger_value")
    private String triggerValue;
    
    @Column(name = "ticket_type")
    private Integer ticketType;
    
    @Column(name = "issue_date")
    private Date dateIssue;
    
    @Column(name = "expiration_date")
    private Date dateExpiration;
    
    @Column(name = "processed_date")
    private Date dateProcessed;
    
    @Column(name = "jeopardy_date")
    private Date dateJeopardy;
    
    @Column(name = "status")
    private Integer status;    

    public TicketData() {
    }
    
    @JsonIgnore
    @Override
    public int getItemId() {
	return ticketId;
    }

    public Integer getRowVersion() {
	return rowVersion;
    }
    public void setRowVersion(Integer rowVersion) {
	this.rowVersion = rowVersion;
    }

    public Integer getTicketId() {
	return ticketId;
    }
    public void setTicketId(Integer ticketId) {
	this.ticketId = ticketId;
    }
    
    public String getTaskId() {
	return taskId;
    }
    public void setTaskId(String taskId) {
	this.taskId = taskId;
    }

    public Integer getRequestId() {
	return requestId;
    }
    public void setRequestId(Integer requestId) {
	this.requestId = requestId;
    }        

    public String getModifyingUser() {
	return modifyingUser;
    }
    public void setModifyingUser(String modifyingUser) {
	this.modifyingUser = modifyingUser;
    }

    public String getUserName() {
	return userName;
    }
    public void setUserName(String userName) {
	this.userName = userName;
    }

    public String getTicketInfo() {
	return ticketInfo;
    }
    public void setTicketInfo(String ticketInfo) {
	this.ticketInfo = ticketInfo;
    }

    public String getTriggerValue() {
	return triggerValue;
    }
    public void setTriggerValue(String triggerValue) {
	this.triggerValue = triggerValue;
    }

    public Date getDateIssue() {
	return dateIssue;
    }
    public void setDateIssue(Date dateIssue) {
	this.dateIssue = dateIssue;
    }

    public Date getDateExpiration() {
	return dateExpiration;
    }
    public void setDateExpiration(Date dateExpiration) {
	this.dateExpiration = dateExpiration;
    }

    public Date getDateProcessed() {
	return dateProcessed;
    }
    public void setDateProcessed(Date dateProcessed) {
	this.dateProcessed = dateProcessed;
    }

    public Date getDateJeopardy() {
	return dateJeopardy;
    }
    public void setDateJeopardy(Date dateJeopardy) {
	this.dateJeopardy = dateJeopardy;
    }

    public Integer getStatus() {
	return status;
    }
    public void setStatus(Integer status) {
	this.status = status;
    }

    public Integer getTicketType() {
	return ticketType;
    }
    public void setTicketType(Integer ticketType) {
	this.ticketType = ticketType;
    }    
    
    /* *** *** */
    
    @Override
    public int hashCode() {
	int hash = 3;
	hash = 23 * hash + Objects.hashCode(this.ticketId);
	hash = 23 * hash + Objects.hashCode(this.requestId);
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
	final TicketData other = (TicketData) obj;
	if (!Objects.equals(this.ticketId, other.ticketId)) {
	    return false;
	}
	if (!Objects.equals(this.requestId, other.requestId)) {
	    return false;
	}
	return true;
    }

    @Override
    public String toString() {
	return "Ticket{" + "d=" + ticketId + ", Request=" + requestId + ", Type=" + ticketType + '}';
    }   
        
}
