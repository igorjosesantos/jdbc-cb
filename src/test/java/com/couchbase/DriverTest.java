
/*
 * //  Copyright (c) 2015 Couchbase, Inc.
 * //  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file
 * //  except in compliance with the License. You may obtain a copy of the License at
 * //    http://www.apache.org/licenses/LICENSE-2.0
 * //  Unless required by applicable law or agreed to in writing, software distributed under the
 * //  License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * //  either express or implied. See the License for the specific language governing permissions
 * //  and limitations under the License.
 */

package com.couchbase;

import com.couchbase.jdbc.TestUtil;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.SQLException;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Unit test for CouchBase JDBC Driver
 */
@RunWith(JUnit4.class)

public class DriverTest extends TestCase
{
    @Before
    public void setUp() throws Exception
    {

    }

    /*
      * Tests parseURL (internal)
      */
  /*
   * Tests the connect method by connecting to the test database
   */
    @org.junit.Test
    public void testConnect() throws Exception
    {

        Connection con = DriverManager.getConnection(TestUtil.getURL(), TestUtil.getUser(), TestUtil.getPassword());
        assertNotNull(con);
        con.close();
        try
        {
            con.createStatement();
            assertEquals("Connection should be unusable", false,true);
        }
        catch (SQLException ex)
        {
            assertEquals("Connection is closed", ex.getMessage());
        }
    }

    @org.junit.Test
    public void testBadConnect() throws Exception
    {
        Connection con = DriverManager.getConnection(TestUtil.getBadURL()+"/?connectionTimeout=1000", TestUtil.getUser(), TestUtil.getPassword());
        assertNotNull(con);
        assertEquals(true, con.isClosed());
        con.close();

    }
    public void getMetaData() throws Exception
    {
        Connection con = DriverManager.getConnection(TestUtil.getBadURL(), TestUtil.getUser(), TestUtil.getPassword());
        assertNotNull(con);
        DatabaseMetaData databaseMetaData = con.getMetaData();
        assertEquals(CBDriver.MAJOR_VERSION,databaseMetaData.getDriverMajorVersion());
        assertEquals(CBDriver.MINOR_VERSION,databaseMetaData.getDatabaseMinorVersion());
        assertEquals(4,databaseMetaData.getJDBCMajorVersion());
        assertEquals(1,databaseMetaData.getJDBCMinorVersion());
    }


}
