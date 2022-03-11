package ru.rt.fsom.wfc.wfcservice.service.users;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import ru.rt.fsom.wfc.wfcservice.service.BaseData;

@Entity
@Table(name="ilink.wfc_user_settings")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserSettings extends BaseData implements Serializable{
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id") 
    private Integer id;
    
    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "filters")
    private String filters;	//List<TicketFilter> in JSON

    @Column(name = "settings")
    private String settings;	//JSON

    public UserSettings() {
    }

    public UserSettings(Integer userId) {
	this.userId = userId;
    }
           
    public Integer getId() {
	return id;
    }
    public void setId(Integer id) {
	this.id = id;
    }

    public Integer getUserId() {
	return userId;
    }
    public void setUserId(Integer userId) {
	this.userId = userId;
    }

    public String getFilters() {
	return filters;
    }
    public void setFilters(String filters) {
	this.filters = filters;
    }

    public String getSettings() {
	return settings;
    }
    public void setSettings(String settings) {
	this.settings = settings;
    }
        
    @JsonIgnore
    @Override
    public int getItemId() {
	return id;
    }

    @Override
    public int hashCode() {
	int hash = 3;
	hash = 41 * hash + Objects.hashCode(this.id);
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
	final UserSettings other = (UserSettings) obj;
	if (!Objects.equals(this.id, other.id)) {
	    return false;
	}
	return true;
    }

    @Override
    public String toString() {
	return "UserSettings{" + "id=" + id + ", userId=" + userId + '}';
    }    
    
}
