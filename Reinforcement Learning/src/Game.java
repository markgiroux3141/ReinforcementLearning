import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Game extends JFrame implements ActionListener
{
    private Simulator simulator = new Simulator();
    private MapSimulator mapSimulator = new MapSimulator();
    private ISimulator currentSimulator = simulator;
    private GamePanel gamePanel = new GamePanel(currentSimulator);
    private JButton startButton = new JButton("Start");
    private JButton createMap = new JButton("Create Map");
    private JButton saveAgent = new JButton("Save Agent");
    private JButton runAgent = new JButton("Run Agent");
    private JButton quitButton = new JButton("Quit");
    private JButton pauseButton = new JButton("Pause");
    private CustomMouseListener customMouseListener = new CustomMouseListener(currentSimulator);
    private boolean running = false;
    private boolean paused = false;
    private boolean mapMode = false;
    private int fps = 60;
    private int frameCount = 0;

    public Game()
    {
        super("Reinforcement Learning");
        Container cp = getContentPane();
        cp.setLayout(new BorderLayout());
        JPanel p = new JPanel();
        p.setLayout(new GridLayout(1,2));
        p.add(startButton);
        p.add(createMap);
        p.add(saveAgent);
        p.add(runAgent);
        p.add(pauseButton);
        p.add(quitButton);
        this.addMouseListener(customMouseListener);
        cp.add(gamePanel, BorderLayout.CENTER);
        cp.add(p, BorderLayout.SOUTH);
        setSize(Simulator.SIMULATOR_SIZE_X, Simulator.SIMULATOR_SIZE_Y);

        startButton.addActionListener(this);
        createMap.addActionListener(this);
        saveAgent.addActionListener(this);
        runAgent.addActionListener(this);
        quitButton.addActionListener(this);
        pauseButton.addActionListener(this);
    }

    public static void main(String[] args)
    {
        Game glt = new Game();
        glt.setVisible(true);
    }

    public void actionPerformed(ActionEvent e)
    {
        Object s = e.getSource();
        if (s == startButton)
        {
            running = !running;
            if (running)
            {
                startButton.setText("Stop");
                runGameLoop();
            }
            else
            {
                startButton.setText("Start");
            }
        }
        else if (s == createMap)
        {
            mapMode = !mapMode;
            if(mapMode){
                this.currentSimulator = mapSimulator;
                this.customMouseListener.setSimulator(mapSimulator);
                this.gamePanel.setSimulator(mapSimulator);
                createMap.setText("Exit Map Mode");
            }else{
                this.mapSimulator.printMap("map1");
                this.currentSimulator = simulator;
                this.customMouseListener.setSimulator(simulator);
                this.gamePanel.setSimulator(simulator);
                createMap.setText("Create Map");
            }
        }
        else if(s == saveAgent){
            simulator.getGeneticAlgorithm().saveAgentAtNextIteration();
        }
        else if(s == runAgent){
            simulator.loadAgent();
        }
        else if (s == pauseButton)
        {
            paused = !paused;
            if (paused)
            {
                pauseButton.setText("Unpause");
            }
            else
            {
                pauseButton.setText("Pause");
            }
        }
        else if (s == quitButton)
        {
            System.exit(0);
        }
    }

    //Starts a new thread and runs the game loop in it.
    public void runGameLoop()
    {
        Thread loop = new Thread()
        {
            public void run()
            {
                gameLoop();
            }
        };
        loop.start();
    }

    //Only run this in another Thread!
    private void gameLoop()
    {
        //This value would probably be stored elsewhere.
        final double GAME_HERTZ = 60.0;
        //Calculate how many ns each frame should take for our target game hertz.
        final double TIME_BETWEEN_UPDATES = 1000000000 / GAME_HERTZ;
        //At the very most we will update the game this many times before a new render.
        //If you're worried about visual hitches more than perfect timing, set this to 1.
        final int MAX_UPDATES_BEFORE_RENDER = 5;
        //We will need the last update time.
        double lastUpdateTime = System.nanoTime();
        //Store the last time we rendered.
        double lastRenderTime = System.nanoTime();

        //If we are able to get as high as this FPS, don't render again.
        final double TARGET_FPS = 60;
        final double TARGET_TIME_BETWEEN_RENDERS = 1000000000 / TARGET_FPS;

        //Simple way of finding FPS.
        int lastSecondTime = (int) (lastUpdateTime / 1000000000);

        while (running)
        {
            double now = System.nanoTime();
            int updateCount = 0;

            if (!paused)
            {
                //Do as many game updates as we need to, potentially playing catchup.
                while( now - lastUpdateTime > TIME_BETWEEN_UPDATES && updateCount < MAX_UPDATES_BEFORE_RENDER )
                {
                    updateGame();
                    lastUpdateTime += TIME_BETWEEN_UPDATES;
                    updateCount++;
                }

                //If for some reason an update takes forever, we don't want to do an insane number of catchups.
                //If you were doing some sort of game that needed to keep EXACT time, you would get rid of this.
                if ( now - lastUpdateTime > TIME_BETWEEN_UPDATES)
                {
                    lastUpdateTime = now - TIME_BETWEEN_UPDATES;
                }

                //Render. To do so, we need to calculate interpolation for a smooth render.
                float interpolation = Math.min(1.0f, (float) ((now - lastUpdateTime) / TIME_BETWEEN_UPDATES) );
                drawGame(interpolation);
                lastRenderTime = now;

                //Update the frames we got.
                int thisSecond = (int) (lastUpdateTime / 1000000000);
                if (thisSecond > lastSecondTime)
                {
                    //System.out.println("NEW SECOND " + thisSecond + " " + frameCount);
                    fps = frameCount;
                    frameCount = 0;
                    lastSecondTime = thisSecond;
                }

                //Yield until it has been at least the target time between renders. This saves the CPU from hogging.
                while ( now - lastRenderTime < TARGET_TIME_BETWEEN_RENDERS && now - lastUpdateTime < TIME_BETWEEN_UPDATES)
                {
                    Thread.yield();

                    //This stops the app from consuming all your CPU. It makes this slightly less accurate, but is worth it.
                    //You can remove this line and it will still work (better), your CPU just climbs on certain OSes.
                    //FYI on some OS's this can cause pretty bad stuttering. Scroll down and have a look at different peoples' solutions to this.
                    try {Thread.sleep(1);} catch(Exception e) {}

                    now = System.nanoTime();
                }
            }
        }
    }

    private void updateGame()
    {
        gamePanel.update();
    }

    private void drawGame(float interpolation)
    {
        gamePanel.setInterpolation(interpolation);
        gamePanel.repaint();
        Toolkit.getDefaultToolkit().sync();
    }

    private class GamePanel extends JPanel
    {
        ISimulator simulator;
        float interpolation;

        public GamePanel(ISimulator simulator)
        {
            this.simulator = simulator;
        }

        public void setInterpolation(float interp)
        {
            interpolation = interp;
        }

        public void update()
        {
            simulator.run(interpolation);
        }

        public void paintComponent(Graphics g)
        {
            g.clearRect(0,0, Simulator.SIMULATOR_SIZE_X, Simulator.SIMULATOR_SIZE_Y);
            simulator.drawGraphics(g);

            frameCount++;
        }

        public ISimulator getSimulator() {
            return simulator;
        }

        public void setSimulator(ISimulator simulator) {
            this.simulator = simulator;
        }
    }

    class CustomMouseListener implements MouseListener {
        private ISimulator simulator;

        public CustomMouseListener(ISimulator simulator){
            this.simulator = simulator;
        }

        public void mouseClicked(MouseEvent e) {
            simulator.receiveInput(e);
        }
        public void mousePressed(MouseEvent e) {
        }
        public void mouseReleased(MouseEvent e) {
        }
        public void mouseEntered(MouseEvent e) {
        }
        public void mouseExited(MouseEvent e) {
        }

        public ISimulator getSimulator() {
            return simulator;
        }

        public void setSimulator(ISimulator simulator) {
            this.simulator = simulator;
        }
    }

}