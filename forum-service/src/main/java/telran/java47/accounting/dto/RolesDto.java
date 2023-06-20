package telran.java47.accounting.dto;

import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Singular;
import telran.java47.accounting.service.UserRole;
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RolesDto {
	String login;
	@Singular
	Set<UserRole> roles;
}
