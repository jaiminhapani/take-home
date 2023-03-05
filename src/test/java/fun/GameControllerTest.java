package fun;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonschema.core.exceptions.ProcessingException;

@SpringBootTest
@AutoConfigureMockMvc
public class GameControllerTest {

	private MockMvc mockMvc;

	@BeforeEach
	public void setUp() throws IOException, ProcessingException {
		// Set up the MockMvc instance
		mockMvc = MockMvcBuilders.standaloneSetup(new GameController()).build();

	}

	@Test
	public void testGameEndpoint() throws Exception {
		// Send a GET request to the /game endpoint with a name parameter
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/game")
				.param("name", "chess").accept(MediaType.APPLICATION_JSON))
				.andReturn();

		// Verify that the response has a HTTP 200 OK status code
		assertEquals(200, result.getResponse().getStatus());

		// Parse the response JSON into a GameData object
		String responseJson = result.getResponse().getContentAsString();
		ObjectMapper objectMapper = new ObjectMapper();
		GameData gameData = objectMapper.readValue(responseJson,
				GameData.class);

		// Verify that the GameData object has the expected properties
		assertNotNull(gameData.getId());
		assertNotNull(gameData.getText());
		assertTrue(gameData.getText().contains("Playing chess is fun!"));
	}

}
