package com.stocktracker.weblayer.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * This class contains a list a {@code PortfolioLastStockDTO} instances.
 *
 * Created by mike on 12/3/2016.
 */
public class PortfolioStockDTOs
{
    private List<PortfolioStockDTO> portfolioStocks = new ArrayList<>();

    public static PortfolioStockDTOs newInstance( final List<PortfolioStockDTO> portfolioStockDTOList )
    {
        Objects.requireNonNull( portfolioStockDTOList );
        PortfolioStockDTOs portfolioStockDTOs = new PortfolioStockDTOs();
        portfolioStockDTOs.portfolioStocks = portfolioStockDTOList;
        return portfolioStockDTOs;
    }
}
