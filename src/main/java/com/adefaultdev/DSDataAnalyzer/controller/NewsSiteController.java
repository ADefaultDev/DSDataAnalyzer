package com.adefaultdev.DSDataAnalyzer.controller;

import com.adefaultdev.DSDataAnalyzer.dto.NewsSiteDTO;
import com.adefaultdev.DSDataAnalyzer.service.NewsSiteService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/sites")
@RequiredArgsConstructor
public class NewsSiteController {

    private final NewsSiteService newsSiteService;

    @PostMapping
    public NewsSiteDTO createOrUpdateSite(@RequestBody NewsSiteDTO dto) {
        return newsSiteService.createOrUpdateSite(dto);
    }

    @GetMapping
    public List<NewsSiteDTO> getAllSites() {
        return newsSiteService.getAllSites();
    }

    @GetMapping("/by-address")
    public NewsSiteDTO getSiteByAddress(@RequestParam String address) {
        return newsSiteService.getSiteByAddress(address);
    }

    @PutMapping("/{id}/trust-index")
    public void updateTrustIndex(@PathVariable Long id, @RequestParam double index) {
        newsSiteService.updateTrustIndex(id, index);
    }
}
