package org.occideas.module.dao;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.occideas.entity.JobModule;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.annotation.Resource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
public class ModuleDaoTest {

    @Resource
    private IModuleDao moduleDao;

    @Test
    public void givenModule_whenSave_shouldReturnSuccess(){
        JobModule module = new JobModule();
        module.setName("Sample Module");
        module.setNodeclass("M");
        module.setNodeDiscriminator("M");
        JobModule savedJobModule = moduleDao.save(module);
        assertNotNull(savedJobModule.getIdNode());
        assertEquals(module.getName(),savedJobModule.getName());
    }
}