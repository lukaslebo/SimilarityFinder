package ch.propulsion.similarityfinder.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ch.propulsion.similarityfinder.domain.User;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
	
	User findById(String id);
	List<User> findAll();
	List<User> findByExpirationDateBefore(LocalDateTime comparisonDate);
	void deleteById(String id);
	
}
