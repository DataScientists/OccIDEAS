package org.occideas.qsf;

import org.occideas.qsf.response.Element;

public class CommonQSFTestData {

    public static Element createElement(boolean active, String name, String id) {
        Element inactiveElement = new Element();
        inactiveElement.setActive(active);
        inactiveElement.setName(name);
        inactiveElement.setId(id);
        return inactiveElement;
    }

}
