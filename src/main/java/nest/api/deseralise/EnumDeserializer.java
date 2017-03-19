package nest.api.deseralise;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

public class EnumDeserializer<E extends Enum<E> & SeralizableEnum<E>> implements JsonDeserializer<E> {
	private final E[] enums;

	public EnumDeserializer(E[] Es) {
		this.enums = Es;
	}

	@Override
	public E deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
		for (E e : enums) {
			if (json.getAsString().equals(e.getJSONValue())) {
				return e;
			}
		}
		return null;
	}

}
