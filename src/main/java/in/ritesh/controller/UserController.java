package in.ritesh.controller;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import in.ritesh.model.User;
import in.ritesh.model.UserRequest;
import in.ritesh.model.UserResponse;
import in.ritesh.repository.UserRepository;
import in.ritesh.service.IUserService;
import in.ritesh.util.JwtUtil;
import io.jsonwebtoken.Claims;

@RestController
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	private UserRepository userRepository;
	
	
	@Autowired
	private IUserService service;
	
	@Autowired
	private JwtUtil util;
	
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	// 1 save user data in database
	
	@PostMapping("/save")
	public ResponseEntity<String> saveUser(@RequestBody User user)
	{
	Integer id=	service.saveUser(user);
	String body="User"+id+"save";
	return new ResponseEntity<String>(body,HttpStatus.OK);
	
	//return ResponseEntity.ok(body);
		
	}
	
	// 2 validate user and generate token(login)
	@PostMapping("/login")
	public ResponseEntity<UserResponse> loginUser(@RequestBody UserRequest request)
	{
		//validate username and password with database
		authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(),request.getPassword()));
		 
		String token=util.generateToken(request.getUsername());
		
		return ResponseEntity.ok(new UserResponse(token,"success Generate by Ritesh..."));
	}
	
	
	//2 after login only
	@PostMapping("/welcome")
	public ResponseEntity<String> accessData(Principal p)
	{
		return ResponseEntity.ok("Hello User"+p.getName());
	}
	
	
	@GetMapping("/getDetails")
	public ResponseEntity<?> getAll()
	{
		List<User> all = userRepository.findAll();
		if(all.size()>0)
		{
			HashMap<String,List<User>> map=new HashMap<String,List<User>>();
			map.put("Users", all);
			return ResponseEntity.ok(map);
		}else {
			return ResponseEntity.ok("Data not found");
		}
	
	
	}
	@PostMapping("/token")
	public ResponseEntity<?>  getToken(HttpServletRequest req)
	{
	   String token = req.getHeader("Authorization");
	   System.out.println(token);
	   Claims c = util.getClaims(token);
	   c.getSubject();
	   
	   return ResponseEntity.ok("ok"+c);
	  
	}
	
}
