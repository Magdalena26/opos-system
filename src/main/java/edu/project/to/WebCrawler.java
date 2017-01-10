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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class WebCrawler {

    private final String CLASSIFIER_PATH = "classifiers/english.all.3class.distsim.crf.ser.gz";
    private final AbstractSequenceClassifier<CoreLabel> classifier =
            CRFClassifier.getClassifierNoExceptions(CLASSIFIER_PATH);

    private static final String noPageRegEx = ".*?\\#.*";

    private final String ROOT_URL;
    private final List<String> urls = new ArrayList<>();
    private final List<Person> peopleFound = new ArrayList<>();

    private static int pagesCrawled = 0;


    public WebCrawler(String url) {
        ROOT_URL = url;
    }

    public void start() {
        System.out.println("CRAWLING # " + pagesCrawled);
        crawlRootPage(ROOT_URL);
        DbConnector.savePersonCollection(peopleFound);
    }

    private void crawlRootPage(String url) {

        pagesCrawled++;

        Optional<Document> doc = getDocument(url);

        if(doc.isPresent()) {

            String bodyText = getBodyFromHtml(doc.get());
            extractPeople(bodyText, url);

            List<String> subpages = new ArrayList<>();
            Elements links = doc.get().select("a[href]");
            for (Element link : links) {
                String linkUrl = link.attr("abs:href");
                if (validateSubpageUrl(linkUrl)) {
                    subpages.add(linkUrl);
                }
            }

            subpages.forEach(page -> {
                Optional<Document> doc1 = getDocument(page);
                if(doc1.isPresent()){
                    String bodyText1 = getBodyFromHtml(doc1.get());
                    extractPeople(bodyText1, page);

                }
            });

        }

    }

    private void extractPeople(String body, String url) {
        for (List<CoreLabel> lcl : classifier.classify(body)) {
            lcl.stream()
                    .filter(cl -> cl.get(CoreAnnotations.AnswerAnnotation.class).equals("PERSON"))
                    .forEach(cl -> addFoundPerson(url, cl.originalText()));
        }
    }

    private String getBodyFromHtml(Document doc) {
        return doc.body().text();
    }

    private Optional<Document> getDocument(String url) {
        Document doc;
        try {
            doc = Jsoup.connect(url).userAgent(Main.APPLICATION_UA).timeout(5000).get();
            return Optional.ofNullable(doc);
        } catch (IOException e) {
            System.out.println("Unable to read page at [" + url + "]: " + e.getMessage());
            return Optional.empty();
        }
    }

    private boolean validateSubpageUrl(String url) {
        return url.startsWith(ROOT_URL)
                && !url.matches(noPageRegEx)
                && !url.equals("");
    }

    private void addFoundPerson(String url, String name) {
        urls.add(url);
        peopleFound.add(new Person(url, name));
    }
}