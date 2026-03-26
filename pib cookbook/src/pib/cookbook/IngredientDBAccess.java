package pib.cookbook;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;

public class IngredientDBAccess
{

  //inserts an ingreident into the databse with its macros and name. prepared statement is used to stop sql injection
  public void insertIngredient(String name, double protein, double carbs, double fats, double calories, int userID)
  {
    String sql = "INSERT INTO Ingredient(IngredientName, ProteinPer100g, CarbsPer100g, FatsPer100g, CaloriesPer100g, UserID)"
      + "VALUES (?, ?, ?, ?, ?, ?)";

    try (Connection conn = DBManager.getDBConnection(); PreparedStatement ps = conn.prepareStatement(sql))
    {
      ps.setString(1, name);
      ps.setDouble(2, protein);
      ps.setDouble(3, carbs);
      ps.setDouble(4, fats);
      ps.setDouble(5, calories);
      ps.setInt(6, userID);

      ps.executeUpdate();
      System.out.println("Ingredient inserted: " + name);
    }
    catch (SQLIntegrityConstraintViolationException e)//catches the exception that it already exists
    {
      System.out.println("Ingredient already exists in this recipe.");
    }
    catch (SQLException e)
    {
      System.out.println("Error inserting ingreidient.");
      e.printStackTrace();
    }
  }

  //shows all of the ingredients in the database
  public void viewAllIngredients()
  {
    String sql = "SELECT * FROM Ingredient";

    try (Connection conn = DBManager.getDBConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery())
    {
      System.out.println("INGREDIENT LIST");

      while (rs.next())
      {
        System.out.println(
          rs.getInt("IngredientID") + " | "
          + rs.getString("IngredientName") + " | "
          + rs.getDouble("ProteinPer100g") + " | "
          + rs.getDouble("CarbsPer100g") + " | "
          + rs.getDouble("FatsPer100g") + " | "
          + rs.getDouble("CaloriesPer100g")
        );
      }
    }
    catch (SQLException e)
    {
      System.err.println("Error retreiving ingredients");
      e.printStackTrace();
    }
  }

  //updates existing ingredients and their macro values. identified using its primary key
  public void updateIngredient(int ingredientID, String name, double protein, double carbs, double fats, double calories, int userID)
  {
    String sql = "UPDATE Ingredient SET IngredientName = ?, ProteinPer100g = ?, CarbsPer100g = ?, FatsPer100g = ?, CaloriesPer100g = ? WHERE IngredientID = ? AND UserID = ?";

    try (Connection conn = DBManager.getDBConnection(); PreparedStatement ps = conn.prepareStatement(sql))
    {
      ps.setString(1, name);
      ps.setDouble(2, protein);
      ps.setDouble(3, carbs);
      ps.setDouble(4, fats);
      ps.setDouble(5, calories);
      ps.setInt(6, ingredientID);
      ps.setInt(7, userID);

      int rows = ps.executeUpdate();
      System.out.println("ingredients updated: " + rows);
    }
    catch (SQLException e)
    {
      System.out.println("error updating ingreident bruh.");
      e.printStackTrace();
    }
  }

  //deletes an ingredient from the database using its id. since ingredients are used in recipes, i foudnb out that i can use cascading delete to remove it from all recipes that the ingredient was in
  public void deleteINgredient(int ingredientID, int UserID)
  {
    String sql = "DELETE FROM Ingredient WHERE IngredientID = ? AND UserID = ?";

    try (Connection conn = DBManager.getDBConnection(); PreparedStatement ps = conn.prepareStatement(sql))
    {
      ps.setInt(1, ingredientID);
      ps.setInt(2, UserID);
      ps.executeUpdate();

      System.out.println("Ingredient deleted: Id " + ingredientID);
    }
    catch (SQLException e)
    {
      System.out.println("error deleting ingreidnet");
      e.printStackTrace();
    }
  }
  
  public ArrayList<Ingredient> getAllIngredients(int userID)
  {
    ArrayList<Ingredient> ingredients = new ArrayList<>();
    String sql = "SELECT IngredientID, IngredientName FROM Ingredient WHERE UserID = ?";
    
    try (Connection conn = DBManager.getDBConnection();
      PreparedStatement ps = conn.prepareStatement(sql))
    {
      ps.setInt(1, userID);
      
      ResultSet rs = ps.executeQuery();
      while(rs.next())
      {
        ingredients.add(new Ingredient(rs.getInt("IngredientID"), rs.getString("IngredientName")));
      }
    }
    catch (SQLException e)
    {
      e.printStackTrace();
    }
    return ingredients;
  }
  
  public ArrayList<Ingredient> searchIngredients(String word, int UserID)
  {
    ArrayList<Ingredient> ingredients = new ArrayList<>();
    String sql = "SELECT IngredientID, IngredientName FROM Ingredient WHERE IngredientName LIKE ? AND UserID = ?";
    
    try (Connection conn = DBManager.getDBConnection();
      PreparedStatement ps = conn.prepareStatement(sql))
    {
      ps.setString(1, "%" + word + "%");
      ps.setInt(2, UserID);
      ResultSet rs = ps.executeQuery();
      
      while(rs.next())
      {
        ingredients.add(new Ingredient(rs.getInt("IngredientID"), rs.getString("IngredientName")));
      }
    }
    catch(SQLException e)
    {
      e.printStackTrace();
    }
    return ingredients;
  }
  
  public Ingredient getIngredientInfo(int ingredientID, int userID)
  {
    String sql = "SELECT * FROM Ingredient WHERE IngredientID = ? AND UserID = ?";
    
    try (Connection conn = DBManager.getDBConnection();
      PreparedStatement ps = conn.prepareStatement(sql))
    {
      ps.setInt(1, ingredientID);
      ps.setInt(2, userID);
      ResultSet rs = ps.executeQuery();
      
      if(rs.next())
      {
        return new Ingredient(
          rs.getInt("IngredientID"), 
        rs.getString("IngredientName"), 
        rs.getInt("ProteinPer100g"), 
        rs.getInt("CarbsPer100g"), 
        rs.getInt("FatsPer100g"), 
        rs.getInt("CaloriesPer100g"),
        rs.getInt("UserID"));
      }
    }
    catch(SQLException e)
    {
      e.printStackTrace();
    }
    return null;
  }

  public static void main(String[] args)
  {
    IngredientDBAccess ingredientDB = new IngredientDBAccess();

//    ingredientDB.insertIngredient("Brocolli", 2.8, 7, 0.4, 34);
//    ingredientDB.insertIngredient("Chicken Breast", 31, 0, 3.6, 165);
//    ingredientDB.insertIngredient("Poo", 67, 67, 67, 67);
//    
//    ingredientDB.viewAllIngredients();
//    
//    ingredientDB.updateIngredient(1, 3, 6.5, 0.5, 35);
//    
//    ingredientDB.deleteINgredient(3);
//    
//    ingredientDB.viewAllIngredients();
//    ingredientDB.insertIngredient("Lettuce", 1.2, 3, 0.1, 15);
//    ingredientDB.insertIngredient("Arugula", 2.6, 3.7, 0.7, 25);
//    ingredientDB.insertIngredient("Tomato", 0.9, 4, 0.2, 19);
  }
}
