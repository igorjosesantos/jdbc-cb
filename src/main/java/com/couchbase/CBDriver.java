
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

import java.lang.String;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.DriverPropertyInfo;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.Properties;
import java.util.logging.Logger;
import java.util.logging.Level;


public class CBDriver implements java.sql.Driver
{
    public static final Logger logger = Logger.getLogger(CBDriver.class.getName());

    public static final int MAJOR_VERSION = 0;

    public static final int MINOR_VERSION = 1;

    public static final String DRIVER_NAME = "Couchbase N1QL Driver";

    static CBDriver registered;

    static
    {
        try
        {
            registered = new CBDriver();
            java.sql.DriverManager.registerDriver(registered);
        }
        catch (SQLException e)
        {
            logger.log(Level.SEVERE, "Error registering driver", e);
        }


    }

    public CBDriver() throws SQLException
    {
        logger.log(Level.INFO,"called");
    }
    /**
     * Attempts to make a database connection to the given URL.
     * The driver should return "null" if it realizes it is the wrong kind
     * of driver to connect to the given URL.  This will be common, as when
     * the JDBC driver manager is asked to connect to a given URL it passes
     * the URL to each loaded driver in turn.
     * <p/>
     * <P>The driver should throw an <code>SQLException</code> if it is the right
     * driver to connect to the given URL but has trouble connecting to
     * the database.
     * <p/>
     * <P>The <code>java.util.Properties</code> argument can be used to pass
     * arbitrary string tag/value pairs as connection arguments.
     * Normally at least "user" and "password" properties should be
     * included in the <code>Properties</code> object.
     *
     * @param url  the URL of the database to which to connect
     * @param info a list of arbitrary string tag/value pairs as
     *             connection arguments. Normally at least a "user" and
     *             "password" property should be included.
     * @return a <code>Connection</code> object that represents a
     * connection to the URL
     * @throws java.sql.SQLException if a database access error occurs
     */
    @Override
    public Connection connect(String url, Properties info) throws SQLException
    {

        if (acceptsURL(url))
        {
            return new CBConnection(url, info);
        }
        else
        {
            return null;
        }
    }

    /**
     * Retrieves whether the driver thinks that it can open a connection
     * to the given URL.  Typically drivers will return <code>true</code> if they
     * understand the subprotocol specified in the URL and <code>false</code> if
     * they do not.
     *
     * @param url the URL of the database
     * @return <code>true</code> if this driver understands the given URL;
     * <code>false</code> otherwise
     * @throws java.sql.SQLException if a database access error occurs
     */
    @Override
    public boolean acceptsURL(String url) throws SQLException
    {
        return url.startsWith("jdbc:couchbase:");
    }

    /**
     * Gets information about the possible properties for this driver.
     * <p/>
     * The <code>getPropertyInfo</code> method is intended to allow a generic
     * GUI tool to discover what properties it should prompt
     * a human for in order to get
     * enough information to connect to a database.  Note that depending on
     * the values the human has supplied so far, additional values may become
     * necessary, so it may be necessary to iterate though several calls
     * to the <code>getPropertyInfo</code> method.
     *
     * @param url  the URL of the database to which to connect
     * @param info a proposed list of tag/value pairs that will be sent on
     *             connect open
     * @return an array of <code>DriverPropertyInfo</code> objects describing
     * possible properties.  This array may be an empty array if
     * no properties are required.
     * @throws java.sql.SQLException if a database access error occurs
     */
    @Override
    public DriverPropertyInfo[] getPropertyInfo(String url, Properties info) throws SQLException
    {
        return new DriverPropertyInfo[0];
    }

    /**
     * Retrieves the driver's major version number. Initially this should be 1.
     *
     * @return this driver's major version number
     */
    @Override
    public int getMajorVersion()
    {
        return 0;
    }

    /**
     * Gets the driver's minor version number. Initially this should be 0.
     *
     * @return this driver's minor version number
     */
    @Override
    public int getMinorVersion()
    {
        return 0;
    }

    /**
     * Reports whether this driver is a genuine JDBC
     * Compliant<sup><font size=-2>TM</font></sup> driver.
     * A driver may only report <code>true</code> here if it passes the JDBC
     * compliance tests; otherwise it is required to return <code>false</code>.
     * <p/>
     * JDBC compliance requires full support for the JDBC API and full support
     * for SQL 92 Entry Level.  It is expected that JDBC compliant drivers will
     * be available for all the major commercial databases.
     * <p/>
     * This method is not intended to encourage the development of non-JDBC
     * compliant drivers, but is a recognition of the fact that some vendors
     * are interested in using the JDBC API and framework for lightweight
     * databases that do not support full database functionality, or for
     * special databases such as document information retrieval where a SQL
     * implementation may not be feasible.
     *
     * @return <code>true</code> if this driver is JDBC Compliant; <code>false</code>
     * otherwise
     */
    @Override
    public boolean jdbcCompliant()
    {
        return false;
    }

    /**
     * Return the parent Logger of all the Loggers used by this driver. This
     * should be the Logger farthest from the root Logger that is
     * still an ancestor of all of the Loggers used by this driver. Configuring
     * this Logger will affect all of the log messages generated by the driver.
     * In the worst case, this may be the root Logger.
     *
     * @return the parent Logger for this driver
     * @throws java.sql.SQLFeatureNotSupportedException if the driver does not use <code>java.util.logging<code>.
     * @since 1.7
     */
    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException
    {
        return Logger.getLogger(CBDriver.class.getPackage().getName());
    }

    public static void cleanup()
    {
        if (registered != null)
        {
            try
            {
                DriverManager.deregisterDriver(registered);
            }
            catch (SQLException e)
            {
                logger.log(Level.WARNING, "Error deregistering driver", e);
            }
        }
    }
}