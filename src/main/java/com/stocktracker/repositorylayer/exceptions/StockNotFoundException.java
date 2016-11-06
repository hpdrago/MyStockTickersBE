package com.stocktracker.repositorylayer.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by mike on 11/5/2016.
 */
@ResponseStatus(value= HttpStatus.NOT_FOUND, reason="Stock not found")  // 404
public class StockNotFoundException extends RuntimeException
{
}
