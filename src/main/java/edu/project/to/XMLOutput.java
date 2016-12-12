package edu.project.to;

/**
 * Created by magda on 29.11.16.
 */

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.util.Map;

public class XMLOutput {


    public static void createXmlOutput(Map<String, Integer> names, File outputFile) {
        // Create DocumentBuilder
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder;
        try {
            docBuilder = docFactory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            System.out.println("Unable to create DocumentBuilder: " + e.getMessage());
            return;
        }

        // new XML-file
        Document doc = docBuilder.newDocument();

        if (!names.isEmpty()) {
            // urlset (contains every url)
            Element urls = doc.createElement("urlset");
            doc.appendChild(urls);

            // internal urls
            if (!names.isEmpty()) {
                Element internalUrls = doc.createElement("internal");
                urls.appendChild(internalUrls);

                // add every internal url + its statuscode
                for (Map.Entry<String, Integer> entry : names.entrySet()) {
                    Element link = doc.createElement("link");
                    Element url = doc.createElement("url");
                    Element code = doc.createElement("code");

                    url.appendChild(doc.createTextNode(entry.getKey()));
                    code.appendChild(doc.createTextNode(String.valueOf(entry.getValue())));

                    link.appendChild(url);
                    link.appendChild(code);
                    internalUrls.appendChild(link);
                }
            }

        }

        // write content into xml file
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer;
        try {
            transformer = transformerFactory.newTransformer();
        } catch (TransformerConfigurationException e) {
            System.out.println("Unable to create Transformer: " + e.getMessage());
            return;
        }
        // use indentation
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(outputFile);

        try {
            transformer.transform(source, result);
        } catch (TransformerException e) {
            System.out.println("Unable to transform: " + e.getMessage());
        }
    }
}
