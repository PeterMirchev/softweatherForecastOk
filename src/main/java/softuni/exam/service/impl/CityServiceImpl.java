package softuni.exam.service.impl;

import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.CitySeedDTO;
import softuni.exam.models.entity.City;
import softuni.exam.repository.CityRepository;
import softuni.exam.service.CityService;
import softuni.exam.service.CountryService;
import softuni.exam.util.ValidationUtil;
import softuni.exam.util.impl.ValidationUtilImpl;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

@Service
public class CityServiceImpl implements CityService {
    private final CountryService countryService;
    private final ModelMapper modelMapper;
    private final CityRepository cityRepository;
    private final Gson gson;
    private final ValidationUtil validationUtil;
    private final String READ_CITY_FILE_PATH = "src/main/resources/files/json/cities.json";

    public CityServiceImpl(CountryService countryService, ModelMapper modelMapper, CityRepository cityRepository, Gson gson, ValidationUtilImpl validationUtil) {
        this.countryService = countryService;
        this.modelMapper = modelMapper;
        this.cityRepository = cityRepository;
        this.gson = gson;
        this.validationUtil = validationUtil;

    }

    @Override
    public boolean areImported() {
        return this.cityRepository.count() > 0;
    }

    @Override
    public String readCitiesFileContent() throws IOException {
        return Files.readString(Path.of(READ_CITY_FILE_PATH));
    }

    @Override
    public String importCities() throws IOException {
        StringBuilder sb = new StringBuilder();

        CitySeedDTO[] citySeedDTOS = gson.fromJson(new FileReader(READ_CITY_FILE_PATH), CitySeedDTO[].class);

        Arrays.stream(citySeedDTOS)
                .filter(dto -> validateCitySeedDto(dto, sb))
                .map(dto -> {
                    City city = modelMapper.map(dto, City.class);
                    city.setCountry(countryService.findById(dto.getCountry()));
                    return city;
                })
                .forEach(cityRepository::save);

        return sb.toString().trim();
    }


    @Override
    public City findById(Long cityId) {
        return cityRepository.findById(cityId).orElse(null);
    }

    private boolean validateCitySeedDto(CitySeedDTO dto, StringBuilder sb) {
        boolean isValid =true;

        if(cityRepository.existsByCityName(dto.getCityName())) {
            isValid = false;
        } else {
            isValid = validationUtil.isValid(dto);
        }

        sb.append(isValid ? String.format("Successfully imported city %s - %s",dto.getCityName(), dto.getPopulation()): "Invalid city").append(System.lineSeparator());
        return isValid;
    }
}
