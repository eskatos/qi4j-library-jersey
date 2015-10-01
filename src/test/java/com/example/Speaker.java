package com.example;

import org.qi4j.api.activation.ActivatorAdapter;
import org.qi4j.api.activation.Activators;
import org.qi4j.api.mixin.Mixins;
import org.qi4j.api.service.ServiceActivation;
import org.qi4j.api.service.ServiceReference;

@Mixins( Speaker.Mixin.class )
//@Activators( Speaker.Activator.class )
public interface Speaker
    extends ServiceActivation
{
    String sayHello();

    public class Mixin
        implements Speaker
    {
        @Override
        public String sayHello()
        {
            return "Hello!";
        }

        @Override
        public void activateService()
            throws Exception
        {
            System.out.println( "ACTIVATE SERVICE" );
        }

        @Override
        public void passivateService()
            throws Exception
        {
            System.out.println( "PASSIVATE SERVICE" );
        }
    }
}
