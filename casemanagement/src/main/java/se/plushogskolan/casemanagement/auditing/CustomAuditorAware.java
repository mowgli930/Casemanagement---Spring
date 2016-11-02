package se.plushogskolan.casemanagement.auditing;

import org.springframework.data.domain.AuditorAware;
import static se.plushogskolan.casemanagement.properties.PropertyReader.readProperty;

public class CustomAuditorAware implements AuditorAware<String> {

	@Override
	public String getCurrentAuditor() {
		return readProperty("name");
	}

}
