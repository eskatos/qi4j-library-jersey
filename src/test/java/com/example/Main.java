package com.example;

import java.io.IOException;
import java.net.URI;

import javax.ws.rs.core.Feature;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import org.qi4j.api.structure.Application;
import org.qi4j.bootstrap.AssemblyException;
import org.qi4j.bootstrap.Energy4Java;
import org.qi4j.library.jersey.Qi4jFeature;

public class Main
{
    public static final String BASE_URI = "http://localhost:8080/myapp/";

    public static HttpServer startServer()
        throws AssemblyException
    {
        // Qi4j Application
        Application application = new Energy4Java().newApplication( new ExampleApplicationAssembler() );

        // Jersey Application
        Feature qi4jFeature = new Qi4jFeature( application, "app", "usecases" );
        final ResourceConfig rc = new ResourceConfig().packages( "com.example" ).register( qi4jFeature );

        // Grizzly Server
        return GrizzlyHttpServerFactory.createHttpServer( URI.create( BASE_URI ), rc );
    }

    public static void main( String[] args )
        throws IOException, AssemblyException
    {
        final HttpServer server = startServer();
        System.out.println( String.format( "Jersey app started with WADL available at "
                                           + "%sapplication.wadl\nHit enter to stop it...", BASE_URI ) );
        System.in.read();
        server.shutdownNow();
    }
}
