package com.caretdev.liquibase.sqlgenerator.core;

import com.caretdev.liquibase.database.core.IRISDatabase;
import liquibase.database.Database;
import liquibase.exception.ValidationErrors;
import liquibase.sqlgenerator.SqlGeneratorChain;
import liquibase.sqlgenerator.core.RenameViewGenerator;
import liquibase.statement.core.RenameViewStatement;

public class RenameViewGeneratorIRIS extends RenameViewGenerator {

  @Override
  public int getPriority() {
    return PRIORITY_DATABASE;
  }

  @Override
  public boolean supports(RenameViewStatement statement, Database database) {
    return database instanceof IRISDatabase;
  }

  @Override
  public ValidationErrors validate(
    RenameViewStatement renameViewStatement,
    Database database,
    SqlGeneratorChain sqlGeneratorChain
  ) {
    ValidationErrors errors = new ValidationErrors();
    errors.addError("Rename view not supported on IRIS");
    return errors;
  }
}
