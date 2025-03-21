package com.stpunk.spring_6_rest_mvc.repositories;

import com.stpunk.spring_6_rest_mvc.entities.BeerOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface BeerOrderRepository extends JpaRepository<BeerOrder, UUID> {

}
