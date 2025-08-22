package com.adefaultdev.DSDataAnalyzer.service;

import com.adefaultdev.DSDataAnalyzer.dto.NewsContentDTO;
import com.adefaultdev.DSDataAnalyzer.dto.NewsSiteDTO;
import com.adefaultdev.DSDataAnalyzer.model.NewsContent;
import com.adefaultdev.DSDataAnalyzer.model.NewsSite;
import com.adefaultdev.DSDataAnalyzer.repository.NewsSiteRepository;
import com.rometools.rome.feed.synd.SyndEntry;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NewsSiteService {

    private final NewsSiteRepository newsSiteRepository;

    public NewsSiteDTO createOrUpdateSite(NewsSiteDTO dto) {
        NewsSite entity = convertToEntity(dto);
        NewsSite saved = newsSiteRepository.save(entity);
        return convertToDTO(saved);
    }

    public List<NewsSiteDTO> getAllSites() {
        return newsSiteRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public NewsSiteDTO getSiteByAddress(String address) {
        NewsSite site = newsSiteRepository.findByAddress(address);
        return site != null ? convertToDTO(site) : null;
    }

    @Transactional
    public void updateTrustIndex(Long siteId, double newIndex) {
        newsSiteRepository.findById(siteId).ifPresent(site -> site.setTrustIndex(newIndex));
    }

    private NewsSiteDTO convertToDTO(NewsSite site) {
        return new NewsSiteDTO(
                site.getId(),
                site.getAddress(),
                site.getName(),
                site.getTrustIndex()
        );
    }

    private NewsSite convertToEntity(NewsSiteDTO dto) {
        NewsSite site = new NewsSite();
        site.setId(dto.id());
        site.setAddress(dto.address());
        site.setName(dto.name());
        site.setTrustIndex(dto.trustIndex());
        return site;
    }

    public void saveNews(List<SyndEntry> news) {
    }
}
