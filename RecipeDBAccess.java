package pib.cookbook;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RecipeDBAccess
{

  public void insertRecipe(String name, String instructions, String description, int cookingTime)
  {
    String sql = "INSERT INTO Recipe (RecipeName, Instructions, Description, CookingTime)"
      + "VALUES (?, ?, ?, ?)";

    try (Connection conn = DBManager.getDBConnection(); PreparedStatement ps = conn.prepareStatement(sql))
    {
      ps.setString(1, name);
      ps.setString(2, instructions);
      ps.setString(3, description);
      ps.setInt(4, cookingTime);

      ps.executeUpdate();
      System.out.println("Recipe inserted: " + name);
    }
    catch (SQLException e)
    {
      System.out.println("error inserting recipe");
      e.printStackTrace();
    }
  }

  public void viewAllRecipes()
  {
    String sql = "SELECT * FROM Recipe";

    try (Connection conn = DBManager.getDBConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery())
    {
      System.out.println("RECIPE LIST");

      while (rs.next())
      {
        System.out.println(
          rs.getInt("RecipeID") + " | "
          + rs.getString("RecipeName") + " | "
          + rs.getString("Instructions") + " | "
          + rs.getString("Description") + " | "
          + rs.getInt("CookingTime")
        );
      }
    }
    catch (SQLException e)
    {
      System.out.println("Error retrieving recipe");
      e.printStackTrace();
    }
  }
  
  public void updateRecipe(int recipeID, String instructions, String description, int cookingTime)
  {
    String sql = "UPDATE Recipe SET "
      + "Instructions = ?,"
      + "Description = ?, "
      + "CookingTime = ? WHERE RecipeID = ?";
    
    try (Connection conn = DBManager.getDBConnection();
      PreparedStatement ps = conn.prepareStatement(sql))
    {
      ps.setString(1, instructions);
      ps.setString(2, description);
      ps.setInt(3, cookingTime);
      ps.setInt(4, recipeID);
      
      int rows = ps.executeUpdate();
      System.out.println("Recipes updates: " + rows);
    }
    catch (SQLException e)
    {
      System.out.println("error updating rcipe");
      e.printStackTrace();
    }
  }
  
  public void deleteRecipe(int recipeID)
  {
    String sql = "DELETE FROM Recipe WHERE RecipeID = ?";
    
    try (Connection conn = DBManager.getDBConnection();
      PreparedStatement ps = conn.prepareStatement(sql))
    {
      ps.setInt(1, recipeID);
      ps.executeUpdate();
      
      System.out.println("redipe deleted: ID " + recipeID);
    }
    catch (SQLException e)
    {
      System.out.println("error deleting recipe");
      e.printStackTrace();
    }
  }
  
  public static void main(String[] args)
  {
    RecipeDBAccess recipeDB = new RecipeDBAccess();
    
    recipeDB.insertRecipe("Pho", "Step 1: Boil Broth\nStep 2: Add nooodles", "A yummy vietnamese soup", 30);
    recipeDB.insertRecipe("Chicken Salad", "Step 1: Chop chicken\nStep 2: Mix with brocolli", "A nutritious chicken salad", 15);
    
    recipeDB.viewAllRecipes();
    
    recipeDB.deleteRecipe(1);
    
    recipeDB.viewAllRecipes();
  }
}
