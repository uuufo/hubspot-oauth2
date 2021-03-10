package dev.jlarsen.hubspotoauth2.models.Hubspot;

public enum RequestType {
    CONTACT("/contacts", "firstname,lastname,phone,email"),
    COMPANY("/companies", "name,city,state,phone,domain"),
    DEAL("/deals","dealname,dealstage,closedate,amount");

    private final String url;
    private final String properties;

    RequestType(String url, String properties) {
        this.url = url;
        this.properties = properties;
    }

    public String getUrl() {
        return url;
    }

    public String getProperties() {
        return properties;
    }
}