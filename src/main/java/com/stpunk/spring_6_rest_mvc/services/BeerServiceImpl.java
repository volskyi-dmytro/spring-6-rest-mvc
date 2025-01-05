package com.stpunk.spring_6_rest_mvc.services;

import com.stpunk.spring_6_rest_mvc.model.Beer;
import com.stpunk.spring_6_rest_mvc.model.BeerStyle;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Service
public class BeerServiceImpl implements BeerService {
    @Override
    public Beer getBeerById(UUID id) {

        log.debug("Getting Beer ID in service was called.");

        return Beer.builder()
                .id(id)
                .version(1)
                .beerName("Novo")
                .beerStyle(BeerStyle.IPA)
                .upc("111222")
                .price(new BigDecimal("47.99"))
                .quantityOnHand(100)
                .createdDate(LocalDateTime.now())
                .updatedDate(LocalDateTime.now())
                .build();
    }
}
