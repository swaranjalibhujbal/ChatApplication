import java.net.*;
import java.io.*;
class Server{

 ServerSocket server;
 Socket socket;

 BufferedReader br;
 PrintWriter  out;

    //  constructor
    public Server()
    {
        try{
            server=new ServerSocket(7777);
            System.out.println("server is ready to accept connection");
            System.out.println("waiting..");
           socket= server.accept();

           br= new BufferedReader(new InputStreamReader(socket.getInputStream()));   //input stream
        out= new PrintWriter(socket.getOutputStream());                                //outstream
   
         startReading();    //method 
         StartWriting();    //method
          
       } catch(Exception e){
          e.printStackTrace();
       }


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
                System.out.println("client terminated the chat");
                socket.close();
                break;
               }

               System.out.println("Client:" +msg);
              }
            } catch(Exception e)
            {
                e.printStackTrace();
            }

          };
          new Thread (r1).start();         //call
     
    }

    public void StartWriting()
    {
    
        // thread- data ko user se lega and will send to client
         Runnable r2=() ->{      
            System.out.println("writer started");
             try{
            while( !socket.isClosed())
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
                    System.out.println("connection Closed");

                }
          };
          new Thread (r2).start();


          
    }
    public static void main(String[] args) {
        System.out.println("server is ready");
        new Server();
        
    }
}