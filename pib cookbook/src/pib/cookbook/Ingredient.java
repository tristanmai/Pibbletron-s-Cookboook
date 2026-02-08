package pib.cookbook;

public class Ingredient
{
  private int ingredientID;
  private String ingredientName;
  
  public Ingredient(int ingredientID, String ingredientName)
  {
    this.ingredientID = ingredientID;
    this.ingredientName = ingredientName;
  }
  
  public int getIngredientID()
  {
    return ingredientID;
  }
  
  public String getIngredientName()
  {
    return ingredientName;
  }
}
