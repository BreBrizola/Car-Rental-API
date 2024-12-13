package com.dentsu.bootcamp.repository;

import com.dentsu.bootcamp.model.LocationEntity;
import io.reactivex.rxjava3.core.Maybe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.reactive.RxJava3CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LocationRepository extends RxJava3CrudRepository<LocationEntity, Long> {
    Maybe<LocationEntity> findByName(String name);
}
