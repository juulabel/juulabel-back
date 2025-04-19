package com.juu.juulabel.common.config;

import com.juu.juulabel.alcohol.service.DataMappingService;
import org.springframework.boot.CommandLineRunner;

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
