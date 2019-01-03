package com.bnguimgo.springbootrestserver.dao;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;

import java.util.List;

import static org.hamcrest.CoreMatchers.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import com.bnguimgo.springbootrestserver.model.User;

@RunWith(SpringRunner.class)//permet d'établir une liaison entre JUnit et Spring
@DataJpaTest
public class UserRepositoryTest {

	@Autowired
    private TestEntityManager entityManager;	
	@Autowired
    private UserRepository userRepository;	
	User user = new User("Dupont", "password", 1);
	
	@Before
	public void setup(){
	    entityManager.persist(user);//on sauvegarde l'objet user au début de chaque test
	    entityManager.flush();
	}
	@Test
	public void testFindAllUsers() {
	    List<User> users = userRepository.findAll();
	    assertThat(4, is(users.size()));//on a 3 Users dans le fichier d'initialisation data.sql et un utilisateur ajouté lors du setup du test
	}
	
	@Test
    public void testSaveUser(){
		User user = new User("Paul", "password", 1);
		User userSaved =  userRepository.save(user);
		assertNotNull(userSaved.getId());
	    assertThat("Paul", is(userSaved.getLogin()));
	}
	@Test
	public void testFindByLogin() {
	    User userFromDB = userRepository.findByLogin("user2");	 
	    assertThat("user2", is(userFromDB.getLogin()));//user2 a été crée lors de l'initialisation du fichier data.sql     
	}
	
	@Test
    public void testDeleteUser(){
		userRepository.delete(user.getId());
		User userFromDB = userRepository.findByLogin(user.getLogin());
		assertNull(userFromDB);
	}
	
	@Test
	public void testUpdateUser() {//Test si le compte utilisateur est désactivé
	    User userToUpdate = userRepository.findByLogin(user.getLogin());
	    userToUpdate.setActive(0);
	    userRepository.save(userToUpdate);	    
	    User userUpdatedFromDB = userRepository.findByLogin(userToUpdate.getLogin());
	    assertNotNull(userUpdatedFromDB);
	    assertThat(0, is(userUpdatedFromDB.getActive()));
	}		
}
