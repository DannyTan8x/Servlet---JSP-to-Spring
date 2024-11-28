package web;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet Filter implementation class AccessFilter
 */
@WebFilter(
		urlPatterns = { "/member", "/member.view", "/new_message", "/del_message", "/logout" }, 
		initParams = {
		@WebInitParam(name = "LOGIN_PATH", value = "indext.html")

})
public class AccessFilter extends HttpFilter {

	private static final long serialVersionUID = 1L;
	private String LOGIN_PATH;
	
	
	
	@Override
	public void init() throws ServletException {
		LOGIN_PATH = getInitParameter("LOGIN_PATH");
	}

	/**
	 * @see HttpFilter#HttpFilter()
	 */
	public AccessFilter() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
		// TODO Auto-generated method stub
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		// TODO Auto-generated method stub
		// place your code here
		
		// pass the request along the filter chain
		if(request.getSession().getAttribute("login") == null) {
			response.sendRedirect(LOGIN_PATH);
		} else {
			chain.doFilter(request, response);
			
		}
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		// TODO Auto-generated method stub
	}

}
