package com.tool.importData;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.HashMap;
import java.util.Map;

import com.tool.importData.constants.ToolConstants;
import com.tool.importData.entity.User;
import com.tool.importData.processor.UserImportProcessor;
import com.tool.importData.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class UserImportProcessorTest {

    @Mock
    private UserRepository userRepo;

    @InjectMocks
    private UserImportProcessor processor;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRetrieveEntityType() {
        assertEquals(ToolConstants.USER, processor.retriveEntityType());
    }

    @Test
    void testValidateAndSaveValidUser() {
        Map<String, Object> data = new HashMap<>();
        data.put(ToolConstants.USERNAME.toUpperCase(), "john_doe");
        data.put(ToolConstants.EMAIL.toUpperCase(), "john@example.com");
        data.put(ToolConstants.ROLE.toUpperCase(), "USER");
        data.put(ToolConstants.ACTIVE.toUpperCase(), "true");

        when(userRepo.existsByEmail("john@example.com")).thenReturn(false);

        assertDoesNotThrow(() -> processor.validateEntityData(data));

        processor.saveEntityData(data);
        verify(userRepo, times(1)).save(any(User.class));
    }

    @Test
    void testMissingRequiredFields() {
        Map<String, Object> data = new HashMap<>();
        data.put(ToolConstants.USERNAME.toUpperCase(), "john_doe");

        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> processor.validateEntityData(data));

        assertEquals(ToolConstants.REQUIRED_USER_FIELDS_MISSING, exception.getMessage());
    }

    @Test
    void testInvalidUsername() {
        Map<String, Object> data = new HashMap<>();
        data.put(ToolConstants.USERNAME.toUpperCase(), "john doe");
        data.put(ToolConstants.EMAIL.toUpperCase(), "john@example.com");
        data.put(ToolConstants.ROLE.toUpperCase(), "USER");

        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> processor.validateEntityData(data));

        assertEquals(ToolConstants.INVALID_USERNAME, exception.getMessage());
    }

    @Test
    void testInvalidEmailFormat() {
        Map<String, Object> data = new HashMap<>();
        data.put(ToolConstants.USERNAME.toUpperCase(), "john_doe");
        data.put(ToolConstants.EMAIL.toUpperCase(), "john.example.com");
        data.put(ToolConstants.ROLE.toUpperCase(), "USER");

        when(userRepo.existsByEmail("john.example.com")).thenReturn(false);

        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> processor.validateEntityData(data));

        assertEquals(ToolConstants.INVALID_EMAIL, exception.getMessage());
    }

    @Test
    void testEmailAlreadyExists() {
        Map<String, Object> data = new HashMap<>();
        data.put(ToolConstants.USERNAME.toUpperCase(), "john_doe");
        data.put(ToolConstants.EMAIL.toUpperCase(), "john@example.com");
        data.put(ToolConstants.ROLE.toUpperCase(), "USER");

        when(userRepo.existsByEmail("john@example.com")).thenReturn(true);

        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> processor.validateEntityData(data));

        assertTrue(exception.getMessage().contains(ToolConstants.USER_ALREADY_EXISTS));
    }

    @Test
    void testInvalidRole() {
        Map<String, Object> data = new HashMap<>();
        data.put(ToolConstants.USERNAME.toUpperCase(), "john_doe");
        data.put(ToolConstants.EMAIL.toUpperCase(), "john@example.com");
        data.put(ToolConstants.ROLE.toUpperCase(), "GUEST");

        when(userRepo.existsByEmail("john@example.com")).thenReturn(false);

        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> processor.validateEntityData(data));

        assertEquals(ToolConstants.INVALID_ROLE, exception.getMessage());
    }

    @Test
    void testInvalidActiveValue() {
        Map<String, Object> data = new HashMap<>();
        data.put(ToolConstants.USERNAME.toUpperCase(), "john_doe");
        data.put(ToolConstants.EMAIL.toUpperCase(), "john@example.com");
        data.put(ToolConstants.ROLE.toUpperCase(), "USER");
        data.put(ToolConstants.ACTIVE.toUpperCase(), "maybe");

        when(userRepo.existsByEmail("john@example.com")).thenReturn(false);

        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> processor.validateEntityData(data));

        assertEquals(ToolConstants.ACTIVE_MUST_BE_TRUEORFALSE, exception.getMessage());
    }
}

