package org.occideas.ipsos.service;

import java.io.IOException;

public interface IIPSOSService {

    void importResponse() throws IOException;

    void generateIPSOSJobModuleDataFile() throws IOException;
}
