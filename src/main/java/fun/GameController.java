package fun;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import com.github.fge.jsonschema.core.report.ProcessingReport;
import com.github.fge.jsonschema.main.JsonSchema;
import com.github.fge.jsonschema.main.JsonSchemaFactory;

@RestController
public class GameController {

	private static final String template = "Playing %s is fun!";
	private int counter = 1;
	private Map<Integer, Integer> map = new HashMap<>();
	private ObjectMapper mapper = new ObjectMapper();
	private List<Game> games = new ArrayList<>();
	private JsonSchema schema;

	public GameController() throws IOException, ProcessingException {
		// Load the JSON schema for GameData
		String schemaJson = "{ \"type\": \"object\", \"properties\": { \"id\": { \"type\": \"integer\" }, \"text\": { \"type\": \"string\" } }, \"required\": [ \"id\", \"text\" ] }";
		JsonSchemaFactory factory = JsonSchemaFactory.byDefault();
		schema = factory.getJsonSchema(mapper.readTree(schemaJson));
	}

	@RequestMapping("/game")
	public GameData greeting(
			@RequestParam(value = "name", defaultValue = "Sudoku") String name)
			throws IOException, ProcessingException {

		String text = String.format(template, name);
		Integer id = fib(counter++);
		Game game = Game.builder().id(id).text(text).build();

		games.add(game);

		String json = mapper.writeValueAsString(game);

		// Validate the JSON against the schema
		ProcessingReport report = schema.validate(mapper.readTree(json));
		if (!report.isSuccess()) {
			throw new RuntimeException("JSON does not conform to the schema: "
					+ report.toString());
		}

		FileWriter fileWriter = new FileWriter("response.json");
		for (Game g : games) {
			json = mapper.writeValueAsString(g);
			// Write JSON data to file
			fileWriter.write(json);
		}
		fileWriter.close();

		return mapper.readValue(json, GameData.class);
	}

	private int fib(int counter) {
		if (counter == 1 || counter == 2) {
			return 1;
		}
		if (map.containsKey(counter)) {
			return map.get(counter);
		}
		int value = fib(counter - 1) + fib(counter - 2);
		map.put(counter, value);
		return value;
	}
}
