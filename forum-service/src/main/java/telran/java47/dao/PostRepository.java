package telran.java47.dao;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import telran.java47.forum.model.Post;



public interface PostRepository extends MongoRepository<Post, String> {
	Stream<Post> findByAuthor(String author);
	@Query("{'tags' : { '$in' : ?0}}")
	Stream<Post> findPostsByTags(List<String> tags);
	Stream<Post> findPostsByDateCreatedBetween(LocalDate startDate, LocalDate endDate);
}
