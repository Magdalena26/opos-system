package edu.project.to;

import com.mongodb.*;

import java.net.UnknownHostException;
import java.util.List;

/**
 * Created by magda on 18.12.16.
 */
public class DbConnector {

    public DbConnector(){

    }

    public void saveToDatabase(List<Person> foundPerson){
        try{
            MongoClient client = new MongoClient(
                    new ServerAddress("localhost", 27017));


            Mongo mongo = new Mongo("localhost", 27017);
            DB db = mongo.getDB("crawlerDB");

            DBCollection collection = db.getCollection("names");

            // 1. BasicDBObject example
            System.out.println("BasicDBObject example...");



            int i = 1;
            // for (Map.Entry<String, Integer> entry : names.entrySet()) {
            for(Person p : foundPerson){
                BasicDBObject document = new BasicDBObject();
                document.put("database", "crawlerDB");
                document.put("table", "names");
                BasicDBObject documentDetail = new BasicDBObject();
                System.out.println("[" + i + "] [" + p.getName() + "] " + p.getUrl());
                i++;

                documentDetail.put("name", p.getName());
                documentDetail.put("index", p.getUrl());
                documentDetail.put("number", i);
                document.put("detail", documentDetail);

                collection.insert(document);
                DBCursor cursorDoc = collection.find();
                while (cursorDoc.hasNext()) {
                    System.out.println(cursorDoc.next());
                }
            }


        }catch(UnknownHostException e) {
            e.printStackTrace();
        }
    }
}
