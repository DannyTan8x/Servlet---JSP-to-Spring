package web;

import java.io.IOException;
import java.util.Optional;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
//外掛的 html標籤策略 （OWASP）
import org.owasp.html.HtmlPolicyBuilder;
import org.owasp.html.PolicyFactory;

/**
 * Servlet Filter implementation class HtmlSanitizer
 */
//過濾使用者輸入 new_message 的標籤 
@WebFilter("/new_message")
public class HtmlSanitizer extends HttpFilter implements Filter {
	private static final long serialVersionUID = 1L;
	private PolicyFactory policy;

	@Override
	public void init() throws ServletException {
		// TODO Auto-generated method stub
		//只允許使用者輸入的 html標籤 如 <a>; <b>; <i> <del>; <pre>; <code> ; http; https; href;
		policy = new HtmlPolicyBuilder().allowElements("a", "b", "i", "del", "pre", "code")
				.allowUrlProtocols("http", "https").allowAttributes("href").onElements("a").requireRelNofollowOnLinks().toFactory();
	}

	private class SanitizeWrapper extends HttpServletRequestWrapper{

		public SanitizeWrapper(HttpServletRequest request) {
			super(request);
			// TODO Auto-generated constructor stub
		}

		@Override
		public String getParameter(String name) {
			// TODO Auto-generated method stub
			return Optional.ofNullable(getRequest().getParameter(name)).map(policy::sanitize).orElse(null);
		}
		
	}
	

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		// TODO Auto-generated method stub
		// place your code here

		// pass the request along the filter chain
		chain.doFilter(new SanitizeWrapper(request), response);
	}



}
