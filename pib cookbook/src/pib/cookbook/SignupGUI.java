package pib.cookbook;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

public class SignupGUI extends JFrame implements ActionListener
{
  //make my own colors
  public static final Color BEIGE_COLOR = new Color(220, 178, 144);
  public static final Color BROWN = new Color(120, 94, 84);
  
  //make the textfields and buttons
  private JTextField usernameField;
  private JPasswordField passwordField;
  private JPasswordField password2Field;
  private JButton signupButton;

  //connect to userdbaccess
  private UserDBAccess userDB;

  public SignupGUI()
  {
    //title of page and set its bounds and set it to close when i exit the program
    super("signup Page");
    this.setBounds(400, 100, 600, 800);
    this.setDefaultCloseOperation(EXIT_ON_CLOSE);

    userDB = new UserDBAccess();

    //set the colort of the background with a jlabel
    JLabel background = new JLabel();
    background.setBackground(BEIGE_COLOR);
    background.setLayout(new BorderLayout());
    background.setOpaque(true);
    setContentPane(background);

    //the panel of my page that ig=s going to hold everything
    JPanel panel = new JPanel();
    //set it to flowlayout so things line uo next to each other neatly
    panel.setLayout(new FlowLayout(FlowLayout.CENTER));
    //i set the borders on the sides
    panel.setBorder(BorderFactory.createEmptyBorder(120, 100, 100, 150));
    //transparent so the background color can show
    panel.setOpaque(false);

    //title jlabel
    JLabel title = new JLabel("    Welcome to Pib's Cookbook!");
    //line it up centrally on the x axis
    title.setAlignmentX(Component.CENTER_ALIGNMENT);
    //font of it
    title.setFont(new Font("SansSerif", Font.BOLD, 35));
    //color of text to brown
    title.setForeground(BROWN);
    panel.add(title);
    //create empty vertical space to space out the title from the rest
    panel.add(Box.createVerticalStrut(60));
    
    //panel for the username stuff
    JPanel userRow = new JPanel(new FlowLayout(FlowLayout.CENTER));
    userRow.setOpaque(false);
    //set its text
    JLabel userLabel = new JLabel("           Username:");
    //text to brown
    userLabel.setForeground(BROWN);
    //decide the width of the trextfield
    usernameField = new JTextField(20);
    //the west and center make sure the label is to the left of the textfield
    userRow.add(userLabel, BorderLayout.WEST);
    userRow.add(usernameField, BorderLayout.CENTER);
    //add it to the panel
    panel.add(userRow);
    
    //same thing for password as for username
    JPanel passRow = new JPanel(new FlowLayout(FlowLayout.CENTER));
    passRow.setOpaque(false);
    JLabel passLabel = new JLabel("              Password:");
    passLabel.setForeground(BROWN);
    passwordField = new JPasswordField(20);
    passRow.add(passLabel, BorderLayout.WEST);
    passRow.add(passwordField, BorderLayout.CENTER);
    panel.add(passRow);
    
    //the only thing diff here is this. i added a password verification thing
    JPanel pass2Row = new JPanel(new FlowLayout(FlowLayout.CENTER));
    pass2Row.setOpaque(false);
    JLabel pass2Label = new JLabel("Confirm Password:");
    pass2Label.setForeground(BROWN);
    password2Field = new JPasswordField(20);
    pass2Row.add(pass2Label, BorderLayout.WEST);
    pass2Row.add(password2Field, BorderLayout.CENTER);
    panel.add(pass2Row);
    
    //create the panel for my buttons. the hgao is btwn the buttons and the vgap is vertical spacing
    JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 20));
    buttonPanel.setOpaque(false);

    //text of buttons and adding action listener to it
    signupButton = new JButton("Signup");
    signupButton.addActionListener(this);

    buttonPanel.add(signupButton);

    //add panel to center and button to the bottom
    background.add(panel, BorderLayout.CENTER);
    background.add(buttonPanel, BorderLayout.SOUTH);
  }

  public void actionPerformed(ActionEvent e)
  {
    String command = e.getActionCommand();

    //ciommand equals signup
    if (command.equals("Signup"))
    {
      //get info to check passwords and to input into db
      String username = usernameField.getText();
      String password = new String(passwordField.getPassword());
      String password2 = new String(password2Field.getPassword());
      
      if(!password.equals(password2))//password diff
      {
        JOptionPane.showMessageDialog(null, "Passwords not identical");
      }
      else
      {
        //insert into db
        boolean success = userDB.insertUser(username, password);
        
        if(success)//i changed the method to return a boolean for working
        {
          JOptionPane.showMessageDialog(null, "Signup Success!");
          this.dispose();
          new LoginGUI().setVisible(true);
        }
        else
        {
          //im like 99 percent sure that the username being taken is the only reason it woud return false at this point so yea
          JOptionPane.showMessageDialog(null, "Username taken");
        }
      }
    }
  }

  public static void main(String[] args)
  {
    new SignupGUI().setVisible(true);
  }
}
