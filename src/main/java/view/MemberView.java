package view;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class MemberView
 */
@WebServlet("/member.view")
public class MemberView extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private static final String LOGIN_PATH = "index.html";

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public MemberView() {
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
		System.out.println("doget memberview");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		processRequest(request, response);
		System.out.println("dopost memberview");
	}

	private void processRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
		// TODO Auto-generated method stub
		if (request.getSession().getAttribute("login") == null) {
			response.sendRedirect(LOGIN_PATH);
			return;
		}

		String username = getUsername(request);
		response.setContentType("text/html; charset=UTF-8");
		PrintWriter out = response.getWriter();
		out.println("<!DOCTYPE html>");
		out.println("<html>");
		out.println("<head>");
		out.println("<meta charset='UTF-8'>");
		out.println("<title>Gossip 微網誌</title>");
		out.println("<link rel='stylesheet' href='css/member.css' type='text/css'>");
		out.println("</head>");
		out.println("<body>");

		out.println("<div class='leftPanel'>");
		out.println("<img src='images/caterpillar.jpg' alt='Gossip 微網誌' /><br><br>");
		out.printf("<a href='logout'>登出 %s</a>", username);
		out.println("</div>");
		out.println("<form method='post' action='new_message'>");
		out.println("分享新鮮事...<br>");

		String preBlabla = request.getParameter("blabla");
		if (preBlabla == null) {
			preBlabla = "";
		} else {
			out.println("訊息要 140 字以內<br>");
		}
		out.printf("<textarea cols='60' rows='4' name='blabla'>%s</textarea><br>", preBlabla);
		out.println("<button type='submit'>送出</button>");
		out.println("</form>");

		Map<Long, String> messages = (Map<Long, String>) request.getAttribute("messages");
		if (messages.isEmpty()) {
			out.println("<p>寫點什麼吧！</p>");
		} else {
			out.println("<table border='0' cellpadding='2' cellspacing='2'>");
			out.println("<thead>");
			out.println("<tr><th><hr></th></tr>");
			out.println("</thead>");
			out.println("<tbody>");

			for (Map.Entry<Long, String> message : messages.entrySet()) {
				Long millis = message.getKey();
				String blabla = message.getValue();

				LocalDateTime dateTime = Instant.ofEpochMilli(millis).atZone(ZoneId.of("Asia/Taipei"))
						.toLocalDateTime();

				out.println("<tr><td style='vertical-align: top;'>");
				out.printf("%s<br>", username);
				out.printf("%s<br>", blabla);
				out.println(dateTime);
				out.println("<form method='post' action='del_message'>");
				out.printf("<input type='hidden' name='millis' value='%s'>", millis);
				out.println("<button type='submit'>刪除</button>");
				out.println("</form>");
				out.println("<hr></td></tr>");
			}

			out.println("</tbody>");
			out.println("</table>");
		}
		out.println("</body>");
		out.println("</html>");
	}

	private String getUsername(HttpServletRequest request) {
		// TODO Auto-generated method stub
		return (String) request.getSession().getAttribute("login");
	}
}
