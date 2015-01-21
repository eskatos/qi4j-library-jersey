package com.example;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.qi4j.api.structure.Application;
import org.qi4j.api.structure.Layer;
import org.qi4j.api.structure.Module;

@Path( "myresource" )
public class MyResource
{
    @Inject Application application;
    @Inject Layer layer;
    @Inject Module module;

    @GET
    @Produces( MediaType.TEXT_PLAIN )
    public String getIt()
    {
        System.out.println( "-----------------------------------------------------------------" );
        System.out.println( "@Injected Qi4j Application is named: '" + application.name() + "'" );
        System.out.println( "Entry Point:" );
        System.out.println( "    Layer: " + layer.name() );
        System.out.println( "    Module: " + module.name() );
        Speaker speaker = module.findService( Speaker.class ).get();
        System.out.println( "Service: " + speaker );
        System.out.println( "-----------------------------------------------------------------" );
        return speaker.sayHello();
    }
}
