package com.stpunk.spring_6_rest_mvc;

import com.stpunk.spring_6_rest_mvc.config.SpringSecConfig;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("proxmox-mysql")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(SpringSecConfig.class)
@SpringBootTest
class Spring6RestMvcApplicationTests {

	@Test
	void contextLoads() {
	}

}
