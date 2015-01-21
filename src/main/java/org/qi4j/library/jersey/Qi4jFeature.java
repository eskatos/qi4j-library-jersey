package org.qi4j.library.jersey;

import javax.ws.rs.core.Feature;
import javax.ws.rs.core.FeatureContext;
import org.glassfish.hk2.api.Factory;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.server.spi.Container;
import org.glassfish.jersey.server.spi.ContainerLifecycleListener;
import org.qi4j.api.activation.ActivationException;
import org.qi4j.api.activation.PassivationException;
import org.qi4j.api.structure.Application;
import org.qi4j.api.structure.Layer;
import org.qi4j.api.structure.Module;

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
        context.register(
            new ContainerLifecycleListener()
            {

                @Override
                public void onStartup( Container container )
                {
                    try
                    {
                        application.activate();
                    }
                    catch( ActivationException ex )
                    {
                        throw new RuntimeException( ex.getMessage(), ex );
                    }
                }

                @Override
                public void onReload( Container container )
                {
                    try
                    {
                        application.passivate();
                        application.activate();
                    }
                    catch( PassivationException | ActivationException ex )
                    {
                        throw new RuntimeException( ex.getMessage(), ex );
                    }
                }

                @Override
                public void onShutdown( Container container )
                {
                    try
                    {
                        application.passivate();
                    }
                    catch( PassivationException ex )
                    {
                        throw new RuntimeException( ex.getMessage(), ex );
                    }
                }
            }
        );
        context.register(
            new AbstractBinder()
            {
                @Override
                protected void configure()
                {
                    bind( application ).to( Application.class );
                    bindFactory(
                        new Factory<Module>()
                        {
                            @Override
                            public Module provide()
                            {
                                return application.findModule( layerName, moduleName );
                            }

                            @Override
                            public void dispose( Module instance )
                            {
                            }
                        }
                    ).to( Module.class );
                    bindFactory(
                        new Factory<Layer>()
                        {
                            @Override
                            public Layer provide()
                            {
                                return application.findLayer( layerName );
                            }

                            @Override
                            public void dispose( Layer instance )
                            {
                            }
                        }
                    ).to( Layer.class );
                }
            }
        );
        return true;
    }
}
