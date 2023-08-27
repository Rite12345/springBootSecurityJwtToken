package in.ritesh.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import in.ritesh.model.User;

public interface UserRepository extends JpaRepository<User,Integer> {
	Optional<User> findByUsername(String username);
	
	
	@Query(value="select * from riteshdb.user order By id",nativeQuery=true)
	List<User> finAll();


}
