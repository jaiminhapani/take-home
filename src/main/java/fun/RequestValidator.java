package fun;

import java.io.IOException;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import com.github.fge.jsonschema.main.JsonSchema;
import com.github.fge.jsonschema.main.JsonSchemaFactory;

public class RequestValidator {

	public static boolean isValid(String json)
			throws IOException, ProcessingException {
		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode jsonNode = objectMapper.readTree(json);

		JsonSchemaFactory schemaFactory = JsonSchemaFactory.byDefault();
		JsonSchema schema = schemaFactory.getJsonSchema(objectMapper
				.readTree(objectMapper.writeValueAsString(new GameData())));

		return schema.validate(jsonNode).isSuccess();
	}

}
