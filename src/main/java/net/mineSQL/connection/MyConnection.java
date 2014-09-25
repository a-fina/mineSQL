package net.mineSQL.connection;

import java.sql.Array;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.NClob;
import java.sql.PreparedStatement;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Savepoint;
import java.sql.Statement;
import java.sql.Struct;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executor;

// Fare metodo stastico releaseConnection, che cancella l'associazione del thread corrente
// dalla has table

public class MyConnection implements Connection {
    private Connection con;

    MyConnection(Connection con) {
                this.con = con;
        }
    public Properties getClientInfo() throws SQLException {
        return this.con.getClientInfo();
    }
    public String getClientInfo(String name) throws SQLException {
        return this.con.getClientInfo(name);
    }
    public Array createArrayOf(String typeName, Object[] attributes) throws SQLException {
        return this.con.createArrayOf(typeName, attributes);
    }
    public Struct createStruct(String typeName, Object[] attributes) throws SQLException {
        return this.con.createStruct(typeName, attributes);
    }
        public void clearWarnings() throws SQLException {
                this.con.clearWarnings();
        }

        public void close() throws SQLException {
                this.con.close();
                ConnectionManager.releaseConnection(this);
        }

        public void
    setClientInfo(String name, String value) throws SQLClientInfoException {
                this.con.setClientInfo(name, value);
        }

        public void setClientInfo(Properties properties) throws SQLClientInfoException {
                this.con.setClientInfo(properties);
        }

        public void commit() throws SQLException {
                this.con.commit();
        }

        public Statement createStatement() throws SQLException {
                return this.con.createStatement();
        }

        public Statement createStatement(int resultSetType,
                        int resultSetConcurrency, int resultSetHoldability)
                        throws SQLException {
                return this.con.createStatement(resultSetType,
                                                                                                resultSetConcurrency,resultSetHoldability);
        }

        public Statement createStatement(int resultSetType,
                        int resultSetConcurrency) throws SQLException {
                return this.con.createStatement(resultSetType,resultSetConcurrency);
        }

        public int getHoldability() throws SQLException {
                // TODO Auto-generated method stub
                return this.con.getHoldability();
        }

        public DatabaseMetaData getMetaData() throws SQLException {
                // TODO Auto-generated method stub
                return this.con.getMetaData();
        }

        public int getTransactionIsolation() throws SQLException {
                // TODO Auto-generated method stub
                return this.con.getTransactionIsolation();
        }

        public Map getTypeMap() throws SQLException {
                // TODO Auto-generated method stub
                return this.con.getTypeMap();
        }

        public SQLWarning getWarnings() throws SQLException {
                // TODO Auto-generated method stub
                return this.con.getWarnings();
        }

        public boolean isClosed() throws SQLException {
                // TODO Auto-generated method stub
                return this.con.isClosed();
        }
        public boolean getAutoCommit() throws SQLException {
                return con.getAutoCommit();
        }
        public String getCatalog() throws SQLException {
                return con.getCatalog();
        }
        public boolean isReadOnly() throws SQLException {
                return con.isReadOnly();
        }
        public String nativeSQL(String sql) throws SQLException {
                return con.nativeSQL(sql);
        }
        public CallableStatement prepareCall(String sql, int resultSetType,
                        int resultSetConcurrency, int resultSetHoldability)
                        throws SQLException {
                return con.prepareCall(sql, resultSetType, resultSetConcurrency,
                                resultSetHoldability);
        }
        public CallableStatement prepareCall(String sql, int resultSetType,
                        int resultSetConcurrency) throws SQLException {
                return con.prepareCall(sql, resultSetType, resultSetConcurrency);
        }
        public CallableStatement prepareCall(String sql) throws SQLException {
                return con.prepareCall(sql);
        }
        public PreparedStatement prepareStatement(String sql, int resultSetType,
                        int resultSetConcurrency, int resultSetHoldability)
                        throws SQLException {
                return con.prepareStatement(sql, resultSetType, resultSetConcurrency,
                                resultSetHoldability);
        }
        public PreparedStatement prepareStatement(String sql, int resultSetType,
                        int resultSetConcurrency) throws SQLException {
                return con.prepareStatement(sql, resultSetType, resultSetConcurrency);
        }
        public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys)
                        throws SQLException {
                return con.prepareStatement(sql, autoGeneratedKeys);
        }
        public PreparedStatement prepareStatement(String sql, int[] columnIndexes)
                        throws SQLException {
                return con.prepareStatement(sql, columnIndexes);
        }
        public PreparedStatement prepareStatement(String sql, String[] columnNames)
                        throws SQLException {
                return con.prepareStatement(sql, columnNames);
        }
        public PreparedStatement prepareStatement(String sql) throws SQLException {
                return con.prepareStatement(sql);
        }
        public void releaseSavepoint(Savepoint savepoint) throws SQLException {
                con.releaseSavepoint(savepoint);
        }
        public void rollback() throws SQLException {
                con.rollback();
        }
        public void rollback(Savepoint savepoint) throws SQLException {
                con.rollback(savepoint);
        }
        public void setAutoCommit(boolean autoCommit) throws SQLException {
                con.setAutoCommit(autoCommit);
        }
        public void setCatalog(String catalog) throws SQLException {
                con.setCatalog(catalog);
        }
        public void setHoldability(int holdability) throws SQLException {
                con.setHoldability(holdability);
        }
        public void setReadOnly(boolean readOnly) throws SQLException {
                con.setReadOnly(readOnly);
        }
        public Savepoint setSavepoint() throws SQLException {
                return con.setSavepoint();
        }
        public Savepoint setSavepoint(String name) throws SQLException {
                return con.setSavepoint(name);
        }
        public void setTransactionIsolation(int level) throws SQLException {
                con.setTransactionIsolation(level);
        }
        public void setTypeMap1(Map map) throws SQLException {
                con.setTypeMap(map);
        }
		@Override
		public Blob createBlob() throws SQLException {
			// TODO Auto-generated method stub
			return null;
		}
		@Override
		public Clob createClob() throws SQLException {
			// TODO Auto-generated method stub
			return null;
		}
		@Override
		public NClob createNClob() throws SQLException {
			// TODO Auto-generated method stub
			return null;
		}
		@Override
		public SQLXML createSQLXML() throws SQLException {
			// TODO Auto-generated method stub
			return null;
		}
		@Override
		public boolean isValid(int timeout) throws SQLException {
			// TODO Auto-generated method stub
			return false;
		}
		@Override
		public void setTypeMap(Map<String, Class<?>> map) throws SQLException {
			// TODO Auto-generated method stub
			
		}
		@Override
		public boolean isWrapperFor(Class<?> arg0) throws SQLException {
			// TODO Auto-generated method stub
			return false;
		}
		@Override
		public <T> T unwrap(Class<T> arg0) throws SQLException {
			// TODO Auto-generated method stub
			return null;
		}

    @Override
    public void setSchema(String schema) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getSchema() throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void abort(Executor executor) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setNetworkTimeout(Executor executor, int milliseconds) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int getNetworkTimeout() throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }



}
