package com.rootcode.practicalevaluation.services;

import com.rootcode.practicalevaluation.dto.responses.StandardResponse;

public interface CrudService<T, ID> {
    StandardResponse insert(T dto);

    StandardResponse update(T dto);

    StandardResponse findById(ID id);

    StandardResponse deleteById(ID id);
}
