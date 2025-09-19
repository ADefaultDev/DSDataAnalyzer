package com.adefaultdev.DSDataAnalyzer.controller;

import com.adefaultdev.DSDataAnalyzer.dto.NewsSiteDTO;
import com.adefaultdev.DSDataAnalyzer.service.NewsSiteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


/**
 * Unit tests for {@link NewsSiteController} using JUnit 5 and Mockito.
 *
 * @since 1.2.0
 * @author ADefaultDev
 */
class NewsSiteControllerTest {

    private NewsSiteService newsSiteService;
    private NewsSiteController newsSiteController;

    @BeforeEach
    void setUp() {
        newsSiteService = mock(NewsSiteService.class);
        newsSiteController = new NewsSiteController(newsSiteService);
    }

    /**
     * Tests {@link NewsSiteController#createOrUpdateSite(NewsSiteDTO)}
     * ensuring that the service is called with the given DTO
     * and that the returned value is correctly propagated.
     */
    @Test
    void testCreateOrUpdateSite() {
        NewsSiteDTO dto = new NewsSiteDTO(1L,
                "TestAddress",
                "TestName",
                0d);

        when(newsSiteService.createOrUpdateSite(dto)).thenReturn(dto);

        NewsSiteDTO result = newsSiteController.createOrUpdateSite(dto);

        assertNotNull(result);
        assertEquals("TestName", result.name());
        verify(newsSiteService, times(1)).createOrUpdateSite(dto);
    }

    /**
     * Tests {@link NewsSiteController#getAllSites()}
     * ensuring that the controller returns the same list
     * as provided by the service.
     */
    @Test
    void testGetAllSites() {
        List<NewsSiteDTO> sites = Arrays.asList(
                new NewsSiteDTO(1L,
                        "TestAddress1",
                        "TestName1",
                        0d),
                new NewsSiteDTO(2L,
                        "TestAddress2",
                        "TestName2",
                        0d)
        );

        when(newsSiteService.getAllSites()).thenReturn(sites);

        List<NewsSiteDTO> result = newsSiteController.getAllSites();

        assertEquals(2, result.size());
        assertEquals("Site1", result.getFirst().name());
        verify(newsSiteService, times(1)).getAllSites();
    }

    /**
     * Tests {@link NewsSiteController#getSiteByName(String)}
     * ensuring that the correct site is returned
     * when requested by name.
     */
    @Test
    void testGetSiteByName() {
        NewsSiteDTO dto = new NewsSiteDTO(1L,
                "TestAddress1",
                "TestName1",
                0d);

        when(newsSiteService.getSiteByName("Example")).thenReturn(dto);

        NewsSiteDTO result = newsSiteController.getSiteByName("Example");

        assertNotNull(result);
        assertEquals("Example", result.name());
        verify(newsSiteService, times(1)).getSiteByName("Example");
    }

    /**
     * Tests {@link NewsSiteController#updateTrustIndex(Long, double)}
     * ensuring that the service is called with correct arguments.
     */
    @Test
    void testUpdateTrustIndex() {
        Long id = 10L;
        double index = 0.85;

        newsSiteController.updateTrustIndex(id, index);

        ArgumentCaptor<Long> idCaptor = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<Double> indexCaptor = ArgumentCaptor.forClass(Double.class);

        verify(newsSiteService, times(1))
                .updateTrustIndex(idCaptor.capture(), indexCaptor.capture());

        assertEquals(id, idCaptor.getValue());
        assertEquals(index, indexCaptor.getValue());
    }
}
