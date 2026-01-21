package com.tool.importData.entity;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document("importMetadata")
public class JobImport {

	@Id
	private ObjectId id;
	private String jobId;
	private String entity;
	private String fileName;
	private ImportStatusEnum status;
	private int totalRecords;
	private int processedRecords;
	private Instant createdDateTime = Instant.now();
	private Instant completedDateTime = Instant.now();
	private List<ImportError> errorMessages = new ArrayList<>();

	public void addErrorMessage(ImportError error) {
		this.errorMessages.add(error);
	}

}
