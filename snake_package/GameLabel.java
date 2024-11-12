package snake_package;
import javax.swing.*;

public class GameLabel extends JLabel{
    public int timeLeft;

    public void setTimeLeft(int timeLeft){
        this.timeLeft = timeLeft;
    }

    public void age(){
        setTimeLeft(timeLeft-1);
    }

}
