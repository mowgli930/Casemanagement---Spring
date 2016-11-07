import java.util.Arrays;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;

import se.plushogskolan.casemanagement.model.Issue;
import se.plushogskolan.casemanagement.model.Team;
import se.plushogskolan.casemanagement.model.User;
import se.plushogskolan.casemanagement.model.WorkItem;
import se.plushogskolan.casemanagement.repository.IssueRepository;
import se.plushogskolan.casemanagement.repository.TeamRepository;
import se.plushogskolan.casemanagement.repository.UserRepository;
import se.plushogskolan.casemanagement.repository.WorkItemRepository;

public class Main {
	
	public static void main(String[] args) {
		
		try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext()) {
			context.scan("se.plushogskolan.casemanagement");
			context.refresh();

			UserRepository userRepository = context.getBean(UserRepository.class);
			TeamRepository teamRepository = context.getBean(TeamRepository.class);
			WorkItemRepository workItemRepository = context.getBean(WorkItemRepository.class);
			IssueRepository issueRepository = context.getBean(IssueRepository.class);
			Team team = Team.builder().setActive(true).build("theTeam");
			teamRepository.save(team);
			Issue issue = Issue.builder(null).setDescription("someone else fucked up").build();
			issue = issueRepository.save(issue);
			
			WorkItem workItem = WorkItem.builder().setDescription("heheh").setStatus(WorkItem.Status.DONE)
					.setIssue(issue).build();
			workItemRepository.save(workItem);
			
			Slice<WorkItem> workItems = workItemRepository.getWorkItemsWithIssue(new PageRequest(0, 10));
			workItems.forEach(wi -> System.out.println(wi.getId()));
//			Collection<WorkItem> workItems = Arrays.asList(workItem);
			User user = User.builder().setActive(true).setFirstName("Analking").setLastName("Skywalker")
					.setTeam(team).setWorkItems(Arrays.asList(workItem)).build("username2");
			userRepository.save(user);
			
			
//			Page<User> result = userRepository.findAll(new PageRequest(0, 5));
//			result.forEach(System.out::println);
		}
	
	}

}
