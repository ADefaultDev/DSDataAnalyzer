package com.adefaultdev.DSDataAnalyzer.service;

import com.adefaultdev.DSDataAnalyzer.dto.NewsContentDTO;
import com.adefaultdev.DSDataAnalyzer.dto.NewsSiteDTO;
import com.adefaultdev.DSDataAnalyzer.model.NewsContent;
import com.adefaultdev.DSDataAnalyzer.model.NewsSite;
import com.adefaultdev.DSDataAnalyzer.repository.NewsSiteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NewsSiteService {

    private final NewsSiteRepository newsSiteRepository;

    public NewsSite createOrUpdateSite(NewsSite site) {
        return newsSiteRepository.save(site);
    }

    public List<NewsSite> getAllSites() {
        return newsSiteRepository.findAll();
    }

    public NewsSite getSiteByAddress(String address) {
        return newsSiteRepository.findByAddress(address);
    }

    public void updateTrustIndex(Long siteId, double newIndex) {

        newsSiteRepository.findById(siteId).ifPresent(site -> {
            site.setTrustIndex(newIndex);
            newsSiteRepository.save(site);

        });
    }

    private NewsSiteDTO convertToDTO(NewsSite site) {

        NewsSiteDTO dto = new NewsSiteDTO();
        dto.setId(site.getId());
        dto.setAddress(site.getAddress());
        dto.setName(site.getName());
        dto.setTrustIndex(site.getTrustIndex());
        return dto;

    }

    private NewsContent convertToEntity(NewsContentDTO dto) {

        NewsContent content = new NewsContent();
        content.setTheme(dto.getTheme());
        content.setContent(dto.getContent());
        content.setSite(newsSiteRepository.findById(dto.getSiteId()).orElseThrow());
        return content;

    }

}