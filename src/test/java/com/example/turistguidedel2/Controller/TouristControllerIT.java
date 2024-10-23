//integration / mockmvc
package com.example.turistguidedel2.Controller;

import com.example.turistguidedel2.Model.TouristAttraction;
import com.example.turistguidedel2.Service.TouristService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TouristControllerIT {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private TouristService touristService;

    // Test GET /attractions
    @Test
    public void testGetAllTouristAttractions() {
        // Prepopulate data (using real service)
        TouristAttraction attraction1 = new TouristAttraction("Bakken", "Amusement park", "Klampenborg", 2930, "nature");
        touristService.saveTouristAttractions(attraction1);

        String url = "http://localhost:" + port + "/attractions";

        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        //assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).contains("Bakken");
    }

    // Example POST integration test
    @Test
    public void testSaveTouristAttractions() {
        String url = "http://localhost:" + port + "/attractions/save";
        TouristAttraction newAttraction = new TouristAttraction("SMK", "Museum", "Copenhagen", 1307, "museum");

        HttpEntity<TouristAttraction> request = new HttpEntity<>(newAttraction);
        ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);

        //assertThat(response.getStatusCodeValue()).isEqualTo(302);  // Redirection
        assertThat(touristService.getTouristAttractionByName("SMK")).isNotNull();
    }
}