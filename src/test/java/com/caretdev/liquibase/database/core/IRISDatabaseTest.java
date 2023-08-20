package com.caretdev.liquibase.database.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

public class IRISDatabaseTest {

  IRISDatabase database;

  @Before
  public void setup() {
    database = new IRISDatabase();
  }

  @Test
  public void testGetShortName() {
    assertEquals("iris", database.getShortName());
  }

  @Test
  public void testGetDefaultDatabaseProductName() {
    assertEquals("InterSystems IRIS", database.getDefaultDatabaseProductName());
  }

  @Test
  public void testGetDefaultDriver() {
    assertEquals(
      "com.intersystems.jdbc.IRISDriver",
      database.getDefaultDriver("jdbc:IRIS:")
    );
    assertNull(database.getDefaultDriver("jdbc:wrong-name:"));
  }
}
