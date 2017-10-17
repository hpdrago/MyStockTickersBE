package com.stocktracker.weblayer.controllers;

import com.stocktracker.common.MyLogger;

/**
 * Created by mike on 12/3/2016.
 */
public abstract class AbstractHandler<I,O> implements MyLogger
{
    public abstract O handleRequest( I i );
}
