package com.stpunk.spring_6_rest_mvc.repositories;

import com.stpunk.spring_6_rest_mvc.bootstrap.BootstrapData;
import com.stpunk.spring_6_rest_mvc.entities.Beer;
import com.stpunk.spring_6_rest_mvc.model.BeerStyle;
import com.stpunk.spring_6_rest_mvc.services.BeerCsvServiceImpl;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("default")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
@Import({BootstrapData.class, BeerCsvServiceImpl.class})
class BeerRepositoryTest {

    @Autowired
    BeerRepository beerRepository;

    @Test
    void testGetBeerListByName() {
        List<Beer> list = beerRepository.findAllByBeerNameIsLikeIgnoreCase("%IPA%");

        assertThat(list.size()).isEqualTo(336);
    }

    @Test
    void testSaveBeer() {

        Beer savedBeer = beerRepository.save(Beer.builder()
                        .beerName("My Beer")
                        .beerStyle(BeerStyle.IPA)
                        .upc("234234")
                        .price(new BigDecimal("12.33"))
                .build());

        beerRepository.flush();

        assertThat(savedBeer).isNotNull();
        assertThat(savedBeer.getId()).isNotNull();

    }

    @Test
    void testSaveBeerTooLongName() {

        assertThrows(ConstraintViolationException.class, () -> {

            Beer savedBeer = beerRepository.save(Beer.builder()
                    .beerName("My BeerMy BeerMy BeerMy BeerMy BeerMy BeerMy BeerMy BeerMy BeerMy Beer")
                    .beerStyle(BeerStyle.IPA)
                    .upc("234234")
                    .price(new BigDecimal("12.33"))
                    .build());

            beerRepository.flush();
        });

    }
}