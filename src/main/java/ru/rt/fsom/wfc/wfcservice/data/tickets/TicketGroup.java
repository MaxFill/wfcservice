package ru.rt.fsom.wfc.wfcservice.data.tickets;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import ru.rt.fsom.wfc.wfcservice.service.BaseData;

@Entity
@Table(name="ilink.wfc_ticket_group")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TicketGroup extends BaseData implements Serializable{
    private static final long serialVersionUID = 1L;
    
    @Id
    @Column(name = "id") 
    private Integer id;
    @Column(name = "ticket_group_name")
    private String name;

    public TicketGroup() {
    }

    public TicketGroup(Integer id, String name) {
	this.id = id;
	this.name = name;
    }
     
    @JsonIgnore
    @Override
    public int getItemId() {
	return id;
    }
    
    public Integer getId() {
	return id;
    }
    public void setId(Integer id) {
	this.id = id;
    }

    public String getName() {
	return name;
    }
    public void setName(String name) {
	this.name = name;
    }

    @Override
    public int hashCode() {
	int hash = 3;
	hash = 37 * hash + Objects.hashCode(this.id);
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
	final TicketGroup other = (TicketGroup) obj;
	if (!Objects.equals(this.id, other.id)) {
	    return false;
	}
	return true;
    }

    @Override
    public String toString() {
	return "WorkGroup{" + "id=" + id + ", name=" + name + '}';
    }
        
}
