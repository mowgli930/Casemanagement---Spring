package se.plushogskolan.casemanagement.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@EntityListeners(AuditingEntityListener.class)
public final class Team extends AbstractEntity {

    private String name;

    private boolean active;

    @CreatedBy
    private String createdBy;

    @LastModifiedBy
    private String lastModifiedBy;

    @CreatedDate
    private Date createdDate;

    @LastModifiedDate
    private Date lastModifiedDate;

    private Team(boolean active, String name) {
	this.active = active;
	this.name = name;
    }

    public static TeamBuilder builder() {
	return new TeamBuilder();
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

    public String getName() {
	return name;
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

    public static final class TeamBuilder {
	private boolean active = true;

	private TeamBuilder() {
	    super();
	}

	public Team build(String name) {
	    return new Team(active, name);
	}

	public TeamBuilder setActive(boolean active) {
	    this.active = active;
	    return this;
	}
    }
}
