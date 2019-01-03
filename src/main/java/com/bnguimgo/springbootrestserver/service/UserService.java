package com.bnguimgo.springbootrestserver.service;
import java.util.Collection;

import com.bnguimgo.springbootrestserver.exception.BusinessResourceException;
import com.bnguimgo.springbootrestserver.model.User;

public interface UserService {

	Collection<User> getAllUsers();
	
	User getUserById(Long id) throws BusinessResourceException;
	
	User findByLogin(String login) throws BusinessResourceException;
	
	User saveOrUpdateUser(User user) throws BusinessResourceException;
	
	void deleteUser(Long id) throws BusinessResourceException;
	
}