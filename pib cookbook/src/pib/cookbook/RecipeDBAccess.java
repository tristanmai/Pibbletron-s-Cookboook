package pib.cookbook;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;

public class RecipeDBAccess
{
  //inserts a recipe into the database
  public void insertRecipe(String name, String instructions, String description, int cookingTime)
  {
    //the attributes it is inserting into
    String sql = "INSERT INTO Recipe (RecipeName, Instructions, Description, CookingTime)"
      + "VALUES (?, ?, ?, ?)";

    //connectingf to the db
    try (Connection conn = DBManager.getDBConnection(); PreparedStatement ps = conn.prepareStatement(sql))
    {
      ps.setString(1, name);
      ps.setString(2, instructions);
      ps.setString(3, description);
      ps.setInt(4, cookingTime);

      ps.executeUpdate();
      System.out.println("Recipe inserted: " + name);
    }
    catch (SQLIntegrityConstraintViolationException e)//catches the expection that it was already created
    {
      System.out.println("Recipe already exists.");
    }
    catch (SQLException e)
    {
      System.out.println("error inserting recipe");
      e.printStackTrace();
    }
  }

  public void viewAllRecipes()
  {
    //uses select clause and * to see all the data
    String sql = "SELECT * FROM Recipe";

    //result set allows to read and output databases
    try (Connection conn = DBManager.getDBConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery())
    {
      System.out.println("RECIPE LIST");

      while (rs.next())//the rs.next moves the cursor from one cell to another to read through all of the data
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
  
  //updates the existing recipes by getting its recipe id and the updated version
  public void updateRecipe(int recipeID, String instructions, String description, int cookingTime)
  {
    //uses sql update clause and finding the recipe its recipe id
    String sql = "UPDATE Recipe SET "
      + "Instructions = ?,"
      + "Description = ?, "
      + "CookingTime = ? WHERE RecipeID = ?";
    
    try (Connection conn = DBManager.getDBConnection();
      PreparedStatement ps = conn.prepareStatement(sql))
    {
      //recieves the values for the cells i want to update
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
  
  //deletes a recipe
  public void deleteRecipe(int recipeID)
  {
    //uses delete from clause and searchuing by its recipeid
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
  
  public ArrayList<Recipe> getAllRecipes()
  {
    ArrayList<Recipe> recipes = new ArrayList<>();
    String sql = "SELECT RecipeID, RecipeName FROM Recipe";
    
    try (Connection conn = DBManager.getDBConnection();
      PreparedStatement ps = conn.prepareStatement(sql);
      ResultSet rs = ps.executeQuery())
    {
      while(rs.next())
      {
        recipes.add(new Recipe(rs.getInt("RecipeID"), rs.getString("RecipeName")));
      }
    }
    catch (SQLException e)
    {
      e.printStackTrace();
    }
    return recipes;
  }
  
  public ArrayList<Recipe> getRecipesSorted()
  {
    ArrayList<Recipe> recipes = new ArrayList<>();
    String sql = "SELECT RecipeID, RecipeName FROM Recipe ORDER BY CookingTime ASC";
    
    try (Connection conn = DBManager.getDBConnection();
      PreparedStatement ps = conn.prepareStatement(sql);
      ResultSet rs = ps.executeQuery())
    {
      while(rs.next())
      {
        recipes.add(new Recipe(rs.getInt("RecipeID"), rs.getString("RecipeName")));
      }
    }
    catch(SQLException e)
    {
      e.printStackTrace();
    }
    return recipes;
  }
  
  public ArrayList<Recipe> searchRecipes(String word)
  {
    ArrayList<Recipe> recipes = new ArrayList<>();
    String sql = "SELECT RecipeID, RecipeName FROM Recipe WHERE RecipeName LIKE ?";
    
    try (Connection conn = DBManager.getDBConnection();
      PreparedStatement ps = conn.prepareStatement(sql))
    {
      ps.setString(1, "%" + word + "%");
      ResultSet rs = ps.executeQuery();
      
      while(rs.next())
      {
        recipes.add(new Recipe(rs.getInt("RecipeID"), rs.getString("RecipeName")));
      }
    }
    catch(SQLException e)
    {
      e.printStackTrace();
    }
    return recipes;
  }
  
  public int getRecipeID(String recipeName)
  {
    String sql = "SELECT RecipeID FROM Recipe WHERE RecipeName = ?";
    
    try (Connection conn = DBManager.getDBConnection();
      PreparedStatement ps = conn.prepareStatement(sql))
    {
      ps.setString(1, recipeName);
      ResultSet rs = ps.executeQuery();
      
      if(rs.next())
      {
        return rs.getInt("RecipeID");
      }
    }
    catch(SQLException e)
    {
      e.printStackTrace();
    }
    return -1;
  }
  
  public static void main(String[] args)
  {
    RecipeDBAccess recipeDB = new RecipeDBAccess();
    
//    recipeDB.insertRecipe("Pho", "Step 1: Boil Broth\nStep 2: Add nooodles", "A yummy vietnamese soup", 30);
//    recipeDB.insertRecipe("Chicken Salad", "Step 1: Chop chicken\nStep 2: Mix with brocolli", "A nutritious chicken salad", 15);
//    
//    recipeDB.viewAllRecipes();
//    
//    recipeDB.deleteRecipe(1);
//    
//    recipeDB.viewAllRecipes();

    recipeDB.insertRecipe("Pizza", "Step 1: Make dough\nStep 2: Add toppings\nStep3: Bake", "A yummy pizza", 25);
  }
}
