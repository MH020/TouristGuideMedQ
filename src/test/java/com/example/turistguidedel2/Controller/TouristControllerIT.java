//integration test for TouristController
package com.example.turistguidedel2.Controller;

import com.example.turistguidedel2.Service.TouristService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
//@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
public class TouristControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TouristService touristService;

    @Test
    public void testGetAllTouristAttractions() throws Exception {
        mockMvc.perform(get("/attractions"))
                .andExpect(status().isOk())
                .andExpect(view().name("attractionList"))
                .andExpect(model().attributeExists("allTouristAttractions"))
                .andExpect(model().attribute("allTouristAttractions", org.hamcrest.Matchers.hasSize(5))); // Ensure 5 initial attractions
    }

    @Test
    public void testSaveTouristAttractions() throws Exception {
        mockMvc.perform(post("/attractions/save")
                        //.contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("name", "Statue of Liberty")
                        .param("description", "Iconic statue in New York City.")
                        .param("postcode", "10001")
                        .param("tags", "Landmark"))
                //.andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/attractions"));

        // Verify the new tourist attraction was added
        mockMvc.perform(get("/attractions"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("allTouristAttractions"))
                .andExpect(model().attribute("allTouristAttractions", org.hamcrest.Matchers.hasSize(6))); // 5 + 1 new attraction
    }

    @Test
    public void testDeleteTouristAttraction() throws Exception {
        mockMvc.perform(post("/attractions/{name}/delete", "Springfield Museum"))
                //.andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/attractions"));

        // Verify the attraction is no longer in the database
        mockMvc.perform(get("/attractions"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("allTouristAttractions", org.hamcrest.Matchers.hasSize(5))); // Assuming 6 attractions, one deleted
    }
}