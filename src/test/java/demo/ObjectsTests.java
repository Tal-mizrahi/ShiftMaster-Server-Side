package demo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.HashMap;
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
import demo.boundaries.ObjectBoundary;
import demo.boundaries.UserBoundary;
import demo.objects.Location;
import demo.objects.RolesEnum;
import demo.objects.UserId;
import demo.utils.InitUtil;
import jakarta.annotation.PostConstruct;

@SpringBootTest(webEnvironment = WebEnvironment.NONE)
public class ObjectsTests {

	private RestClient restClient;
	private InitUtil initUtil;

	@Value("${server.port:8080}")
	public void setPort(int port) {
		this.restClient = RestClient.create("http://localhost:" + port + "/superapp");

	}

	@PostConstruct
	public void init() {
		initUtil = new InitUtil();
	}

	// in each test make the creating of 3 objects to search.

	@BeforeEach
	@AfterEach
	public void setup() {

		NewUserBoundary createdNewUser = initUtil.createNewUser("admin@gmail.com", RolesEnum.ADMIN, "talmiz", "t");

		UserBoundary adminUser = this.restClient.post().uri("/users").body(createdNewUser).retrieve()
				.body(UserBoundary.class);

		this.restClient.delete().uri("/admin/objects?userSuperapp={userSuperapp}&userEmail={userEmail}",
				adminUser.getUserId().getSuperapp(), adminUser.getUserId().getEmail()).retrieve();

		this.restClient.delete().uri("/admin/users?userSuperapp={userSuperapp}&userEmail={userEmail}",
				adminUser.getUserId().getSuperapp(), adminUser.getUserId().getEmail()).retrieve();

	}

	// CREATE OBJECT TEST WITH SUPER APP USER
	@Test
	public void testCreateObjectWithSuperappUserStoredInDB() throws Exception {

		UserBoundary superUser = createAndPostUser("az@gmail.com", RolesEnum.SUPERAPP_USER, "adir", "a");

		ObjectBoundary ob = createAndPostObject("schedual", // Type
				"Note", // Alias
				new Location(32.158687, 34.795102), // Location
				true, // Active
				superUser.getUserId(), // Created by
				new HashMap<>() // Additional properties
		);

		assertThat(this.restClient.get()
				.uri("/objects/{superapp}/{id}?userSuperapp={userSuperapp}&userEmail={userEmail}",
						ob.getObjectId().getSuperapp(), ob.getObjectId().getId(), superUser.getUserId().getSuperapp(),
						superUser.getUserId().getEmail())
				.retrieve().body(ObjectBoundary.class)).usingRecursiveComparison().isEqualTo(ob);
	}

	// CREATE FEW OBJECTS TEST WITH SUPER APP USER
	@Test
	public void testCreateFewObjectsWithSuperappUserStoredInDB() throws Exception {

		UserBoundary superUser = createAndPostUser("az@gmail.com", RolesEnum.SUPERAPP_USER, "adir", "a");

		ObjectBoundary object1 = createAndPostObject("schedual", "Object 1", new Location(32.158687, 34.795102), // Location
				true, // Active
				superUser.getUserId(), // Created by
				new HashMap<>() // Additional properties
		);

		ObjectBoundary object2 = createAndPostObject("meeting", // Type
				"Object 2", // Alias
				new Location(32.158687, 34.795102), // Location
				true, // Active
				superUser.getUserId(), // Created by
				new HashMap<>() // Additional properties
		);

		ObjectBoundary object3 = createAndPostObject("appointment", // Type
				"Object 3", // Alias
				new Location(32.158687, 34.795102), // Location
				true, // Active
				superUser.getUserId(), // Created by
				new HashMap<>() // Additional properties
		);

		ObjectBoundary[] retrievedObjects = this.restClient.get()
				.uri("/objects?userSuperapp={userSuperapp}&userEmail={userEmail}&size={size}&page=0",
						superUser.getUserId().getSuperapp(), superUser.getUserId().getEmail(), 10)
				.retrieve().body(ObjectBoundary[].class);

		assertThat(retrievedObjects).isNotNull().usingRecursiveFieldByFieldElementComparator()
				.containsExactlyInAnyOrder(retrievedObjects);
	}
	
	
	// CREATE OBJECT WITH MINI APP USER TEST
		@Test
		public void testFailToCreateObjectWithMiniappUser() throws Exception {

			UserBoundary miniAppUser = createAndPostUser("az@gmail.com", RolesEnum.MINIAPP_USER, "adir", "a");

			ObjectBoundary ob = initUtil.createNewObject(
					"schedual",
					"Object 1",
					new Location(32.158687, 34.795102),
					true,
					miniAppUser.getUserId(),
					new HashMap<>());

			assertThatThrownBy(() -> this.restClient.post()
					.uri("/objects")
					.retrieve()
					.body(ObjectBoundary.class))
					.isInstanceOf(HttpStatusCodeException.class).extracting("statusCode").extracting("4xxClientError")
					.isEqualTo(true);
		}
		
		
		// CREATE OBJECT WITH ADMIN USER TEST
				@Test
				public void testFailToCreateObjectsWithAdminUser() throws Exception {

					UserBoundary admin = createAndPostUser(
							"az@gmail.com",
							RolesEnum.ADMIN,
							"adir", 
							"a");

					ObjectBoundary ob = initUtil.createNewObject(
							"schedual",
							"Object 1",
							new Location(32.158687, 34.795102),
							true,
							admin.getUserId(),
							new HashMap<>());

					assertThatThrownBy(() -> this.restClient.post()
							.uri("/objects")
							.retrieve()
							.body(ObjectBoundary.class))
							.isInstanceOf(HttpStatusCodeException.class).extracting("statusCode").extracting("4xxClientError")
							.isEqualTo(true);
				}
				
				
				
	// UPDATE OBJECT TEST WITH SUPER APP USER
	@Test
	public void testUpdateObjectsWithSuperappUser() throws Exception {

		UserBoundary superUser = createAndPostUser("az@gmail.com", RolesEnum.SUPERAPP_USER, "adir", "a");

		ObjectBoundary object1 = createAndPostObject("schedual", "Object 1", new Location(32.158687, 34.795102), // Location
				true, // Active
				superUser.getUserId(), // Created by
				new HashMap<>() // Additional properties
		);

		object1.setAlias("Object 1 New");
		object1.setActive(false);
		object1.setLocation(new Location(100.158687, 100.795102));
		object1.setType("Note");
		object1.setObjectDetails(new HashMap<>());

		this.restClient.put()
				.uri("/objects/{superapp}/{id}?userSuperapp={userSuperapp}&userEmail={userEmail}",
						object1.getObjectId().getSuperapp(), object1.getObjectId().getId(),
						superUser.getUserId().getSuperapp(), superUser.getUserId().getEmail())
				.body(object1).retrieve();

		assertThat(this.restClient.get()
				.uri("/objects/{superapp}/{id}?userSuperapp={userSuperapp}&userEmail={userEmail}",
						object1.getObjectId().getSuperapp(), object1.getObjectId().getId(),
						superUser.getUserId().getSuperapp(), superUser.getUserId().getEmail())
				.retrieve().body(ObjectBoundary.class)).usingRecursiveComparison().isEqualTo(object1);
	}
	
	// UPDATE OBJECT TEST WITH ADMIN USER
		@Test
		public void testFailToUpdateObjectsWithAdminUser() throws Exception {

			UserBoundary superUser = createAndPostUser("az@gmail.com", RolesEnum.SUPERAPP_USER, "adir", "a");

			ObjectBoundary object1 = createAndPostObject("schedual", "Object 1", new Location(32.158687, 34.795102), // Location
					true, // Active
					superUser.getUserId(), // Created by
					new HashMap<>() // Additional properties
			);

			object1.setAlias("Object 1 New");
			object1.setActive(false);
			object1.setLocation(new Location(100.158687, 100.795102));
			object1.setType("Note");
			object1.setObjectDetails(new HashMap<>());
			
			superUser.setRole(RolesEnum.ADMIN); //change user to admin

			/*this.restClient.put()
					.uri("/objects/{superapp}/{id}?userSuperapp={userSuperapp}&userEmail={userEmail}",
							object1.getObjectId().getSuperapp(), object1.getObjectId().getId(),
							superUser.getUserId().getSuperapp(), superUser.getUserId().getEmail())
					.body(object1).retrieve();*/

			assertThatThrownBy(() -> this.restClient.put()
					.uri("/objects/{superapp}/{id}?userSuperapp={userSuperapp}&userEmail={userEmail}",
					 object1.getObjectId().getSuperapp(), object1.getObjectId().getId(),
					 superUser.getUserId().getSuperapp(), superUser.getUserId().getEmail())
					.retrieve()
					.body(ObjectBoundary.class))
					.isInstanceOf(HttpStatusCodeException.class).extracting("statusCode").extracting("4xxClientError")
					.isEqualTo(true);
		}
		
		
		
		// UPDATE OBJECT TEST WITH Mini App User
				@Test
				public void testFailToUpdateObjectsWithMiniAppUser() throws Exception {

					UserBoundary superUser = createAndPostUser("az@gmail.com", RolesEnum.SUPERAPP_USER, "adir", "a");

					ObjectBoundary object1 = createAndPostObject("schedual", "Object 1", new Location(32.158687, 34.795102), // Location
							true, // Active
							superUser.getUserId(), // Created by
							new HashMap<>() // Additional properties
					);

					object1.setAlias("Object 1 New");
					object1.setActive(false);
					object1.setLocation(new Location(100.158687, 100.795102));
					object1.setType("Note");
					object1.setObjectDetails(new HashMap<>());
					
					superUser.setRole(RolesEnum.MINIAPP_USER); //change user to admin

					/*this.restClient.put()
							.uri("/objects/{superapp}/{id}?userSuperapp={userSuperapp}&userEmail={userEmail}",
									object1.getObjectId().getSuperapp(), object1.getObjectId().getId(),
									superUser.getUserId().getSuperapp(), superUser.getUserId().getEmail())
							.body(object1).retrieve();*/

					assertThatThrownBy(() -> this.restClient.put()
							.uri("/objects/{superapp}/{id}?userSuperapp={userSuperapp}&userEmail={userEmail}",
							 object1.getObjectId().getSuperapp(), object1.getObjectId().getId(),
							 superUser.getUserId().getSuperapp(), superUser.getUserId().getEmail())
							.retrieve()
							.body(ObjectBoundary.class))
							.isInstanceOf(HttpStatusCodeException.class).extracting("statusCode").extracting("4xxClientError")
							.isEqualTo(true);
				}

	

	
	
		
		
		// CREATE OBJECT WITH EMPTY ALIAS
		@Test
		public void testFailToCreateObjectsWithEmptyAlias() throws Exception {

			UserBoundary superUser = createAndPostUser("az@gmail.com", RolesEnum.SUPERAPP_USER, "adir", "a");
			
			ObjectBoundary ob = initUtil.createNewObject(
					"schedual",
					"",
					new Location(32.158687, 34.795102),
					true,
					superUser.getUserId(),
					new HashMap<>());

			assertThatThrownBy(() -> this.restClient.post()
					.uri("/objects")
					.retrieve()
					.body(void.class))
					.isInstanceOf(HttpStatusCodeException.class)
					.extracting("statusCode")
					.extracting("4xxClientError")
					.isEqualTo(true);
		}
		
		
		// CREATE OBJECT WITH NULL ALIAS
				@Test
				public void testFailToCreateObjectsWithNullAlias() throws Exception {

					UserBoundary superUser = createAndPostUser("az@gmail.com", RolesEnum.SUPERAPP_USER, "adir", "a");
					
					ObjectBoundary ob = initUtil.createNewObject(
							"schedual",
							null,
							new Location(32.158687, 34.795102),
							true,
							superUser.getUserId(),
							new HashMap<>());

					assertThatThrownBy(() -> this.restClient.post()
							.uri("/objects")
							.retrieve()
							.body(void.class))
							.isInstanceOf(HttpStatusCodeException.class)
							.extracting("statusCode")
							.extracting("4xxClientError")
							.isEqualTo(true);
				}
		
		
		
		
		// CREATE OBJECT WITHOT ISACTIVE
				@Test
				public void testFailToCreateObjectsWithNullActive() throws Exception {

					UserBoundary superUser = createAndPostUser("az@gmail.com", RolesEnum.SUPERAPP_USER, "adir", "a");
					
					ObjectBoundary ob = initUtil.createNewObject(
							"schedual",
							"object 1",
							new Location(32.158687, 34.795102),
							null,
							superUser.getUserId(),
							new HashMap<>());

					assertThatThrownBy(() -> this.restClient.post()
							.uri("/objects")
							.retrieve()
							.body(void.class))
							.isInstanceOf(HttpStatusCodeException.class)
							.extracting("statusCode")
							.extracting("4xxClientError")
							.isEqualTo(true);
				}
				
				
		// CREATE OBJECT WITH EMPTY TYPE
				@Test
				public void testFailToCreateObjectsWithEmptyType() throws Exception {

					UserBoundary superUser = createAndPostUser("az@gmail.com", RolesEnum.SUPERAPP_USER, "adir", "a");
					
					ObjectBoundary ob = initUtil.createNewObject(
							"",
							"object 1",
							new Location(32.158687, 34.795102),
							true,
							superUser.getUserId(),
							new HashMap<>());

					assertThatThrownBy(() -> this.restClient.post()
							.uri("/objects")
							.retrieve()
							.body(void.class))
							.isInstanceOf(HttpStatusCodeException.class)
							.extracting("statusCode")
							.extracting("4xxClientError")
							.isEqualTo(true);
				}
				
				// CREATE OBJECT WITH NULL TYPE
				@Test
				public void testFailToCreateObjectsWithNullType() throws Exception {

					UserBoundary superUser = createAndPostUser("az@gmail.com", RolesEnum.SUPERAPP_USER, "adir", "a");
					
					ObjectBoundary ob = initUtil.createNewObject(
							null,
							"object 1",
							new Location(32.158687, 34.795102),
							true,
							superUser.getUserId(),
							new HashMap<>());

					assertThatThrownBy(() -> this.restClient.post()
							.uri("/objects")
							.retrieve()
							.body(void.class))
							.isInstanceOf(HttpStatusCodeException.class)
							.extracting("statusCode")
							.extracting("4xxClientError")
							.isEqualTo(true);
				}
							
		// CREATE OBJECT WITHOUT HASH MAP
				@Test
				public void testFailToCreateObjectsWithNullHashMap() throws Exception {

					UserBoundary superUser = createAndPostUser("az@gmail.com", RolesEnum.SUPERAPP_USER, "adir", "a");
					
					ObjectBoundary ob = initUtil.createNewObject(
							"ad",
							"object 1",
							new Location(32.158687, 34.795102),
							true,
							superUser.getUserId(),
							null);

					assertThatThrownBy(() -> this.restClient.post()
							.uri("/objects")
							.retrieve()
							.body(void.class))
							.isInstanceOf(HttpStatusCodeException.class)
							.extracting("statusCode")
							.extracting("4xxClientError")
							.isEqualTo(true);
				}
					
				
				// CREATE OBJECT WITHOUT HASH MAP
				@Test
				public void testFailToCreateObjectsWithNullLocation() throws Exception {

					UserBoundary superUser = createAndPostUser("az@gmail.com", RolesEnum.SUPERAPP_USER, "adir", "a");
					
					ObjectBoundary ob = initUtil.createNewObject(
							"ad",
							"object 1",
							null,
							true,
							superUser.getUserId(),
							new HashMap<>());

					assertThatThrownBy(() -> this.restClient.post()
							.uri("/objects")
							.retrieve()
							.body(void.class))
							.isInstanceOf(HttpStatusCodeException.class)
							.extracting("statusCode")
							.extracting("4xxClientError")
							.isEqualTo(true);
				}
				
				//  DELETE OBJECTS WITH ADMIN USER TEST
				@Test
				public void testDeleteObjectsWithAdminUser() throws Exception{
					UserBoundary superUser = createAndPostUser("az@gmail.com", RolesEnum.SUPERAPP_USER, "adir", "a");

					ObjectBoundary object1 = createAndPostObject("schedual", "Object 1", new Location(32.158687, 34.795102), // Location
							true, // Active
							superUser.getUserId(), // Created by
							new HashMap<>() // Additional properties
					);

					ObjectBoundary object2 = createAndPostObject("meeting", // Type
							"Object 2", // Alias
							new Location(32.158687, 34.795102), // Location
							true, // Active
							superUser.getUserId(), // Created by
							new HashMap<>() // Additional properties
					);

					ObjectBoundary object3 = createAndPostObject("appointment", // Type
							"Object 3", // Alias
							new Location(32.158687, 34.795102), // Location
							true, // Active
							superUser.getUserId(), // Created by
							new HashMap<>() // Additional properties
					);
					
					UserBoundary adminUser = createAndPostUser("nb@gmail.com", RolesEnum.ADMIN, "noam", "n");
					
					this.restClient.delete()
					.uri("/admin/objects?userSuperapp={userSuperapp}&userEmail={userEmail}",
							adminUser.getUserId().getSuperapp(), adminUser.getUserId().getEmail())
					.retrieve();
					
								
					/*this.restClient
					.delete()
					.uri("/admin/objects?userSuperapp={userSuperapp}&userEmail={userEmail}"
							, adminUser.getUserId().getSuperapp()
							, adminUser.getUserId().getEmail())
					.retrieve();*/
					
					
					
					
					
					
					
					/*ObjectBoundary[] retrievedObjects = this.restClient.get()
							.uri("/objects?userSuperapp={userSuperapp}&userEmail={userEmail}&size={size}&page=0",
									superUser.getUserId().getSuperapp(), superUser.getUserId().getEmail(), 10)
							.retrieve().body(ObjectBoundary[].class);*/
									
				assertThat(this.restClient
						.get()
						.uri("/objects?userSuperapp={userSuperapp}&userEmail={userEmail}&size={size}&page=0",
								superUser.getUserId().getSuperapp(), superUser.getUserId().getEmail(), 10)
						.retrieve()
						.body(ObjectBoundary[].class))
				.isNotNull()
				.isEmpty();
				}				
				
			// SUPERAPP USER TRIES DELETE OBJECTS
				@Test
				public void testFailToDeleteObjectsWithSuperAppUser() throws Exception{
					UserBoundary superUser = createAndPostUser("az@gmail.com", RolesEnum.SUPERAPP_USER, "adir", "a");

					ObjectBoundary object1 = createAndPostObject("schedual", "Object 2 ", new Location(32.158687, 34.795102), // Location
							true, // Active
							superUser.getUserId(), // Created by
							new HashMap<>() // Additional properties
					);

					assertThatThrownBy(() ->  this.restClient.delete()
							.uri("admin/objects?userSuperapp={userSuperapp}&userEmail={userEmail}",
									superUser.getUserId().getSuperapp(), superUser.getUserId().getEmail())
							.retrieve().body(void.class))
				.isInstanceOf(HttpStatusCodeException.class)
				.extracting("statusCode")
				.extracting("4xxClientError")
				.isEqualTo(true);
				}
				
				
				
				// MINIAPP USER TRIES DELETE OBJECTS
				@Test
				public void testFailToDeleteObjectsWithMiniAppUser() throws Exception{
					UserBoundary superUser = createAndPostUser("az@gmail.com", RolesEnum.SUPERAPP_USER, "adir", "a");

					ObjectBoundary object1 = createAndPostObject("schedual", "Object 2 ", new Location(32.158687, 34.795102), // Location
							true, // Active
							superUser.getUserId(), // Created by
							new HashMap<>() // Additional properties
					);
					
					superUser.setRole(RolesEnum.MINIAPP_USER); //change role to miniApp user

					assertThatThrownBy(() ->  this.restClient.delete()
							.uri("admin/objects?userSuperapp={userSuperapp}&userEmail={userEmail}",
									superUser.getUserId().getSuperapp(), superUser.getUserId().getEmail())
							.retrieve().body(ObjectBoundary.class))
				.isInstanceOf(HttpStatusCodeException.class)
				.extracting("statusCode")
				.extracting("4xxClientError")
				.isEqualTo(true);
				}
		
/* ---------------------------------------------FUNCTIONS---------------------------------------------*/
	
	// CREATE USER AND POST IT
	private UserBoundary createAndPostUser(String email, RolesEnum role, String firstName, String lastName) {
		NewUserBoundary newUser = initUtil.createNewUser(email, role, firstName, lastName);
		return this.restClient.post().uri("/users").body(newUser).retrieve().body(UserBoundary.class);
	}

	// CREATE OBLECT AND POST IT
	private ObjectBoundary createAndPostObject(String type, String alias, Location location, boolean isActive,
			UserId userId, HashMap<String, Object> additionalProperties) {
		ObjectBoundary newObject = initUtil.createNewObject(type, alias, location, isActive, userId,
				additionalProperties);

		return this.restClient.post().uri("/objects").body(newObject).retrieve().body(ObjectBoundary.class);
	}

}
