package ru.rt.fsom.wfc.wfcservice.data.tickets;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import ru.rt.fsom.wfc.wfcservice.service.BaseData;

@Entity
@Table(name="ilink.wfc_ticket_param")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TicketParam extends BaseData implements Serializable{
    private static final long serialVersionUID = 1L;
    
    @Id
    @Column(name = "id")
    private int id;
    
    @Column(name = "ticket_id")
    private int ticketId;
    
    @Column(name = "name")
    private String name;
    
    @Column(name = "value")
    private String value;

    public TicketParam() {
    }

    @JsonIgnore
    @Override
    public int getItemId() {
	return id;
    }
    
    public int getId() {
	return id;
    }
    public void setId(int id) {
	this.id = id;
    }

    public int getTicketId() {
	return ticketId;
    }
    public void setTicketId(int ticketId) {
	this.ticketId = ticketId;
    }

    public String getName() {
	return name;
    }
    public void setName(String name) {
	this.name = name;
    }

    public String getValue() {
	return value;
    }
    public void setValue(String value) {
	this.value = value;
    }

    @Override
    public int hashCode() {
	int hash = 7;
	hash = 37 * hash + this.id;
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
	final TicketParam other = (TicketParam) obj;
	if (this.id != other.id) {
	    return false;
	}
	return true;
    }

    @Override
    public String toString() {
	return "TicketParam{" + "id=" + id + '}';
    }        
    
}
