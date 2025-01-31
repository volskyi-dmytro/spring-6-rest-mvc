package com.stpunk.spring_6_rest_mvc.repositories;

import com.stpunk.spring_6_rest_mvc.entities.Beer;
import com.stpunk.spring_6_rest_mvc.model.BeerStyle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface BeerRepository extends JpaRepository<Beer, UUID> {

    List<Beer> findAllByBeerNameIsLikeIgnoreCase(String beerName);

    List<Beer> findAllByBeerStyle(BeerStyle beerStyle);

}
