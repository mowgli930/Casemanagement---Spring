package se.plushogskolan.casemanagement.repository;

import java.util.List;

import org.springframework.data.domain.Slice;
import org.springframework.data.repository.PagingAndSortingRepository;

import se.plushogskolan.casemanagement.model.Team;

public interface TeamRepository extends PagingAndSortingRepository<Team, Long>{
	
	List<Team> findAll();
	
	Slice<Team> findByNameContaining(String name);
	
}
