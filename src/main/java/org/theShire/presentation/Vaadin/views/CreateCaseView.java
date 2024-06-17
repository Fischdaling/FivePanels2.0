package org.theShire.presentation.Vaadin.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.theShire.domain.media.Content;
import org.theShire.domain.medicalCase.Case;
import org.theShire.domain.medicalDoctor.User;
import org.theShire.foundation.Knowledges;
import org.theShire.presentation.Vaadin.MainLayout;
import org.theShire.service.CaseService;
import org.theShire.service.UserService;

import java.util.ArrayList;
import java.util.HashSet;

@Route(value = "create-case", layout = MainLayout.class)
@PageTitle("Create Case")
public class CreateCaseView extends VerticalLayout {

    public CreateCaseView() {
        // Create form fields
        TextField titleField = new TextField("Title");

        // Create the form layout
        FormLayout formLayout = new FormLayout();
        formLayout.add(titleField);

        // Create a save button
        Button saveButton = new Button("Save", event -> {
            // Get the current logged-in user
            User owner = UserService.userLoggedIn;

            if (owner == null) {
                Notification.show("No user logged in", 3000, Notification.Position.MIDDLE);
                return;
            }

            // Create a new Case object
            Case newCase = new Case(owner, titleField.getValue(), new HashSet<>(), new ArrayList<>(), null);

            // Save the case using CaseService
            CaseService.createCase(null, owner, newCase.getTitle(), new HashSet<>(), new ArrayList<>(), null);

            // Show a notification
            Notification.show("Case created successfully");

            // Clear the form fields
            titleField.clear();
        });

        // Add components to the layout
        add(formLayout, saveButton);
    }
}
