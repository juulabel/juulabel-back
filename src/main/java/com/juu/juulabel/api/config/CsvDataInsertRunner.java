package com.juu.juulabel.api.config;

import com.juu.juulabel.api.service.alcohol.DataMappingService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

//@Service
public class CsvDataInsertRunner implements CommandLineRunner {
    private final DataMappingService service;

    public CsvDataInsertRunner(DataMappingService service) {
        this.service = service;
    }

    @Override
    public void run(String... args) throws Exception {
        service.parseAndInsertData("/Users/kyungmi/Downloads/alcoholicDrinks.csv","/Users/kyungmi/Downloads/Ingredient.csv");
    }
}
