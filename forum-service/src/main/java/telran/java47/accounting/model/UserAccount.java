package telran.java47.accounting.model;

import java.time.LocalDateTime;
import java.util.HashSet;
import telran.java47.accounting.service.UserRole;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;

import lombok.Setter;

@Getter
@Document(collection = "users")
public class UserAccount {
	@Id
	String login;
	@Setter
	String password;
	@Setter
	LocalDateTime passwordExpireDateTime;
	@Setter
	String firstName;
	@Setter
	String lastName;
	HashSet<UserRole> roles;
	
		
	public UserAccount() {
		roles = new HashSet<UserRole>();
	}

	public UserAccount(String login, String password, String firstName, String lastName) {
		this();
		this.login = login;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
	}

	public boolean addRole(UserRole role) {
		return roles.add(role);
	}

	public boolean removeRole(UserRole role) {
		return roles.remove(role);
	}

}
