package com.sovesky.sarscov2tracker.controllers;

import com.sovesky.sarscov2tracker.models.LocationStats;
import com.sovesky.sarscov2tracker.services.CoronaVirusDataService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class HomeController {

    private CoronaVirusDataService service;

    public HomeController(CoronaVirusDataService cvds){
        this.service = cvds;
    }

    @GetMapping("/")
    public String home(Model m){
        List<LocationStats> listOfLocationStats = service.getAllStats();
        int totalReportedCases = listOfLocationStats.stream().mapToInt(LocationStats::getLatestTotalCases).sum();
        int totalNewCases = listOfLocationStats.stream().mapToInt(LocationStats::getDiffFromPrevDay).sum();
        m.addAttribute("locationStats", listOfLocationStats);
        m.addAttribute("totalReportedCases", totalReportedCases);
        m.addAttribute("totalNewCases", totalNewCases);
        return "home";
    }
}
