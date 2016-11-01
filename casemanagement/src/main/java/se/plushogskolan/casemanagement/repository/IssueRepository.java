package se.plushogskolan.casemanagement.repository;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;

import se.plushogskolan.casemanagement.exception.RepositoryException;
import se.plushogskolan.casemanagement.model.Issue;

public interface IssueRepository extends PagingAndSortingRepository<Issue, Long>{

    void saveIssue(Issue issue) throws RepositoryException;

    void updateIssue(Issue newValues) throws RepositoryException;

    void deleteIssue(int issueId) throws RepositoryException;

    List<Issue> getIssuesByWorkItemId(int workItemId) throws RepositoryException;
    
    Issue getIssueById(int issueId) throws RepositoryException;

}
