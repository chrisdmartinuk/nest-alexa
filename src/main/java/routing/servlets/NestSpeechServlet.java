package routing.servlets;

import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.amazon.speech.speechlet.servlet.SpeechletServlet;

public class NestSpeechServlet extends SpeechletServlet {

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		Enumeration<String> iter = request.getAttributeNames();
		System.out.println("Attribute:");
		while ( iter.hasMoreElements() ) {
			String s = iter.nextElement();
			System.out.println(s+" "+request.getAttribute(s));
		}
		super.doPost(request, response);
	}
	
}
