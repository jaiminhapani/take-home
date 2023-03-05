package fun;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;

@Getter
public class GameData {
	@JsonProperty("id")
	private Integer id;
	@JsonProperty("text")
	private String text;
}