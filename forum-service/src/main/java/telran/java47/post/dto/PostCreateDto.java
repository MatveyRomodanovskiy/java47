package telran.java47.post.dto;

import java.util.Set;

import lombok.Getter;

@Getter
public class PostCreateDto {
	String title;
	String content;
	Set<String> tags; 
}