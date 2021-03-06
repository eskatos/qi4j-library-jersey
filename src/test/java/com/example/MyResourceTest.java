package com.example;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.grizzly.http.server.HttpServer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class MyResourceTest
{
    private HttpServer server;
    private WebTarget target;

    @Before
    public void setUp()
        throws Exception
    {
        // start the server
        server = Main.startServer();
        // create the client
        Client c = ClientBuilder.newClient();

        // uncomment the following line if you want to enable
        // support for JSON in the client (you also have to uncomment
        // dependency on jersey-media-json module in pom.xml and Main.startServer())
        // --
        // c.configuration().enable(new org.glassfish.jersey.media.json.JsonJaxbFeature());
        target = c.target( Main.BASE_URI );
    }

    @After
    public void tearDown()
        throws Exception
    {
        server.shutdownNow();
    }

    /**
     * Test to see that the message "Hello!" is sent in the response.
     */
    @Test
    public void testGetIt()
    {
        String responseMsg = target.path( "myresource" ).request().get( String.class );
        assertEquals( "Hello!", responseMsg );
    }

    @Test
    public void testBodyWriter()
    {
        String response = target.path( "myresource/value" ).request().get( String.class );
        System.out.println( response );
        assertThat( response, containsString( "foo" ) );
    }

    @Test
    public void testBodyReader()
    {
        Entity<String> body = Entity.entity( "{\"identity\":\"bar\"}", MediaType.APPLICATION_JSON );
        target.path( "myresource/send" ).request().post( body );
    }

    @Test
    public void testHateoas()
    {
        Response response = target.path( "myresource/hateoas" ).request().get();
        String body = response.readEntity( String.class );
        System.out.println( response.getHeaders() );
        System.out.println( body );
        assertThat( body, containsString( "foo" ) );
        assertThat( response.getHeaderString( "Link" ), containsString( "self" ) );

    }
}
