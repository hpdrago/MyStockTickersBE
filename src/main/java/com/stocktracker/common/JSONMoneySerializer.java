package com.stocktracker.common;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.math.BigDecimal;

/**
 * This class formats currency to two decimal places.
 */
public class JSONMoneySerializer extends JsonSerializer<BigDecimal>
{
    @Override
    public void serialize( BigDecimal value, JsonGenerator jgen, SerializerProvider provider)
        throws IOException
    {
        // put your desired money style here
        jgen.writeNumber( value.setScale(2, BigDecimal.ROUND_HALF_UP ) );
    }
}
