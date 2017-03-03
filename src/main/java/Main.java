import java.io.IOException;

import okhttp3.Authenticator;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;

public class Main {

    public static void main(String[] args) throws IOException {
    	
        String auth = System.getenv().get("NEST-AUTH");
        
        OkHttpClient client = new OkHttpClient.Builder()
        .authenticator(new Authenticator() {
          @Override public Request authenticate(Route route, Response response) throws IOException {
            return response.request().newBuilder()
                .header("Authorization", auth)
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
        .addHeader("authorization", auth)
        .build();
        
        try { 
            System.out.println("Begin request:  ");
            Response response = client.newCall(request).execute();
            System.out.println(response.body().string());
            System.out.println("End request");
            System.out.println();
            Thread.sleep(2 * 1000);                    
        } catch (InterruptedException e) {
                e.printStackTrace();
        }
    }
	
}
