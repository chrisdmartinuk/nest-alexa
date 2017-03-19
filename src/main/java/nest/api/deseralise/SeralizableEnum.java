package nest.api.deseralise;

public interface SeralizableEnum<E extends Enum<E>> {
	public String getJSONValue();
}
