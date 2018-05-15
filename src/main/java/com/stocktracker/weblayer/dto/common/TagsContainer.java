package com.stocktracker.weblayer.dto.common;

import com.stocktracker.repositorylayer.entity.StockTagEntity;
import com.stocktracker.servicelayer.service.stocks.TickerSymbolContainer;

import java.util.List;

/**
 * This interface is implemented by DTO's that contain tags.
 */
public interface TagsContainer extends TickerSymbolContainer,
                                       CustomerIdContainer
{
    /**
     * Set the tags.
     * @param tags
     */
    void setTags( final List<String> tags );

    /**
     * Get the tags.
     * @return
     */
    List<String> getTags();

    /**
     * Get the Customer ID (String UUID)
     * @return
     */
    String getCustomerId();

    /**
     * Get the ticker symbol.
     * @return
     */
    String getTickerSymbol();

    /**
     * Get the type of stock tag
     * @return
     */
    StockTagEntity.StockTagReferenceType getStockTagReferenceType();

    /**
     * Get the entity id.
     * @return
     */
    String getEntityId();
}
