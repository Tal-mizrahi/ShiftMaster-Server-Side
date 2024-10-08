package demo;

import org.hibernate.engine.transaction.jta.platform.internal.WebSphereJtaPlatform.WebSphereEnvironment;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.web.client.RestClient;

@SpringBootTest(webEnvironment = WebEnvironment.NONE)
class ApplicationTests {
	private RestClient restClient;
	
	
	@Value("${server.port:8080}")
	public void setPort(int port) {
		this.restClient = RestClient.create("http://localhost:" + port + "/superapp");
		
	}

	@Test
	void contextLoads() {
	}
	
	@BeforeEach
	@AfterEach
	public void setUp() {
		
	}


}
