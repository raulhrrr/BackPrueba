package com.prueba.api.services;

import java.sql.Date;
import java.util.Set;

public interface ICrudService<T, R> {

    default Set<R> getAll(String filtro) {
        return null;
    }

    default Set<R> getAllByDate(Date fechaInicio, Date fechaFin, String filtro) {
        return null;
    }

    default Set<T> getByDatesAndId(Date fechaInicio, Date fechaFin, Integer clientId) {
        return null;
    }

    default void create(T dto) {
    }

    default void update(T dto) {
    }

    default void delete(Integer id) {
    }

}
