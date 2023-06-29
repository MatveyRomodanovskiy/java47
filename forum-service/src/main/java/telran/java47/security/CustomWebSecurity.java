package telran.java47.security;

import java.time.LocalDateTime;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import telran.java47.accounting.dao.UserAccountRepository;
import telran.java47.accounting.model.UserAccount;
import telran.java47.dao.PostRepository;
import telran.java47.forum.model.Post;

@Service("customSecurity")
@RequiredArgsConstructor
public class CustomWebSecurity {
	final PostRepository postRepository;
	final UserAccountRepository userAccountRepository;
	
	public boolean checkPostAuthor(String postId, String userName) {
		Post post = postRepository.findById(postId).orElse(null);
		return post != null && userName.equalsIgnoreCase(post.getAuthor());
	}
	
	public boolean isPasswordNonExpired(String username) {
		UserAccount userAccount = userAccountRepository.findById(username)
			.orElseThrow(() -> new UsernameNotFoundException(username));
		return 	LocalDateTime.now().isBefore(userAccount.getPasswordExpireDateTime()); 
		
	}

}
