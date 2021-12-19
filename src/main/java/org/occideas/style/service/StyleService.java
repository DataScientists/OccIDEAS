package org.occideas.style.service;

import org.occideas.entity.SystemProperty;
import org.occideas.systemproperty.SystemConfigTypes;
import org.occideas.systemproperty.dao.SystemPropertyDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class StyleService {

    private final SystemPropertyDao systemPropertyDao;

    @Autowired
    public StyleService(SystemPropertyDao systemPropertyDao) {
        this.systemPropertyDao = systemPropertyDao;
    }

    public Map<String, String> getStyles() {
        Map<String, String> results = new HashMap<>();
        List<SystemProperty> styles = systemPropertyDao.getByType(SystemConfigTypes.styles.name());
        styles.forEach(style -> results.put(style.getName(), style.getValue()));
        return results;
    }

}
