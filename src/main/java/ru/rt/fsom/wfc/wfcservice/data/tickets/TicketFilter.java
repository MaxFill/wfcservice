package ru.rt.fsom.wfc.wfcservice.data.tickets;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@JsonInclude(Include.NON_NULL)
public class TicketFilter implements Serializable{
    private static final long serialVersionUID = 1L;
    private static final int MAX_ROW_LAZY_LOAD = 1000;
	
    private int userId;
    private String name;
    private Integer ticketId;
    private Integer requestId;
    private String userName;
    private String ticketInfo;   
    private TicketGroup group;    
    private String jeopardyStatus;
    private Date dateIssueFrom;
    private Date dateIssueTo;
    private Date dateJeopardyFrom;
    private Date dateJeopardyTo;
    private List<TicketStatus> statuses;
    private Integer positionStart = 0;
    private Integer positionEnd = MAX_ROW_LAZY_LOAD;
    
    public TicketFilter() {	
    }

    public int getUserId() {
	return userId;
    }
    public void setUserId(int userId) {
	this.userId = userId;
    }
    
    public String getName() {
	return name;
    }
    public void setName(String name) {
	this.name = name;
    }

    public List<TicketStatus> getStatuses() {
	return statuses;
    }
    public void setStatuses(List<TicketStatus> statuses) {
	this.statuses = statuses;
    }    

    public Integer getTicketId() {
	return ticketId;
    }
    public void setTicketId(Integer ticketId) {
	this.ticketId = ticketId;
    }

    public Integer getRequestId() {
	return requestId;
    }
    public void setRequestId(Integer requestId) {
	this.requestId = requestId;
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
   
    public Date getDateIssueFrom() {
	return dateIssueFrom;
    }
    public void setDateIssueFrom(Date dateIssueFrom) {
	this.dateIssueFrom = dateIssueFrom;
    }

    public Date getDateIssueTo() {
	return dateIssueTo;
    }
    public void setDateIssueTo(Date dateIssueTo) {
	this.dateIssueTo = dateIssueTo;
    }

    public Date getDateJeopardyFrom() {
	return dateJeopardyFrom;
    }
    public void setDateJeopardyFrom(Date dateJeopardyFrom) {
	this.dateJeopardyFrom = dateJeopardyFrom;
    }

    public Date getDateJeopardyTo() {
	return dateJeopardyTo;
    }
    public void setDateJeopardyTo(Date dateJeopardyTo) {
	this.dateJeopardyTo = dateJeopardyTo;
    }       

    public TicketGroup getGroup() {
	return group;
    }
    public void setGroup(TicketGroup group) {
	this.group = group;
    }     

    public String getJeopardyStatus() {
	return jeopardyStatus;
    }
    public void setJeopardyStatus(String jeopardyStatus) {
	this.jeopardyStatus = jeopardyStatus;
    }
    
    public Integer getPositionStart() {
	return positionStart;
    }
    public void setPositionStart(Integer positionStart) {
	this.positionStart = positionStart;
    }

    public Integer getPositionEnd() {
	return positionEnd;
    }
    public void setPositionEnd(Integer positionEnd) {
	this.positionEnd = positionEnd;
    }
    
    /* *** *** */

    @Override
    public int hashCode() {
	int hash = 7;
	hash = 83 * hash + this.userId;
	hash = 83 * hash + Objects.hashCode(this.name);
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
	final TicketFilter other = (TicketFilter) obj;
	if (this.userId != other.userId) {
	    return false;
	}
	if (!Objects.equals(this.name, other.name)) {
	    return false;
	}
	return true;
    }        

    @Override
    public String toString() {
	return "TicketFilter{" + "userId=" + userId + ", name=" + name + '}';
    }    
        
}
