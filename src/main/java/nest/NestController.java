package nest;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;

import com.google.inject.Inject;

import nest.api.NestAPI;
import nest.api.TemperatureUnit;
import routing.AlexaController;
import routing.AlexaResponse;
import routing.LinkAccountResponse;
import routing.attributes.FilterFor;
import routing.attributes.Utterances;
import routing.providers.AlexaSessionProvider;
import routing.providers.RequestContextProvider;

public class NestController extends AlexaController {

	private static final String AUTH = "Authorization";
	private static final String BEARER = "Bearer ";
	private final RequestContextProvider requestContext;
	private final AlexaSessionProvider session;

	@Inject
	public NestController(RequestContextProvider requestContext, AlexaSessionProvider session) {
		this.requestContext = requestContext;
		this.session = session;
	}

	/**
	 * Generate a request to NEST with correct authorisation header. Returns
	 * null if cannot find access token
	 * 
	 * @return request with authorisation header, or null if cannot find the
	 *         access token to authorise with nest. In which case
	 *         {@link LinkAccountResponse} should be returned to alexa.
	 */
	private Request prepareAuthorisedRequest() {
		if (session.getSession().getUser() == null) {
			return null;
		}
		Request request = Request.Get(Constants.URL_NEST_FIREBASE);
		return addAuthorisation(request);
	}

	private Request addAuthorisation(Request request) {
		String token = session.getSession().getUser().getAccessToken();
		if (token == null) {
			return null;
		}
		return request.addHeader(AUTH, BEARER + token);
	}

	@FilterFor({ "CurrentTemperature" })
	@Utterances({ "What is the temperature inside" })
	public AlexaResponse currentTemperature() throws IOException {
		Request request = prepareAuthorisedRequest();
		if (request == null) {
			return new LinkAccountResponse();
		}
		String response = request.execute().returnContent().asString();
		NestAPI nestAPI = NestAPI.construct(response);
		double temperature = nestAPI.getCurrentTemp();
		TemperatureUnit unit = nestAPI.getCurrentUnit();

		return endSessionResponse("Current temperature is " + temperature + " degrees " + unit);
	}

	@FilterFor({ "SetAway" })
	@Utterances({ "I'm going out" })
	public AlexaResponse setAway() throws IOException {
		Request request = prepareAuthorisedRequest();
		if (request == null) {
			return new LinkAccountResponse();
		}
		String response = request.execute().returnContent().asString();
		NestAPI nestAPI = NestAPI.construct(response);

		String url = Constants.URL_NEST_FIREBASE + "devices/thermostats/" + nestAPI.getCurrentDeviceID();
		request = addAuthorisation(Request.Patch(url));

		ContentType contentType = ContentType.parse("application/octet-stream");
		String value = "{\"away\": \"away\"}";
		request.bodyString(value, contentType);
		HttpResponse returnResponse = request.execute().returnResponse();
		if (returnResponse.getStatusLine().getStatusCode() == 200) {
			return endSessionResponse("Heating set to away");
		} else {
			return endSessionResponse("Failed to update heating");
		}

	}
}
