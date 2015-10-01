package com.example;

import org.qi4j.bootstrap.ApplicationAssembler;
import org.qi4j.bootstrap.ApplicationAssembly;
import org.qi4j.bootstrap.ApplicationAssemblyFactory;
import org.qi4j.bootstrap.AssemblyException;
import org.qi4j.valueserialization.orgjson.OrgJsonValueSerializationService;

public class ExampleApplicationAssembler
    implements ApplicationAssembler
{
    @Override
    public ApplicationAssembly assemble( ApplicationAssemblyFactory applicationFactory )
        throws AssemblyException
    {
        ApplicationAssembly assembly = applicationFactory.newApplicationAssembly();
        assembly.setName( "Sample Qi4j Application" );
        assembly.layer( "app" ).module( "usecases" ).services( Speaker.class );
        assembly.layer( "app" ).module( "usecases" ).values( SomeValue.class );
        assembly.layer( "app" ).module( "usecases" ).services( OrgJsonValueSerializationService.class );
        return assembly;
    }
}
