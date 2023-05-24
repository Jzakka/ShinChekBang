package com.ll.ShinChekBang.base.service;

import com.ll.ShinChekBang.base.result.RsData;

public interface BaseService<T> {
    RsData<T> findOne(Long id);
}
