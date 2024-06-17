package org.theShire.presentation.Vaadin;

import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.theme.Theme;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.theShire.service.CaseService;
import org.theShire.service.UserService;

import static org.theShire.service.UniversalService.initData;

/**
 * The entry point of the Spring Boot application.
 *
 * Use the @PWA annotation make the application installable on phones, tablets
 * and some desktop browsers.
 *
 */
@SpringBootApplication
@Theme(value = "FivePanels")
public class Application implements AppShellConfigurator {

    public static void main(String[] args) {
        initData();
        SpringApplication.run(Application.class, args);
    }

}
