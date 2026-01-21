package com.tool.importData.processor;

import java.util.Map;

public interface EntityImportprocessor {

	public String retriveEntityType();

	public void validateEntityData(Map<String, Object> entityData);

	public void saveEntityData(Map<String, Object> entitydata);

}
