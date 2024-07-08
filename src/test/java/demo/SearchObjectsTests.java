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
	
	  
	  
	  @Test
	    public void testSearchObjectsByAliasWithMiniAppUser() throws Exception {
	        // Create a super user
	        UserBoundary miniAppuser = createAndPostUser("mini@gmail.com", RolesEnum.MINIAPP_USER, "noam", "n");
	        
	        
	        UserBoundary superUser = createAndPostUser("su2@gmail.com", RolesEnum.SUPERAPP_USER, "noam2", "n2");
	        
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
	        
	      ObjectBoundary createdObject =  createAndPostObject(
	            "meeting",  // Different type
	            searchAlias,  // Same Alias
	            new Location(32.159000, 34.795300),  // Different location
	            false,  // Active
	            superUser.getUserId(),  // Created by
	            new HashMap<>()  // Additional properties
	        );

	        // Retrieve objects by alias
	        ObjectBoundary[] retrievedObjects = this.restClient
	            .get()
	            .uri("/objects/search/byAlias/{alias}?userSuperapp={userSuperapp}&userEmail={userEmail}&size={size}&page=0",
	                searchAlias,
	                miniAppuser.getUserId().getSuperapp(),
	                miniAppuser.getUserId().getEmail(),
	                10)
	            .retrieve()
	            .body(ObjectBoundary[].class);

	       
	        assertThat(retrievedObjects)
	            .isNotNull()
	            .usingRecursiveComparison()
	    		.isEqualTo(createdObject);
	    }
	
	  
	  

@Test
public void testSearchForNonExistingAliasReturnsEmptyArray() throws Exception {
		  
		  
		  UserBoundary superUser = createAndPostUser("su@gmail.com", RolesEnum.SUPERAPP_USER, "noam2", "n2");
		  String nonExistentAlias = "non";
		  
		// Perform the GET request to search for objects by type
		  ObjectBoundary[] retrievedObjects = this.restClient
			        .get()
			        .uri("/objects/search/byAlias/{alias}?userSuperapp={userSuperapp}&userEmail={userEmail}&size={size}&page=0",
			                nonExistentAlias,
			                superUser.getUserId().getSuperapp(),
			                superUser.getUserId().getEmail(),
			                10)
			        .retrieve()
			        .body(ObjectBoundary[].class);

			    assertThat(retrievedObjects)
			        .isNotNull()
			        .isEmpty();


		  
		}
		  


@Test
public void testFailToSearchForObjectByAliasWithAdmin() throws Exception {
		  
	UserBoundary superUser = createAndPostUser("su@gmail.com", RolesEnum.SUPERAPP_USER, "noam2", "n2");
		  UserBoundary adminUser = createAndPostUser("adminnn@gmail.com", RolesEnum.ADMIN, "noam222", "n22");
		  String nonExistentAlias = "non";
		  
		  
		  
		  
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
		  
		  
		// Perform the GET request to search for objects by type

	      assertThatThrownBy(() -> {
	            this.restClient
	                .get()
	                .uri("/superapp/objects/search/byElias/{elias}?userSuperapp={userSuperapp}&userEmail={userEmail}&size={size}&page=0",
	                		searchAlias,
	                        adminUser.getUserId().getSuperapp(),
	                        adminUser.getUserId().getEmail(),
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
public void testSearchObjectsByAliasPatternWithSuperappUser() throws Exception {
    // Create a user with a "superapp" role
    UserBoundary superAppUser = createAndPostUser("superapp@gmail.com", RolesEnum.SUPERAPP_USER, "Tal", "Mizrahi");

    // Seed the database with objects whose alias starts with 'e'
    createAndPostObject(
        "schedual",  // Type
        "enter",  // Alias starting with 'e'
        new Location(32.158687, 34.795102),  // Location
        true,  // Active
        superAppUser.getUserId(),  // Created by
        new HashMap<>()  // Additional properties
    );
    createAndPostObject(
        "schedual",  // Type
        "example",  // Another Alias starting with 'e'
        new Location(32.159000, 34.795300),  // Different location
        true,  // Active
        superAppUser.getUserId(),  // Created by
        new HashMap<>()  // Additional properties
    );

    // Execute the GET request to search for objects by alias pattern

    
    
    String searchAliasPattern = "e%";

    // Retrieve objects by alias pattern
    ObjectBoundary[] retrievedObjects = this.restClient
        .get()
        .uri("/objects/search/byAliasPattern/{pattern}?userSuperapp={userSuperapp}&userEmail={userEmail}&size={size}&page={page}",
            searchAliasPattern,
            superAppUser.getUserId().getSuperapp(),
            superAppUser.getUserId().getEmail(),
            10,  // size of the page
            0)   // page number
        .retrieve()
        .body(ObjectBoundary[].class);

    // Assert that the retrieved objects match the pattern
    assertThat(retrievedObjects)
        .as("Check if the server responds with objects whose aliases start with 'e'")
        .isNotNull()
        .allMatch(obj -> obj.getAlias().startsWith("e"))
        .hasSizeGreaterThan(0);  // Adjust according to expected test outcomes
}
    

@Test
public void testSearchObjectsByAliasPatternWithMiniappUser() throws Exception {
    // Create a user with a "miniapp" role
    UserBoundary miniAppUser = createAndPostUser("miniapp@gmail.com", RolesEnum.MINIAPP_USER, "Tal", "Mizrahi");
    UserBoundary superAppUser = createAndPostUser("superapp@gmail.com", RolesEnum.SUPERAPP_USER, "Noam", "Ben");

    // Seed the database with objects whose alias starts with 'e' and are active
    createAndPostObject(
        "schedule",  // Type
        "enter",     // Alias starting with 'e'
        new Location(32.158687, 34.795102),  // Location
        true,        // Active
        superAppUser.getUserId(),  // Created by
        new HashMap<>()  // Additional properties
    );
    createAndPostObject(
        "schedule",  // Type
        "example",   // Another Alias starting with 'e'
        new Location(32.159000, 34.795300),  // Different location
        true,        // Active
        superAppUser.getUserId(),  // Created by
        new HashMap<>()  // Additional properties
    );

    // Execute the GET request to search for objects by alias pattern 'e%'
    String searchAliasPattern = "e%";

    // Retrieve objects by alias pattern
    ObjectBoundary[] retrievedObjects = this.restClient
        .get()
        .uri("/objects/search/byAliasPattern/{pattern}?userSuperapp={userSuperapp}&userEmail={userEmail}&size={size}&page={page}",
            searchAliasPattern,
            miniAppUser.getUserId().getSuperapp(),
            miniAppUser.getUserId().getEmail(),
            10,  // size of the page
            0)   // page number
        .retrieve()
        .body(ObjectBoundary[].class);

    // Assert that the retrieved objects match the pattern and are active
    assertThat(retrievedObjects)
        .as("Check if the server responds with objects whose aliases start with 'e' and are active")
        .isNotNull()
        .allMatch(obj -> obj.getAlias().startsWith("e") && obj.getActive())
        .hasSizeGreaterThan(0);  // Adjust according to expected test outcomes
    
    
}


@Test
public void testSearchObjectsByAliasContainingEWithSuperappUser() throws Exception {
    UserBoundary superAppUser = createAndPostUser("superappUser@gmail.com", RolesEnum.SUPERAPP_USER, "Super", "App");

    createAndPostObject(
        "schedule",
        "meeting",
        new Location(32.158687, 34.795102),
        true,
        superAppUser.getUserId(),
        new HashMap<>()
    );
    createAndPostObject(
        "reminder",
        "conference",
        new Location(32.159000, 34.795300),
        true,
        superAppUser.getUserId(),
        new HashMap<>()
    );

    String searchAliasPattern = "%e%";

    ObjectBoundary[] retrievedObjects = this.restClient
        .get()
        .uri("/objects/search/byAliasPattern/{pattern}?userSuperapp={userSuperapp}&userEmail={userEmail}&size={size}&page={page}",
            searchAliasPattern,
            superAppUser.getUserId().getSuperapp(),
            superAppUser.getUserId().getEmail(),
            10,
            0)
        .retrieve()
        .body(ObjectBoundary[].class);

    assertThat(retrievedObjects)
        .isNotNull()
        .allMatch(obj -> obj.getAlias().contains("e"))
        .hasSizeGreaterThan(0);
}
    
    


@Test
public void testSearchObjectsByAliasContainingEWithMiniappUser() throws Exception {
    UserBoundary miniAppUser = createAndPostUser("miniapp@gmail.com", RolesEnum.MINIAPP_USER, "Mini", "App");
    UserBoundary superAppUser = createAndPostUser("superapp@gmail.com", RolesEnum.SUPERAPP_USER, "Super", "User");

    createAndPostObject(
        "notification",
        "email update",
        new Location(32.115139, 34.817804),
        true,
        superAppUser.getUserId(),
        new HashMap<>()
    );
    createAndPostObject(
        "notification",
        "event reminder",
        new Location(32.116000, 34.818300),
        true,
        superAppUser.getUserId(),
        new HashMap<>()
    );
    
    createAndPostObject(
            "notification",
            "e",
            new Location(32.115139, 34.817804),
            true,
            superAppUser.getUserId(),
            new HashMap<>()
        );
        createAndPostObject(
            "notification",
            "event reminder",
            new Location(32.116000, 34.818300),
            true,
            superAppUser.getUserId(),
            new HashMap<>()
        );

    String searchAliasPattern = "%e%";

    ObjectBoundary[] retrievedObjects = this.restClient
        .get()
        .uri("/objects/search/byAliasPattern/{pattern}?userSuperapp={userSuperapp}&userEmail={userEmail}&size={size}&page={page}",
            searchAliasPattern,
            miniAppUser.getUserId().getSuperapp(),
            miniAppUser.getUserId().getEmail(),
            10,
            0)
        .retrieve()
        .body(ObjectBoundary[].class);

    assertThat(retrievedObjects)
        .isNotNull()
        .allMatch(obj -> obj.getAlias().contains("e") && obj.getActive())
        .hasSizeGreaterThan(0);
}




@Test
public void testSearchObjectsByAliasEndingWithR() throws Exception {
    UserBoundary superAppUser = createAndPostUser("superapp@gmail.com", RolesEnum.SUPERAPP_USER, "Super", "App");

    createAndPostObject(
        "scheduler",  // Type
        "calendar",   // Alias ending with 'r'
        new Location(32.115139, 34.817804),  // Location
        false,        // Active
        superAppUser.getUserId(),  // Created by
        new HashMap<>()  // Additional properties
    );
    createAndPostObject(
        "reminder",  // Type
        "reminder",  // Another Alias ending with 'r'
        new Location(32.158687, 34.795102),  // Different location
        true,  // Active
        superAppUser.getUserId(),  // Created by
        new HashMap<>()  // Additional properties
    );

    String searchAliasPattern = "%R";

    ObjectBoundary[] retrievedObjects = this.restClient
        .get()
        .uri("/objects/search/byAliasPattern/{pattern}?userSuperapp={userSuperapp}&userEmail={userEmail}&size={size}&page={page}",
            searchAliasPattern,
            superAppUser.getUserId().getSuperapp(),
            superAppUser.getUserId().getEmail(),
            10,
            0)
        .retrieve()
        .body(ObjectBoundary[].class);

    assertThat(retrievedObjects)
        .isNotNull()
        .allMatch(obj -> obj.getAlias().toLowerCase().endsWith("r"))
        .hasSizeGreaterThan(0);
}




@Test
public void testSearchObjectsByAliasByMiniAppuserEndingWithR() throws Exception {
    UserBoundary superAppUser = createAndPostUser("superapp@gmail.com", RolesEnum.SUPERAPP_USER, "Super", "App");

   ObjectBoundary createdObjectEndWithsR = createAndPostObject(
            "schedule",  // Type
            "scheduler", // Alias ending with 'r'
            new Location(32.158687, 34.795102),  // Location
            true,        // Active
            superAppUser.getUserId(),  // Created by
            new HashMap<>()  // Additional properties
        );
    
    
   ObjectBoundary createdObjectEndWithsR2 = createAndPostObject(
            "schedule",  // Type
            "schedulea", // Alias ending with 'r'
            new Location(32.158687, 34.795102),  // Location
            true,        // Active
            superAppUser.getUserId(),  // Created by
            new HashMap<>()  // Additional properties
        );
    UserBoundary miniAppUser = createAndPostUser("miniapp@gmail.com", RolesEnum.MINIAPP_USER, "Mini", "App");

    String searchAliasPattern = "%R";

    
    
	assertThat(this.restClient
			.get()
			.uri("/objects/search/byAliasPattern/{pattern}?userSuperapp={userSuperapp}&userEmail={userEmail}&size={size}&page={page}"
					,searchAliasPattern
					, miniAppUser.getUserId().getSuperapp()
					, miniAppUser.getUserId().getEmail()
					, 10,0)
			.retrieve()
			.body(ObjectBoundary[].class))
	.usingRecursiveFieldByFieldElementComparator()
	.containsExactly(createdObjectEndWithsR);

}




@Test
public void testNegetiveSearchObjectsByAliasByMiniAppuserEndingWithR() throws Exception {
    
    UserBoundary superAppUser = createAndPostUser("superapp@gmail.com", RolesEnum.SUPERAPP_USER, "Super", "App");

    
    ObjectBoundary createdObjectEndsWithR = createAndPostObject(
        "schedule",  
        "dasdada", 
        new Location(32.158687, 34.795102),  
        true,        
        superAppUser.getUserId(),  
        new HashMap<>()  
    );
    
    
    ObjectBoundary createdObjectDoesNotEndWithR = createAndPostObject(
        "schedule",  
        "schedulea",
        new Location(32.158687, 34.795102),  
        true,      
        superAppUser.getUserId(),  
        new HashMap<>()  
    );

    
    UserBoundary miniAppUser = createAndPostUser("miniapp@gmail.com", RolesEnum.MINIAPP_USER, "Mini", "App");
    String searchAliasPattern = "%R";

   
    ObjectBoundary[] retrievedObjects = this.restClient
        .get()
        .uri("/objects/search/byAliasPattern/{pattern}?userSuperapp={userSuperapp}&userEmail={userEmail}&size={size}&page={page}",
            searchAliasPattern,
            miniAppUser.getUserId().getSuperapp(),
            miniAppUser.getUserId().getEmail(),
            10,
            0)
        .retrieve()
        .body(ObjectBoundary[].class);

    assertThat(retrievedObjects)
    .isNotNull()
    .isEmpty();

   
}



@Test
public void testNegetiveSearchObjectsByAliasByAdmin() throws Exception {
    
    UserBoundary superAppUser = createAndPostUser("superapp@gmail.com", RolesEnum.SUPERAPP_USER, "Super", "App");

    
    ObjectBoundary createdObjectEndsWithR = createAndPostObject(
        "schedule",  
        "dasdadar", 
        new Location(32.158687, 34.795102),  
        true,        
        superAppUser.getUserId(),  
        new HashMap<>()  
    );
    
    
    ObjectBoundary createdObjectDoesNotEndWithR = createAndPostObject(
        "schedule",  
        "scheduler",
        new Location(32.158687, 34.795102),  
        true,      
        superAppUser.getUserId(),  
        new HashMap<>()  
    );

    
    UserBoundary admin = createAndPostUser("miniapp@gmail.com", RolesEnum.ADMIN, "admin", "adm");
    String searchAliasPattern = "%R";

    assertThatThrownBy(() -> 
	this.restClient
	.delete()
	.uri("/objects/search/byAliasPattern/{pattern}?userSuperapp={userSuperapp}&userEmail={userEmail}&size={size}&page={page}"
			,searchAliasPattern,
            admin.getUserId().getSuperapp(),
            admin.getUserId().getEmail(),
            10,
            0)
	.retrieve()
	.body(void.class))
	.isInstanceOf(HttpStatusCodeException.class)
	.extracting("statusCode")
	.extracting("4xxClientError")
	.isEqualTo(true);
   
}



//SEARCH BY LOCATION

@Test
public void testRetrieveObjectsByLocation() throws Exception {
	UserBoundary superAppUser = createAndPostUser("superapp@gmail.com", RolesEnum.SUPERAPP_USER, "Super", "App");

	Location centralLocation = new Location(32.115139, 34.817804);




	Location outsideLocation = new Location(12.115132,34.817804); // Example location
	ObjectBoundary objectNotWithinDistance = createAndPostObject(
			"schedual",
			"sched",
			outsideLocation,
			true,
			superAppUser.getUserId(),
			new HashMap<>()
			);


	
	Location outsideLocation2 = new Location(2.0,34.817804); // Example location
	ObjectBoundary objectNotWithinDistance2 = createAndPostObject(
			"schedual",
			"sched",
			outsideLocation,
			true,
			superAppUser.getUserId(),
			new HashMap<>()
			);

	
	// Object outside the boundary
	Location InsideRadius = new Location(29.555803, 34.952435); // Example location farther away
	ObjectBoundary objectWithinDistance = createAndPostObject(
			"schedual",
			"exit",
			InsideRadius,
			true,
			superAppUser.getUserId(),
			new HashMap<>()
			);
	
	
	
	


	assertThat(this.restClient
			.get()
			.uri("/objects/search/byLocation/{lat}/{lng}/{distance}?distanceUnits={distanceUnits}&userSuperapp={userSuperapp}&userEmail={userEmail}&size={size}&page={page}",
		        centralLocation.getLat(),
		        centralLocation.getLng(),
		        290,
		        "KILOMETERS",
		        superAppUser.getUserId().getSuperapp(),
		        superAppUser.getUserId().getEmail(),
		        10,
		        0)
			.retrieve()
			.body(ObjectBoundary[].class))
			.usingRecursiveFieldByFieldElementComparator()
			.containsExactly(objectWithinDistance);


}





@Test
public void testRetrieveObjectsByLocationWithObjectInTheCentralLocation() throws Exception {
	UserBoundary superAppUser = createAndPostUser("superapp@gmail.com", RolesEnum.SUPERAPP_USER, "Super", "App");

	Location centralLocation = new Location(32.115139, 34.817804);


	Location outsideLocation = new Location(12.115132,34.817804); // Example location
	ObjectBoundary objectNotWithinDistance = createAndPostObject(
			"schedual",
			"sched",
			outsideLocation,
			true,
			superAppUser.getUserId(),
			new HashMap<>()
			);


	

	ObjectBoundary objectInSameLocation = createAndPostObject(
			"schedual",
			"sched",
			centralLocation,
			true,
			superAppUser.getUserId(),
			new HashMap<>()
			);

	
	// Object outside the boundary
	Location InsideRadius = new Location(29.555803, 34.952435); // Example location farther away
	ObjectBoundary objectWithinDistance = createAndPostObject(
			"schedual",
			"exit",
			InsideRadius,
			true,
			superAppUser.getUserId(),
			new HashMap<>()
			);
	
	
	
	
	assertThat(this.restClient
	        .get()
	        .uri("/objects/search/byLocation/{lat}/{lng}/{distance}?distanceUnits={distanceUnits}&userSuperapp={userSuperapp}&userEmail={userEmail}&size={size}&page={page}",
	            centralLocation.getLat(),
	            centralLocation.getLng(),
	            290,
	            "KILOMETERS",
	            superAppUser.getUserId().getSuperapp(),
	            superAppUser.getUserId().getEmail(),
	            10,
	            0)
	        .retrieve()
	        .body(ObjectBoundary[].class))
	    .usingRecursiveFieldByFieldElementComparator()
	    .containsExactlyInAnyOrder(objectInSameLocation, objectWithinDistance);


}






@Test
public void testRetrieveObjectsWithin30Miles() throws Exception {
    UserBoundary superAppUser = createAndPostUser("superapp@gmail.com", RolesEnum.SUPERAPP_USER, "Super", "App");

    Location centralLocation = new Location(32.115139, 34.817804);

    // Object exactly at the central location
    ObjectBoundary objectAtCenter = createAndPostObject(
        "schedual",
        "enter",
        centralLocation,
        false,  // Active status is false
        superAppUser.getUserId(),
        new HashMap<>()
    );

    // Object within the 30 miles radius
    Location locationWithin30Miles = new Location(32.158687, 34.795102); // Correctly within 30 miles
    ObjectBoundary objectWithin30Miles = createAndPostObject(
        "schedual",
        "enter",
        locationWithin30Miles,
        true,  // Active status is true
        superAppUser.getUserId(),
        new HashMap<>()
    );

    // Object outside the 30 miles radius
    Location locationOutside30Miles = new Location(29.555803, 34.952435); // Adjust this location if necessary
    ObjectBoundary objectOutside30Miles = createAndPostObject(
        "schedual",
        "exit",
        locationOutside30Miles,
        true,
        superAppUser.getUserId(),
        new HashMap<>()
    );

    ObjectBoundary[] retrievedObjects = this.restClient
        .get()
        .uri("/objects/search/byLocation/{lat}/{lng}/{distance}?distanceUnits={distanceUnits}&userSuperapp={userSuperapp}&userEmail={userEmail}&size={size}&page={page}",
            centralLocation.getLat(),
            centralLocation.getLng(),
            30,  // distance in miles
            "MILES",
            superAppUser.getUserId().getSuperapp(),
            superAppUser.getUserId().getEmail(),
            10,
            0)
        .retrieve()
        .body(ObjectBoundary[].class);

    assertThat(retrievedObjects)
        .usingRecursiveFieldByFieldElementComparator()
        .containsExactlyInAnyOrder(objectAtCenter, objectWithin30Miles)
        .doesNotContain(objectOutside30Miles);
}




@Test
public void testRetrieveObjectsWithinMinimalNeutralDistance() throws Exception {
    UserBoundary superAppUser = createAndPostUser("superapp@gmail.com", RolesEnum.SUPERAPP_USER, "Super", "App");

    Location centralLocation = new Location(32.115139, 34.817804);

    // Object exactly at the central location
    ObjectBoundary objectAtCenter = createAndPostObject(
        "schedual",
        "enter",
        centralLocation,
        false,  // Active status is false
        superAppUser.getUserId(),
        new HashMap<>()
    );


    Location nearLocation = new Location(32.115139, 34.817804); 
    ObjectBoundary objectNearby = createAndPostObject(
        "schedual",
        "enter",
        nearLocation,
        true,  // Active status is true
        superAppUser.getUserId(),
        new HashMap<>()
    );

    // Object outside the "neutral" minimal distance
    Location outsideLocation = new Location(29.116000, 34.818000); // Clearly outside the "neutral" range
    createAndPostObject(
        "schedual",
        "exit",
        outsideLocation,
        true,
        superAppUser.getUserId(),
        new HashMap<>()
    );

    ObjectBoundary[] retrievedObjects = this.restClient
        .get()
        .uri("/objects/search/byLocation/{lat}/{lng}/{distance}?distanceUnits={distanceUnits}&userSuperapp={userSuperapp}&userEmail={userEmail}&size={size}&page={page}",
            centralLocation.getLat(),
            centralLocation.getLng(),
            0.01,  // very minimal distance in a "neutral" unit
            "NEUTRAL",  // distance unit
            superAppUser.getUserId().getSuperapp(),
            superAppUser.getUserId().getEmail(),
            10,
            0)
        .retrieve()
        .body(ObjectBoundary[].class);

    assertThat(retrievedObjects)
        .usingRecursiveFieldByFieldElementComparator()
        .containsExactlyInAnyOrder(objectAtCenter, objectNearby)
        .as("Check that only objects within a minimal 'neutral' distance are returned and match the given details.");
}





@Test
public void testRetrieveObjectsByLocationByMiniappUser() throws Exception {
	UserBoundary superAppUser = createAndPostUser("superapp@gmail.com", RolesEnum.SUPERAPP_USER, "Super", "App");

	Location centralLocation = new Location(32.115139, 34.817804);




	Location outsideLocation = new Location(12.115132,34.817804); // Example location
	ObjectBoundary objectNotWithinDistance = createAndPostObject(
			"schedual",
			"sched",
			outsideLocation,
			true,
			superAppUser.getUserId(),
			new HashMap<>()
			);


	
	Location outsideLocation2 = new Location(2.0,34.817804); // Example location
	ObjectBoundary objectNotWithinDistance2 = createAndPostObject(
			"schedual",
			"sched",
			outsideLocation,
			true,
			superAppUser.getUserId(),
			new HashMap<>()
			);

	
	// Object outside the boundary
	Location InsideRadius = new Location(29.555803, 34.952435); // Example location farther away
	ObjectBoundary objectWithinDistance = createAndPostObject(
			"schedual",
			"exit",
			InsideRadius,
			true,
			superAppUser.getUserId(),
			new HashMap<>()
			);
	
	
	
	
	UserBoundary miniappUser = createAndPostUser("mini@gmail.com", RolesEnum.MINIAPP_USER, "mini", "App");

	assertThat(this.restClient
			.get()
			.uri("/objects/search/byLocation/{lat}/{lng}/{distance}?distanceUnits={distanceUnits}&userSuperapp={userSuperapp}&userEmail={userEmail}&size={size}&page={page}",
		        centralLocation.getLat(),
		        centralLocation.getLng(),
		        290,
		        "KILOMETERS",
		        miniappUser.getUserId().getSuperapp(),
		        miniappUser.getUserId().getEmail(),
		        10,
		        0)
			.retrieve()
			.body(ObjectBoundary[].class))
			.usingRecursiveFieldByFieldElementComparator()
			.containsExactly(objectWithinDistance);


}



@Test
public void testRetrieveActiveObjectsWithin30MilesByMiniappUser() throws Exception {
    // Create a superapp user to create objects
    UserBoundary superAppUser = createAndPostUser("superapp@gmail.com", RolesEnum.SUPERAPP_USER, "Super", "App");

    // Central location for the search
    Location centralLocation = new Location(32.115139, 34.817804);

    // Object at the central location but with active status set to false (should not be retrieved by the miniapp user)
    createAndPostObject(
        "schedual",
        "centerInactive",
        centralLocation,
        false,  // Active status is false
        superAppUser.getUserId(),
        new HashMap<>()
    );

    // Object within the 30 miles radius, active is true (should be retrieved)
    Location locationWithin30Miles = new Location(32.158687, 34.795102);
    ObjectBoundary objectWithin30Miles = createAndPostObject(
        "schedual",
        "centerActive",
        locationWithin30Miles,
        true,  // Active status is true
        superAppUser.getUserId(),
        new HashMap<>()
    );

    // Create a miniapp user to perform the search
    UserBoundary miniappUser = createAndPostUser("mini@gmail.com", RolesEnum.MINIAPP_USER, "Mini", "App");

    // Perform the search with the miniapp user
    ObjectBoundary[] retrievedObjects = this.restClient
        .get()
        .uri("/objects/search/byLocation/{lat}/{lng}/{distance}?distanceUnits={distanceUnits}&userSuperapp={userSuperapp}&userEmail={userEmail}&size={size}&page={page}",
            centralLocation.getLat(),
            centralLocation.getLng(),
            30,  // Distance in miles
            "MILES",  // Distance unit
            miniappUser.getUserId().getSuperapp(),
            miniappUser.getUserId().getEmail(),
            10,  // Size of the result set
            0)   // Page number
        .retrieve()
        .body(ObjectBoundary[].class);

    // Assert that only active objects within 30 miles are returned
    assertThat(retrievedObjects)
        .usingRecursiveFieldByFieldElementComparator()
        .containsExactly(objectWithin30Miles)
        .as("Check that only active objects within 30 miles are returned and match the given details.");
}


@Test
public void testRetrieveActiveObjectsWithinMinimalNeutralDistanceByMiniappUser() throws Exception {
    UserBoundary superAppUser = createAndPostUser("superapp@gmail.com", RolesEnum.SUPERAPP_USER, "Super", "App");
    UserBoundary miniAppUser = createAndPostUser("miniapp@gmail.com", RolesEnum.MINIAPP_USER, "Mini", "App");

    Location centralLocation = new Location(32.115139, 34.817804);

    // Object exactly at the central location and active
    ObjectBoundary objectActiveAtCenter = createAndPostObject(
        "schedual",
        "enterActive",
        centralLocation,
        true,  // Active status
        superAppUser.getUserId(),
        new HashMap<>()
    );

    // Object exactly at the central location but inactive, should not be retrieved by a miniapp user
    createAndPostObject(
        "schedual",
        "enterInactive",
        centralLocation,
        false,  // Inactive status
        superAppUser.getUserId(),
        new HashMap<>()
    );

    ObjectBoundary[] retrievedObjects = this.restClient
        .get()
        .uri("/objects/search/byLocation/{lat}/{lng}/{distance}?distanceUnits={distanceUnits}&userSuperapp={userSuperapp}&userEmail={userEmail}&size={size}&page={page}",
            centralLocation.getLat(),
            centralLocation.getLng(),
            0.01,  // very minimal distance in a "neutral" unit
            "NEUTRAL",  // distance unit
            miniAppUser.getUserId().getSuperapp(),
            miniAppUser.getUserId().getEmail(),
            10,
            0)
        .retrieve()
        .body(ObjectBoundary[].class);

    assertThat(retrievedObjects)
        .as("Check that only active objects within a minimal 'neutral' distance are returned to the miniapp user")
        .usingRecursiveFieldByFieldElementComparator()
        .containsExactly(objectActiveAtCenter);  // Correct method to ensure exactly these objects are in the result
}




@Test
public void testNegativeRetrieveActiveObjectsWithinMinimalNeutralDistanceByAdmin() throws Exception {
    UserBoundary superAppUser = createAndPostUser("superapp@gmail.com", RolesEnum.SUPERAPP_USER, "Super", "App");
   
    Location centralLocation = new Location(32.115139, 34.817804);

    // Object exactly at the central location and active
    ObjectBoundary objectActiveAtCenter = createAndPostObject(
        "schedual",
        "enterActive",
        centralLocation,
        true,  // Active status
        superAppUser.getUserId(),
        new HashMap<>()
    );

    // Object exactly at the central location but inactive, should not be retrieved by a miniapp user
    createAndPostObject(
        "schedual",
        "enterInactive",
        centralLocation,
        false,  // Inactive status
        superAppUser.getUserId(),
        new HashMap<>()
    );
    
    UserBoundary admin = createAndPostUser("miniapp@gmail.com", RolesEnum.ADMIN, "admin", "adm");

    assertThatThrownBy(() -> this.restClient
            .get()
            .uri("/objects/search/byLocation/{lat}/{lng}/{distance}?distanceUnits={distanceUnits}&userSuperapp={userSuperapp}&userEmail={userEmail}&size={size}&page={page}",
                centralLocation.getLat(),
                centralLocation.getLng(),
                0.01,  // very minimal distance in a "neutral" unit
                "NEUTRAL",  // distance unit
                admin.getUserId().getSuperapp(),
                admin.getUserId().getEmail(),
                10,
                0)
            .retrieve()
            .body(ObjectBoundary[].class))
        .isInstanceOf(HttpStatusCodeException.class)
        .hasMessageContaining("403");  // Assuming the exception message contains a hint about 4XX client error
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



	
	
	
	

