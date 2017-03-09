package nest;
import java.util.ArrayList;

import com.google.gson.Gson;

public class NestAPI {

	public ArrayList<Device> devices = new ArrayList<>();
	
	
	public class Device {
		ArrayList<Thermostat> theromostats = new ArrayList<>();
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
		return devices.get(0).theromostats.get(0).ambient_temperature_c;
		
	}
}
