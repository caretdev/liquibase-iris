package com.caretdev.liquibase.datatype.core;

import com.caretdev.liquibase.database.core.IRISDatabase;
import liquibase.database.Database;
import liquibase.datatype.DatabaseDataType;
import liquibase.datatype.core.DoubleType;

public class DoubleTypeIRIS extends DoubleType {

  @Override
  public int getPriority() {
    return PRIORITY_DATABASE;
  }

  @Override
  public boolean supports(Database database) {
    return database instanceof IRISDatabase;
  }

  @Override
  public DatabaseDataType toDatabaseDataType(Database database) {
    return new DatabaseDataType("FLOAT");
  }
}
