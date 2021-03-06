package org.qi4j.library.jersey;

import javax.ws.rs.core.Feature;
import javax.ws.rs.core.FeatureContext;

import org.qi4j.api.structure.Application;
import org.qi4j.library.jersey.internal.ContainerLifecycle;
import org.qi4j.library.jersey.internal.ResourceInjector;
import org.qi4j.library.jersey.internal.ValueMessageBodyWriter;
import org.qi4j.library.jersey.internal.ValueMessageBodyReader;

public class Qi4jFeature
    implements Feature
{
    private final Application application;
    private final String layerName;
    private final String moduleName;

    public Qi4jFeature( Application application, String layerName, String moduleName )
    {
        this.application = application;
        this.layerName = layerName;
        this.moduleName = moduleName;
    }

    @Override
    public boolean configure( FeatureContext context )
    {
        context.register( new ContainerLifecycle( application ) );
        context.register( new ResourceInjector( application, layerName, moduleName ) );
        context.register( new ValueMessageBodyReader( application, layerName, moduleName ) );
        context.register( new ValueMessageBodyWriter( application, layerName, moduleName ) );
        return true;
    }
}
