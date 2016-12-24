package com.stocktracker.weblayer.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * This class contains a list a {@code PortfolioStockDTO} instances.
 *
 * Created by mike on 12/3/2016.
 */
public class PortfolioStockDTOs
{
    private List<PortfolioStockDTO> portfolioStocks = new ArrayList<>();

    public static PortfolioStockDTOs newInstance( final List<PortfolioStockDTO> portfolioStockDTOList )
    {
        PortfolioStockDTOs portfolioStockDTOs = new PortfolioStockDTOs();
        portfolioStockDTOs.portfolioStocks = portfolioStockDTOList;
        return portfolioStockDTOs;
    }
}
