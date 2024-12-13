package com.dentsu.bootcamp.service;

import com.dentsu.bootcamp.dto.AdditionalProductDTO;
import com.dentsu.bootcamp.repository.AdditionalProductRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Maybe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdditionalProductService {

    @Autowired
    private AdditionalProductRepository additionalProductRepository;

    @Autowired
    private ObjectMapper objectMapper;

    public Maybe<AdditionalProductDTO> getAdditionProducts(long id) {
        return additionalProductRepository.findById(id)
                .map(product -> objectMapper.convertValue(product, AdditionalProductDTO.class));
    }

    public Flowable<AdditionalProductDTO> getAllAdditionalProducts() {
        return additionalProductRepository.findAll()
                .map(product -> objectMapper.convertValue(product, AdditionalProductDTO.class));
    }
}
