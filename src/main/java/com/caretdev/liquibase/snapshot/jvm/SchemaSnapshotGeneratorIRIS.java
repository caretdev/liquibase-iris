package com.caretdev.liquibase.snapshot.jvm;

import com.caretdev.liquibase.database.core.IRISDatabase;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import liquibase.CatalogAndSchema;
import liquibase.database.Database;
import liquibase.database.ObjectQuotingStrategy;
import liquibase.database.jvm.JdbcConnection;
import liquibase.diff.compare.DatabaseObjectComparatorFactory;
import liquibase.exception.DatabaseException;
import liquibase.snapshot.DatabaseSnapshot;
import liquibase.snapshot.InvalidExampleException;
import liquibase.snapshot.jvm.SchemaSnapshotGenerator;
import liquibase.structure.DatabaseObject;
import liquibase.structure.core.Catalog;
import liquibase.structure.core.Schema;
import liquibase.util.JdbcUtil;

public class SchemaSnapshotGeneratorIRIS extends SchemaSnapshotGenerator {

  @Override
  public int getPriority(Class<? extends DatabaseObject> objectType, Database database) {
    return database instanceof IRISDatabase ? PRIORITY_DATABASE : PRIORITY_NONE;
  }

  @Override
  protected String[] getDatabaseSchemaNames(Database database)
    throws SQLException, DatabaseException {
    List<String> returnList = new ArrayList<>();

    try (
      ResultSet schemas = ((JdbcConnection) database.getConnection()).getMetaData()
        .getSchemas()
    ) {
      while (schemas.next()) {
        String schemaName = JdbcUtil.getValueForColumn(schemas, "TABLE_SCHEM", database);
        // Skip System tables
        if (schemaName.startsWith("%") || schemaName.startsWith("Ens")) {
          continue;
        }
        returnList.add(schemaName);
      }
    }
    String defaultSchema = database.getDefaultSchemaName();
    if (!returnList.contains(defaultSchema)) {
      returnList.add(defaultSchema);
    }

    return returnList.toArray(new String[0]);
  }
}
