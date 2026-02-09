package pib.cookbook;

public class Ingredient
{
  private int ingredientID;
  private String ingredientName;
  private int protein;
  private int carbs;
  private int fats;
  private int calories;
  
  public Ingredient(int ingredientID, String ingredientName)
  {
    this.ingredientID = ingredientID;
    this.ingredientName = ingredientName;
  }
  
  public Ingredient(int ingredientID, String ingredientName, int protein, int carbs, int fats, int calories)
  {
    this.ingredientID = ingredientID;
    this.ingredientName = ingredientName;
    this.protein = protein;
    this.carbs = carbs;
    this.fats = fats;
    this.calories = calories;
  }
  
  public int getIngredientID()
  {
    return ingredientID;
  }
  
  public String getIngredientName()
  {
    return ingredientName;
  }
  public int getProtein()
  {
    return protein;
  }
  public int getCarbs()
  {
    return carbs;
  }
  public int getFats()
  {
    return fats;
  }
  public int getCals()
  {
    return calories;
  }
  public String toString()
  {
    return ingredientName;
  }
}
