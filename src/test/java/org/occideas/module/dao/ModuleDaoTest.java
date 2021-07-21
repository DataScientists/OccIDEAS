package org.occideas.module.dao;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.occideas.HibernateConfigH2;
import org.occideas.entity.JobModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@Import(HibernateConfigH2.class)
@AutoConfigureTestDatabase(replace= AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource("/occideas.properties")
public class ModuleDaoTest {

    @Autowired
    private ModuleDao moduleDao;

    @AfterEach
    void cleanup(){
        moduleDao.deleteAll();
    }

    @Test
    void givenModule_whenSave_shouldReturnSuccess(){
        JobModule module = createJobModule("Sample Module",0);

        JobModule savedJobModule = moduleDao.save(module);

        assertNotNull(savedJobModule.getIdNode());
        assertEquals(module.getName(),savedJobModule.getName());
    }


    @Test
    void givenModule_whenDelete_shouldReturnSuccess() {
        JobModule module = createJobModule("Sample Module",0);
        JobModule saveModule = moduleDao.save(module);

        moduleDao.delete(saveModule);

        assertFalse(moduleDao.findById(saveModule.getIdNode()).isPresent());
    }

    @Test
    void givenModule_whenDeleteAll_shouldReturnSuccess() {
        JobModule module = createJobModule("Sample Module",0);
        JobModule saveModule = moduleDao.save(module);

        moduleDao.deleteAll();

        assertFalse(moduleDao.findById(saveModule.getIdNode()).isPresent());
    }


    @Test
    void givenModuleName_whenFindByName_shouldReturnSuccess() {
        JobModule module = createJobModule("Sample Module",0);
        JobModule saveModule = moduleDao.save(module);

        List<JobModule> actual = moduleDao.findByName(saveModule.getName());

        assertNotNull(actual);
        assertEquals(1, actual.size());
    }

    @Test
    void givenId_whenGet_shouldReturnSuccess() {
        JobModule module = createJobModule("Sample Module",0);
        JobModule saveModule = moduleDao.save(module);

        Optional<JobModule> actual = moduleDao.findById(saveModule.getIdNode());

        assertNotNull(actual);
        assertTrue(actual.isPresent());
    }

    @Test
    void givenModuleName_whenFindByNameLength_shouldReturnSuccess() {
        JobModule module = createJobModule("Sample Module",0);
        JobModule saveModule = moduleDao.save(module);

        List<JobModule> actual = moduleDao.findByNameStartsWith("Sample");

        assertNotNull(actual);
        assertEquals(1, actual.size());
        assertEquals(saveModule.getIdNode(), actual.get(0).getIdNode());
    }


    @Test
    void givenModuleAlreadySaved_whenGetAll_shouldReturnAllActive() {
        JobModule activeModule = moduleDao.save(createJobModule("Sample Module",0));
        moduleDao.save(createJobModule("Inactive Module",1));

        List<JobModule> actual = moduleDao.findByDeleted(0);

        assertNotNull(actual);
        assertEquals(1, actual.size());
        assertEquals(activeModule.getIdNode(), actual.get(0).getIdNode());
    }

    @Test
    void givenId_whenGetNodeNameById_shouldReturnName() {
        JobModule module = createJobModule("Sample Module",0);
        module.setTopNodeId(1L);
        module.setLink(0);
        JobModule saveModule = moduleDao.save(module);

        List<String> actual = moduleDao.findDistinctNameByIdNode(saveModule.getIdNode());

        assertNotNull(actual);
    }

    @Test
    void givenLinkAndModId_whenGetNodeByLinkAndModId_shouldReturnModule() {
        JobModule module = createJobModule("Sample Module",0);
        module.setTopNodeId(1L);
        module.setParentId("1");
        module.setLink(0);
        JobModule saveModule = moduleDao.save(module);

        List<JobModule> nodeByLinkAndModId = moduleDao.findByLinkAndIdNode(module.getLink(), saveModule.getIdNode());

        assertNotNull(nodeByLinkAndModId);
    }

    @Test
    void givenType_whenGetNodeByType_shouldReturnModule() {
        JobModule module = createJobModule("Sample Module",0);
        moduleDao.save(module);

        List<JobModule> nodeByType = moduleDao.findByType(module.getType());

        assertNotNull(nodeByType);
        assertEquals(module.getName(), nodeByType.get(0).getName());
    }

    private JobModule createJobModule(String name, int deleted) {
        JobModule module = new JobModule();
        module.setName(name);
        module.setNodeclass("M");
        module.setNodeDiscriminator("M");
        module.setType("M");
        module.setDeleted(deleted);
        return module;
    }
}