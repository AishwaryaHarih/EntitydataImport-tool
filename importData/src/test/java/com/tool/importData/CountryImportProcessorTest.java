package com.tool.importData;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.HashMap;
import java.util.Map;

import com.tool.importData.constants.ToolConstants;
import com.tool.importData.entity.Country;
import com.tool.importData.processor.CountryImportProcessor;
import com.tool.importData.repository.CountryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class CountryImportProcessorTest {

    @Mock
    private CountryRepository countryRepo;

    @InjectMocks
    private CountryImportProcessor processor;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRetrieveEntityType() {
        assertEquals(ToolConstants.COUNTRY, processor.retriveEntityType());
    }

    @Test
    void testValidateAndSaveValidCountry() {
        Map<String, Object> data = new HashMap<>();
        data.put(ToolConstants.COUNTRY_NAME.toUpperCase(), "India");
        data.put(ToolConstants.ISO_CODE.toUpperCase(), "IN");
        data.put(ToolConstants.POPULATION.toUpperCase(), "1400000000");

        assertDoesNotThrow(() -> processor.validateEntityData(data));

        processor.saveEntityData(data);
        verify(countryRepo, times(1)).save(any(Country.class));
    }

    @Test
    void testMissingRequiredFields() {
        Map<String, Object> data = new HashMap<>();
        data.put(ToolConstants.COUNTRY_NAME.toUpperCase(), "India");

        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> processor.validateEntityData(data));

        assertEquals(ToolConstants.REQUIRED_COUNTRY_FIELDS_MISSING, exception.getMessage());
    }

    @Test
    void testInvalidCountryName() {
        Map<String, Object> data = new HashMap<>();
        data.put(ToolConstants.COUNTRY_NAME.toUpperCase(), "India123");
        data.put(ToolConstants.ISO_CODE.toUpperCase(), "IN");
        data.put(ToolConstants.POPULATION.toUpperCase(), "1000");

        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> processor.validateEntityData(data));

        assertEquals(ToolConstants.INVALID_COUNTRY_NAME, exception.getMessage());
    }

    @Test
    void testInvalidIsoCode() {
        Map<String, Object> data = new HashMap<>();
        data.put(ToolConstants.COUNTRY_NAME.toUpperCase(), "India");
        data.put(ToolConstants.ISO_CODE.toUpperCase(), "IND");
        data.put(ToolConstants.POPULATION.toUpperCase(), "1000");

        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> processor.validateEntityData(data));

        assertEquals(ToolConstants.ISO_CODE_VALIDATION, exception.getMessage());
    }

    @Test
    void testInvalidPopulation() {
        Map<String, Object> data = new HashMap<>();
        data.put(ToolConstants.COUNTRY_NAME.toUpperCase(), "India");
        data.put(ToolConstants.ISO_CODE.toUpperCase(), "IN");
        data.put(ToolConstants.POPULATION.toUpperCase(), "-100");

        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> processor.validateEntityData(data));

        assertEquals(ToolConstants.POPULATION_MUST_BE_POSITIVE_INTEGER, exception.getMessage());
    }
}
