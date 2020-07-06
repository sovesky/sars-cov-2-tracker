package com.sovesky.sarscov2tracker.models;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class LocationStats {
    private String state;
    private String country;
    private int latestTotalCases;
    private int diffFromPrevDay;
}
