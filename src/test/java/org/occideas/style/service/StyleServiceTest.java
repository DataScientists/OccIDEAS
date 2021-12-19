package org.occideas.style.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.occideas.entity.SystemProperty;
import org.occideas.systemproperty.SystemConfigTypes;
import org.occideas.systemproperty.dao.SystemPropertyDao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StyleServiceTest {

    @Mock
    SystemPropertyDao systemPropertyDao;

    @InjectMocks
    StyleService styleService;

    @Test
    void givenExistingStyles_whenGetStyles_shouldReturnStyles() {
        List<SystemProperty> systemPropertyList = new ArrayList<>();
        SystemProperty colorRed = new SystemProperty();
        colorRed.setType(SystemConfigTypes.styles.name());
        colorRed.setName("color");
        colorRed.setValue("red");
        SystemProperty backgroundColorBlue = new SystemProperty();
        backgroundColorBlue.setType(SystemConfigTypes.styles.name());
        backgroundColorBlue.setName("background-color");
        backgroundColorBlue.setValue("blue");
        systemPropertyList.add(colorRed);
        systemPropertyList.add(backgroundColorBlue);
        when(systemPropertyDao.getByType(SystemConfigTypes.styles.name())).thenReturn(systemPropertyList);

        Map<String, String> actual = styleService.getStyles();

        assertNotNull(actual);
        assertEquals(2, actual.size());
        assertTrue(actual.containsKey("color"));
        assertEquals("red", actual.get("color"));
        assertTrue(actual.containsKey("background-color"));
        assertEquals("blue", actual.get("background-color"));
    }

    @Test
    void givenNonExistingStyles_whenGetStyles_shouldReturnEmptyStyles() {
        List<SystemProperty> systemPropertyList = new ArrayList<>();
        when(systemPropertyDao.getByType(SystemConfigTypes.styles.name())).thenReturn(systemPropertyList);

        Map<String, String> actual = styleService.getStyles();

        assertNotNull(actual);
        assertTrue(actual.isEmpty());
    }

}