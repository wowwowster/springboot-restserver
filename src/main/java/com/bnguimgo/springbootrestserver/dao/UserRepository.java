package com.bnguimgo.springbootrestserver.dao;
import org.springframework.data.jpa.repository.JpaRepository;

import com.bnguimgo.springbootrestserver.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
	
	User findByLogin(String login);
	
}
