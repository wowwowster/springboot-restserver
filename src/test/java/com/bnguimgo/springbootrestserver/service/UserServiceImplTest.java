package com.bnguimgo.springbootrestserver.service;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import com.bnguimgo.springbootrestserver.dao.UserRepository;
import com.bnguimgo.springbootrestserver.model.Role;
import com.bnguimgo.springbootrestserver.model.User;

@RunWith(SpringRunner.class)
public class UserServiceImplTest {
 
    @TestConfiguration //création des Beans nécessaires pour les tests
    static class UserServiceImplTestContextConfiguration {
    	
        @Bean//bean de service
        public UserService userService () {
            return new UserServiceImpl();
        }
        
    	@Bean//nécessaire pour encrypter le mot de passe sinon échec des tests
    	public BCryptPasswordEncoder passwordEncoder() {
    		BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
    		return bCryptPasswordEncoder;
    	}
    }
 
    @Autowired
    private UserService userService;
 
    @MockBean //création d’un mock Bean pour UserRepository
    private UserRepository userRepository;
    
    User user = new User("Dupont", "password", 1);
    
    @Test
    public void testFindAllUsers() throws Exception {
    	User user = new User("Dupont", "password", 1);
        Role role = new Role("USER_ROLE");//initialisation du role utilisateur
        Set<Role> roles = new HashSet<>();
    	roles.add(role);
    	user.setRoles(roles);
    	List<User> allUsers = Arrays.asList(user);           
        Mockito.when(userRepository.findAll()).thenReturn(allUsers);
    	Collection<User> users = userService.getAllUsers();
    	assertNotNull(users);
    	assertEquals(users, allUsers);
    	assertEquals(users.size(), allUsers.size());
    	verify(userRepository).findAll();
    }
    
    @Test
    public void testSaveUser() throws Exception {
    	User user = new User("Dupont", "password", 1);
    	User userMock = new User(1L,"Dupont", "password", 1);
    	Mockito.when(userRepository.save((user))).thenReturn(userMock);
    	User userSaved = userService.saveOrUpdateUser(user);
    	assertNotNull(userSaved);
    	assertEquals(userMock.getId(), userSaved.getId());
     	assertEquals(userMock.getLogin(), userSaved.getLogin());
     	verify(userRepository).save(any(User.class));
    }
    
    @Test
    public void testFindUserByLogin() {
    	User user = new User("Dupont", "password", 1);
    	Mockito.when(userRepository.findByLogin(user.getLogin())).thenReturn(user);
        User userFromDB = userService.findByLogin(user.getLogin());  
        assertNotNull(userFromDB);
        assertThat(userFromDB.getLogin(), is(user.getLogin()));  
        verify(userRepository).findByLogin(any(String.class));
     }
    
    @Test
    public void testDelete() throws Exception {
    	User user = new User("Dupont", "password", 1);
    	User userMock = new User(1L,"Dupont", "password", 1);
    	Mockito.when(userRepository.save((user))).thenReturn(userMock);
    	User userSaved = userService.saveOrUpdateUser(user);
    	assertNotNull(userSaved);
    	assertEquals(userMock.getId(), userSaved.getId());
    	userService.deleteUser(userSaved.getId());
    	verify(userRepository).delete(any(Long.class));
    }
    
    @Test
    public void testUpdateUser() throws Exception {
    	User userToUpdate = new User(1L,"Dupont", "password", 1);
    	User userUpdated = new User(1L,"Paul", "password", 1);
    	Mockito.when(userRepository.save((userToUpdate))).thenReturn(userUpdated);
    	User userFromDB = userService.saveOrUpdateUser(userToUpdate);
    	assertNotNull(userFromDB);
    	assertEquals(userUpdated.getLogin(), userFromDB.getLogin());
    	verify(userRepository).save(any(User.class));    	
    }    
}
