package com.stocktracker.servicelayer.service;

import com.stocktracker.repositorylayer.entity.LinkedAccountPositionEntity;
import com.stocktracker.repositorylayer.repository.LinkedAccountPositionRepository;
import com.stocktracker.weblayer.dto.LinkedAccountPositionDTO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

public class PositionService extends StockQuoteContainerEntityService<Integer,
                                                                      LinkedAccountPositionEntity,
                                                                      LinkedAccountPositionDTO,
                                                                      LinkedAccountPositionRepository>
{
    @Autowired
    private ApplicationContext context;
    private LinkedAccountPositionRepository linkedAccountPositionRepository;

    @Override
    protected LinkedAccountPositionDTO entityToDTO( final LinkedAccountPositionEntity entity )
    {
        final LinkedAccountPositionDTO linkedAccountPositionDTO = this.context.getBean( LinkedAccountPositionDTO.class );
        BeanUtils.copyProperties( entity, linkedAccountPositionDTO );
        return linkedAccountPositionDTO;
    }

    @Override
    protected LinkedAccountPositionEntity dtoToEntity( final LinkedAccountPositionDTO dto )
    {
        final LinkedAccountPositionEntity entity = this.context.getBean( LinkedAccountPositionEntity.class );
        return null;
    }

    @Override
    protected LinkedAccountPositionRepository getRepository()
    {
        return linkedAccountPositionRepository;
    }

    @Autowired
    public void setLinkedAccountPositionRepository( final LinkedAccountPositionRepository linkedAccountPositionRepository )
    {
        this.linkedAccountPositionRepository = linkedAccountPositionRepository;
    }
}