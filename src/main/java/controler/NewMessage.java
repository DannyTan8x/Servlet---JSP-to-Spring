package controler;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Instant;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class NewMessage
 */
@WebServlet("/new_message")
public class NewMessage extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private String USERS;

	@Override
	public void init() throws ServletException {
		// Get the real path to the "users" directory within the project
		USERS = getServletContext().getRealPath("/users");
	}

	private static final String LOGIN_PATH = "index.html";
	private static final String MEMBER_PATH = "member";

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		if (request.getSession().getAttribute("login") == null) {
			response.sendRedirect(LOGIN_PATH);
			return;
		}

		request.setCharacterEncoding("UTF-8");
		var blabla = request.getParameter("blabla");

		if (blabla == null || blabla.length() == 0) {
			response.sendRedirect(MEMBER_PATH);
			return;
		}

		if (blabla.length() <= 140) {
			addMessage(getUsername(request), blabla);
			response.sendRedirect(MEMBER_PATH);
		} else {
			request.getRequestDispatcher(MEMBER_PATH).forward(request, response);
		}
	}

	private void addMessage(String username, String blabla) throws IOException {
		// TODO Auto-generated method stub
		var txt = Paths.get(USERS, username, String.format("%s.txt", Instant.now().toEpochMilli()));
		try(var writer = Files.newBufferedWriter(txt)){
			writer.write(blabla);
		}
	}

	private String getUsername(HttpServletRequest request) {
		// TODO Auto-generated method stub
		return (String) request.getSession().getAttribute("login");
	}

}
