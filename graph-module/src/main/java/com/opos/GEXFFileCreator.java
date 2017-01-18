package com.opos;

import edu.project.to.DbConnector;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by kasia on 11.01.17.
 */
public class GEXFFileCreator {

    private String name;
    private int num;

    // the argument list is only applied for test purposes
    public GEXFFileCreator(String name, int num) {
        this.name = name;
        this.num = num;
        saveFile(name);
    }

    // this function creates file with basic gexf info and saves it
    private void saveFile(String name) {
        try{
            PrintWriter writer = new PrintWriter("src/main/resources/graphData.gexf", "UTF-8");
            writer.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                    "<gexf xmlns:viz=\"http:///www.gexf.net/1.1draft/viz\" version=\"1.1\" xmlns=\"http://www.gexf.net/1.1draft\">\n" +
                    "<meta lastmodifieddate=\"2010-03-03+23:44\">\n" +
                    "<creator>Gephi 0.7</creator>\n" +
                    "</meta>\n" +
                    "<graph defaultedgetype=\"undirected\" idtype=\"string\" type=\"static\">");

            ArrayList<String> names = getData(name);
            parseData(writer, names);

            writer.println("</graph>\n" + "</gexf>");
            writer.close();
        } catch (IOException e) {
            // do something
        }
    }

    // function to filter db collection to get people associated with a given name
    private ArrayList<String> getData(String name) {
        DbConnector dbconnector = new DbConnector();
        List<String> urls = dbconnector.getPeopleByName(name);
        ArrayList<String> names = new ArrayList<>();
        for(String u: urls) {
            names.addAll(dbconnector.getPeopleByUrl(u));
        }
        return names;
    }

    // function to create the nodes and edges part of the file
    private void parseData(PrintWriter writer, List<String> names) {
        HashMap<String, Integer> namesMap = new HashMap<>();
        for (String name : names) {
            if(namesMap.containsKey(name)) {
                int weight = namesMap.get(name);
                namesMap.put(name, weight + 1);
            }
            else {
                namesMap.put(name, 1);
            }
        }

        writer.println("<nodes count=\"" + (namesMap.size() + 1) + "\">");
        writer.println("<node id=\"0\" label=\"" + name + "\"/>");
        int count = 0;
        for(Map.Entry<String, Integer> name : namesMap.entrySet()) {
            if(name.getValue() > num) {
                writer.println("<node id=\"" + (count + 1) + "\" label=\"" + name.getKey() + "\" weight=\"" + name.getValue() + "\"/>");
                count++;
            }
        }
        writer.println("</nodes>");

        writer.println("<edges count=\"" + namesMap.size() + "\">");
        for (int i = 0; i < count; i++) {
            writer.println("<edge id=\"" + i + "\" source=\"0\" target=\"" + (i + 1) + "\"/>");
        }
        writer.println("</edges>");
    }
}
