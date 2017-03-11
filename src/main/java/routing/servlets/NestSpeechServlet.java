package routing.servlets;

import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.amazon.speech.speechlet.servlet.SpeechletServlet;
import com.google.inject.Singleton;

@Singleton
public class NestSpeechServlet extends SpeechletServlet {

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		Enumeration<String> iter = request.getHeaderNames();
		System.out.println("cookies");
		for ( Cookie c : request.getCookies() ) {
			System.out.println(c.getName()+" "+c.getValue());
		}
		System.out.println("Header:");
		while ( iter.hasMoreElements() ) {
			String s = iter.nextElement();
			System.out.println(s+" "+request.getHeader(s));
		}
		super.doPost(request, response);
	}
	
}
