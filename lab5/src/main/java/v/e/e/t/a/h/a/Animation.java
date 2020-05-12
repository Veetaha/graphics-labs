package v.e.e.t.a.h.a;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.media.j3d.*;
import javax.swing.JFrame;
import javax.swing.Timer;
import javax.vecmath.*;

class Animation implements ActionListener, KeyListener {
    private Button startButton;
    private TransformGroup wholeWolf;
    private Transform3D transform;
    private Transform3D rotateTransformY;

    private Timer timer;
    private JFrame mainFrame;


    private float x = 0;
    private float y = 0;
    private float z = 0;

    private float scale = 0.5f;
    private int i = 1;

    public Animation(Canvas3D canvas, TransformGroup wholeWolf, Transform3D t, JFrame f){
        startButton = new Button("Start animation");
        this.wholeWolf = wholeWolf;
        this.transform = t;
        this.mainFrame = f;
        rotateTransformY = new Transform3D();

        canvas.addKeyListener(this);
        timer = new Timer(100, this);

        Panel p = new Panel();
        p.add(startButton);
        mainFrame.add("North", p);
        startButton.addActionListener(this);
        startButton.addKeyListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
       if (e.getSource() == startButton){
            if (!timer.isRunning()) {
                timer.start();
                x = 0.0f;
                y = -1.9499986f;
                z = -11.969989f;
                startButton.setLabel("Stop");
            } else {
                timer.stop();
                startButton.setLabel("Go");
            }
        } else {
            tick();
        }
    }

    private void tick() {
        //
        updatePos();
        transform.setScale(scale);

        // x -= 0.005 + i * 0.002;

        if (rot == 0 && z <= -1.4) {
            y += 0.001 + i * 0.0002;
            z += 0.005 + i * 0.002;

            i += 3;
            scale += 0.002;
        } else if (rot <= 13.4) {
            rotY(rot += .5);
        } else {
            y -= 0.001 + i * 0.0002;
            z -= 0.005 + i * 0.002;

            i -= 1;
            scale -= 0.002;
        }
    }
    float rot = 0;

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyChar()) {
            case 'a': rotY(1); break;
            case 'd': rotY(-1); break;
            case 'w': z -= 0.03; updatePos(); break;
            case 's': z += 0.03; updatePos(); break;
            case 'q': y -= 0.03; updatePos(); break;
            case 'e': y += 0.03; updatePos(); break;
            case 'f': x -= 0.03; updatePos(); break;
            case 'g': x += 0.03; updatePos(); break;
        }
        System.err.println("(" + x + ", " + y + ", " + z + ")");
    }
    private void updatePos() {
        transform.setTranslation(new Vector3f(x, y, z));
        wholeWolf.setTransform(transform);
    }

    private void rotY(float howMany) {
        rotateTransformY.rotY(deg(howMany));
        transform.mul(rotateTransformY);
        wholeWolf.setTransform(transform);
    }

    private double deg(double degree) {
        return 0.0174533 * degree;
    }

    @Override
    public void keyReleased(KeyEvent e) {}

    @Override
    public void keyTyped(KeyEvent e) {}

}
