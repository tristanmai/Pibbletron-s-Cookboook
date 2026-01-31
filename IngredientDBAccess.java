package pib.cookbook;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class IngredientDBAccess
{

  public void insertIngredient(String name, double protein, double carbs, double fats, double calories)
  {
    String sql = "INSERT INTO Ingredient(IngredientName, ProteinPer100g, CarbsPer100g, FatsPer100g, CaloriesPer100g)"
      + "VALUES (?, ?, ?, ?, ?)";

    try (Connection conn = DBManager.getDBConnection(); PreparedStatement ps = conn.prepareStatement(sql))
    {
      ps.setString(1, name);
      ps.setDouble(2, protein);
      ps.setDouble(3, carbs);
      ps.setDouble(4, fats);
      ps.setDouble(5, calories);

      ps.executeUpdate();
      System.out.println("Ingredient inserted: " + name);
    }
    catch (SQLException e)
    {
      System.out.println("Error inserting ingreidient.");
      e.printStackTrace();
    }
  }

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
  
  public void updateIngredient(int ingredientID, double protein, double carbs, double fats, double calories)
  {
    String sql = "UPDATE Ingredient SET ProteinPer100g = ?, CarbsPer100g = ?, FatsPer100g = ?, CaloriesPer100g = ? WHERE IngredientID = ?";
    
    try (Connection conn = DBManager.getDBConnection();
      PreparedStatement ps = conn.prepareStatement(sql))
    {
      ps.setDouble(1, protein);
      ps.setDouble(2, carbs);
      ps.setDouble(3, fats);
      ps.setDouble(4, calories);
      ps.setInt(5, ingredientID);
      
      int rows = ps.executeUpdate();
      System.out.println("ingredients updated: " + rows);
    }
    catch (SQLException e)
    {
      System.out.println("error updating ingreident bruh.");
      e.printStackTrace();
    }
  }
  
  public void deleteINgredient(int ingredientID)
  {
    String sql = "DELETE FROM Ingredient WHERE IngredientID = ?";
    
    try (Connection conn = DBManager.getDBConnection();
      PreparedStatement ps = conn.prepareStatement(sql))
    {
      ps.setInt(1, ingredientID);
      ps.executeUpdate();
      
      System.out.println("Ingredient deleted: Id " +ingredientID);
    }
    catch (SQLException e)
    {
      System.out.println("error deleting ingreidnet");
      e.printStackTrace();
    }
  }
  
  public static void main (String[] args)
  {
    IngredientDBAccess ingredientDB = new IngredientDBAccess();
    
    ingredientDB.insertIngredient("Brocolli", 2.8, 7, 0.4, 34);
    ingredientDB.insertIngredient("Chicken Breast", 31, 0, 3.6, 165);
    ingredientDB.insertIngredient("Poo", 67, 67, 67, 67);
    
    ingredientDB.viewAllIngredients();
    
    ingredientDB.updateIngredient(1, 3, 6.5, 0.5, 35);
    
    ingredientDB.deleteINgredient(3);
    
    ingredientDB.viewAllIngredients();
  }
}
