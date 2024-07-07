package com.zahid.kc.ProductMicroservice.service;

import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.zahid.kc.ProductMicroservice.service.dto.CreateProductDTO;

import java.util.concurrent.ExecutionException;

public interface ProductService {
    String createProduct(CreateProductDTO createProductDTO) throws ExecutionException, InterruptedException;
}
