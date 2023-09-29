package com.semanticsquare.thrillio.managers;

import com.semanticsquare.thrillio.constants.Gender;
import com.semanticsquare.thrillio.constants.UserType;
import com.semanticsquare.thrillio.dao.UserDao;
import com.semanticsquare.thrillio.entities.User;

import java.util.List;

public class UserManager {

	private static UserDao dao = new UserDao();
	
	private UserManager() {}
	
	private static class SingletonInstance{
		private static UserManager INSTANCE = new UserManager();
	}
	
	public static UserManager getInstance() {
		return SingletonInstance.INSTANCE;
	}
	
	public User createUser(long id, String email, String password, String firstName,
						   String lastName, Gender gender, UserType userType ) {
		
		User user = new User();
		user.setId(id);
		user.setEmail(email);
		user.setPassword(password);
		user.setFirstName(firstName);
		user.setLastName(lastName);
		user.setGender(gender);
		user.setUserType(userType);
		
		return user;
	}

	public List<User> getUsers() {
		return dao.getUsers();
	}

}
