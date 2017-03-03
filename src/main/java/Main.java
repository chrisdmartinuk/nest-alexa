import static spark.Spark.get;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import okhttp3.Authenticator;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.Route;

public class Main {
	
	
	private static final String REDIRECT_URI = "/callback";
	public static final MediaType JSON
    = MediaType.parse("application/json; charset=utf-8");
	
	private String token = "";
	

    public static void main(String[] args) throws IOException {
    	Main main = new Main();
    }
    
    public Main()
    {
    	get("/currentTemp", (req, res )-> "Current Temperature is "+currentTemp());
    	get("/access", (req,res) -> access() );
    	get("/callback", (req, res)-> {
    	callback( req, res);	
    	return "OK"; });
    }

    
    
    
    private String access() throws Exception
    {
    	
    	Request request = new Request.Builder()
    	.url("https://home.nest.com/login/oauth2?client_id=d0187803-8cdc-4517-8261-329f1d2d2a66&state=STATE&redirect_uri="+REDIRECT_URI)
    	.get().build();
    	  try (Response response = new OkHttpClient().newCall(request).execute()) {
    	      System.out.println(response.body().toString());
    	      return response.body().toString();
    	    }
    }
    
    private void callback( spark.Request req, spark.Response  res ) throws IOException
    {
    	  String pincode = (String) req.attribute("pincode");
    	  System.out.println("pincode="+pincode);
          String nestAccessToken = "https://api.home.nest.com/oauth2/access_token";
          String accessTokenResponse = post(nestAccessToken, pinCodeJson(pincode));
          
          ResponseBody body = ResponseBody.create(JSON, accessTokenResponse);
          System.out.println(body);
              
              

    }
          
          private String pinCodeJson( String pincode ) {
        	  return "{" + "'code':'"+pincode+"', "
        			  + "'client_id' : "+Constants.PRODUCT_ID+"', "
        			  + "'client_secret' : '"+Constants.PRODUCT_SECRET+"',"
        			  + "'grant_type' : 'authorization_code'"
        			  + "}";
          }
          
          String post(String url, String json) throws IOException {
        	    RequestBody body = RequestBody.create(JSON, json);
        	    Request request = new Request.Builder()
        	        .url(url)
        	        .post(body)
        	        .build();
        	    try (Response response = new OkHttpClient().newCall(request).execute()) {
        	      return response.body().string();
        	    }
        	  }
    
    
    public final int currentTemp() throws IOException
    {
    	int temp = -1;
        
        OkHttpClient client = new OkHttpClient.Builder()
        .authenticator(new Authenticator() {
          @Override public Request authenticate(Route route, Response response) throws IOException {
            return response.request().newBuilder()
                .header("Authorization", token)
                .build();
          }
        })
        .followRedirects(true)
        .followSslRedirects(true)
        .build();
 
        Request request = new Request.Builder()
        .url("https://developer-api.nest.com")
        .get()
        .addHeader("content-type", "application/json; charset=UTF-8")
        .addHeader("authorization", token)
        .build();
            System.out.println("Begin request:  ");
            Response response = client.newCall(request).execute();
            System.out.println(response.body().string());
            System.out.println("End request");
            System.out.println();
                              
        return temp;
    }
	
}
