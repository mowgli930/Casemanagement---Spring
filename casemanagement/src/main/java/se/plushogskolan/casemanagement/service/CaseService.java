package se.plushogskolan.casemanagement.service;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
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
			throw new ServiceException(String.format("User with id: %d already exists", user.getId()));
		}
	}

	@Transactional
	public User updateUserFirstName(Long userId, String firstName) {

		if (userRepository.exists(userId)) {

			User user = userRepository.findOne(userId);

			user.setFirstName(firstName);

			return userRepository.save(user);

		} else {
			throw new ServiceException("User doesnt exist :" + userId);
		}
	}

	@Transactional
	public User updateUserLastName(Long userId, String lastName) {

		if (userRepository.exists(userId)) {
			User user = userRepository.findOne(userId);

			user.setLastName(lastName);

			return userRepository.save(user);

		} else {
			throw new ServiceException("User doesnt exist :" + userId);
		}
	}

	@Transactional
	public User updateUserUsername(Long userId, String username) {

		if (usernameLongEnough(username) && userRepository.exists(userId)) {

			User user = userRepository.findOne(userId);

			user.setUsername(username);

			return userRepository.save(user);

		} else {
			throw new ServiceException("User doesnt exist or username to long :" + userId);
		}
	}

	@Transactional
	public User inactivateUser(Long userId) {

		if (userRepository.exists(userId)) {

			User user = userRepository.findOne(userId);

			user.setActive(false);

			setStatusOfAllWorkItemsOfUserToUnstarted(userId);

			return userRepository.save(user);

		} else {
			throw new ServiceException("User doesnt exists :" + userId);
		}
	}

	@Transactional
	public User activateUser(Long userId) {

		if (userRepository.exists(userId)) {

			User user = userRepository.findOne(userId);

			user.setActive(true);

			return userRepository.save(user);

		} else {
			throw new ServiceException("User doesnt exists :" + userId);
		}
	}

	public User getUser(Long userId) {

		if (userRepository.exists(userId)) {
			return userRepository.findOne(userId);
		} else {
			throw new ServiceException("User doesnt exists :" + userId);
		}

	}

	public Slice<User> searchUsersByFirstName(String firstName, PageRequest page) {

		return userRepository.findByFirstNameContaining(firstName, page);
	}

	public Slice<User> searchUsersByLastName(String lastName, PageRequest page) {

		return userRepository.findByLastNameContaining(lastName, page);
	}

	public Slice<User> searchUsersByUsername(String username, PageRequest page) {

		return userRepository.findByUsernameContaining(username, page);
	}

	public Slice<User> getUsersByTeam(Long teamId, PageRequest page) {

		return userRepository.findByTeamId(teamId, page);
	}

	// TEAM

	public Team save(Team team) {

		if (!isPersistedObject(team)) {
			return teamRepository.save(team);
		} else {
			throw new ServiceException("Team already exsists: " + team.getName());
		}

	}

	@Transactional
	public Team updateTeam(Long teamId, Team newValues) {
		if (teamRepository.exists(teamId)) {
			Team team = teamRepository.findOne(teamId);
			team.setActive(newValues.isActive()).setName(newValues.getName());
			return teamRepository.save(team);
		} else {
			throw new ServiceException("Could not update Team with id " + newValues.getId());
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
	
	public Team getTeam(Long teamId){
		return teamRepository.findOne(teamId);
	}
	
	public Slice<Team> searchTeamByName(String name){
		return teamRepository.findByNameContaining(name);
	}

	public Slice<Team> getAllTeams(PageRequest pageRequest) {
		try {
			return teamRepository.findAll(pageRequest);
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

	public WorkItem save(WorkItem workItem) {
		try {
			return workItemRepository.save(workItem);
		} catch (Exception e) {
			throw new ServiceException("Could not save workItem: " + workItem.toString(), e);
		}
	}

	public void updateStatusById(Long workItemId, WorkItem.Status workItemStatus) {
		try {
			workItemRepository.updateStatusById(workItemId, workItemStatus);
		} catch (Exception e) {
			throw new ServiceException("Could not update status to: \"" + workItemStatus.toString()
					+ "\" on WorkItem with id: " + workItemId, e);
		}
	}

	public void deleteWorkItem(Long workItemId) {

		try {
			workItemRepository.delete(workItemId);

			cleanRelatedDataOnWorkItemDelete(workItemId);
		} catch (Exception e) {
			throw new ServiceException("Could not delete WorkItem with id: " + workItemId, e);
		}
	}

	public void addWorkItemToUser(Long workItemId, Long userId) {

		try {
			if (userIsActive(userId) && userHasSpaceForAdditionalWorkItem(workItemId, userId)) {
				workItemRepository.addWorkItemToUser(workItemId, userId);
			} else {
				throw new ServiceException(
						"Could not add work item to user, either user is inactive or there is no space for additional work items");
			}
		} catch (Exception e) {
			throw new ServiceException("Could not add WorkItem " + workItemId + " to User " + userId, e);
		}

	}

	public Slice<WorkItem> getWorkItemsByStatus(WorkItem.Status workItemStatus) {
		try {
			// TODO How should the PageRequest look?
			return workItemRepository.getWorkItemsByStatus(workItemStatus, new PageRequest(10, 10));
		} catch (Exception e) {
			throw new ServiceException("Could not WorkItems with status " + workItemStatus, e);
		}
	}

	public Slice<WorkItem> getWorkItemsByTeamId(Long teamId) {
		try {
			// TODO How should the PageRequest look?
			return workItemRepository.getWorkItemsByTeamId(teamId, new PageRequest(10, 10));
		} catch (Exception e) {
			throw new ServiceException("Could not get WorkItem connected to Team id " + teamId, e);
		}
	}

	public Slice<WorkItem> getWorkItemsByUserId(Long userId) {
		try {
			// TODO How should the PageRequest look?
			return workItemRepository.findByUserId(userId, new PageRequest(10, 10));
		} catch (Exception e) {
			throw new ServiceException("Could not WorkItem connected to User id " + userId, e);
		}
	}

	public Slice<WorkItem> getWorkItemsWithIssue() {
		try {
			// TODO How should the PageRequest look?
			return workItemRepository.getWorkItemsWithIssue(new PageRequest(10, 10));
		} catch (Exception e) {
			throw new ServiceException("Could not WorkItems with Issues", e);
		}
	}

	// ISSUE

	@Transactional
	public Issue save(Issue issue) {
		if (!workItemIsDone(issue.getWorkitem().getId())) {
			if (isPersistedObject(issue)) {
				throw new ServiceException("Issue already exsists");
			}
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
		if (issueRepository.exists(issueId)) {
			Issue issue = issueRepository.findOne(issueId);
			issue.setDescription(description);
			return issueRepository.save(issue);
		} else {
			throw new ServiceException("Could not change description of issue with id: " + issueId);
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

	public Slice<Issue> getAllIssues(PageRequest pageRequest) {
			return issueRepository.findAll(pageRequest);
	}

	private boolean userFillsRequirements(User user) {
		if (!usernameLongEnough(user.getUsername())) {
			throw new ServiceException("Username is too short.");
		}
		if (user.getTeam() != null && !teamHasSpaceForUser(user.getTeam().getId())) {
			throw new ServiceException("Team has no space for additional user");
		}
		return true;
	}

	private boolean usernameLongEnough(String username) {

		return username.length() >= 10;
	}

	private boolean teamHasSpaceForUser(Long teamId) {
		Slice<User> users = userRepository.findByTeamId(teamId, new PageRequest(0, 10));
		return users.getSize() < 10;
	}

<<<<<<< HEAD
	private void setStatusOfAllWorkItemsOfUserToUnstarted(Long userId) {
		
=======
	// TODO Unused method, should be removed?
	private void setStatusOfAllWorkItemsOfUserToUnstarted(Long userId) {

		// TODO How should the PageRequest look?
>>>>>>> 422791f97c851105f3340a921c01710ab7a507af
		Slice<WorkItem> workItems = workItemRepository.findByUserId(userId, new PageRequest(10, 10));
		for (WorkItem workItem : workItems) {
			workItemRepository.updateStatusById(workItem.getId(), WorkItem.Status.UNSTARTED);
		}
	}

	private boolean userIsActive(Long userId) {

		User user = userRepository.findOne(userId);
		return user.isActive();
	}

	private boolean userHasSpaceForAdditionalWorkItem(Long workItemId, Long userId) {
		// TODO How should the PageRequest look?
		Slice<WorkItem> workItems = workItemRepository.findByUserId(userId, new PageRequest(10, 10));

		if (workItems == null) {
			return true;
		}
		for (WorkItem workItem : workItems) {
			if (workItem.getId() == workItemId) {
				return true;
			}
		}
		return workItems.getSize() < 5;
	}

	private boolean workItemIsDone(Long workItemId) {
		WorkItem workItem = workItemRepository.findOne(workItemId);
		return WorkItem.Status.DONE.equals(workItem.getStatus());
	}

	private void cleanRelatedDataOnWorkItemDelete(Long workItemId) {
		for (Issue issue : issueRepository.getIssuesByWorkItemId(workItemId))
			issueRepository.delete(issue.getId());
	}

	private boolean isPersistedObject(AbstractEntity entity) {
		return entity.getId() != null;
	}

}
