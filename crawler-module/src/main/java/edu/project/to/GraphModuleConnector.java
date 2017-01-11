package edu.project.to;

import java.io.File;
import java.util.List;

public class GraphModuleConnector implements GraphConnectorInterface {

    @Override
    public File getGraphFile(String name) {
        //List<String> personList = DbConnector.getPeopleByName(name);

        //TODO

        //Object object = createGexfFile(personList);

        return null;
    }

    //TODO refactor this
    private Object createGexfFile(List<String> personList) {
        return new Object();
    }

}
