package com.caretdev.liquibase.database.core;

import liquibase.CatalogAndSchema;
import liquibase.database.AbstractJdbcDatabase;
import liquibase.database.DatabaseConnection;
import liquibase.exception.DatabaseException;
import liquibase.statement.DatabaseFunction;
import liquibase.statement.SqlStatement;
import liquibase.statement.core.RawSqlStatement;
import liquibase.structure.DatabaseObject;
import liquibase.structure.core.Table;
import liquibase.structure.core.View;

public class IRISDatabase extends AbstractJdbcDatabase {

  public IRISDatabase() {
    super.setCurrentDateTimeFunction("CURRENT_TIMESTAMP");

    this.dateFunctions.add(new DatabaseFunction("SYSDATE"));
    this.dateFunctions.add(new DatabaseFunction("CURRENT_DATE"));
    this.dateFunctions.add(new DatabaseFunction("CURRENT_TIME"));
    this.dateFunctions.add(new DatabaseFunction("CURRENT_TIMESTAMP"));
    this.dateFunctions.add(new DatabaseFunction("NOW"));
  }

  @Override
  protected String getDefaultDatabaseProductName() {
    return "InterSystems IRIS";
  }

  @Override
  public boolean isCorrectDatabaseImplementation(DatabaseConnection conn)
    throws DatabaseException {
    if ("InterSystems IRIS".equalsIgnoreCase(conn.getDatabaseProductName())) {
      return true;
    }
    return false;
  }

  @Override
  public String getDefaultDriver(String url) {
    if (url.startsWith("jdbc:IRIS:")) {
      return "com.intersystems.jdbc.IRISDriver";
    }
    return null;
  }

  @Override
  public String getShortName() {
    return "iris";
  }

  @Override
  public Integer getDefaultPort() {
    return 1972;
  }

  @Override
  public boolean supportsInitiallyDeferrableColumns() {
    return false;
  }

  @Override
  public int getPriority() {
    return PRIORITY_DEFAULT;
  }

  @Override
  public String getLineComment() {
    return "--";
  }

  @Override
  public boolean supportsSequences() {
    return false;
  }

  @Override
  public boolean supportsTablespaces() {
    return false;
  }

  @Override
  public boolean supportsAutoIncrement() {
    return true;
  }

  @Override
  protected String getAutoIncrementClause() {
    return "IDENTITY";
  }

  @Override
  public String getViewDefinition(CatalogAndSchema schema, String viewName)
    throws DatabaseException {
    String viewDefinition = super.getViewDefinition(schema, viewName);

    viewDefinition = viewDefinition.replaceAll("^\\([^)]*\\)\r?\n*", "");
    viewDefinition = viewDefinition.replaceAll("\\/\\*[^*]*\\*\\/", "");

    return viewDefinition;
  }

  @Override
  protected SqlStatement getConnectionSchemaNameCallStatement() {
    return new RawSqlStatement("SELECT %SYSTEM_SQL.DefaultSchema()");
  }

  @Override
  public boolean supportsDropTableCascadeConstraints() {
    return true;
  }

  @Override
  public boolean supportsCatalogs() {
    return true;
  }

  @Override
  public boolean isSystemObject(DatabaseObject example) {
    if (example == null) {
      return false;
    }
    if ((example.getSchema() != null) && (example.getSchema().getName() != null)) {
      String schemaName = example.getSchema().getName();
      if (
        "information_schema".equalsIgnoreCase(schemaName) ||
        schemaName.startsWith("%") ||
        schemaName.startsWith("Ens") ||
        "Ens".equalsIgnoreCase(schemaName)
      ) {
        return true;
      }
    }
    if ((example instanceof Table) && getSystemTables().contains(example.getName())) {
      return true;
    }

    return (example instanceof View) && getSystemViews().contains(example.getName());
  }

  @Override
  public String generateDatabaseFunctionValue(DatabaseFunction databaseFunction) {
    String code = databaseFunction.getValue();
    // Assume that ObjectScript code, called only during setup default values in create table or alter table
    if (code.startsWith("##") || code.startsWith("$")) {
      return "OBJECTSCRIPT '" + code + "' ";
    }
    return super.generateDatabaseFunctionValue(databaseFunction);
  }
}
