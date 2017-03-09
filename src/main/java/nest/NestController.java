package nest;
import java.io.IOException;

import javax.xml.ws.Response;

import org.apache.http.client.fluent.Request;

import com.amazon.speech.ui.LinkAccountCard;
import com.google.inject.Inject;
import com.sun.net.httpserver.Authenticator;

import routing.AlexaController;
import routing.AlexaResponse;
import routing.LinkAccountResponse;
import routing.attributes.FilterFor;
import routing.attributes.Utterances;
import routing.providers.AlexaSessionProvider;
import routing.providers.RequestContextProvider;

public class NestController extends AlexaController {

	  private final RequestContextProvider requestContext;
	  private final AlexaSessionProvider session;


	    @Inject
	    public NestController(RequestContextProvider requestContext, AlexaSessionProvider session) {
	        this.requestContext = requestContext;
	        this.session = session;
	    }
	    @FilterFor({"CurrentTemperature"})
	    @Utterances({
	    	"What is the temperature inside"
	    })
	    public AlexaResponse currentTemperature() throws IOException
	    {
	    	if ( session.getSession().getUser() == null ) {
	    		return new LinkAccountResponse();
	    	}
	    	String token = session.getSession().getUser().getAccessToken();
	    	
	    	if ( token == null ) {
	    		return new LinkAccountResponse();
	    	}
	    	System.out.println("token = "+token);
	    	Request request = Request.Get(Constants.URL_NEST_FIREBASE);
	    	request.addHeader("Authorization", "Bearer "+token);
	    	
	    	String response = request.execute().returnContent().asString();
	    	System.out.println("nest-data: "+response);
	    	int temperature = NestAPI.construct(response).getCurrentTemp();
	    	
	    	return endSessionResponse("Current temperature is "+temperature);
	    }	
}
