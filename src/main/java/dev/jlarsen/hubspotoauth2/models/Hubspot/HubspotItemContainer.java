package dev.jlarsen.hubspotoauth2.models.Hubspot;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class HubspotItemContainer<T> {

    private Long id;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private boolean archived;
    private T properties;

    public HubspotItemContainer() {
    }

    public HubspotItemContainer(T properties) {
        this.properties = properties;
    }
}
