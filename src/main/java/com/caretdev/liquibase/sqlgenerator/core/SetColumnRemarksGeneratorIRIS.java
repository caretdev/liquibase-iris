package com.caretdev.liquibase.sqlgenerator.core;

import liquibase.database.Database;
import liquibase.exception.ValidationErrors;
import liquibase.sqlgenerator.SqlGeneratorChain;
import liquibase.sqlgenerator.core.SetColumnRemarksGenerator;
import liquibase.statement.core.SetColumnRemarksStatement;

public class SetColumnRemarksGeneratorIRIS extends SetColumnRemarksGenerator {

  @Override
  public int getPriority() {
    return PRIORITY_DATABASE;
  }

  @Override
  public boolean supports(SetColumnRemarksStatement statement, Database database) {
    return false;
    //        return database instanceof IRISDatabase;
  }

  @Override
  public ValidationErrors validate(
    SetColumnRemarksStatement setColumnRemarksStatement,
    Database database,
    SqlGeneratorChain sqlGeneratorChain
  ) {
    ValidationErrors errors = new ValidationErrors();
    errors.addError("Set remarks available only during add or create in IRIS");
    return errors;
  }
}
