package com.opos;

import com.opos.graphs.GraphView;
import com.opos.graphs.MainView;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.UI;
import edu.project.to.Main;

import javax.servlet.annotation.WebServlet;

/**
 * This UI is the application entry point. A UI may either represent a browser window 
 * (or tab) or some part of a html page where a Vaadin application is embedded.
 * <p>
 * The UI is initialized using {@link #init(VaadinRequest)}. This method is intended to be 
 * overridden to add component to the user interface and initialize non-component functionality.
 */
@Theme("mytheme")
public class MyUI extends UI {

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        MainView mainView = new MainView();
        Navigator nav =new Navigator(this,this);
        GraphView graphView = new GraphView();
        nav.addView(MainView.NAME,mainView);
        nav.addView(GraphView.NAME,graphView);
        nav.navigateTo(MainView.NAME);

    }

    @WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = MyUI.class, productionMode = false)
    public static class MyUIServlet extends VaadinServlet {
    }

    public boolean crawlUrl(String url){
        Main mainCrawler = new Main();
        mainCrawler.crawlUrl(url);

        return true;
    }

    public void createGraph(String name) {
        GEXFFileCreator gexfFileCreator = new GEXFFileCreator(name);
        Graph graph = new Graph();
    }
}
