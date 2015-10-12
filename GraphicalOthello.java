import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.lang.*;
/**
 * Use first main method to test GraphicalOthello
 */
public class GraphicalOthello extends Othello implements ActionListener
{
    private JFrame gameFrame;
    private JPanel
    private JButton[][] buttons;
    private JPanel gridPanel;
    private JPanel gridPanel2;
    /**
     * Constructor for objects of class GraphicalOthello
     */
    public GraphicalOthello()
    {
        super();
        gameFrame = new JFrame("Othello!");
        gameFrame.setLayout(new BorderLayout());
        buttons = new JButton[SIZE][SIZE];
        gridPanel = new JPanel();
        gridPanel.setLayout(new GridLayout(8,8));
        gridPanel2 = new JPanel();
        gridPanel2.setLayout(new GridLayout(1,3));
        JButton rand = new JButton("Random");
        rand.addActionListener(this);
        rand.setActionCommand("rand");
        JButton first = new JButton("First Available");
        first.addActionListener(this);
        first.setActionCommand("first");
        JButton greedy = new JButton("Greedy");
        greedy.addActionListener(this);
        greedy.setActionCommand("greedy");
        gridPanel2.add(rand);
        gridPanel2.add(first);
        gridPanel2.add(greedy);
        for (int i = 0; i < buttons.length; i++) {
            for (int j = 0; j < buttons.length; j++) {
                 buttons[i][j] = new JButton("_");
                 buttons[i][j].addActionListener(this);
                 gridPanel.add(buttons[i][j]);
                 buttons[i][j].setActionCommand("game");
            }
        }
        gameFrame.add(gridPanel);
        gameFrame.add(gridPanel2, BorderLayout.SOUTH);
        gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gameFrame.setVisible(true);
        gameFrame.setSize(500, 500);
        
        Object[] options = {"Yes","No"};
        int n = JOptionPane.showOptionDialog(gameFrame,"Would you like to begin the game?","Othello",
        JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE,null,options,options[1]);
        if(n == JOptionPane.YES_OPTION){}
        else if(n == JOptionPane.NO_OPTION){
            status = machinePlay();
            toggleTurn();
        }
        this.print();
    }
    
    public void print() {
        String s = "";
        for (int i = 0; i < buttons.length; i++) {
            for (int j = 0; j < buttons.length; j++) {
                 s += grid[i][j];
                buttons[i][j].setText(s);
                 s = "";
            }
        }
    }
    
    public static void main(String[] args) {
        new GraphicalOthello();
    }   
    
    public void actionPerformed(ActionEvent e)
    {
        String action = e.getActionCommand();
      
        if(action.equals("game")){
              int row = 9;
              int col = 9;
              JButton NB;
              for (int i = 0; i < buttons.length; i++) {
                  for (int j = 0; j < buttons.length; j++) {
                     if(e.getSource() == buttons[i][j]){
                           row = i;
                           col = j;
                           NB = buttons[i][j];
                       }
                  }
              }
              Move move;
              boolean hasPassed = false;
              print();
               while(status != GAME_OVER){
                   if (!(generateMoves().isEmpty())) {    
                           //System.out.println("Enter your move: <row, col> or quit <q>");
                           move = new Move(row,col);
                           if (canPlay(move) == false){
                               print();
                               break;
                            }
                           if (move == null) {
                               System.out.println("Bye bye!");
                               return;
                           }
                       status = play(move);
                       hasPassed = false;
                   }
                   
                   else {
                       if (hasPassed) status = GAME_OVER;
                       else {
                           hasPassed = true;
                           //System.out.println("No available move for you!");
                       }
                   }
                   
                   if (status == ONGOING) {
                       print();
                       //System.out.println("Computer's turn: ");
                       toggleTurn();
                       if (!(generateMoves().isEmpty())) {
                           status = machinePlay();
                           hasPassed = false;
                        }
                       else {
                           if (hasPassed) status = GAME_OVER;
                           else {
                               hasPassed = true;
                               //System.out.println("No available move for me!");
                           }
                        }
                   }           
                   toggleTurn();
                   print();
                   gridPanel.repaint();
              }
              if (!(status == ONGOING)) {
                  determineWinner();
                  if(status == X_WON){
                      JOptionPane.showMessageDialog(gameFrame,"X won!");
                  }
                  if(status == O_WON){
                      JOptionPane.showMessageDialog(gameFrame,"O won!");
                  }
                  if(status == TIE){
                      JOptionPane.showMessageDialog(gameFrame,"It's a tie!");
                  }
              }
        }
        if(action.equals("rand")){
            setMoveStrategy(new RandomMove());
        }
        if(action.equals("first")){
            setMoveStrategy(new FirstAvailableMove());
        }
        if(action.equals("greedy")){
            setMoveStrategy(new GreedyMove());
        }
    }
    
}

