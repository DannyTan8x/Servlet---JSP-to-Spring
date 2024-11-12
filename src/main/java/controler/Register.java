
package controler;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/register")
public class Register extends HttpServlet {
	
	//路徑在 /Applications/Eclipse.app/Contents/MacOS/users/admin
//	private final String USERS = "users";
//	private final String USERS = Paths.get(System.getProperty("user.dir"), "users").toString();\\
	
	//路徑在 /Volumes/1TB/javaProjectWorkSpace/.metadata/.plugins/org.eclipse.wst.server.core/tmp0/wtpwebapps/CH3EX1/users/admin
	private String USERS;
	@Override
	public void init() throws ServletException {
		// Get the real path to the "users" directory within the project
		USERS = getServletContext().getRealPath("/users");
	}
	private final String SUCCESS_PATH = "register_success.view";
	private final String ERROR_PATH = "register_error.view";

	private final Pattern emailRegex = Pattern.compile("^[_a-z0-9-]+([.][_a-z0-9-]+)*@[a-z0-9-]+([.][a-z0-9-]+)*$");

	private final Pattern passwdRegex = Pattern.compile("^\\w{8,16}$");

	private final Pattern usernameRegex = Pattern.compile("^\\w{1,16}$");

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		var email = request.getParameter("email");
		var username = request.getParameter("username");
		var password = request.getParameter("password");
		var password2 = request.getParameter("password2");

		var errors = new ArrayList<String>();
		if (!validateEmail(email)) {
			errors.add("未填寫郵件或格式不正確");
		}
		if (!validateUsername(username)) {
			errors.add("未填寫使用者名稱或格式不正確");
		}
		if (!validatePassword(password, password2)) {
			errors.add("請確認密碼符合格式並再度確認密碼");
		}

		String path;
		if (errors.isEmpty()) {
			path = SUCCESS_PATH;
			tryCreateUser(email, username, password);
		} else {
			path = ERROR_PATH;
			request.setAttribute("errors", errors);
		}

		request.getRequestDispatcher(path).forward(request, response);
	}

	private boolean validateEmail(String email) {
		return email != null && emailRegex.matcher(email).find();
	}

	private boolean validateUsername(String username) {
		return username != null && usernameRegex.matcher(username).find();
	}

	private boolean validatePassword(String password, String password2) {
		return password != null && passwdRegex.matcher(password).find() && password.equals(password2);
	}

	private void tryCreateUser(String email, String username, String password) throws IOException {
		var userhome = Paths.get(USERS, username);
		System.out.println(USERS);
		System.out.println(userhome);
		if (Files.notExists(userhome)) {
			createUser(userhome, email, password);
		}
	}

	private void createUser(Path userhome, String email, String password) throws IOException {
		Files.createDirectories(userhome);

		var salt = ThreadLocalRandom.current().nextInt();
		var encrypt = String.valueOf(salt + password.hashCode());

		var profile = userhome.resolve("profile");
		try (var writer = Files.newBufferedWriter(profile)) {
			writer.write(String.format("%s\t%s\t%d", email, encrypt, salt));
		}
	}
}
