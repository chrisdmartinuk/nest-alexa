package nest.api;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import nest.api.deseralise.EnumDeserializer;

public class NestAPI {

	public DeviceCollection devices;
	public Map<String, Structure> structures;
	private Thermostat currentThermostat;
	private String currentID;

	public class Structure {
		private Set<String> thermostats;
		private HomeAway away;

	}

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
		gsonBuilder.registerTypeAdapter(TemperatureUnit.class, new EnumDeserializer<>(TemperatureUnit.values()));
		gsonBuilder.registerTypeAdapter(HomeAway.class, new EnumDeserializer<>(HomeAway.values()));
		Gson gson = gsonBuilder.create();

		NestAPI api = gson.fromJson(nestData, NestAPI.class);
		Entry<String, Thermostat> entry = api.devices.thermostats.entrySet().iterator().next();
		api.currentThermostat = entry.getValue();
		api.currentID = entry.getKey();
		return api;
	}

	private NestAPI() {

	}

	public TemperatureUnit getCurrentUnit() {
		return currentThermostat.temperature_scale;
	}

	public String getCurrentDeviceID() {
		return currentID;
	}

	public void setCurrentThermostat(String deviceID) {
		for (Entry<String, Thermostat> thermostat : devices.thermostats.entrySet()) {
			if (thermostat.getKey().equals(deviceID)) {
				currentID = thermostat.getKey();
				currentThermostat = thermostat.getValue();
				return;
			}
		}
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
