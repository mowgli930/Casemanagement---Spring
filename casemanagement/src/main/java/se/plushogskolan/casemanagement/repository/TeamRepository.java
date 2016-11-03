package se.plushogskolan.casemanagement.repository;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;

import se.plushogskolan.casemanagement.exception.RepositoryException;
import se.plushogskolan.casemanagement.model.Team;

public interface TeamRepository extends PagingAndSortingRepository<Team, Long>{

//    void saveTeam(Team team) throws RepositoryException;
//
//    void updateTeam(Team newValues) throws RepositoryException;
//
//    void inactivateTeam(int teamId) throws RepositoryException;
//
//    List<Team> getAllTeams() throws RepositoryException;
//
//    void addUserToTeam(int userId, int teamId) throws RepositoryException;
//
//    void activateTeam(int teamId) throws RepositoryException;

}
