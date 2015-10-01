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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyWriter;

import org.qi4j.api.structure.Application;
import org.qi4j.api.value.ValueSerializer;

/**
 * Value MessageBodyWriter.
 */
public class ValueMessageBodyWriter
    implements MessageBodyWriter<Object>
{
    private final Application application;
    private final String layerName;
    private final String moduleName;

    public ValueMessageBodyWriter( Application application, String layerName, String moduleName )
    {
        this.application = application;
        this.layerName = layerName;
        this.moduleName = moduleName;
    }

    @Override
    public boolean isWriteable( Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType )
    {
        return ValueMessageBodyUtils.findValueType( application.findModule( layerName, moduleName ), type ) != null;
    }

    @Override
    public long getSize( Object value, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType )
    {
        ValueSerializer serializer = application.findModule( layerName, moduleName )
            .findService( ValueSerializer.class ).get();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        serializer.serialize( value, baos );
        long size = baos.toByteArray().length;
        return size;
    }

    @Override
    public void writeTo( Object value, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType,
                         MultivaluedMap<String, Object> httpHeaders, OutputStream entityStream )
        throws IOException, WebApplicationException
    {
        ValueSerializer serializer = application.findModule( layerName, moduleName )
            .findService( ValueSerializer.class ).get();
        serializer.serialize( value, entityStream );
    }
}
