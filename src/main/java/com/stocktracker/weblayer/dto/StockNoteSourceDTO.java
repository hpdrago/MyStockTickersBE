package com.stocktracker.weblayer.dto;

/**
 * Created by mike on 9/10/2017.
 */
public class StockNoteSourceDTO
{
    private Integer id;
    private String name;
    private Integer customerId;


    public static StockNoteSourceDTO newInstance()
    {
        StockNoteSourceDTO stockNoteSourceDTO = new StockNoteSourceDTO();
        return stockNoteSourceDTO;
    }

    public Integer getId()
    {
        return id;
    }

    public void setId( final Integer id )
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName( final String name )
    {
        this.name = name;
    }

    public Integer getCustomerId()
    {
        return customerId;
    }

    public void setCustomerId( final Integer customerId )
    {
        this.customerId = customerId;
    }

    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder( "StockNoteSourceDTO{" );
        sb.append( "id=" ).append( id );
        sb.append( ", name='" ).append( name ).append( '\'' );
        sb.append( ", customerId=" ).append( customerId );
        sb.append( '}' );
        return sb.toString();
    }
}
