package config.filters;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Response.Status;

@WebFilter(filterName = "AuthFilter", urlPatterns = {"/api/*"})
public class AuthFilter implements Filter {

	private static final String URI_LOGIN = "/andorinha-backend/api/auth/login";
	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;
				
		System.out.println(req.getRequestURI());
		
		if (!URI_LOGIN.equalsIgnoreCase(req.getRequestURI())) {
			res.setStatus(Status.UNAUTHORIZED.getStatusCode());
			return;
		}
		
		chain.doFilter(request, response);
	}

}