package com.stpunk.spring_6_rest_mvc.bootstrap;

import com.stpunk.spring_6_rest_mvc.entities.Beer;
import com.stpunk.spring_6_rest_mvc.entities.Customer;
import com.stpunk.spring_6_rest_mvc.model.BeerDTO;
import com.stpunk.spring_6_rest_mvc.model.BeerStyle;
import com.stpunk.spring_6_rest_mvc.model.CustomerDTO;
import com.stpunk.spring_6_rest_mvc.repositories.BeerRepository;
import com.stpunk.spring_6_rest_mvc.repositories.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class BootstrapData implements CommandLineRunner {

    private final BeerRepository beerRepository;
    private final CustomerRepository customerRepository;

    @Override
    public void run(String... args) throws Exception {
        loadBeerData();
        loadCustomerData();
    }

    private void loadBeerData() {

        if (beerRepository.count() == 0) {
            Beer beer1 = Beer.builder()
                    .beerName("Obolon")
                    .beerStyle(BeerStyle.PALE_ALE)
                    .upc("1111")
                    .price(new BigDecimal("2.99"))
                    .quantityOnHand(22)
                    .createdDate(LocalDateTime.now())
                    .updatedDate(LocalDateTime.now())
                    .build();

            Beer beer2 = Beer.builder()
                    .beerName("Slavutych")
                    .beerStyle(BeerStyle.PALE_ALE)
                    .upc("2222")
                    .price(new BigDecimal("1.99"))
                    .quantityOnHand(92)
                    .createdDate(LocalDateTime.now())
                    .updatedDate(LocalDateTime.now())
                    .build();

            Beer beer3 = Beer.builder()
                    .beerName("Rohan")
                    .beerStyle(BeerStyle.IPA)
                    .upc("3333")
                    .price(new BigDecimal("3.99"))
                    .quantityOnHand(144)
                    .createdDate(LocalDateTime.now())
                    .updatedDate(LocalDateTime.now())
                    .build();

            beerRepository.save(beer1);
            beerRepository.save(beer2);
            beerRepository.save(beer3);
        }

    }

    private void loadCustomerData() {

        if(customerRepository.count() == 0) {

            Customer customer1 = Customer.builder()
                    .customerName("Adam")
                    .createdDate(LocalDateTime.now())
                    .lastModifiedDate(LocalDateTime.now())
                    .build();

            Customer customer2 = Customer.builder()
                    .customerName("Bobby")
                    .createdDate(LocalDateTime.now())
                    .lastModifiedDate(LocalDateTime.now())
                    .build();

            Customer customer3 = Customer.builder()
                    .customerName("Casey")
                    .createdDate(LocalDateTime.now())
                    .lastModifiedDate(LocalDateTime.now())
                    .build();

            customerRepository.save(customer1);
            customerRepository.save(customer2);
            customerRepository.save(customer3);

        }
    }
}
