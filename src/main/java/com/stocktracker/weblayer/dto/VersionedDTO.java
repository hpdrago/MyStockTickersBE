package com.stocktracker.weblayer.dto;


import com.stocktracker.repositorylayer.entity.VersionedEntity;

import java.io.Serializable;

public interface VersionedDTO<K extends Serializable> extends VersionedEntity<K>
{
}
