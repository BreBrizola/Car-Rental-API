package com.dentsu.bootcamp.repository;

import com.dentsu.bootcamp.model.TermsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TermsRepository extends JpaRepository<TermsEntity, Long> {
}
