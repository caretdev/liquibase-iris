package com.caretdev.liquibase.sqlgenerator.core;

import com.caretdev.liquibase.database.core.IRISDatabase;
import liquibase.database.Database;
import liquibase.sql.Sql;
import liquibase.sql.UnparsedSql;
import liquibase.sqlgenerator.SqlGeneratorChain;
import liquibase.sqlgenerator.core.AddAutoIncrementGenerator;
import liquibase.statement.core.AddAutoIncrementStatement;

public class AddAutoIncrementGeneratorIRIS extends AddAutoIncrementGenerator {

  @Override
  public int getPriority() {
    return PRIORITY_DATABASE;
  }

  @Override
  public boolean supports(AddAutoIncrementStatement statement, Database database) {
    return database instanceof IRISDatabase;
  }

  @Override
  public Sql[] generateSql(
    AddAutoIncrementStatement statement,
    Database database,
    SqlGeneratorChain sqlGeneratorChain
  ) {
    String sql =
      "ALTER TABLE " +
      database.escapeTableName(
        statement.getCatalogName(),
        statement.getSchemaName(),
        statement.getTableName()
      ) +
      " ALTER " +
      database.escapeColumnName(
        statement.getCatalogName(),
        statement.getSchemaName(),
        statement.getTableName(),
        statement.getColumnName()
      ) +
      " SERIAL";

    return new Sql[] { new UnparsedSql(sql, getAffectedColumn(statement)) };
  }
}
