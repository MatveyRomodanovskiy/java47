package telran.java47.student.dao;

import java.util.List;
import java.util.stream.Stream;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import telran.java47.student.model.Student;

public interface StudentRepository extends MongoRepository<Student, Integer> {
	Stream<Student> findAllBy();
	
	Stream<Student> findByNameIgnoreCase(String name);
	
	long countByNameInIgnoreCase(List<String> names);
	
	@Query("{'scores.?0': {'$gte': ?1}}")
	Stream<Student> findByExamAndScoresGreaterThanEquals(String exam, int score);
}
