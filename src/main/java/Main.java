import static spark.Spark.get;
import static spark.SparkBase.port;
import static spark.SparkBase.staticFileLocation;

import java.io.IOException;

import okhttp3.Authenticator;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.Route;
import org.json.JSONObject;

public class Main {
	
	
	private static final String REDIRECT_URI = "/callback";
	public static final MediaType JSON
    = MediaType.parse("application/json; charset=utf-8");
	
	private String token;
	
	

    public static void main(String[] args) throws IOException {
    	Main main = new Main();
    }
    
    public Main()
    {
    	String s = System.getenv("PORT");
    	int port = Integer.valueOf(s == null ? "8181" : s );
        port(port);
        staticFileLocation("/public");
        get("/", (req, res ) -> alexa(req, res) );
        get("/hello", (req, res) -> "Hello World");
    	get("/currentTemp", (req, res )-> currentTemp());
    	get("/access", (req,res)-> { res.redirect("https://home.nest.com/login/oauth2?client_id="+Constants.PRODUCT_ID+"&state=STATE"); return ""; } );
    	get("/callback", (req, res)-> {
    	callback( req, res);	
    	return "OK"; });
    }
    
    private String alexa(spark.Request req, spark.Response  res)
    {
    	
    	return "alexa";
    }
    
    private void callback( spark.Request req, spark.Response  res ) throws IOException
    {
    	  String pincode = (String) req.queryParams("code");
    	  System.out.println("pincode="+pincode);
          MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
          RequestBody body = RequestBody.create(mediaType, "code="+pincode+"&client_id="+Constants.PRODUCT_ID+"&client_secret="+Constants.PRODUCT_SECRET+"&grant_type=authorization_code");
          Request request = new Request.Builder()
            .url("https://api.home.nest.com/oauth2/access_token")
            .post(body)
            .build();

          Response response = new OkHttpClient().newCall(request).execute();
          String jsonData = response.body().string();
          System.out.println(jsonData);
          JSONObject authRespone = new JSONObject(jsonData);
          token = (String) authRespone.get("access_token");

    }
    
    public final String currentTemp() throws IOException
    {
    	if ( token == null ) {
    		return "Please authenticate using access page";
    	}
    	int temp = -1;
        System.out.println("token="+token);
        OkHttpClient client = new OkHttpClient.Builder()
        .authenticator(new Authenticator() {
          @Override public Request authenticate(Route route, Response response) throws IOException {
            return response.request().newBuilder()
                .header("Authorization", "Bearer "+token)
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
            String nestData = response.body().string();
            System.out.println(nestData);
            temp = new NestAPI(nestData).getCurrentTemp();
            System.out.println("End request");
            System.out.println();
                              
        return "Current Temperature is "+temp;
    }
	
}
