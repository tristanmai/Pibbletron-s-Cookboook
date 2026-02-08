package pib.cookbook;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class FavoriteDBAccess
{
  public void addFavorite(int userID, int recipeID)
  {
    //using ignore in the insert into clause lets the program silently skip a duplicate favorite since double favoriting isnt a big issue that should be brought up to the user
    String sql = "INSERT IGNORE INTO Favorite (UserID, RecipeID) VALUES (?, ?)";
    
    try (Connection conn = DBManager.getDBConnection();
      PreparedStatement ps = conn.prepareStatement(sql))
    {
      ps.setInt(1, userID);
      ps.setInt(2, recipeID);
      
      ps.executeUpdate();
      System.out.println("Recipe " + recipeID + " favorited by User " + userID);
    }
    catch(SQLException e)
    {
      System.out.println("Recipe already favorited or error favoriting");
      e.printStackTrace();
    }
  }
  
  public void removeFavorite(int userID, int recipeID)
  {
    String sql = "DELETE FROM Favorite WHERE UserID = ? AND RecipeID = ?";
    
    try (Connection conn = DBManager.getDBConnection();
      PreparedStatement ps = conn.prepareStatement(sql))
    {
      ps.setInt(1, userID);
      ps.setInt(2, recipeID);
      
      ps.executeUpdate();
      System.out.println("Recipe " + recipeID + " unfavorited by User " + userID);
    }
    catch(SQLException e)
    {
      System.out.println("error removing facvorite");
      e.printStackTrace();
    }
  }
  
  public void viewFavorites(int userID)
  {
    String sql = "SELECT r.RecipeID, r.RecipeName " + 
      "FROM Favorite f " + 
      "JOIN Recipe r " + 
      "ON f.RecipeID = r.RecipeID " + 
      "WHERE f.UserID = ?";
      
    try (Connection conn = DBManager.getDBConnection();
      PreparedStatement ps = conn.prepareStatement(sql))
    {
      ps.setInt(1, userID);
      ResultSet rs = ps.executeQuery();
      
      System.out.println("Favorites for User " + userID + ":");
      
      while(rs.next())
      {
        System.out.println(
        rs.getInt("RecipeID") + " | " + 
          rs.getString("RecipeName")
        );
      }
    }
    catch (SQLException e)
    {
      System.out.println("error retrieving favorites");
      e.printStackTrace();
    }
  }
  
  public ArrayList<Recipe> getFavorites(int userID)
  {
    ArrayList<Recipe> recipes = new ArrayList<>();
    
    String sql = "SELECT r.RecipeID, r.RecipeName " + 
      "FROM Favorite f " + 
      "JOIN Recipe r " + 
      "ON f.RecipeID = r.RecipeID " + 
      "WHERE f.UserID = ?";
      
    try (Connection conn = DBManager.getDBConnection();
      PreparedStatement ps = conn.prepareStatement(sql))
    {
      ps.setInt(1, userID);
      ResultSet rs = ps.executeQuery();
      
      while(rs.next())
      {
        recipes.add(new Recipe(rs.getInt("RecipeID"),rs.getString("RecipeName")));
      }
    }
    catch (SQLException e)
    {
      e.printStackTrace();
    }
    
    return recipes;
  }
  
  public boolean isFavorite(int userID, int recipeID)
  {
    String sql = "SELECT 1 FROM Favorite WHERE UserID = ? AND RecipeID = ?";
    
    try (Connection conn = DBManager.getDBConnection();
      PreparedStatement ps = conn.prepareStatement(sql))
    {
      ps.setInt(1, userID);
      ps.setInt(2, recipeID);
      
      ResultSet rs = ps.executeQuery();
      return rs.next(); //true is a row exists
    }
    catch(SQLException e)
    {
      e.printStackTrace();
    }
    return false;
  }
  
  public static void main(String[] args)
  {
    FavoriteDBAccess favDB = new FavoriteDBAccess();
    
    favDB.addFavorite(1, 1);
    favDB.addFavorite(1, 2);
    
    favDB.viewFavorites(1);
    
    favDB.removeFavorite(1, 1);
    
    favDB.viewFavorites(1);
  }
}
