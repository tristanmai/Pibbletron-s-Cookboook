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
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import static pib.cookbook.LoginGUI.BROWN;
import static pib.cookbook.SignupGUI.BEIGE_COLOR;

public class HomePageGUI extends JFrame implements ActionListener
{

  private JPanel recipeListPanel;
  private RecipeDBAccess recipeDB = new RecipeDBAccess();
  private RecipeIngredientDBAccess recipeIngredientDB = new RecipeIngredientDBAccess();
  private JTextField searchField;
  private FavoriteDBAccess favDB = new FavoriteDBAccess();
  JComboBox<String> drop;
  private int currentUserID;

  public HomePageGUI(int userID)
  {
    super("Recipe Book Home");
    this.setBounds(410, 100, 640, 800);
    this.setDefaultCloseOperation(EXIT_ON_CLOSE);
    this.setLayout(new BorderLayout());

    this.currentUserID = userID;

    //background
    JLabel background = new JLabel();
    background.setBackground(BEIGE_COLOR);
    background.setLayout(new BorderLayout());
    background.setOpaque(true);
    setContentPane(background);

    //top section
    JPanel topContainer = new JPanel();
    topContainer.setOpaque(false);
    topContainer.setLayout(new BoxLayout(topContainer, BoxLayout.Y_AXIS));

    //header
    JPanel headerPanel = new JPanel(new BorderLayout());
    headerPanel.setOpaque(false);
    JButton logoutButton = new JButton("Logout");
    logoutButton.addActionListener(this);
    logoutButton.setForeground(BROWN);
    logoutButton.setBackground(BEIGE_COLOR);
    JLabel titleLabel = new JLabel("         Pib's Recipe Book");
    titleLabel.setFont(new Font("SansSerif", Font.BOLD, 30));
    titleLabel.setForeground(BROWN);

    headerPanel.add(logoutButton, BorderLayout.WEST);
    headerPanel.add(titleLabel, BorderLayout.CENTER);

    //sort
    JPanel sortPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 5));
    sortPanel.setOpaque(false);
    JLabel sortLabel = new JLabel("   Sort By:");
    sortLabel.setForeground(BROWN);
    JButton recentButton = new JButton("Recent");
    recentButton.setForeground(BROWN);
    recentButton.setBackground(BEIGE_COLOR);
    recentButton.addActionListener(this);
    JButton cookTimeButton = new JButton("Cook Time");
    cookTimeButton.setForeground(BROWN);
    cookTimeButton.setBackground(BEIGE_COLOR);
    cookTimeButton.addActionListener(this);
    JButton favSortButton = new JButton("Favorites");
    favSortButton.setForeground(BROWN);
    favSortButton.setBackground(BEIGE_COLOR);
    favSortButton.addActionListener(this);

    JButton breakfastSortButton = new JButton("Breakfast");
    breakfastSortButton.setForeground(BROWN);
    breakfastSortButton.setBackground(BEIGE_COLOR);
    breakfastSortButton.addActionListener(this);

    JButton lunchSortButton = new JButton("Lunch");
    lunchSortButton.setForeground(BROWN);
    lunchSortButton.setBackground(BEIGE_COLOR);
    lunchSortButton.addActionListener(this);

    JButton dinnerSortButton = new JButton("Dinner");
    dinnerSortButton.setForeground(BROWN);
    dinnerSortButton.setBackground(BEIGE_COLOR);
    dinnerSortButton.addActionListener(this);

    sortPanel.add(sortLabel);
    sortPanel.add(recentButton);
    sortPanel.add(cookTimeButton);
    sortPanel.add(favSortButton);
    sortPanel.add(breakfastSortButton);
    sortPanel.add(lunchSortButton);
    sortPanel.add(dinnerSortButton);

    //search
    JPanel searchPanel = new JPanel(new BorderLayout(7, 5));
    searchPanel.setOpaque(false);
    searchField = new JTextField(33);
    JButton searchButton = new JButton("Search");
    searchButton.setForeground(BROWN);
    searchButton.setBackground(BEIGE_COLOR);
    searchButton.addActionListener(this);

    String[] options =
    {
      "All", "Recipe Name", "Ingredients Used"
    };
    drop = new JComboBox<>(options);
    drop.setPreferredSize(new Dimension(5, 10));

    searchPanel.setBorder(BorderFactory.createEmptyBorder(5, 9, 5, 11));
    searchPanel.add(searchButton, BorderLayout.WEST);
    searchPanel.add(drop);
    searchPanel.add(searchField, BorderLayout.EAST);

    topContainer.add(headerPanel);
    topContainer.add(sortPanel);
    topContainer.add(searchPanel);
    background.add(topContainer, BorderLayout.NORTH);

    //recipes
    recipeListPanel = new JPanel();
    recipeListPanel.setOpaque(true);
    recipeListPanel.setBackground(BEIGE_COLOR);
    recipeListPanel.setLayout(new BoxLayout(recipeListPanel, BoxLayout.Y_AXIS));

    displayRecipes(recipeDB.getAllRecipes(currentUserID));

    JScrollPane scrollPanel = new JScrollPane(recipeListPanel);
    scrollPanel.setOpaque(false);
    scrollPanel.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
    background.add(scrollPanel, BorderLayout.CENTER);

    //buttons
    JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 20));
    bottomPanel.setOpaque(false);

    JButton createRecipeButton = new JButton("Create Recipe");
    createRecipeButton.setForeground(BROWN);
    createRecipeButton.setBackground(BEIGE_COLOR);
    createRecipeButton.addActionListener(this);

    JButton ingredientsButton = new JButton("Ingredients Page");
    ingredientsButton.setForeground(BROWN);
    ingredientsButton.setBackground(BEIGE_COLOR);
    ingredientsButton.addActionListener(this);

    bottomPanel.add(createRecipeButton);
    bottomPanel.add(ingredientsButton);
    background.add(bottomPanel, BorderLayout.SOUTH);
  }

  private void addRecipeRow(int recipeID, String recipeName)
  {
    JPanel row = new JPanel(new BorderLayout());
    row.setBackground(BEIGE_COLOR);
    row.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

    JButton recipeButton = new JButton(recipeName);
    recipeButton.setForeground(BROWN);
    recipeButton.setBackground(BEIGE_COLOR);
    recipeButton.addActionListener(this);

    JPanel recipeButtonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    recipeButtonsPanel.setBackground(BEIGE_COLOR);

    boolean isFav = favDB.isFavorite(currentUserID, recipeID);
    if (isFav)
    {
      JButton favButton = new JButton("★");
      favButton.setForeground(BROWN);
      favButton.setBackground(BEIGE_COLOR);
      favButton.setActionCommand("FAV_" + recipeID);
      favButton.addActionListener(this);
      recipeButtonsPanel.add(favButton);
    }
    else
    {
      JButton favButton = new JButton("☆");
      favButton.setForeground(BROWN);
      favButton.setBackground(BEIGE_COLOR);
      favButton.setActionCommand("FAV_" + recipeID);
      favButton.addActionListener(this);
      recipeButtonsPanel.add(favButton);
    }
    JButton editButton = new JButton("Edit");
    editButton.setForeground(BROWN);
    editButton.setBackground(BEIGE_COLOR);
    JButton deleteButton = new JButton("Delete");
    deleteButton.setForeground(BROWN);
    deleteButton.setBackground(BEIGE_COLOR);

    editButton.addActionListener(this);
    deleteButton.addActionListener(this);

    recipeButtonsPanel.add(editButton);
    recipeButtonsPanel.add(deleteButton);

    recipeButton.setActionCommand("VIEW_" + recipeID);
    editButton.setActionCommand("EDIT_" + recipeID);
    deleteButton.setActionCommand("DELETE_" + recipeID);

    row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));

    row.add(recipeButton, BorderLayout.WEST);
    row.add(recipeButtonsPanel, BorderLayout.EAST);

    recipeListPanel.add(row);
  }

  private void displayRecipes(ArrayList<Recipe> recipes)
  {
    recipeListPanel.removeAll();

    //uses the addreciperow method to go thru all recipes and add them all
    for (Recipe r : recipes)
    {
      //add each row with correct arguemtns
      addRecipeRow(r.getRecipeID(), r.getRecipeName());
      //add spaxing between rows
      recipeListPanel.add(Box.createVerticalStrut(5));
    }

    //updating the page
    recipeListPanel.revalidate();
    recipeListPanel.repaint();
  }

  private void displayRecipes(ArrayList<Recipe> recipes, ArrayList<Recipe> recipes2)
  {
    recipeListPanel.removeAll();

    //uses the addreciperow method to go thru all recipes and add them all
    for (Recipe r : recipes)
    {
      //add each row with correct arguemtns
      addRecipeRow(r.getRecipeID(), r.getRecipeName());
      //add spaxing between rows
      recipeListPanel.add(Box.createVerticalStrut(5));
    }
    for (Recipe r2 : recipes2)
    {
      boolean alreadyExists = false;
      for (Recipe r : recipes)
      {
        if (r.getRecipeName().equals(r2.getRecipeName()))
        {
          alreadyExists = true;
          break;
        }
      }
      if (!alreadyExists)
      {
        addRecipeRow(r2.getRecipeID(), r2.getRecipeName());
        recipeListPanel.add(Box.createVerticalStrut(5));
      }
    }

    //updating the page
    recipeListPanel.revalidate();
    recipeListPanel.repaint();
  }

  public void actionPerformed(ActionEvent e)
  {
    String command = e.getActionCommand();

    if (command.equals("Logout"))
    {
      this.dispose();
      new LoginGUI().setVisible(true);
    }
    else if (command.equals("Recent"))
    {
      displayRecipes(recipeDB.getAllRecipes(currentUserID));
    }
    else if (command.equals("Cook Time"))
    {
      displayRecipes(recipeDB.getRecipesSorted(currentUserID));
    }
    else if (command.equals("Favorites"))
    {
      displayRecipes(favDB.getFavorites(currentUserID));
    }
    else if (command.equals("Breakfast"))
    {
      displayRecipes(recipeDB.searchRecipeType("Breakfast", currentUserID));
    }
    else if (command.equals("Lunch"))
    {
      displayRecipes(recipeDB.searchRecipeType("Lunch", currentUserID));
    }
    else if (command.equals("Dinner"))
    {
      displayRecipes(recipeDB.searchRecipeType("Dinner", currentUserID));
    }
    else if (command.equals("Search"))
    {
      String option = (String) drop.getSelectedItem();

      if (option.equals("All"))
      {
        displayRecipes(recipeDB.searchRecipes(searchField.getText(), currentUserID), recipeIngredientDB.searchIngredientsInRecipe(searchField.getText(), currentUserID));
      }
      else if (option.equals("Recipe Name"))
      {
        displayRecipes(recipeDB.searchRecipes(searchField.getText(), currentUserID));
      }
      else if (option.equals("Ingredients Used"))
      {
        displayRecipes(recipeIngredientDB.searchIngredientsInRecipe(searchField.getText(), currentUserID));
      }
    }
    else if (command.startsWith("VIEW_"))
    {
      int recipeID = Integer.parseInt(command.substring(5));
      this.dispose();
      new RecipePageGUI(recipeID, currentUserID).setVisible(true);
    }
    else if (command.startsWith("EDIT_"))
    {
      int recipeID = Integer.parseInt(command.substring(5));
      this.dispose();
      new EditRecipeGUI(recipeID, currentUserID).setVisible(true);
    }
    else if (command.startsWith("DELETE_"))
    {
      int recipeID = Integer.parseInt(command.substring(7));
      recipeDB.deleteRecipe(recipeID, currentUserID);
      displayRecipes(recipeDB.getAllRecipes(currentUserID));
    }
    else if (command.equals("Create Recipe"))
    {
      this.dispose();
      new CreateRecipeGUI(currentUserID).setVisible(true);
    }
    else if (command.equals("Ingredients Page"))
    {
      this.dispose();
      new IngredientsListGUI(currentUserID).setVisible(true);
    }
    else if (command.startsWith("FAV_"))
    {
      int recipeID = Integer.parseInt(command.substring(4));
      if (favDB.isFavorite(currentUserID, recipeID))
      {
        favDB.removeFavorite(currentUserID, recipeID);
      }
      else
      {
        favDB.addFavorite(currentUserID, recipeID);
      }
      displayRecipes(recipeDB.getAllRecipes(currentUserID));
    }
  }

  public static void main(String[] args)
  {
    new HomePageGUI(5).setVisible(true);
  }
}
