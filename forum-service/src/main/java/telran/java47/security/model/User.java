package telran.java47.security.model;

import java.security.Principal;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Getter;
import telran.java47.accounting.service.UserRole;

@AllArgsConstructor
public class User implements Principal{
	String userName;
	@Getter
	Set<UserRole> roles;

	@Override
	public String getName() {	
		return userName;
	}

}
