package telran.java47.forum.service;

import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import telran.java47.dao.PostRepository;
import telran.java47.forum.dto.exceptions.PostNotFoundExceptions;
import telran.java47.forum.model.Comment;
import telran.java47.forum.model.Post;
import telran.java47.post.dto.DatePeriodDto;
import telran.java47.post.dto.NewCommentDto;
import telran.java47.post.dto.PostCreateDto;
import telran.java47.post.dto.PostDto;

@Service
@RequiredArgsConstructor
public class ForumServiceImpl implements ForumService {
	
	final PostRepository postRepository;
	final ModelMapper modelMapper;
	
	@Override
	public PostDto addPost(String authorString, PostCreateDto postCreateDto) {
		Post post = modelMapper.map(postCreateDto, Post.class);
		post.setAuthor(authorString);
		postRepository.save(post);
		return modelMapper.map(post, PostDto.class);
	}

	@Override
	public PostDto findPostByID(String id) {
		Post post = postRepository.findById(id).orElseThrow(() -> new PostNotFoundExceptions());
		return modelMapper.map(post, PostDto.class);
	}

	@Override
	public void addLike(String id) {
		Post post = postRepository.findById(id).orElseThrow(() -> new PostNotFoundExceptions());
		post.addLike();
		postRepository.save(post);
	}

	@Override
	public PostDto addComment(String id, String author, NewCommentDto c) {
		Post post = postRepository.findById(id).orElseThrow(() -> new PostNotFoundExceptions());
		Comment comment = modelMapper.map(c, Comment.class);
		comment.setUser(author);
		post.addComment(comment);
		postRepository.save(post);
		return modelMapper.map(post, PostDto.class);
	}

	@Override
	public PostDto removePostById(String id) {
		Post post = postRepository.findById(id).orElseThrow(() -> new PostNotFoundExceptions());
		postRepository.delete(post);
		return modelMapper.map(post, PostDto.class);
	}

	@Override
	public PostDto updatePost(String id, PostCreateDto postCreateDto) {
		Post post = postRepository.findById(id).orElseThrow(() -> new PostNotFoundExceptions());
		if(postCreateDto.getTitle() != null) {
			post.setTitle(postCreateDto.getTitle());
		}
		if(postCreateDto.getContent() != null) {
			post.setContent(postCreateDto.getContent());
		}
		if(postCreateDto.getTags() != null) {
			postCreateDto.getTags().stream()
				.forEach(t -> post.addTag(t));
		}
		postRepository.save(post);
		return modelMapper.map(post, PostDto.class);
	}

	@Override
	public Iterable<PostDto> findPostsByAuthor(String author) {
		return postRepository.findByAuthor(author)
				.map(p  -> modelMapper.map(p, PostDto.class))
				.collect(Collectors.toList());
	}

	@Override
	public Iterable<PostDto> findPostsByTags(List<String> tags) {
		return postRepository.findPostsByTags(tags)
				.map(p  -> modelMapper.map(p, PostDto.class))
				.collect(Collectors.toList());
	}

	@Override
	public Iterable<PostDto> findPostsByPeriod(DatePeriodDto datePeriodDto) {
		return postRepository.findPostsByDateCreatedBetween(datePeriodDto.getDateFrom(), datePeriodDto.getDateTo())
				.map(p  -> modelMapper.map(p, PostDto.class))
				.collect(Collectors.toList());
	}

}
