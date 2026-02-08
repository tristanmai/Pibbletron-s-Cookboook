package pib.cookbook;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

public class UserDBAccess
{
  public boolean insertUser(String username, String password)
  {
    String sql = "INSERT INTO Users (Username, Password) VALUES (?, ?)";
    
    try (Connection conn = DBManager.getDBConnection();
      PreparedStatement ps = conn.prepareStatement(sql))
    {
      ps.setString(1, username);
      ps.setString(2, password);
      
      ps.executeUpdate();
      System.out.println("user created: " + username);
      return true;
    }
    catch(SQLIntegrityConstraintViolationException e)
    {
      System.out.println("This user already exists.");
      return false;
    }
    catch(SQLException e)
    {
      System.out.println("Error creating user.");
      e.printStackTrace();
      return false;
    }
  }
  
  public int login(String username, String password)
  {
    String sql = "SELECT * FROM Users WHERE Username = ? AND Password = ?";
    
    try(Connection conn = DBManager.getDBConnection();
      PreparedStatement ps = conn.prepareStatement(sql))
    {
      ps.setString(1, username);
      ps.setString(2, password);
      
      ResultSet rs = ps.executeQuery();
      
      if(rs.next())
      {
        return rs.getInt("UserID");
      }
    }
    catch(SQLException e)
    {
      System.out.println("Error checking login data");
      e.printStackTrace();
    }
    return -1;
  }
  
  public void viewAllUsers()
  {
    String sql = "SELECT UserID, Username FROM Users";
    
    try(Connection conn = DBManager.getDBConnection();
      PreparedStatement ps = conn.prepareStatement(sql);
      ResultSet rs = ps.executeQuery())
    {
      System.out.println("USER LIST: ");
      
      while(rs.next())
      {
        System.out.println(
        rs.getInt("UserID") + " | " + 
          rs.getString("Username")
        );
      }
    }
    catch(SQLException e)
    {
      System.out.println("error viewing users");
      e.printStackTrace();
    }
  }
  
  public void updatePassword(int userID, String newPassword)
  {
    String sql = "UPDATE Users SET Password = ? WHERE UserID = ?";
    
    try(Connection conn = DBManager.getDBConnection();
      PreparedStatement ps = conn.prepareStatement(sql))
    {
      ps.setString(1, newPassword);
      ps.setInt(2, userID);
      
      ps.executeUpdate();
      System.out.println("Password updated for UserID " + userID);
    }
    catch(SQLException e)
    {
      System.out.println("error updating password");
      e.printStackTrace();
    }
  }
  
  public void deleteUser(int userID)
  {
    String sql = "DELETE FROM Users WHERE UserID = ?";
    
    try(Connection conn = DBManager.getDBConnection();
      PreparedStatement ps = conn.prepareStatement(sql))
    {
      ps.setInt(1, userID);
      ps.executeUpdate();
      System.out.println("User deleted: ID " + userID);
    }
    catch(SQLException e)
    {
      System.out.println("error deleting user");
      e.printStackTrace();
    }
  }
  
  public static void main(String[] args)
  {
    UserDBAccess userDB = new UserDBAccess();
    
    userDB.insertUser("Tristan", "pibblenator123");
    userDB.insertUser("Ryan", "joojoojoo");
    userDB.insertUser("joe", "mama");
    
    userDB.viewAllUsers();
    
    int loginSuccess = userDB.login("Tristan", "pibblenator123");
    System.out.println("Login success: " + loginSuccess);
    
    userDB.updatePassword(2, "joojoojoo123");
    
    userDB.deleteUser(3);
    
    userDB.viewAllUsers();
  }
}
