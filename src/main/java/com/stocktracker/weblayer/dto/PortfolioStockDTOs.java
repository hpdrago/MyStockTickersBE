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
    private List<PortfolioLastStockDTO> portfolioStocks = new ArrayList<>();

    public static PortfolioStockDTOs newInstance( final List<PortfolioLastStockDTO> portfolioStockDTOList )
    {
        Objects.requireNonNull( portfolioStockDTOList );
        PortfolioStockDTOs portfolioStockDTOs = new PortfolioStockDTOs();
        portfolioStockDTOs.portfolioStocks = portfolioStockDTOList;
        return portfolioStockDTOs;
    }
}
