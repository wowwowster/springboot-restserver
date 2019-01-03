package com.bnguimgo.springbootrestserver.integrationtest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import com.bnguimgo.springbootrestserver.model.User;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:test.properties")
//@Sql({ "classpath:init-test-data.sql" })
public class UserControllerIntegrationTest {

	private static Logger logger = LoggerFactory.getLogger(UserControllerIntegrationTest.class);
	
	@Autowired
	private TestRestTemplate restTemplate;
	@LocalServerPort //permet d'utiliser le port local du serveur, sinon une erreur "Connection refused"
	private int port;
	private static final String URL= "http://localhost:";//url du serveur REST. Cet url peut être celle d'un serveur distant
	
	private String getURLWithPort(String uri) {
		return URL + port + uri;
	}
	 @Test
	 public void testFindAllUsers() throws Exception {
		 ResponseEntity<Object> responseEntity = restTemplate.getForEntity(getURLWithPort("/springboot-restserver/user/users"), Object.class);

		Collection<User> userCollections = (Collection<User>) responseEntity.getBody();
		logger.info("Utilisateur trouvé : " + userCollections.toString());

		// On vérifie le code de réponse HTTP, en cas de différence entre les deux valeurs, le message "Réponse inattendue" est affiché
		assertEquals("Réponse inattendue", HttpStatus.FOUND.value(), responseEntity.getStatusCodeValue());

		assertNotNull(userCollections);
		assertEquals(4, userCollections.size());//on a bien 3 utilisateurs initialés par les scripts data.sql
	 }
	 
	 @Test
	    public void testSaveUser() throws Exception {
		 User user = new User("PIPO", "password", 1);
		 ResponseEntity<User> userEntitySaved =  restTemplate.postForEntity(getURLWithPort("/springboot-restserver/user/users"), user, User.class);
		 User userSaved = userEntitySaved.getBody();
		 assertNotNull(userSaved);
		 assertEquals(user.getLogin(),userSaved.getLogin());
		 assertEquals("Réponse inattendue", HttpStatus.CREATED.value(), userEntitySaved.getStatusCodeValue());
	 }
	@Test
	public void testFindUserByLogin() throws Exception {

		 Map<String, String> variables = new HashMap<>(1);
	        variables.put("loginName", "user3");
		ResponseEntity<User> responseEntity = restTemplate.getForEntity(getURLWithPort("/springboot-restserver/user/users/{loginName}"), User.class, variables);

		User userFound = responseEntity.getBody();
		logger.info("Utilisateur trouvé : " + userFound.toString());

		// On vérifie le code de réponse HTTP, en cas de différence entre les deux valeurs, le message "Réponse inattendue" est affiché
		assertEquals("Réponse inattendue", HttpStatus.FOUND.value(), responseEntity.getStatusCodeValue());

		assertNotNull(userFound);
		assertEquals(3, userFound.getId().longValue());
	}
	
	 @Test
	    public void testDeleteUser() throws Exception {
		 Map<String, Long> variables = new HashMap<>(1);
	        variables.put("id", new Long(1));
	    ResponseEntity<Void> responseEntity = restTemplate.exchange(getURLWithPort("/springboot-restserver/user/users/{id}"), 
					HttpMethod.DELETE, 
					null, 
					Void.class,
					variables);
	    assertEquals("Réponse inattendue", HttpStatus.GONE.value(), responseEntity.getStatusCodeValue());
	 }

	 @Test
	    public void testUpdateUser() throws Exception {
		 Map<String, Long> variables = new HashMap<>(2);
		 User userToUpdate = new User("updateLogin", "password", 0);//on met à jour l'utilisateur qui a l'identifiant 1
	        variables.put("id", new Long(2));//on va désactiver l'utilsateur qui a l'identifiant 1
	        HttpEntity<User> requestEntity = new HttpEntity<User>(userToUpdate);
	    ResponseEntity<User> responseEntity = restTemplate.exchange(getURLWithPort("/springboot-restserver/user/users/{id}"), 
	    		HttpMethod.PUT, 
				requestEntity, 
				User.class,variables);
	    assertEquals("Réponse inattendue", HttpStatus.OK.value(), responseEntity.getStatusCodeValue());
	 }
}
