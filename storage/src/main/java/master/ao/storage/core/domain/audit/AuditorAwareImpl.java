package master.ao.storage.core.domain.audit;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

public class AuditorAwareImpl implements AuditorAware<String> {
	@Override
	public Optional<String> getCurrentAuditor() {
		String loggedInUsername = SecurityContextHolder.getContext().getAuthentication().getName();
		return Optional.of(loggedInUsername);
	}
}
