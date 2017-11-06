package com.stocktracker.common;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * This class formats Timestamp into JSON dates
 */
public class JSONTimestampSerializer extends JsonSerializer<Timestamp>
{
    @Override
    public void serialize( Timestamp timestamp, JsonGenerator jgen, SerializerProvider provider)
        throws IOException
    {
        // put your desired money style here
        jgen.writeString( JSONDateConverter.toString( timestamp ));
    }
}
