package org.theShire.presentation.Vaadin.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.theShire.domain.richType.Email;
import org.theShire.domain.medicalDoctor.User;
import org.theShire.service.UserService;
import org.theShire.presentation.Vaadin.MainLayout;

@Route("")
@PageTitle("Login")
public class LoginView extends VerticalLayout {

    public LoginView() {
        TextField emailField = new TextField("Email");
        PasswordField passwordField = new PasswordField("Password");

        FormLayout formLayout = new FormLayout();
        formLayout.add(emailField, passwordField);

        Button loginButton = new Button("Login", event -> {
            String email = emailField.getValue();
            String password = passwordField.getValue();

            try {
                User user = UserService.login(new Email(email), password);
                UserService.userLoggedIn = user;
                Notification.show("Login successful");

                getUI().ifPresent(ui -> ui.navigate(CaseView.class));
            } catch (Exception e) {
                Notification.show("Invalid email or password");
            }
        });

        add(formLayout, loginButton);
    }
}
