package demo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClient;

import demo.boundaries.NewUserBoundary;
import demo.boundaries.UserBoundary;
import demo.objects.RolesEnum;
import demo.utils.InitUtil;
import jakarta.annotation.PostConstruct;

@SpringBootTest(webEnvironment = WebEnvironment.NONE)
public class UserTests {
	
	private RestClient restClient;
	private InitUtil userUtil;
	//private UserBoundary adminUser;

	
	@Value("${server.port:8080}")
	public void setPort(int port) {
		this.restClient = RestClient.create("http://localhost:" + port + "/superapp");
		
	}
	
	@PostConstruct
	public void init() {
		userUtil = new InitUtil();
	}
	
	@BeforeEach
	@AfterEach
	public void setup() {
		
		NewUserBoundary createdNewUser = userUtil
				.createNewUser(
						"su@gmail.com",
						RolesEnum.ADMIN,
						"talmiz",
						"t");
						
		UserBoundary adminUser = this.restClient
			.post()
			.uri("/users")	
			.body(createdNewUser)
			.retrieve()
			.body(UserBoundary.class);
					
		this.restClient
			.delete()
			.uri("/admin/users?userSuperapp={userSuperapp}&userEmail={userEmail}"
					, adminUser.getUserId().getSuperapp()
					, adminUser.getUserId().getEmail())
			.retrieve();
	}
	
	//email, role, username, avatar)
	@Test
	public void testCreatedUserIsStoredInDB() throws Exception{
		NewUserBoundary createdNewUser = userUtil
				.createNewUser(
						"adminUser@gmail.com",
						RolesEnum.ADMIN,
						"talmiz",
						"t");
						
		UserBoundary createdUser = this.restClient
			.post()
			.uri("/users")	
			.body(createdNewUser)
			.retrieve()
			.body(UserBoundary.class);
		
		assertThat(this.restClient
				.get()
				.uri("/users/login/{superapp}/{email}", createdUser.getUserId().getSuperapp(), createdUser.getUserId().getEmail())
				.retrieve()
				.body(UserBoundary.class))
		.usingRecursiveComparison()
		.isEqualTo(createdUser);
	}
	
	@Test
	public void testFailToCreateUserDueNullAvatar() throws Exception{
		NewUserBoundary createdNewUser = userUtil
				.createNewUser(
						"adminUser@gmail.com",
						RolesEnum.ADMIN,
						"talmiz",
						null);
						
		assertThatThrownBy(() ->			
		this.restClient
			.post()
			.uri("/users")	
			.body(createdNewUser)
			.retrieve()
			.body(UserBoundary.class))
	.isInstanceOf(HttpStatusCodeException.class)
	.extracting("statusCode")
	.extracting("4xxClientError")
	.isEqualTo(true);
	}
	
	@Test
	public void testFailToCreateUserDueEmptyAvatar() throws Exception{
		NewUserBoundary createdNewUser = userUtil
				.createNewUser(
						"adminUser@gmail.com",
						RolesEnum.ADMIN,
						"talmiz",
						" ");
						
		assertThatThrownBy(() ->			
		this.restClient
			.post()
			.uri("/users")	
			.body(createdNewUser)
			.retrieve()
			.body(UserBoundary.class))
	.isInstanceOf(HttpStatusCodeException.class)
	.extracting("statusCode")
	.extracting("4xxClientError")
	.isEqualTo(true);
	}
	
	@Test
	public void testFailToCreateUserDueNullUsername() throws Exception{
		NewUserBoundary createdNewUser = userUtil
				.createNewUser(
						"adminUser@gmail.com",
						RolesEnum.ADMIN,
						null,
						"talmiz");
		assertThatThrownBy(() ->			
			this.restClient
				.post()
				.uri("/users")	
				.body(createdNewUser)
				.retrieve()
				.body(UserBoundary.class))
		.isInstanceOf(HttpStatusCodeException.class)
		.extracting("statusCode")
		.extracting("4xxClientError")
		.isEqualTo(true);
		
	}
	
	@Test
	public void testFailToCreateUserDueEmptyUsername() throws Exception{
		NewUserBoundary createdNewUser = userUtil
				.createNewUser(
						"adminUser@gmail.com",
						RolesEnum.ADMIN,
						" ",
						"t");
						
		assertThatThrownBy(() ->			
		this.restClient
			.post()
			.uri("/users")	
			.body(createdNewUser)
			.retrieve()
			.body(UserBoundary.class))
	.isInstanceOf(HttpStatusCodeException.class)
	.extracting("statusCode")
	.extracting("4xxClientError")
	.isEqualTo(true);
	}
	
	@Test
	public void testFailToCreateUserDueNullEmail() throws Exception{
		NewUserBoundary createdNewUser = userUtil
				.createNewUser(
						null,
						RolesEnum.ADMIN,
						"talmiz",
						"t");
		assertThatThrownBy(() ->			
			this.restClient
				.post()
				.uri("/users")	
				.body(createdNewUser)
				.retrieve()
				.body(UserBoundary.class))
		.isInstanceOf(HttpStatusCodeException.class)
		.extracting("statusCode")
		.extracting("4xxClientError")
		.isEqualTo(true);
		
	}
	
	@Test
	public void testFailToCreateUserDueInvalidEmail() throws Exception{
		NewUserBoundary createdNewUser = userUtil
				.createNewUser(
						"adminUser@gmail",
						RolesEnum.ADMIN,
						"talmiz",
						"t");
		assertThatThrownBy(() ->			
			this.restClient
				.post()
				.uri("/users")	
				.body(createdNewUser)
				.retrieve()
				.body(UserBoundary.class))
		.isInstanceOf(HttpStatusCodeException.class)
		.extracting("statusCode")
		.extracting("4xxClientError")
		.isEqualTo(true);
		
	}
	
	@Test
	public void testFailToCreateUserDueConflictEmail() throws Exception{
		NewUserBoundary createdNewUser = userUtil
				.createNewUser(
						"adminUser@gmail.com",
						RolesEnum.ADMIN,
						"talmiz",
						"t");
		
		this.restClient
			.post()
			.uri("/users")	
			.body(createdNewUser)
			.retrieve()
			.body(UserBoundary.class);
		
		assertThatThrownBy(() ->			
			this.restClient
				.post()
				.uri("/users")	
				.body(createdNewUser)
				.retrieve()
				.body(UserBoundary.class))
		.isInstanceOf(HttpStatusCodeException.class)
		.extracting("statusCode")
		.extracting("4xxClientError")
		.isEqualTo(true);
		
	}
	
	@Test
	public void testFailToCreateUserDueEmptyEmail() throws Exception{
		NewUserBoundary createdNewUser = userUtil
				.createNewUser(
						" ",
						RolesEnum.ADMIN,
						"talmiz",
						"t");
						
		assertThatThrownBy(() ->			
		this.restClient
			.post()
			.uri("/users")	
			.body(createdNewUser)
			.retrieve()
			.body(UserBoundary.class))
	.isInstanceOf(HttpStatusCodeException.class)
	.extracting("statusCode")
	.extracting("4xxClientError")
	.isEqualTo(true);
	}
	
	@Test
	public void testGetNonExistingUser() throws Exception {
		NewUserBoundary createdNewUser = userUtil
				.createNewUser(
						"talmiz@gmail.com",
						RolesEnum.ADMIN,
						"talmiz",
						"t");
		
		UserBoundary createdUser= this.restClient
				.post()
				.uri("/users")	
				.body(createdNewUser)
				.retrieve()
				.body(UserBoundary.class);
		
		this.restClient
			.delete()
			.uri("/admin/users?userSuperapp={userSuperapp}&userEmail={userEmail}", createdUser.getUserId().getSuperapp()
					, createdUser.getUserId().getEmail())
			.retrieve();
		
		assertThatThrownBy(() -> this.restClient
				.get()
				.uri("/users/login/{superapp}/{email}", createdUser.getUserId().getSuperapp(), createdUser.getUserId().getEmail())
				.retrieve()
				.body(UserBoundary.class))
		.isInstanceOf(HttpStatusCodeException.class)
		.extracting("statusCode")
		.extracting("4xxClientError")
		.isEqualTo(true);
		
	}
	
	@Test
	public void testCreatedMultiplyUsersAreStoreInDB() throws Exception {
		List<UserBoundary> createdUsers =
				Stream.of("tal@gmail.com", "noam@gmail.com", "hadas@gmail.com")
				.map(email ->
				userUtil
				.createNewUser(
						email,
						RolesEnum.ADMIN,
						"talmiz",
						"t"))
				.map(newUser ->
				this.restClient
			.post()
			.uri("/users")	
			.body(newUser)
			.retrieve()
			.body(UserBoundary.class))
			.toList();
		
		UserBoundary adminUser = createdUsers.get(0);
		
		assertThat(this.restClient
				.get()
				.uri("/admin/users?userSuperapp={userSuperapp}&userEmail={userEmail}&size={size}&page=0", adminUser.getUserId().getSuperapp()
						, adminUser.getUserId().getEmail(), createdUsers.size() + 10)
				.retrieve()
				.body(UserBoundary[].class))
		.usingRecursiveFieldByFieldElementComparator()
		.containsExactlyInAnyOrderElementsOf(createdUsers);
	
	}
	
	
	
	@Test
	public void testCreateUsersDeleteThemAndMakeSureTheyAreRemovedFromDB() throws Exception {
		List<UserBoundary> createdUsers =
				Stream.of("tal@gmail.com", "noam@gmail.com", "hadas@gmail.com")
				.map(email ->
				userUtil
				.createNewUser(
						email,
						RolesEnum.ADMIN,
						"talmiz",
						"t"))
				.map(newUser ->
				this.restClient
				.post()
				.uri("/users")	
				.body(newUser)
				.retrieve()
				.body(UserBoundary.class))
				.toList();
		
		UserBoundary adminUser = createdUsers.get(0);
		this.restClient
		.delete()
		.uri("/admin/users?userSuperapp={userSuperapp}&userEmail={userEmail}"
				, adminUser.getUserId().getSuperapp()
				, adminUser.getUserId().getEmail())
		.retrieve();
		
		NewUserBoundary createdNewUser = userUtil
				.createNewUser(
						"tal@gmail.com",
						RolesEnum.ADMIN,
						"talmiz",
						"t");
						
		adminUser = this.restClient
			.post()
			.uri("/users")	
			.body(createdNewUser)
			.retrieve()
			.body(UserBoundary.class);
		
		assertThat(this.restClient
				.get()
				.uri("/admin/users?userSuperapp={userSuperapp}&userEmail={userEmail}&size={size}&page=0"
						, adminUser.getUserId().getSuperapp()
						, adminUser.getUserId().getEmail()
						, createdUsers.size() + 10)
				.retrieve()
				.body(UserBoundary[].class))
		.usingRecursiveFieldByFieldElementComparator()
		.containsExactly(adminUser);
	}
	
	
	@Test
	public void testCreateUsersAndFailedToDeleteThem_SUPERAPP_USER() throws Exception {
		List<UserBoundary> deleteUsers =
				Stream.of("tal@gmail.com", "noam@gmail.com", "hadas@gmail.com")
				.map(email ->
				userUtil
				.createNewUser(
						email,
						RolesEnum.SUPERAPP_USER,
						"talmiz",
						"t"))
				.map(newUser ->
				this.restClient
				.post()
				.uri("/users")	
				.body(newUser)
				.retrieve()
				.body(UserBoundary.class))
				.toList();
		
		UserBoundary superappUser = deleteUsers.get(0);
	
		assertThatThrownBy(() -> 
		this.restClient
		.delete()
		.uri("/admin/users?userSuperapp={userSuperapp}&userEmail={userEmail}"
				, superappUser.getUserId().getSuperapp()
				, superappUser.getUserId().getEmail())
		.retrieve()
		.body(void.class))
		.isInstanceOf(HttpStatusCodeException.class)
		.extracting("statusCode")
		.extracting("4xxClientError")
		.isEqualTo(true);

	}
	
	@Test
	public void testCreateUsersAndFailedToDeleteThem_MINIAPP_USER() throws Exception {
		List<UserBoundary> deleteUsers =
				Stream.of("tal@gmail.com", "noam@gmail.com", "hadas@gmail.com")
				.map(email ->
				userUtil
				.createNewUser(
						email,
						RolesEnum.SUPERAPP_USER,
						"talmiz",
						"t"))
				.map(newUser ->
				this.restClient
				.post()
				.uri("/users")	
				.body(newUser)
				.retrieve()
				.body(UserBoundary.class))
				.toList();
		
		UserBoundary superappUser = deleteUsers.get(0);
		
		assertThatThrownBy(() -> 
		this.restClient
		.delete()
		.uri("/admin/users?userSuperapp={userSuperapp}&userEmail={userEmail}"
				, superappUser.getUserId().getSuperapp()
				, superappUser.getUserId().getEmail())
		.retrieve()
		.body(void.class))
		.isInstanceOf(HttpStatusCodeException.class)
		.extracting("statusCode")
		.extracting("4xxClientError")
		.isEqualTo(true);

	}
	
	//email, role, username, avatar)
		@Test
		public void testSuccessfulUpdatedUserRoleUsernameAvatarInDB() throws Exception{
			NewUserBoundary createdNewUser = userUtil
					.createNewUser(
							"adminUser@gmail.com",
							RolesEnum.ADMIN,
							"talmiz",
							"t");
							
			UserBoundary createdUser = this.restClient
				.post()
				.uri("/users")	
				.body(createdNewUser)
				.retrieve()
				.body(UserBoundary.class);
			
			// changing the role, username and the avatar of the created user
			userUtil.updateUserAvatar(createdUser, "dummy");
			userUtil.updateUserUsername(createdUser, "dummyUser");
			userUtil.updateUserRole(createdUser, RolesEnum.MINIAPP_USER);
			
			this.restClient
			.put()
			.uri("/users/{superapp}/{email}", createdUser.getUserId().getSuperapp(), createdUser.getUserId().getEmail())
			.body(createdUser)
			.retrieve();
			
			assertThat(this.restClient
					.get()
					.uri("/users/login/{superapp}/{email}", createdUser.getUserId().getSuperapp(), createdUser.getUserId().getEmail())
					.retrieve()
					.body(UserBoundary.class))
			.usingRecursiveComparison()
			.isEqualTo(createdUser);
		}
		
	

}
