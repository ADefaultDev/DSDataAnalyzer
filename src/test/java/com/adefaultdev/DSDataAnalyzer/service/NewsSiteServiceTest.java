package com.adefaultdev.DSDataAnalyzer.service;

import com.adefaultdev.DSDataAnalyzer.dto.NewsSiteDTO;
import com.adefaultdev.DSDataAnalyzer.entity.NewsSite;
import com.adefaultdev.DSDataAnalyzer.repository.NewsSiteRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Unit tests for  {@link NewsSiteService}.
 *
 * @since 1.2.0
 * @author ADefaultDev
 */
@ExtendWith(MockitoExtension.class)
class NewsSiteServiceTest {

    @Mock
    private NewsSiteRepository newsSiteRepository;

    @InjectMocks
    private NewsSiteService newsSiteService;

    @Test
    void createOrUpdateSite_shouldUpdateExistingSite() {
        NewsSite existing = new NewsSite();
        existing.setId(1L);
        existing.setAddress("example.com");
        existing.setName("Old");
        existing.setTrustIndex(0.1);

        NewsSiteDTO dto = new NewsSiteDTO(1L, "example.com", "NewName", 0.9);

        when(newsSiteRepository.findByAddress("example.com")).thenReturn(Optional.of(existing));
        when(newsSiteRepository.save(existing)).thenReturn(existing);

        NewsSiteDTO result = newsSiteService.createOrUpdateSite(dto);

        assertThat(result.name()).isEqualTo("NewName");
        assertThat(result.trustIndex()).isEqualTo(0.9);
        verify(newsSiteRepository).save(existing);
    }

    @Test
    void createOrUpdateSite_shouldCreateNewSiteIfNotExists() {
        NewsSiteDTO dto = new NewsSiteDTO(null, "newsite.com", "NewSite", 0.5);
        NewsSite saved = new NewsSite();
        saved.setId(2L);
        saved.setAddress("newsite.com");
        saved.setName("NewSite");
        saved.setTrustIndex(0.5);

        when(newsSiteRepository.findByAddress("newsite.com")).thenReturn(Optional.empty());
        when(newsSiteRepository.save(any(NewsSite.class))).thenReturn(saved);

        NewsSiteDTO result = newsSiteService.createOrUpdateSite(dto);

        assertThat(result.id()).isEqualTo(2L);
        assertThat(result.name()).isEqualTo("NewSite");
        verify(newsSiteRepository).save(any(NewsSite.class));
    }

    @Test
    void getAllSites_shouldReturnListOfDTOs() {
        NewsSite site = new NewsSite();
        site.setId(1L);
        site.setAddress("site.com");
        site.setName("Site");
        site.setTrustIndex(0.3);

        when(newsSiteRepository.findAll()).thenReturn(List.of(site));

        List<NewsSiteDTO> result = newsSiteService.getAllSites();

        assertThat(result).hasSize(1);
        assertThat(result.getFirst().name()).isEqualTo("Site");
        verify(newsSiteRepository).findAll();
    }

    @Test
    void getSiteByAddress_shouldReturnDTOIfFound() {
        NewsSite site = new NewsSite();
        site.setId(1L);
        site.setAddress("site.com");
        site.setName("Site");
        site.setTrustIndex(0.3);

        when(newsSiteRepository.findByAddress("site.com")).thenReturn(Optional.of(site));

        NewsSiteDTO result = newsSiteService.getSiteByAddress("site.com");

        assertThat(result).isNotNull();
        assertThat(result.name()).isEqualTo("Site");
    }

    @Test
    void getSiteByAddress_shouldReturnNullIfNotFound() {
        when(newsSiteRepository.findByAddress("missing.com")).thenReturn(Optional.empty());

        NewsSiteDTO result = newsSiteService.getSiteByAddress("missing.com");

        assertThat(result).isNull();
    }

    @Test
    void updateTrustIndex_shouldUpdateIfSiteFound() {
        NewsSite site = new NewsSite();
        site.setId(1L);
        site.setTrustIndex(0.1);

        when(newsSiteRepository.findById(1L)).thenReturn(Optional.of(site));

        newsSiteService.updateTrustIndex(1L, 0.9);

        assertThat(site.getTrustIndex()).isEqualTo(0.9);
    }

    @Test
    void updateTrustIndex_shouldDoNothingIfNotFound() {
        when(newsSiteRepository.findById(99L)).thenReturn(Optional.empty());

        newsSiteService.updateTrustIndex(99L, 0.9);

        verify(newsSiteRepository).findById(99L);
    }

    @Test
    void getSiteByName_shouldReturnDTOIfFound() {
        NewsSite site = new NewsSite();
        site.setId(1L);
        site.setName("Site");

        when(newsSiteRepository.findByName("Site")).thenReturn(Optional.of(site));

        NewsSiteDTO result = newsSiteService.getSiteByName("Site");

        assertThat(result).isNotNull();
        assertThat(result.name()).isEqualTo("Site");
    }

    @Test
    void getSiteByName_shouldReturnNullIfNotFound() {
        when(newsSiteRepository.findByName("Unknown")).thenReturn(Optional.empty());

        NewsSiteDTO result = newsSiteService.getSiteByName("Unknown");

        assertThat(result).isNull();
    }
}
