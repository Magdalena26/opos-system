package edu.project.to;

/**
 * Created by magda on 17.12.16.
 */

public class Person {
    private String name;
    private String url;

    public Person(String url, String name){
        this.url = url;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        url = url;
    }

    public void setName(String name) {
        name = name;
    }
}
