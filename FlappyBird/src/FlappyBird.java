import java.awt.*;
import java.awt.event.*;
import javax.swing.*;   
import java.util.ArrayList;
import java.util.Random;

public class FlappyBird extends JPanel implements ActionListener,KeyListener{
    int boardwidth = 360;
    int boardheight = 640;

    //Images
    Image backgroundImg;
    Image birdImg;
    Image topPipeImg; 
    Image bottomPipeImg;

    //Bird
    int birdX = boardwidth/8;
    int birdY = boardheight/2;
    int birdWidth = 34;
    int birdHeight = 25;
    int birdSpeed = 0;

class Bird{

    int x = birdX;
    int y = birdY;
    int width = birdWidth;
    int height = birdHeight;
    Image img;

    Bird(Image img){
        this.img = img;
    }
}

//Pipes
int pipeX = boardwidth;
int pipeY = 0;
int pipeWidth = 64; 
int pipeHeight = 512;

class Pipe{
    int x = pipeX;
    int y = pipeY;
    int width = pipeWidth;
    int height = pipeHeight;
    Image img;
    boolean passed = false;

    Pipe(Image img){
        this.img = img;
    }
}

//game logic
Bird bird;
int velocityX =-4; //move pipes to the left speed (simulates bird moving right)
int velocityY = 0; //move bird up and down speed
int gravity = 1;

ArrayList<Pipe> pipes;
Random random = new Random();

Timer gameLoop;
Timer placePipeTimer;
boolean gameOver = false;
double score = 0;

    FlappyBird(){
        setPreferredSize(new Dimension(boardwidth, boardheight));
        setFocusable(true);
        addKeyListener(this);
       
        //Load images
        backgroundImg = new ImageIcon(getClass().getResource("./flappybirdbg.png")).getImage();
        birdImg = new ImageIcon(getClass().getResource("./flappybird.png")).getImage();
        topPipeImg = new ImageIcon(getClass().getResource("./toppipe.png")).getImage();
        bottomPipeImg = new ImageIcon(getClass().getResource("./bottompipe.png")).getImage();

        //Create bird
        bird = new Bird(birdImg);

        //Create pipes  
        pipes = new ArrayList<Pipe>();

        //Place pipes timer
        placePipeTimer = new Timer(1500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                placePipes();
            }
        });
        placePipeTimer.start();

        //Create game timer
        gameLoop = new Timer(1000/60, this);
        gameLoop.start();
    }

    public void placePipes(){

        //(0-1) * (pipeHeight/2) = 0 - 256
        //128
        //0-128 -(0-256) ---> 1/4 pipeHeight ->3/4 pipeHeight

        int randomPipeY =(int)(pipeY - pipeHeight/4 - Math.random()*(pipeHeight/2));
        int openingSpace = boardheight/4;

        Pipe topPipe = new Pipe(topPipeImg);
        topPipe.y = randomPipeY;
        pipes.add(topPipe);

        Pipe bottomPipe = new Pipe(bottomPipeImg);
        bottomPipe.y = topPipe.y + pipeHeight + openingSpace;
        pipes.add(bottomPipe);
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g){
        System.out.println("draw");
        //Draw background
        g.drawImage(backgroundImg, 0, 0, boardwidth, boardheight, null);

        //Draw bird
        g.drawImage(bird.img, bird.x, bird.y, bird.width, bird.height, null);

        //Draw pipes
        for(int i= 0; i < pipes.size(); i++){
            Pipe pipe = pipes.get(i);
            g.drawImage(pipe.img, pipe.x, pipe.y, pipe.width, pipe.height, null);
        }

         //score
    g.setColor(Color.BLACK);
    g.setFont(new Font("Arial", Font.BOLD, 32));
    if (gameOver){
        g.drawString("Game Over : " + String.valueOf((int) score),boardwidth/4,boardheight/2);
    }else{
        g.drawString("Score : " + String.valueOf((int) score),10,35);
    }
    }
   
    public void move(){
        //bird movement upwards
        velocityY += gravity;
        bird.y += velocityY;
        bird.y = Math.max(bird.y, 0);
        
        //Move pipes
        for(int i = 0; i < pipes.size(); i++){
            Pipe pipe = pipes.get(i);
            pipe.x += velocityX;  

            if (!pipe.passed && bird.x > pipe.x + pipe.width){
                pipe.passed = true;
                score += 0.5; //0.5 because there are 2 pipes ! so 0.5*2 =1 , 1 for each set of pipes
            }
            
            if(collision(bird, pipe)){
                gameOver = true;
            }  
        }

        if (bird.y > boardheight){
            gameOver = true;
        }
    }

    public boolean collision(Bird a , Pipe b){
        return a.x < b.x + b.width && //a is top left corner doesn't reach b's top right corner
               a.x + a.width > b.x && //a is top right corner doesn't reach b's top left corner
               a.y < b.y + b.height && //a is bottom left corner doesn't reach b's bottom right corner
               a.y + a.height > b.y; //a is bottom right corner doesn't reach b's bottom left corner
    }

    @Override
    public void actionPerformed(ActionEvent e) {
      move();
      repaint();
      if (gameOver){
         placePipeTimer.stop();
         gameLoop.stop();
      }
    }

    

    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_SPACE){
            velocityY = -9;
            if (gameOver){
                //restart the game by resetting the conditions
                bird.y =birdY;
                velocityY = 0;
                pipes.clear();
                placePipeTimer.start();
                gameLoop.start();
                score = 0;
                gameOver = false;
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
}

