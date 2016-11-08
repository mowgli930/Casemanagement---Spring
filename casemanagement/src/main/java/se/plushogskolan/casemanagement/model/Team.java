package se.plushogskolan.casemanagement.model;

import java.util.Collection;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.OneToMany;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

@Entity
//@EntityListeners(AuditingEntityListener.class)
public final class Team extends AbstractEntity {

	private String name;

	private boolean active;

	@OneToMany(mappedBy = "team")
	private Collection<User> users;

	@CreatedBy
	private String createdBy;

	@LastModifiedBy
	private String lastModifiedBy;

	@CreatedDate
	private Date createdDate;

	@LastModifiedDate
	private Date lastModifiedDate;

	protected Team() {
	}

	public Team(String name) {
		this.active = true;
		this.name = name;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + name.hashCode();
		return result;
	}

	@Override
	public boolean equals(Object other) {

		if (this == other) {
			return true;
		}

		if (null == other) {
			return false;
		}

		if (other instanceof Team) {
			Team otherTeam = (Team) other;
			return id == otherTeam.getId() && name.equals(otherTeam.getName());
		}
		return false;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
	}

	public boolean isActive() {
		return active;
	}
	
	public Team setActive(boolean active) {
		this.active = active;
		return this;
	}

	public String getName() {
		return name;
	}
	
	public Team setName(String name) {
		this.name = name;
		return this;
	}

	public Collection<User> getUsers() {
		return users;
	}
	
	public void addUser(User user){
		users.add(user);
	}
	
	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getLastModifiedBy() {
		return lastModifiedBy;
	}

	public void setLastModifiedBy(String lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Date getLastModifiedDate() {
		return lastModifiedDate;
	}

	public void setLastModifiedDate(Date lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}
}
