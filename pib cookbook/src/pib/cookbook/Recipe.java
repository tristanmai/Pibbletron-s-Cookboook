package pib.cookbook;

public class Recipe
{
  private int recipeID;
  private String recipeName;
  private String instructions;
  private String description;
  private int cookingTime;
  private String mealType;
  private int userID;
  
  public Recipe(int recipeID, String recipeName)
  {
    this.recipeID = recipeID;
    this.recipeName = recipeName;
  }
  public Recipe(int recipeID)
  {
    this.recipeID = recipeID;
  }
  public Recipe(int recipeID, String recipeName, String instructions, String description, int cookingTime, String mealType, int userID)
  {
    this.recipeID = recipeID;
    this.recipeName = recipeName;
    this.instructions = instructions;
    this.description = description;
    this.cookingTime = cookingTime;
    this.mealType = mealType;
    this.userID = userID;
  }
  
  public int getRecipeID()
  {
    return recipeID;
  }
  
  public String getRecipeName()
  {
    return recipeName;
  }
  public String getDescription()
  {
    return description;
  }
  public String getInstructions()
  {
    return instructions;
  }
  public int getCookTime()
  {
    return cookingTime;
  }
  public String getMealType()
  {
    return mealType;
  }
}
