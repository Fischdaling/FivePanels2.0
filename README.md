# My App

This project can be used as a starting point to create your own Vaadin application with Spring Boot.
It contains all the necessary configuration and some placeholder files to get you started.

## Running the application

The project is a standard Maven project. To run it from the command line,
type `mvnw` (Windows), or `./mvnw` (Mac & Linux), then open
http://localhost:8080 in your browser.

You can also import the project to your IDE of choice as you would with any
Maven project. Read more on [how to import Vaadin projects to different IDEs](https://vaadin.com/docs/latest/guide/step-by-step/importing) (Eclipse, IntelliJ IDEA, NetBeans, and VS Code).

## Deploying to Production

To create a production build, call `mvnw clean package -Pproduction` (Windows),
or `./mvnw clean package -Pproduction` (Mac & Linux).
This will build a JAR file with all the dependencies and front-end resources,
ready to be deployed. The file can be found in the `target` folder after the build completes.

Once the JAR file is built, you can run it using
`java -jar target/company-01-1.0-SNAPSHOT.jar`

## Project structure

- `MainLayout.java` in `src/main/java` contains the navigation setup (i.e., the
  side/top bar and the main menu). This setup uses
  [App Layout](https://vaadin.com/docs/components/app-layout).
- `views` package in `src/main/java` contains the server-side Java views of your application.
- `views` folder in `frontend/` contains the client-side JavaScript views of your application.
- `themes` folder in `frontend/` contains the custom CSS styles.

## Useful links

- Read the documentation at [vaadin.com/docs](https://vaadin.com/docs).
- Follow the tutorial at [vaadin.com/docs/latest/tutorial/overview](https://vaadin.com/docs/latest/tutorial/overview).
- Create new projects at [start.vaadin.com](https://start.vaadin.com/).
- Search UI components and their usage examples at [vaadin.com/docs/latest/components](https://vaadin.com/docs/latest/components).
- View use case applications that demonstrate Vaadin capabilities at [vaadin.com/examples-and-demos](https://vaadin.com/examples-and-demos).
- Build any UI without custom CSS by discovering Vaadin's set of [CSS utility classes](https://vaadin.com/docs/styling/lumo/utility-classes). 
- Find a collection of solutions to common use cases at [cookbook.vaadin.com](https://cookbook.vaadin.com/).
- Find add-ons at [vaadin.com/directory](https://vaadin.com/directory).
- Ask questions on [Stack Overflow](https://stackoverflow.com/questions/tagged/vaadin) or join our [Discord channel](https://discord.gg/MYFq5RTbBn).
- Report issues, create pull requests in [GitHub](https://github.com/vaadin).

## Create a grid
```

        grid = new Grid<>(XXX.class, false);
        grid.addColumn("attribute").setAutoWidth(true);
       
        grid.addComponentColumn((ValueProvider<Employee, Component>) employee -> {
                    HorizontalLayout actionLayout = new HorizontalLayout();
                    // add components
                    return actionLayout;
                }).setHeader("Actions")
                .setWidth("100px")
                .setTextAlign(ColumnTextAlign.END)
                .setFrozenToEnd(true)
                .setAutoWidth(true).setFlexGrow(0);

        grid.setHeightFull();

        grid.setItems( .... ); // set items to the grid

        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
        grid.addClassNames(LumoUtility.Border.TOP, LumoUtility.BorderColor.CONTRAST_10);
```
navigate to other page

```
getUI().ifPresent(ui -> ui.navigate(XXX.class, someValue));
```


## Binder

```
    private Binder<DomainOrModelClass> binder = new Binder<>(DomainOrModelClass.class);
    
    public XXX() {
        createLayout();
        addHandlers();

        binder.bindInstanceFields(this);
        binder.readBean(DomainOrModelClassObject);
    }

```

## Routing and navigation
```
@PageTitle("XXX")
@Route(value = "routeName", layout = MainLayout.class)
public class EmployeeFormView extends VerticalLayout implements HasUrlParameter<Long> {

    @Override
    public void setParameter(BeforeEvent event, @OptionalParameter Long eId) {
        if (eId != null) {
            readBean(Object based on provided parameter);
        } else {
            readBean(new empty object);
        }
    }

```
