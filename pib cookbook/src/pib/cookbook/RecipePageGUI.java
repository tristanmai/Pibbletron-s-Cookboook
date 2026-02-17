package pib.cookbook;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import static javax.swing.WindowConstants.EXIT_ON_CLOSE;
import static pib.cookbook.LoginGUI.BROWN;
import static pib.cookbook.SignupGUI.BEIGE_COLOR;

public class RecipePageGUI extends JFrame implements ActionListener
{
  //connect to dbaccess'
  private RecipeDBAccess recipeDB = new RecipeDBAccess();
  private RecipeIngredientDBAccess riDB = new RecipeIngredientDBAccess();
  private int currentUserID;
  private int recipeID;
  
  public RecipePageGUI(int recipeID, int userID)
  {
    super("Recipe Page");
    this.setBounds(400, 100, 600, 800);
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
    
    Recipe recipe = recipeDB.getRecipeByID(recipeID);
    
    //header
    JPanel header = new JPanel(new BorderLayout());
    header.setOpaque(false);
    
    //bac button
    JButton back = new JButton("Back");
    back.addActionListener(this);
    back.setBackground(BEIGE_COLOR);
    back.setForeground(BROWN);
    
    //title with the recipe name
    JLabel titleLabel = new JLabel("          " + recipe.getRecipeName());
    titleLabel.setFont(new Font("SansSerif", Font.BOLD, 30));
    titleLabel.setForeground(BROWN);
    
    header.add(back, BorderLayout.WEST);
    header.add(titleLabel, BorderLayout.CENTER);
    background.add(header, BorderLayout.NORTH);
    
    //main section
    JPanel content = new JPanel();
    content.setOpaque(true);
    content.setBackground(BEIGE_COLOR);
    content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
    
    //spacing
    content.add(Box.createVerticalStrut(20));
    //add info
    content.add(new JLabel("Description: " + recipe.getDescription()));
    content.add(new JLabel("Instructions: " + recipe.getInstructions()));
    content.add(new JLabel("Cooking Time: " + recipe.getCookTime() + "minutes"));
    
    JLabel ingLabel = new JLabel("Ingredients:");
    ingLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
    ingLabel.setForeground(BROWN);
    content.add(Box.createVerticalStrut(20));
    content.add(ingLabel);
    
    ArrayList<String> ingredients = riDB.getIngredientDisplay(recipeID);
    
    //add list of ingredients
    for(String s : ingredients)
    {
      JLabel ing = new JLabel("- " + s);
      ing.setFont(new Font("SansSerif", Font.PLAIN, 16));
      ing.setAlignmentX(LEFT_ALIGNMENT);
      content.add(ing);
    }
    
    //scroll bar if there r a lot of ingredients
    JScrollPane scroll = new JScrollPane(content);
    scroll.setOpaque(false);
    background.add(scroll, BorderLayout.CENTER);
  }
  public void actionPerformed(ActionEvent e)
  {
    String command = e.getActionCommand();
    
    if(command.equals("Back"))
    {
      this.dispose();
      new HomePageGUI(currentUserID).setVisible(true);
    }
  }
  
  public static void main(String[] args)
  {
    new RecipePageGUI(7, 5).setVisible(true);
  }
}
