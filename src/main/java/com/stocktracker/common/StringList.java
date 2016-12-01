package com.stocktracker.common;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mike on 11/19/2016.
 */
public class StringList
{
    @JsonProperty
    private List<String> list = new ArrayList();

    public void add( final String s )
    {
        list.add( s );
    }
}
