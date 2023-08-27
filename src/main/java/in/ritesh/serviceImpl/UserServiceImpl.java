package in.ritesh.serviceImpl;

import java.util.Base64;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import in.ritesh.model.User;
import in.ritesh.repository.UserRepository;
import in.ritesh.service.IUserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

@Service
public class UserServiceImpl implements IUserService ,UserDetailsService {
  @Autowired
  private UserRepository userRepository;
  
  @Autowired
  private BCryptPasswordEncoder pwdEncoder;
	@Override
	public Integer saveUser(User user) {
		//EnCode password
		user.setPassword(
				pwdEncoder.encode(user.getPassword())
				);
		
		return userRepository.save(user).getId();
		 
	}
	
	//get user by username;
	@Override
	public Optional<User> findByUsername(String username) {
		
		return userRepository.findByUsername(username);
	}
	
	
	

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<User> opt=findByUsername(username);
		// read user from database
		User user=opt.get();
		if(opt.isEmpty()) throw new UsernameNotFoundException("user not exists");
		return new org.springframework.security.core.userdetails. User(
				username,
				user.getPassword(),
				user.getRoles().stream()
				.map(role->
				new SimpleGrantedAuthority(role))
					.collect(Collectors.toList())	
				);
				
	}

}
