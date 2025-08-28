package info.eecc.weather.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class WeatherResponse {
	private double latitude;
	private double longitude;
	@JsonProperty("current")
	private CurrentWeather currentWeather;
	@JsonProperty("current_units")
	private CurrentWeatherUnits currentWeatherUnits;

	@Data
	public static class CurrentWeather {
		private String time;
		private int interval;
		private double temperature_2m;
		private int relative_humidity_2m;
		private double apparent_temperature;
		private int is_day;
		private int weather_code;
		private double wind_speed_10m;
		private int wind_direction_10m;
		private double surface_pressure;
		private int cloud_cover;
	}

	@Data
	public static class CurrentWeatherUnits {
		private String time;
		private String interval;
		private String temperature_2m;
		private String relative_humidity_2m;
		private String apparent_temperature;
		private String is_day;
		private String weather_code;
		private String wind_speed_10m;
		private String wind_direction_10m;
		private String surface_pressure;
		private String cloud_cover;
	}
}
