import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Properties;
 
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager; 

public class SwingEmailSender extends JFrame 
{
    private ConfigUtility configUtil = new ConfigUtility();
    private JMenuBar menuBar = new JMenuBar();
    private JMenu menuFile = new JMenu("File");
    private JMenuItem menuItemSetting = new JMenuItem("Settings..");
    private JLabel labelSubject = new JLabel("Subject: ");
    private JTextField fieldSubject = new JTextField(30);
    private JButton buttonSend = new JButton("SEND");
    private JFilePicker filePicker = new JFilePicker("Attached", "Attach File...");
    private JTextArea textAreaMessage = new JTextArea(10, 30);
    private GridBagConstraints constraints = new GridBagConstraints();
    
    private String[] mailList;
     
    public SwingEmailSender(String[] mailList) 
    {
        super("Swing E-mail Sender Program");
         
        // set up layout
        setLayout(new GridBagLayout());
        constraints.anchor = GridBagConstraints.WEST;
        constraints.insets = new Insets(5, 5, 5, 5);
     
        setupMenu();
        setupForm();
         
        pack();
        setLocationRelativeTo(null);    // center on screen
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
        
        this.mailList = mailList;
    }
 
    private void setupMenu() 
    {
        menuItemSetting.addActionListener(new ActionListener() 
        {
            @Override
            public void actionPerformed(ActionEvent event) {
                SettingsDialog dialog = new SettingsDialog(SwingEmailSender.this, configUtil);
                dialog.setVisible(true);
            }
        });
         
        menuFile.add(menuItemSetting);
        menuBar.add(menuFile);
        setJMenuBar(menuBar);      
    }
     
    private void setupForm() 
    {
        constraints.gridx = 0;
        constraints.gridy = 0;
      //  add(labelTo, constraints);
         
        constraints.gridx = 1;
        constraints.fill = GridBagConstraints.HORIZONTAL;
      //  add(fieldTo, constraints);
         
        constraints.gridx = 0;
        constraints.gridy = 1;
        add(labelSubject, constraints);
         
        constraints.gridx = 1;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        add(fieldSubject, constraints);
         
        constraints.gridx = 2;
        constraints.gridy = 0;
        constraints.gridheight = 2;
        constraints.fill = GridBagConstraints.BOTH;
        buttonSend.setFont(new Font("Arial", Font.BOLD, 16));
        add(buttonSend, constraints);
         
        buttonSend.addActionListener(new ActionListener() 
        {
            @Override
            public void actionPerformed(ActionEvent event) {
                buttonSendActionPerformed(event);
            }
        });
         
        constraints.gridx = 0;
        constraints.gridy = 2;
        constraints.gridheight = 1;
        constraints.gridwidth = 3;
        filePicker.setMode(JFilePicker.MODE_OPEN);
        add(filePicker, constraints);
         
        constraints.gridy = 3;
        constraints.weightx = 1.0;
        constraints.weighty = 1.0;
         
        add(new JScrollPane(textAreaMessage), constraints);    
    }
     
    private void buttonSendActionPerformed(ActionEvent event) 
    {
        if (!validateFields())
            return;

         //loop through recipiants 
        for(int i = 0; i < mailList.length; i++)
        {
	        String toAddress = mailList[i];
	        String subject = fieldSubject.getText();
	        String message = textAreaMessage.getText();
	         
	        File[] attachFiles = null;
	         
	        if (!filePicker.getSelectedFilePath().equals("")) 
            {
	            File selectedFile = new File(filePicker.getSelectedFilePath());
	            attachFiles = new File[] {selectedFile};
	        }
	         
	        try {
	            Properties smtpProperties = configUtil.loadProperties();
	            EmailUtility.sendEmail(smtpProperties, toAddress, subject, message, attachFiles);
	             
	        } catch (Exception ex) {
	            JOptionPane.showMessageDialog(this, "Error while sending e-mail to: " + toAddress + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
	        }
        }
        JOptionPane.showMessageDialog(this, "The e-mails have been sent successfully!");
    }
     
    private boolean validateFields() 
    {
        if (fieldSubject.getText().equals("")) 
        {
            JOptionPane.showMessageDialog(this, "Please enter subject!", "Error", JOptionPane.ERROR_MESSAGE);
            fieldSubject.requestFocus();
            return false;
        }
         
        if (textAreaMessage.getText().equals("")) 
        {
            JOptionPane.showMessageDialog(this, "Please enter message!", "Error", JOptionPane.ERROR_MESSAGE);
            textAreaMessage.requestFocus();
            return false;
        }
         
        return true;
    }
}