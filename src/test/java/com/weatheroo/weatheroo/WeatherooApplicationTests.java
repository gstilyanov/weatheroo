package com.weatheroo.weatheroo;

import com.weatheroo.weatheroo.service.WeatherService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest
class WeatherooApplicationTests {

	@MockitoBean
	private WeatherService weatherService;

	@Test
	void contextLoads() {
	}

}
