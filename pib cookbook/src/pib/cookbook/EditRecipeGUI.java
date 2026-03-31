package pib.cookbook;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import static javax.swing.WindowConstants.EXIT_ON_CLOSE;
import static pib.cookbook.LoginGUI.BROWN;
import static pib.cookbook.SignupGUI.BEIGE_COLOR;

public class EditRecipeGUI extends JFrame implements ActionListener
{

  private int currentUserID;
  private int recipeID;

  private JTextField timeField;
  private JTextArea descriptionArea;
  private JTextArea instructionsArea;

  private String mealType;
  private JButton breakfastButton;
  private JButton lunchButton;
  private JButton dinnerButton;
  private JButton dessertButton;

  private JComboBox<Ingredient> ingredientDropdown;
  private JTextField weightField;

  //connecting to dbs
  RecipeDBAccess recipeDB = new RecipeDBAccess();
  IngredientDBAccess ingredientDB = new IngredientDBAccess();
  RecipeIngredientDBAccess recipeIngredientDB = new RecipeIngredientDBAccess();

  public EditRecipeGUI(int recipeID, int userID)
  {
    super("Edit Recipe Page");
    this.setBounds(410, 100, 600, 800);
    this.setDefaultCloseOperation(EXIT_ON_CLOSE);
    this.setLayout(new BorderLayout());

    this.currentUserID = userID;
    this.recipeID = recipeID;

    //backgriud
    JLabel background = new JLabel();
    background.setBackground(BEIGE_COLOR);
    background.setLayout(new BorderLayout());
    background.setOpaque(true);
    setContentPane(background);

    //header
    JPanel header = new JPanel(new BorderLayout());
    header.setOpaque(false);

    //bac button
    JButton back = new JButton("Back");
    back.addActionListener(this);
    back.setBackground(BEIGE_COLOR);
    back.setForeground(BROWN);

    JLabel titleLabel = new JLabel("        Edit Recipe");
    titleLabel.setFont(new Font("SansSerif", Font.BOLD, 30));
    titleLabel.setForeground(BROWN);

    header.add(back, BorderLayout.WEST);
    header.add(titleLabel, BorderLayout.CENTER);
    background.add(header, BorderLayout.NORTH);

    //center panel
    JPanel center = new JPanel();
    center.setOpaque(false);
    center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));

    //timerow for inputing values
    JPanel timeRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 2, 0));
    timeRow.setOpaque(false);
    timeField = new JTextField(20);
    timeRow.add(new JLabel("Cooking Time (min):"));
    timeRow.add(timeField);

    //descriptino row for inputing values
    JPanel descRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 2, 0));
    descRow.setOpaque(false);
    descriptionArea = new JTextArea(3, 20);
    descRow.add(new JLabel("Description:"));
    descRow.add(descriptionArea);

    //instruction row for inputing values
    JPanel instRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 2, 0));
    instRow.setOpaque(false);
    instructionsArea = new JTextArea(5, 20);
    instRow.add(new JLabel("Instructions:"));
    instRow.add(instructionsArea);

    center.add(Box.createVerticalStrut(30));
    center.add(timeRow);
    center.add(descRow);
    center.add(instRow);

    JPanel mealTypeRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
    mealTypeRow.setOpaque(false);

    JLabel mealTypeLabel = new JLabel("Meal Type:");
    mealTypeLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
    mealTypeLabel.setForeground(BROWN);

    breakfastButton = new JButton("Breakfast");
    lunchButton = new JButton("Lunch");
    dinnerButton = new JButton("Dinner");
    dessertButton = new JButton("Dessert");

    breakfastButton.addActionListener(this);
    lunchButton.addActionListener(this);
    dinnerButton.addActionListener(this);
    dessertButton.addActionListener(this);

    breakfastButton.setBackground(BEIGE_COLOR);
    breakfastButton.setForeground(BROWN);
    lunchButton.setBackground(BEIGE_COLOR);
    lunchButton.setForeground(BROWN);
    dinnerButton.setBackground(BEIGE_COLOR);
    dinnerButton.setForeground(BROWN);
    dessertButton.setBackground(BEIGE_COLOR);
    dessertButton.setForeground(BROWN);

    mealTypeRow.add(mealTypeLabel);
    mealTypeRow.add(breakfastButton);
    mealTypeRow.add(lunchButton);
    mealTypeRow.add(dinnerButton);
    mealTypeRow.add(dessertButton);

    center.add(Box.createVerticalStrut(20));
    center.add(mealTypeRow);

    JButton recipeButton = new JButton("Update");
    recipeButton.setPreferredSize(new Dimension(150, 50));
    recipeButton.setFont(new Font("SansSerif", Font.PLAIN, 20));
    recipeButton.setForeground(BROWN);
    recipeButton.setBackground(BEIGE_COLOR);
    recipeButton.addActionListener(this);

    JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
    buttonPanel.setOpaque(false);

    buttonPanel.add(recipeButton);
    center.add(Box.createVerticalStrut(20));
    center.add(buttonPanel);

    JLabel ingredientTitle = new JLabel("Add Ingredients");
    ingredientTitle.setFont(new Font("SansSerif", Font.BOLD, 22));
    ingredientTitle.setForeground(BROWN);
    ingredientTitle.setAlignmentX(CENTER_ALIGNMENT);

    JPanel ingredientRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
    ingredientRow.setOpaque(false);

    ingredientDropdown = new JComboBox<>();
    loadIngredients();

    weightField = new JTextField(5);

    JButton addIngredientButton = new JButton("Add");
    addIngredientButton.addActionListener(this);
    addIngredientButton.setBackground(BEIGE_COLOR);
    addIngredientButton.setForeground(BROWN);

    ingredientRow.add(new JLabel("Ingredient:"), BorderLayout.CENTER);
    ingredientRow.add(ingredientDropdown);
    ingredientRow.add(new JLabel("Weight (g):"));
    ingredientRow.add(weightField);
    ingredientRow.add(addIngredientButton);

    center.add(Box.createVerticalStrut(40));
    center.add(ingredientTitle);
    center.add(ingredientRow);

    background.add(center, BorderLayout.CENTER);

    this.loadRecipeData();
  }

  private void loadRecipeData()
  {
    Recipe r = recipeDB.getRecipeByID(recipeID, currentUserID);
    if (r != null)
    {
      timeField.setText(String.valueOf(r.getCookTime()));
      descriptionArea.setText(r.getDescription());
      instructionsArea.setText(r.getInstructions());
      mealType = r.getMealType();

      if ("Breakfast".equals(mealType))
      {
        highlightMeal(breakfastButton);
      }
      else if ("Lunch".equals(mealType))
      {
        highlightMeal(lunchButton);
      }
      else if ("Dinner".equals(mealType))
      {
        highlightMeal(dinnerButton);
      }
      else if ("Dessert".equals(mealType))
      {
        highlightMeal(dessertButton);
      }
    }
    else
    {
      JOptionPane.showMessageDialog(this, "Recipe not found!");
    }
  }

  //load the ingredients into the dropdown box
  private void loadIngredients()
  {
    ingredientDropdown.removeAllItems();
    ArrayList<Ingredient> ingredients = ingredientDB.getAllIngredients(currentUserID);

    for (Ingredient i : ingredients)
    {
      ingredientDropdown.addItem(i);
    }
  }

  private void highlightMeal(JButton selectedButton)
  {
    //reset all  da buttons
    breakfastButton.setBorder(null);
    lunchButton.setBorder(null);
    dinnerButton.setBorder(null);
    dessertButton.setBorder(null);

    //highlight the selected one
    selectedButton.setBorder(BorderFactory.createLineBorder(BROWN, 2));
  }

  public void actionPerformed(ActionEvent e)
  {
    String command = e.getActionCommand();

    if (command.equals("Back"))
    {
      this.dispose();
      new HomePageGUI(currentUserID).setVisible(true);
    }
    else if (command.equals("Update"))
    {
      //update the recipe db
      try
      {
        String desc = descriptionArea.getText();
        String instr = instructionsArea.getText();
        int cookTime = Integer.parseInt(timeField.getText());

        recipeDB.updateRecipe(recipeID, instr, desc, cookTime, mealType, currentUserID);
        JOptionPane.showMessageDialog(null, "Updated");
        this.dispose();
        new HomePageGUI(currentUserID).setVisible(true); 
      }
      catch (NumberFormatException ex)
      {
        JOptionPane.showMessageDialog(null, "Cooking time has to be a number bruh.");
      }
    }
    else if (command.equals("Breakfast"))
    {
      mealType = "Breakfast";
      highlightMeal(breakfastButton);
    }
    else if (command.equals("Lunch"))
    {
      mealType = "Lunch";
      highlightMeal(lunchButton);
    }
    else if (command.equals("Dinner"))
    {
      mealType = "Dinner";
      highlightMeal(dinnerButton);
    }
    else if (command.equals("Dessert"))
    {
      mealType = "Dessert";
      highlightMeal(dessertButton);
    }
    else if (command.equals("Add"))
    {
      //connect ingredients to recipe
      try
      {
        Ingredient ing = (Ingredient) ingredientDropdown.getSelectedItem();

        if (ing == null)
        {
          return;
        }

        double weight = Double.parseDouble(weightField.getText());

        recipeIngredientDB.insertIngredientToRecipe(this.recipeID, ing.getIngredientID(), weight);

        JOptionPane.showMessageDialog(null, "Ingredient added");
        weightField.setText("");
      }
      catch (Exception ex)
      {
        JOptionPane.showMessageDialog(null, "Invalid weight or ingredient");
      }
    }
  }

  public static void main(String[] args)
  {
    new EditRecipeGUI(7, 5).setVisible(true);
  }
}
 