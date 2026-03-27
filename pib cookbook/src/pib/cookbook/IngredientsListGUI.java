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
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import static javax.swing.WindowConstants.EXIT_ON_CLOSE;
import static pib.cookbook.LoginGUI.BROWN;
import static pib.cookbook.SignupGUI.BEIGE_COLOR;

public class IngredientsListGUI extends JFrame implements ActionListener
{
  private JTextField searchField;
  private JPanel ingredientListPanel;
  private IngredientDBAccess ingredientDB = new IngredientDBAccess();
  private int currentUserID;
  
  public IngredientsListGUI(int userID)
  {
    super("Ingredients Page");
    this.setBounds(410, 100, 600, 800);
    this.setDefaultCloseOperation(EXIT_ON_CLOSE);
    this.setLayout(new BorderLayout());
    
    this.currentUserID = userID;
    
    JLabel background = new JLabel();
    background.setBackground(BEIGE_COLOR);
    background.setLayout(new BorderLayout());
    background.setOpaque(true);
    setContentPane(background);
    
    JPanel topContainer = new JPanel();
    topContainer.setOpaque(false);
    topContainer.setLayout(new BoxLayout(topContainer, BoxLayout.Y_AXIS));

    //header
    JPanel headerPanel = new JPanel(new BorderLayout());
    headerPanel.setOpaque(false);
    JButton backButton = new JButton("Back");
    backButton.addActionListener(this);
    backButton.setForeground(BROWN);
    backButton.setBackground(BEIGE_COLOR);
    JLabel titleLabel = new JLabel("         Pib's Ingredients");
    titleLabel.setFont(new Font("SansSerif", Font.BOLD, 30));
    titleLabel.setForeground(BROWN);
    
    headerPanel.add(backButton, BorderLayout.WEST);
    headerPanel.add(titleLabel, BorderLayout.CENTER);
    
    JPanel searchPanel = new JPanel(new BorderLayout(10, 5));
    searchPanel.setOpaque(false);
    searchField = new JTextField(40);
    JButton searchButton = new JButton("Search");
    searchButton.setForeground(BROWN);
    searchButton.setBackground(BEIGE_COLOR);
    searchButton.addActionListener(this);

    searchPanel.setBorder(BorderFactory.createEmptyBorder(5, 20, 5, 20));
    searchPanel.add(searchButton, BorderLayout.WEST);
    searchPanel.add(searchField, BorderLayout.CENTER);
    
    topContainer.add(headerPanel);
    topContainer.add(searchPanel);
    background.add(topContainer, BorderLayout.NORTH);
    
    ingredientListPanel = new JPanel();
    ingredientListPanel.setOpaque(true);
    ingredientListPanel.setBackground(BEIGE_COLOR);
    ingredientListPanel.setLayout(new BoxLayout(ingredientListPanel, BoxLayout.Y_AXIS));

    displayIngredients(ingredientDB.getAllIngredients(currentUserID));

    JScrollPane scrollPanel = new JScrollPane(ingredientListPanel);
    scrollPanel.setOpaque(false);
    scrollPanel.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
    background.add(scrollPanel, BorderLayout.CENTER);
    
    JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 20));
    bottomPanel.setOpaque(false);

    JButton ingredientsButton = new JButton("New Ingredient");
    ingredientsButton.setForeground(BROWN);
    ingredientsButton.setBackground(BEIGE_COLOR);
    ingredientsButton.addActionListener(this);

    bottomPanel.add(ingredientsButton);
    background.add(bottomPanel, BorderLayout.SOUTH);
  }
  private void displayIngredients(ArrayList<Ingredient> ingredients)
  {
    ingredientListPanel.removeAll();

    //uses the addreciperow method to go thru all ingredient and add them all
    for (Ingredient r : ingredients)
    {
      //add each row with correct arguemtns
      addIngredientRow(r.getIngredientID(), r.getIngredientName());
      //add spaxing between rows
      ingredientListPanel.add(Box.createVerticalStrut(5));
    }

    //updating the page
    ingredientListPanel.revalidate();
    ingredientListPanel.repaint();
  }
  
  private void addIngredientRow(int ingredientID, String ingredientName)
  {
    JPanel row = new JPanel(new BorderLayout());
    row.setBackground(BEIGE_COLOR);
    row.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

    JButton ingredientButton = new JButton(ingredientName);
    ingredientButton.setForeground(BROWN);
    ingredientButton.setBackground(BEIGE_COLOR);
    ingredientButton.addActionListener(this);

    JPanel ingredientButtonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    ingredientButtonsPanel.setBackground(BEIGE_COLOR);

    JButton editButton = new JButton("Edit");
    editButton.setForeground(BROWN);
    editButton.setBackground(BEIGE_COLOR);
    JButton deleteButton = new JButton("Delete");
    deleteButton.setForeground(BROWN);
    deleteButton.setBackground(BEIGE_COLOR);

    editButton.addActionListener(this);
    deleteButton.addActionListener(this);

    ingredientButtonsPanel.add(editButton);
    ingredientButtonsPanel.add(deleteButton);

    ingredientButton.setActionCommand("VIEW_" + ingredientID);
    editButton.setActionCommand("EDIT_" + ingredientID);
    deleteButton.setActionCommand("DELETE_" + ingredientID);

    row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));

    row.add(ingredientButton, BorderLayout.WEST);
    row.add(ingredientButtonsPanel, BorderLayout.EAST);

    ingredientListPanel.add(row);
  }
  
  public void actionPerformed(ActionEvent e)
  {
    String command = e.getActionCommand();

    if (command.equals("Back"))
    {
      this.dispose();
      new HomePageGUI(currentUserID).setVisible(true);
    }
    else if (command.equals("Search"))
    {
      displayIngredients(ingredientDB.searchIngredients(searchField.getText(), currentUserID));
    }
    else if (command.startsWith("VIEW_"))
    {
      int ingredientID = Integer.parseInt(command.substring(5));
      this.dispose();
      new IngredientPageGUI(ingredientID, currentUserID).setVisible(true);
    }
    else if (command.startsWith("EDIT_"))
    {
      int ingredientID = Integer.parseInt(command.substring(5));
      this.dispose();
      new EditIngredientGUI(currentUserID, ingredientID).setVisible(true);
    }
    else if (command.startsWith("DELETE_"))
    {
      int ingredientID = Integer.parseInt(command.substring(7));
      ingredientDB.deleteINgredient(ingredientID, currentUserID);
      displayIngredients(ingredientDB.getAllIngredients(currentUserID));
    }
    else if (command.equals("New Ingredient"))
    {
      this.dispose();
      new CreateIngredientGUI(currentUserID).setVisible(true);
    }
  }
  public static void main(String[] args)
  {
    new IngredientsListGUI(5).setVisible(true);
  }
}
