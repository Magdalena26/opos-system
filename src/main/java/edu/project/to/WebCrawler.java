package edu.project.to;

/**
 * Created by magda on 29.11.16.
 */
import com.mongodb.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import java.io.File;
import java.io.IOException;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class WebCrawler {
    private String filepath = "src/main/resources/names.txt";
    private final String ROOT_URL;
    private Map<String, Integer> names = new TreeMap<>();
    private int pagesCrawled = 1;

    public WebCrawler(String url) {
        ROOT_URL = url;
    }

    public void start() {

        System.out.print("CRAWLING #" + pagesCrawled);
        crawlPage(ROOT_URL);

        System.out.println("\n\n NAMES:");
        int i = 1;
        for (Map.Entry<String, Integer> entry : names.entrySet()) {
            System.out.println("[" + i + "] [" + entry.getValue() + "] " + entry.getKey());
            i++;
        }

        XMLOutput.createXmlOutput(names, new File("output.xml"));
        try{
            MongoClient client = new MongoClient(
                    new ServerAddress("localhost", 27017));


            Mongo mongo = new Mongo("localhost", 27017);
            DB db = mongo.getDB("crawlerDB");

            DBCollection collection = db.getCollection("names");

            // 1. BasicDBObject example
            System.out.println("BasicDBObject example...");



            i = 1;
            for (Map.Entry<String, Integer> entry : names.entrySet()) {
                BasicDBObject document = new BasicDBObject();
                document.put("database", "crawlerDB");
                document.put("table", "names");
                BasicDBObject documentDetail = new BasicDBObject();
                System.out.println("[" + i + "] [" + entry.getValue() + "] " + entry.getKey());
                i++;

                documentDetail.put("name", entry.getKey());
                documentDetail.put("index", entry.getValue());
                documentDetail.put("number", i);
                document.put("detail", documentDetail);

                collection.insert(document);
                DBCursor cursorDoc = collection.find();
                while (cursorDoc.hasNext()) {
                    System.out.println(cursorDoc.next());
                }
            }


        }catch(UnknownHostException e){
            e.printStackTrace();
        }
    }

    private void crawlPage(String url) {
        addPage(0, url);

        for (int i = 0; i < String.valueOf(pagesCrawled - 1).length() + 10; i++) {
            System.out.print("\b");
        }
        System.out.print("CRAWLING #" + pagesCrawled);
        pagesCrawled++;

        // Open page at url
        Document doc;
        try {
            doc = Jsoup.connect(url).userAgent(Main.APPLICATION_UA).timeout(5000).get();
        } catch (IOException e) {
            System.out.println("\nUnable to read Page at [" + url + "]: " + e.getMessage());
            addPage(0, url, 500);
            return;
        }

        //Get every name from that page

        // nltk  python

        Path filePath = new File(filepath).toPath();
        Charset charset = Charset.defaultCharset();
        List<String> englishNames = null;
        try {
            englishNames = Files.readAllLines(filePath, charset);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String bodyText = doc.body().text();
        String[] words = bodyText.split("\\s+");

        List<String> filteredWords = Arrays.stream(words)
                .filter(englishNames::contains)
                .collect(Collectors.toList());


        filteredWords.forEach(word -> addPage(0, word));

        System.out.println(" -------------- ");
        filteredWords.forEach(System.out::println);
    }

    private void addPage(int listId, String url) {
       //  int statuscode = new ConnectionTester(url).getStatuscode();
        if (listId == 0) {
            names.put(url, 200);
        }
    }


    private void addPage(int listId, String url, int statuscode) {
        if (listId == 0) {
            names.put(url, statuscode);
        }
    }

    private boolean listContains(Map<String, Integer> list, String url) {
        return list.keySet().contains(url);
    }

    public Map<String, Integer> getNames() {
        return names;
    }

    public void setNames(Map<String, Integer> names) {
        this.names = names;
    }

}
