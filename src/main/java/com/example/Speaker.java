package com.example;

import org.qi4j.api.mixin.Mixins;

@Mixins( value = Speaker.Mixin.class )
public interface Speaker
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
    }
}
