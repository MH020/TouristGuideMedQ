package com.example.turistguidedel2.Repository;

import com.example.turistguidedel2.Model.TouristAttraction;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TouristRepositoryTest {

    @Test
    void addTouristAttractionTest() {
        // Arrange
        TouristRepository repository = new TouristRepository();
        TouristAttraction attraction = new TouristAttraction("name", "description", "city", null);

        // Act
        repository.addTouristAttraction(attraction);

        // Assert
        assertTrue(repository.getAllTouristAttractions().contains(attraction));
    }

    @Test
    void getAllTouristAttractionsTest() {
        // Arrange
        TouristRepository repository = new TouristRepository();

        // Act
        List<TouristAttraction> attractions = repository.getAllTouristAttractions();

        // Assert

    }

    @Test
    void updateTouristAttractionTest() {
        // Arrange
        TouristRepository repository = new TouristRepository();
        String newDescription = "new description";

        // Act
        repository.updateTouristAttraction("name", newDescription);

        // Assert

    }

    @Test
    void deleteTouristAttractionTest() {
        // Arrange
        TouristRepository repository = new TouristRepository();
        //TouristAttraction attraction = new TouristAttraction("name", "description", "city", null);
        //repository.addTouristAttraction(attraction);

        // Act
        repository.deleteTouristAttraction("name");

        // Assert

    }

    @Test
    void populateAttractionsTest() {
        //
    }
}