package com.caretdev.liquibase.sqlgenerator.core;

import com.caretdev.liquibase.database.core.IRISDatabase;
import liquibase.database.Database;
import liquibase.sql.Sql;
import liquibase.sql.UnparsedSql;
import liquibase.sqlgenerator.SqlGeneratorChain;
import liquibase.sqlgenerator.core.RenameTableGenerator;
import liquibase.statement.core.RenameTableStatement;
import liquibase.structure.core.Table;

public class RenameTableGeneratorIRIS extends RenameTableGenerator {

  @Override
  public int getPriority() {
    return PRIORITY_DATABASE;
  }

  @Override
  public boolean supports(RenameTableStatement statement, Database database) {
    return database instanceof IRISDatabase;
  }

  @Override
  public Sql[] generateSql(
    RenameTableStatement statement,
    Database database,
    SqlGeneratorChain sqlGeneratorChain
  ) {
    String sql =
      "ALTER TABLE " +
      database.escapeTableName(
        statement.getCatalogName(),
        statement.getSchemaName(),
        statement.getOldTableName()
      ) +
      " RENAME " +
      database.escapeObjectName(statement.getNewTableName(), Table.class);

    return new Sql[] {
      new UnparsedSql(
        sql,
        getAffectedOldTable(statement),
        getAffectedNewTable(statement)
      ),
    };
  }
}
