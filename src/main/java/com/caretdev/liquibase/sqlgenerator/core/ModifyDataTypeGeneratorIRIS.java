package com.caretdev.liquibase.sqlgenerator.core;

import com.caretdev.liquibase.database.core.IRISDatabase;
import liquibase.database.Database;
import liquibase.datatype.DataTypeFactory;
import liquibase.datatype.DatabaseDataType;
import liquibase.sql.Sql;
import liquibase.sql.UnparsedSql;
import liquibase.sqlgenerator.SqlGeneratorChain;
import liquibase.sqlgenerator.core.ModifyDataTypeGenerator;
import liquibase.statement.core.ModifyDataTypeStatement;

public class ModifyDataTypeGeneratorIRIS extends ModifyDataTypeGenerator {

  @Override
  public int getPriority() {
    return PRIORITY_DATABASE;
  }

  @Override
  public boolean supports(ModifyDataTypeStatement statement, Database database) {
    return database instanceof IRISDatabase;
  }

  @Override
  public Sql[] generateSql(
    ModifyDataTypeStatement statement,
    Database database,
    SqlGeneratorChain sqlGeneratorChain
  ) {
    String alterTable =
      "ALTER TABLE " +
      database.escapeTableName(
        statement.getCatalogName(),
        statement.getSchemaName(),
        statement.getTableName()
      );

    alterTable += " ALTER COLUMN ";

    String columnName = database.escapeColumnName(
      statement.getCatalogName(),
      statement.getSchemaName(),
      statement.getTableName(),
      statement.getColumnName()
    );
    alterTable += columnName + " ";

    DatabaseDataType newDataType = DataTypeFactory
      .getInstance()
      .fromDescription(statement.getNewDataType(), database)
      .toDatabaseDataType(database);
    alterTable += newDataType;

    return new Sql[] { new UnparsedSql(alterTable, getAffectedTable(statement)) };
  }
}
