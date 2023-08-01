import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.BorderLayout;

import java.io.*;

 class Client extends JFrame{

    Socket socket;

    BufferedReader br;
     PrintWriter  out;
  
    //  Declare components
    private JLabel heading = new JLabel("Client");
    private JTextArea messageArea =new JTextArea();
    private JTextField messageInput= new JTextField();
    private Font font= new Font("roboto", Font.BOLD,20);
    


public Client(){

    try{ 
        System.out.println("sending request to server");
        socket = new Socket("127.0.0.1",7777);
        System.out.println("connection done");


         br= new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out= new PrintWriter(socket.getOutputStream());

        createGUI();
        handleEvents();
   
         startReading();
    //      StartWriting();

    } catch(Exception e){

    }
}
/**
 * 
 */
private void handleEvents() {
    messageInput.addKeyListener(new KeyListener(){

        @Override
        public void keyTyped(KeyEvent e) {
           
        }
        @Override
        public void keyPressed(KeyEvent e) {
            
        }
        @Override
        public void keyReleased(KeyEvent e) {
        // System.out.println("key released" +e.getKeyCode());
        if(e.getKeyCode()==10)
        {
        //     System.out.println("you have pressed enter");

        String contentToSend=messageInput.getText();
        messageArea.append("Me :"+contentToSend+"\n" );
        out.println(contentToSend);   
        out.flush();            
        messageInput.setText("");
        messageInput.requestFocus();

        }
        }

    } );


}
private void createGUI() {

    this.setTitle("client Messager[END]");
    this.setSize(600, 700);
    this.setLocationRelativeTo(null);
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
// coding for component
heading.setFont(font);
messageArea.setFont(font);
messageInput.setFont(font);

//  heading.setIcon(new ImageIcon( "CLOGO.png"));
heading.setHorizontalTextPosition(SwingConstants.CENTER);
heading.setVerticalTextPosition(SwingConstants.BOTTOM);
heading.setHorizontalAlignment(SwingConstants.CENTER);
heading.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
messageArea.setEditable(false);
messageInput.setHorizontalAlignment(SwingConstants.CENTER);


// frame layout
this.setLayout(new BorderLayout());

// adding components to frame
this.add(heading,BorderLayout.NORTH);
JScrollPane jscrollpane = new JScrollPane(messageArea);
this.add(messageArea,BorderLayout.CENTER);
this.add(messageInput,BorderLayout.SOUTH);


    this.setVisible(true);

}


public void startReading()
    {
    //   thread-For Reading
        //   variable lamda
          Runnable r1=() ->{
            System.out.println("reader started..");
            try{
            while(true)
            {
                
               String msg=br.readLine();
               if(msg.equals("exit")){
                System.out.println("Server terminated the chat");
                JOptionPane.showInternalMessageDialog(this, "server terminanted the chat");
                messageInput.setEnabled(false);
                socket.close();
                break;
               }

            //    System.out.println("Server:" +msg);server
            messageArea.append("Server:" +msg+"\n");
           

            }
             }catch(Exception e){
                e.printStackTrace();
            }

          };
          new Thread (r1).start();
    
    }


    public void StartWriting()
    {
    
        // thread- data ko user se lega and will send to client
         Runnable r2=() ->{      
            System.out.println("writer started");
             try{
            while(!socket.isClosed())
            {
               
                  

                    BufferedReader br1 = new BufferedReader(new InputStreamReader(System.in));

                    String content = br1.readLine();
                    out.println(content);
                    out.flush();
                    if(content.equals("exit")){
                        socket.close();
                        break;

                     }
 
               

            }  
             }catch(Exception e){
                    // e.printStackTrace();
                    System.out.println("connection closed");

                }
          };
          new Thread (r2).start();


          
    }
    public static void main(String[] args) {
        System.out.println("client is ready");
        new Client();
        
    }
}