package com.caretdev.liquibase.snapshot.jvm;

import com.caretdev.liquibase.database.core.IRISDatabase;
import liquibase.database.Database;
import liquibase.exception.DatabaseException;
import liquibase.snapshot.DatabaseSnapshot;
import liquibase.snapshot.InvalidExampleException;
import liquibase.snapshot.jvm.IndexSnapshotGenerator;
import liquibase.structure.DatabaseObject;
import liquibase.structure.core.Index;
import liquibase.structure.core.Relation;

public class IndexSnapshotGeneratorIRIS extends IndexSnapshotGenerator {

  @Override
  public int getPriority(Class<? extends DatabaseObject> objectType, Database database) {
    return database instanceof IRISDatabase ? PRIORITY_DATABASE : PRIORITY_NONE;
  }

  @Override
  protected void addTo(DatabaseObject foundObject, DatabaseSnapshot snapshot)
    throws DatabaseException, InvalidExampleException {
    super.addTo(foundObject, snapshot);

    // Ignore CLUSTERED from the server, it does mean anything there
    if (foundObject instanceof Relation) {
      Relation relation = (Relation) foundObject;
      for (Index index : relation.getIndexes()) {
        if (index.getClustered() != null && index.getClustered()) {
          index.setClustered(null);
        }
      }
    }
  }

  @Override
  protected DatabaseObject snapshotObject(
    DatabaseObject example,
    DatabaseSnapshot snapshot
  ) throws DatabaseException, InvalidExampleException {
    DatabaseObject snapshotObject = super.snapshotObject(example, snapshot);

    // Ignore CLUSTERED from the server, it does mean anything there
    if (snapshotObject instanceof Index) {
      Index index = (Index) snapshotObject;
      if (index.getClustered() != null && index.getClustered()) {
        index.setClustered(null);
      }
    }
    return snapshotObject;
  }
}
