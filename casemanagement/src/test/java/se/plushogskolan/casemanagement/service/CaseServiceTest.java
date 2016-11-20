package se.plushogskolan.casemanagement.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.time.LocalDate;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;

import se.plushogskolan.casemanagement.exception.ServiceException;
import se.plushogskolan.casemanagement.model.Issue;
import se.plushogskolan.casemanagement.model.Team;
import se.plushogskolan.casemanagement.model.User;
import se.plushogskolan.casemanagement.model.WorkItem;
import se.plushogskolan.casemanagement.repository.IssueRepository;
import se.plushogskolan.casemanagement.repository.TeamRepository;
import se.plushogskolan.casemanagement.repository.UserRepository;
import se.plushogskolan.casemanagement.repository.WorkItemRepository;

public class CaseServiceTest {
	
	private static CaseService service;
	private static AnnotationConfigApplicationContext context;
	private static UserRepository userRepository;
	private static TeamRepository teamRepository;
	private static IssueRepository issueRepository;
	private static WorkItemRepository workItemRepository;
	
	@BeforeClass
	public static void setUp() {
		
		try {
			context = new AnnotationConfigApplicationContext();
			context.scan("se.plushogskolan.casemanagement");
			context.refresh();
			service = context.getBean(CaseService.class);
			
			userRepository = context.getBean(UserRepository.class);
			teamRepository = context.getBean(TeamRepository.class);
			issueRepository = context.getBean(IssueRepository.class);
			workItemRepository = context.getBean(WorkItemRepository.class);
			
			UserRepository userRepository = context.getBean(UserRepository.class);
			TeamRepository teamRepository = context.getBean(TeamRepository.class);
			IssueRepository issueRepository = context.getBean(IssueRepository.class);
			WorkItemRepository workItemRepository = context.getBean(WorkItemRepository.class);
		
			System.out.printf("%d\n%d\n%d\n%d\n", userRepository.count(), teamRepository.count(),
					issueRepository.count(), workItemRepository.count());
			
			for(int i = 1; i <= 25; i++) {
				service.save(new User("username_" + i).setActive(true)
						.setFirstName("firstname" + i).setLastName("lastname" + i));
			}
			for(int i = 1; i <= 5; i++) {
				service.save(new Team("team" + i).setActive(true));
			}
			for(int i = 1; i <= 10; i++) {
				WorkItem.Status status;
				if(i <= 5)
					status = WorkItem.Status.UNSTARTED;
				else if(i <= 8)
					status = WorkItem.Status.STARTED;
				else
					status = WorkItem.Status.DONE;
				WorkItem issuesWorkItem = service.save(new WorkItem(String.format("%d%d%d description %d%d%d", i,i,i,i,i,i), status));
				if(i > 8) {
					service.save(new Issue(issuesWorkItem,
							String.format("%d%d%d issue description %d%d%d", i,i,i,i,i,i)));
				}
			}			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	//User
	
	@Test
	public void canSaveUser() {
		User userToSave = new User("UniqueUsername").setActive(true)
				.setFirstName("Unique").setLastName("User");
		User savedUser = service.save(userToSave);
		assertEquals(userToSave, savedUser);
	}
	
	@Test(expected = ServiceException.class)
	public void shouldThrowExceptionIfUsernameTooShort() {
		User usernameTooShort = new User("tooShort");
		service.save(usernameTooShort);
	}
	
	@Test
	public void canUpdateUserFirstName() {
		Long userId = 1L;
		User originalUser = service.getUser(userId);
		String newFirstName = "Guillermo";
		
		User updatedUser = service.updateUserFirstName(userId, newFirstName);
		originalUser.setFirstName(newFirstName);
		assertEquals(originalUser, updatedUser);
	}
	
	@Test(expected = ServiceException.class)
	public void shouldThrowExceptionIfUserDoesNotExist() {
		Long userId = 1000L;
		service.updateUserFirstName(userId, "Irrelevant");
	}
	
	@Test
	public void canUpdateUserLastName() {
		Long userId = 1L;
		User originalUser = service.getUser(userId);
		String newLastName = "Del Torro";
		
		User updatedUser = service.updateUserLastName(userId, newLastName);
		originalUser.setLastName(newLastName);
		assertEquals(originalUser, updatedUser);
	}
	
	@Test
	public void canUpdateUsername() {
		Long userId = 1L;
		User originalUser = service.getUser(userId);
		String newUsername = "newUsername";
		
		User updatedUser = service.updateUserUsername(userId, newUsername);
		originalUser.setUsername(newUsername);
		assertEquals(originalUser, updatedUser);
	}
	
	@Test
	public void canInactivateUser() {
		Long userId = 1L; // an active user
		User inactivatedUser = service.inactivateUser(userId);
		assertFalse(inactivatedUser.isActive());
	}
	
	@Test
	public void canActivateUser() {
		User inactiveUser = new User("inactiveUser").setActive(false);
		inactiveUser = service.save(inactiveUser);
		inactiveUser = service.activateUser(inactiveUser.getId());
		assertTrue(inactiveUser.isActive());
	}
	
	@Test
	public void canSearchUserByFirstName() {
		Long userId = 1L;
		User user = service.getUser(userId);
		Slice<User> slice = service.searchUsersByFirstName(user.getFirstName(), new PageRequest(0, 1));
		assertEquals(slice.getContent().get(0), user);
	}
	
	@Test
	public void canSearchUserByLastName() {
		Long userId = 1L;
		User user = service.getUser(userId);
		Slice<User> slice = service.searchUsersByLastName(user.getLastName(), new PageRequest(0, 1));
		assertEquals(slice.getContent().get(0), user);
	}
	
	@Test
	public void canSearchUserByUsername() {
		Long userId = 1L;
		User user = service.getUser(userId);
		Slice<User> slice = service.searchUsersByUsername(user.getUsername(), new PageRequest(0, 1));
		assertEquals(slice.getContent().get(0), user);
	}
	
	@Test
	public void canGetUsersByTeam() {
		Long userId = 1L;
		Long teamId = 26L;
		User userInTeam = service.addUserToTeam(userId, teamId);
		Slice<User> slice = service.getUsersByTeam(teamId, new PageRequest(0, 1));
		assertEquals(userInTeam, slice.getContent().get(0));
	}
	
	@Test
	public void canGetAllUsers() {
		Page<User> page = service.getAllUsers(new PageRequest(0, 50));
		assertEquals(page.getContent().size(), userRepository.count());
	}
	
	//Team
	
	@Test
	public void canSaveTeam() {
		Team teamToSave = new Team("theNewTeam");
		teamToSave = service.save(teamToSave);
		Team savedTeam = service.getTeam(teamToSave.getId());
		assertEquals(teamToSave, savedTeam);
	}
	
	@Test
	public void canUpdateTeam() {		
		Long teamId = 26L; //first teamId
		Team newTeam = new Team("newTeam");
		Team updatedTeam = service.updateTeam(teamId, newTeam);
		assertEquals(newTeam, updatedTeam);
	}
	
	@Test
	public void canInactivateTeam() {
		Team activeTeam = new Team("activeTeam").setActive(true);
		activeTeam = service.save(activeTeam);
		Team inactivatedTeam = service.inactivateTeam(activeTeam.getId());
		assertFalse(inactivatedTeam.isActive());
	}
	
	@Test
	public void canActivateTeam() {
		Team inactiveTeam = new Team("inactivaTeam").setActive(false);
		inactiveTeam = service.save(inactiveTeam);
		Team activatedTeam = service.activateTeam(inactiveTeam.getId());
		assertTrue(activatedTeam.isActive());
	}
	
	@Test
	public void canSearchTeamByName() {
		Long teamId = 26L;
		Team team = service.getTeam(teamId);
		Slice<Team> slice = service.searchTeamByName(team.getName(), new PageRequest(0, 1));
		assertEquals(team, slice.getContent().get(0));
	}
	
	@Test
	public void canGetAllTeams() {
		Page<Team> page = service.getAllTeams(new PageRequest(0, 10));
		assertEquals(page.getContent().size(), teamRepository.count());
	}
	
	@Test
	public void canAddUserToTeam() {
		Long teamId = 26L;
		Long userId = 1L;
		
		User userInTeam = service.addUserToTeam(userId, teamId);
		Team team = teamRepository.findOne(teamId);
		assertEquals(team, userInTeam.getTeam());
	}
	
	// WorkItem
	
	@Test
	public void canSaveWorkItem() {
		WorkItem workItemToSave = new WorkItem("new WorkItem", WorkItem.Status.UNSTARTED);
		workItemToSave = service.save(workItemToSave);
		WorkItem savedWorkItem = workItemRepository.findOne(workItemToSave.getId());
		assertEquals(workItemToSave, savedWorkItem);
	}
	
	@Test
	public void canUpdateStatusById() {
		Long workItemId = 31L; // first WorkItem id (unstarted)
		WorkItem updatedWorkItem = service.updateStatusById(workItemId, WorkItem.Status.STARTED);
		assertEquals(updatedWorkItem.getStatus(), WorkItem.Status.STARTED);
	}
	
	@Test
	public void canDeleteWorkItem() {
		WorkItem workItem = new WorkItem("This WorkItem should be deleted", WorkItem.Status.UNSTARTED);
		workItem = workItemRepository.save(workItem);
		service.deleteWorkItem(workItem.getId());
		assertNull(workItemRepository.findOne(workItem.getId()));
	}
	
	@Test
	public void canAddWorkItemToUser() {
		Long userId = 2L; // first active user id
		Long workItemId = 31L;
		
		WorkItem workItemInUser = service.addWorkItemToUser(workItemId, userId);
		User user = userRepository.findOne(userId);
		assertEquals(user, workItemInUser.getUser());
	}
	
	@Test
	public void canSearchWorkItemByDescription() {
		Long workItemId = 31L;
		WorkItem workItem = workItemRepository.findOne(workItemId);
		Slice<WorkItem> slice = service.searchWorkItemByDescription(workItem.getDescription(), new PageRequest(0, 1));
		assertEquals(workItem, slice.getContent().get(0));
	}

	@Test
	public void canGetWorkItemByStatus() {
		int unstartedCount = 0;
		int startedCount = 0;
		int doneCount = 0;
		Page<WorkItem> page = workItemRepository.findAll(new PageRequest(0, 15));
		for(WorkItem w: page) {
			if(w.getStatus().equals(WorkItem.Status.UNSTARTED))
				unstartedCount++;
			else if(w.getStatus().equals(WorkItem.Status.STARTED))
				startedCount++;
			else if(w.getStatus().equals(WorkItem.Status.DONE))
				doneCount++;
		}
		Slice<WorkItem> unstartedSlice = service.getWorkItemsByStatus(WorkItem.Status.UNSTARTED, new PageRequest(0, 10));
		Slice<WorkItem> startedSlice = service.getWorkItemsByStatus(WorkItem.Status.STARTED, new PageRequest(0, 15));
		Slice<WorkItem> doneSlice = service.getWorkItemsByStatus(WorkItem.Status.DONE, new PageRequest(0, 15));
		assertEquals(unstartedSlice.getContent().size(), unstartedCount);
		assertEquals(startedSlice.getContent().size(), startedCount);
		assertEquals(doneSlice.getContent().size(), doneCount);
	}
	
	@Test
	public void canGetWorkItemsByTeamId() {
		Team team = new Team("Team for workItems");
		team = service.save(team);
		
		Long userId1 = 3L;
		Long userId2 = 4L;
		Long userId3 = 5L;
		
		WorkItem workItem1 = new WorkItem("first WorkItem in team", WorkItem.Status.UNSTARTED);
		WorkItem workItem2 = new WorkItem("second WorkItem in team", WorkItem.Status.UNSTARTED);
		WorkItem workItem3 = new WorkItem("third WorkItem in team", WorkItem.Status.UNSTARTED);
		
		workItem1 = workItemRepository.save(workItem1);
		workItem2 = workItemRepository.save(workItem2);
		workItem3 = workItemRepository.save(workItem3);
		
		service.addWorkItemToUser(workItem1.getId(), userId1);
		service.addWorkItemToUser(workItem2.getId(), userId2);
		service.addWorkItemToUser(workItem3.getId(), userId3);
		
		service.addUserToTeam(userId1, team.getId());
		service.addUserToTeam(userId2, team.getId());
		service.addUserToTeam(userId3, team.getId());
		
		Slice<WorkItem> slice = service.getWorkItemsByTeamId(team.getId(), new PageRequest(0, 3));
		assertEquals(slice.getContent().get(0), workItem1);
		assertEquals(slice.getContent().get(1), workItem2);
		assertEquals(slice.getContent().get(2), workItem3);
	}
	
	@Test
	public void canGetWorkItemsByUserId() {
		Long userId1 = 6L;
		Long userId2 = 7L;
		Long workItemId1 = 31L;
		Long workItemId2 = 32L;
		
		WorkItem expectedWorkItem1 = service.addWorkItemToUser(workItemId1, userId1);
		WorkItem expectedWorkItem2 = service.addWorkItemToUser(workItemId2, userId2);
		
		Slice<WorkItem> slice1 = service.getWorkItemsByUserId(userId1, new PageRequest(0, 1));
		Slice<WorkItem> slice2 = service.getWorkItemsByUserId(userId2, new PageRequest(0, 1));
		
		assertEquals(expectedWorkItem1, slice1.getContent().get(0));
		assertEquals(expectedWorkItem2, slice2.getContent().get(0));
	}
	
	@Test
	public void canGetWorkItemsWithIssue() {				
		Long workItemId1 = 39L; // workItem with Issue
		Long workItemId2 = 41L; // workItem with Issue
		
		WorkItem workItemWithIssue1 = workItemRepository.findOne(workItemId1);
		WorkItem workItemWithIssue2 = workItemRepository.findOne(workItemId2);
		
		Slice<WorkItem> slice = service.getWorkItemsWithIssue(new PageRequest(0, 2));
		
		assertEquals(slice.getContent().get(0), workItemWithIssue1);
		assertEquals(slice.getContent().get(1), workItemWithIssue2);
	}
	
	@Test
	public void canGetAllWorkItems() {
		Page<WorkItem> page = service.getAllWorkItems(new PageRequest(0, 20));
		assertEquals(page.getContent().size(), workItemRepository.count());
	}
	
	@Test
	public void canGetAllWorkItemsBetweenDates() {
		// Every work item now changes to Status.DONE so getAllDoneWorkItemsBetween(now, now)
		// should equal repo.count() if method is correct
		workItemRepository.findAll().forEach(w -> service.updateStatusById(w.getId(), WorkItem.Status.DONE));
		assertEquals(workItemRepository.count(), service.getAllDoneWorkItemsBetween(LocalDate.now(), LocalDate.now()).size());
	}
	
	//Issue
	
	@Test
	public void canUpdateIssueDescription() {
		Long issueId = 40L;
		Issue originalIssue = service.getIssue(issueId);
		String newDescription = "Wow, much Issue";
		
		Issue updatedIssue = service.updateIssueDescription(issueId, newDescription);
		originalIssue.setDescription(newDescription);
		assertEquals(originalIssue, updatedIssue);
	}
	
	@Test
	public void canGetAllIssues() {
		Page<Issue> page = service.getAllIssues(new PageRequest(0, 10));
		assertEquals(page.getContent().size(), issueRepository.count());
	}
	
	@AfterClass
	public static void tearDown() {
		context.close();
	}
	
	
}
