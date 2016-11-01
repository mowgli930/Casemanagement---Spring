package se.plushogskolan.casemanagement.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

@Entity
public final class Issue {
	
	@Id
	@GeneratedValue
    private  Long id;
	
    @OneToOne
    private WorkItem workItem;
    
    private String description;

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
    
    public Long getId() {
        return id;
    }
    
    public WorkItem getWorkitem() {
		return workItem;
	}
    
    public String getDescription() {
        return description;
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
