package com.tool.importData.registry;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

import com.tool.importData.constants.ToolConstants;
import com.tool.importData.exception.ProcessorNotFoundException;
import com.tool.importData.processor.EntityImportprocessor;

@Component
public class ImportDataProcessRegistry {

	private final Map<String, EntityImportprocessor> processorMap;

	public ImportDataProcessRegistry(List<EntityImportprocessor> processorList) {
		Map<String, EntityImportprocessor> tempMap = processorList.stream()
				.collect(Collectors.toMap(process -> process.retriveEntityType().toUpperCase(), process -> process));
		this.processorMap = Collections.unmodifiableMap(tempMap);
	}

	public EntityImportprocessor getImportProcessor(String entityType) {
		EntityImportprocessor processor = processorMap.get(entityType.toUpperCase());
		if (processor == null) {
			new ProcessorNotFoundException(ToolConstants.ENTITY_NOT_FOUND + ": " + entityType);
		}
		return processor;
	}
	
	 public Set<String> getSupportedEntities() {
	        return processorMap.keySet();
	    }
}
