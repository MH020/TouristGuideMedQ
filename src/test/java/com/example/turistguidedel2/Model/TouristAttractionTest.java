//unittest for TouristAttraction
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
        TouristAttraction attraction = new TouristAttraction("Bakken", "Amusement park", "Klampenborg", 2930, "nature");

        assertEquals("Bakken", attraction.getName());
        assertEquals("Amusement park", attraction.getDescription());
        assertEquals("Klampenborg", attraction.getCity());
        assertEquals(2930, attraction.getPostcode());
        assertEquals("nature", attraction.getTags());
    }

    @Test
    public void testSettersAndGetters() {
        // Test the setters and getters
        TouristAttraction attraction = new TouristAttraction();

        attraction.setName("Bakken");
        assertEquals("Bakken", attraction.getName());

        attraction.setDescription("Amusement park");
        assertEquals("Amusement park", attraction.getDescription());

        attraction.setCity("Klampenborg");
        assertEquals("Klampenborg", attraction.getCity());

        attraction.setPostcode(2930);
        assertEquals(2930, attraction.getPostcode());

        attraction.setTags("nature, park");
        assertEquals("nature, park", attraction.getTags());
    }

    @Test
    public void testGetPostcodeDefaultValue() {
        // Test default value of postcode (which should be 0)
        TouristAttraction attraction = new TouristAttraction();
        assertEquals(0, attraction.getPostcode());
    }
}