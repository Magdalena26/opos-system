package edu.project.to;

/**
 * Created by magda on 29.11.16.
 */

import edu.stanford.nlp.ie.AbstractSequenceClassifier;
import edu.stanford.nlp.ie.crf.CRFClassifier;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;


public class WebCrawler {
    private final String filepath = "src/main/resources/names.txt";
    private final String ROOT_URL;
    private Map<String, Integer> internalLinks = new TreeMap<>();
    private final String serializedClassifier = "classifiers/english.all.3class.distsim.crf.ser.gz";
    private final AbstractSequenceClassifier<CoreLabel> classifier = CRFClassifier.getClassifierNoExceptions(serializedClassifier);
    private final Map<String, Integer> names = new TreeMap<>();
    private List<Person> foundPerson = new ArrayList<>();
    private int pagesCrawled = 1;
    public final String noPageRegEx = ".*?\\#.*";
    private DbConnector dbConnector = new DbConnector();

    public static final List<String> fileRegEx = new ArrayList<>();


    public WebCrawler(String url) {
        ROOT_URL = url;
    }

    public void start() {

        System.out.print("CRAWLING # " + pagesCrawled);
        crawlPage(ROOT_URL);
        dbConnector.saveToDatabase(foundPerson);
/*
        System.out.println("\n\n NAMES:");
        int i = 1;

        for (Map.Entry<String, Integer> entry : names.entrySet()) {
            System.out.println("[" + i + "] [" + entry.getValue() + "] " + entry.getKey());
            i++;
        }
*/
        for(Person p : foundPerson){
            System.out.println(p.getName()+" "+p.getUrl());
        }

        XMLOutput.createXmlOutput(names, new File("output.xml"));
        /*
        try{
            MongoClient client = new MongoClient(
                    new ServerAddress("localhost", 27017));


            Mongo mongo = new Mongo("localhost", 27017);
            DB db = mongo.getDB("crawlerDB");

            DBCollection collection = db.getCollection("names");

            // 1. BasicDBObject example
            System.out.println("BasicDBObject example...");



            i = 1;
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


        }catch(UnknownHostException e){
            e.printStackTrace();
        }*/
    }

    private void crawlPage(String url) {
        fileRegEx.add(".*?\\.png");
        fileRegEx.add(".*?\\.jpg");
        fileRegEx.add(".*?\\.jpeg");
        fileRegEx.add(".*?\\.gif");
        fileRegEx.add(".*?\\.zip");
        fileRegEx.add(".*?\\.7z");
        fileRegEx.add(".*?\\.rar");
        fileRegEx.add(".*?\\.css.*");
        fileRegEx.add(".*?\\.js.*");

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
            addPage(url, 500);
            return;
        }


        //Get every name from that page

        // nltk  python
/*
        Path filePath = new File(filepath).toPath();
        Charset charset = Charset.defaultCharset();
        List<String> englishNames = null;
        try {
            englishNames = Files.readAllLines(filePath, charset);
        } catch (IOException e) {
            e.printStackTrace();
        }
*/
        String bodyText = doc.body().text();
        String[] words = bodyText.split("\\s+");
/*
        List<String> filteredWords = Arrays.stream(words)
                .filter(englishNames::contains)
                .collect(Collectors.toList());


        filteredWords.forEach(word -> addPage(0, word));
*/
        System.out.println(" -------------- ");
        System.out.println(classifier.classifyWithInlineXML(bodyText));
        for(Person p : foundPerson){
            System.out.println(p.getName()+" "+p.getUrl());
        }
        for (List<CoreLabel> lcl : classifier.classify(bodyText)) {
            // System.out.println(i++ + ":");
            lcl.stream()
                    .filter(cl -> cl.get(CoreAnnotations.AnswerAnnotation.class).equals("PERSON"))
                    .forEach(cl -> addPage(url, cl.originalText()));

        }


        Elements links = doc.select("a[href]");
        for(int i=0;i<10;i++){
        for (Element link : links) {
            String linkUrl = link.attr("abs:href");
            if (linkUrl.startsWith(ROOT_URL)) { // Found an internal link
                System.out.println("hiiiii--------------------" + linkUrl);
                if (!linkUrl.matches(noPageRegEx)) { // This link does not end with a "#"
                    // Is link a file?
                    for (String regex : fileRegEx) {
                        if (linkUrl.matches(regex)) {
                            // It is a file --> add to list, but do not try to crawl
                            addPage(0, linkUrl);
//System.out.println(linkUrl);
                        }
                    }
                    if (!listContains(internalLinks, linkUrl) && !linkUrl.equals("")) {
                        System.out.println(linkUrl);
                        crawlPage(linkUrl);
                    }
                }
            }
        }
               /*     System.out.println(classifier.classifyWithInlineXML(bodyText));
                    for (List<CoreLabel> lcl : classifier.classify(bodyText)) {
                        // System.out.println(i++ + ":");
                        lcl.stream()
                                .filter(cl -> cl.get(CoreAnnotations.AnswerAnnotation.class).equals("PERSON"))
                                .forEach(cl -> addPage(linkUrl, cl.originalText()));

                    }*/
                    //filteredWords.forEach(System.out::println);

                      //  }
                    //}

                    // No file --> crawl this page
                }
            }






    private void addPage(int listId, String url) {
       //  int statuscode = new ConnectionTester(url).getStatuscode();
        if (listId == 0) {
            names.put(url, 200);
        }
    }


    private void addPage(String url, int statuscode) {
        names.put(url, statuscode);
    }

    private void addPage(String url, String name) {
        internalLinks.put(url, 200);
        foundPerson.add(new Person(url, name));
    }

    private boolean listContains(Map<String, Integer> list, String url) {
        return list.keySet().contains(url);
    }

    public Map<String, Integer> getNames() {
        return names;
    }



}
