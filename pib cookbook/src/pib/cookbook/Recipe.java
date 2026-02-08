package pib.cookbook;

public class Recipe
{
  private int recipeID;
  private String recipeName;
  
  public Recipe(int recipeID, String recipeName)
  {
    this.recipeID = recipeID;
    this.recipeName = recipeName;
  }
  
  public int getRecipeID()
  {
    return recipeID;
  }
  
  public String getRecipeName()
  {
    return recipeName;
  }
}
