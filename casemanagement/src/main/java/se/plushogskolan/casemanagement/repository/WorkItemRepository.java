package se.plushogskolan.casemanagement.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import se.plushogskolan.casemanagement.model.WorkItem;

public interface WorkItemRepository extends PagingAndSortingRepository<WorkItem, Long>{
	
//	WorkItem updateStatusById(Long workItemId, WorkItem.Status workItemStatus);
	
	@Modifying
	@Query("UPDATE #{#entityName} wi SET wi.user.id = :userId WHERE wi.id = :workItemId")
	void addWorkItemToUser(@Param("workItemId") Long workItemId, @Param("userId") Long userId);
//	
//	Slice<WorkItem> getWorkItemByStatus(WorkItem.Status workItemStatus, Pageable pageable);
//   
//	Slice<WorkItem> getWorkItemsByTeamId(Long teamId, Pageable pageable);
	
	Slice<WorkItem> findByUserId(Long userId, Pageable pageable);
	
	@Query("SELECT wi FROM #{#entityName} wi WHERE wi.user.id = :userId")
	Slice<WorkItem> getWorkItemsByUserId(@Param("userId")Long userId, Pageable pageable);
	
	@Query("SELECT wi FROM #{#entityName} wi WHERE wi.issue IS NOT NULL")
	Slice<WorkItem> getWorkItemsWithIssue(Pageable pageable);

}
