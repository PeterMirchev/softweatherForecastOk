package softuni.exam.service.impl;

import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.CountrySeedDTO;
import softuni.exam.models.entity.Country;
import softuni.exam.repository.CountryRepository;
import softuni.exam.service.CountryService;
import softuni.exam.util.ValidationUtil;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

@Service
public class CountryServiceImpl implements CountryService {
    private final CountryRepository countryRepository;
    private final ModelMapper modelMapper;
    private final Gson gson;
    private final ValidationUtil validationUtil;
    private final String READ_FILE_INPUT = "src/main/resources/files/json/countries.json";

    public CountryServiceImpl(CountryRepository countryRepository, ModelMapper modelMapper, Gson gson, ValidationUtil validationUtil) {
        this.countryRepository = countryRepository;
        this.modelMapper = modelMapper;
        this.gson = gson;
        this.validationUtil = validationUtil;
    }

    @Override
    public boolean areImported() {
        return countryRepository.count() > 0;
    }

    @Override
    public String readCountriesFromFile() throws IOException {
        return Files.readString(Path.of(READ_FILE_INPUT));
    }

    @Override
    public String importCountries() throws IOException {
        StringBuilder sb = new StringBuilder();

        CountrySeedDTO[] countrySeedDTOS = gson.fromJson(new FileReader(READ_FILE_INPUT), CountrySeedDTO[].class);

        Arrays.stream(countrySeedDTOS)
                .filter(dto -> validateCountrySeedDto(dto, sb))
                .map(dto -> modelMapper.map(dto, Country.class))
                .forEach(countryRepository::save);
        return sb.toString().trim();
    }

    private boolean validateCountrySeedDto(CountrySeedDTO dto, StringBuilder sb) {
        boolean isValid;

        if(countryRepository.existsByName(dto.getCountryName())) {
            isValid = false;
        } else {
            isValid = validationUtil.isValid(dto);
        }
        sb.append(isValid ? String.format("Successfully imported country %s - %s",dto.getCountryName(), dto.getCurrency()) : "Invalid country").append(System.lineSeparator());
        return isValid;
    }

    @Override
    public Country findById(Long id) {
        return countryRepository.findById(id).orElse(null);
    }
}
