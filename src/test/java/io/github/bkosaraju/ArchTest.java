package io.github.bkosaraju;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import org.junit.jupiter.api.Test;

class ArchTest {

    @Test
    void servicesAndRepositoriesShouldNotDependOnWebLayer() {
        JavaClasses importedClasses = new ClassFileImporter()
            .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
            .importPackages("io.github.bkosaraju");

        noClasses()
            .that()
            .resideInAnyPackage("io.github.bkosaraju.service..")
            .or()
            .resideInAnyPackage("io.github.bkosaraju.repository..")
            .should()
            .dependOnClassesThat()
            .resideInAnyPackage("..io.github.bkosaraju.web..")
            .because("Services and repositories should not depend on web layer")
            .check(importedClasses);
    }
}
