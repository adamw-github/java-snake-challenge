package snake_package;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.swing.*;

public class SnakeGame extends JFrame implements ActionListener, KeyListener {


    private class Snake {
        public int headPosX;
        public int headPosY;
        private int length; //not including head (length 0 means just head)
        public String direction = "north"; //nesw

        public Snake(int x, int y){
            headPosX = x;
            headPosY = y;
            length = 3;
        }

        public int getLength() {
            return this.length;
        }
        public void setLength(int length){
            this.length = length;
        }

        public void setDirection(String direction){
            this.direction = direction;
        }

        public void eat(){
            this.length += 1;
        }
    }

    private Snake conda;
    private GameLabel labels[][];
    private final int game_tick;
    private final JButton resetButton;
    private final int size;
    private boolean gameOver = false;
    private boolean hasFood = false;
    private Timer timer;
    private String currDirection;
    List<GameLabel> snakeTiles = new ArrayList<>();

    public SnakeGame(){
        this(16, 150);
    }

    public SnakeGame(int size, int game_tick){
        super("Snake Game");
        this.size = size;
        this.game_tick = game_tick;
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel gamePanel = new JPanel();
        gamePanel.setLayout(new GridLayout(size, size));

        labels = new GameLabel[size][];
        for (int i = 0; i < size; i++) {
            labels[i] = new GameLabel[size];
            for (int j = 0; j < size; j++) {
                GameLabel label = new GameLabel();
                label.setPreferredSize(new Dimension(30, 30));
                label.setOpaque(true);
                label.setBackground(Color.BLACK);
                label.setBorder(BorderFactory.createLineBorder(Color.WHITE,1));
                gamePanel.add(label);
                labels[i][j] = label;
            }
        }
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(gamePanel,BorderLayout.CENTER);

        resetButton = new JButton("New Game");
        getContentPane().add(resetButton, BorderLayout.NORTH);
        resetButton.addActionListener(this);
        resetButton.addKeyListener(this);
        pack();

        conda = new Snake(size/2, size/2);
        run();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == resetButton){
            timer.setDelay(0);
            if(timer.isRunning()){
                timer.stop();
                System.out.println("stopped now");
            }else{
                System.out.println("already stopped");
            }
            timer.restart();

            for(int i = 0; i < size; i++){
                for(int j = 0; j < size; j++){
                    labels[i][j].setBackground(Color.BLACK);
                }
            }
            conda = new Snake(size/2, size/2);
            snakeTiles.clear();
            hasFood = false;
            play();
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }
    @Override
    public void keyReleased(KeyEvent e) {
    }
    @Override
    public void keyPressed(KeyEvent e) {

        switch(e.getKeyCode()){ //wasd
            case KeyEvent.VK_W, KeyEvent.VK_UP -> {
                tryMoveNorth();
            }
            case KeyEvent.VK_A, KeyEvent.VK_LEFT ->  {
                tryMoveWest();
            }
            case KeyEvent.VK_S, KeyEvent.VK_DOWN -> {
                tryMoveSouth();
            }
            case KeyEvent.VK_D, KeyEvent.VK_RIGHT -> {
                tryMoveEast();
            }
            case KeyEvent.VK_R -> {
                timer.start();
            }
            case KeyEvent.VK_P -> {
                timer.stop();
            }
        }
        //wasd
            }

    public void tryMoveNorth(){
        if(!"south".equals(currDirection)){
            conda.setDirection("north");
        }
    }
    public void tryMoveSouth(){
        if(!"north".equals(currDirection)){
            conda.setDirection("south");
        }
    }
    public void tryMoveEast(){
        if(!"west".equals(currDirection)){
            conda.setDirection("east");
        }
    }
    public void tryMoveWest(){
        if(!"east".equals(currDirection)){
            conda.setDirection("west");
        }
    }

    public int[] generateFood(List<GameLabel> snakeTiles){
        int foodPosX;
        int foodPosY;
        do{
            foodPosX = new Random().nextInt(size-1);
            foodPosY = new Random().nextInt(size-1);
        }while( snakeTiles.contains(labels[foodPosY][foodPosX]) );
        labels[foodPosY][foodPosX].setBackground(Color.GREEN);
        hasFood = true;
        return new int[]{foodPosX,foodPosY};
    }
    public void run(){
        setVisible(true);

        ActionListener playAction = new ActionListener(){
            int foodPosX;
            int foodPosY;
            public void actionPerformed(ActionEvent e) {
                setTitle("Snake Length: " + (conda.getLength() +1));

                if(!snakeTiles.isEmpty()) {
                    snakeTiles.get(snakeTiles.size() - 1).setBackground(Color.WHITE);

                    if (snakeTiles.get(0).timeLeft == 0) {
                        snakeTiles.get(0).setBackground(Color.BLACK);
                        snakeTiles.remove(0);
                    }
                }
                if(snakeTiles.size() >= size*size){
                    System.out.println("winner");
                    timer.stop();
                }

                if(!hasFood){
                    int[] foodTemp = generateFood(snakeTiles);
                    foodPosX = foodTemp[0];
                    foodPosY = foodTemp[1];
                }

                currDirection = conda.direction;
                switch(currDirection){
                    case "north": {
                        if(conda.headPosY !=0){
                            conda.headPosY -= 1;
                        }else{
                            gameOver = true;
                        }
                        break;
                    }
                    case "east": {
                        if(conda.headPosX != size -1){
                            conda.headPosX += 1;
                        }else{
                            gameOver = true;
                        }
                        break;
                    }
                    case "south":{
                        if(conda.headPosY != size-1){
                            conda.headPosY +=1;
                        }else{
                            gameOver = true;
                        }
                        break;
                    }
                    case "west":{
                        if(conda.headPosX != 0){
                            conda.headPosX -=1;
                        }else{
                            gameOver = true;
                        }
                        break;
                    }
                }

                if(snakeTiles.contains(labels[conda.headPosY][conda.headPosX])){
                    gameOver = true;
                }

                if(gameOver){   //game finished!!!!!!!!!!!!!!!!
                    System.out.println("done");
                    JOptionPane.showMessageDialog(getParent(), "Game Over! \n your final length was: " + (conda.getLength() +1));
                    timer.stop();
                }else{
                    GameLabel currTile = labels[conda.headPosY][conda.headPosX];
                    currTile.setBackground(Color.RED);
                    currTile.setTimeLeft(conda.getLength() + 1);
                    snakeTiles.add(currTile);

                    if(conda.headPosY == foodPosY && conda.headPosX == foodPosX){
                        conda.eat();
                        hasFood = false;
                        for(GameLabel tile: snakeTiles){
                            tile.setTimeLeft(tile.timeLeft + 1);
                        }
                        int[] foodTemp = generateFood(snakeTiles);
                        foodPosX = foodTemp[0];
                        foodPosY = foodTemp[1];
                    }

                    for (GameLabel activeTile : snakeTiles) {
                        activeTile.age();
                    }
                }

            }
        };

        timer = new Timer(game_tick, playAction);
        play();
    }
    public void play(){
        gameOver = false;
        timer.setDelay(game_tick);
        timer.start();
    }

    public static void main(String[] args){
        if(args.length != 2){
            System.out.println("Invalid arguments. Using default values: Size=16x16, tick=150");
            SnakeGame game = new SnakeGame();
        } else{
            int size = Integer.parseInt(args[0]);
            int tick = Integer.parseInt(args[1]);
            SnakeGame game = new SnakeGame(size, tick);
        }
    }
}