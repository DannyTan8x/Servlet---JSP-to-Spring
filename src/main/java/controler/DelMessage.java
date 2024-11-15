package controler;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class DelMessage
 */
@WebServlet("/del_message")
public class DelMessage extends HttpServlet {
	private static final long serialVersionUID = 1L;
       private String USERS;
       private final String LOGIN_PATH = "index.html";
       private final String MEMBER_PATH = "member";
       
       
    @Override
	public void init() throws ServletException {
		// TODO Auto-generated method stub
		USERS = getServletContext().getRealPath("/users");
	}

	
    /**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	if(request.getSession().getAttribute("login") == null) {
		response.sendRedirect(LOGIN_PATH);
		return;
	}
	
	var millis = request.getParameter("millis");
	if(millis !=null) {
		deleteMessage(getUsername(request), millis);
	}
	response.sendRedirect(MEMBER_PATH);
	}


	private void deleteMessage(String username, String millis) throws IOException {
		// TODO Auto-generated method stub
		var txt = Paths.get(USERS, username, String.format("%s.txt",millis));
		Files.delete(txt);
	}


	private String getUsername(HttpServletRequest request) {
		// TODO Auto-generated method stub
		return (String) request.getSession().getAttribute("login");
	}

}
