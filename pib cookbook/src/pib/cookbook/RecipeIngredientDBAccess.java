package pib.cookbook;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;

public class RecipeIngredientDBAccess
{

  //inserts an ingredient into a specific recipe with a specified weight for how much of that ingredient there is
  public void insertIngredientToRecipe(int recipeID, int ingredientID, double weight)
  {
    String sql = "INSERT INTO RecipeIngredient (RecipeID, IngredientID, WeightInGrams) "
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
    String sql = "SELECT i.IngredientName, ri.WeightInGrams "
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
        System.out.println(rs.getString("IngredientName") + " | " + rs.getDouble("WeightInGrams") + " g");
      }
    }
    catch (SQLException e)
    {
      System.out.println("error viewing ingredients for recipe");
      e.printStackTrace();
    }
  }

  //updates the weight of ingredients
  public void updateIngredientWeight(int recipeID, int ingredientID, double weight)
  {
    String sql = "UPDATE RecipeIngredient "
      + "SET WeightInGrams = ? "
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

  //deletes an ingredient from a recipe
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

  //this is an algorithm that i made to calculate the total macros of all ingredients in a recipe
  public double[] calculateRecipeMacros(int recipeID, int userID)
  {
    String sql = "SELECT i.ProteinPer100g, "
      + "i.CarbsPer100g, "
      + "i.FatsPer100g, "
      + "i.CaloriesPer100g, "
      + "ri.WeightInGrams "
      + "FROM RecipeIngredient ri " //again using ri for recipe ingredient
      + "JOIN Ingredient i ON ri.IngredientID = i.IngredientID " //joined on ingredient id
      + "JOIN Recipe r ON ri.RecipeID = r.RecipeID"
      + "WHERE ri.RecipeID = ? AND UserID = ?";

    //initialize macros
    double totalProtein = 0;
    double totalCarbs = 0;
    double totalFats = 0;
    double totalCalories = 0;

    try (Connection conn = DBManager.getDBConnection(); PreparedStatement ps = conn.prepareStatement(sql))
    {
      ps.setInt(1, recipeID);
      ps.setInt(2, userID);
      ResultSet rs = ps.executeQuery();

      while (rs.next())//use rs.next to go thru all the ingredients in the recipe
      {
        double weightFactor = rs.getDouble("WeightInGrams") / 100; //since i put the macros in each ingredient by 100g, i div by 100 to get macros per 1g
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
    };//manually input the total macros after calculating. i can then use this array to get protein, carb, fat, and calories
    return totalMacros;
  }

  public ArrayList<String> getIngredientDisplay(int recipeID, int userID)
  {
    ArrayList<String> list = new ArrayList<>();

    String sql = "SELECT i.IngredientName, ri.WeightInGrams "
      + "FROM RecipeIngredient ri "
      + "JOIN Ingredient i ON ri.IngredientID = i.IngredientID "
      + "JOIN Recipe r ON ri.RecipeID = r.RecipeID"
      + "WHERE ri.RecipeID = ? AND r.UserID = ?";

    try (Connection conn = DBManager.getDBConnection(); PreparedStatement ps = conn.prepareStatement(sql))
    {
      ps.setInt(1, recipeID);
      ps.setInt(2, userID);
      ResultSet rs = ps.executeQuery();

      while (rs.next())
      {
        list.add(rs.getString("IngredientName") + " - " + rs.getDouble("WeightInGrams") + " g");
      }
    }
    catch (SQLException e)
    {
      e.printStackTrace();
    }
    return list;
  }

  public ArrayList<Recipe> searchIngredientsInRecipe(String word, int userID)
  {
    ArrayList<Recipe> recipes = new ArrayList<>();

    String sql = "SELECT DISTINCT r.RecipeID, r.RecipeName "
      + "FROM Recipe r "
      + "JOIN RecipeIngredient ri ON r.RecipeID = ri.RecipeID "
      + "JOIN Ingredient i ON ri.IngredientID = i.IngredientID "
      + "WHERE i.IngredientName LIKE ? AND r.UserID = ?";

    try (Connection conn = DBManager.getDBConnection(); PreparedStatement ps = conn.prepareStatement(sql))
    {
      ps.setString(1, "%" + word + "%");
      ps.setInt(2, userID);

      ResultSet rs = ps.executeQuery();

      while (rs.next())
      {
        recipes.add(new Recipe(
          rs.getInt("RecipeID"),
          rs.getString("RecipeName")
        ));
      }
    }
    catch (SQLException e)
    {
      e.printStackTrace();
    }

    return recipes;
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

//    double[] macros = riDB.calculateRecipeMacros(2);
//    System.out.println("Recipe 2 Macros\n"
//      + "Protein:" + macros[0]
//      + "g\nCarbs:" + macros[1]
//      + "g\nFats:" + macros[2]
//      + "g\nCalories:" + macros[3] + "kcal"); //print out the tota; macros. i chose not to implement it into the database directly and instead I will show the total macros on screen when doing the gui layer
  }
}
