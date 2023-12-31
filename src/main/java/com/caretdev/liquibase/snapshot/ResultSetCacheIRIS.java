package com.caretdev.liquibase.snapshot;

import java.sql.SQLException;
import java.util.List;
import liquibase.database.Database;
import liquibase.exception.DatabaseException;
import liquibase.snapshot.CachedRow;
import liquibase.snapshot.ResultSetCache;

public class ResultSetCacheIRIS extends ResultSetCache {

  public static class RowData extends ResultSetCache.RowData {

    public RowData(
      String catalog,
      String schema,
      Database database,
      String... parameters
    ) {
      super(catalog, schema, database, parameters);
    }
  }

  public abstract static class SingleResultSetExtractor
    extends ResultSetCache.SingleResultSetExtractor {

    public SingleResultSetExtractor(Database database) {
      super(database);
    }

    protected boolean shouldBulkSelect(
      String schemaKey,
      ResultSetCacheIRIS resultSetCache
    ) {
      return super.shouldBulkSelect(schemaKey, resultSetCache);
    }

    @Override
    public List<CachedRow> executeAndExtract(String sql, Database database)
      throws DatabaseException, SQLException {
      return super.executeAndExtract(sql, database);
    }

    @Override
    public List<CachedRow> executeAndExtract(
      String sql,
      Database database,
      boolean informixTrimHint
    ) throws DatabaseException, SQLException {
      return super.executeAndExtract(sql, database, informixTrimHint);
    }
  }
}
