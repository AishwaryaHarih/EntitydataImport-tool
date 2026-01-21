package com.tool.importData.processor;

import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tool.importData.constants.ToolConstants;
import com.tool.importData.entity.User;
import com.tool.importData.repository.UserRepository;

@Component
public class UserImportProcessor implements EntityImportprocessor {

    @Autowired
    UserRepository userRepo;

    @Override
    public String retriveEntityType() {
        return ToolConstants.USER;
    }

    @Override
    public void validateEntityData(Map<String, Object> entityData) {
        validateRequiredFields(entityData);
        validateUsername(entityData.get(ToolConstants.USERNAME.toUpperCase()));
        validateEmail(entityData.get(ToolConstants.EMAIL.toUpperCase()));
        validateRole(entityData.get(ToolConstants.ROLE.toUpperCase()));
        validateActive(entityData.get(ToolConstants.ACTIVE.toUpperCase()));
    }

    @Override
    public void saveEntityData(Map<String, Object> entityData) {

        User user = new User();
        user.setUsername(entityData.get(ToolConstants.USERNAME.toUpperCase()).toString());
        user.setEmail(entityData.get(ToolConstants.EMAIL.toUpperCase()).toString());
        user.setRole(entityData.get(ToolConstants.ROLE.toUpperCase()).toString());

        Object active = entityData.get(ToolConstants.ACTIVE.toUpperCase());
        user.setActive(active != null && Boolean.parseBoolean(active.toString()));

        System.out.println("Saving user: " + entityData);
        userRepo.save(user);
        System.out.println("Saved user: " + user);
    }

    private void validateRequiredFields(Map<String, Object> entityData) {
        Optional.ofNullable(entityData)
                .filter(data ->
                        data.containsKey(ToolConstants.USERNAME.toUpperCase()) &&
                        data.containsKey(ToolConstants.EMAIL.toUpperCase()) &&
                        data.containsKey(ToolConstants.ROLE.toUpperCase()))
                .orElseThrow(() ->
                        new IllegalArgumentException(ToolConstants.REQUIRED_USER_FIELDS_MISSING));
    }

    private void validateUsername(Object username) {
        Optional.ofNullable(username)
                .map(Object::toString)
                .map(String::trim)
                .filter(name ->
                        name.length() <= ToolConstants.MAX_LENGTH_USERNAME &&
                        name.matches("[a-zA-Z0-9._-]+"))
                .orElseThrow(() ->
                        new IllegalArgumentException(ToolConstants.INVALID_USERNAME));
    }

    private void validateEmail(Object email) {
        Optional.ofNullable(email)
                .map(Object::toString)
                .map(String::trim)
                .filter(mail ->
                        mail.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$"))
                .orElseThrow(() ->
                        new IllegalArgumentException(ToolConstants.INVALID_EMAIL));

        if (userRepo.existsByEmail(email.toString())) {
            throw new IllegalArgumentException(
                    ToolConstants.USER_ALREADY_EXISTS + email);
        }
    }

    private void validateRole(Object role) {
        Optional.ofNullable(role)
                .map(Object::toString)
                .map(String::trim)
                .filter(r ->
                        r.equalsIgnoreCase("ADMIN") ||
                        r.equalsIgnoreCase("USER"))
                .orElseThrow(() ->
                        new IllegalArgumentException(ToolConstants.INVALID_ROLE));
    }

    private void validateActive(Object active) {
        Optional.ofNullable(active)
                .map(Object::toString)
                .map(String::trim)
                .filter(val ->
                        val.equalsIgnoreCase("true") ||
                        val.equalsIgnoreCase("false"))
                .orElseThrow(() ->
                        new IllegalArgumentException(ToolConstants.ACTIVE_MUST_BE_TRUEORFALSE));
    }
}

