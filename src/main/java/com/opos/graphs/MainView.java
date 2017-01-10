package com.opos.graphs;


import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.*;

/**
 * Created by damia_000 on 2016-11-29.
 */
public class MainView extends CustomComponent implements View{
    public static final String NAME = "";

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        final VerticalLayout layout = new VerticalLayout();

        final TextField name = new TextField();
        name.setCaption("Type your name here:");

        Button button = new Button("Click Me");
        button.addClickListener( e -> {
            layout.addComponent(new Label("Thanks " + name.getValue()
                    + ", it works!"));
        });

        layout.addComponents(name, button);
        layout.setMargin(true);
        layout.setSpacing(true);



        Button graph = new Button("Show graph");
        graph.addClickListener((Button.ClickListener) clickEvent -> UI.getCurrent().getNavigator().navigateTo(GraphView.NAME));
        layout.addComponent(graph);
        setCompositionRoot(layout);

    }
}
