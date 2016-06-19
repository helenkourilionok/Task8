package by.training.filmstore.dao.pool;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import by.training.filmstore.dao.poolexception.PoolConnectionException;

public class PoolConnection {
	private static Logger logger = LogManager.getLogger(PoolConnection.class);
	private BlockingQueue<Connection> availableConnections;
	private BlockingQueue<Connection> usedConnections;
	private static PoolConnection poolConnection;

	public static synchronized PoolConnection getInstance() throws PoolConnectionException {
		if (poolConnection == null) {
			poolConnection = new PoolConnection();
		}
		return poolConnection;
	}

	public Connection takeConnection() throws PoolConnectionException
	{
		Connection connection = null;
		try {
			connection = availableConnections.take();
			usedConnections.offer(connection);
		} catch (InterruptedException e) {
			logger.error("Can't take connection(InterruptedException)");
			throw new PoolConnectionException(e);
		}
		return connection;
	}
	
	public boolean putbackConnection(Connection connection) {
		boolean success = false;
		if (connection != null) {
			if(usedConnections.contains(connection))
			{
				availableConnections.offer(connection);
				usedConnections.remove(connection);
				success = true;
			}
		 }
		return success;
	}

	public void disposePoolConnection()
	{
		closeConnections(availableConnections);
		closeConnections(usedConnections);
		availableConnections.clear();
		usedConnections.clear();
	}

	private PoolConnection() throws PoolConnectionException
	{
			ResourceBundle bundle = ResourceBundle.getBundle(PoolConnectionSetting.RESOURCEPATH);
			String driver = bundle.getString(PoolConnectionSetting.DBDRIVER);
			String url = bundle.getString(PoolConnectionSetting.DBURL);
			String user = bundle.getString(PoolConnectionSetting.DBUSER);
			String password = bundle.getString(PoolConnectionSetting.DBPASSWORD);
			int minSize = Integer.parseInt(bundle.getString(PoolConnectionSetting.POOLSIZE));
			initPoolConnection(driver, url, user, password, minSize);
	}
	
	private void initPoolConnection(String driver,String url,String user,String password,int minSize) throws PoolConnectionException
	{
		try {
			Class.forName(driver);
			availableConnections = new ArrayBlockingQueue<Connection>(minSize);
			usedConnections = new ArrayBlockingQueue<Connection>(minSize);
			for(int i = 0;i<minSize;i++)
			{
				Connection connection = DriverManager.getConnection(url,user,password);
				availableConnections.add(connection);
			}
		} catch (ClassNotFoundException e) {
			logger.error("Can't find database driver class");
			throw new PoolConnectionException("Can't find database driver class");
		} catch (SQLException e) {
			logger.error("Error creating database connection");
			throw new PoolConnectionException("Error creating database connection");
		}
	}
	
	private void closeConnections(BlockingQueue<Connection> connections)
	{
		Connection connection = null;
		while((connection = connections.poll())!=null)
		{
			try {
				if(!connection.getAutoCommit())
				{
					connection.commit();
				}
				connection.close();
			} catch (SQLException e) {
				logger.error("Error closing database connection");
			}
		}
		
	}
}
