package demo;
//
//public class ObjectsTests {
//
//}


import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collections;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.web.client.RestClient;

import demo.boundaries.NewUserBoundary;
import demo.boundaries.UserBoundary;
import demo.objects.RolesEnum;
import jakarta.annotation.PostConstruct;

@SpringBootTest(webEnvironment = WebEnvironment.NONE)
class ObjectsTests {
	private String url;
	private int port;
	private RestClient restClient;

	@Value("${server.port:8080}")
	public void setPort(int port) {
		this.port = port;
	}

	@PostConstruct
	public void init() {
		System.err.println("beginning test");
		this.url = "http://localhost:" + port + "/superapp";
		this.restClient = RestClient.create(url);
	}

	@BeforeEach
	@AfterEach
	public void setup() {
		System.err.println("set up");
		String username = "Gal";
		NewUserBoundary newUser = new NewUserBoundary();
		newUser.setAvatar("tal");
		newUser.setUsername(username);
		newUser.setRole(RolesEnum.ADMIN);
		newUser.setEmail("ravid@gmail.com");

		UserBoundary newUser1 = this.restClient.post().uri("/users").body(newUser).retrieve().body(UserBoundary.class);
		System.err.println("reef");

		// DELETE database
		this.restClient.delete().uri("/admin/users?userSuperapp={userSuperapp}&userEmail={email}",
				newUser1.getUserId().getSuperapp(), newUser1.getUserId().getEmail()).retrieve();
		System.err.println("reeff");

	}

	@Test
	public void contextLoads() {
	}

	@Test
	public void testStoreUserWithUserNameGalToDatabase() throws Exception {
		// GIVEN the server is up
		// AND the database is empty

		// WHEN I POST /users {"username":"Gal"}
		String username = "Gal";
		NewUserBoundary newUser = new NewUserBoundary();
		newUser.setAvatar("fd");
		newUser.setUsername(username);
		newUser.setRole(RolesEnum.ADMIN);
		newUser.setEmail("gal@gmail.com");

		UserBoundary newUser1 = this.restClient.post().uri("/users").body(newUser).retrieve().body(UserBoundary.class);

		// THEN the username is store to the database
		// AND the server responds with status 2xx
		assertThat(this.restClient.get()
				.uri("/users/login/{superapp}/{email}", newUser1.getUserId().getSuperapp(),
						newUser1.getUserId().getEmail())
				.retrieve().body(UserBoundary.class).getUsername()).isEqualTo(username);
	}

	@Test
	public void testStoreUserWithUserNameHelloToDatabaseWithExistingInfo() throws Exception {
		// GIVEN the server is up
		// AND the database contains another User with username "Gal"
		String username = "Gal";
		NewUserBoundary user = new NewUserBoundary();
		user.setAvatar("fb");
		user.setUsername(username);
		user.setRole(RolesEnum.ADMIN);
		user.setEmail("gali@gmail.com");

		this.restClient.post().uri("/users").body(user).retrieve().body(UserBoundary.class);

		// WHEN I POST /users {"username":"Gal"}
		NewUserBoundary newUser = new NewUserBoundary();
		newUser.setAvatar("ndt");
		newUser.setUsername(username);
		newUser.setRole(RolesEnum.ADMIN);
		newUser.setEmail("galR@gmail.com");

		UserBoundary user2 = this.restClient.post().uri("/users").body(newUser).retrieve().body(UserBoundary.class);

		// THEN the user is store to the database
		// AND the server responds with status 2xx
		assertThat(this.restClient.get()
				.uri("/users/login/{superapp}/{email}", user2.getUserId().getSuperapp(), user2.getUserId().getEmail())
				.retrieve().body(UserBoundary.class)).usingRecursiveComparison().isEqualTo(user2);
	}

	@Test
	public void testUpdateRoleToDatabaseOfExistingUser() throws Exception {
		// GIVEN the server is up
		// AND the database contains user with role "ADMIN"
		String username = "Gal";
		NewUserBoundary user = new NewUserBoundary();
		user.setAvatar("bfd");
		user.setUsername(username);
		user.setRole(RolesEnum.ADMIN);
		user.setEmail("galii@gmail.com");

		UserBoundary newUser = this.restClient.post().uri("/users").body(user).retrieve().body(UserBoundary.class);


		// WHEN I PUT /users {"role":"SUPERAPP_USER"}
		newUser.setRole(RolesEnum.MINIAPP_USER);
		this.restClient.put().uri("/users/{superapp}/{email}",newUser.getUserId().getSuperapp(),newUser.getUserId().getEmail()).body(newUser).retrieve();

		// THEN the updated role is store to the database
		// AND the server responds with status 2xx
		
		assertThat(this.restClient.get()
				.uri("/users/login/{superapp}/{userEmail}", newUser.getUserId().getSuperapp(), newUser.getUserId().getEmail())
				.retrieve().body(UserBoundary.class).getRole()).isEqualTo(RolesEnum.MINIAPP_USER);

	}
	
	@Test
	public void testUpdateUserNameToDatabaseOfExistingUser() throws Exception {
		// GIVEN the server is up
		// AND the database contains user with username "Gali"
		String username = "Gali";
		NewUserBoundary user = new NewUserBoundary();
		user.setAvatar("njds");
		user.setUsername(username);
		user.setRole(RolesEnum.ADMIN);
		user.setEmail("garl@gmail.com");

		UserBoundary newUser = this.restClient.post().uri("/users").body(user).retrieve().body(UserBoundary.class);


		// WHEN I PUT /users {"username":"Gal"}
		newUser.setUsername("Gal");;
		this.restClient.put().uri("/users/{superapp}/{email}",newUser.getUserId().getSuperapp(),newUser.getUserId().getEmail()).body(newUser).retrieve();

		// THEN the updated username is store to the database
		// AND the server responds with status 2xx
		
		assertThat(this.restClient.get()
				.uri("/users/login/{superapp}/{userEmail}", newUser.getUserId().getSuperapp(), newUser.getUserId().getEmail())
				.retrieve().body(UserBoundary.class).getUsername()).isEqualTo("Gal");

	}

}
