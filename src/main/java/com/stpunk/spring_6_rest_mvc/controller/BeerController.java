package com.stpunk.spring_6_rest_mvc.controller;

import com.stpunk.spring_6_rest_mvc.model.BeerDTO;
import com.stpunk.spring_6_rest_mvc.services.BeerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@RestController
public class BeerController {

    private final BeerService beerService;
    private static final String BEER_PATH = "/api/v1/beer";
    private static final String BEER_PATH_ID = BEER_PATH + "/{beerId}";

    @PatchMapping(BEER_PATH_ID)
    public ResponseEntity updateBeerPatchById(@PathVariable("beerId") UUID beerId,
                                              @RequestBody BeerDTO beer) {

        beerService.patchBeerById(beerId, beer);

        return new ResponseEntity(beer, HttpStatus.NO_CONTENT);
    }

    @DeleteMapping(BEER_PATH_ID)
    public ResponseEntity deleteById(@PathVariable("beerId") UUID beerId) {

        beerService.deleteById(beerId);

        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @PutMapping(BEER_PATH_ID)
    public ResponseEntity updateById(@PathVariable("beerId") UUID beerId,
                                           @RequestBody BeerDTO beer) {

        beerService.updateBeerById(beerId, beer);

        return new ResponseEntity(beer, HttpStatus.NO_CONTENT);
    }

    @PostMapping(BEER_PATH)
    public ResponseEntity<BeerDTO> handlePost(@RequestBody BeerDTO beer) {

        BeerDTO savedBeer = beerService.saveNewBeer(beer);

        HttpHeaders headers = new HttpHeaders();

        headers.add("Location", savedBeer.getId().toString());
        return new ResponseEntity<>(savedBeer, headers, HttpStatus.CREATED);
    }

    @GetMapping(BEER_PATH)
    public List<BeerDTO> listBeers(){
        return beerService.listBeers();
    }

    @GetMapping(BEER_PATH_ID)
    public BeerDTO getBeerById(@PathVariable("beerId") UUID beerId) {
        log.debug("Getting Beer ID from controller. Another test");

        return beerService.getBeerById(beerId).orElseThrow(NotFoundException::new);
    }

}
