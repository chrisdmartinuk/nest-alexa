package nest.api;

import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import nest.api.deseralise.TemperatureUnitDeserialiser;

public class NestAPI {

	public DeviceCollection devices;
	private Thermostat currentThermostat;

	public class DeviceCollection {
		Map<String, Thermostat> thermostats;
	}

	public class Thermostat {
		double ambient_temperature_c;
		double ambient_temperature_f;
		TemperatureUnit temperature_scale;
	}

	public static NestAPI construct(String nestData) {
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapter(TemperatureUnit.class, new TemperatureUnitDeserialiser());
		Gson gson = gsonBuilder.create();

		NestAPI api = gson.fromJson(nestData, NestAPI.class);
		api.currentThermostat = api.devices.thermostats.entrySet().iterator().next().getValue();
		return api;
	}

	private NestAPI() {

	}

	public TemperatureUnit getCurrentUnit() {
		return currentThermostat.temperature_scale;
	}

	public double getCurrentTemp() {
		switch (getCurrentUnit()) {
		default:
		case CELCIUS:
			return currentThermostat.ambient_temperature_c;
		case FAHRENHEIT:
			return currentThermostat.ambient_temperature_f;
		}
	}
}
