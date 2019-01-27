package org.modules.base.dao;

import org.modules.base.domain.Auditable;

import java.util.List;
import java.util.Map;

/**
 * Created by aleksandra on 05.10.17.
 */
public interface BaseDao<T extends Auditable> {

    T getOne(Long id);

    Map<Long, T> getAll();

    void add(T obj);

}
