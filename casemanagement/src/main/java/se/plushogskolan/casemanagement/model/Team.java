package se.plushogskolan.casemanagement.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

@Entity
public final class Team {
	
	@Id
	@GeneratedValue
    private Long id;
    
    private String name;
   
    private boolean active;

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

    public Long getId() {
        return id;
    }

    public boolean isActive() {
        return active;
    }

    public String getName() {
        return name;
    }

    public static final class TeamBuilder {
        private boolean active = true;

        private TeamBuilder() {
            super();
        }

        public Team build(String name) {
            // Required name
            return new Team(active, name);
        }

        public TeamBuilder setActive(boolean active) {
            this.active = active;
            return this;
        }
    }
}
