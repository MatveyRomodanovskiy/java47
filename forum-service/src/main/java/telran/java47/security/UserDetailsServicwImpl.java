package telran.java47.security;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import telran.java47.accounting.dao.UserAccountRepository;
import telran.java47.accounting.model.UserAccount;

@Service
@RequiredArgsConstructor
public class UserDetailsServicwImpl implements UserDetailsService {

	final UserAccountRepository userAccountRepository;

	
	
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		UserAccount userAccount = userAccountRepository.findById(username)
				.orElseThrow(() -> new UsernameNotFoundException(username));
		String[] rolesList = userAccount.getRoles()
									.stream()
									.map(r -> "ROLE_" + r)
									.toArray(String[]::new);
//		boolean isCredentialsNonExpired = LocalDateTime.now().isBefore(userAccount.getPasswordExpireDateTime()); 
//		return new User(username, userAccount.getPassword(),true, true, isCredentialsNonExpired, true, AuthorityUtils.createAuthorityList(rolesList));
		return new User(username, userAccount.getPassword(), AuthorityUtils.createAuthorityList(rolesList));
		
	}

}
