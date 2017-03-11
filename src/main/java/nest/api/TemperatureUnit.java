package nest.api;

public enum TemperatureUnit {

	CELCIUS("C"), FAHRENHEIT("F");

	private String jsonValue;

	private TemperatureUnit(String value) {
		this.jsonValue = value;
	}

	public static TemperatureUnit construct(String value) {
		if (value.equals(CELCIUS.jsonValue)) {
			return CELCIUS;
		} else {
			return FAHRENHEIT;
		}
	}
}
