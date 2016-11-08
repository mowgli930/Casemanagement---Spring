package se.plushogskolan.casemanagement.model;

import java.util.Collection;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

@Entity
//@EntityListeners(AuditingEntityListener.class)
public class User extends AbstractEntity {

	@Column(unique = true)
	private String username;

	@ManyToOne
	private Team team;

	@OneToMany(mappedBy = "user")
	private Collection<WorkItem> workItems = null;

	private boolean isActive = true;

	private String firstName= "";

	private String lastName = "";

	@CreatedBy
	private String createdBy;

	@LastModifiedBy
	private String lastModifiedBy;

	@CreatedDate
	private Date createdDate;

	@LastModifiedDate
	private Date lastModifiedDate;
	
	public User(String username){
		this.username = username;
	}

	protected User() {
	}

	public boolean isIdentical(Object other) {
		if (!equals(other)) {
			return false;
		}
		User otherUser = (User) other;
		return firstName.equals(otherUser.firstName) && lastName.equals(otherUser.lastName) && team == otherUser.team
				&& isActive == otherUser.isActive;
	}

	@Override
	public boolean equals(Object other) {

		if (this == other) {
			return true;
		}

		if (null == other) {
			return false;
		}

		if (other instanceof User) {
			User otherUser = (User) other;
			return id == otherUser.getId() && username.equals(otherUser.getUsername());
		}
		return false;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((username == null) ? 0 : username.hashCode());
		return result;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(ToStringStyle.JSON_STYLE);
	}

	public boolean isActive() {
		return isActive;
	}

	public Team getTeam() {
		return team;
	}

	public Collection<WorkItem> getWorkItem() {
		return workItems;
	}

	public String getUsername() {
		return username;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
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
	
	public User setFirstName(String firstName) {
		this.firstName = firstName;
		return this;
	}
	
	public User setLastName(String lastName) {
		this.lastName = lastName;
		return this;
	}
	
	public User setUsername(String username) {
		this.username = username;
		return this;
	}
	
	public User setTeam(Team team) {
		this.team = team;
		return this;
	}
	
	public User setActive(boolean isActive) {
		this.isActive = isActive;
		return this;
	}
	
	public User setWorkItems(Collection<WorkItem> workItems) {
		this.workItems = workItems;
		return this;
	}

}
