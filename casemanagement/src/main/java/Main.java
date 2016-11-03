import java.util.Arrays;
import java.util.Collection;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

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
			WorkItem workItem = WorkItem.builder().setDescription("heheh").setStatus(WorkItem.Status.DONE)
					.build();
			Collection<WorkItem> workItems = Arrays.asList(workItem);
			User user = User.builder().setActive(true).setFirstName("Analking").setLastName("Skywalker")
					.setTeam(team).setWorkItems(workItems).build("username");
			userRepository.save(user);
			Page<User> result = userRepository.findAll(new PageRequest(0, 5));
			result.forEach(System.out::println);
		}
	
	}

}
