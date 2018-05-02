package com.stocktracker.repositorylayer.entity;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;
import java.util.Objects;
import java.util.UUID;

@MappedSuperclass
public abstract class UUIDEntity extends BaseEntity<UUID>

{
    private UUID uuid;

    @Id
    @GeneratedValue( generator = "UUID")
    @GenericGenerator(
        name = "UUID",
        strategy = "org.hibernate.id.UUIDGenerator",
        parameters =
            {
                @Parameter
                    (
                        name = "uuid_gen_strategy_class",
                        value = "org.hibernate.id.uuid.CustomVersionOneStrategy"
                    )
            }
    )
    @Column( name = "uuid", columnDefinition = "binary(16)", updatable = false, nullable = false)
    public UUID getUuid()
    {
        return uuid;
    }

    public void setUuid( final UUID uuid )
    {
        this.uuid = uuid;
    }

    @Transient
    @Override
    public UUID getId()
    {
        return this.uuid;
    }

    @Transient
    public void setId( final UUID uuid )
    {
        this.uuid = uuid;
    }

    /**
     * Get the string version of the uuid.
     * @return
     */
    @Transient
    public String getUuidString()
    {
        return getUuid() == null ? "null"
                                 : getUuid().toString();
    }

    @Override
    public boolean equals( final Object o )
    {
        if ( this == o )
        {
            return true;
        }
        if ( o == null || getClass() != o.getClass() )
        {
            return false;
        }
        final CustomerEntity that = (CustomerEntity) o;
        return Objects.equals( uuid, that.getUuid() );
    }

    @Override
    public int hashCode()
    {
        return Objects.hash( uuid );
    }
}
