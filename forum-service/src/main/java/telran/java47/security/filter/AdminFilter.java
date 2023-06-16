package telran.java47.security.filter;

import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.Set;

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
import telran.java47.accounting.dto.exceptions.UserNotFoundException;
import telran.java47.accounting.model.UserAccount;
import telran.java47.dao.PostRepository;
import telran.java47.forum.dto.exceptions.PostNotFoundExceptions;
import telran.java47.forum.model.Post;

@Component
@RequiredArgsConstructor
@Order(20)
public class AdminFilter implements Filter {

	final PostRepository postRepository;
	final UserAccountRepository userAccountRepository;
	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest request =  (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse)  res;
		if (checkAllUsersAccess(request.getMethod(), request.getServletPath())) {
		if (checkAllAuthenticatedAccess(request.getMethod(), request.getServletPath())) {
			if(checkAuthor(request.getMethod(), request.getServletPath(), request.getUserPrincipal().toString())) {
				try {
					if (("PUT".equalsIgnoreCase(request.getMethod()) || "DELETE".equalsIgnoreCase(request.getMethod()))) {
						throw new Exception("Invalid method or endpoint");
					}
					checkUserAccountManipulation(request.getServletPath(), request.getUserPrincipal().toString());
					checkPostsManipulation(request.getMethod(), request.getServletPath(), request.getUserPrincipal().toString());		
				} catch (Exception e) {
					//response.sendError(401, e.getMessage());
				}
			}
			}
		}
		chain.doFilter(request, response);
	}

	private void checkPostsManipulation(String method, String path, String login) throws Exception {
		UserAccount userAccount = userAccountRepository.findById(login).orElseThrow(UserNotFoundException::new);
		if (!path.matches("/forum/post/(.*)")) {
			throw new Exception("Invalid method or endpoint");
		} 
		boolean adminFlag = userAccount.getRoles().stream()
				.anyMatch(r -> r.equalsIgnoreCase("Moderator"));
		String postId = path.substring(12);
			Post post = postRepository.findById(postId).orElseThrow(() -> new PostNotFoundExceptions());
			String postAuthor = post.getAuthor();
			if (!postAuthor.equalsIgnoreCase(login) || !(adminFlag && "DELETE".equalsIgnoreCase(method))) {
				throw new Exception("Access denied");
			}
	}

	private void checkUserAccountManipulation(String path, String login) throws Exception {
		UserAccount userAccount = userAccountRepository.findById(login).orElseThrow(UserNotFoundException::new);
		boolean adminFlag = userAccount.getRoles().stream()
				.anyMatch(r -> r.equalsIgnoreCase("Administrator"));
		if (path.matches("/account/user/(.*)/role/(.*)")){
			if (!adminFlag) {
				throw new Exception("Access denied");		
			}
		}
		if (path.matches("/account/user/(.*)") && (!adminFlag || !path.substring(14).equalsIgnoreCase(login))){
			throw new Exception("Access denied");
		}
	}

	private boolean checkAuthor(String method, String path, String login) {
		//endpoint: /forum/posts/ && method: post or endpoint:/post/{id}/comment/{author}, method: put
		String truePathPostString = "/forum/post/" + login.toLowerCase() + "/?";
		String truePathCommentString = "/post/(.*)/comment/" + login;
		return !(("POST".equalsIgnoreCase(method) && path.toLowerCase().matches(truePathPostString)) ||
				("PUT".equalsIgnoreCase(method) && path.matches(truePathCommentString)));
	}

	private boolean checkAllAuthenticatedAccess(String method, String path) {
		//endpoint:  /account/login/  method: post
		if ("POST".equalsIgnoreCase(method) && path.matches("/account/login/?")) {
			return false;
		};
		
		// endpoint: /forum/post/, method: get
		if("GET".equalsIgnoreCase(method) && path.substring(0,12).equalsIgnoreCase("/forum/post/")){
			return false;
		}
		
		// endpoint: /account/password/, method: put
		if("PUT".equalsIgnoreCase(method) && path.matches("/account/password/?")){
			return false;
		}
		
		// endpoint: /account/user/, method: get
		if("GET".equalsIgnoreCase(method) && path.substring(0,14).matches("/account/user/")){
			return false;
		}
		
		// endpoint: /forum/(.*)/like/, method: put
		if("PUT".equalsIgnoreCase(method) && path.matches("/forum/post/(.*)/like/?")) {
			return false;
		}
		return true;
	}
		private boolean checkAllUsersAccess(String method, String path) {
			//endpoint: /forum/posts/ method: post, get
		if (("POST".equalsIgnoreCase(method) || "GET".equalsIgnoreCase(method)) && path.matches("/forum/posts/(.*)")) {
				return false;
		};
		return !("POST".equalsIgnoreCase(method) && path.matches("/account/register/?"));
	}

}
