package proz.docker.rest;


import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.enterprise.context.RequestScoped;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import bsh.EvalError;
import bsh.Interpreter;

@Path("/calc")
@RequestScoped
public class RESTCalculationHandler {
	private Interpreter beanShell = new Interpreter();
	private ObjectMapper mapper = new ObjectMapper();
	
	public class ResultResponse {
		public String result;
		public String exception;
	}

	private void initBeanShell() throws EvalError {
		final String factorialDef = ""
				+ "double factorial(double number) {"
				+ "if (number > 300) return Double.POSITIVE_INFINITY;"
				+ "if (number == 1 || number == 0)"
				+ "	return 1;"
				+ "else"
				+ "	return number * factorial(number - 1);"
				+ "}";
		beanShell.eval(factorialDef);
	}

	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response handleRequest(InputStream stream) {
		ResultResponse resultResponse = new ResultResponse();
		try {
			ObjectNode jsonTree = (ObjectNode) mapper.readTree(stream);
			//v
			//System.out.println(jsonTree.toString());
			initBeanShell();
			String equation = jsonTree.get("equation").asText();
			resultResponse.result = beanShell.eval(equation).toString();
		}
		catch (IOException e) {
			resultResponse.exception = "Bad Json";
	    }
		catch (EvalError e) {
			resultResponse.exception = "Bad equation";
		}
		catch (Exception e) {
			resultResponse.exception = "Calculation error on server. Likely a stack overflow."+e.getCause();
		}
		return Response
				.ok()
				.entity(resultResponse)
				.type(MediaType.APPLICATION_JSON)
				.build();
	}
}
