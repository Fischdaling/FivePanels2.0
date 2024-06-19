package org.theShire.presentation.Vaadin.views.User;


import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MultiFileMemoryBuffer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import org.theShire.domain.exception.MedicalDoctorException;
import org.theShire.domain.media.Media;
import org.theShire.domain.richType.*;
import org.theShire.domain.richType.Knowledges;
import org.theShire.presentation.Vaadin.MainLayout;
import org.theShire.service.UserService;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Arrays;
import java.util.stream.Collectors;

@Route(value = "create-user", layout = MainLayout.class)
@PageTitle("Create user")
public class CreateUserView extends VerticalLayout {

    public CreateUserView() {
        // Create form fields
        TextField firstName = new TextField("First Name");
        TextField lastName = new TextField("Last Name");
        TextField email = new TextField("Email");
        TextField password = new TextField("Password");
        TextField language = new TextField("Language");
        TextField location = new TextField("Location");MultiFileMemoryBuffer buffer = new MultiFileMemoryBuffer();
        Upload upload = new Upload(buffer);
        Image uploadedImage = new Image();
        // Handle file upload
        upload.addSucceededListener(event -> {
            String fileName = event.getFileName();
            InputStream inputStream = buffer.getInputStream(fileName);
            Media profilePicture = new Media(inputStream, fileName);
            StreamResource resource = new StreamResource(fileName, () -> new ByteArrayInputStream(profilePicture.getImageData()));
            uploadedImage.setSrc(resource);
        });
        MultiSelectComboBox<String> specialization = new MultiSelectComboBox<>("Specialization");
        specialization.setItems(Knowledges.getLegalKnowledges());
        TextField educationalTitle = new TextField("Educational Title");

        // Create the form layout
        FormLayout formLayout = new FormLayout();
        formLayout.add(firstName, lastName, email, password, language, location, upload, specialization, educationalTitle);

        HorizontalLayout horizontalLayout = new HorizontalLayout();
        // Create a save button
        Button saveButton = new Button("Save", event -> {
            try {
                // Create a new Case object
                UserService.createUser(null, new Name(firstName.getValue()),
                        new Name(lastName.getValue()),
                        new Email(email.getValue()),
                        new Password(password.getValue()),
                        new Language(language.getValue()),
                        new Location(location.getValue()),
                        uploadedImage.toString(),
                        specialization.getValue().stream().
                                map(Knowledges::new).
                                collect(Collectors.toSet()),
                        Arrays.stream(educationalTitle.getValue().split(" ")).
                                map(EducationalTitle::new).
                                collect(Collectors.toList()));


                // Show a notification
                Notification.show("User created successfully");


                // Change back to Cases
                getUI().ifPresent(ui -> ui.navigate(UserView.class));
            }catch (MedicalDoctorException e){
                Notification.show("Error: " + e.getMessage());
            }
        });

        Button cancelButton = new Button("Cancel",event -> {
            getUI().ifPresent(ui -> ui.navigate(UserView.class));
        });
        horizontalLayout.add(saveButton, cancelButton);
        horizontalLayout.setAlignItems(Alignment.CENTER);
        // Add components to the layout
        add(formLayout, horizontalLayout);
    }
}

