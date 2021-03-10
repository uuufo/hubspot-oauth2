package dev.jlarsen.hubspotoauth2.models.Hubspot;

import lombok.Data;

import java.util.ArrayList;

@Data
public class HubspotResponse<T> {

    private ArrayList<HubspotItemContainer<T>> results;

}
