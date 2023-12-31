package com.caretdev.liquibase.snapshot.jvm;

import com.caretdev.liquibase.snapshot.ResultSetCacheIRIS;
import java.sql.SQLException;
import java.util.List;
import liquibase.CatalogAndSchema;
import liquibase.database.AbstractJdbcDatabase;
import liquibase.database.Database;
import liquibase.exception.DatabaseException;
import liquibase.snapshot.CachedRow;
import liquibase.snapshot.DatabaseSnapshot;
import liquibase.snapshot.ResultSetCache;
import liquibase.structure.core.Schema;

public class IRISResultSetConstraintsExtractor
  extends ResultSetCacheIRIS.SingleResultSetExtractor {

  private final Database database;
  private final String catalogName;
  private final String schemaName;
  private final String tableName;

  public IRISResultSetConstraintsExtractor(
    DatabaseSnapshot databaseSnapshot,
    String catalogName,
    String schemaName,
    String tableName
  ) {
    super(databaseSnapshot.getDatabase());
    this.database = databaseSnapshot.getDatabase();
    this.catalogName = catalogName;
    this.schemaName = schemaName;
    this.tableName = tableName;
  }

  @Override
  public boolean bulkContainsSchema(String schemaKey) {
    return false;
  }

  @Override
  public ResultSetCache.RowData rowKeyParameters(CachedRow row) {
    return new ResultSetCacheIRIS.RowData(
      catalogName,
      schemaName,
      database,
      row.getString("TABLE_NAME")
    );
  }

  @Override
  public ResultSetCache.RowData wantedKeyParameters() {
    return new ResultSetCacheIRIS.RowData(
      this.catalogName,
      this.schemaName,
      this.database,
      this.tableName
    );
  }

  @Override
  public List<CachedRow> fastFetchQuery() throws SQLException, DatabaseException {
    CatalogAndSchema catalogAndSchema = new CatalogAndSchema(
      this.catalogName,
      this.schemaName
    )
      .customize(this.database);

    return executeAndExtract(
      createSql(
        ((AbstractJdbcDatabase) this.database).getJdbcCatalogName(catalogAndSchema),
        ((AbstractJdbcDatabase) this.database).getJdbcSchemaName(catalogAndSchema),
        this.tableName
      ),
      this.database,
      false
    );
  }

  @Override
  public List<CachedRow> bulkFetchQuery() throws SQLException, DatabaseException {
    CatalogAndSchema catalogAndSchema = new CatalogAndSchema(
      this.catalogName,
      this.schemaName
    )
      .customize(this.database);

    return executeAndExtract(
      createSql(
        ((AbstractJdbcDatabase) this.database).getJdbcCatalogName(catalogAndSchema),
        ((AbstractJdbcDatabase) this.database).getJdbcSchemaName(catalogAndSchema),
        null
      ),
      this.database
    );
  }

  private String createSql(String catalog, String schema, String table) {
    CatalogAndSchema catalogAndSchema = new CatalogAndSchema(catalog, schema)
      .customize(this.database);

    String jdbcSchemaName =
      this.database.correctObjectName(
          ((AbstractJdbcDatabase) this.database).getJdbcSchemaName(catalogAndSchema),
          Schema.class
        );

    String sql =
      "select CONSTRAINT_NAME, CONSTRAINT_TYPE, TABLE_NAME from " +
      this.database.getSystemSchema() +
      ".TABLE_CONSTRAINTS " +
      "where TABLE_SCHEMA='" +
      jdbcSchemaName +
      "' and CONSTRAINT_TYPE='UNIQUE'";
    if (table != null) {
      sql += " and TABLE_NAME='" + table + "'";
    }

    return sql;
  }
}
