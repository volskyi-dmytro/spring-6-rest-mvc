package com.stpunk.spring_6_rest_mvc.controller;

import com.stpunk.spring_6_rest_mvc.model.Beer;
import com.stpunk.spring_6_rest_mvc.services.BeerService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@Slf4j
@AllArgsConstructor
@RestController
public class BeerController {

    private final BeerService beerService;

    @RequestMapping("/api/v1/beer")
    public List<Beer> listBeers(){
        return beerService.listBeers();
    }

    public Beer getBeerById(UUID id) {
        log.debug("Getting Beer ID from controller");

        return beerService.getBeerById(id);
    }

}
