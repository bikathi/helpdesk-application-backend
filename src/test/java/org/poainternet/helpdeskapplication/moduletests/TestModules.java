package org.poainternet.helpdeskapplication.moduletests;

import org.junit.jupiter.api.Test;
import org.poainternet.helpdeskapplication.HelpdeskApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.modulith.core.ApplicationModules;

@SpringBootTest
public class TestModules {
    @Test
    void createApplicationModuleModel() {
        ApplicationModules modules = ApplicationModules.of(HelpdeskApplication.class);
        modules.forEach(System.out::println);
    }
}
