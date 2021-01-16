/**
 * 
 */
package com.visionous.dms.pojo;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.SequenceGenerator;

import org.springframework.format.annotation.DateTimeFormat;

import com.visionous.dms.configuration.helpers.DmsCoreVersion;

/**
 * @author delimeta
 *
 */
@Entity
public class Teeth implements Serializable{
    
    /**
	 * 
	 */
	private static final long serialVersionUID = DmsCoreVersion.SERIAL_VERSION_UID;

	@Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TEETH_SEQ")
    @SequenceGenerator(sequenceName = "teeth_seq", allocationSize = 1, name = "TEETH_SEQ")    
    private Long id;
	
	private String name;
	
	private String description;
	
	@DateTimeFormat (pattern="dd-MMM-YYYY")
	private String addeddate;
	
	@OneToOne(mappedBy = "tooth", cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@PrimaryKeyJoinColumn
	private Record record;

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the addeddate
	 */
	public String getAddeddate() {
		return addeddate;
	}

	/**
	 * @param addeddate the addeddate to set
	 */
	public void setAddeddate(String addeddate) {
		this.addeddate = addeddate;
	}

	/**
	 * @return the allRecords
	 */
	public Record getAllRecords() {
		return record;
	}

	/**
	 * @param record the allRecords to set
	 */
	public void setAllRecords(Record record) {
		this.record = record;
	}

	@Override
	public String toString() {
		return "Teeth [id=" + id + ", name=" + name + ", description=" + description + ", addeddate=" + addeddate
				+ "]";
	}

}
