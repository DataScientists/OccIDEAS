package org.occideas.anzscocoder.service;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.occideas.anzscocoder.client.AbsCoderClient;
import org.occideas.anzscocoder.client.AbsCoderCodeResponse;
import org.occideas.anzscocoder.client.AbsCoderResultItem;
import org.occideas.config.AbsCoderConfig;
import org.occideas.module.service.ModuleService;
import org.occideas.vo.AnzscoLookupResultVO;
import org.occideas.vo.AnzscoSuggestionVO;
import org.occideas.vo.ModuleVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AnzscoCoderServiceImpl implements AnzscoCoderService {

    private static final Logger log = LogManager.getLogger(AnzscoCoderServiceImpl.class);

    private static final int NUMBER_OF_SUGGESTIONS = 5;

    // Maps a 6-digit ANZSCO code to an OccIDEAS job module code, e.g. "331212" -> "CONS".
    // Sourced from src/main/resources/anzsco-module-mapping.csv.
    private final Map<String, String> anzscoToModuleCode = new HashMap<>();

    @Autowired
    private AbsCoderClient absCoderClient;

    @Autowired
    private AbsCoderConfig config;

    @Autowired
    private ModuleService moduleService;

    @PostConstruct
    public void loadAnzscoModuleMapping() {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(
            new ClassPathResource("anzsco-module-mapping.csv").getInputStream(), StandardCharsets.UTF_8))) {
            String line = reader.readLine(); // header
            while ((line = reader.readLine()) != null) {
                if (StringUtils.isBlank(line)) {
                    continue;
                }
                int comma = line.indexOf(',');
                if (comma < 0) {
                    continue;
                }
                String anzscoCode = line.substring(0, comma).trim();
                String mapping = line.substring(comma + 1).trim().replaceAll("^\"|\"$", "");
                int underscore = mapping.indexOf('_');
                String moduleCode = underscore > 0 ? mapping.substring(0, underscore) : mapping;
                anzscoToModuleCode.put(anzscoCode, moduleCode);
            }
            log.info("Loaded {} ANZSCO to OccIDEAS job module mappings", anzscoToModuleCode.size());
        } catch (IOException e) {
            log.error("Failed to load anzsco-module-mapping.csv", e);
        }
    }

    @Override
    public AnzscoLookupResultVO lookup(String jobTitle, String jobDescription) {
        if (StringUtils.isBlank(jobTitle)) {
            throw new IllegalArgumentException("jobTitle is required");
        }
        if (!config.isConfigured()) {
            throw new IllegalStateException("ABS Coder service is not configured");
        }

        AbsCoderCodeResponse response = absCoderClient.code(jobTitle, jobDescription, NUMBER_OF_SUGGESTIONS);

        List<AnzscoSuggestionVO> suggestions = new ArrayList<>();
        if (response != null && response.getResult() != null) {
            for (AbsCoderResultItem item : response.getResult()) {
                AnzscoSuggestionVO suggestion = new AnzscoSuggestionVO(item.getCodeCategory(), item.getCodeLabel(),
                    item.getCodeConfidence());
                resolveModule(suggestion);
                suggestions.add(suggestion);
            }
            Collections.sort(suggestions, (a, b) -> Double.compare(b.getConfidence(), a.getConfidence()));
        }

        return new AnzscoLookupResultVO(jobTitle, jobDescription, suggestions);
    }

    private void resolveModule(AnzscoSuggestionVO suggestion) {
        String moduleCode = anzscoToModuleCode.get(suggestion.getCode());
        if (StringUtils.isBlank(moduleCode)) {
            log.warn("No OccIDEAS job module mapping for ANZSCO code {}", suggestion.getCode());
            return;
        }
        suggestion.setModuleCode(moduleCode);
        try {
            ModuleVO module = moduleService.getModuleByNameLength(moduleCode, moduleCode.length());
            if (module != null) {
                suggestion.setModuleId(module.getIdNode());
                suggestion.setModuleName(module.getName());
            } else {
                log.warn("No OccIDEAS job module found in database for code {} (ANZSCO {})", moduleCode,
                    suggestion.getCode());
            }
        } catch (Exception e) {
            log.error("Failed to resolve OccIDEAS job module for code {} (ANZSCO {})", moduleCode,
                suggestion.getCode(), e);
        }
    }
}
