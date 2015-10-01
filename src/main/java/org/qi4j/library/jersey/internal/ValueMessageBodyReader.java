/*
 * Copyright (c) 2014 the original author or authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.qi4j.library.jersey.internal;

import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;

import org.qi4j.api.structure.Application;
import org.qi4j.api.value.ValueDeserializer;

/**
 * Value MessageBodyReader.
 */
public class ValueMessageBodyReader
    implements MessageBodyReader<Object>
{
    private final Application application;
    private final String layerName;
    private final String moduleName;

    public ValueMessageBodyReader( Application application, String layerName, String moduleName )
    {
        this.application = application;
        this.layerName = layerName;
        this.moduleName = moduleName;
    }

    @Override
    public boolean isReadable( Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType )
    {
        return ValueMessageBodyUtils.findValueType( application.findModule( layerName, moduleName ), type ) != null;
    }

    @Override
    public Object readFrom( Class<Object> type, Type genericType, Annotation[] annotations, MediaType mediaType,
                            MultivaluedMap<String, String> httpHeaders, InputStream entityStream )
        throws IOException, WebApplicationException
    {
        ValueDeserializer deserializer = application.findModule( layerName, moduleName )
            .findService( ValueDeserializer.class ).get();
        return deserializer.deserialize( type, entityStream );
    }
}
