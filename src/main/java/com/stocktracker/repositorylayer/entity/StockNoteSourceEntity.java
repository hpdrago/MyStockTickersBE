package com.stocktracker.repositorylayer.entity;

import com.stocktracker.repositorylayer.common.CustomerUuidContainer;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.UUID;

/**
 * Created by mike on 5/7/2017.
 */
@Component
@Scope( BeanDefinition.SCOPE_PROTOTYPE )
@Entity
@Table( name = "stock_note_source", schema = "stocktracker", catalog = "" )
public class StockNoteSourceEntity extends UUIDEntity
                                   implements CustomerUuidContainer
{
    private String name;
    private UUID customerUuid;
    private Integer timesUsed;
    /*
    private Collection<StockAnalystConsensusEntity> stockAnalystConsensusesById;
    private Collection<StockNoteEntity> stockNotesById;
    private Collection<StockToBuyEntity> stockToBuysById;
    */

    public static StockNoteSourceEntity newInstance()
    {
        StockNoteSourceEntity stockNoteSourceEntity = new StockNoteSourceEntity();
        return stockNoteSourceEntity;
    }

    @Basic
    @Column( name = "name", nullable = false, length = 20 )
    public String getName()
    {
        return name;
    }

    public void setName( final String name )
    {
        this.name = name;
    }

    @Basic
    @Column( name = "customer_uuid", nullable = false )
    public UUID getCustomerUuid()
    {
        return customerUuid;
    }

    public void setCustomerUuid( final UUID customerId )
    {
        this.customerUuid = customerId;
    }

    @Basic
    @Column( name = "times_used", nullable = false, insertable = false, updatable = false )
    public Integer getTimesUsed()
    {
        return timesUsed;
    }

    public void setTimesUsed( final Integer timesUsed )
    {
        this.timesUsed = timesUsed;
    }

    /*
    @OneToMany( mappedBy = "stockNoteSourceByNoteSourceUuid" )
    public Collection<StockAnalystConsensusEntity> getStockAnalystConsensusesById()
    {
        return stockAnalystConsensusesById;
    }

    public void setStockAnalystConsensusesById( final Collection<StockAnalystConsensusEntity> stockAnalystConsensusesById )
    {
        this.stockAnalystConsensusesById = stockAnalystConsensusesById;
    }

    @OneToMany( mappedBy = "stockNoteSourceByNotesSourceUuid" )
    public Collection<StockNoteEntity> getStockNotesById()
    {
        return stockNotesById;
    }

    public void setStockNotesById( final Collection<StockNoteEntity> stockNotesById )
    {
        this.stockNotesById = stockNotesById;
    }

    @OneToMany( mappedBy = "stockNoteSourceByNotesSourceUuid" )
    public Collection<StockToBuyEntity> getStockToBuysById()
    {
        return stockToBuysById;
    }

    public void setStockToBuysById( final Collection<StockToBuyEntity> stockToBuysById )
    {
        this.stockToBuysById = stockToBuysById;
    }
    */

    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder( "StockNoteSourceEntity{" );
        sb.append( "uuid=" ).append( getUuidString() );
        sb.append( ", name='" ).append( name ).append( '\'' );
        sb.append( ", customerUuid=" ).append( customerUuid );
        sb.append( ", timesUsed=" ).append( timesUsed );
        sb.append( ", super=" ).append( super.toString() );
        sb.append( '}' );
        return sb.toString();
    }
}
