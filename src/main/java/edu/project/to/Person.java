package edu.project.to;

/**
 * Created by magda on 17.12.16.
 */
public class Person {
    private String Name;
    private String Url;

    public Person(String url, String name){
        this.Url = url;
        this.Name = name;
    }

    public String getName() {
        return Name;
    }

    public String getUrl() {
        return Url;
    }

    public void setUrl(String url) {
        Url = url;
    }

    public void setName(String name) {
        Name = name;
    }
}
