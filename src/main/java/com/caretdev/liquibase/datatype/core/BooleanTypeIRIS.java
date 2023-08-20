package com.caretdev.liquibase.datatype.core;

import com.caretdev.liquibase.database.core.IRISDatabase;
import liquibase.database.Database;
import liquibase.datatype.DatabaseDataType;
import liquibase.datatype.core.BooleanType;

public class BooleanTypeIRIS extends BooleanType {

  @Override
  public int getPriority() {
    return PRIORITY_DATABASE;
  }

  @Override
  public boolean supports(Database database) {
    return database instanceof IRISDatabase;
  }

  @Override
  protected boolean isNumericBoolean(Database database) {
    return true;
  }

  @Override
  public DatabaseDataType toDatabaseDataType(Database database) {
    return new DatabaseDataType("BIT");
  }
}
