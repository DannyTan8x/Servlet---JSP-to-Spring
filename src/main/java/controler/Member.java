package controler;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class Member
 */
@WebServlet("/member")
public class Member extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private String USERS;

	@Override
	public void init() throws ServletException {
		// Get the real path to the "users" directory within the project
		USERS = getServletContext().getRealPath("/users");
	}

	private static final String MEMBER_PATH = "member.view";
	private static final String LOGIN_PATH = "index.html";

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Member() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
//		response.getWriter().append("Served at: ").append(request.getContextPath());
		processRequest(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		processRequest(request, response);
	}

	private void processRequest(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		// TODO Auto-generated method stub
		if (request.getSession().getAttribute("login") == null) {
			response.sendRedirect(LOGIN_PATH);
			return;
		}
		request.setAttribute("messages", messages(getUsername(request)));

		request.getRequestDispatcher(MEMBER_PATH).forward(request, response);
	}

	private String getUsername(HttpServletRequest request) {
		// TODO Auto-generated method stub
		return (String) request.getSession().getAttribute("login");
	}

	private Map<Long, String> messages(String username) throws IOException {
		var userhome = Paths.get(USERS, username);
		var messages = new TreeMap<Long, String>(Comparator.reverseOrder());
		try (var txts = Files.newDirectoryStream(userhome, "*.txt")) {
			for (var txt : txts) {
				var millis = txt.getFileName().toString().replace(".txt", "");
				var blabla = Files.readAllLines(txt).stream().collect(Collectors.joining(System.lineSeparator()));
				messages.put(Long.parseLong(millis), blabla);
			}
		}

//		return messages;
		return messages;
	}

}
