package pib.cookbook;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import static javax.swing.WindowConstants.EXIT_ON_CLOSE;
import static pib.cookbook.LoginGUI.BROWN;
import static pib.cookbook.SignupGUI.BEIGE_COLOR;

public class IngredientPageGUI extends JFrame implements ActionListener
{
  private int currentUserID;
  private IngredientDBAccess ingredientDB = new IngredientDBAccess();
  
  public IngredientPageGUI(int ingredientID, int userID)
  {
    super("Ingredient");
    this.setBounds(410, 100, 600, 800);
    this.setDefaultCloseOperation(EXIT_ON_CLOSE);
    this.setLayout(new BorderLayout());
    
    this.currentUserID = userID;
    Ingredient ingredient = ingredientDB.getIngredientInfo(ingredientID);

    //background
    JLabel background = new JLabel();
    background.setBackground(BEIGE_COLOR);
    background.setLayout(new BorderLayout());
    background.setOpaque(true);
    setContentPane(background);
    
    JPanel header = new JPanel(new BorderLayout());
    header.setOpaque(false);
    
    //bac button
    JButton back = new JButton("Back");
    back.addActionListener(this);
    back.setBackground(BEIGE_COLOR);
    back.setForeground(BROWN);
    
    //title with the recipe name
    JLabel titleLabel = new JLabel("          " + ingredient.getIngredientName());
    titleLabel.setFont(new Font("SansSerif", Font.BOLD, 30));
    titleLabel.setForeground(BROWN);
    
    header.add(back, BorderLayout.WEST);
    header.add(titleLabel, BorderLayout.CENTER);
    background.add(header, BorderLayout.NORTH);
    
    JPanel content = new JPanel();
    content.setOpaque(true);
    content.setBackground(BEIGE_COLOR);
    content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
    
    //spacing
    content.add(Box.createVerticalStrut(20));
    //add info
    content.add(new JLabel("Protein: " + ingredient.getProtein() + " per 100g"));
    content.add(new JLabel("Carbs: " + ingredient.getCarbs() + " per 100g"));
    content.add(new JLabel("Fats: " + ingredient.getFats() + " per 100g"));
    content.add(new JLabel("Calories: " + ingredient.getCals() + " per 100g"));
    
    background.add(content, BorderLayout.CENTER);
  }
  public void actionPerformed(ActionEvent e)
  {
    String command = e.getActionCommand();
    
    if(command.equals("Back"))
    {
      this.dispose();
      new IngredientsListGUI(currentUserID).setVisible(true);
    }
  }
  
  public static void main(String[] args)
  {
    new IngredientPageGUI(7, 5).setVisible(true);
  }
}
