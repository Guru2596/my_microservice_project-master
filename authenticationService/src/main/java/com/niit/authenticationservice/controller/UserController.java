package com.niit.authenticationservice.controller;

import java.util.List;
import java.util.Map;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.niit.authenticationservice.exception.UserNotFoundException;
import com.niit.authenticationservice.model.User;
import com.niit.authenticationservice.service.SecurityTokenGenerator;
import com.niit.authenticationservice.service.UserService;

@RestController
@RequestMapping("/api/v1")
public class UserController {

	@Autowired
	UserService userService;

	@Autowired
	SecurityTokenGenerator securityTokenGenerator;

	@PostMapping("/login")
	@HystrixCommand(fallbackMethod = "hystrixmethod",commandKey = "loginKey",groupKey = "login")
	@HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds",value = "1000")
	public ResponseEntity<?> userLogin(@RequestBody User user) throws UserNotFoundException {

		ResponseEntity<?> responseEntity;
		try {
			User newUser = userService.findByUsernameAndPassword(user.getUsername(), user.getPassword());
			if(newUser.getUsername().equals(user.getUsername())) {
				Map<String, String> tokenMap = securityTokenGenerator.generateToken(newUser);
				responseEntity = new ResponseEntity<>(tokenMap,HttpStatus.OK);
			} else {
				responseEntity = new ResponseEntity<>("Invalid User", HttpStatus.OK);
			}
			
		} catch (UserNotFoundException e) {
			throw new UserNotFoundException();
		} catch (Exception e) {
			responseEntity = new ResponseEntity<>("Some other error occured!!!", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return responseEntity;
	}

	public ResponseEntity<?> hystrixmethod(@RequestBody User user) throws UserNotFoundException{
		String alart = "Server is unable to take the request, Now please try after some time";
		return new ResponseEntity<>(alart,HttpStatus.BAD_REQUEST);
	}
	
	@PostMapping("/register")
	public ResponseEntity<?> registerUser(@RequestBody User user) {
		userService.registerUser(user);
		return new ResponseEntity<>("User created !", HttpStatus.CREATED);
	}
	
	@GetMapping("/userdetails/users")
	public ResponseEntity<List<User>> getAllUsers() {
		return new ResponseEntity<>(userService.getAllUsers(), HttpStatus.OK);
	}
	
	
}
