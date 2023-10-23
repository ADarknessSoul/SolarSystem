import javax.swing.*;

public class Main {



    public static void main(String[] args) {

        int screenWidth = 1214;
        int screenHeight = 737;

        JFrame frame = new JFrame("Sistema solar");
        SolarSystem mss = new SolarSystem();
        frame.setResizable(false);
        frame.add(mss);
        frame.setSize(screenWidth, screenHeight);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        mss.requestFocus();
        mss.repeatDraws();

    }

}
