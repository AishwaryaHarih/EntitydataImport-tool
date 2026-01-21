package com.tool.importData.entity;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document("user")
public class User {

    private String username;
    private String email;
    private String role;
    private boolean active;

}

