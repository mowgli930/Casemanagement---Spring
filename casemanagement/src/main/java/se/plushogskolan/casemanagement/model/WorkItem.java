package se.plushogskolan.casemanagement.model;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

@Entity
public final class WorkItem extends AbstractEntity {	
	
    private int userId;
    private String description;
    
    @OneToOne
    private Issue issue;
    
    @ManyToOne
    private Status status;

    public enum Status {
        UNSTARTED, STARTED, DONE
    }
    
    protected WorkItem() {}
    
    private WorkItem(int userId, String description, Status status) {
        this.userId = userId;
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
            return userId == otherWorkItem.userId
                    && description.equals(otherWorkItem.description) && status.equals(otherWorkItem.status);
        }
        return false;
    }

    @Override
    public int hashCode() {
        int result = 17;
        result += 31 * userId;
        result += 31 * description.hashCode();
        return result;
    }

    public int getUserId() {
        return userId;
    }

    public String getDescription() {
        return description;
    }

    public Status getStatus() {
        return status;
    }

    public static final class WorkItemBuilder {
        private int userId = 0;
        private String description = "";
        private Status status = Status.UNSTARTED;

        // Empty private constructor to hide visibility
        private WorkItemBuilder() {
            ;
        }

        public WorkItemBuilder setUserId(int userId) {
            this.userId = userId;
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
            return new WorkItem(userId, description, status);
        }

    }
}
