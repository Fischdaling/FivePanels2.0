package org.theShire.presentation.Vaadin.views.User;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import org.theShire.domain.medicalDoctor.User;
import org.theShire.presentation.Vaadin.MainLayout;
import org.theShire.service.UserService;

@Route(value = "user", layout = MainLayout.class)
public class UserView extends VerticalLayout {
    public UserView() {
        Button button = new Button("Create User");
        button.addClickListener(buttonClickEvent -> {
            getUI().ifPresent(ui -> ui.navigate(CreateUserView.class));
        });
        Grid<User> grid = new Grid<>(User.class);
        grid.setItems(UserService.findAllUser());

        // automatische Spalten entfernen
        grid.removeAllColumns();

        //Spalten
        grid.addColumn(user -> user.getProfile().getFirstName()+" "+user.getProfile().getLastName()).setHeader("Name");
        grid.addColumn(user -> user.getProfile().getEducationalTitles()).setHeader("Titles");
        grid.addColumn(User::getScore).setHeader("Score");
        grid.addColumn(user -> user.getProfile().getLocation()).setHeader("Location");
        grid.addColumn(user -> user.getProfile().getLanguage()).setHeader("Language");

        add(button, grid);
    }
}
