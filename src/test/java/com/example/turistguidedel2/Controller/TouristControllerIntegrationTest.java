package com.example.turistguidedel2.Controller;

import com.example.turistguidedel2.Repository.TouristRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc // Auto-configures MockMvc
@Transactional        // Rollback changes after each test
@TestPropertySource(locations = "classpath:application-test.properties")
public class TouristControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TouristRepository touristRepository;

    @Test
    public void testGetAllTouristAttractions() throws Exception {
        mockMvc.perform(get("/attractions"))
                .andExpect(status().isOk())
                .andExpect(view().name("attractionList"))
                .andExpect(model().attributeExists("allTouristAttractions"))
                .andExpect(model().attribute("allTouristAttractions", org.hamcrest.Matchers.hasSize(5))) // Assuming you have 5 initial attractions in your data.sql
                .andExpect(model().attribute("allTouristAttractions", org.hamcrest.Matchers.contains(
                        org.hamcrest.Matchers.hasProperty("name", org.hamcrest.Matchers.is("Springfield Museum")),
                        org.hamcrest.Matchers.hasProperty("name", org.hamcrest.Matchers.is("Shelbyville Park")),
                        org.hamcrest.Matchers.hasProperty("name", org.hamcrest.Matchers.is("Capital City Library")),
                        org.hamcrest.Matchers.hasProperty("name", org.hamcrest.Matchers.is("Smalltown Historic District")),
                        org.hamcrest.Matchers.hasProperty("name", org.hamcrest.Matchers.is("The Great Landmark"))
                )));
    }

    @Test
    public void testSaveTouristAttractions() throws Exception {
        mockMvc.perform(post("/attractions/save")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("name", "Statue of Liberty")
                        .param("description", "Iconic statue in New York City."))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/attractions"));

        // Verify the new tourist attraction was added
        mockMvc.perform(get("/attractions"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("allTouristAttractions"))
                .andExpect(model().attribute("allTouristAttractions", org.hamcrest.Matchers.hasSize(6))); // 5 + 1 new attraction
    }

    @Test
    public void testDeleteTouristAttraction() throws Exception {
        mockMvc.perform(post("/attractions/{name}/delete", "Springfield Museum")) // Deleting an existing attraction
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/attractions"));

        // Verify the attraction is no longer in the database
        mockMvc.perform(get("/attractions"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("allTouristAttractions", org.hamcrest.Matchers.hasSize(4))); // Assuming 5 initial attractions, one deleted
    }
}