package nest;
import com.google.gson.Gson;

public class NestAPI {

	public Device[] devices;
	
	
	public class Device {
		Thermostat[] theromostats;
	}
	
	public class Thermostat {
		int ambient_temperature_c;
	}
	

	public static NestAPI construct( String nestData )
	{
		Gson gson = new Gson();
		return gson.fromJson(nestData, NestAPI.class);
	}
	

	
	public int getCurrentTemp()
	{
		return devices[0].theromostats[0].ambient_temperature_c;
		
	}
}
