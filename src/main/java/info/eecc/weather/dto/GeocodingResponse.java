package info.eecc.weather.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class GeocodingResponse {
	private List<Result> results;

	@Data
	public static class Result {
		private double latitude;
		private double longitude;
		private String name;
		private String country;
		@JsonProperty("admin1")
		private String admin1;
		@JsonProperty("admin2")
		private String admin2;
	}
}
