package se.plushogskolan.casemanagement.service;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

		if (!userFillsRequirements(user)) {
			throw new ServiceException("Username is to short or team is full");
		}
		if (isPersistedObject(user)) {
			throw new ServiceException(String.format("User with id: %d already exists", user.getId()));
		}

		try {
			return userRepository.save(user);
		} catch (Exception e) {
			throw new ServiceException("User couldnt be saved : " + user.getUsername());
		}
	}

	@Transactional
	public User updateUserFirstName(Long userId, String firstName) {
		if (userRepository.exists(userId)) {
			try {
				User user = userRepository.findOne(userId);

				user.setFirstName(firstName);

				return userRepository.save(user);

			} catch (Exception e) {
				throw new ServiceException("User couldnt be updated");
			}
		} else {
			throw new ServiceException("User doesnt exist :" + userId);
		}
	}

	@Transactional
	public User updateUserLastName(Long userId, String lastName) {

		if (userRepository.exists(userId)) {
			try {
				User user = userRepository.findOne(userId);

				user.setLastName(lastName);

				return userRepository.save(user);

			} catch (Exception e) {
				throw new ServiceException("User couldnt be updated");
			}
		} else {
			throw new ServiceException("User doesnt exist :" + userId);
		}
	}

	@Transactional
	public User updateUserUsername(Long userId, String username) {

		if (!usernameLongEnough(username)) {
			throw new ServiceException("Username not long enogh!");
		}

		if (userRepository.exists(userId)) {
			try {

				User user = userRepository.findOne(userId);

				user.setUsername(username);

				return userRepository.save(user);

			} catch (/* MoreSpecific */Exception e) {
				throw new ServiceException("User couldnt be updated");
			}
		} else
			throw new ServiceException("User could not be updated");
	}

	@Transactional
	public User inactivateUser(Long userId) {

		if (userRepository.exists(userId)) {

			try {

				User user = userRepository.findOne(userId);

				user.setActive(false);

				setStatusOfAllWorkItemsOfUserToUnstarted(userId);

				return userRepository.save(user);

			} catch (Exception e) {
				throw new ServiceException("User couldnt be updated");
			}
		} else {
			throw new ServiceException("User doesnt exists :" + userId);
		}
	}

	@Transactional
	public User activateUser(Long userId) {

		if (userRepository.exists(userId)) {
			
			try {
				
				User user = userRepository.findOne(userId);
				
				user.setActive(true);
				
				return userRepository.save(user);
			} catch (Exception e) {
				throw new ServiceException("User could not be updated");
			}
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

	// // TEAM

	public Team save(Team team) {
		if (!isPersistedObject(team)) {
			try {
				return teamRepository.save(team);				
			} catch (Exception e) {
				throw new ServiceException("Team could not be saved");
			}
		} else {
			throw new ServiceException("Team already exsist");
		}
	}

	@Transactional
	public Team updateTeam(Long teamId, Team newValues) {
		if (teamRepository.exists(teamId)) {
			try {
				Team team = teamRepository.findOne(teamId);
				team.setActive(newValues.isActive()).setName(newValues.getName());
				return teamRepository.save(team);
			} catch (Exception e) {
				throw new ServiceException("Could not update Team");
			}
		} else {
			throw new ServiceException("Team does not exsist");
		}
	}

	@Transactional
	public Team inactivateTeam(Long teamId) {
		Team team = teamRepository.findOne(teamId);
		if (team.isActive() == true) {
			try {
				team.setActive(false);
				return teamRepository.save(team);
			} catch (Exception e) {
				throw new ServiceException("Team could not be inactivated");
			}
		} else {
			throw new ServiceException("Team is already inactive");
		}
	}

	@Transactional
	public Team activateTeam(Long teamId) {
		Team team = teamRepository.findOne(teamId);
		if (team.isActive() == false) {
			try {
				team.setActive(true);
				return teamRepository.save(team);
			} catch (Exception e) {
				throw new ServiceException("Team could not be activated");
			}
		} else {
			throw new ServiceException("Could not activate Team with id: " + teamId);
		}
	}

	public Team getTeam(Long teamId) {
		return teamRepository.findOne(teamId);
	}

	public Slice<Team> searchTeamByName(String name) {
		return teamRepository.findByNameContaining(name);
	}

	public Slice<Team> getAllTeams(PageRequest pageRequest) {
		return teamRepository.findAll(pageRequest);
	}

	@Transactional
	public void addUserToTeam(Long userId, Long teamId) {
		if (teamHasSpaceForUser(teamId)) {
			try {
				Team team = teamRepository.findOne(teamId);
				User user = userRepository.findOne(userId);
				team.addUser(user);
				teamRepository.save(team);
				//Has several possible exceptions probably
			} catch (Exception e) {
				throw new ServiceException("User could not be added to Team");
			}
		} else {
			throw new ServiceException("No space in team for user. userId = " + userId + "teamId = " + teamId);
		}
	}

	// WORKITEM

	public WorkItem save(WorkItem workItem) {
		if (isPersistedObject(workItem))
			throw new ServiceException("WorkItem already exists");
		try {
			return workItemRepository.save(workItem);
		} catch (Exception e) {
			throw new ServiceException("WorkItem could not be saved");
		}
	}

	public void updateStatusById(Long workItemId, WorkItem.Status workItemStatus) {
		if (workItemRepository.exists(workItemId))
			try {
				workItemRepository.updateStatusById(workItemId, workItemStatus);
			} catch (Exception e) {
				throw new ServiceException("This WorkItem could not be updated");
			}
		else
			throw new ServiceException("This WorkItem does not exist");
	}

	public void deleteWorkItem(Long workItemId) {
		if (workItemRepository.exists(workItemId)) {
			try {
				workItemRepository.delete(workItemId);
				cleanRelatedDataOnWorkItemDelete(workItemId);
			} catch (Exception e) {
				throw new ServiceException("WorkItem could not be deleted");
			}
		} else
			throw new ServiceException("This WorkItem does not exist");
	}

	public void addWorkItemToUser(Long workItemId, Long userId) {
		// PageRequest(0, 5) because if page 0 has 5 entries the method will
		// throw a ServiceException as desired
		if (userIsActive(userId) && userHasSpaceForAdditionalWorkItem(workItemId, userId, new PageRequest(0, 5))) {
			try {
				workItemRepository.addWorkItemToUser(workItemId, userId);				
			} catch (Exception e) {
				throw new ServiceException("Could not add WorkItem to User");
			}
		}
		else
			throw new ServiceException("User is either inactive or has no space for additional WorkItems");
	}
	
	public Slice<WorkItem> searchWorkItemByDescription(String description, Pageable pageable) {
		return workItemRepository.findByDescriptionContaining(description, pageable);
	}

	public Slice<WorkItem> getWorkItemsByStatus(WorkItem.Status workItemStatus, Pageable pageable) {
		try {
			return workItemRepository.getWorkItemsByStatus(workItemStatus, pageable);
		} catch (Exception e) {
			throw new ServiceException("Could not WorkItems with status " + workItemStatus, e);
		}
	}

	public Slice<WorkItem> getWorkItemsByTeamId(Long teamId, Pageable pageable) {
		try {
			return workItemRepository.getWorkItemsByTeamId(teamId, pageable);
		} catch (Exception e) {
			throw new ServiceException("Could not get WorkItem connected to Team id " + teamId, e);
		}
	}

	public Slice<WorkItem> getWorkItemsByUserId(Long userId, Pageable pageable) {
		try {
			return workItemRepository.findByUserId(userId, pageable);
		} catch (Exception e) {
			throw new ServiceException("Could not WorkItem connected to User id " + userId, e);
		}
	}

	public Slice<WorkItem> getWorkItemsWithIssue(Pageable pageable) {
		try {
			return workItemRepository.getWorkItemsWithIssue(pageable);
		} catch (Exception e) {
			throw new ServiceException("Could not WorkItems with Issues", e);
		}
	}

	// ISSUE

	@Transactional
	public Issue save(Issue issue) {
		if (workItemIsDone(issue.getWorkitem().getId())) {
			try {
				issueRepository.save(issue);
				WorkItem workItem = workItemRepository.findOne(issue.getWorkitem().getId());
				workItem.setIssue(issue).setStatus(Status.UNSTARTED);
				workItemRepository.save(workItem);
				return issue;
			} catch (Exception e) {
				throw new ServiceException("Issue could not be saved");
			}
		} else {
			throw new ServiceException("WorkItem does not have status done");
		}
	}

	@Transactional
	public Issue updateIssueDescription(Long issueId, String description) {
		if (issueRepository.exists(issueId)) {
			try {
				Issue issue = issueRepository.findOne(issueId);
				issue.setDescription(description);
				return issueRepository.save(issue);
			} catch (Exception e) {
				throw new ServiceException("Issue could not be updated");
			}
		} else {
			throw new ServiceException("Could not change description of issue with id: " + issueId);
		}
	}

	public Issue getIssue(Long id) {
		return issueRepository.findOne(id);
	}

	public Slice<Issue> getAllIssues(PageRequest pageRequest) {
		return issueRepository.findAll(pageRequest);
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
		
		return userRepository.countByTeamId(teamId) < 10;
	}

	private void setStatusOfAllWorkItemsOfUserToUnstarted(Long userId) {

		Slice<WorkItem> workItems = workItemRepository.findByUserId(userId, new PageRequest(0, 5));
		for (WorkItem workItem : workItems) {
			workItemRepository.updateStatusById(workItem.getId(), WorkItem.Status.UNSTARTED);
		}
	}

	private boolean userIsActive(Long userId) {

		User user = userRepository.findOne(userId);
		if (user.isActive())
			return true;
		else
			return false;
	}

	private boolean userHasSpaceForAdditionalWorkItem(Long workItemId, Long userId, Pageable pageable) {
		// TODO How should the PageRequest look?
		Slice<WorkItem> workItems = workItemRepository.findByUserId(userId, pageable);

		if (workItems == null) {
			return true;
		}
		for (WorkItem workItem : workItems) {
			if (workItem.getId() == workItemId) {
				return true;
			}
		}
		if (workItems.getSize() < 5)
			return true;
		else
			return false;
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
