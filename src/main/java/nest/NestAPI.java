package nest;
import java.util.Map;

import com.google.gson.Gson;

public class NestAPI {

	public DeviceCollection devices;
	
	
	public class DeviceCollection {
		Map<String, Thermostat> thermostats;
	}
	
	public class Thermostat {
		double ambient_temperature_c;
	}
	

	public static NestAPI construct( String nestData )
	{
		Gson gson = new Gson();
		return gson.fromJson(nestData, NestAPI.class);
	}
	
	private NestAPI()
	{
		
	}
	
	public double getCurrentTemp()
	{
		return devices.thermostats.entrySet().iterator().next().getValue().ambient_temperature_c;
	}
}
