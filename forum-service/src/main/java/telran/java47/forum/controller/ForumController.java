package telran.java47.forum.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import telran.java47.forum.service.ForumService;
import telran.java47.post.dto.CommentDto;
import telran.java47.post.dto.DatePeriodDto;
import telran.java47.post.dto.NewCommentDto;
import telran.java47.post.dto.PostCreateDto;
import telran.java47.post.dto.PostDto;

@RestController
@RequiredArgsConstructor
@RequestMapping("/forum")
public class ForumController {
	
	final ForumService forumService;

	@PostMapping("/post/{author}")
	public PostDto addPost(@RequestBody PostCreateDto postCreateDto, @PathVariable String author) {
		return forumService.addPost(author, postCreateDto);
	}

	@GetMapping("/post/{id}")
	public PostDto findPostByID(@PathVariable String id) {
		return forumService.findPostByID(id);
	}

	@PutMapping("/post/{id}/like")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void addLike(@PathVariable String id) {
		forumService.addLike(id);
		
	}

	@GetMapping("/posts/author/{author}")
	public Iterable<PostDto> findPostsByAuthor(@PathVariable String author) {
		return forumService.findPostsByAuthor(author);
	}

	@PutMapping("/post/{id}/comment/{author}")
	public PostDto addComment(@PathVariable String id, @PathVariable String author, @RequestBody NewCommentDto newCommentDto) {
		return forumService.addComment(id, author, newCommentDto);
	}

	@DeleteMapping("/post/{id}")
	public PostDto removePostById(@PathVariable String id) {
		return forumService.removePostById(id);
	}

	@PostMapping("/posts/tags")
	public Iterable<PostDto> findPostsByTags(@RequestBody List<String> tags) {
		return forumService.findPostsByTags(tags);
	}

	@PostMapping("/posts/period")
	public Iterable<PostDto> findPostsByPeriod(@RequestBody DatePeriodDto datePeriodDto) {
		return forumService.findPostsByPeriod(datePeriodDto);
	}

	@PutMapping("/post/{id}")
	public PostDto updatePost(@PathVariable String id, @RequestBody PostCreateDto postCreateDto) {
		return forumService.updatePost(id, postCreateDto);
	}

}
