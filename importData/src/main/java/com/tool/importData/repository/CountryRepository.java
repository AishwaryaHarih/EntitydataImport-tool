package com.tool.importData.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.tool.importData.entity.Country;

@Repository
public interface CountryRepository extends MongoRepository<Country, String> {

}
