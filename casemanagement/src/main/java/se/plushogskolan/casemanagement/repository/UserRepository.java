package se.plushogskolan.casemanagement.repository;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.repository.PagingAndSortingRepository;

import se.plushogskolan.casemanagement.model.User;

public interface UserRepository extends PagingAndSortingRepository<User, Long> {

	Slice<User> findByFirstNameContaining(String firstName, PageRequest page);
	
	Slice<User> findByLastNameContaining(String lastName, PageRequest page);
	
	Slice<User> findByUsernameContaining(String username, PageRequest page);
	
	Slice<User> findByTeamId(Long id, PageRequest page);
	
	Long countByTeamId(Long id);
}
