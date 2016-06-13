package ibm.test;

import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.mockito.Matchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

/**
 * A class made the mock the components used to run an HttpServlet. Used for testing the functionality of an HttpServlet.<P>
 * 
 * To mock a servlet you construct an instance of this object.
 * Add the necessary URL pattern and parameters to the HttpServletRequest through the MockThatServlets methods (setUrlPattern() and putParameter()).
 * Get the now mocked HttpServletRequest and HttpServletResponse with getRequest() and getResponse().
 * Pass these to you HttpServlet to be tested.
 * Collect and assert the data gained by the MockThatServlet and its components using JUnit.
 */
public class MockThatServlet {
	// Servlet Components
	private HttpServletRequest request;
	private HttpServletResponse response;
	private HttpSession session;
	private RequestDispatcher reqdisp;
	private Integer error;
	private String forwardedTo;
	private String redirectedToUrl;
	private String urlPattern = "";
	private String contextPath = "/HTTPBank";
	private Map<String, Object> attributes;
	private Map<String, String> parameters;

	/**
	 * Setup the mocked servlet components.
	 * 
	 * @throws IOException
	 */
	public MockThatServlet() throws IOException {
		//Instantiate objects used for mocking.
		attributes = new HashMap<String, Object>();
		parameters = new HashMap<String, String>();
		
		//Create mocks.
		request = mock(HttpServletRequest.class);
		response = mock(HttpServletResponse.class);
		session = mock(HttpSession.class);
		reqdisp = mock(RequestDispatcher.class);
		
		//Start to mock any used request method.
		//Mocks: getContextPath()
		when(request.getContextPath()).then(new Answer<String>(){
			@Override
			public String answer(InvocationOnMock invocation) throws Throwable {
				return contextPath;
			}
		});
		//Mocks: getRequestURI()
		when(request.getRequestURI()).then(new Answer<String>(){
			@Override
			public String answer(InvocationOnMock invocation) throws Throwable {
				return contextPath+urlPattern;
			}
		});
		//Mocks: getSession()
		when(request.getSession()).thenReturn(session);
		//Mocks: getSession(boolean)
		when(request.getSession(Matchers.anyBoolean())).thenReturn(session);
		//Mocks: getRequestDispatcher(String)
		when(request.getRequestDispatcher(anyString())).then(new Answer<RequestDispatcher>() {
			@Override
			public RequestDispatcher answer(InvocationOnMock invocation)
					throws Throwable {
				forwardedTo = (String) invocation.getArguments()[0];
				return reqdisp;
			}
		});
		//Mocks: getParameter(String)
		when(request.getParameter(anyString())).thenAnswer(new Answer<String>() {
			@Override
			public String answer(InvocationOnMock invocation)
					throws Throwable {
				String key = (String) invocation.getArguments()[0];
				return parameters.get(key);
			}
		});
		//Mocks: getParameterNames()
		when(request.getParameterNames()).then(new Answer<Enumeration<String>>(){
			@Override
			public Enumeration<String> answer(InvocationOnMock invocation) throws Throwable {
				if (attributes.isEmpty()) return null;
				return java.util.Collections.enumeration(parameters.keySet());
			}
		});
		
		//Start to mock any used response method.
		//The code below is the method to mock a method that does not return a result (void).
		//Mocks: sendRedirect(String)
		Mockito.doAnswer(new Answer<Object>() {
			@Override
			public Object answer(InvocationOnMock invocation) throws Throwable {
				redirectedToUrl = (String) invocation.getArguments()[0];
				return null;
			}
		}).when(response).sendRedirect(anyString());
		//Mocks: sendError(int)
		Mockito.doAnswer(new Answer<Object>() {
			@Override
			public Object answer(InvocationOnMock invocation) throws Throwable {
				error = (int) invocation.getArguments()[0];
				return null;
			}
		}).when(response).sendError(Matchers.anyInt());
		
		//Start to mock any used session methods.
		//Mocks: getAttribute(String)
		when(session.getAttribute(anyString())).thenAnswer(new Answer<Object>() {
					@Override
					public Object answer(InvocationOnMock invocation)
							throws Throwable {
						return attributes.get((String) invocation.getArguments()[0]);
					}
				});
		//Mocks: getAttributeNames()
		when(session.getAttributeNames()).then(new Answer<Enumeration<String>>(){
			@Override
			public Enumeration<String> answer(InvocationOnMock invocation) throws Throwable {
				if (attributes.isEmpty()) return null;
				return java.util.Collections.enumeration(attributes.keySet());
			}
		});
		//Mocks: setAttribute(String, Object)
		Mockito.doAnswer(new Answer<Object>() {
			@Override
			public Object answer(InvocationOnMock invocation) throws Throwable {
				attributes.put((String) invocation.getArguments()[0], invocation.getArguments()[1]);
				return null;
			}
		}).when(session).setAttribute(anyString(), anyObject());
	}
	
	/**
	 * Sets the URL pattern for the mocked HttpServletRequest, this is the last part of the URL the HttpRequest has followed.<P>
	 * This can be done at anytime before calling the Servlet with the Request and Response.
	 * Example: "user/accounts"
	 * @param urlPattern The URL pattern to set.
	 */
	public void setUrlPattern(String urlPattern){
		this.urlPattern = urlPattern;
	}
	
	/**
	 * Adds a parameter to the mocked HttpServletRequest.
	 * @param key The key to use for the given parameter.
	 * @param value The value for the given parameter.
	 * @return The previous value associated with the given key or null.
	 */
	public String putParameter(String key, String value){
		return parameters.put(key, value);
	}
	
	/**
	 * Returns a reference to the mocked HttpServletRequest.<P>
	 * Any changes made to the request through this MockThatServlet will also apply to the reference returned by this method.
	 * @return Returns a reference to the mocked HttpServletRequest.
	 */
	public HttpServletRequest getRequest(){
		return request;
	}
	
	/**
	 * Returns a reference to the mocked HttpServletResponse.<P>
	 * @return Returns a reference to the mocked HttpServletResponse.
	 */
	public HttpServletResponse getResponse(){
		return response;
	}
	
	/**
	 * Returns the string passed to the mocked HttpServletResponse through its sendRedirect(String) method.
	 * @return The string passed to HttpServletResponse.sendRedirect(String) or null if this method has not been called.
	 */
	public String getRedirectedTo(){
		return redirectedToUrl;
	}
	
	/**
	 * Returns the string passed to the mocked HttpServletRequest through its getRequestDispatcher(String) method.
	 * @return The string passed to HttpServletRequest.getRequestDispatcher(String) or null if this method has not been called.
	 */
	public String getForwardedTo(){
		return forwardedTo;
	}
	
	/**
	 * Returns the integer passed to the mocked HttpServletResponse through its sendError(int) method.
	 * @return The integer passed to HttpServletResponse.sendError(int) or null if this method has not been called.
	 */
	public Integer getError(){
		return error;
	}
	
	/**
	 * Constructs a string containing information known by the mocked servlet components.
	 * @return A string of collected data from the mocked servlet components.
	 */
	public String printInfo() throws IOException {
		//Print
		StringBuilder builder = new StringBuilder();
		if (forwardedTo != null) builder.append("getRedirectedTo() : "+forwardedTo+System.lineSeparator());
		if (redirectedToUrl != null) builder.append("getRedirectedTo() : "+redirectedToUrl+System.lineSeparator());
		if (error != null) builder.append("getError() : "+error+System.lineSeparator());
		if (request.getContextPath() != null) builder.append("request.getContextPath() : "+request.getContextPath()+System.lineSeparator());
		if (request.getRequestURI() != null) builder.append("request.getRequestURI() :"+request.getRequestURI()+System.lineSeparator());
		
		Enumeration<String> headernames = request.getHeaderNames();
		if (headernames != null && headernames.hasMoreElements()){
			builder.append("request.getHeaderNames() :"+System.lineSeparator());
			while (headernames.hasMoreElements())
				builder.append(headernames.nextElement()+System.lineSeparator());
		}
		Enumeration<String> parameternames = request.getParameterNames();
		if (parameternames != null && parameternames.hasMoreElements()){
			builder.append("request.getParameterNames() :"+System.lineSeparator());
			while (parameternames.hasMoreElements())
				builder.append(parameternames.nextElement()+System.lineSeparator());
		}
		Enumeration<String> attributenames = request.getSession().getAttributeNames();
		if (attributenames != null && attributenames.hasMoreElements()){
			builder.append("request.getSession().getAttributeNames() :"+System.lineSeparator());
			while (attributenames.hasMoreElements())
				builder.append(attributenames.nextElement()+System.lineSeparator());
		}
		if (request.getInputStream() != null){
			builder.append("request.getInputStream() :"+System.lineSeparator());
			BufferedReader reader = new BufferedReader(new InputStreamReader(request.getInputStream()));
			while (reader.ready())
				builder.append(reader.readLine()+System.lineSeparator());
		}
		
		return builder.toString();
	}
}