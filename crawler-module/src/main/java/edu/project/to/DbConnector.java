package edu.project.to;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * Created by magda on 18.12.16.
 */

public class DbConnector {

    private static final String DATABASE_NAME = "crawler-db";
    private static final String COLLECTION_NAME = "names";

    private static final MongoClient mongoClient = new MongoClient("localhost", 27017);
    private static final MongoDatabase database = mongoClient.getDatabase(DATABASE_NAME);
    private static final MongoCollection<Document> collection = database.getCollection(COLLECTION_NAME);


    private static void savePerson(Person person) {

        Document document = new Document();
        document.put("name", person.getName());
        document.put("url", person.getUrl());
        collection.insertOne(document);

        System.out.println(document.toJson());

    }

    public static void savePersonCollection(List<Person> personList) {
        personList.forEach(DbConnector::savePerson);
    }

    public List<String> getPeopleByName(String name) {

        Document filter = new Document("name", name);
        MongoCursor<Document> cursor = collection.find(filter).iterator();

        List<Person> personList = new ArrayList<>();
        List<String> personUrlList = new ArrayList<>();
        cursor.forEachRemaining(document ->
                personUrlList.add(document.getString("url"))
        );

        return personUrlList;
    }

    public List<String> getPeopleByUrl(String url){
        Document filter = new Document("url", url);
        MongoCursor<Document> cursor = collection.find(filter).iterator();

        List<String> personNameList = new ArrayList<>();
        cursor.forEachRemaining(document ->
                personNameList.add(document.getString("name"))
        );
        return personNameList;
    }

    public Set<String> getPeopleNameFromDatabase(){
        Document filter = new Document();
        MongoCursor<Document> cursor = collection.find(filter).iterator();

        Set<String> personNameList = new HashSet<>();
        cursor.forEachRemaining(document ->
            personNameList.add(document.getString("name"))
        );
        return personNameList;
    }
}
