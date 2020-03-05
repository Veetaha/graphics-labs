package v.e.e.t.a.h.a;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.GeneralPath;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

import javafx.geometry.Rectangle2D;

public class Main extends JPanel implements ActionListener {
    Timer timer;
    private float alpha = 1;
    private float delta = 0.15f;
    private float theta = 0;
    private float velocity = 0.03f;

    private static int maxWidth;
    private static int maxHeight;

    private static final int imageHeight = 250;
    private static final int imageWidth = 300;
    private static final double[][] manPoints = {
        { 207, 243 },
        { 207, 220 },
        { 197, 220 },
        { 197, 150 },
        { 216, 150 },
        { 216, 80 },
        { 173, 80 },
        { 173, 34 },
        { 120, 34 },
        { 120, 80 },
        { 80, 80 },
        { 80, 155 },
        { 103, 155 },
        { 104, 243 },
        { 150, 243 },
        { 150, 220 },
        { 138, 220 },
        { 138, 174 },
        { 167, 174 },
        { 167, 243 }
    };

    public Main() {
        timer = new Timer(10, this);
        timer.start();
    }

    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D ctx = (Graphics2D)g;

        ctx.setBackground(new Color(127, 255, 0));
        ctx.clearRect(0, 0, maxWidth, maxHeight);

        ctx.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        ctx.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

        var tx = maxWidth / 4 + imageWidth / 2;
        var ty = maxHeight / 2 + imageHeight / 2;

        ctx.translate(tx, ty);
        drawHuman(ctx);
        ctx.translate(-tx, -ty);

        BasicStroke bs1 = new BasicStroke(5, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
        ctx.setStroke(bs1);
        ctx.drawRect(maxWidth / 2, 0, maxWidth / 2 - 1, maxHeight - 1);
        ctx.setStroke(new BasicStroke());

        ctx.translate(tx + maxWidth / 2, ty);
        ctx.rotate(theta, - imageWidth / 2, - imageHeight / 2);

        ctx.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
        drawHuman(ctx);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Lab #2");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1800, 1000);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.add(new Main());
        frame.setVisible(true);
        var size = frame.getSize();
        var insets = frame.getInsets();
        maxWidth = size.width - insets.left - insets.right - 1;
        maxHeight = size.height - insets.top - insets.bottom - 1;
    }

    // @SuppressWarnings("all")
    public void actionPerformed(ActionEvent e) {
        if (alpha < 0.5f || alpha > 0.99f) {
            delta = -delta;
        }
        alpha += delta;

        theta += velocity;
        if (theta > 2 * Math.PI) {
            theta -= 2 * Math.PI;
        }
        repaint();
    }

    static class Colors {
        static Color background = Color.BLACK;
        static Color ear = Color.BLUE;
        static Color eye = Color.YELLOW;
        static Color imageBorder = Color.BLACK;
        static GradientPaint body = new GradientPaint(
            0, 0,   new Color(100, 165, 0),
            0, 140, new Color(124, 80, 21)
        );
    }

    void drawHuman(Graphics2D ctx) {
        var man = new GeneralPath();

        int originX = -imageWidth / 2;
        int originY = -imageHeight / 2;

        man.moveTo(originX + manPoints[0][0], originY + manPoints[0][1]);
        for (int i = 1; i < manPoints.length; i++) {
            man.lineTo(originX + manPoints[i][0], originY + manPoints[i][1]);
        }
        man.closePath();

        ctx.setPaint(Colors.body);
        ctx.fill(man);

        ctx.setPaint(Colors.eye);

        ctx.fillRect(originX + 134, originY + 46, 7, 7);
        ctx.fillRect(originX + 154, originY + 45, 7, 7);

        var leftEar = new GeneralPath();
        leftEar.moveTo(originX + 122, originY + 36);
        leftEar.lineTo(originX + 122, originY + 4);
        leftEar.lineTo(originX + 92, originY + 14);

        var rightEar = new GeneralPath();

        rightEar.moveTo(originX + 204, originY + 36);
        rightEar.lineTo(originX + 172, originY + 6);
        rightEar.lineTo(originX + 170, originY + 36);

        ctx.setPaint(Colors.ear);

        ctx.fill(leftEar);
        ctx.fill(rightEar);

        ctx.setPaint(Colors.imageBorder);
        ctx.drawRect(originX, originY, imageWidth, imageHeight);
    }
}
