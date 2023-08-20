package com.caretdev.liquibase.snapshot.jvm;

import com.caretdev.liquibase.database.core.IRISDatabase;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import liquibase.Scope;
import liquibase.database.Database;
import liquibase.exception.DatabaseException;
import liquibase.executor.ExecutorService;
import liquibase.snapshot.CachedRow;
import liquibase.snapshot.DatabaseSnapshot;
import liquibase.snapshot.SnapshotGenerator;
import liquibase.snapshot.jvm.UniqueConstraintSnapshotGenerator;
import liquibase.statement.core.RawSqlStatement;
import liquibase.structure.DatabaseObject;
import liquibase.structure.core.Relation;
import liquibase.structure.core.Schema;
import liquibase.structure.core.Table;
import liquibase.structure.core.UniqueConstraint;

public class UniqueConstraintSnapshotGeneratorIRIS
  extends UniqueConstraintSnapshotGenerator {

  @Override
  public int getPriority(Class<? extends DatabaseObject> objectType, Database database) {
    if (database instanceof IRISDatabase) {
      return PRIORITY_DATABASE;
    } else {
      return PRIORITY_NONE;
    }
  }

  @Override
  public Class<? extends SnapshotGenerator>[] replaces() {
    return new Class[] { UniqueConstraintSnapshotGenerator.class };
  }

  @Override
  protected List<CachedRow> listConstraints(
    Table table,
    DatabaseSnapshot snapshot,
    Schema schema
  ) throws DatabaseException, SQLException {
    return new IRISResultSetConstraintsExtractor(
      snapshot,
      schema.getCatalogName(),
      schema.getName(),
      table.getName()
    )
      .fastFetch();
  }

  @Override
  protected List<Map<String, ?>> listColumns(
    UniqueConstraint example,
    Database database,
    DatabaseSnapshot snapshot
  ) throws DatabaseException {
    Relation table = example.getRelation();
    Schema schema = example.getSchema();
    String name = example.getName();

    String schemaName = database.correctObjectName(schema.getName(), Schema.class);
    String tableName = database.correctObjectName(table.getName(), Table.class);
    String constraintName = database.correctObjectName(name, UniqueConstraint.class);

    String sql =
      "select CONSTRAINT_NAME, COLUMN_NAME, constraint_schema as CONSTRAINT_CONTAINER " +
      "from information_schema.table_constraints " +
      "join information_schema.indexes on index_name=constraint_name " +
      "where constraint_type='UNIQUE' ";
    if (schemaName != null) {
      sql += "and constraint_schema='" + schemaName + "' ";
    }
    if (tableName != null) {
      sql += "and table_constraints.table_name='" + tableName + "' ";
    }
    if (constraintName != null) {
      sql += "and constraint_name='" + constraintName + "'";
    }

    List<Map<String, ?>> rows = Scope
      .getCurrentScope()
      .getSingleton(ExecutorService.class)
      .getExecutor("jdbc", database)
      .queryForList(new RawSqlStatement(sql));

    return rows;
  }
}
