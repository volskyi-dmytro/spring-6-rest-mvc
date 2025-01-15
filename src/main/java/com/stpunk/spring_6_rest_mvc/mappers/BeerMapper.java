package com.stpunk.spring_6_rest_mvc.mappers;

import com.stpunk.spring_6_rest_mvc.entities.Beer;
import com.stpunk.spring_6_rest_mvc.model.BeerDTO;
import org.mapstruct.Mapper;

@Mapper
public interface BeerMapper {

    Beer beerDTOtoBeer(BeerDTO dto);

    BeerDTO beerToBeerDTO(Beer beer);
}
