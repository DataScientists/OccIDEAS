package org.occideas.anzscocoder.service;

import org.occideas.vo.AnzscoLookupResultVO;

public interface AnzscoCoderService {

    AnzscoLookupResultVO lookup(String jobTitle, String jobDescription);
}
