package se.plushogskolan.casemanagement.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import se.plushogskolan.casemanagement.model.WorkItem;

public interface WorkItemRepository extends PagingAndSortingRepository<WorkItem, Long>{
	
	@Query("UPDATE #{#entityName} wi SET wi.status = :status WHERE wi.id = :workItemId")
	WorkItem updateStatusById(@Param("workItemId") Long workItemId, @Param("status") WorkItem.Status workItemStatus);
	
	@Modifying
	@Query("UPDATE #{#entityName} wi SET wi.user.id = :userId WHERE wi.id = :workItemId")
	void addWorkItemToUser(@Param("workItemId") Long workItemId, @Param("userId") Long userId);
	
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
