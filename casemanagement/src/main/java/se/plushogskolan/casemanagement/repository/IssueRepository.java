package se.plushogskolan.casemanagement.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import se.plushogskolan.casemanagement.model.Issue;

public interface IssueRepository extends PagingAndSortingRepository<Issue, Long>{

	@Query("SELECT i FROM #{#entityName} i WHERE i.workItem.id = :workItemId")
    List<Issue> getIssuesByWorkItemId(@Param("workItemId")Long workItemId);    
}
