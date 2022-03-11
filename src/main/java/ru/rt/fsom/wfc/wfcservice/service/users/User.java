package ru.rt.fsom.wfc.wfcservice.service.users;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import ru.rt.fsom.wfc.wfcservice.service.BaseData;

@Entity
@Table(name="ilink.caas_user")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class User extends BaseData implements Serializable{
    private static final long serialVersionUID = 1L;
    
    @Id
    @Column(name = "id") 
    private Integer id;
    
    @Column(name = "username")
    private String userName;	
    
    @Column(name = "password")
    private String password;
    
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

    public String getUserName() {
	return userName;
    }
    public void setUserName(String userName) {
	this.userName = userName;
    }

    public String getPassword() {
	return password;
    }
    public void setPassword(String password) {
	this.password = password;
    }
    
    @Override
    public int hashCode() {
	int hash = 3;
	hash = 89 * hash + Objects.hashCode(this.id);
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
	final User other = (User) obj;
	if (!Objects.equals(this.id, other.id)) {
	    return false;
	}
	return true;
    }

    @Override
    public String toString() {
	return "User{" + "id=" + id + ", userName=" + userName + '}';
    }
    
}
