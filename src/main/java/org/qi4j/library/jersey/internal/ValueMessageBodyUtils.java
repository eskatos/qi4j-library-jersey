package org.qi4j.library.jersey.internal;

import org.qi4j.api.structure.Module;
import org.qi4j.api.value.ValueComposite;

final class ValueMessageBodyUtils
{
    static Class<?> findValueType( Module module, Class<?> type )
    {
        if( module.valueDescriptor( type.getName() ) != null )
        {
            return type;
        }
        for( Class<?> iface : type.getInterfaces() )
        {
            if( ValueComposite.class.equals( iface ) )
            {
                continue;
            }
            if( module.valueDescriptor( iface.getName() ) != null )
            {
                return iface;
            }
        }
        return null;
    }

    private ValueMessageBodyUtils()
    {
    }
}
