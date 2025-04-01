package com.example.softwaredesigntechniques.service.common.impl;

import com.example.softwaredesigntechniques.exception.NotFoundException;
import com.example.softwaredesigntechniques.service.common.BaseService;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Transactional(readOnly = true)
public class DefaultBaseService<T, P extends Serializable> implements BaseService<T, P> {

    private final CrudRepository<T, P> crudRepository;

    public DefaultBaseService(@NotNull CrudRepository<T, P> crudRepository) {
        this.crudRepository = crudRepository;
    }

    @Override
    public T get(@NotNull P id) throws NotFoundException {
        return crudRepository
                .findById(id)
                .orElseThrow(() -> new NotFoundException("Record not found for id: " + id));
    }

    @Override
    public List<T> get(@NotNull List<P> ids) throws NotFoundException {
        List<T> list = new ArrayList<>();

        for (P id : ids) {
            list.add(get(id));
        }

        return list;
    }

    @Override
    @Transactional
    public T saveOrUpdate(@NotNull T t) {
        return crudRepository.save(t);
    }

    @Override
    @Transactional
    public List<T> saveOrUpdate(@NotNull List<T> t) {
        List<T> persisted = new ArrayList<>();

        for (T currentObject : t) {
            persisted.add(saveOrUpdate(currentObject));
        }
        return persisted;
    }

    @Override
    public void delete(@NotNull T t) {
        crudRepository.delete(t);
    }

    @Override
    public void deleteById(@NotNull Long id) {
        crudRepository.deleteById((P) id);
    }
} 