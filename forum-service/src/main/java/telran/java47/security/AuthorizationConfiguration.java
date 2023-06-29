package telran.java47.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

import telran.java47.accounting.service.UserRole;

import static org.springframework.security.config.Customizer.withDefaults;

import javax.security.sasl.AuthorizeCallback;

@Configuration
public class AuthorizationConfiguration {
	
	@Bean
	SecurityFilterChain configure(HttpSecurity http) throws Exception {
		http.httpBasic(withDefaults());
        http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.authorizeRequests(authorize -> authorize
        		.mvcMatchers("/account/register", "/forum/posts/**")
        			.permitAll()
        		.mvcMatchers("/account/user/{login}/role/{role}")
        			.access("hasRole(UserRole.ADMINISTRATOR.name()) and @customSecurity.isPasswordNonExpired(authentication.name)")
        		.mvcMatchers(HttpMethod.PUT, "/account/user/{login}")
        			.access("#login == authentication.name and @customSecurity.isPasswordNonExpired(authentication.name)")
//        		.mvcMatchers(HttpMethod.PUT, "/account/password")
//        			.access("@customSecurity.isPasswordNonExpired(authentication.name)")
        		.mvcMatchers(HttpMethod.DELETE, "/account/user/{login}")
        			.access("#login == authentication.name or hasRole(T(telran.java47.accounting.service.UserRole).ADMINISTRATOR.name())")
        		.mvcMatchers(HttpMethod.POST, "/forum/post/{author}")
        			.access("#author == authentication.name and @customSecurity.isPasswordNonExpired(authentication.name)")
        		.mvcMatchers(HttpMethod.PUT, "/post/{id}/comment/{author}")
        			.access("#author == authentication.name and @customSecurity.isPasswordNonExpired(authentication.name)")
        		.mvcMatchers(HttpMethod.PUT, "/post/{id}")
        			.access("@customSecurity.checkPostAuthor(#id, authentication.name) and @customSecurity.isPasswordNonExpired(authentication.name)")
        		.mvcMatchers(HttpMethod.DELETE, "/post/{id}")
        			.access("(@customSecurity.checkPostAuthor(#id, authentication.name) or hasRole(T(telran.java47.accounting.service.UserRole).MODERATOR.name())) and @customSecurity.isPasswordNonExpired(authentication.name)")
        		.anyRequest()
        			.access("@customSecurity.isPasswordNonExpired(authentication.name)")
        		);
		return http.build();
	}
}
