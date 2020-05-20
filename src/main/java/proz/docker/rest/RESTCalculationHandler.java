package proz.docker.rest;

import javax.enterprise.context.RequestScoped;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import jdk.jshell.JShell;

@Path("/calc")
@RequestScoped
public class RESTCalculationHandler {
	private final JShell jshell = JShell.create();

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String handleRequest() {
		return "<html><body><h1>Calc</h1></body></html>";
	}
}
