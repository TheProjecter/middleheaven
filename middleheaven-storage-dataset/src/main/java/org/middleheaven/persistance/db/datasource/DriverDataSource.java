package org.middleheaven.persistance.db.datasource;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;

public class DriverDataSource extends AbstractDataSource{

	private String URL;
	private String login;
	private String pass;

	public DriverDataSource(){}

    protected DriverDataSource(String driver,String url,String username,String password){

    	this.URL = url;
        this.login = username;
        this.pass = password;
        try {
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            throw new DriverNotFoundException("Driver " + driver + " was not found.");
        }
        
    }

    
	@Override
	public Connection getConnection() throws SQLException {
		Connection con = DriverManager.getConnection(URL, login, pass);
		con.setAutoCommit(isAutoCommit());
		return con;
	}

	@Override
	public Connection getConnection(String nlogin, String npass) throws SQLException {
		
		Connection con = DriverManager.getConnection(URL, nlogin, npass);
		con.setAutoCommit(isAutoCommit());
		return con;
	}

	protected String getURL() {
		return URL;
	}

	protected void setURL(String url) {
		URL = url;
	}

	protected String getLogin() {
		return login;
	}

	protected void setLogin(String login) {
		this.login = login;
	}

	protected String getPassword() {
		return pass;
	}

	protected void setPassword(String pass) {
		this.pass = pass;
	}



}
