package nest.api.deseralise;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import nest.api.TemperatureUnit;

public class TemperatureUnitDeserialiser implements JsonDeserializer<TemperatureUnit> {

	@Override
	public TemperatureUnit deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
			throws JsonParseException {
		return TemperatureUnit.construct(json.getAsString());
	}

}
