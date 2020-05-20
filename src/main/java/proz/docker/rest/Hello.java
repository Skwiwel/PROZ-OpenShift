package proz.docker.rest;

import javax.enterprise.context.RequestScoped;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("/hello")
@RequestScoped
public class Hello {
	@GET
	public String sayHello(){
		return "Hello";
	}

}
