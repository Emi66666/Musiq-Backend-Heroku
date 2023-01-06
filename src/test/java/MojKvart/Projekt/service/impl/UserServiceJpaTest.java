package MojKvart.Projekt.service.impl;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import MojKvart.Projekt.dao.UserRepository;
import MojKvart.Projekt.domain.User;
import MojKvart.Projekt.rest.UserDTO;

@SpringBootTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
class UserServiceJpaTest {

	@Autowired
	PasswordEncoder passwordEncoder;

	@Autowired
	UserRepository repository;

	@Autowired
	UserServiceJpa service;

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		System.out.println("Starting Unit tests for UserService");
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
		System.out.println("Finished Unit tests for UserService");
	}

	@BeforeEach
	void setUp() throws Exception {

	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	void testRegisterWithWrongStreet() {
		UserDTO userDTO = new UserDTO();

		userDTO.setEmail("netkolijevi@fer.hr");
		userDTO.setName("Netko");
		userDTO.setSurname("Lijevi");
		userDTO.setStreetName("Ulica koja ne postoji");
		userDTO.setPasswordNoHash("pass");

		assertEquals(null, service.createUser(userDTO).getStreetId(), "Ulica postoji!");
	}

	@Test
	void testRegisterEmailAlreadyExists() {
		UserDTO userDTO = new UserDTO();

		userDTO.setEmail("katarina.brnic@fer.hr");
		userDTO.setName("Netko");
		userDTO.setSurname("Lijevi");
		userDTO.setStreetName("Omišaljska ulica");
		userDTO.setPasswordNoHash("pass");

		assertEquals(null, service.createUser(userDTO).getEmail(), "E-mail nije jos korišten!");
	}

	@Test
	void testRegisterWithEmailOfDeletedUser() {

		UserDTO userDTO = new UserDTO();
		userDTO.setEmail("izbrisan@fer.hr");
		userDTO.setName("Netko");
		userDTO.setSurname("Lijevi");
		userDTO.setStreetName("Omišaljska ulica");
		userDTO.setPasswordNoHash("pass");

		User expected = new User();

		assertEquals(expected, service.createUser(userDTO), "E-mail nije izbrisan!");
	}

	@Test
	void testCorrectLogin() {

		Long userId = service.findByEmail("katarina.brnic@fer.hr").get().getUserId();
		
		service.unbanUser(userId);

		User userLogin = new User();
		userLogin.setEmail("katarina.brnic@fer.hr");
		userLogin.setPassword("pass1");

		User expected = new User();
		expected.setEmail("katarina.brnic@fer.hr");
		expected.setBlocked(false);
		expected.setName("Katarina");
		expected.setPassword(service.findByEmail("katarina.brnic@fer.hr").get().getPassword());
		expected.setRole(0);
		expected.setStreetId(1L);
		expected.setSurname("Brnić");
		expected.setUserId(userId);

		assertEquals(expected, service.loginUser(userLogin.getEmail(), userLogin.getPassword()), "Pogreška");

	}

	@Test
	void testIncorrectLogin() {
		User userLogin = new User();
		userLogin.setEmail("katarinabrnic@fer.hr");
		userLogin.setPassword("pass1");

		assertEquals(null, service.loginUser(userLogin.getEmail(), userLogin.getPassword()), "Uspješan login!");
	}

	@Test
	void testBannedUserLogin() {
		Long userId = service.findByEmail("blokiran@fer.hr").get().getUserId();
		
		User userLogin = new User();
		userLogin.setEmail("blokiran@fer.hr");
		userLogin.setPassword("pass");

		User expected = new User();
		expected.setEmail("blokiran@fer.hr");
		expected.setBlocked(true);
		expected.setName("Blokiran");
		expected.setPassword(repository.findByEmail("blokiran@fer.hr").get().getPassword());
		expected.setRole(0);
		expected.setStreetId(1L);
		expected.setSurname("Blokiranko");
		expected.setUserId(userId);

		assertEquals(expected, service.loginUser(userLogin.getEmail(), userLogin.getPassword()), "Pogreška");

	}

}
