package com.dentsu.bootcamp.service;

import com.dentsu.bootcamp.dto.AdditionalProductDTO;
import com.dentsu.bootcamp.repository.AdditionalProductRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.reactivex.rxjava3.core.Observable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdditionalProductService {

    private final AdditionalProductRepository additionalProductRepository;

    private final ObjectMapper objectMapper;

    public AdditionalProductService(AdditionalProductRepository additionalProductRepository, ObjectMapper objectMapper){
        this.additionalProductRepository = additionalProductRepository;
        this.objectMapper = objectMapper;
    }

    public Observable<AdditionalProductDTO> getAdditionProducts(long id) {
        return Observable.fromCallable(() -> additionalProductRepository.findById(id))
                .flatMap(optinalProduct -> optinalProduct
                .map(product -> Observable.just(objectMapper.convertValue(product, AdditionalProductDTO.class)))
                .orElse(Observable.error(new RuntimeException("Product not found"))));
    }

    public Observable<List<AdditionalProductDTO>> getAllAdditionalProducts() {
        return Observable.fromCallable(() -> additionalProductRepository.findAll())
                .flatMap(list -> Observable.fromIterable(list)
                .map(product -> objectMapper.convertValue(product, AdditionalProductDTO.class))
                .toList()
                .toObservable());
    }
}
