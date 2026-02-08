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

public class LoginGUI extends JFrame implements ActionListener
{
  //make my own colors
  public static final Color BEIGE_COLOR = new Color(220, 178, 144);
  public static final Color BROWN = new Color(120, 94, 84);
  
  //make the textfields and buttons
  private JTextField usernameField;
  private JPasswordField passwordField;
  private JButton loginButton;
  private JButton signupButton;

  //connect to userdbaccess
  private UserDBAccess userDB;

  public LoginGUI()
  {
    //title of page and set its bounds and set it to close when i exit the program
    super("Login Page");
    this.setBounds(100, 200, 600, 800);
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
    panel.setBorder(BorderFactory.createEmptyBorder(120, 100, 100, 100));
    //transparent so the background color can show
    panel.setOpaque(false);

    //title jlabel
    JLabel title = new JLabel("Welcome to Pib's Cookbook");
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
    JLabel userLabel = new JLabel("Username:");
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
    JLabel passLabel = new JLabel("   Password:");
    passLabel.setForeground(BROWN);
    //btw password field js makes the text view as dots like a password on a website. i think it functions the same for everyting else as textfield
    passwordField = new JPasswordField(20);
    passRow.add(passLabel, BorderLayout.WEST);
    passRow.add(passwordField, BorderLayout.CENTER);
    panel.add(passRow);
    
    //create the panel for my buttons. the hgao is btwn the buttons and the vgap is vertical spacing
    JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 20));
    buttonPanel.setOpaque(false);

    //text of buttons and adding action listener to it
    loginButton = new JButton("Login");
    loginButton.addActionListener(this);
    signupButton = new JButton("Signup");
    signupButton.addActionListener(this);

    buttonPanel.add(loginButton);
    buttonPanel.add(signupButton);

    //add panel to center and buttons to the bottom
    background.add(panel, BorderLayout.CENTER);
    background.add(buttonPanel, BorderLayout.SOUTH);
  }

  public void actionPerformed(ActionEvent e)
  {
    String command = e.getActionCommand();

    //if the command == the text login from the button then this activates
    if (command.equals("Login"))
    {
      //get values to test what userdb returns
      String username = usernameField.getText();
      String password = new String(passwordField.getPassword());

      boolean success = userDB.login(username, password);

      //if returns tru then the login worked
      if (success)
      {
        this.dispose();
        //show a pop up box of success
        JOptionPane.showMessageDialog(null, "Login successful!");
        //new HomePage(userID).setVisible(true);
      }
      else
      {
        //pop up box of doom
        JOptionPane.showMessageDialog(null, "Invalid login details");
      }
    }
    //ciommand equals signup
    if (command.equals("Signup"))
    {
      this.dispose();
      //new SignupPage().setVisible(true);
    }
  }

  public static void main(String[] args)
  {
    new LoginGUI().setVisible(true);
  }
}
