package se.plushogskolan.casemanagement.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import se.plushogskolan.casemanagement.exception.ServiceException;
import se.plushogskolan.casemanagement.model.AbstractEntity;
import se.plushogskolan.casemanagement.model.Issue;
import se.plushogskolan.casemanagement.model.Team;
import se.plushogskolan.casemanagement.model.User;
import se.plushogskolan.casemanagement.model.WorkItem;
import se.plushogskolan.casemanagement.model.WorkItem.Status;
import se.plushogskolan.casemanagement.repository.IssueRepository;
import se.plushogskolan.casemanagement.repository.TeamRepository;
import se.plushogskolan.casemanagement.repository.UserRepository;
import se.plushogskolan.casemanagement.repository.WorkItemRepository;

@Service
public class CaseService {

	private final UserRepository userRepository;
	private final TeamRepository teamRepository;
	private final WorkItemRepository workItemRepository;
	private final IssueRepository issueRepository;

	@Autowired
	public CaseService(UserRepository userRepository, TeamRepository teamRepository,
			WorkItemRepository workItemRepository, IssueRepository issueRepository) {
		this.userRepository = userRepository;
		this.teamRepository = teamRepository;
		this.workItemRepository = workItemRepository;
		this.issueRepository = issueRepository;
	}

	// USER

	public User save(User user) {

		if (userFillsRequirements(user) && !isPersistedObject(user)) {
			return userRepository.save(user);
		} else {
			throw new ServiceException("Username too short, team is full or object already exists" + user.toString());
		}
	}

	@Transactional
	public void updateUserFirstName(Long userId, String firstName) {

		if (userRepository.exists(userId)) {

			User user = userRepository.findOne(userId);

			user.setFirstName(firstName);

			userRepository.save(user);

		} else {
			throw new ServiceException("User doesnt exist :" + userId);
		}
	}

	@Transactional
	public void updateUserLastName(Long userId, String lastName) {

		if (userRepository.exists(userId)) {
			User user = userRepository.findOne(userId);

			user.setLastName(lastName);

			userRepository.save(user);

		} else {
			throw new ServiceException("User doesnt exist :" + userId);
		}
	}

	public void updateUserUsername(Long userId, String username) {

		if (usernameLongEnough(username) && userRepository.exists(userId)) {

			User user = userRepository.findOne(userId);

			user.setUsername(username);

			userRepository.save(user);
		} else {
			throw new ServiceException("User doesnt exist or username to long :" + userId);
		}
	}

	public void inactivateUser(Long userId) {
		
		if(userRepository.exists(userId)){
			User user = userRepository.findOne(userId);
			user.setActive(false);
			userRepository.save(user);
		}else{
			throw new ServiceException("User doesnt exists :" + userId);
		}
	}
//
//	public void activateUserById(int userId) {
//		try {
//			userRepository.activateUserById(userId);
//		} catch (RepositoryException e) {
//			throw new ServiceException("Could not activate User with id " + userId, e);
//		}
//	}
//
//	public User getUserById(int userId) {
//		try {
//			return userRepository.getUserById(userId);
//		} catch (RepositoryException e) {
//			throw new ServiceException("Could not get User by id " + userId, e);
//		}
//	}
//
//	public List<User> searchUsersBy(String firstName, String lastName, String username) {
//		try {
//			return userRepository.searchUsersBy(firstName, lastName, username);
//		} catch (RepositoryException e) {
//			throw new ServiceException("Could not get User by first name, last name, username.", e);
//		}
//	}
//
//	public List<User> getUsersByTeamId(int teamId) {
//		try {
//			return userRepository.getUsersByTeamId(teamId);
//		} catch (RepositoryException e) {
//			throw new ServiceException("Could not get User by TeamId, teamId=" + teamId, e);
//		}
//	}

	// // TEAM

	@Transactional
	public Team save(Team team) {
		try {
			return teamRepository.save(team);
		} catch (Exception e) {
			throw new ServiceException("Could not save Team: " + team.toString(), e);
		}
	}

	@Transactional
	public Team updateTeam(Long teamId, Team newValues) {
		try {
			Team team = teamRepository.findOne(teamId);
			team.setActive(newValues.isActive()).setName(newValues.getName());
			return teamRepository.save(team);
		} catch (Exception e) {
			throw new ServiceException("Could not update Team with id " + newValues.getId(), e);
		}
	}

	@Transactional
	public Team inactivateTeam(Long teamId) {
		try {
			Team team = teamRepository.findOne(teamId);
			team.setActive(false);
			return teamRepository.save(team);
		} catch (Exception e) {
			throw new ServiceException("Could not inactivate Team with id: " + teamId, e);
		}
	}

	@Transactional
	public Team activateTeam(Long teamId) {
		try {
			Team team = teamRepository.findOne(teamId);
			team.setActive(true);
			return teamRepository.save(team);
		} catch (Exception e) {
			throw new ServiceException("Could not activate Team with id: " + teamId, e);
		}
	}

	public Page<Team> getAllTeams(int page, int size) {
		try {
			Page<Team> teams = (Page<Team>) teamRepository.findAll(new PageRequest(page, size));
			return teams;
		} catch (Exception e) {
			throw new ServiceException("Could not retriev all Teams", e);
		}
	}

	@Transactional
	public void addUserToTeam(Long userId, Long teamId) {
		try {
			if (teamHasSpaceForUser(teamId)) {
				Team team = teamRepository.findOne(teamId);
				User user = userRepository.findOne(userId);
				team.addUser(user);
				teamRepository.save(team);
			} else {
				throw new ServiceException("No space in team for user. userId = " + userId + "teamId = " + teamId);
			}
		} catch (Exception e) {
			throw new ServiceException("Could not add User with id: " + userId + " " + "to Team with id: " + teamId, e);
		}
	}

	// WORKITEM

//	public void save(WorkItem workItem) {
//		try {
//			workItemRepository.saveWorkItem(workItem);
//		} catch (RepositoryException e) {
//			throw new ServiceException("Could not save workItem: " + workItem.toString(), e);
//		}
//	}
//
//	public void updateStatusById(int workItemId, WorkItem.Status workItemStatus) {
//		try {
//			workItemRepository.updateStatusById(workItemId, workItemStatus);
//		} catch (RepositoryException e) {
//			throw new ServiceException("Could not update status to: \"" + workItemStatus.toString()
//					+ "\" on WorkItem with id: " + workItemId, e);
//		}
//	}
//
//	public void deleteWorkItem(int workItemId) {
//
//		try {
//			workItemRepository.deleteWorkItemById(workItemId);
//
//			cleanRelatedDataOnWorkItemDelete(workItemId);
//		} catch (RepositoryException e) {
//			throw new ServiceException("Could not delete WorkItem with id: " + workItemId, e);
//		}
//	}
//
//	public void addWorkItemToUser(int workItemId, int userId) {
//
//		try {
//			if (userIsActive(userId) && userHasSpaceForAdditionalWorkItem(workItemId, userId)) {
//				workItemRepository.addWorkItemToUser(workItemId, userId);
//			} else {
//				throw new ServiceException("Could not add work item to user, "
//						+ "either user is inactive or there is no space for additional work items");
//			}
//		} catch (RepositoryException e) {
//			throw new ServiceException("Could not add WorkItem " + workItemId + " to User " + userId, e);
//		}
//
//	}
//
//	public List<WorkItem> getWorkItemsByStatus(WorkItem.Status workItemStatus) {
//		try {
//			return workItemRepository.getWorkItemsByStatus(workItemStatus);
//		} catch (RepositoryException e) {
//			throw new ServiceException("Could not WorkItems with status " + workItemStatus, e);
//		}
//	}
//
//	public List<WorkItem> getWorkItemsByTeamId(int teamId) {
//		try {
//			return workItemRepository.getWorkItemsByTeamId(teamId);
//		} catch (RepositoryException e) {
//			throw new ServiceException("Could not get WorkItem connected to Team id " + teamId, e);
//		}
//	}
//
//	public List<WorkItem> getWorkItemsByUserId(int userId) {
//		try {
//			return workItemRepository.getWorkItemsByUserId(userId);
//		} catch (RepositoryException e) {
//			throw new ServiceException("Could not WorkItem connected to User id " + userId, e);
//		}
//	}
//
//	public List<WorkItem> getWorkItemsWithIssue() {
//		try {
//			return workItemRepository.getWorkItemsWithIssue();
//		} catch (RepositoryException e) {
//			throw new ServiceException("Could not WorkItems with Issues", e);
//		}
//	}

	// ISSUE

	@Transactional
	public Issue save(Issue issue) {
		if (workItemIsDone(issue.getWorkitem().getId())) {
			issueRepository.save(issue);
			WorkItem workItem = workItemRepository.findOne(issue.getWorkitem().getId());
			workItem.setIssue(issue).setStatus(Status.UNSTARTED);
			workItemRepository.save(workItem);
			return issue;
		} else {
			throw new ServiceException("WorkItem does not have status done");
		}
	}

	@Transactional
	public Issue updateIssueDescription(Long issueId, String description) {
		try {
			Issue issue = issueRepository.findById(issueId);
			issue.setDescription(description);
			return issueRepository.save(issue);
		} catch (Exception e) {
			throw new ServiceException("Could not change description of issue with id: " + issueId, e);
		}
	}

	@Transactional
	public Issue getIssue(Long id) {
		try {
			return issueRepository.findOne(id);
		} catch (Exception e) {
			throw new ServiceException("Could not find issue with id: " + id, e);
		}
	}

	@Transactional
	public Page<Issue> getAllIssues(int page, int size) {
		try {
			Page<Issue> issues = (Page<Issue>) issueRepository.findAll(new PageRequest(page, size));
			return issues;
		} catch (Exception e) {
			throw new ServiceException("Could not retrive issues", e);
		}
	}

	private boolean userFillsRequirements(User user) {
		if (!usernameLongEnough(user.getUsername())) {
			return false;
		}
		if (user.getTeam() != null && !teamHasSpaceForUser(user.getTeam().getId())) {
			return false;
		}
		return true;
	}

	private boolean usernameLongEnough(String username) {

		return username.length() >= 10;
	}

	private boolean teamHasSpaceForUser(Long teamId) {

		if (teamId == 0) {
			return true;
		}
		return numberOfUsersInTeamLessThanTen(teamId);
	}

	private boolean numberOfUsersInTeamLessThanTen(Long teamId) {
		List<User> users = userRepository.getUsersByTeamId(teamId);
		return users.size() < 10;
	}

//	private void setStatusOfAllWorkItemsOfUserToUnstarted(Long userId) throws RepositoryException {
//
//		List<WorkItem> workItems = workItemRepository.getWorkItemsByUserId(userId);
//		for (WorkItem workItem : workItems) {
//			workItemRepository.updateStatusById(workItem.getId(), WorkItem.Status.UNSTARTED);
//		}
//	}
//
//	private boolean userIsActive(Long userId) throws RepositoryException {
//
//		User user = userRepository.getUserById(userId);
//		return user.isActive();
//	}
//
//	private boolean userHasSpaceForAdditionalWorkItem(Long workItemId, Long userId) throws RepositoryException {
//
//		List<WorkItem> workItems = workItemRepository.getWorkItemsByUserId(userId);
//
//		if (workItems == null) {
//			return true;
//		}
//		for (WorkItem workItem : workItems) {
//			if (workItem.getId() == workItemId) {
//				return true;
//			}
//		}
//		return workItems.size() < 5;
//	}

	private boolean workItemIsDone(Long workItemId) {
		WorkItem workItem = workItemRepository.findOne(workItemId);
		return WorkItem.Status.DONE.equals(workItem.getStatus());
	}

//	private void cleanRelatedDataOnWorkItemDelete(Long workItemId) throws RepositoryException {
//		for (Issue issue : issueRepository.getIssuesByWorkItemId(workItemId))
//			issueRepository.removeById(issue.getId());
//	}

	private boolean isPersistedObject(AbstractEntity entity) {
		return entity.getId() != null;
	}

}
