package nest.api;

import nest.api.deseralise.SeralizableEnum;

public enum TemperatureUnit implements SeralizableEnum<TemperatureUnit> {

	CELCIUS("C"), FAHRENHEIT("F");

	private String jsonValue;

	private TemperatureUnit(String value) {
		this.jsonValue = value;
	}

	@Override
	public String getJSONValue() {
		return jsonValue;
	}

}
