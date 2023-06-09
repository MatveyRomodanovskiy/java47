package telran.java47.forum.dto.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class PostNotFoundExceptions extends RuntimeException{

	
	private static final long serialVersionUID = -496641146129323977L;

	
}
