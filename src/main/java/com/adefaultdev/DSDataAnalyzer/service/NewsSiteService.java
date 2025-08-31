package com.adefaultdev.DSDataAnalyzer.service;

import com.adefaultdev.DSDataAnalyzer.dto.NewsSiteDTO;
import com.adefaultdev.DSDataAnalyzer.entity.NewsSite;
import com.adefaultdev.DSDataAnalyzer.repository.NewsSiteRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service for managing news site operations.
 * Handles creation, retrieval, and updating of news site information.
 *
 * <p>Dependency injected via Lombok:</p>
 * <ul>
 *   <li>{@link NewsSiteRepository} - Handles news site data access</li>
 * </ul>
 *
 * @since 1.0.0
 * @author ADefaultDev
 */
@Service
@RequiredArgsConstructor
public class NewsSiteService {

    private final NewsSiteRepository newsSiteRepository;

    /**
     * Creates or updates a news site from DTO.
     *
     * @param dto NewsSiteDTO with site details
     * @return created/updated NewsSiteDTO
     */
    public NewsSiteDTO createOrUpdateSite(NewsSiteDTO dto) {
        NewsSite entity = convertToEntity(dto);
        NewsSite saved = newsSiteRepository.save(entity);
        return convertToDTO(saved);
    }

    /**
     * Retrieves all news sites.
     *
     * @return list of all NewsSiteDTOs
     */
    public List<NewsSiteDTO> getAllSites() {
        return newsSiteRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves news site by address.
     *
     * @param address site address/URL
     * @return NewsSiteDTO or null if not found
     */
    public NewsSiteDTO getSiteByAddress(String address) {
        return newsSiteRepository.findByAddress(address)
                .map(this::convertToDTO)
                .orElse(null);
    }

    /**
     * Updates trust index for a news site.
     *
     * @param siteId news site ID
     * @param newIndex new trust index value
     */
    @Transactional
    public void updateTrustIndex(Long siteId, double newIndex) {
        newsSiteRepository.findById(siteId).ifPresent(site -> site.setTrustIndex(newIndex));
    }

    /**
     * Converts NewsSite entity to DTO.
     *
     * @param site NewsSite entity
     * @return NewsSiteDTO
     */
    private NewsSiteDTO convertToDTO(NewsSite site) {
        return new NewsSiteDTO(
                site.getId(),
                site.getAddress(),
                site.getName(),
                site.getTrustIndex()
        );
    }

    /**
     * Converts NewsSiteDTO to entity.
     * Looks up existing site by address or creates new one.
     *
     * @param dto NewsSiteDTO
     * @return NewsSite entity
     */
    private NewsSite convertToEntity(NewsSiteDTO dto) {
        return newsSiteRepository.findByAddress(dto.address())
                .map(existingSite -> {
                    existingSite.setName(dto.name());
                    existingSite.setTrustIndex(dto.trustIndex());
                    return existingSite;
                })
                .orElseGet(() -> {
                    NewsSite newSite = new NewsSite();
                    newSite.setAddress(dto.address());
                    newSite.setName(dto.name());
                    newSite.setTrustIndex(dto.trustIndex());
                    return newSite;
                });
    }

    /**
     * Retrieves news site by name.
     *
     * @param name site name
     * @return NewsSiteDTO or null if not found
     */
    public NewsSiteDTO getSiteByName(String name) {
        return newsSiteRepository.findByName(name)
                .map(this::convertToDTO)
                .orElse(null);
    }
}
