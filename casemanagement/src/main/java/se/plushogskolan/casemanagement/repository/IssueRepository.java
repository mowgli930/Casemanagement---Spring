package se.plushogskolan.casemanagement.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import se.plushogskolan.casemanagement.exception.RepositoryException;
import se.plushogskolan.casemanagement.model.Issue;

public interface IssueRepository extends PagingAndSortingRepository<Issue, Long>{

	@Query("SELECT i FROM #{entityName} i WHERE i.workItem.id = :workItemId")
    List<Issue> getIssuesByWorkItemId(@Param("workItemId")Long workItemId) throws RepositoryException;
    
    Issue findById(Long id);
    
    @Transactional
	Issue removeById(Long id);
    
}
