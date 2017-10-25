package com.stocktracker.weblayer.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Objects;

/**
 * This DTO is returned to the UI after a stock has been added to a portfolio.
 * It contains the Stock that was added, the Portfolio that the Stock was added to
 * and the list of all Stocks in the Portfolio.
 *
 * The Portfolio is returned so that Portfolio details can be updated.
 * The Stocks for the Portfolio are returned so that the Stocks in the Portfolio table
 * can be update.
 *
 * Created by mike on 12/3/2016.
 */
public class AddPortfolioStockDTO
{
    @JsonProperty
    private PortfolioDTO portfolioDTO;
    @JsonProperty
    private PortfolioLastStockDTO portfolioStockDTO;
    @JsonProperty
    private PortfolioStockDTOs portfolioStockDTOs;

    /**
     * Creates a new instance.  Converts the DE instances to DTOs
     * @param portfolioStockDTO
     * @param portfolioDTO
     * @param portfolioStockDTOList
     * @return Fully populated {@code AddPortfolioStockDTO}
     */
    public static AddPortfolioStockDTO newInstance( final PortfolioLastStockDTO portfolioStockDTO,
                                                    final PortfolioDTO portfolioDTO,
                                                    final List<PortfolioLastStockDTO> portfolioStockDTOList )
    {
        Objects.requireNonNull( portfolioStockDTO );
        Objects.requireNonNull( portfolioDTO );
        Objects.requireNonNull( portfolioStockDTOList );
        AddPortfolioStockDTO addPortfolioStockDTO = new AddPortfolioStockDTO();
        addPortfolioStockDTO.setPortfolioStockDTO( portfolioStockDTO );
        addPortfolioStockDTO.setPortfolioDTO( portfolioDTO );
        addPortfolioStockDTO.setPortfolioStockDTOs( PortfolioStockDTOs.newInstance( portfolioStockDTOList ));
        return addPortfolioStockDTO;
    }

    public PortfolioDTO getPortfolioDTO()
    {
        return portfolioDTO;
    }

    public void setPortfolioDTO( PortfolioDTO portfolioDTO )
    {
        this.portfolioDTO = portfolioDTO;
    }

    public PortfolioLastStockDTO getPortfolioStockDTO()
    {
        return portfolioStockDTO;
    }

    public void setPortfolioStockDTO( PortfolioLastStockDTO portfolioStockDTO )
    {
        this.portfolioStockDTO = portfolioStockDTO;
    }

    public PortfolioStockDTOs getPortfolioStockDTOs()
    {
        return portfolioStockDTOs;
    }

    public void setPortfolioStockDTOs( PortfolioStockDTOs portfolioStockDTOs )
    {
        this.portfolioStockDTOs = portfolioStockDTOs;
    }
}
