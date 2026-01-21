package com.tool.importData.entity;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor  // Important for Swagger/OpenAPI
@AllArgsConstructor // Optional, convenient for creating objects
@Document("country")
public class Country {
	private String countryName;
	private String isoCode;
	private String population;

}
