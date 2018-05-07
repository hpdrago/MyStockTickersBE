package com.stocktracker.servicelayer.service;

import com.stocktracker.common.UUIDUtil;
import com.stocktracker.common.exceptions.DuplicateEntityException;
import com.stocktracker.common.exceptions.EntityVersionMismatchException;
import com.stocktracker.repositorylayer.common.NotesSourceUuidContainer;
import com.stocktracker.repositorylayer.entity.StockNoteSourceEntity;
import com.stocktracker.repositorylayer.entity.StockTagEntity;
import com.stocktracker.repositorylayer.entity.UUIDEntity;
import com.stocktracker.servicelayer.service.stocks.StockCompanyContainer;
import com.stocktracker.servicelayer.service.stocks.StockInformationService;
import com.stocktracker.servicelayer.service.stocks.StockPriceContainer;
import com.stocktracker.servicelayer.service.stocks.StockPriceWhenCreatedContainer;
import com.stocktracker.servicelayer.service.stocks.TickerSymbolContainer;
import com.stocktracker.servicelayer.stockinformationprovider.StockPriceFetchMode;
import com.stocktracker.weblayer.dto.common.NotesSourceIdContainer;
import com.stocktracker.weblayer.dto.common.TagsContainer;
import com.stocktracker.weblayer.dto.common.UuidDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * This is the base class for services that service DTO's that contain stock information.
 * @param <E> Entity type.
 * @param <D> DTO type.
 * @param <R> Repository type.
 */
public abstract class StockInformationEntityService<E extends UUIDEntity &
                                                              TickerSymbolContainer,
                                                    D extends StockPriceContainer &
                                                              UuidDTO,
                                                    R extends JpaRepository<E,UUID>>
    extends UuidEntityService<E,D,R>
{
    protected StockCompanyEntityService stockCompanyEntityService;

    protected StockInformationService stockInformationService;

    protected enum StockPriceFetchAction
    {
        NONE,
        FETCH;
        protected boolean isFetch() { return this == FETCH; }
    }

    /**
     * Saves the DTO to the database and checks for child entity data including notes sources and tags to determine
     * if there are any changes there to be updated/inserted.
     * @param dto
     * @return DTO after saved to the database.
     * @throws EntityVersionMismatchException
     */
    @Override
    public D saveDTO( final D dto )
        throws EntityVersionMismatchException
    {
        final String methodName = "saveDTO";
        logMethodBegin( methodName, dto );
        Objects.requireNonNull( dto, "dto cannot be null" );
        final D returnDTO = super.saveDTO( dto );
        this.saveChildEntities( dto, returnDTO );
        logMethodEnd( methodName, returnDTO );
        return returnDTO;
    }

    /**
     * Adds the DTO to the database and checks for child entity data including notes sources and tags to determine
     * if there are any changes there to be updated/inserted.
     * @param dto
     * @return
     * @throws EntityVersionMismatchException
     * @throws DuplicateEntityException
     */
    @Override
    public D addDTO( final D dto )
        throws EntityVersionMismatchException, DuplicateEntityException
    {
        final String methodName = "addDTO";
        logMethodBegin( methodName, dto );
        Objects.requireNonNull( dto, "dto cannot be null" );
        final D returnDTO = super.addDTO( dto );
        this.saveChildEntities( dto, returnDTO );
        logMethodEnd( methodName, returnDTO );
        return returnDTO;
    }

    /**
     * Checks tags and notes sources to see if there are any changes to be made.
     * @param dto
     * @param returnDTO
     */
    private void saveChildEntities( final D dto, final D returnDTO )
    {
        /*
         * Save any tags contained in the dto
         */
        if ( dto instanceof TagsContainer )
        {
            this.saveStockTags( dto, returnDTO );
        }
        /*
         * Check for new sources
         */
        if ( dto instanceof NotesSourceIdContainer )
        {
            this.stockNoteSourceService
                .checkForNewSource( (NotesSourceIdContainer)dto );
        }
    }

    /**
     * Checks for new stock tags and add them to the database.
     * @param originalDTO Contains the original values received from the client.
     * @param newDTO This DTO contains the values after being saved/inserted into the database.
     */
    protected void saveStockTags( final D originalDTO, final D newDTO )
    {
        final String methodName = "saveStockTags";
        logMethodBegin( methodName, originalDTO, newDTO );
        final TagsContainer container = (TagsContainer)originalDTO;
        this.stockTagService.saveStockTags( UUIDUtil.uuid( container.getCustomerId() ),
                                            container.getTickerSymbol(),
                                            container.getStockTagReferenceType(),
                                            UUIDUtil.uuid( newDTO.getId() ),
                                            container.getTags() );
        logMethodEnd( methodName );
    }


    /**
     * Transforms {@code Page<ENTITY>} objects into {@code Page<DTO>} objects.
     *
     * @param pageRequest The information of the requested page.
     * @param entityPage The {@code Page<ENTITY>} object.
     * @return The created {@code Page<DTO>} object.
     */
    protected Page<D> entitiesToDTOs( @NotNull final Pageable pageRequest,
                                      @NotNull final Page<E> entityPage )
    {
        return this.entitiesToDTOs( pageRequest, entityPage, StockPriceFetchAction.FETCH );
    }

    /**
     * Converts a list of entities to to a list of DTO's.
     * @param entities
     * @return
     */
    protected List<D> entitiesToDTOs( @NotNull final List<E> entities,
                                      @NotNull final StockPriceFetchAction stockPriceFetchAction )
    {
        final List<D> dtos = super.entitiesToDTOs( entities );
        if ( stockPriceFetchAction.isFetch() )
        {
            this.getStockPrices( dtos );
        }
        return dtos;
    }

    /**
     * Transforms {@code Page<ENTITY>} objects into {@code Page<DTO>} objects.
     *
     * @param pageRequest The information of the requested page.
     * @param entityPage The {@code Page<ENTITY>} object.
     * @param stockPriceFetchAction Determines if the stock quotes should be fetched.
     * @return The created {@code Page<DTO>} object.
     */
    protected Page<D> entitiesToDTOs( @NotNull final Pageable pageRequest,
                                      @NotNull final Page<E> entityPage,
                                      @NotNull final StockPriceFetchAction stockPriceFetchAction )
    {
        final String methodName = "entitiesToDTOs";
        logMethodBegin( methodName, pageRequest );
        Objects.requireNonNull( pageRequest, "pageRequest cannot be null" );
        Objects.requireNonNull( entityPage, "source cannot be null" );
        final List<D> dtos = this.entitiesToDTOs( entityPage.getContent(), stockPriceFetchAction );
        logMethodEnd( methodName );
        return new PageImpl<>( dtos, pageRequest, entityPage.getTotalElements() );
    }

    /**
     * Updates the stock quote information for all dtos
     * @param dtos
     */
    protected void getStockPrices( final List<? extends StockPriceContainer> dtos )
    {
        final String methodName = "getStockPrices";
        logMethodBegin( methodName );
        this.stockInformationService
            .setStockPrice( dtos );
        logMethodEnd( methodName );
    }

    /**
     * Converts the entity to a DTO and sets the stock price on the return DTO.
     * @param entity Contains the entity information.
     * @return
     */
    @Override
    protected D entityToDTO( final E entity )
    {
        //final String methodName = "entityToDTO";
        //logMethodBegin( methodName, entity );
        Objects.requireNonNull( entity, "entity argument cannot be null" );
        D dto = super.entityToDTO( entity );
        /*
         * I think this is a good use of instanceof...although I am not convinced, I'll have to think about this...
         * If any stock DTO is a stock company container, it will be populated automatically with the stock company
         * information.  No need for any sub cvl
         */
        if ( dto instanceof StockCompanyContainer )
        {
            this.stockInformationService
                .setCompanyInformation( (StockCompanyContainer)dto );
        }
        /*
         * Convert the UUID to a string and get the notes source name for the UUID
         */
        if ( entity instanceof NotesSourceUuidContainer &&
             dto instanceof NotesSourceIdContainer )
        {
            final NotesSourceUuidContainer notesSourceUuidContainer = (NotesSourceUuidContainer)entity;
            if ( notesSourceUuidContainer.getNotesSourceEntity().isPresent() )
            {
                final NotesSourceIdContainer notesSourceIdContainer = (NotesSourceIdContainer)dto;
                /*
                final StockNoteSourceEntity stockNoteSourceEntity = this.stockNoteSourceService
                                                                        .getStockNoteSource( notesSourceUuidContainer
                                                                                             .getNotesSourceUuid() );
                                                                                             */
                final StockNoteSourceEntity stockNoteSourceEntity = notesSourceUuidContainer.getNotesSourceEntity().get();
                notesSourceIdContainer.setNotesSourceName( stockNoteSourceEntity.getName() );
                notesSourceIdContainer.setNotesSourceId( stockNoteSourceEntity.getUuid().toString() );
            }
        }

        if ( dto instanceof TagsContainer )
        {
            final TagsContainer tagsContainer = (TagsContainer)dto;
            tagsContainer.setTags( this.stockTagService.findStockTags( UUIDUtil.uuid( tagsContainer.getCustomerId() ),
                                                                       StockTagEntity.StockTagReferenceType.STOCK_TO_BUY,
                                                                       UUIDUtil.uuid( tagsContainer.getEntityId() ) ) );
        }
        //logMethodEnd( methodName, dto );
        return dto;
    }

    @Override
    protected E dtoToEntity( final D dto )
    {
        //final String methodName = "dtoToEntity";
        //logMethodBegin( methodName, dto );
        Objects.requireNonNull( dto, "dto argument cannot be null" );
        final E entity = super.dtoToEntity( dto );
        /*
         * Convert the notes source ID to UUID
         */
        if ( dto instanceof NotesSourceIdContainer &&
             entity instanceof NotesSourceUuidContainer )
        {
            final NotesSourceIdContainer notesSourceIdContainer = (NotesSourceIdContainer) dto;
            /*
             * Check for new sources
             */
            this.stockNoteSourceService.checkForNewSource( notesSourceIdContainer );
            final NotesSourceUuidContainer notesSourceUuidContainer = (NotesSourceUuidContainer)entity;
            /*
             * Convert the String UUID to a real uuid if present
             */
            if ( notesSourceIdContainer.getNotesSourceId() != null &&
                 !notesSourceIdContainer.getNotesSourceId().isEmpty() )
            {
                final StockNoteSourceEntity stockNoteSourceEntity = this.stockNoteSourceService
                                                                        .getStockNoteSource( notesSourceIdContainer.getNotesSourceId() );
                notesSourceUuidContainer.setNotesSourceEntity( stockNoteSourceEntity );
            }
        }
        //logMethodEnd( methodName, entity );
        return entity;
    }

    /**
     * Updates the stock price after adding the DTO to the database.
     * @param dto
     */
    @Override
    protected void postAddDTO( final D dto )
    {
        final String methodName = "postAddDTO";
        logMethodBegin( methodName, dto );
        Objects.requireNonNull( dto, "dto argument cannot be null" );
        super.postAddDTO( dto );
        this.updateStockPrice( dto );
        logMethodEnd( methodName );
    }

    /**
     * Updates the stock price after updating the DTO in the database.
     * @param dto
     */
    @Override
    protected void postSaveDTO( final D dto )
    {
        final String methodName = "postSaveDTO";
        logMethodBegin( methodName, dto );
        Objects.requireNonNull( dto, "dto argument cannot be null" );
        super.postSaveDTO( dto );
        this.updateStockPrice( dto );
        logMethodEnd( methodName );
    }

    /**
     * Ansynchronous update request of the stock price.
     * @param dto
     */
    protected void updateStockPrice( final D dto )
    {
        final String methodName = "updateStockPrice";
        logMethodBegin( methodName, dto );
        Objects.requireNonNull( dto, "dto argument cannot be null" );
        this.stockInformationService
            .setStockPrice( dto, StockPriceFetchMode.ASYNCHRONOUS );
        logMethodEnd( methodName );
    }

    /**
     * Add the DTO and check the ticker symbol to ensure a company exists or create one if not.
     * @param dto
     */
    @Override
    protected void preAddDTO( final D dto )
    {
        final String methodName = "preAddDTO";
        logMethodBegin( methodName, dto );
        Objects.requireNonNull( dto, "dto argument cannot be null" );
        checkTickerSymbol( dto );
        super.preAddDTO( dto );
        logMethodEnd( methodName );
    }

    /**
     * Before the entity is added, need to set the stock price when created field if the entity contains one.
     * @param entity
     */
    @Override
    protected void preAddEntity( final E entity )
    {
        final String methodName = "preAddEntity";
        logMethodBegin( methodName, entity );
        Objects.requireNonNull( entity, "entity argument cannot be null" );
        super.preAddEntity( entity );
        if ( entity instanceof StockPriceWhenCreatedContainer )
        {
            setStockPriceWhenCreated( entity.getTickerSymbol(), (StockPriceWhenCreatedContainer)entity );
        }
        logMethodEnd( methodName );
    }

    /**
     * Sets the stock price when created field with the current stock price value -- this is a synchronous call to
     * get the stockprice.
     * @param tickerSymbol
     * @param entity
     */
    protected void setStockPriceWhenCreated( final String tickerSymbol, final StockPriceWhenCreatedContainer entity )
    {
        Objects.requireNonNull( tickerSymbol, "tickerSymbol argument cannot be null" );
        Objects.requireNonNull( entity, "entity argument cannot be null" );
        /*
         * The stock price needs to be set the first time as it records the stock price when the record was created.
         */
        entity.setStockPriceWhenCreated( this.stockInformationService
                                             .getLastPrice( tickerSymbol ));
    }

    /**
     * Save the DTO and check the ticker symbol to ensure a company exists or create one if not.
     * @param dto
     */
    @Override
    protected void preSaveDTO( final D dto )
    {
        final String methodName = "preSaveDTO";
        logMethodBegin( methodName, dto );
        checkTickerSymbol( dto );
        super.preSaveDTO( dto );
        logMethodEnd( methodName );
    }

    /**
     * Validate the ticker symbol and add a new stock_company table entry if necessary.
     * @param dto
     */
    protected void checkTickerSymbol( final D dto )
    {
        final String methodName = "checkTickerSymbol";
        logMethodBegin( methodName, dto );
        Objects.requireNonNull( dto, "dto argument cannot be null" );
        this.stockCompanyEntityService
            .checkStockTableEntry( dto.getTickerSymbol() );
        logMethodEnd( methodName );
    }

    @Autowired
    public void setStockCompanyEntityService( final StockCompanyEntityService stockCompanyEntityService )
    {
        this.stockCompanyEntityService = stockCompanyEntityService;
    }

    @Autowired
    public void setStockInformationService( final StockInformationService stockInformationService )
    {
        this.stockInformationService = stockInformationService;
    }

    protected StockInformationService getStockInformationService()
    {
        return stockInformationService;
    }

}
