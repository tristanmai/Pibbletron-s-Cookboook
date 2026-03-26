package pib.cookbook;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DBManager
{

  private static final String DB_NAME = "recipe_db";
  private static final String URL = "jdbc:mysql://localhost:3306/";
  private static final String USER = "root";
  private static final String PASSWORD = "mysql1";

  public static void initialize()
  {
    createDatabase();
    createTables();
  }

  public static Connection getServerConnection()
  {
    Connection conn = null;
    try
    {
      // load the mysql jdbc driver
      Class.forName("com.mysql.cj.jdbc.Driver");

      //attempt to connect to the database
      conn = DriverManager.getConnection(URL, USER, PASSWORD);
    }
    catch (ClassNotFoundException e)
    {
      System.err.println("JDBC Driver not found");
    }
    catch (SQLException e)
    {
      System.err.println("SQL Error while connecting: " + e.getMessage());
    }

    return conn;
  }

  public static Connection getDBConnection()
  {
    Connection conn = null;
    try
    {
      //load the mysql jdbc driver
      Class.forName("com.mysql.cj.jdbc.Driver");

      //attempt to connect to database
      conn = DriverManager.getConnection(URL + DB_NAME, USER, PASSWORD);
    }
    catch (ClassNotFoundException e)
    {
      System.err.println("JDBC Driver not found");
    }
    catch (SQLException e)
    {
      System.err.println("SQL Error while connecting: " + e.getMessage());
    }
    return conn;
  }

  public static void createDatabase()
  {
    Statement s;
    String sqlQuery = "CREATE DATABASE IF NOT EXISTS " + DB_NAME;

    try
    {
      Connection conn = getServerConnection();
      if (conn != null)
      {
        s = conn.createStatement();
        s.executeUpdate(sqlQuery);
        System.out.println("Database " + DB_NAME + " created or already exists.");
      }
      else
      {
        System.err.println("Database connection is null.");
      }
    }
    catch (SQLException se)
    {
      System.out.println("SQL Exception: " + se.getMessage());
    }
  }

  public static void createTables()
  {
    Connection conn = getDBConnection();

    if (conn == null)
    {
      System.err.println("Failed to connect to database. Tables not created.");
      return;
    }

    try (Statement s = conn.createStatement())
    {

      //talbe for users and their info
      s.execute("CREATE TABLE IF NOT EXISTS Users ("
        + "UserID INT AUTO_INCREMENT PRIMARY KEY, "
        + "Username VARCHAR(50) UNIQUE NOT NULL, "
        + "Password VARCHAR(200) NOT NULL"
        + ")");

      //table for ingredients and their macros
      s.execute("CREATE TABLE IF NOT EXISTS Ingredient ("
        + "IngredientID INT AUTO_INCREMENT PRIMARY KEY, "
        + "IngredientName VARCHAR(100) UNIQUE NOT NULL, "
        + "ProteinPer100g DOUBLE, "
        + "CarbsPer100g DOUBLE, "
        + "FatsPer100g DOUBLE, "
        + "CaloriesPer100g DOUBLE,"
        + "UserID INT NOT NULL, "
        + "FOREIGN KEY (UserID) REFERENCES Users(UserID) ON DELETE CASCADE, "
        + "UNIQUE (UserID, IngredientName)"
        + ")");

      //recipe table and its data
      s.execute("CREATE TABLE IF NOT EXISTS Recipe ("
        + "RecipeID INT AUTO_INCREMENT PRIMARY KEY, "
        + "RecipeName VARCHAR(100) NOT NULL, "
        + "Instructions TEXT, "
        + "Description TEXT, "
        + "CookingTime INT,"
        + "UserID INT NOT NULL, "
        + "FOREIGN KEY (UserID) REFERENCES Users(UserID) ON DELETE CASCADE, "
        + "UNIQUE (UserID, RecipeName)"
        + ")");

      //i created a composite key table to connect ingredients to recipes and their weights
      s.execute("CREATE TABLE IF NOT EXISTS RecipeIngredient ("
        + "RecipeID INT NOT NULL, "
        + "IngredientID INT NOT NULL, "
        + "WeightInGrams DOUBLE, "
        + "PRIMARY KEY (RecipeID, IngredientID), "
        + "FOREIGN KEY (RecipeID) REFERENCES Recipe(RecipeID) ON DELETE CASCADE, "
        + "FOREIGN KEY (IngredientID) REFERENCES Ingredient(IngredientID) ON DELETE CASCADE"
        + ")");

      //i created a favorittes table since i realised that if i made a favorites attribute in recipe then it would be favorited for all users
      s.execute("CREATE TABLE IF NOT EXISTS Favorite ("
        + "UserID INT NOT NULL, "
        + "RecipeID INT NOT NULL, "
        + "PRIMARY KEY (UserID, RecipeID), "
        + "FOREIGN KEY (UserID) REFERENCES Users(UserID) ON DELETE CASCADE, "
        + "FOREIGN KEY (RecipeID) REFERENCES Recipe(RecipeID) ON DELETE CASCADE"
        + ")");

      System.out.println("All tables created successfully.");

    }
    catch (SQLException e)
    {
      e.printStackTrace();
    }
  }

  public static void main(String[] args)
  {
    try
    {
      Class.forName("com.mysql.cj.jdbc.Driver");
      System.out.println("MySQL driver FOUND");
    }
    catch (ClassNotFoundException e)
    {
      System.out.println("MySQL driver NOT FOUND");
    }

    DBManager.initialize();
  }

}
