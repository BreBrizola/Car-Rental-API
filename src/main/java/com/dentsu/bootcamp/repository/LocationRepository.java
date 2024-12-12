package com.dentsu.bootcamp.repository;

import com.dentsu.bootcamp.model.LocationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.reactive.RxJava3CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LocationRepository extends RxJava3CrudRepository<LocationEntity, Long> {
    Optional<LocationEntity> findByName(String name);
}
