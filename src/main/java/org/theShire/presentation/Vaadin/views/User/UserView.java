package org.theShire.presentation.Vaadin.views.User;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import org.theShire.domain.medicalDoctor.User;
import org.theShire.domain.messenger.Chat;
import org.theShire.presentation.Vaadin.MainLayout;
import org.theShire.service.UserService;

import java.util.stream.Collectors;

@Route(value = "user", layout = MainLayout.class)
public class UserView extends VerticalLayout {
    private  Grid<User> grid;
    private Dialog userDetailDialog;
    public UserView() {

        userDetailDialog = new Dialog();
        initDialog();
        initGrid();

        add(grid);
        Button button = new Button("Create User");

        button.addClickListener(buttonClickEvent -> {
            getUI().ifPresent(ui -> ui.navigate(CreateUserView.class));
        });
        add(button);

    }

    private void initGrid() {
        grid = new Grid<>(User.class);
        grid.setItems(UserService.findAllUser());

        // Remove automatic columns and add custom columns
        grid.removeAllColumns();
        grid.addColumn(user->user.getProfile().getFirstName()).setHeader("First Name");
        grid.addColumn(user->user.getProfile().getLastName()).setHeader("Last Name");
        grid.addColumn(User::getEmail).setHeader("Email");
        grid.addColumn(user->user.getProfile().getLanguage()).setHeader("Language");
        grid.addColumn(user->user.getProfile().getLocation()).setHeader("Location");

        // Add selection listener
        grid.asSingleSelect().addValueChangeListener(event -> {
            User selectedUser = event.getValue();
            if (selectedUser != null) {
                showUserDetailDialog(selectedUser);
            }
        });
    }

    private void initDialog() {
        userDetailDialog = new Dialog();
        userDetailDialog.setWidth("75%");
        userDetailDialog.setHeight("50%");
    }

    private void showUserDetailDialog(User user) {
        userDetailDialog.removeAll();

        // Create form fields
        TextField firstName = new TextField("First Name");
        firstName.setValue(user.getProfile().getFirstName().toString());
        TextField lastName = new TextField("Last Name");
        lastName.setValue(user.getProfile().getLastName().toString());
        TextField email = new TextField("Email");
        email.setValue(user.getEmail().toString());
        TextField language = new TextField("Language");
        language.setValue(user.getProfile().getLanguage().toString());
        TextField location = new TextField("Location");
        location.setValue(user.getProfile().getLocation().toString());
        IntegerField score = new IntegerField("Score");
        score.setValue(user.getScore());
        TextField contact = new TextField("Contact");
        contact.setWidthFull();
        contact.setValue(user.getChats().stream().map(chat -> chat.getPeople().stream().map(User::getEmail).toList()).toList().toString());

//        Image profilePic = user.getProfile().getProfilePicture().getImage();
        Avatar avatar = new Avatar(user.getProfile().getFirstName().value());
//        avatar.setImage(user.getProfile().getProfilePicture().getImage().getSrc());
        // Make fields read-only
        firstName.setReadOnly(true);
        lastName.setReadOnly(true);
        email.setReadOnly(true);
        language.setReadOnly(true);
        location.setReadOnly(true);
        score.setReadOnly(true);
        contact.setReadOnly(true);

        // Create the form layout
        FormLayout formLayout = new FormLayout();
        formLayout.add(avatar, firstName, lastName, email, language, location, score, contact);

        // Create a close button
        Button closeButton = new Button("Close", event -> userDetailDialog.close());
        this.setAlignSelf(Alignment.AUTO);
        // Add components to the dialog
        userDetailDialog.add(formLayout, closeButton);
        userDetailDialog.open();
    }
}
