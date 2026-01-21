package com.tool.importData.processor;

import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tool.importData.constants.ToolConstants;
import com.tool.importData.entity.Country;
import com.tool.importData.repository.CountryRepository;

@Component
public class CountryImportProcessor implements EntityImportprocessor {

	@Autowired
	CountryRepository countryRepo;

	@Override
	public String retriveEntityType() {
		return ToolConstants.COUNTRY;
	}

	@Override
	public void validateEntityData(Map<String, Object> entityData) {
		validateRequiredFields(entityData);
		validateCountryName(entityData.get(ToolConstants.COUNTRY_NAME.toUpperCase()));
		validateIscoCode(entityData.get(ToolConstants.ISO_CODE.toUpperCase()));
		validatePopulation(entityData.get(ToolConstants.POPULATION.toUpperCase()));
	}

	@Override
	public void saveEntityData(Map<String, Object> entityData) {
		Country country = new Country();
		country.setCountryName(entityData.get(ToolConstants.COUNTRY_NAME.toUpperCase()).toString());
		country.setIsoCode(entityData.get(ToolConstants.ISO_CODE.toUpperCase()).toString());
		country.setPopulation(entityData.get(ToolConstants.POPULATION.toUpperCase()).toString());
		System.out.println("Saving country: " + entityData);
		countryRepo.save(country);
		System.out.println("Saved country: " + country);

	}

	private void validateRequiredFields(Map<String, Object> entityData) {
		Optional.ofNullable(entityData)
				.filter(data -> data.containsKey(ToolConstants.COUNTRY_NAME.toUpperCase())
						&& data.containsKey(ToolConstants.ISO_CODE.toUpperCase())
						&& data.containsKey(ToolConstants.POPULATION.toUpperCase()))
				.orElseThrow(() -> new IllegalArgumentException(ToolConstants.REQUIRED_COUNTRY_FIELDS_MISSING));

	}

	private void validateCountryName(Object countryName) {
		Optional.ofNullable(countryName).map(Object::toString).map(String::trim)
				.filter(name -> name.length() < ToolConstants.MAX_LENGTH_COUNTRY_NAME && name.matches("[a-zA-Z .'-]+"))
				.orElseThrow(() -> new IllegalArgumentException(ToolConstants.INVALID_COUNTRY_NAME));
	}

	private void validateIscoCode(Object isoCode) {
		Optional.ofNullable(isoCode).map(Object::toString).map(String::trim)
				.filter(code -> code.length() == 2 && code.matches("[a-zA-z]{2}"))
				.orElseThrow(() -> new IllegalArgumentException(ToolConstants.ISO_CODE_VALIDATION));
	}

	private void validatePopulation(Object population) {
		Optional.ofNullable(population).map(Object::toString).map(String::trim).map(populations -> {
			try {
				return Long.parseLong(populations);
			} catch (NumberFormatException e) {
				return null;
			}
		}).filter(CountryPopulation -> CountryPopulation != null && CountryPopulation >= 0)
				.orElseThrow(() -> new IllegalArgumentException(ToolConstants.POPULATION_MUST_BE_POSITIVE_INTEGER));
	}

}
