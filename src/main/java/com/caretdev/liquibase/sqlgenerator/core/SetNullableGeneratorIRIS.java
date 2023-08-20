package com.caretdev.liquibase.sqlgenerator.core;

import com.caretdev.liquibase.database.core.IRISDatabase;
import liquibase.database.Database;
import liquibase.sql.Sql;
import liquibase.sql.UnparsedSql;
import liquibase.sqlgenerator.SqlGeneratorChain;
import liquibase.sqlgenerator.core.SetNullableGenerator;
import liquibase.statement.core.SetNullableStatement;

public class SetNullableGeneratorIRIS extends SetNullableGenerator {

  @Override
  public int getPriority() {
    return PRIORITY_DATABASE;
  }

  @Override
  public boolean supports(SetNullableStatement statement, Database database) {
    return database instanceof IRISDatabase;
  }

  @Override
  public Sql[] generateSql(
    SetNullableStatement statement,
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
      " ALTER COLUMN  " +
      database.escapeColumnName(
        statement.getCatalogName(),
        statement.getSchemaName(),
        statement.getTableName(),
        statement.getColumnName()
      ) +
      (statement.isNullable() ? " NULL" : " NOT NULL");

    return new Sql[] { new UnparsedSql(sql, getAffectedColumn(statement)) };
  }
}
