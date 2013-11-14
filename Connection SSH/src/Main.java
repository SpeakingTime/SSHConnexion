import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;


public class Main{

	
	JFrame frame; 
	JPanel panel;
	JLabel hostLabel;
    JTextField hostTextField;
    JLabel usernameLabel;
    JTextField usernameTextField;
    JLabel passwordLabel;
    JTextField passwordTextField;
    JButton connectButton;
    JTextArea textArea;
    JScrollPane scrollPane;     
    
    public Main() { 
    	
    	
    	JFrame frame = new JFrame("Connection SSH");
		frame.setSize(400, 600);
		
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel panel = new JPanel();
		panel.setLayout(null);
		panel.setLocation(0, 0);
		panel.setSize(250, 30);
        frame.add(panel);
        
        JLabel hostLabel = new JLabel("Host : ");
        hostLabel.setLocation(5, 0);
        hostLabel.setSize(100, 30);
        hostLabel.setForeground(Color.red);
        panel.add(hostLabel);
                
        final JTextField hostTextField= new JTextField();
        hostTextField.setText("ftp.data-mysql.fr");
        hostTextField.setLocation(5, 30);
        hostTextField.setSize(300, 30);
        panel.add(hostTextField);
        
        JLabel usernameLabel = new JLabel("Username : ");
        usernameLabel.setLocation(5, 60);
        usernameLabel.setSize(100, 30);
        usernameLabel.setForeground(Color.red);
        panel.add(usernameLabel);
                
        final JTextField usernameTextField= new JTextField();
        usernameTextField.setText("datamysq");
        usernameTextField.setLocation(5, 90);
        usernameTextField.setSize(300, 30);
        panel.add(usernameTextField);
        
        JLabel passwordLabel = new JLabel("Password : ");
        passwordLabel.setLocation(5, 120);
        passwordLabel.setSize(100, 30);
        passwordLabel.setForeground(Color.red);
        panel.add(passwordLabel);
                
        final JTextField passwordTextField= new JTextField();
        passwordTextField.setText("ross92");
        passwordTextField.setLocation(5, 150);
        passwordTextField.setSize(300, 30);
        panel.add(passwordTextField);

        JLabel meetingnameLabel = new JLabel("meeting name : ");
        meetingnameLabel.setLocation(5, 180);
        meetingnameLabel.setSize(100, 30);
        meetingnameLabel.setForeground(Color.red);
        panel.add(meetingnameLabel);
        
        final JTextField meetingnameTextField= new JTextField();
        meetingnameTextField.setText("");
        meetingnameTextField.setLocation(5, 210);
        meetingnameTextField.setSize(300, 30);
        panel.add(meetingnameTextField);
        
        final JTextArea textArea = new JTextArea();
        JScrollPane scrollPane = new JScrollPane(textArea); 
        textArea.setLocation(5, 310);
        textArea.setSize(380, 200);
        textArea.setEditable(false);
        panel.add(textArea);
        
        JButton connectButton = new JButton("Connect!");
        connectButton.setLocation(5, 260);
        connectButton.setSize(100, 30);
        connectButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent ae)
            {
            	
            	JSch jsch=new JSch();
            	
            	String host = hostTextField.getText();
            	
            	String user = usernameTextField.getText();
            	 
            	try {
					Session session=jsch.getSession(user, host, 22);
					
					java.util.Properties config = new java.util.Properties(); 
					config.put("StrictHostKeyChecking", "no");
					session.setConfig(config);					
					
					String password = passwordTextField.getText();
					session.setPassword(password);
					
					session.connect(30000);
					
					Channel channel=session.openChannel("shell");
					channel.setInputStream(System.in);
					channel.setOutputStream(System.out);
					channel.connect(3*1000);
					
					StringBuilder meeting_result = new StringBuilder();
					meeting_result.append("meeting_name: TEST");
					meeting_result.append("total_time: 00:10:00");
					
					File myFile = new File("C:\\SpeakingTime",meetingnameTextField.getText().toString() + ".txt"); //on déclare notre futur fichier
			        File myDir = new File("C:\\SpeakingTime"); //pour créer le repertoire dans lequel on va mettre notre fichier
			        Boolean success=true;
			        if (!myDir.exists()) {
			        	success = myDir.mkdir(); //On crée le répertoire (s'il n'existe pas!!)
			        }
			        if (success){
			           	//String data= "Ce que je veux ecrire dans mon fichier \r\n";
			           	FileOutputStream output = new FileOutputStream(myFile,true); //le true est pour écrire en fin de fichier, et non l'écraser
			           	//output.write(data.getBytes());
			           	output.write(meeting_result.toString().getBytes());
			        }
			        else {System.out.println ("ERREUR DE CREATION DE DOSSIER");}
					
					ChannelSftp sftpChannel = (ChannelSftp) session.openChannel("sftp");
			        sftpChannel.connect();
			        sftpChannel.put(new FileInputStream(myFile), myFile.getName());
					
				} catch(Exception e){
					System.out.println(e);
				}
            	
            	
            	
            	textArea.append("Ok!/n");
            }
         });
        panel.add(connectButton);
        
        frame.setVisible(true);
        
        
	} 

	public static void main (String[] args){

		new Main();
		
	}
	


	
	
}
