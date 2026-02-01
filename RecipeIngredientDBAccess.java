package pib.cookbook;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

public class RecipeIngredientDBAccess
{

  public void insertIngredientToRecipe(int recipeID, int ingredientID, double weight)
  {
    String sql = "INSERT INTO RecipeIngredient (RecipeID, IngredientID, Weight) "
      + "VALUES (?, ?, ?)";

    try (Connection conn = DBManager.getDBConnection(); PreparedStatement ps = conn.prepareStatement(sql))
    {
      ps.setInt(1, recipeID);
      ps.setInt(2, ingredientID);
      ps.setDouble(3, weight);

      ps.executeUpdate();
      System.out.println("IngredientID " + ingredientID + " added to RecipeID " + recipeID);
    }
    catch (SQLIntegrityConstraintViolationException e)
    {
      System.out.println("Ingredient already exists in this recipe.");
    }
    catch (SQLException e)
    {
      System.out.println("error inserting ingrediient into recipe");
      e.printStackTrace();
    }
  }

  public void viewAllIngredientsForRecipe(int recipeID)
  {
    String sql = "SELECT i.IngredientName, ri.Weight "
      + "FROM RecipeIngredient ri " //the name to for recipeingredient is ri so i can acces its attributes with ri.
      + "JOIN Ingredient i " //the name i put for ingredient is i so when i access ingredients attributes i do i.
      + "ON ri.IngredientID = i.IngredientID " //i connect them by ingredient id in recipe ingredient and ingredient
      + "WHERE ri.RecipeID = ?"; //? means its determined by the user

    try (Connection conn = DBManager.getDBConnection(); PreparedStatement ps = conn.prepareStatement(sql))
    {
      ps.setInt(1, recipeID);
      ResultSet rs = ps.executeQuery();

      System.out.println("Ingredients for RecipeID " + recipeID + ":");
      while (rs.next())//since i do not know how many i have, the while loop lets me go thru all of it using the .next propety so it reads it all
      {
        System.out.println(rs.getString("IngredientName") + " | " + rs.getDouble("Weight") + " g");
      }
    }
    catch (SQLException e)
    {
      System.out.println("error viewing ingredients for recipe");
      e.printStackTrace();
    }
  }

  public void updateIngredientWeight(int recipeID, int ingredientID, double weight)
  {
    String sql = "UPDATE RecipeIngredient "
      + "SET Weight = ? "
      + "WHERE RecipeId = ? "
      + "AND IngredientID = ?";

    try (Connection conn = DBManager.getDBConnection(); PreparedStatement ps = conn.prepareStatement(sql))
    {
      ps.setDouble(1, weight);
      ps.setInt(2, recipeID);
      ps.setInt(3, ingredientID);

      int rows = ps.executeUpdate();
      System.out.println("Ingredient weight updated: " + rows + " rows");
    }
    catch (SQLException e)
    {
      System.out.println("error updating ingredient weight");
      e.printStackTrace();
    }
  }

  public void deleteIngredientFromRecipe(int recipeID, int ingredientID)
  {
    String sql = "DELETE FROM RecipeIngredient WHERE RecipeID = ? and IngredientID = ?";

    try (Connection conn = DBManager.getDBConnection(); PreparedStatement ps = conn.prepareStatement(sql))
    {
      ps.setInt(1, recipeID);
      ps.setInt(2, ingredientID);

      ps.executeUpdate();
      System.out.println("IngredientID " + ingredientID + " removed from RecipeID " + recipeID);
    }
    catch (SQLException e)
    {
      System.out.println("error deleting ingredient from recipe");
      e.printStackTrace();
    }
  }

  public double[] calculateRecipeMacros(int recipeID)
  {
    String sql = "SELECT i.ProteinPer100g, "
      + "i.CarbsPer100g, "
      + "i.FatsPer100g, "
      + "i.CaloriesPer100g, "
      + "ri.Weight "
      + "FROM RecipeIngredient ri " //again using ri for recipe ingredient
      + "JOIN Ingredient i " //and i for ingredient
      + "ON ri.IngredientID = i.IngredientID " //joined on ingredient id
      + "WHERE ri.RecipeID = ?";

    double totalProtein = 0;
    double totalCarbs = 0;
    double totalFats = 0;
    double totalCalories = 0;

    try (Connection conn = DBManager.getDBConnection(); PreparedStatement ps = conn.prepareStatement(sql))
    {
      ps.setInt(1, recipeID);
      ResultSet rs = ps.executeQuery();

      while (rs.next())
      {
        double weightFactor = rs.getDouble("Weight") / 100;
        totalProtein += rs.getDouble("ProteinPer100g") * weightFactor;
        totalCarbs += rs.getDouble("CarbsPer100g") * weightFactor;
        totalFats += rs.getDouble("FatsPer100g") * weightFactor;
        totalCalories += rs.getDouble("CaloriesPer100g") * weightFactor;
      }
    }
    catch (SQLException e)
    {
      System.out.println("Error clalculating recipe macros");
      e.printStackTrace();
    }

    double[] totalMacros =
    {
      totalProtein, totalCarbs, totalFats, totalCalories
    };
    return totalMacros;
  }

  public static void main(String[] args)
  {
    RecipeIngredientDBAccess riDB = new RecipeIngredientDBAccess();

//    riDB.insertIngredientToRecipe(2, 1, 100);
//    riDB.insertIngredientToRecipe(2, 2, 300);
//    riDB.insertIngredientToRecipe(2, 4, 250);
//    riDB.insertIngredientToRecipe(2, 5, 250);
//    riDB.insertIngredientToRecipe(2, 6, 100);
//
//    riDB.viewAllIngredientsForRecipe(2);
//
//    riDB.updateIngredientWeight(2, 1, 75);
//
//    riDB.deleteIngredientFromRecipe(2, 4);

    riDB.viewAllIngredientsForRecipe(2);

    double[] macros = riDB.calculateRecipeMacros(2);
    System.out.println("Recipe 2 Macros\n"
      + "Protein:" + macros[0]
      + "g\nCarbs:" + macros[1]
      + "g\nFats:" + macros[2]
      + "g\nCalories:" + macros[3] + "kcal");
  }
}
