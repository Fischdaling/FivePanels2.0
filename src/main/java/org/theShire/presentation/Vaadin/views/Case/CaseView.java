package org.theShire.presentation.Vaadin.views.Case;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.theShire.domain.medicalCase.Case;
import org.theShire.presentation.Vaadin.MainLayout;
import org.theShire.service.CaseService;

@Route(value = "cases", layout = MainLayout.class)
@PageTitle("Cases")
public class CaseView extends VerticalLayout {

    public CaseView() {
        Button button = new Button("Create Cases");
        button.addClickListener(buttonClickEvent -> {
            getUI().ifPresent(ui -> ui.navigate(CreateCaseView.class));
        });
        Grid<Case> grid = new Grid<>(Case.class);
        grid.setItems(CaseService.findAllCase());

        // automatische Spalten entfernen
        grid.removeAllColumns();

        //Spalten
        grid.addColumn(Case::getTitle).setHeader("Title");
        grid.addColumn(Case::getViewcount).setHeader("View Count");
        grid.addColumn(caseItem -> caseItem.getOwner().getProfile().getFirstName()).setHeader("Owner");
        grid.addColumn(Case::getLikeCount).setHeader("Like Count");

        add(button, grid);
    }
}
