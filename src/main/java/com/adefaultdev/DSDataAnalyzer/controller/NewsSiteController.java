package com.adefaultdev.DSDataAnalyzer.controller;

import com.adefaultdev.DSDataAnalyzer.dto.NewsSiteDTO;
import com.adefaultdev.DSDataAnalyzer.service.NewsSiteService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Controller providing news sites management.
 * Provides API to request creating, editing or getting news sites.
 *
 * <p>Dependencies injected via Lombok's @RequiredArgsConstructor:</p>
 * <ul>
 *   <li>{@link NewsSiteService} - Handles news site accessing and editing</li>
 * </ul>
 * @since 1.0.0
 * @author ADefaultDev
 */
@RestController
@RequestMapping("/api/sites")
@RequiredArgsConstructor
public class NewsSiteController {

    private final NewsSiteService newsSiteService;

    /**
     * Creates or updates the site
     * Available for admins only
     *
     * @return NewsSiteDTO created or edited news site
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public NewsSiteDTO createOrUpdateSite(@RequestBody NewsSiteDTO dto) {
        return newsSiteService.createOrUpdateSite(dto);
    }

    /**
     * Gets all news sites stored in database
     * Available for admins only
     *
     * @return List of all sites
     */
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<NewsSiteDTO> getAllSites() {
        return newsSiteService.getAllSites();
    }

    /**
     * Gets site by name
     * Available for admins only
     *
     * @return NewsSiteDTO site if it's found, null otherwise
     */
    @GetMapping("/by-address")
    @PreAuthorize("hasRole('ADMIN')")
    public NewsSiteDTO getSiteByName(@RequestParam String name) {
        return newsSiteService.getSiteByName(name);
    }

    /**
     * Updates trust index for targeted site
     * Available for admins only
     *
     * @param id targeted site's id
     * @param index new trust index
     */
    @PutMapping("/{id}/trust-index")
    @PreAuthorize("hasRole('ADMIN')")
    public void updateTrustIndex(@PathVariable Long id, @RequestParam double index) {
        newsSiteService.updateTrustIndex(id, index);
    }
}
