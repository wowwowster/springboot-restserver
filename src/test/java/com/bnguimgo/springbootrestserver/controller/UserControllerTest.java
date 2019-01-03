package com.bnguimgo.springbootrestserver.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
//for HTTP methods
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//for JSON
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//for HTTP status
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.bnguimgo.springbootrestserver.model.Role;
import com.bnguimgo.springbootrestserver.model.User;
import com.bnguimgo.springbootrestserver.service.RoleService;
import com.bnguimgo.springbootrestserver.service.UserService;

@RunWith(SpringRunner.class)
@WebMvcTest(UserController.class)
public class UserControllerTest {

	@Autowired
    private MockMvc mockMvc;
 
    @MockBean
    private UserService userService;
    @MockBean
    private RoleService roleService;
    
    User user = new User(1L,"Dupont", "password", 1);
    @Before
    public void setUp() { 
    	//Initialisation du setup avant chaque test
        Role role = new Role("USER_ROLE");//initialisation du role utilisateur
        Set<Role> roles = new HashSet<>();
    	roles.add(role);
    	user.setRoles(roles);
    	List<User> allUsers = Arrays.asList(user);
    	
        //Mockito.when(service.getAllUsers()).thenReturn(allUsers);// Mock de la couche de service
        
    	// Mock de la couche de service
        given(userService.getAllUsers()).willReturn(allUsers);
        when(userService.getUserById(any(Long.class))).thenReturn(user);        
        when(userService.saveOrUpdateUser(any(User.class))).thenReturn(user);
        when(roleService.getAllRolesStream()).thenReturn(roles.stream());
        
    }
    
    @Test
    public void testFindAllUsers() throws Exception {
     
    	MvcResult result = mockMvc.perform(get("/user/users")
          .contentType(MediaType.APPLICATION_JSON))
          .andExpect(status().isFound())	//statut HTTP de la réponse
          .andExpect(jsonPath("$", hasSize(1)))
          .andExpect(jsonPath("$[0].login", is(user.getLogin())))
          .andExpect(jsonPath("$[0].password", is(user.getPassword())))
          .andExpect(jsonPath("$[0].active", is(user.getActive())))
          .andReturn();
    	
    	// ceci est une redondance, car déjà vérifié par: isCreated())
    	assertEquals("Réponse incorrecte", HttpStatus.FOUND.value(), result.getResponse().getStatus());
    	
    	//on s'assure que la méthode de service getAllUsers() a bien été appelée
    	verify(userService).getAllUsers();
    }
    
    @Test
    public void testSaveUser() throws Exception {

    	given(userService.findByLogin("Dupont")).willReturn(null);
    	//on execute la requête
    	mockMvc.perform(MockMvcRequestBuilders.post("/user/users")
		  .contentType(MediaType.APPLICATION_XML)
		  .accept(MediaType.APPLICATION_XML)
		  .content("<user><login>Dupont</login><password>password</password><active>1</active></user>"))
		  .andExpect(status().isCreated());
     
    	//on s'assure que la méthode de service saveOrUpdateUser(User) a bien été appelée
    	verify(userService).saveOrUpdateUser(any(User.class));
     
    }
    
    @Test
    public void testFindUserByLogin() throws Exception {
    	given(userService.findByLogin("Dupont")).willReturn(user);
    	//on execute la requête
    	mockMvc.perform(get("/user/users/{loginName}", new String("Dupont"))
          .contentType(MediaType.APPLICATION_JSON))
          .andExpect(status().isFound())
          .andExpect(jsonPath("$.login", is(user.getLogin())))
          .andExpect(jsonPath("$.password", is(user.getPassword())))
          .andExpect(jsonPath("$.active", is(user.getActive())));
    	
    	//Résultat: on s'assure que la méthode de service findByLogin(login) a bien été appelée
    	verify(userService).findByLogin(any(String.class));
    }
    
    @Test
    public void testDeleteUser() throws Exception {
    	// on exécute le test
    	mockMvc.perform(MockMvcRequestBuilders.delete("/user/users/{id}", new Long(1)))
    			.andExpect(status().isGone());
     
    	// On vérifie que la méthode de deleteUser(Id) a bien été appelée
    	verify(userService).deleteUser(any(Long.class));     
    }
    
    @Test
    public void testUpdateUser() throws Exception {

    	//on execute la requête
    	mockMvc.perform(MockMvcRequestBuilders.put("/user/users/{id}",new Long(1))
			  .contentType(MediaType.APPLICATION_XML)
			  .accept(MediaType.APPLICATION_XML)
			  .content("<user><active>0</active></user>"))
			  .andExpect(status().isOk());
     
    	//on s'assure que la méthode de service saveOrUpdateUser(User) a bien été appelée
    	verify(userService).saveOrUpdateUser(any(User.class));
     
    }
}
