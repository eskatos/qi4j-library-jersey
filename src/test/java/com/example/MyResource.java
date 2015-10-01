package com.example;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.qi4j.api.injection.scope.Service;
import org.qi4j.api.injection.scope.Structure;
import org.qi4j.api.structure.Application;
import org.qi4j.api.structure.Layer;
import org.qi4j.api.structure.Module;
import org.qi4j.api.value.ValueBuilder;

@Path( "myresource" )
public class MyResource
{
    @Inject Application application;
    @Inject Layer layer;
    @Inject Module module;

    @Structure Application applicationStruct;
    @Structure Layer layerStruct;
    @Structure Module moduleStruct;

    @Service Speaker speaker;

    @GET
    @Produces( MediaType.TEXT_PLAIN )
    public String getIt()
    {
        System.out.println( "-----------------------------------------------------------------" );
        System.out.println( "@Inject Application '" + application.name() + "'" );
        System.out.println( "Entry Point:" );
        System.out.println( "    Layer: " + layer.name() );
        System.out.println( "    Module: " + module.name() );
        Speaker service = module.findService( Speaker.class ).get();
        System.out.println( "Service: " + service );
        System.out.println( "-----------------------------------------------------------------" );

        System.out.println( "-----------------------------------------------------------------" );
        System.out.println( "@Structure Application '" + applicationStruct.name() + "'" );
        System.out.println( "Entry Point:" );
        System.out.println( "    Layer: " + layerStruct.name() );
        System.out.println( "    Module: " + moduleStruct.name() );
        service = moduleStruct.findService( Speaker.class ).get();
        System.out.println( "Service: " + service );
        System.out.println( "-----------------------------------------------------------------" );

        System.out.println( "-----------------------------------------------------------------" );
        System.out.println( "@Service " + speaker );
        System.out.println( "-----------------------------------------------------------------" );

        return speaker.sayHello();
    }

    @Path( "value" )
    @GET
    @Produces( MediaType.APPLICATION_JSON )
    public SomeValue test()
    {
        ValueBuilder<SomeValue> builder = module.newValueBuilder( SomeValue.class );
        builder.prototype().identity().set( "foo" );
        return builder.newInstance();
    }

    @Path( "send" )
    @POST
    @Consumes( MediaType.APPLICATION_JSON )
    public void send( SomeValue value )
    {
        System.out.println( "Server GOT value: " + value );
    }

    @Path( "hateoas" )
    @GET
    @Produces( MediaType.APPLICATION_JSON )
    public Response hateoas()
    {
        ValueBuilder<SomeValue> builder = module.newValueBuilder( SomeValue.class );
        builder.prototype().identity().set( "foo" );
        SomeValue value = builder.newInstance();
        return Response.ok( value )
            .link( "/hateoas", "self" )
            .link( "/send", "send" )
            .build();
    }
}
