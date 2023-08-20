package com.caretdev.liquibase.snapshot.jvm;

import com.caretdev.liquibase.database.core.IRISDatabase;
import java.sql.SQLException;
import liquibase.database.Database;
import liquibase.exception.DatabaseException;
import liquibase.snapshot.CachedRow;
import liquibase.snapshot.SnapshotGenerator;
import liquibase.snapshot.jvm.ColumnSnapshotGenerator;
import liquibase.structure.DatabaseObject;
import liquibase.structure.core.Column;
import liquibase.structure.core.Relation;
import liquibase.util.SqlUtil;

public class ColumnSnapshotGeneratorIRIS extends ColumnSnapshotGenerator {

  @Override
  public int getPriority(Class<? extends DatabaseObject> objectType, Database database) {
    return database instanceof IRISDatabase ? PRIORITY_DATABASE : PRIORITY_NONE;
  }

  @Override
  public Class<? extends SnapshotGenerator>[] replaces() {
    return new Class[] { ColumnSnapshotGenerator.class };
  }

  @Override
  protected Column readColumn(
    CachedRow columnMetadataResultSet,
    Relation table,
    Database database
  ) throws SQLException, DatabaseException {
    Column column = super.readColumn(columnMetadataResultSet, table, database);
    // IRIS always returns Remarks equals to column name if it's supposed to be null
    if (column.getRemarks() != null && column.getRemarks().equals(column.getName())) {
      column.setRemarks(null);
    }
    return column;
  }

  @Override
  protected Object readDefaultValue(
    CachedRow columnMetadataResultSet,
    Column columnInfo,
    Database database
  ) {
    if (columnInfo.getAutoIncrementInformation() != null) {
      // AutoIncrement columns not requires defaults
      return null;
    }

    Object defaultValue = columnMetadataResultSet.get(COLUMN_DEF_COL);
    if (!(defaultValue instanceof String)) {
      return defaultValue;
    }
    String defaultValueStr = (String) defaultValue;
    if (defaultValueStr.startsWith("\"") && defaultValueStr.endsWith("\"")) {
      return SqlUtil.parseValue(
        database,
        defaultValueStr.substring(1, defaultValueStr.length() - 1),
        columnInfo.getType()
      );
    }

    Object parsedDefaultValue = SqlUtil.parseValue(
      database,
      defaultValueStr,
      columnInfo.getType()
    );

    return parsedDefaultValue;
  }
}
