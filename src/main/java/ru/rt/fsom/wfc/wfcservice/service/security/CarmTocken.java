package ru.rt.fsom.wfc.wfcservice.service.security;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.io.Serializable;
import java.util.Objects;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CarmTocken implements Serializable{
    private static final long serialVersionUID = 1L;
    
    private final Integer userId;
    private final String tocken;

    public CarmTocken(Integer userId, String tocken) {
	this.userId = userId;
	this.tocken = tocken;
    }

    public Integer getUserId() {
	return userId;
    }

    public String getTocken() {
	return tocken;
    }

    @Override
    public int hashCode() {
	int hash = 7;
	hash = 37 * hash + Objects.hashCode(this.userId);
	hash = 37 * hash + Objects.hashCode(this.tocken);
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
	final CarmTocken other = (CarmTocken) obj;
	if (!Objects.equals(this.tocken, other.tocken)) {
	    return false;
	}
	if (!Objects.equals(this.userId, other.userId)) {
	    return false;
	}
	return true;
    }

    @Override
    public String toString() {
	return "CarmTocken{" + "userId=" + userId + '}';
    }
    
    
}
