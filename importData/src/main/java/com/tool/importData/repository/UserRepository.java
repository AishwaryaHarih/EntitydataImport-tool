package com.tool.importData.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.tool.importData.entity.User;

public interface UserRepository extends MongoRepository<User, String> {

    boolean existsByEmail(String email);
}
