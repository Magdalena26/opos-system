package com.opos.graphs;


import com.opos.MyUI;
import com.vaadin.data.validator.IntegerRangeValidator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.*;

/**
 * Created by damia_000 on 2016-11-29.
 */
public class MainView extends CustomComponent implements View{
    public static final String NAME = "MainView";

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        final VerticalLayout layout = new VerticalLayout();

        ThemeResource resource = new ThemeResource("img/logo.png");
        Image image = new Image("",resource);
        layout.addComponent(image);
        layout.setComponentAlignment(image,Alignment.MIDDLE_CENTER);

        CssLayout groupLayout1 = new CssLayout();
        groupLayout1.setCaption("Find names in the article:");

        TextField urlField = new TextField();
        urlField.setWidth("500px");

        Button button1 = new Button("Parse");
        button1.addClickListener( e -> {
            if(urlField.getValue()!= null && urlField.getValue().trim().length()>0)
            if(((MyUI) UI.getCurrent()).crawlUrl(urlField.getValue())){
                urlField.setValue("");
                showPopUpParse(0);
            } else {
                showPopUpParse(-1);
            }
        });

        groupLayout1.addComponents(urlField,button1);

        CssLayout groupLayout2 = new CssLayout();
        groupLayout2.setCaption("Show graph for name:");

        ComboBox nameField = new ComboBox();
        for(String name :((MyUI) UI.getCurrent()).getNameSet()){
            nameField.addItem(name);
        }

        TextField numField = new TextField();
        numField.setWidth("100px");


        Button button2 = new Button("Show");
        button2.addClickListener( e -> {
            if(nameField.getValue()!=null && numField.getValue()!=null && numField.getValue().trim().length()>0) {
                try{
                    int num = Integer.parseInt(numField.getValue().trim());
                ((MyUI) UI.getCurrent()).createGraph(nameField.getValue().toString(), num);
                UI.getCurrent().getNavigator().navigateTo(GraphView.NAME);
                } catch (Exception exception){
                    UI.getCurrent().addWindow(createPopUpError());                }
            } else {
                    UI.getCurrent().addWindow(createPopUpError());            }
        });

        groupLayout2.addComponents(nameField,numField,button2);

        layout.addComponents(groupLayout1,groupLayout2);
        layout.setComponentAlignment(groupLayout1,Alignment.MIDDLE_CENTER);
        layout.setComponentAlignment(groupLayout2,Alignment.MIDDLE_CENTER);
        layout.setMargin(true);
        layout.setSpacing(true);


        setCompositionRoot(layout);

    }

    private Window createPopUpError(){

        Window w = new Window();
        w.center();
        w.setModal(true);
        w.setResizable(false);


        VerticalLayout errorLayout = new VerticalLayout();
        errorLayout.setWidth("200px");
        errorLayout.setHeight("50px");
        errorLayout.setMargin(true);


        Label l = new Label("Podano bledne dane");

        errorLayout.addComponent(l);
        w.setContent(errorLayout);
        w.setCaption("Blad");
        return w;
    }

    public void showPopUpParse(int i){

        Window w = new Window();
        w.center();
        w.setModal(true);
        w.setResizable(false);


        VerticalLayout errorLayout = new VerticalLayout();
        errorLayout.setWidth("200px");
        errorLayout.setHeight("50px");
        errorLayout.setMargin(true);

        Label l;

        if(i == -1) {
            w.setCaption("Blad");
            l = new Label("Wystapil blad podczas parsingu");
        } else {
            w.setCaption("Sukces");
            l = new Label("Parsing zakonczony sukcesem");
        }
        errorLayout.addComponent(l);
        w.setContent(errorLayout);

        UI.getCurrent().addWindow(w);
    }
}
