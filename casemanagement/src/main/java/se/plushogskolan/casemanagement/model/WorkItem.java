package se.plushogskolan.casemanagement.model;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

@Entity
public final class WorkItem extends AbstractEntity {

	private String description;

	@ManyToOne
	private User user;

	@OneToOne
	private Issue issue;

	private Status status;

	public enum Status {
		UNSTARTED, STARTED, DONE
	}

	protected WorkItem() {
	}

	private WorkItem(User user, Issue issue, String description, Status status) {
		this.user = user;
		this.issue = issue;
		this.description = description;
		this.status = status;
	}

	public static WorkItemBuilder builder() {
		return new WorkItemBuilder();
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(ToStringStyle.SIMPLE_STYLE);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj instanceof WorkItem) {
			WorkItem otherWorkItem = (WorkItem) obj;
			return user == otherWorkItem.user && description.equals(otherWorkItem.description)
					&& status.equals(otherWorkItem.status);
		}
		return false;
	}

	@Override
	public int hashCode() {
		int result = 17;
		result += 31 * user.hashCode();
		result += 31 * description.hashCode();
		return result;
	}

	public User getUser() {
		return user;
	}
	
	public Issue getIssue() {
		return issue;
	}

	public String getDescription() {
		return description;
	}

	public Status getStatus() {
		return status;
	}

	public static final class WorkItemBuilder {
		private User user = null;
		private Issue issue = null;
		private String description = "";
		private Status status = Status.UNSTARTED;

		// Empty private constructor to hide visibility
		private WorkItemBuilder() {
			;
		}

		public WorkItemBuilder setUser(User user) {
			this.user = user;
			return this;
		}

		public WorkItemBuilder setIssue(Issue issue) {
			this.issue = issue;
			return this;
		}
		public WorkItemBuilder setDescription(String description) {
			this.description = description;
			return this;
		}

		public WorkItemBuilder setStatus(Status status) {
			this.status = status;
			return this;
		}

		public WorkItemBuilder setStatus(int statusIndex) {
			switch (statusIndex) {
			case 1:
				this.status = Status.UNSTARTED;
				break;
			case 2:
				this.status = Status.STARTED;
				break;
			case 3:
				this.status = Status.DONE;
				break;
			default:
				throw new IllegalArgumentException("Status index can be 1, 2 or 3. Was " + statusIndex);
			}
			return this;
		}

		public WorkItem build() {
			return new WorkItem(user, issue, description, status);
		}

	}
}
