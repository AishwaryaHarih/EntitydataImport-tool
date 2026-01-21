package com.tool.importData.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.tool.importData.entity.JobImport;
import com.tool.importData.service.DataImportService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/dataimport")
@RequiredArgsConstructor
@CrossOrigin
@Tag(name = "CSV Import", description = "Endpoints to import CSV files and track jobs")
public class DataImportController {

    private final DataImportService dataImportService;

    @Operation(summary = "Import CSV file for a given entity")
    @PostMapping(value = "/csv", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<JobImport> importCsvFile(
            @RequestParam("entityType") String entityType,
            @RequestPart("file") MultipartFile file) {

    	JobImport job = dataImportService.doCsvImport(entityType, file);
        return ResponseEntity.ok(job);
    }

    @Operation(summary = "Get details of a specific import job")
    @GetMapping("/details/{jobId}")
    public ResponseEntity<JobImport> getJobDetails(@PathVariable String jobId) {
        JobImport job = dataImportService.getImportJobs(jobId);
        return ResponseEntity.ok(job);
    }
}
