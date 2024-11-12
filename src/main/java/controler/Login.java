package controler;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class Login
 */
@WebServlet("/login")
public class Login extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private String USERS;
	@Override
	public void init() throws ServletException {
		// Get the real path to the "users" directory within the project
		USERS = getServletContext().getRealPath("/users");
	}
	private final String SUCCESS_PATH = "member.html";
	private final String ERROR_PATH = "index.html";
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Login() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
//		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
//		doGet(request, response);
		var username = request.getParameter("username");
		var password = request.getParameter("password");
		
		response.sendRedirect(login(username, password)? SUCCESS_PATH: ERROR_PATH);
	}

	private boolean login(String username, String password) throws IOException{
		if(username != null && username.trim().length() != 0 && password != null) {
			var userhome = Paths.get(USERS, username);
			return Files.exists(userhome) && isCorrectPassword(password, userhome);
		}
		return false;
		
	}
	
	private boolean isCorrectPassword(String password, Path userhome) throws IOException {
		Path profile = userhome.resolve("profile");
		try(var reader = Files.newBufferedReader(profile)){
			var data = reader.readLine().split("\t");
			var encrypt = Integer.parseInt(data[1]);
			var salt = Integer.parseInt(data[2]);
			return password.hashCode() + salt == encrypt;
		}
	}
}
