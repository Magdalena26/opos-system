package edu.project.to;

import java.io.File;
import java.util.List;

public class GraphModuleConnector implements GraphConnectorInterface {

    @Override
    public File getGraphFile(String name) {
        List<Person> personList = DbConnector.getPeopleByName(name);

        //TODO

        Object object = createGexfFile(personList);

        return null;
    }

    //TODO refactor this
    private Object createGexfFile(List<Person> personList) {
        return new Object();
    }

}
