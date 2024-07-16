package demo;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.web.client.RestClient;

import demo.boundaries.MiniAppCommandBoundary;
import demo.boundaries.NewUserBoundary;
import demo.boundaries.ObjectBoundary;
import demo.boundaries.UserBoundary;
import demo.objects.Location;
import demo.objects.ObjectId;
import demo.objects.RolesEnum;
import demo.objects.UserId;
import demo.utils.InitUtil;
import jakarta.annotation.PostConstruct;

@SpringBootTest(webEnvironment = WebEnvironment.NONE)
public class CommandTests {

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
	
	// GET ALL NOTES
		@Test
		public void TestgetNotedByTypeAliasAndCreatedBy() {
			UserBoundary superUser = createAndPostUser("az@gmail.com", RolesEnum.SUPERAPP_USER, "adir", "a");
			ObjectBoundary ob = createAndPostObject("note", // Type
					"ACTIVE", // Alias
					new Location(32.158687, 34.795102), // Location
					true, // Active
					superUser.getUserId(), // Created by
					new HashMap<>() // Additional properties
			);
			ObjectBoundary ob2 = createAndPostObject("note", // Type
					"ACTIVE", // Alias
					new Location(32.158687, 34.795102), // Location
					true, // Active
					superUser.getUserId(), // Created by
					new HashMap<>() // Additional properties
			);
			ObjectBoundary ob3 = createAndPostObject("note", // Type
					"ACTIVE", // Alias
					new Location(32.158687, 34.795102), // Location
					true, // Active
					superUser.getUserId(), // Created by
					new HashMap<>() // Additional properties
			);
			UserBoundary MiniAppUser = createAndPostUser("nb@gmail.com", RolesEnum.MINIAPP_USER, "noam", "n");
			HashMap<String,Object> map1 = new HashMap<>();
			map1.put("type", "note");
			map1.put("alias", "ACTIVE");
			MiniAppCommandBoundary m1 = createAndPostCommand
					("getAllObjectsOfSpecificWorker"
					,ob.getObjectId()
					,MiniAppUser.getUserId()
					,map1);
			System.err.println(m1);
			Object[] res =  this.restClient.post().uri("/miniapp/{miniAppName}", m1.getCommand())
				.body(m1).retrieve().body(Object[].class);
			
			UserBoundary adminUser = createAndPostUser("ad@gmail.com", RolesEnum.ADMIN,"ad" , "a");
			
			MiniAppCommandBoundary[] mini = this.restClient.get()
					.uri("/admin/miniapp?userSuperapp={superapp}&userEmail={adminEmail}&size=5&page=0",
							adminUser.getUserId().getSuperapp(), adminUser.getUserId().getEmail())
					.retrieve().body(MiniAppCommandBoundary[].class);

			assertThat(res).usingRecursiveComparison().isEqualTo(m1);
		}
		
		// GET ALL TASK
				@Test
				public void TestgetTaskdByTypeAliasAndCreatedBy() {
					UserBoundary superUser = createAndPostUser("az@gmail.com", RolesEnum.SUPERAPP_USER, "adir", "a");
					ObjectBoundary ob = createAndPostObject("note", // Type
							"ACTIVE", // Alias
							new Location(32.158687, 34.795102), // Location
							true, // Active
							superUser.getUserId(), // Created by
							new HashMap<>() // Additional properties
					);
					ObjectBoundary ob2 = createAndPostObject("note", // Type
							"ACTIVE", // Alias
							new Location(32.158687, 34.795102), // Location
							true, // Active
							superUser.getUserId(), // Created by
							new HashMap<>() // Additional properties
					);
					ObjectBoundary ob3 = createAndPostObject("note", // Type
							"ACTIVE", // Alias
							new Location(32.158687, 34.795102), // Location
							true, // Active
							superUser.getUserId(), // Created by
							new HashMap<>() // Additional properties
					);
					UserBoundary MiniAppUser = createAndPostUser("nb@gmail.com", RolesEnum.MINIAPP_USER, "noam", "n");
					HashMap<String,Object> map1 = new HashMap<>();
					map1.put("type", "task");
					map1.put("alias", "ACTIVE");
					MiniAppCommandBoundary m1 = createAndPostCommand
							("getAllObjectsOfSpecificWorker"
							,ob.getObjectId()
							,MiniAppUser.getUserId()
							,map1);
					System.err.println(m1);
					Object[] res =  this.restClient.post().uri("/miniapp/{miniAppName}", m1.getCommand())
						.body(m1).retrieve().body(Object[].class);
					
					UserBoundary adminUser = createAndPostUser("ad@gmail.com", RolesEnum.ADMIN,"ad" , "a");
					
					MiniAppCommandBoundary[] mini = this.restClient.get()
							.uri("/admin/miniapp?userSuperapp={superapp}&userEmail={adminEmail}&size=5&page=0",
									adminUser.getUserId().getSuperapp(), adminUser.getUserId().getEmail())
							.retrieve().body(MiniAppCommandBoundary[].class);

					assertThat(res).usingRecursiveComparison().isEqualTo(m1);
				}
				
				
				// GET ALL TASK
				@Test
				public void TestgetTaskdByTypeAlias() {
					UserBoundary superUser = createAndPostUser("az@gmail.com", RolesEnum.SUPERAPP_USER, "adir", "a");
					ObjectBoundary ob = createAndPostObject("note", // Type
							"ACTIVE", // Alias
							new Location(32.158687, 34.795102), // Location
							true, // Active
							superUser.getUserId(), // Created by
							new HashMap<>() // Additional properties
					);
					ObjectBoundary ob2 = createAndPostObject("note", // Type
							"ACTIVE", // Alias
							new Location(32.158687, 34.795102), // Location
							true, // Active
							superUser.getUserId(), // Created by
							new HashMap<>() // Additional properties
					);
					ObjectBoundary ob3 = createAndPostObject("note", // Type
							"ACTIVE", // Alias
							new Location(32.158687, 34.795102), // Location
							true, // Active
							superUser.getUserId(), // Created by
							new HashMap<>() // Additional properties
					);
					UserBoundary MiniAppUser = createAndPostUser("nb@gmail.com", RolesEnum.MINIAPP_USER, "noam", "n");
					HashMap<String,Object> map1 = new HashMap<>();
					map1.put("type", "task");
					map1.put("alias", "ACTIVE");
					MiniAppCommandBoundary m1 = createAndPostCommand
							("getAllObjectsByTypeAndAlias"
							,ob.getObjectId()
							,MiniAppUser.getUserId()
							,map1);
					System.err.println(m1);
					Object[] res =  this.restClient.post().uri("/miniapp/{miniAppName}", m1.getCommand())
						.body(m1).retrieve().body(Object[].class);
					
					UserBoundary adminUser = createAndPostUser("ad@gmail.com", RolesEnum.ADMIN,"ad" , "a");
					
					MiniAppCommandBoundary[] mini = this.restClient.get()
							.uri("/admin/miniapp?userSuperapp={superapp}&userEmail={adminEmail}&size=5&page=0",
									adminUser.getUserId().getSuperapp(), adminUser.getUserId().getEmail())
							.retrieve().body(MiniAppCommandBoundary[].class);

					assertThat(res).usingRecursiveComparison().isEqualTo(m1);
				}

		/* ---------------------------------------------FUNCTIONS-------------------------------------------------*/
		
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
		
		private MiniAppCommandBoundary createAndPostCommand(String command,ObjectId objectId,UserId userId,Map<String, Object> commandAttribute) {
			 MiniAppCommandBoundary newCommand = initUtil.createNewCommand(command, objectId, userId, commandAttribute);
			 return newCommand;
			
		}

}
	


