package com.juu.juulabel.api.service.alcohol;

import com.juu.juulabel.domain.entity.alcohol.*;
import com.juu.juulabel.domain.repository.jpa.*;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class DataMappingService {
    private final AlcoholicDrinksJpaRepository alcoholicDrinksRepository;
    private final IngredientJpaRepository ingredientRepository;
    private final AlcoholicDrinksIngredientJpaRepository alcoholicDrinksIngredientRepository;
    private final BreweryJpaRepository breweryJpaRepository;
    private final AlcoholTypeJpaRepository alcoholTypeJpaRepository;

    @Transactional
    public void parseAndInsertData(String alcoholicDrinksCsvPath, String ingredientCsvPath) throws IOException, CsvValidationException {
        System.out.println("파일 경로: " + alcoholicDrinksCsvPath + ", " + ingredientCsvPath);

        // 원재료 데이터 파싱 및 ingredientIdMap 생성
        Map<String, Ingredient> ingredientMap = parseIngredientCsv(ingredientCsvPath);

        // 주류 데이터 파싱 및 매핑 데이터 생성
        List<AlcoholicDrinksIngredient> alcoholicDrinksIngredientList = parseAlcoholicDrinksCsv(alcoholicDrinksCsvPath, ingredientMap);

        System.out.println("CSV 파싱 완료. 데이터 저장 중...");
        alcoholicDrinksIngredientRepository.saveAll(alcoholicDrinksIngredientList);

        System.out.println("데이터 삽입 완");
    }

    // 원재료 CSV 파싱
    private Map<String, Ingredient> parseIngredientCsv(String ingredientCsvPath) throws IOException, CsvValidationException {
        Map<String, Ingredient> ingredientMap = new HashMap<>();
        try (CSVReader reader = new CSVReader(new FileReader(ingredientCsvPath))) {
            String[] nextLine;
            reader.readNext();  // 헤더 건너뛰기
            while ((nextLine = reader.readNext()) != null) {
                String ingredientName = nextLine[1].trim();
                Long ingredientId = Long.parseLong(nextLine[0].replace("ID", "").trim());

                // Ingredient 엔티티를 id로 가져와서 map에 저장
                Ingredient ingredient = ingredientRepository.findById(ingredientId)
                        .orElseThrow(() -> new RuntimeException("Ingredient not found for id: " + ingredientId));
                ingredientMap.put(ingredientName, ingredient);
            }
        }
        return ingredientMap;
    }


    // 주류 CSV 파싱 및 매핑 데이터 생성
    public List<AlcoholicDrinksIngredient> parseAlcoholicDrinksCsv(
            String alcoholicDrinksCsvPath, Map<String, Ingredient> ingredientMap) throws CsvValidationException, IOException {

        List<AlcoholicDrinksIngredient> resultList = new ArrayList<>();
        try (CSVReader reader = new CSVReader(new FileReader(alcoholicDrinksCsvPath))) {
            String[] nextLine;
            reader.readNext();  // 헤더 건너뛰기
            while ((nextLine = reader.readNext()) != null) {
                String description = nextLine[0].trim();
                String alcoholicDrinksName = nextLine[7].trim();
                String image = nextLine[3].trim();  // 이미지 URL
                Double alcoholContent = Double.parseDouble(nextLine[8].trim());
                int volume = Integer.parseInt(nextLine[9].trim());
                String[] ingredients = nextLine[12].split(",");
                String alcoholTypeName = nextLine[11].trim();
                String breweryName = nextLine[6].trim();
                int discountPrice = Integer.parseInt(nextLine[4].trim().replaceAll("[^0-9]", ""));
                int regularPrice = Integer.parseInt(nextLine[5].trim().replaceAll("[^0-9]", ""));

                // AlcoholType 매핑
                AlcoholType alcoholType = alcoholTypeJpaRepository.findFirstByName(alcoholTypeName)
                        .orElseThrow(() -> new RuntimeException("AlcoholType not found for name: " + alcoholTypeName));

                // 주류 이름으로 AlcoholicDrinks 객체 찾기
//                AlcoholicDrinks alcoholicDrinks = alcoholicDrinksRepository.findByName(alcoholicDrinksName)
//                        .orElseThrow(() -> new RuntimeException("AlcoholicDrinks not found for name: " + alcoholicDrinksName));


                // 양조장 이름으로 Brewery 객체 찾기
                Brewery brewery = breweryJpaRepository.findFirstByName(breweryName)
                        .orElseThrow(() -> new RuntimeException("Brewery not found for name: " + breweryName));

                // AlcoholicDrinks 객체 생성
                AlcoholicDrinks alcoholicDrinks = AlcoholicDrinks.builder()
                        .name(alcoholicDrinksName)
                        .alcoholContent(alcoholContent)
                        .volume(volume)
                        .discountPrice(discountPrice)
                        .regularPrice(regularPrice)
                        .image(image)
                        .alcoholType(alcoholType)
                        .brewery(brewery)
                        .rating(alcoholContent)  // 기본값 0
                        .description(description)
                        .tastingNoteCount(0)  // 초기 값
                        .build();

                alcoholicDrinksRepository.save(alcoholicDrinks);

                // 각 원재료와 매핑
                for (String ingredientName : ingredients) {
                    ingredientName = ingredientName.trim();
                    Ingredient ingredient = ingredientMap.get(ingredientName);

                    if (ingredient != null) {
                        // AlcoholicDrinks, Ingredient 매핑
                        AlcoholicDrinksIngredient mapping = AlcoholicDrinksIngredient.builder()
                                .alcoholicDrinks(alcoholicDrinks)
                                .ingredient(ingredient)
                                .build();
                        alcoholicDrinksIngredientRepository.save(mapping);
                    } else {
                        System.out.println("Ingredient not found: " + ingredientName);
                    }
                }
            }
        }
        return resultList;
    }
}
