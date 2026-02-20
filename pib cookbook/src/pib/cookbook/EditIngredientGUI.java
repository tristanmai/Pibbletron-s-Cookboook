package pib.cookbook;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import static javax.swing.WindowConstants.EXIT_ON_CLOSE;
import static pib.cookbook.LoginGUI.BROWN;
import static pib.cookbook.SignupGUI.BEIGE_COLOR;

public class EditIngredientGUI extends JFrame implements ActionListener
{
  private int currentUserID;
  private int ingredientID;
  private JTextField nameField;
  private JTextField proteinField;
  private JTextField carbsField;
  private JTextField fatsField;
  private JTextField calsField;
  IngredientDBAccess ingredientDB = new IngredientDBAccess();
  
  public EditIngredientGUI(int userID, int ingredientID)
  {
    super("Edit Ingredients Page");
    this.setBounds(410, 100, 600, 800);
    this.setDefaultCloseOperation(EXIT_ON_CLOSE);
    this.setLayout(new BorderLayout());
    
    this.currentUserID = userID;

    JLabel background = new JLabel();
    background.setBackground(BEIGE_COLOR);
    background.setLayout(new BorderLayout());
    background.setOpaque(true);
    setContentPane(background);

    //topcontainer
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
    JLabel titleLabel = new JLabel("        Create Ingredients");
    titleLabel.setFont(new Font("SansSerif", Font.BOLD, 30));
    titleLabel.setForeground(BROWN);

    headerPanel.add(backButton, BorderLayout.WEST);
    headerPanel.add(titleLabel, BorderLayout.CENTER);
    background.add(headerPanel, BorderLayout.NORTH);

    JPanel infoPanel = new JPanel();
    infoPanel.setOpaque(false);
    infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));

    JPanel nameRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 2, 0));
    nameRow.setOpaque(false);
    JLabel name = new JLabel("Ingredient Name:");
    nameField = new JTextField(15);
    nameRow.add(name);
    nameRow.add(nameField);

    JPanel proteinRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 2, 0));
    proteinRow.setOpaque(false);
    JLabel protein = new JLabel("Protein:");
    proteinField = new JTextField(5);
    JLabel proteinPer = new JLabel("per 100g");
    proteinRow.add(protein);
    proteinRow.add(proteinField);
    proteinRow.add(proteinPer);
    
    JPanel carbsRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 2, 0));
    carbsRow.setOpaque(false);
    JLabel carbs = new JLabel("Carbs:");
    carbsField = new JTextField(5);
    JLabel carbsPer = new JLabel("per 100g");
    carbsRow.add(carbs);
    carbsRow.add(carbsField);
    carbsRow.add(carbsPer);

    JPanel fatsRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 2, 0));
    fatsRow.setOpaque(false);
    JLabel fats = new JLabel("Fats:");
    fatsField = new JTextField(5);
    JLabel fatsPer = new JLabel("per 100g");
    fatsRow.add(fats);
    fatsRow.add(fatsField);
    fatsRow.add(fatsPer);

    JPanel calsRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 2, 0));
    calsRow.setOpaque(false);
    JLabel cals = new JLabel("Calories:");
    calsField = new JTextField(5);
    JLabel calsPer = new JLabel("per 100g");
    calsRow.add(cals);
    calsRow.add(calsField);
    calsRow.add(calsPer);

    //addfing to info panel
    infoPanel.add(Box.createVerticalStrut(60));
    infoPanel.add(nameRow);
    infoPanel.add(proteinRow);
    infoPanel.add(carbsRow);
    infoPanel.add(fatsRow);
    infoPanel.add(calsRow);
    infoPanel.add(Box.createVerticalStrut(300));
    background.add(infoPanel, BorderLayout.CENTER);

    JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 20));
    bottomPanel.setOpaque(false);

    JButton ingredientsButton = new JButton("Update");
    ingredientsButton.setPreferredSize(new Dimension(150, 50));
    ingredientsButton.setFont(new Font("SansSerif", Font.PLAIN, 20));
    ingredientsButton.setForeground(BROWN);
    ingredientsButton.setBackground(BEIGE_COLOR);
    ingredientsButton.addActionListener(this);

    bottomPanel.add(ingredientsButton);
    background.add(bottomPanel, BorderLayout.SOUTH);
    
    loadIngredientInfo();
  }
  
  //load up ingredients old info
  private void loadIngredientInfo()
  {
    Ingredient ing = ingredientDB.getIngredientInfo(ingredientID);
    if(ing != null)
    {
      nameField.setText(ing.getIngredientName());
      proteinField.setText(String.valueOf(ing.getProtein()));
      carbsField.setText(String.valueOf(ing.getCarbs()));
      fatsField.setText(String.valueOf(ing.getFats()));
      calsField.setText(String.valueOf(ing.getCals()));
    }
    this.repaint();
    this.revalidate();
  }

  public void actionPerformed(ActionEvent e)
  {
    String command = e.getActionCommand();
    
    if(command.equals("Back"))
    {
      this.dispose();
      new IngredientsListGUI(currentUserID).setVisible(true);
    }
    else if(command.equals("Update"))
    {
      String name = nameField.getText();
      String proteinText = proteinField.getText();
      String carbsText = carbsField.getText();
      String fatsText = fatsField.getText();
      String calsText = calsField.getText();
      
      if(name.isEmpty() || proteinText.isEmpty() || carbsText.isEmpty() || fatsText.isEmpty() || calsText.isEmpty())
      {
        JOptionPane.showMessageDialog(null, "Please Fill All Field!");
        return;
      }
      
      try
      {
        String name2 = String.valueOf(name);
        double protein = Double.parseDouble(proteinText);
        double carbs = Double.parseDouble(carbsText);
        double fats = Double.parseDouble(fatsText);
        double cals = Double.parseDouble(calsText);
        
        ingredientDB.updateIngredient(ingredientID, name2, protein, carbs, fats, cals);
        
        JOptionPane.showMessageDialog(null, "Ingredient Updated");
      }
      catch(NumberFormatException ex)
      {
        ex.printStackTrace();
        JOptionPane.showMessageDialog(null, "Please Enter Valid Numbers for the Macros");
      }
    }
  }
  public static void main(String[] args)
  {
    new EditIngredientGUI(5, 2).setVisible(true);
  }
}
