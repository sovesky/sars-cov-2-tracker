package com.sovesky.sarscov2tracker.services;

import com.sovesky.sarscov2tracker.models.LocationStats;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@Service
public class CoronaVirusDataService {

    private final RestTemplate rt;
    private final Logger logger = LoggerFactory.getLogger(this.toString());
    private static final String VIRUS_DATA_URL = "https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/csse_covid_19_time_series/time_series_covid19_confirmed_global.csv";

    public CoronaVirusDataService(RestTemplate rt){
        this.rt = rt;
    }

    private List<LocationStats> allStats = new ArrayList<>();

    @PostConstruct
    @Scheduled(cron = "* * 1 * * *") // second minute hour day month year
    public void fetchDataFromRepo() throws IOException {
        logger.debug("Getting data from Github");
        ResponseEntity<String> response
                = rt.getForEntity(URI.create(VIRUS_DATA_URL), String.class);
        // Created for concurrency reasons. We don't failures during updates.
        List<LocationStats> newStats = new ArrayList<>();

        Reader csvBodyReader = new StringReader(response.getBody());
        Iterable<CSVRecord> records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(csvBodyReader);
        for(CSVRecord record: records){
            LocationStats ls = new LocationStats();
            ls.setCountry(record.get("Country/Region"));
            ls.setState(record.get("Province/State"));
            int latestCases = Integer.parseInt(record.get(record.size()-1));
            int prevDayCases = Integer.parseInt(record.get(record.size()-2));
            ls.setLatestTotalCases(latestCases);
            ls.setDiffFromPrevDay(latestCases - prevDayCases);
            logger.debug(ls.toString());
            newStats.add(ls);
        }
        synchronized(this) {
            allStats = newStats;
        }
    }

    public List<LocationStats> getAllStats() {
        return allStats;
    }
}
