package com.example.turistguidedel2.Model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TouristAttractionTest {

    @Test
    public void testEmptyConstructor() {
        // Test the empty constructor
        TouristAttraction attraction = new TouristAttraction();

        assertNull(attraction.getName());
        assertNull(attraction.getDescription());
        assertNull(attraction.getCity());
        assertNull(attraction.getTags());
        assertEquals(0, attraction.getPostcode());  // default value for int is 0
    }

    @Test
    public void testParameterizedConstructor() {
        // Test the constructor with parameters
        TouristAttraction attraction = new TouristAttraction("Eiffel Tower", "Famous Paris monument", "Paris", 75007, "landmark");

        assertEquals("Eiffel Tower", attraction.getName());
        assertEquals("Famous Paris monument", attraction.getDescription());
        assertEquals("Paris", attraction.getCity());
        assertEquals(75007, attraction.getPostcode());
        assertEquals("landmark", attraction.getTags());
    }

    @Test
    public void testSettersAndGetters() {
        // Test the setters and getters
        TouristAttraction attraction = new TouristAttraction();

        attraction.setName("Great Wall of China");
        assertEquals("Great Wall of China", attraction.getName());

        attraction.setDescription("Ancient wall in China");
        assertEquals("Ancient wall in China", attraction.getDescription());

        attraction.setCity("Beijing");
        assertEquals("Beijing", attraction.getCity());

        attraction.setPostcode(100000);
        assertEquals(100000, attraction.getPostcode());

        attraction.setTags("historic, UNESCO");
        assertEquals("historic, UNESCO", attraction.getTags());
    }

    @Test
    public void testGetPostcodeDefaultValue() {
        // Test default value of postcode (which should be 0)
        TouristAttraction attraction = new TouristAttraction();
        assertEquals(0, attraction.getPostcode());
    }
}