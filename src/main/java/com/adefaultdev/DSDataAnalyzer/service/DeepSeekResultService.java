package com.adefaultdev.DSDataAnalyzer.service;

import com.adefaultdev.DSDataAnalyzer.dto.DeepSeekResponseDTO;
import com.adefaultdev.DSDataAnalyzer.entity.NewsSite;
import com.adefaultdev.DSDataAnalyzer.repository.NewsSiteRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.io.IOException;

/**
 * Service that processes answer from DeepSeek.
 * Reads and puts data from json-answer to database.
 *
 * @author ADefaultDev
 * @since 1.4.0
 */
@Service
@RequiredArgsConstructor
public class DeepSeekResultService {

    private final NewsSiteRepository newsSiteRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Updates or adds NewsSite using JSON-response.
     * @param jsonResponse JSON from DeepSeek
     */
    public void processResponse(String jsonResponse) throws IOException {
        DeepSeekResponseDTO dto = objectMapper.readValue(jsonResponse, DeepSeekResponseDTO.class);

        NewsSite site = newsSiteRepository.findByAddress(dto.getAddress())
                .orElseGet(NewsSite::new);

        site.setAddress(dto.getAddress());
        site.setName(dto.getName());
        site.setTrustIndex(dto.getTrustIndex());
        site.setProcessedCount(dto.getProcessedCount());


        newsSiteRepository.save(site);
    }
}
