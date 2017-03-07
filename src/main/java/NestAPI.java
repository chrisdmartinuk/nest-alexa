import org.json.JSONObject;

public class NestAPI {

	private static final String DEVICES_KEY = "devices";
	private static final String THERMOSTATS_KEY = "thermostats";
	private static final String TEMP_KEY = "ambient_temperature_c";
	
	private final JSONObject nestJSON;

	public NestAPI( String nestData )
	{
		nestJSON = new JSONObject(nestData);
	}
	
	public int getCurrentTemp()
	{
		JSONObject d = (JSONObject)nestJSON.get(DEVICES_KEY);
		JSONObject t = (JSONObject) d.get(THERMOSTATS_KEY);
		JSONObject nestThermo = (JSONObject) t.get(t.keys().next());
		return nestThermo.getInt(TEMP_KEY);
		
	}
}
