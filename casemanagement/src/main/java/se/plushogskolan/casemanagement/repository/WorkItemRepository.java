package se.plushogskolan.casemanagement.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import se.plushogskolan.casemanagement.model.WorkItem;

public interface WorkItemRepository extends PagingAndSortingRepository<WorkItem, Long>{
	
	Slice<WorkItem> findByDescriptionContaining(String description, Pageable pageable);
	
	@Query("SELECT wi FROM #{#entityName} wi WHERE wi.status = :status")
	Slice<WorkItem> getWorkItemsByStatus(@Param("status") WorkItem.Status workItemStatus, Pageable pageable);

	@Query("SELECT wi FROM #{#entityName} wi WHERE wi.user.team.id = :teamId")
	Slice<WorkItem> getWorkItemsByTeamId(@Param("teamId") Long teamId, Pageable pageable);
	
	Slice<WorkItem> findByUserId(Long userId, Pageable pageable);
	
	@Query("SELECT wi FROM #{#entityName} wi WHERE wi.user.id = :userId")
	Slice<WorkItem> getWorkItemsByUserId(@Param("userId")Long userId, Pageable pageable);
	
	@Query("SELECT wi FROM #{#entityName} wi WHERE wi.issue IS NOT NULL")
	Slice<WorkItem> getWorkItemsWithIssue(Pageable pageable);

}
