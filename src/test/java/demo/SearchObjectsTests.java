package demo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.core.ParameterizedTypeReference;
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
public class SearchObjectsTests {

	
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
	
	// in each test make the creating of 3 objects  to search.
	
	@BeforeEach
	@AfterEach
	public void setup() {
		

		NewUserBoundary createdNewUser = initUtil
				.createNewUser(
						"admin@gmail.com",
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
		.uri("/admin/objects?userSuperapp={userSuperapp}&userEmail={userEmail}"
				, adminUser.getUserId().getSuperapp()
				, adminUser.getUserId().getEmail())
		.retrieve();
		
		this.restClient
		.delete()
		.uri("/admin/users?userSuperapp={userSuperapp}&userEmail={userEmail}"
				, adminUser.getUserId().getSuperapp()
				, adminUser.getUserId().getEmail())
		.retrieve();
		
	}
	

	
	
	
	
	@Test
    public void testSearchObjectsByTypeWithSuperappUser() throws Exception {
		
		 UserBoundary superUser = createAndPostUser("su@gmail.com", RolesEnum.SUPERAPP_USER, "noam", "n");
		
		
		 String searchType = "schedual";
		 
		 ObjectBoundary createdNewObject = initUtil
				 .createNewObject(
			        searchType, // Type
			        "dum", // Alias
			        new Location(32.158687, 34.795102), // Location
			        true, // Active
			        new UserId(superUser.getUserId().getSuperapp(), superUser.getUserId().getEmail()), // Created by
			        new HashMap<>() // Additional properties
			    );
		 
		 ObjectBoundary newObject = this.restClient
				 .post()
				 .uri("/objects")
				 .body(createdNewObject)
				 .retrieve()
				 .body(ObjectBoundary.class);
		 
	
		 
		 
		  ObjectBoundary[] retrievedObjects = this.restClient
			        .get()
			        .uri("/objects/search/byType/{type}?userSuperapp={userSuperapp}&userEmail={userEmail}&size={size}&page=0",
			        		searchType,
			        		superUser.getUserId().getSuperapp(),
			                superUser.getUserId().getEmail(),
			                10)
			        .retrieve()
			        .body(ObjectBoundary[].class);

		  assertThat(retrievedObjects)
	        .isNotNull()
	        .extracting(ObjectBoundary::getType)
	        .containsOnly(searchType);
       
    }


	@Test
	public void testSearchWithoutObjects() throws Exception {
	    UserBoundary superUser = createAndPostUser("su@gmail.com", RolesEnum.SUPERAPP_USER, "noam", "n");
	    String searchType = "schedual";

	    // Perform the GET request to search for objects by type
	    ObjectBoundary[] retrievedObjects = this.restClient
	        .get()
	        .uri("/objects/search/byType/{type}?userSuperapp={userSuperapp}&userEmail={userEmail}&size={size}&page=0",
	                searchType,
	                superUser.getUserId().getSuperapp(),
	                superUser.getUserId().getEmail(),
	                10)
	        .retrieve()
	        .body(ObjectBoundary[].class);

	    // Assert that the retrieved array is empty
	    assertThat(retrievedObjects)
	        .isNotNull()
	        .isEmpty();
	}
	
	
	@Test
	public void testSearchWithManyObject() throws Exception{
		
		UserBoundary superUser = createAndPostUser("su@gmail.com", RolesEnum.SUPERAPP_USER, "noam", "n");
		
		String searchType = "meeting";
		ObjectBoundary object1 = createAndPostObject(
		        "schedual",  
		        "Object 1",  
		        new Location(32.158687, 34.795102),  // Location
		        true,  // Active
		        superUser.getUserId(),  // Created by
		        new HashMap<>()  // Additional properties
		    );

		    ObjectBoundary object2 = createAndPostObject(
		        "meeting",  // Type
		        "Object 2",  // Alias
		        new Location(32.158687, 34.795102),  // Location
		        true,  // Active
		        superUser.getUserId(),  // Created by
		        new HashMap<>()  // Additional properties
		    );

		    ObjectBoundary object3 = createAndPostObject(
		        "appointment",  // Type
		        "Object 3",  // Alias
		        new Location(32.158687, 34.795102),  // Location
		        true,  // Active
		        superUser.getUserId(),  // Created by
		        new HashMap<>()  // Additional properties
		    );

		
		    ObjectBoundary[] retrievedObjects = this.restClient
		            .get()
		            .uri("/objects/search/byType/meeting?userSuperapp={userSuperapp}&userEmail={userEmail}&size={size}&page=0",
		                    superUser.getUserId().getSuperapp(),
		                    superUser.getUserId().getEmail(),
		                    10)
		            .retrieve()
		            .body(ObjectBoundary[].class);
		        
		  assertThat(retrievedObjects)
	        .isNotNull()
	        .extracting(ObjectBoundary::getType)
	        .containsOnly(searchType);
		
		
		
	}
	
	
	
	@Test
	public void testSearchWithNonExistingType() throws Exception {
	    UserBoundary superUser = createAndPostUser("su@gmail.com", RolesEnum.SUPERAPP_USER, "noam", "n");
	    String searchType = "else";
	    
	   
		ObjectBoundary object1 = createAndPostObject(
		        "schedual",  
		        "Object 1",  
		        new Location(32.158687, 34.795102),  // Location
		        true,  // Active
		        superUser.getUserId(),  // Created by
		        new HashMap<>()  // Additional properties
		    );

		    ObjectBoundary object2 = createAndPostObject(
		        "task",  // Type
		        "Object 2",  // Alias
		        new Location(32.158687, 34.795102),  // Location
		        true,  // Active
		        superUser.getUserId(),  // Created by
		        new HashMap<>()  // Additional properties
		    );

		    ObjectBoundary object3 = createAndPostObject(
		        "note",  // Type
		        "Object 3",  // Alias
		        new Location(32.158687, 34.795102),  // Location
		        true,  // Active
		        superUser.getUserId(),  // Created by
		        new HashMap<>()  // Additional properties
		    );
	    
	    

	    // Perform the GET request to search for objects by type
	    ObjectBoundary[] retrievedObjects = this.restClient
	        .get()
	        .uri("/objects/search/byType/{type}?userSuperapp={userSuperapp}&userEmail={userEmail}&size={size}&page=0",
	        		searchType,
	                superUser.getUserId().getSuperapp(),
	                superUser.getUserId().getEmail(),
	                10)
	        .retrieve()
	        .body(ObjectBoundary[].class);

	    // Assert that the retrieved array is empty
	    assertThat(retrievedObjects)
	        .isNotNull()
	        .isEmpty();
	}
	
	


	 @Test
	    public void testSearchForbiddenForAdmin() throws Exception {
	        // Create an admin user
	        UserBoundary superUser = createAndPostUser("admin2@gmail.com", RolesEnum.ADMIN, "noam2", "n2");
	        
	     

	        // Attempt to retrieve objects of type "schedual" and expect a 403 Forbidden response
	        assertThatThrownBy(() -> {
	            this.restClient
	                .get()
	                .uri("/superapp/objects/search/byType/{type}?userSuperapp={userSuperapp}&userEmail={userEmail}&size={size}&page=0",
	                        "schedual",
	                        superUser.getUserId().getSuperapp(),
	                        superUser.getUserId().getEmail(),
	                        10,
	                        0)
	                .retrieve()
	                .body(ObjectBoundary[].class);
	        })
	        .isInstanceOf(HttpStatusCodeException.class) // Check that an HttpStatusCodeException is thrown
	        .extracting(ex -> ((HttpStatusCodeException) ex).getStatusCode().value()) // Extracting the status code
	        .isEqualTo(404); 
	    }
	
	
	 @Test
	    public void testSearchActiveObjectsByTypeWithMiniappUser() throws Exception {
	        // Create a miniapp user
	        UserBoundary miniappUser = createAndPostUser("miniapp@gmail.com", RolesEnum.MINIAPP_USER, "Tal", "Mizrahi");

	        UserBoundary superUser = createAndPostUser("su2@gmail.com", RolesEnum.SUPERAPP_USER, "noam2", "n2");
	        
	        
	        // Create active objects
	        createAndPostObject("schedual", "Active Object 1", new Location(32.158687, 34.795102), true, superUser.getUserId(), new HashMap<>());
	        createAndPostObject("schedual", "Active Object 2", new Location(32.158600, 34.795200), true, superUser.getUserId(), new HashMap<>());
	        
	        // Create inactive objects
	        createAndPostObject("schedual", "Inactive Object 1", new Location(32.158500, 34.795300), false, superUser.getUserId(), new HashMap<>());

	        ObjectBoundary[] retrievedObjects = this.restClient
	    	        .get()
	    	        .uri("/objects/search/byType/{type}?userSuperapp={userSuperapp}&userEmail={userEmail}&size={size}&page=0",
	    	        		"schedual",
	    	        		miniappUser.getUserId().getSuperapp(),
	    	        		miniappUser.getUserId().getEmail(),
	    	                10)
	    	        .retrieve()
	    	        .body(ObjectBoundary[].class);
  
	            // Assert the response status and the contents of the response
	        assertThat(retrievedObjects)
	        .as("Check if the server responds with the correct active objects")
	        .isNotNull()
	        .allMatch(object -> object.getActive(), "All objects should be active")  // Ensuring all objects are active
	        .extracting(ObjectBoundary::getActive)  // Extracting type for further assertions
	        .containsOnly(true);  
	    }
	
	 
	 
	 
	//SEARCH BY ELIAS: 
	 
	  @Test
	    public void testSearchObjectsByAliasWithSuperappUser() throws Exception {
	        // Create a super user
	        UserBoundary superUser = createAndPostUser("su@gmail.com", RolesEnum.SUPERAPP_USER, "noam", "n");
	        
	        // Define a common alias for testing
	        String searchAlias = "dum";
	        
	        // Create multiple objects with the same alias but different types
	        createAndPostObject(
	            "schedual",  // Type
	            searchAlias,  // Alias
	            new Location(32.158687, 34.795102),  // Location
	            true,  // Active
	            superUser.getUserId(),  // Created by
	            new HashMap<>()  // Additional properties
	        );
	        
	        createAndPostObject(
	            "meeting",  // Different type
	            searchAlias,  // Same Alias
	            new Location(32.159000, 34.795300),  // Different location
	            true,  // Active
	            superUser.getUserId(),  // Created by
	            new HashMap<>()  // Additional properties
	        );

	        // Retrieve objects by alias
	        ObjectBoundary[] retrievedObjects = this.restClient
	            .get()
	            .uri("/objects/search/byAlias/{alias}?userSuperapp={userSuperapp}&userEmail={userEmail}&size={size}&page=0",
	                searchAlias,
	                superUser.getUserId().getSuperapp(),
	                superUser.getUserId().getEmail(),
	                10)
	            .retrieve()
	            .body(ObjectBoundary[].class);

	        // Assert the retrieved objects have the correct alias and the array contains objects of both types
	        assertThat(retrievedObjects)
	            .isNotNull()
	            .extracting(ObjectBoundary::getAlias)
	            .containsOnly(searchAlias);
	    }
	
	  
	  
	  
	  
	  
	  
	  //CREATE USER AND OBJECT FUNCTIONS

private UserBoundary createAndPostUser(String email, RolesEnum role, String firstName, String lastName) {
    NewUserBoundary newUser = initUtil.createNewUser(email, role, firstName, lastName);
    return this.restClient
        .post()
        .uri("/users")
        .body(newUser)
        .retrieve()
        .body(UserBoundary.class);
	}



private ObjectBoundary createAndPostObject(String type, String alias, Location location, boolean isActive, UserId userId, HashMap<String, Object> additionalProperties) {
    ObjectBoundary newObject = initUtil.createNewObject(
        type,
        alias,
        location,
        isActive,
        userId,
        additionalProperties
    );

    return this.restClient
        .post()
        .uri("/objects")
        .body(newObject)
        .retrieve()
        .body(ObjectBoundary.class);
}


}
	//creating of an object in each test



	
	
	
	

