package com.example.turistguidedel2.Controller;
import com.example.turistguidedel2.Service.TouristService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.example.turistguidedel2.Model.TouristAttraction;

import java.util.List;


@Controller
public class TouristController {
    // This class is a controller class
    private final TouristService touristService;

    public TouristController(TouristService touristService) {
        this.touristService = touristService;
    }
    //homepage. Displays all attractions and has links to other endpoints.
    @GetMapping("/attractions")
    public String getAllTouristAttractions(Model model){
        List<TouristAttraction> attractions = touristService.getAllTouristAttractions();
        model.addAttribute("allTouristAttractions", attractions);
        return "attractionList";
    }

    //not used.
    @GetMapping("/attractions/{name}")
    public String getTouristAttractionByName(@PathVariable String name, Model model){
        TouristAttraction attractions = touristService.getTouristAttractionByName(name);
        return "name";
    }

    @PostMapping("/attractions/save")
    public String saveTouristAttractions(@ModelAttribute TouristAttraction touristAttraction) {
        touristService.saveTouristAttractions(touristAttraction);
        return "redirect:/attractions";
    }

    @GetMapping("/attractions/add")
    public String addTouristAttraction(Model model){
        model.addAttribute("touristAttraction", new TouristAttraction());
        List <String> availableTags  = touristService.getAllTags();
        model.addAttribute("availableTags", availableTags);
        return "add";
    }
    @PostMapping("/attractions/update")
    public String updateTouristAttraction(@ModelAttribute TouristAttraction touristAttraction){
        touristService.updateTouristAttraction(touristAttraction);
        return "redirect:/attractions";
    }
    @GetMapping("/attractions/{name}/edit")
    public String editTouristAttraction(@PathVariable String name, Model model){
        TouristAttraction attraction = touristService.getTouristAttractionByName(name);
        List <String> availableTags  = touristService.getAllTags();
        model.addAttribute("touristAttraction", attraction);
        model.addAttribute("availableTags", availableTags);

        return "edit";
    }

   @PostMapping("/attractions/{name}/delete")
public String deleteTouristAttraction(@PathVariable String name) {
    touristService.deleteTouristAttraction(name);
    return "redirect:/attractions";
}

}

