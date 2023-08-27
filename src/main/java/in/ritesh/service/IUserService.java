package in.ritesh.service;

import java.util.Optional;

import in.ritesh.model.User;

public interface IUserService {
	Integer saveUser(User user);
	Optional<User> findByUsername(String username);

}
