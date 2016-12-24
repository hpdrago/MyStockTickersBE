package com.stocktracker.weblayer.controllers;

/**
 * Created by mike on 12/3/2016.
 */
public abstract class AbstractHandler<I,O> extends AbstractAutowiredBean
{
    public abstract O handleRequest( I i );
}
