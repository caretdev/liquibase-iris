package com.caretdev.liquibase.sqlgenerator.core;

import com.caretdev.liquibase.database.core.IRISDatabase;
import liquibase.database.Database;
import liquibase.sql.Sql;
import liquibase.sql.UnparsedSql;
import liquibase.sqlgenerator.SqlGeneratorChain;
import liquibase.sqlgenerator.core.RenameColumnGenerator;
import liquibase.statement.core.RenameColumnStatement;

public class RenameColumnGeneratorIRIS extends RenameColumnGenerator {

  @Override
  public int getPriority() {
    return PRIORITY_DATABASE;
  }

  @Override
  public boolean supports(RenameColumnStatement statement, Database database) {
    return database instanceof IRISDatabase;
  }

  @Override
  public Sql[] generateSql(
    RenameColumnStatement statement,
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
      " ALTER COLUMN " +
      database.escapeColumnName(
        statement.getCatalogName(),
        statement.getSchemaName(),
        statement.getTableName(),
        statement.getOldColumnName()
      ) +
      " RENAME " +
      database.escapeColumnName(
        statement.getCatalogName(),
        statement.getSchemaName(),
        statement.getTableName(),
        statement.getNewColumnName()
      );

    return new Sql[] {
      new UnparsedSql(
        sql,
        getAffectedOldColumn(statement),
        getAffectedNewColumn(statement)
      ),
    };
  }
}
