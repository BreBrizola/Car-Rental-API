package com.dentsu.bootcamp.repository;

import com.dentsu.bootcamp.model.AdditionalProductEntity;
import org.springframework.data.repository.reactive.RxJava3CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdditionalProductRepository extends RxJava3CrudRepository<AdditionalProductEntity, Long> {
}
