package com.stpunk.spring_6_rest_mvc.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.stpunk.spring_6_rest_mvc.config.SpringSecConfig;
import com.stpunk.spring_6_rest_mvc.entities.Beer;
import com.stpunk.spring_6_rest_mvc.mappers.BeerMapper;
import com.stpunk.spring_6_rest_mvc.model.BeerDTO;
import com.stpunk.spring_6_rest_mvc.model.BeerStyle;
import com.stpunk.spring_6_rest_mvc.repositories.BeerRepository;
import org.hamcrest.core.IsNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.util.*;


import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@ActiveProfiles("proxmox-mysql")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@SpringBootTest
class BeerControllerIT {

    @Autowired
    BeerController beerController;

    @Autowired
    BeerRepository beerRepository;

    @Autowired
    BeerMapper beerMapper;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    WebApplicationContext webApplicationContext;

    @Value("${SS_USERNAME}")
    String username;

    @Value("${SS_PASSWORD}")
    String password;

    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(springSecurity())
                .build();
    }

    @Test
    void testListBeersByStyleAndNameShowInventoryTruePage2() throws Exception{

        mockMvc.perform(get(BeerController.BEER_PATH)
                        .with(httpBasic(username, password))
                        .queryParam("beerName", "IPA")
                        .queryParam("beerStyle", BeerStyle.IPA.name())
                        .queryParam("showInventory", "true")
                        .queryParam("pageNumber", "2")
                        .queryParam("pageSize", "50"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.size()", is(50)))
                .andExpect(jsonPath("$.content[0].quantityOnHand").value(IsNull.notNullValue()));

    }

    @Test
    void testListBeersByStyleAndNameShowInventoryFalse() throws Exception{

        mockMvc.perform(get(BeerController.BEER_PATH)
                        .with(httpBasic(username, password))
                        .queryParam("beerName", "IPA")
                        .queryParam("beerStyle", BeerStyle.IPA.name())
                        .queryParam("showInventory", "false")
                        .queryParam("pageSize", "800"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.size()", is(336)))
                .andExpect(jsonPath("$.content[0].quantityOnHand").value(IsNull.nullValue()));

    }

    @Test
    void testListBeersByStyleAndNameShowInventoryTrue() throws Exception{

        mockMvc.perform(get(BeerController.BEER_PATH)
                        .with(httpBasic(username, password))
                .queryParam("beerName", "IPA")
                .queryParam("beerStyle", BeerStyle.IPA.name())
                .queryParam("showInventory", "true")
                        .queryParam("pageSize", "800"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.size()", is(336)))
                .andExpect(jsonPath("$.content[0].quantityOnHand").value(IsNull.notNullValue()));

    }

    @Test
    void testNoAuth() throws Exception{

        mockMvc.perform(get(BeerController.BEER_PATH)
                        .queryParam("beerName", "IPA")
                        .queryParam("beerStyle", BeerStyle.IPA.name())
                        .queryParam("pageSize", "800"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testListBeersByStyleAndName() throws Exception{

        mockMvc.perform(get(BeerController.BEER_PATH)
                        .with(httpBasic(username, password))
                .queryParam("beerName", "IPA")
                .queryParam("beerStyle", BeerStyle.IPA.name())
                        .queryParam("pageSize", "800"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.size()", is(336)));

    }

    @Test
    void testListBeersByStyle() throws Exception {
        mockMvc.perform(get(BeerController.BEER_PATH)
                        .with(httpBasic(username, password))
                        .queryParam("beerStyle", BeerStyle.IPA.name()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.size()", is(25)));

    }

    @Test
    void testListBeersByName() throws Exception {
        mockMvc.perform(get(BeerController.BEER_PATH)
                        .with(httpBasic(username, password))
                .queryParam("beerName", "IPA")
                        .queryParam("pageSize", "800"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.size()", is(336)));

    }

    @Test
    void testPatchBeerBadName() throws Exception {

        Beer beer = beerRepository.findAll().get(0);

        Map<String, Object> beerMap = new HashMap<>();
        beerMap.put("beerName", "New nameNew nameNew nameNew nameNew nameNew nameNew nameNew nameNew name");

        MvcResult mvcResult = mockMvc.perform(patch(BeerController.BEER_PATH_ID, beer.getId())
                        .with(httpBasic(username, password))
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(beerMap)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.length()", is(1)))
                .andReturn();

        System.out.println(mvcResult.getResponse().getContentAsString());
    }

    @Test
    void testDeleteByIdNotFound() {

        assertThrows(NotFoundException.class, () -> {
            beerController.deleteById(UUID.randomUUID());
        });
    }

    @Rollback
    @Transactional
    @Test
    void deleteByIdFound() {

        Beer beer = beerRepository.findAll().get(0);

        ResponseEntity responseEntity = beerController.deleteById(beer.getId());
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(204));

        assertThat(beerRepository.findById(beer.getId())).isEmpty();
    }

    @Rollback
    @Transactional
    @Test
    void testUpdateNotFound() {

        assertThrows(NotFoundException.class, () -> {
            beerController.updateById(UUID.randomUUID(), BeerDTO.builder().build());
        });
    }

    @Test
    void updateExistingBeer() {

        Beer beer = beerRepository.findAll().get(0);
        BeerDTO beerDTO = beerMapper.beerToBeerDTO(beer);
        beerDTO.setId(null);
        beerDTO.setVersion(null);

        final String beerName = "UPDATED";
        beerDTO.setBeerName(beerName);

        ResponseEntity responseEntity = beerController.updateById(beer.getId(), beerDTO);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(204));

        Beer updatedBeer = beerRepository.findById(beer.getId()).get();
        assertThat(updatedBeer.getBeerName()).isEqualTo(beerName);

    }

    @Rollback
    @Transactional
    @Test
    void saveNewBeerTest() {
        BeerDTO beerDTO = BeerDTO.builder()
                .beerName("New Beer")
                .build();

        ResponseEntity responseEntity = beerController.handlePost(beerDTO);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(201));
        assertThat(responseEntity.getHeaders().getLocation()).isNotNull();

        String[] locationUUID = responseEntity.getHeaders().getLocation().getPath().split("/");
        UUID savedUUID = UUID.fromString(locationUUID[0]);

        Beer beer = beerRepository.findById(savedUUID).get();
        assertThat(beer).isNotNull();
    }

    @Test
    void testListBeers() {

        Page<BeerDTO> dtos = beerController.listBeers(null, null, false, 1, 25);

        assertThat(dtos.getContent().size()).isEqualTo(25);
    }

    @Rollback
    @Transactional
    @Test
    void testEmptyList() {
        beerRepository.deleteAll();

        Page<BeerDTO> dtos = beerController.listBeers(null, null, false, 1, 25);

        assertThat(dtos.getContent().size()).isEqualTo(0);
    }

    @Test
    void testGetById() {

        Beer beer = beerRepository.findAll().get(0);

        BeerDTO dto = beerController.getBeerById(beer.getId());

        assertThat(dto).isNotNull();
    }

    @Test
    void testBeerIdNotFound() {

        assertThrows(NotFoundException.class, () -> {
           beerController.getBeerById(UUID.randomUUID());
        });

    }
}