package se.plushogskolan.casemanagement.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.ManyToOne;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@EntityListeners(AuditingEntityListener.class)
public class User extends AbstractEntity {

    private String username;

    @ManyToOne
    private Team team;

    private boolean isActive;

    private String firstName;

    private String lastName;

    @CreatedBy
    private String createdBy;

    @LastModifiedBy
    private String lastModifiedBy;

    @CreatedDate
    private Date createdDate;

    @LastModifiedDate
    private Date lastModifiedDate;

    private User(boolean isActive, Team team, String username, String firstName, String lastName) {
	this.isActive = isActive;
	this.team = team;
	this.username = username;
	this.firstName = firstName;
	this.lastName = lastName;
    }

    protected User() {
    }

    public static UserBuilder builder() {
	return new UserBuilder();
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

    public static final class UserBuilder {
	// Optional
	private Team team = null; // ?? Hur fan ska vi göra här
	private boolean isActive = true;
	private String firstName = "";
	private String lastName = "";

	private UserBuilder() {
	    super();
	}

	public User build(String username) {

	    return new User(isActive, team, username, firstName, lastName);
	}

	public UserBuilder setTeam(Team team) {
	    this.team = team;
	    return this;
	}

	public UserBuilder setActive(boolean isActive) {
	    this.isActive = isActive;
	    return this;
	}

	public UserBuilder setFirstName(String firstName) {
	    this.firstName = firstName;
	    return this;
	}

	public UserBuilder setLastName(String lastName) {
	    this.lastName = lastName;
	    return this;
	}
    }
}
