import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Random;

public class SolarSystem extends JPanel implements MouseListener {

    private BufferedImage buffer;
    private BufferedImage background;
    private BufferedImage rectangles;
    private Graphics rectanglesG;
    private Graphics realBackground;
    private int planetSection = 200;
    private int screenWidth = 1214;
    private int screenHeight = 737;
    private int centerPointsX[] = new int[8];
    private int centerPointsY[] = new int[8];
    private int centerPointsElipse[] = new int[2];
    private int angleElipse = 0;
    private int initialElipseAngle = 0;
    private double anglePoints[] = new double[8];
    private boolean circleThickFlags[] = new boolean[8];
    private boolean flag = false;
    private boolean drawRectangles = false;
    private double angle = 0;
    private ImageIcon mercury;
    private ImageIcon venus;
    private ImageIcon earth;
    private ImageIcon mars;
    private ImageIcon jupiter;
    private ImageIcon saturn;
    private ImageIcon uranus;
    private ImageIcon neptune;

    public SolarSystem() {

        addMouseListener(this);
        definePanel();
        initializeValues();
        drawPlanetsSection();
        setDoubleBuffered(true);
        playAudio();

    }

    @Override
    public void paint(Graphics g) {
        update(g);
        clear();
        int center = (getWidth() - planetSection) / 2;
        int initialPositionXPlanet = ((getWidth() - planetSection) + 5);
        int initialPositionYPlanet = 3;

        //Dibujar imágenes
        g.drawImage(rectangles, getWidth() - planetSection, 0, null);

        //Dibujar círculos de animación
        drawCirculoRelleno(center, getHeight() / 2, 40, Color.WHITE, new Color(205, 56, 0), -1);

        drawCirculoRelleno(centerPointsX[0], centerPointsY[0], 8, Color.WHITE, Color.YELLOW, 0);
        drawCirculoRelleno(centerPointsX[1], centerPointsY[1], 9, Color.WHITE, Color.ORANGE, 1);
        drawCirculoRelleno(centerPointsX[2], centerPointsY[2], 10, Color.WHITE, Color.BLUE, 2);
        drawCirculoRelleno(centerPointsX[3], centerPointsY[3], 10, Color.WHITE, new Color(202, 137, 106), 3);
        drawCirculoRelleno(centerPointsX[4], centerPointsY[4], 15, Color.WHITE, new Color(109, 60, 26), 4);
        drawCirculoRelleno(centerPointsX[5], centerPointsY[5], 12, Color.WHITE, new Color(185, 161, 120), 5);
        drawCirculoRelleno(centerPointsX[6], centerPointsY[6], 10, Color.WHITE, new Color(176, 197, 206), 6);
        drawCirculoRelleno(centerPointsX[7], centerPointsY[7], 10, Color.WHITE, new Color(175, 199, 247), 7);
        drawCirculoRelleno(centerPointsElipse[0], centerPointsElipse[1], 5, Color.WHITE, new Color(219, 204, 185), -1);
        AlgortmoPolarCirculo(center, getHeight() / 2, 60, Color.GRAY);
        AlgortmoPolarCirculo(center, getHeight() / 2, 90, Color.GRAY);
        AlgortmoPolarCirculo(center, getHeight() / 2, 120, Color.GRAY);
        AlgortmoPolarCirculo(center, getHeight() / 2, 150, Color.GRAY);
        drawCirculoTipo(center, getHeight() / 2, 165, 3, 3);
        AlgortmoPolarCirculo(center, getHeight() / 2, 180, Color.GRAY);
        AlgortmoPolarCirculo(center, getHeight() / 2, 210, Color.GRAY);
        AlgortmoPolarCirculo(center, getHeight() / 2, 240, Color.GRAY);
        AlgortmoPolarCirculo(center, getHeight() / 2, 270, Color.GRAY);
        drawElipse(center, getHeight() / 2, 300, 400, 1000);
        drawLineaBresenham(getWidth() - planetSection, 0, getWidth() - planetSection, getHeight(), Color.WHITE);

    }

    @Override
    public void update(Graphics g) {
        g.drawImage(background, 0, 0, this);
    }

    public void clear() {
        realBackground.setColor(Color.BLACK);
        realBackground.fillRect(0, 0, getWidth(), getHeight());
    }

    public void definePanel() {

        setSize(screenWidth, screenHeight);
        setVisible(true);
        buffer = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
        background = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
        realBackground = background.getGraphics();
        realBackground.setColor(Color.BLACK);
        realBackground.fillRect(0, 0, getWidth(), getHeight());

    }

    public void initializeValues() {

        //Establecer imagenes
        mercury = new ImageIcon("D:\\CETI_2da_parte\\6to_semestre\\Graficas2Dy3D\\SolarSystem\\images\\mercury-bg.png");
        venus = new ImageIcon("D:\\CETI_2da_parte\\6to_semestre\\Graficas2Dy3D\\SolarSystem\\images\\venus-bg.png");
        earth = new ImageIcon("D:\\CETI_2da_parte\\6to_semestre\\Graficas2Dy3D\\SolarSystem\\images\\earth-bg.png");
        mars = new ImageIcon("D:\\CETI_2da_parte\\6to_semestre\\Graficas2Dy3D\\SolarSystem\\images\\mars-bg.png");
        jupiter = new ImageIcon("D:\\CETI_2da_parte\\6to_semestre\\Graficas2Dy3D\\SolarSystem\\images\\jupiter-bg.png");
        saturn = new ImageIcon("D:\\CETI_2da_parte\\6to_semestre\\Graficas2Dy3D\\SolarSystem\\images\\saturn-bg.png");
        uranus = new ImageIcon("D:\\CETI_2da_parte\\6to_semestre\\Graficas2Dy3D\\SolarSystem\\images\\uranus-bg.png");
        neptune = new ImageIcon("D:\\CETI_2da_parte\\6to_semestre\\Graficas2Dy3D\\SolarSystem\\images\\neptune-bg.png");

        rectangles = new BufferedImage(planetSection, 701, BufferedImage.TYPE_INT_RGB);
        rectanglesG = rectangles.getGraphics();
        rectanglesG.setColor(Color.BLACK);
        rectanglesG.fillRect(0, 0, planetSection, 701);

        calculateInitialCenters();
        calculateInitialElipse();

    }

    public void calculateInitialCenters() {

        double y;
        double x;
        Random random = new Random();
        int centerX = (1200 - planetSection) / 2;
        int centerY = 700 / 2;
        int radius = 60;

        int randomNumber;

        double newAngle;

        for(int i = 0; i < centerPointsX.length; i++) {

            randomNumber = random.nextInt(643);
            newAngle = 0.01 * randomNumber;
            anglePoints[i] = newAngle;
            x = centerX + radius * Math.sin(newAngle);
            y = centerY + radius * Math.cos(newAngle);
            centerPointsX[i] = (int) Math.round(x);
            centerPointsY[i] = (int) Math.round(y);
            radius += 30;

        }

    }

    public void calculateInitialElipse() {

        int x = 0, y = 0;
        Random random = new Random();
        double angulo = 2*Math.PI/1000, a = 0;
        int randomNumber;

        randomNumber = random.nextInt(1100);

        a = randomNumber*angulo;
        initialElipseAngle = randomNumber;
        angleElipse = randomNumber;
        x = (int)(((1200 - planetSection) / 2) + 400*Math.cos(a));
        y = (int)((700 / 2) + 300*Math.sin(a));
        centerPointsElipse[0] = x;
        centerPointsElipse[1] = y;


    }

    public void drawPlanetsSection() {

        drawRectangles = true;
        drawFilledRectangle2();
        drawRectangles = false;

        Graphics2D g2d = rectangles.createGraphics();
        // Add text
        g2d.setColor(Color.BLACK); // Set the color of the text
        g2d.setFont(new Font("SansSerif", Font.PLAIN, 20)); // Set the font

        rectangles.getGraphics().drawImage(mercury.getImage(), 3, 3, null);
        rectangles.getGraphics().drawImage(venus.getImage(), 3, 90, null);
        rectangles.getGraphics().drawImage(earth.getImage(), 3, 178, null);
        rectangles.getGraphics().drawImage(mars.getImage(), 3, 265, null);
        rectangles.getGraphics().drawImage(jupiter.getImage(), 3, 353, null);
        rectangles.getGraphics().drawImage(saturn.getImage(), 3, 440, null);
        rectangles.getGraphics().drawImage(uranus.getImage(), 3, 528, null);
        rectangles.getGraphics().drawImage(neptune.getImage(), 3, 615, null);

        g2d.drawString("Mercury", 100, 20);
        g2d.drawString("Venus", 100, 107);
        g2d.drawString("Earth", 100, 195);
        g2d.drawString("Mars", 100, 282);
        g2d.drawString("Jupiter", 100, 370);
        g2d.drawString("Saturn", 100, 457);
        g2d.drawString("Uranus", 100, 545);
        g2d.drawString("Neptune", 100, 632);



    }

    public void drawFilledRectangle() {

        int ancho;
        int posicionY = 0;
        for(int i = 0; i <= 8; i++) {

            if(i % 2 == 0) ancho = 87;
            else ancho = 88;
            if(i > 0) posicionY = posicionY + ancho;
            drawRectangulo(getWidth() - planetSection, posicionY, ancho, planetSection, Color.WHITE);
            if( i <= 7) scanLine((getWidth() - planetSection) + 1, posicionY + 1, getWidth() - 1, (posicionY + ancho) - 1, Color.GRAY);

        }

    }

    public void drawFilledRectangle2() {

        int ancho;
        int posicionY = 0;
        for(int i = 0; i <= 8; i++) {

            if(i % 2 == 0) ancho = 87;
            else ancho = 88;
            if(i > 0) posicionY = posicionY + ancho;
            drawRectangulo(0, posicionY, ancho, planetSection, Color.WHITE);
            if(i <= 7) scanLine(0, posicionY, planetSection - 1, (posicionY + ancho) - 1, Color.GRAY);

        }

    }

    public void putPixel(int x, int y, Color c) {
        if(flag) {
            int width = 3;
            int height = 3;
            buffer = new BufferedImage(3, 3, BufferedImage.TYPE_INT_RGB);
            buffer.setRGB(0, 0, c.getRGB());
            buffer.setRGB(0, 1, c.getRGB());
            buffer.setRGB(0, 2, c.getRGB());
            buffer.setRGB(1, 2, c.getRGB());
            buffer.setRGB(1, 0, c.getRGB());
            buffer.setRGB(1, 1, c.getRGB());
            buffer.setRGB(2, 0, c.getRGB());
            buffer.setRGB(2, 1, c.getRGB());
            buffer.setRGB(2, 2, c.getRGB());
        }
        else {
            buffer = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
            buffer.setRGB(0, 0, c.getRGB());
        }

        background.getGraphics().drawImage(buffer, x, y, this);
        repaint();
    }

    public void putPixelRectangles(int x, int y, Color c) {

        buffer = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
        buffer.setRGB(0, 0, c.getRGB());
        rectangles.getGraphics().drawImage(buffer, x, y, this);
        repaint();
    }

    public void mouseClicked(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();

        int startingXPoint = (1200 - planetSection) + 3;

        Arrays.fill(circleThickFlags, false);

        if(x > startingXPoint && x <= startingXPoint + mercury.getIconWidth() && y >= 3 && y <= mercury.getIconHeight()) {

            circleThickFlags[0] = true;


        } else if(x > startingXPoint && x <= startingXPoint + venus.getIconWidth() && y >= 90 && y <= 90 + venus.getIconHeight()) {

            circleThickFlags[1] = true;

        } else if(x > startingXPoint && x <= startingXPoint + earth.getIconWidth() && y >= 178 && y <= 178 + earth.getIconHeight()) {

            circleThickFlags[2] = true;
        } else if(x > startingXPoint && x <= startingXPoint + mars.getIconWidth() && y >= 265 && y <= 265 + mars.getIconHeight()) {

            circleThickFlags[3] = true;

        } else if(x > startingXPoint && x <= startingXPoint + jupiter.getIconWidth() && y >= 353 && y <= 353 + jupiter.getIconHeight()) {

            circleThickFlags[4] = true;

        } else if(x > startingXPoint && x <= startingXPoint + saturn.getIconWidth() && y >= 440 && y <= 440 + saturn.getIconHeight()) {

            circleThickFlags[5] = true;

        } else if(x > startingXPoint && x <= startingXPoint + uranus.getIconWidth() && y >= 528 && y <= 528 + uranus.getIconHeight()) {

            circleThickFlags[6] = true;

        } else if(x > startingXPoint && x <= startingXPoint + neptune.getIconWidth() && y >= 615 && y <= 615 + neptune.getIconHeight()) {

            circleThickFlags[7] = true;

        } else if(x >= ((1200 - planetSection) / 2) - 40 && x <= ((1200 - planetSection) / 2) + 40 && y >= ((700 / 2) - 40) && y <= ((700 / 2) + 40)) {

            calculateInitialCenters();
            calculateInitialElipse();

        }

    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    public void repeatDraws() {
        Timer timer = new Timer(10, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                calculateNewCenters();
                calculateNewCenterElipse();

                repaint();
            }
        });
        timer.setRepeats(true);
        timer.start();
    }

    public void calculateNewCenters() {

        double y;
        double x;
        int centerX = (1200 - planetSection) / 2;
        int centerY = 700 / 2;
        int radius = 60;

        if(angle >= Math.PI * 2.05) angle = 0;
        for(int i = 0; i < centerPointsX.length; i++) {

            angle = anglePoints[i] + 0.01;
            anglePoints[i] = angle;
            x = centerX + radius * Math.sin(angle);
            y = centerY + radius * Math.cos(angle);
            centerPointsX[i] = (int) Math.round(x);
            centerPointsY[i] = (int) Math.round(y);
            radius += 30;

        }

    }

    public void calculateNewCenterElipse() {

        int x = 0, y = 0;
        double angulo = 2*Math.PI/1000, a= 0;

        //if(angleElipse > 1100) {
        //    angleElipse = initialElipseAngle + 1100;
        //} else {
            angleElipse++;
        //}

        a = (angleElipse)*angulo;
        x = (int)(((1200 - planetSection) / 2) + 400*Math.cos(a));
        y = (int)((700 / 2) + 300*Math.sin(a));
        centerPointsElipse[0] = x;
        centerPointsElipse[1] = y;

    }

    public void drawCirculoRelleno(int xc, int yc, int r, Color linea, Color background, int index){
        try {
            int x = 0, y = r, d = 3 - 2 * r;

            if(index >= 0){
                flag = circleThickFlags[index];
            }

            while (x <= y) {

                putPixel(xc + x, yc + y, linea);
                putPixel(xc + y, yc + x, linea);
                putPixel(xc - x, yc + y, linea);
                putPixel(xc - y, yc + x, linea);
                putPixel(xc - x, yc - y, linea);
                putPixel(xc - y, yc - x, linea);
                putPixel(xc + x, yc - y, linea);
                putPixel(xc + y, yc - x, linea);

                if (d < 0) {

                    d = d + 4 * x + 6;

                } else {
                    d = d + 4 * (x - y) + 10;
                    y--;
                }
                x++;
            }

            flag = false;

            int IntDelColorOld;
            IntDelColorOld = this.background.getRGB(xc, yc);

            Color ColorOld = new Color(IntDelColorOld);

            if (ColorOld.equals(Color.BLACK)) {
                putPixel(xc, yc, background);
                floodFill(xc + 1, yc, background);
                floodFill(xc - 1, yc, background);
                floodFill(xc, yc + 1, background);
                floodFill(xc, yc - 1, background);
            }
        }catch (Exception e){}
    }

    public void AlgortmoPolarCirculo(int xc, int yc, int r, Color c) {

        double y;
        double x;

        for (double t = 0; t <= Math.PI * 2.05; t += 0.01) {
            x = xc + r * Math.sin(t);
            y = yc + r * Math.cos(t);
            putPixel((int) Math.round(x), (int) Math.round(y), c);
        }


    }

    public void drawRectangulo(int x, int y, int a, int l, Color c) {

        int x0 = x;
        int x1 = x + l;
        int y0 = y;
        int y1 = y + a;

            drawLineaBresenham(x0, y0, x0, y1, c);
            drawLineaBresenham(x1, y0, x1, y1, c);
            drawLineaBresenham(x0, y1, x1, y1, c);
            drawLineaBresenham(x0, y0, x1, y0, c);


    }

    public void floodFill(int x, int y, Color c) {
        int IntDelColorOld;
        IntDelColorOld = this.background.getRGB(x, y);
        Color ColorOld = new Color(IntDelColorOld);

        if (ColorOld.equals(Color.BLACK)) {
            putPixel(x, y, c);
            floodFill(x + 1, y, c);
            floodFill(x - 1, y, c);
            floodFill(x, y + 1, c);
            floodFill(x, y - 1, c);
        }
    }



    public void scanLine(int x0, int y0, int x1, int y1, Color c) {

        int x = x0, y = y0;
        Color pixelColor = Color.BLACK;

        while (y<=y1) {
            if(drawRectangles) {
                pixelColor = new Color(rectangles.getRGB(x, y));
            } else {
                pixelColor = new Color(background.getRGB(x, y));
            }

            if(pixelColor.equals(Color.BLACK)) {
                if(drawRectangles){
                    putPixelRectangles(x, y, c);
                } else {
                    putPixel(x, y, c);
                }
            }
            if(x<x1) {
                x++;
            }
            else if(x==x1) {
                x=x0;
                y++;
            }
        }
    }

    public void drawCirculoTipo(int xc, int yc, int r, int up, int down) {

        BitSet mask = new BitSet(up + down);
        double x1, y1, x2, y2, xt1, xt2, yt1, yt2, k=0;
        int  delta = (yc>=xc) ? yc - xc: xc - yc;
        putPixel(xc + r, 0, Color.WHITE);
        putPixel(xc - r, 0, Color.WHITE);
        putPixel(0, yc + r, Color.WHITE);
        putPixel(0, yc - r, Color.WHITE);

        for(int a = 0; a<up; a++) {

            mask.set(a);

        }


        for (double t = 0; t <= Math.PI /4; t += 0.01) {

            x1 = xc + Math.round(r * Math.cos(t));
            y1 = yc + Math.round(r * Math.sin(t));
            x2 = xc - Math.round(r * Math.cos(t));
            y2 = yc - Math.round(r * Math.sin(t));

            if(yc>xc) {

                xt1 = y1 - delta;
                xt2 = y2 - delta;
                yt1 = x1 + delta;
                yt2 = x2 + delta;

            }else {

                xt1 = y1 + delta;
                xt2 = y2 + delta;
                yt1 = x1 - delta;
                yt2 = x2 - delta;

            }

            if(mask.get((int) (k%(up + down)))) {

                putPixel((int) x1, (int) y1, Color.WHITE); // I.D
                putPixel((int) x1, (int) y2, Color.WHITE); // I.I
                putPixel((int) x2, (int) y1, Color.WHITE); // S.D
                putPixel((int) x2, (int) y2, Color.WHITE); // S.I

                putPixel((int) xt1, (int) yt1, Color.WHITE); // I.D
                putPixel((int) xt1, (int) yt2, Color.WHITE); // I.I
                putPixel((int) xt2, (int) yt1, Color.WHITE); // S.D
                putPixel((int) xt2, (int) yt2, Color.WHITE); // S.I

            }
            k++;

        }

    }

    public void drawLineaBresenham(int x0, int y0, int x1, int y1, Color c) {

        int dx = Math.abs(x1 - x0);
        int dy = Math.abs(y1 - y0);
        int sx = (x0 < x1) ? 1 : -1;
        int sy = (y0 < y1) ? 1 : -1;
        int err = dx - dy;
        int e2;

        // putPixel(x0, y0);

        while(x0 != x1 || y0 != y1) {
            if(drawRectangles) putPixelRectangles(x0, y0, c);
            else putPixel(x0, y0, c);
            e2 = 2 * err;
            if (e2 > -dy) {
                err = err - dy;
                x0 = x0 + sx;
            }
            if (e2 < dx) {
                err = err + dx;
                y0 = y0 + sy;
            }

        }

        if(drawRectangles) putPixelRectangles(x1, y1, c);
        else putPixel(x1, y1, c);
    }

    public void drawElipse(int xc, int yc, int r1, int r2, int def) {

        int x = 0, y = 0;
        double angulo = 2*Math.PI/def, a= 0;

        for(int i=0; i<=def+10; i++) {

            a = i*angulo;
            x = (int)(xc + r2*Math.cos(a));
            y = (int)(yc + r1*Math.sin(a));
            putPixel(x, y, Color.WHITE);

        }

    }

    public void playAudio() {
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File("D:\\CETI_2da_parte\\6to_semestre\\Graficas2Dy3D\\SolarSystem\\audio\\Interestellar.wav")); // Change "your_audio_file.wav" to the actual audio file path
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);

            clip.loop(Clip.LOOP_CONTINUOUSLY); // Loop the audio infinitely

            clip.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
