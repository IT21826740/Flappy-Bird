import javax.swing.*;
public class App {
    public static void main(String[] args) throws Exception {
       int boardwidth = 360;
       int boardheight = 640;

       JFrame frame = new JFrame();
       frame.setTitle("Flappy Bird");
       frame.setSize(boardwidth, boardheight);
       frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
       //frame.setVisible(true);
       frame.setResizable(false);
       frame.setLocationRelativeTo(null);

       FlappyBird flappyBird = new FlappyBird();
       frame.add(flappyBird);
       frame.pack();
       frame.requestFocus();
       frame.setVisible(true);
    }
}
