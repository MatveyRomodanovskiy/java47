package telran.java47.security.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import telran.java47.accounting.dao.UserAccountRepository;
import telran.java47.accounting.service.UserRole;
import telran.java47.security.model.User;

@Component
@RequiredArgsConstructor
@Order(40)
public class DeleteUserFilter implements Filter {

		final UserAccountRepository userAccountRepository;
		
	@Override
	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
			throws IOException, ServletException {
		
		HttpServletRequest request =  (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse)  resp;
		String path = request.getServletPath();
		
		if (checkEndPoint(request.getMethod(), path)) {
			String[] arr = path.split("/");
			String userName =  arr[arr.length-1];
			User  user = (User) request.getUserPrincipal();
			if(!(user.getName().equalsIgnoreCase(userName) 
					|| user.getRoles().contains(UserRole.ADMINISTRATOR))) {
				response.sendError(403);
				return;
			}
		
		}
		
		
		chain.doFilter(request, response);
	}

	private boolean checkEndPoint(String method, String path) {
		return HttpMethod.DELETE.toString().equals(method)&&path.matches("/account/user/\\w+/?");
	}

}
