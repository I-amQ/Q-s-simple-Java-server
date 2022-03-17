import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.ServerSocket;

public class gui{

    static int choice = 420;

    static String[] array = server.Address.split("/");

    static String address = array[array.length-1];

    public static void main(String[] args){
        createWindow();
    }

    public static void createWindow(){
        JFrame frame = new JFrame("SERVER_GUI_BY_TRUONG_VAN_QUANG VER 1.1");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        createUI(frame);
        frame.setSize(500,600);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        frame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                if (JOptionPane.showConfirmDialog(frame,
                        "Are you sure you want to close the server?", "Close Window?",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION){
                    Runtime.getRuntime().halt(0);
                }
            }
        });
    }

    private static void createUI(final JFrame frame){
        JPanel panel = new JPanel();
        LayoutManager layout = new BorderLayout();
        panel.setLayout(layout);

        JButton button = new JButton("RUN");


        JComboBox<Integer> dropDownList = new JComboBox<>();

        
        dropDownList.addItem(420);
        dropDownList.addItem(500);
        dropDownList.addItem(999);
        dropDownList.addItem(8080);


        dropDownList.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                    choice = Integer.parseInt(dropDownList.getSelectedItem().toString());
                    System.out.println("You just selected port: "+ choice);
                    server.PORT = choice;
            }
        });




        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                RunMyProgram myProgram = new RunMyProgram();
                Thread thread = new Thread(myProgram);

                if (button.getText().equals("RUN")){

                    button.setText("STOP");

                    myProgram = new RunMyProgram();

                    thread = new Thread(myProgram);

                    thread.start();

                    System.out.println("\tOpening PORT: " + choice);

                    System.out.println("ADDRESS FOR LOCAL HOST: "+ address +":"+choice);


                }else {

                    button.setText("RUN");

                    System.out.println("Closing...");;

                    thread.interrupt();;

                    try {
                        server.the_server_socket.close();
                    }catch (IOException exception){
                        System.out.println("IOException");
                    }

                    System.out.println("Closed");

                }
            }



            });




        JTextArea textArea = new JTextArea("Barebone JAVA HTTP SERVER, DEVELOPED BY TRUONG VAN QUANG ! \n");

        JPanel buttonPanel = new JPanel();
        JPanel dropDownPanel = new JPanel(new FlowLayout());
        JPanel textAreaPanel = new JPanel(new BorderLayout());

        dropDownPanel.add(new JLabel("PORT: "));
        dropDownPanel.add(dropDownList);

        textAreaPanel.add(textArea);

        TextAreaOutputStream taos = new TextAreaOutputStream( textArea,"Console");
        PrintStream ps = new PrintStream( taos );
        System.setOut( ps );
        System.setErr( ps );

        buttonPanel.add(button, BorderLayout.CENTER);

        panel.add(dropDownPanel, BorderLayout.PAGE_START);
        panel.add(textAreaPanel, BorderLayout.CENTER);
        panel.add(new JScrollPane(textArea));
        panel.add(buttonPanel, BorderLayout.PAGE_END);

        frame.add(panel);
        frame.pack();
        frame.setVisible(true);
    }

}

class TextAreaOutputStream extends OutputStream {

    private final JTextArea textArea;
    private final StringBuilder sb = new StringBuilder();
    private String title;

    public TextAreaOutputStream(final JTextArea textArea, String title) {
        this.textArea = textArea;
        this.title = title;
        sb.append(title + "> ");
    }

    @Override
    public void flush() {
    }

    @Override
    public void close() {
    }

    @Override
    public void write(int b) throws IOException {

        if (b == '\r')
            return;

        if (b == '\n') {
            final String text = sb.toString() + "\n";
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    textArea.append(text);
                }
            });
            sb.setLength(0);
            sb.append(title).append("> ");
        }

        sb.append((char) b);
    }
}

class RunMyProgram implements Runnable{

    @Override
    public void run() {
        try {

            server.PORT = gui.choice;

            server.the_server_socket = new ServerSocket(server.PORT);

            System.out.println("Server_socket available, awaiting connection! Port: " + server.PORT);

            server myServer = new server();

            System.out.println("Server is now operational!");



            while( myServer.the_server_socket.isBound() && myServer.the_server_socket != null ) {

                Thread thread = new Thread(myServer);

                thread.start();

                thread.join(200);
            }



        }catch (Exception ex){
            System.out.print("");
        }
    }
}

//made by Truong Van Quang.