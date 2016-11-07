package se.plushogskolan.casemanagement.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import se.plushogskolan.casemanagement.model.User;

public interface UserRepository extends PagingAndSortingRepository<User, Long>{

//    void saveUser(User user) throws RepositoryException;
//
//    void updateUser(User newValues) throws RepositoryException;
//
	@Modifying
	@Query("UPDATE User u SET u.firstName = :firstName WHERE u.id = :userId")
	void updateUserFirstName(@Param("userId")Long userId, @Param("firstName")String firstName);
//    void inactivateUserById(int userId) throws RepositoryException;
//
//    void activateUserById(int userId) throws RepositoryException;
//
//    User getUserById(int userId) throws RepositoryException;
//
//    List<User> searchUsersBy(String firstName, String lastName, String username) throws RepositoryException;
//
//    List<User> getUsersByTeamId(int teamId) throws RepositoryException;

}
