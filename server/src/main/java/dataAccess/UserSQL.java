package dataAccess;
import model.UserData;
import dataAccess.DatabaseManager;
import com.google.gson.Gson;
import java.io.*;
import java.net.*;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import static dataAccess.DatabaseManager.createDatabase;

public class UserSQL implements UserDAO{

    public UserSQL(){
    }

    @Override
    public UserData getUser(String username) throws DataAccessException {
        return null;
    }

    @Override
    public String getPassword(String username) throws DataAccessException {
        return null;
    }

    @Override
    public void createUser(UserData user) throws DataAccessException {
        var statement = "INSERT INTO user (username, password, email, json) VALUES (?, ?, ?, ?)";
        var json = new Gson().toJson(statement);

    }

    @Override
    public void clear() throws DataAccessException {

    }
}
