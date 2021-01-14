package org.occideas.voxco.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.occideas.module.service.ModuleService;
import org.occideas.vo.ModuleVO;
import org.occideas.voxco.model.Survey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VoxcoService {
    private static final Logger log = LogManager.getLogger(VoxcoService.class);

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private ModuleService moduleService;
    @Autowired
    private VoxcoSurveyService surveyService;

    public void loadSurvey(Long id) throws Exception {
        List<ModuleVO> modules = moduleService.findById(id);
        if (!modules.isEmpty()) {
            ModuleVO module = modules.get(0);
            Survey survey = new Survey(module.getIdNode(), module.getName(), module.getDescription());
            ResponseEntity response = surveyService.create(survey);
            log.info("id={}, response={}, body={}", id, response.getStatusCode(), objectMapper.writeValueAsString(response.getBody()));
        }
    }
}
