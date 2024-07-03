package demo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.web.client.RestClient;

import demo.utils.InitUtil;

@SpringBootTest(webEnvironment = WebEnvironment.NONE)
public class CommandTests {

	private RestClient restClient;

	@Value("${server.port:8080}")
	public void setPort(int port) {
		this.restClient = RestClient.create("http://localhost:" + port + "/superapp");
		

	}
	

}
	


