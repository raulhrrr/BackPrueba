package com.prueba.api.services;

import java.sql.Date;
import java.util.Set;

public interface IBasicCrudService<T> {

    default Set<T> getAllByDate(Date fechaInicio, Date fechaFin, String filtro) {
        return null;
    }

    default Set<T> getAll(String filtro) {
        return null;
    }

    void create(T dto);

    void update(T dto);

    void delete(Integer id);

}
