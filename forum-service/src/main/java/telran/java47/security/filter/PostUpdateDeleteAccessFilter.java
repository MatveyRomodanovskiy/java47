package telran.java47.security.filter;

import java.io.IOException;
import java.security.Principal;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import telran.java47.accounting.dao.UserAccountRepository;
import telran.java47.accounting.model.UserAccount;
import telran.java47.accounting.service.UserRole;
import telran.java47.dao.PostRepository;
import telran.java47.forum.dto.exceptions.PostNotFoundExceptions;
import telran.java47.forum.model.Post;

@Component
@RequiredArgsConstructor
@Order(60)
public class PostUpdateDeleteAccessFilter implements Filter {

	final UserAccountRepository userAccountRepository;
	final PostRepository postRepository;
	@Override
	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest request =  (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse)  resp;
		String path = request.getServletPath();
		String method = request.getMethod();
		if (checkEndPoint(method, path)) {
			Principal principal = request.getUserPrincipal();
			String[] arr = path.split("/");
			String postId =  arr[arr.length-1];
			Post post = postRepository.findById(postId).orElseThrow(() -> new PostNotFoundExceptions());
			String user = post.getAuthor();
			if(!principal.getName().equalsIgnoreCase(user)) {
				if(!("DELETE".equalsIgnoreCase(method) && isModerator(principal.getName()))) {
				response.sendError(403);
				return;
			}
		
		}
		
		}
		chain.doFilter(request, response);
	}

	private boolean isModerator(String name) {
		UserAccount userAccount = userAccountRepository.findById(name).get();
		return userAccount.getRoles().contains(UserRole.MODERATOR);
	}

	private boolean checkEndPoint(String method, String path) {
		boolean isDelete = "DELETE".equalsIgnoreCase(method)&&path.matches("/forum/post/\\w+/?");
		boolean isUpdate = "PUT".equalsIgnoreCase(method)&&path.matches("/forum/post/\\w+/?");
		return isDelete || isUpdate;
	}
}
