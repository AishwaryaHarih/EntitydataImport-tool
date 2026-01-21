package com.tool.importData.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.tool.importData.constants.ToolConstants;
import com.tool.importData.entity.ImportError;
import com.tool.importData.entity.ImportStatusEnum;
import com.tool.importData.entity.JobImport;
import com.tool.importData.processor.EntityImportprocessor;
import com.tool.importData.registry.ImportDataProcessRegistry;
import com.tool.importData.repository.DataImportRepository;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DataImportService {

	private final ImportDataProcessRegistry processorRegistry;
	private final DataImportRepository jobRepository;

	public JobImport doCsvImport(String entityType, MultipartFile file) {
		JobImport importJob = createJobImport(entityType, file);

		try {
			importJob.setStatus(ImportStatusEnum.PROCESSING);
			importJob = jobRepository.save(importJob);
			EntityImportprocessor processor = processorRegistry.getImportProcessor(entityType);
			processCsvFile(file, processor, importJob);
			importJob.setCompletedDateTime(Instant.now());
			importJob.setStatus(
					importJob.getProcessedRecords() == 0 ? ImportStatusEnum.FAILED : ImportStatusEnum.COMPLETED);
		} catch (Exception exception) {
			failJob(importJob, exception);
		}
		return jobRepository.save(importJob);

	}

	private JobImport createJobImport(String entityType, MultipartFile file) {
		JobImport job = new JobImport();
	    job.setEntity(entityType);
	    job.setFileName(file.getOriginalFilename());
	    job.setStatus(ImportStatusEnum.PENDING);
	    job.setCreatedDateTime(Instant.now());
	    job = jobRepository.save(job);
	    job.setJobId(job.getId().toHexString());
	    return jobRepository.save(job);
	}

	private void processCsvFile(MultipartFile file, EntityImportprocessor processor, JobImport importJob)
			throws Exception {
		try (Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {
			CSVFormat csvFormat = CSVFormat.DEFAULT.builder().setHeader().setSkipHeaderRecord(true)
					.setIgnoreEmptyLines(true).setTrim(true).build();

			CSVParser parser = csvFormat.parse(reader);
			for (CSVRecord record : parser) {
				importJob.setTotalRecords(importJob.getTotalRecords() + 1);
				Map<String, Object> entityData = normalizeKeysToUppercase(record.toMap());
				try {
					processor.validateEntityData(entityData);
					processor.saveEntityData(entityData);
					importJob.setProcessedRecords(importJob.getProcessedRecords() + 1);
				} catch (Exception exceptionInRows) {
					importJob.addErrorMessage(new ImportError(record.getRecordNumber(), exceptionInRows.getMessage()));
				}
			}

		}

	}

	private void failJob(JobImport importJob, Exception exception) {
		importJob.setStatus(ImportStatusEnum.FAILED);
		importJob.setCompletedDateTime(Instant.now());
		importJob.addErrorMessage(new ImportError(0, ToolConstants.FAILED_IMPORT + exception.getMessage()));
	}

	public JobImport getImportJobs(String jobId) {
		return jobRepository.findByJobId(jobId)
	            .orElseThrow(() ->
	                    new RuntimeException(ToolConstants.JOB_NOT_FOUND + jobId));
	}
	private Map<String, Object> normalizeKeysToUppercase(Map<String, ?> input) {
	    Map<String, Object> normalized = new HashMap<>();
	    input.forEach((key, value) -> {
	        if (key != null) {
	        	 String cleanKey = key
	                     .replace("\uFEFF", "")
	                     .trim()
	                     .toUpperCase();

	             normalized.put(cleanKey, value);
	        }
	    });
	    return normalized;
	}
	@PostConstruct
	public void testMongoConnection() {
	    long count = jobRepository.count();
	    System.out.println("MongoDB connected, job count: " + count);
	}



}
