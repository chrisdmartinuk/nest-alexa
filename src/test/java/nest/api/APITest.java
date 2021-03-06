package nest.api;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class APITest {

	private NestAPI nestAPI;

	@Test
	public void testCurrentTemp() {
		Assert.assertEquals(21.5, nestAPI.getCurrentTemp(), 0.1);
	}

	@Test
	public void testTempUnit() {
		Assert.assertEquals(TemperatureUnit.CELCIUS, nestAPI.getCurrentUnit());
	}

	@Before
	public void read() {
		try {
			List<String> lines = Files
					.readAllLines(Paths.get("C:\\Users\\Chris\\git\\nest-alexa\\src\\test\\java\\testData.txt"));
			String data = lines.stream().collect(Collectors.joining(" "));
			nestAPI = NestAPI.construct(data);
		} catch (IOException e) {
			e.printStackTrace();
			Assert.fail();
		}
	}

}
