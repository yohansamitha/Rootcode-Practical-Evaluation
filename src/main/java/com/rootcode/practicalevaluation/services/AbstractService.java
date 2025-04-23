package com.rootcode.practicalevaluation.services;

import com.rootcode.practicalevaluation.dto.responses.StandardResponse;

public interface AbstractService<T, ID> {
    StandardResponse insert(T t);

    StandardResponse update(T u);

    StandardResponse findById(ID id);

    StandardResponse deleteById(ID id);
}
