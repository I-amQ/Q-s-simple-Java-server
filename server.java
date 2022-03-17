import java.io.*;
import java.net.*;
import java.util.Date;
import java.util.StringTokenizer;


public class server implements Runnable /*multithreading for each client*/ {
    public static int PORT = 420; //default
    public static ServerSocket the_server_socket = null;
    public static String Address = "";

    static {
        try {
            Address = Inet4Address.getLocalHost().toString();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        try {
            the_server_socket = new ServerSocket(PORT);

            System.out.println("Server_socket available, awaiting connection! Port: " + PORT);

            System.out.println("ADDRESS: " + Address);

            server myServer = new server();

            System.out.println("Server is now operational!");

            while (the_server_socket.isBound()) {


                    Thread theServerThread = new Thread(myServer);

                    theServerThread.start();

                    theServerThread.join(200);


            }
        } catch (IOException | InterruptedException e) {
            System.out.println(e);
        }
    }

    public void run()/*where we are gonna deal with each client connected to the server*/ {

        //we need to make variables available to catch exception

        OutputStream output = null;

        try {
            Socket client = the_server_socket.accept();
            System.out.println("\n\n\nserver_socket is now connected and opened");
            System.out.println(client.getRemoteSocketAddress() + " has joined the connection");
            System.out.println("Time of successful connection :" + new Date());



            // read HTTP Request Line . eg: GET /doc/index.html HTTP/1.1
            BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
            String input = in.readLine();


            StringTokenizer parse = new StringTokenizer(input);//chop the request into pieces whenever a space appears
            String method = parse.nextToken().toUpperCase(); // we get the HTTP method asked by the client
            String fileRequested = parse.nextToken().toLowerCase();// and the file it requested


            // send HTTP RESPONSE if the METHOD is either "GET" or "HEAD"
            output = client.getOutputStream();


            if (method.equals("GET")  ||  method.equals("HEAD")) {
                if (fileRequested.endsWith("/")) {
                    fileRequested = "./index.html";
                }

                File file = new File(".", fileRequested);
                int fileLength = (int) file.length();
                String content = getContentType(fileRequested);


                if (method.equals("GET")) { // RESPONSE FORMAT FOR GET METHOD, HEAD DOESN'T RETURN CONTENT
                    write(output, "HTTP/1.1 200 OK");
                    write(output, "Server: Quang's Server");
                    write(output, "Date: " + new Date());
                    write(output, "Content-type: " + content);
                    write(output, "Content-length: " + fileLength);
                    write(output, "\n\n");
                    output.write(new FileInputStream(file).readAllBytes());
                }

                System.out.println("File " + fileRequested + " of type " + content + " returned");

                System.out.println("\n\n\n");

                client.close();
                output.close();
                in.close();
                
                System.out.println("Client socket closed. Connection ended at: " + new Date());
            }


        } catch (Exception e) {

            if(e instanceof SocketException || e instanceof NullPointerException) {
            }else {
                try {
                    System.out.println(e + "\n");
                    stop404(output);
                } catch (IOException ex) {
                    System.out.println(ex);
                }
            }
            }
        }

    void write(OutputStream output, String s) throws IOException {
        output.write(s.getBytes());
    }

    private String getContentType(String fileRequested) {
        if (fileRequested.endsWith(".htm")  ||  fileRequested.endsWith(".html"))
            return "text/html";
        else
            return "text/plain";
    }

    private void stop404(OutputStream outputStream) throws IOException{
        write(outputStream, "HTTP/1.1 404 Not Found\n");
        write(outputStream, "Server: Quang's Server\n");
        write(outputStream, "Date: " + new Date() +"\n");
        write(outputStream, "Content-type: text/html");
        write(outputStream, "\n\n");
        outputStream.close();
    }
}

