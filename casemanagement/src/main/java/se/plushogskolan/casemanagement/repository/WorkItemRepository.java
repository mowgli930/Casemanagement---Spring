package se.plushogskolan.casemanagement.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import se.plushogskolan.casemanagement.model.WorkItem;

public interface WorkItemRepository extends PagingAndSortingRepository<WorkItem, Long>{
	
//	WorkItem updateStatusById(Long workItemId, WorkItem.Status workItemStatus);
//	
//	void addWorkItemToUser(Long workItemId, Long userId);
//	
//	Slice<WorkItem> getWorkItemByStatus(WorkItem.Status workItemStatus, Pageable pageable);
//   
//	Slice<WorkItem> getWorkItemsByTeamId(Long teamId, Pageable pageable);
	
	@Query("SELECT wi FROM #{#entityName} wi WHERE wi.user.id = :id")
	Slice<WorkItem> getWorkItemsByUserId(@Param("id") Long userId, Pageable pageable);
	
	@Query("SELECT wi FROM #{#entityName} wi WHERE wi.issue IS NOT NULL")
	Slice<WorkItem> getWorkItemsWithIssue(Pageable pageable);

}
