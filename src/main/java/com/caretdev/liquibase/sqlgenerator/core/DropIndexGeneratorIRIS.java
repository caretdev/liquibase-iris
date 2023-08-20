package com.caretdev.liquibase.sqlgenerator.core;

import com.caretdev.liquibase.database.core.IRISDatabase;
import liquibase.database.Database;
import liquibase.sql.Sql;
import liquibase.sql.UnparsedSql;
import liquibase.sqlgenerator.SqlGeneratorChain;
import liquibase.sqlgenerator.core.DropIndexGenerator;
import liquibase.statement.core.DropIndexStatement;

public class DropIndexGeneratorIRIS extends DropIndexGenerator {

  @Override
  public int getPriority() {
    return PRIORITY_DATABASE;
  }

  @Override
  public boolean supports(DropIndexStatement statement, Database database) {
    return database instanceof IRISDatabase;
  }

  @Override
  public Sql[] generateSql(
    DropIndexStatement statement,
    Database database,
    SqlGeneratorChain sqlGeneratorChain
  ) {
    return new Sql[] {
      new UnparsedSql(
        "DROP INDEX " +
        database.escapeIndexName(null, null, statement.getIndexName()) +
        " ON " +
        database.escapeTableName(
          statement.getTableCatalogName(),
          statement.getTableSchemaName(),
          statement.getTableName()
        ),
        getAffectedIndex(statement)
      ),
    };
  }
}
