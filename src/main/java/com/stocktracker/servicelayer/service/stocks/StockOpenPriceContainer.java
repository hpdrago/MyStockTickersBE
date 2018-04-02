package com.stocktracker.servicelayer.service.stocks;

import java.math.BigDecimal;

public interface StockOpenPriceContainer
{
    void setOpenPrice( final BigDecimal openPrice );
    BigDecimal getOpenPrice();
}
