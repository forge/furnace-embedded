package org.wsdemo.rest;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import org.jboss.forge.furnace.container.simple.lifecycle.SimpleContainer;

@ApplicationScoped
@Path("/hello")
public class HelloWorldEndpoint
{

   @Inject
   Instance<Object> instances;

   @GET
   @Produces("text/plain")
   public Response doGet(@QueryParam("class") String className) throws Exception
   {
      System.out.println("ANTES INSTANCES");
      instances.select(Class.forName(className)).forEach(System.out::println);
      System.out.println("DEPOIS");
      System.out.println("ANTES SIMPLECONTAINER");
      SimpleContainer.getServices(getClass().getClassLoader(), Class.forName(className)).forEach(System.out::println);
      System.out.println("DEPOIS");
      return Response.ok("Hello from WildFly Swarm!").build();
   }
}