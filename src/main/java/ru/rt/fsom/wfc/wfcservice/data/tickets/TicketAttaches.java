package ru.rt.fsom.wfc.wfcservice.data.tickets;

import java.io.File;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import org.apache.commons.lang3.StringUtils;
import ru.rt.fsom.wfc.wfcservice.service.BaseData;

@Entity
@Table(name="ilink.wfc_attached")
public class TicketAttaches extends BaseData implements Serializable {
    private static final long serialVersionUID = -4633936978772232516L;
 
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="id")
    private Integer id;
    
    @Column(name="name")
    private String name;    
    
    @Column(name="ticket_id")
    private int ticketId; 
	
    @Column(name="file_size")
    private Long fileSize;

    @Column(name="date_create")
    private Date dateCreate;
    
    @Column(name="type")
    private String type;
    
    @Column(name="extension")
    private String extension;
    
    @Column(name="guid")
    private String guid;    
    
    @Column(name="author_id")
    private int author;    
           
    public TicketAttaches() {
    }

    public String getFullName(){        
        StringBuilder sb = new StringBuilder();
	sb.append(guid.substring(0, 2))
            .append(File.separator)
            .append(guid.substring(2, 4))
            .append(File.separator)
            .append(guid)
	    .append(".").append(getExtension());
        return sb.toString();
    }    

    public int getTicketId() {
	return ticketId;
    }
    public void setTicketId(int ticketId) {
	this.ticketId = ticketId;
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
        if (StringUtils.isNotBlank(name) && name.length() > 1024){
            name = name.substring(0, 1023);
        }
        this.name = name;
    }

    public Long getFileSize() {
	return fileSize;
    }
    public void setFileSize(Long fileSize) {
	this.fileSize = fileSize;
    }

    public Date getDateCreate() {
        return dateCreate;
    }
    public void setDateCreate(Date dateCreate) {
        this.dateCreate = dateCreate;
    }

    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }

    public int getAuthor() {
	return author;
    }
    public void setAuthor(int author) {
	this.author = author;
    }

    public String getExtension() {
        return extension;
    }
    public void setExtension(String extension) {
        this.extension = extension;
    }

    public String getGuid() {
        return guid;
    }
    public void setGuid(String guid) {
        this.guid = guid;
    } 

    @Override
    public int hashCode() {
	int hash = 5;
	hash = 83 * hash + Objects.hashCode(this.guid);
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
	final TicketAttaches other = (TicketAttaches) obj;
	if (!Objects.equals(this.guid, other.guid)) {
	    return false;
	}
	return true;
    }
    
    @Override
    public String toString() {
        return "Attaches[ id=" + id + " ]";
    }

    @Override
    public int getItemId() {
	return id;
    }
    
}
