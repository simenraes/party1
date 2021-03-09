package be.thomamore.party.controllers;

import be.thomamore.party.model.Venue;
import be.thomamore.party.repositories.VenueRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.Optional;

@Controller
public class HomeController {
    @Autowired
    private VenueRepository venueRepository;


    private final String[] venueNames = {"De Loods", "De Club", "De Hangar", "Zapoi", "Kuub", "Cuba Libre"};
    private final Venue v1 = new Venue("v1", "link1", 6, true, false, true, false, "brussel", 40);
    private final Venue v2 = new Venue("v2", "link2", 6, true, false, true, false, "brussel", 40);
    private final Venue v3 = new Venue("v3", "link3", 6, true, false, true, false, "brussel", 40);
    private final Venue v4 = new Venue("v4", "link4", 6, true, false, true, false, "brussel", 40);

    private final Venue[] venues ={v1,v2,v3,v4};
    private final LocalDate datum = LocalDate.now(); // controller moet eig stateless zijn dusmoet ditniet final zijn ?
    private final Calendar c1 = Calendar.getInstance();




    @GetMapping({"/", "/home"})
    public String home(Model model) {

        return "home";
    }

    @GetMapping("/about")
    public String about(Model model) {

        return "about";
    }

    @GetMapping("/pay")

    public String pay(Model model,
                      @PathVariable(required = false) boolean weekend) {

        model.addAttribute("LocalDate", datum);


        return "pay";
    }


    /*@GetMapping({"/venuedetailsbyid", "/venuedetailsbyid/{id}"})
    public String venueDetailsById(Model model,
                                   @PathVariable(required = false)Integer id){
            model.addAttribute("venue", venueRepository.findById(id).get());
            return "venuedetails";
    }*/
    @GetMapping({"/venuedetails", "/venuedetails/{id}"})
    public String venueDetailsById(Model model,
                                   @PathVariable(required = false) Integer id) {
        if (id == null) return "venuedetails";

        Optional<Venue> optionalVenue = venueRepository.findById(id);
        if (optionalVenue.isPresent()) {
            long nrOfVenues = venueRepository.count();
            model.addAttribute("venue", optionalVenue.get());
            model.addAttribute("prevId", id > 1 ? id - 1 : nrOfVenues);
            model.addAttribute("nextId", id < nrOfVenues ? id + 1 : 1);

        }
        return "venuedetails";
    }
// WERKT MAAR GEEFT GEEN ERROR WANNEER INDEX OVERSCHREDEN WORDT ZIE BUNDEL 2 P 14








    @GetMapping("/venuelist")
    public String venueList(Model model) {
        Iterable<Venue> venues = venueRepository.findAll();
        model.addAttribute("venues", venues);
        return "venuelist";
    }
    //    @GetMapping("/venuelist")
//    public String venueList(Model model) {
//        model.addAttribute("venueNames", venueNames);
//        return "venuelist";
//    }
    @GetMapping("/venuelist/outdoor/{filter}")
    public String venueListOutdoor(Model model,
                                   @PathVariable String filter) {
        Iterable<Venue> venues = venueRepository.findByOutdoor(filter.equals("yes"));
        model.addAttribute("venues", venues);
        model.addAttribute("filterOutdoor", filter.equals("yes") ? "yes" : "no");
        return "venuelist";
    }
    @GetMapping("/venuelist/indoor/{filter}")
    public String venueListIndoor(Model model,
                                  @PathVariable String filter) {
        Iterable<Venue> venues = venueRepository.findByIndoor(filter.equals("yes"));
        model.addAttribute("venues", venues);
        model.addAttribute("filterIndoor", filter.equals("yes") ? "yes" : "no");
        return "venuelist";
    }

    @GetMapping("/venuelist/size/{filter}")
    public String venueListSize(Model model,
                                @PathVariable String filter) {
        Iterable<Venue> venues;
        switch (filter) {
            case "S":
                venues = venueRepository.findByCapacityBetween(0, 200);
                break;
            case "M":
                venues = venueRepository.findByCapacityBetween(200, 600);
                break;
            case "L":
                venues = venueRepository.findByCapacityGreaterThan(600);
                break;
            default:
                venues = venueRepository.findAll();
                filter = null;
                break;
        }
        model.addAttribute("venues", venues);
        model.addAttribute("filterSize", filter);
        return "venuelist";
    }



}
