package nest.api;

import nest.api.deseralise.SeralizableEnum;

public enum HomeAway implements SeralizableEnum<HomeAway> {
	HOME("home"), AWAY("away");

	private String jsonValue;

	private HomeAway(String value) {
		this.jsonValue = value;
	}

	@Override
	public String getJSONValue() {
		return jsonValue;
	}

}
