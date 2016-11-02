package se.plushogskolan.casemanagement.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.OneToOne;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@EntityListeners(AuditingEntityListener.class)
public final class Issue extends AbstractEntity {

	@OneToOne
	private WorkItem workItem;

	private String description;

	@CreatedBy
	private String createdBy;

	@LastModifiedBy
	private String lastModifiedBy;

	@CreatedDate
	private Date createdDate;

	@LastModifiedDate
	private Date lastModifiedDate;

	private Issue(WorkItem workItem, String description) {

		this.workItem = workItem;
		this.description = description;
	}

	public static IssueBuilder builder(WorkItem workItem) {
		return new IssueBuilder(workItem);
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj instanceof Issue) {
			Issue otherIssue = (Issue) obj;
			return id == otherIssue.id && description.equals(otherIssue.description);
		}
		return false;
	}

	@Override
	public int hashCode() {
		int result = 17;
		result += 31 * id;
		result += 31 * description.hashCode();
		return result;
	}

	public WorkItem getWorkitem() {
		return workItem;
	}

	public String getDescription() {
		return description;
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

	public static final class IssueBuilder {
		private WorkItem workItem;
		private String description = "";

		private IssueBuilder(WorkItem workItem) {
			this.workItem = workItem;
		}

		public IssueBuilder setId() {
			return this;
		}

		public IssueBuilder setDescription(String description) {
			this.description = description;
			return this;
		}

		public Issue build() {
			return new Issue(workItem, description);
		}
	}
}
