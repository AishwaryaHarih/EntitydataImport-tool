package com.tool.importData.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.tool.importData.entity.JobImport;

import java.util.Optional;

@Repository
public interface DataImportRepository extends MongoRepository<JobImport, ObjectId> {

	Optional<JobImport> findByJobId(String jobId);
}
