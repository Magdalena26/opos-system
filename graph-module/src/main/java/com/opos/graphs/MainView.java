package com.opos.graphs;


import com.opos.MyUI;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.*;

/**
 * Created by damia_000 on 2016-11-29.
 */
public class MainView extends CustomComponent implements View{
    public static final String NAME = "MainView";

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        final VerticalLayout layout = new VerticalLayout();

        CssLayout groupLayout1 = new CssLayout();
        groupLayout1.setCaption("Find names in the article:");

        TextField urlField = new TextField();
        urlField.setWidth("500px");

        Button button1 = new Button("Parse");
        button1.addClickListener( e -> {
            if(urlField.getValue()!= null && urlField.getValue().trim().length()>0)
            if(((MyUI) UI.getCurrent()).crawlUrl(urlField.getValue())){
                urlField.setValue("");
            } else {
                //TODO popup
            }
        });

        groupLayout1.addComponents(urlField,button1);

        CssLayout groupLayout2 = new CssLayout();
        groupLayout2.setCaption("Show graph for name:");

        ComboBox nameField = new ComboBox();
        nameField.setWidth("500px");
        nameField.addItem("Tim");
        nameField.addItem("Alice");
        nameField.addItem("Paul");

        Button button2 = new Button("Show");
        button2.addClickListener( e -> {
            ((MyUI) UI.getCurrent()).createGraph(nameField.getValue().toString());
            UI.getCurrent().getNavigator().navigateTo(GraphView.NAME);
        });

        groupLayout2.addComponents(nameField,button2);

        layout.addComponents(groupLayout1,groupLayout2);
        layout.setMargin(true);
        layout.setSpacing(true);


        setCompositionRoot(layout);

    }
}
