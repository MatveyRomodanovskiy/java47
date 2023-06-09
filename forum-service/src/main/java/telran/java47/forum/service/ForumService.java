package telran.java47.forum.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import telran.java47.post.dto.CommentDto;
import telran.java47.post.dto.DatePeriodDto;
import telran.java47.post.dto.NewCommentDto;
import telran.java47.post.dto.PostCreateDto;
import telran.java47.post.dto.PostDto;

public interface ForumService {
	PostDto addPost(String authorString, PostCreateDto postCreateDto);
	PostDto findPostByID(String id);
	void addLike(String id);
	PostDto addComment(String id, String author, NewCommentDto commentDto);
	PostDto removePostById(String id);
	PostDto updatePost(String id, PostCreateDto postCreateDto);
	Iterable<PostDto> findPostsByAuthor(String author);
	Iterable<PostDto> findPostsByTags(List<String> tags);
	Iterable<PostDto> findPostsByPeriod(DatePeriodDto datePeriodDto);
}
