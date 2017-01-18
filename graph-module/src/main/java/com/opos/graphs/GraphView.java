package com.opos.graphs;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.FileResource;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.*;

import java.io.File;

/**
 * Created by damia_000 on 2016-11-29.
 */
public class GraphView extends CustomComponent implements View {

    public static final String NAME = "GraphView";

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        VerticalLayout mainLayout = new VerticalLayout();

        ThemeResource resource = new ThemeResource("img/graph.png");
        Image image = new Image("",resource);

        CssLayout groupLayout = new CssLayout();

        ComboBox optionField = new ComboBox();

        Button button = new Button("Show");
        button.addClickListener( e -> {
            UI.getCurrent().getNavigator().navigateTo(GraphView.NAME);
        });

        groupLayout.addComponents(optionField,button);

        Button returnButton = new Button("Return");
        returnButton.addClickListener(e -> {

                    UI.getCurrent().getNavigator().navigateTo(MainView.NAME);
                }
        );

        mainLayout.addComponents(image,returnButton);
        setCompositionRoot(mainLayout);
    }
}
