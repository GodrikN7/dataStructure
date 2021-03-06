/* 
 * MagicSquare.java
 * 
 * Author:          Computer Science S-111 staff
 * Modified by:     Nicolas Rannou, huko.rannou@gmail.com
 * Date modified:   Fall 2011
 */

import java.util.*;

public class MagicSquare {
    // the current contents of the cells of the puzzle values[r][c]
    // gives the value in the cell at row r, column c
    private int[][] values;

    // the order (i.e., the dimension) of the puzzle
    private int order;
    
    // numbers to be assigned
    // list might be more efficient to remove index?
    private int[] tobeassigned;
    
    // the magic sum!
    private int magicSum;

    /**
     * Creates a MagicSquare object for a puzzle with the specified
     * dimension/order.
     */
    public MagicSquare(int order) {
        values = new int[order][order];
        this.order = order;
        // we don't want to compute it each time we need it!
        magicSum = order*(order*order + 1)/2;
        System.out.print("magic sum: " + magicSum + "\n");

        // Numbers in array shouldn't be ordered for a better efficiency...
        System.out.println("We reorder numbers to be assigned,  in a \"smart\" way: ");
        tobeassigned = new int[order*order];
        int decrement = 0;
        for(int i=1; i<order*order+1; i++){
          // decrement
         if(i%2 == 0){
          tobeassigned[order*order - decrement -1] = i;
          System.out.println("position: " + (order*order - decrement -1) + " -> " + tobeassigned[order*order - decrement -1] + " ");
          decrement ++;
          }
          else{
          // increment
          tobeassigned[(i-1)/2] = i;
          System.out.println("position: " + ((i-1)/2) + " -> " + tobeassigned[(i-1)/2] + " ");
          }
        }
    }

    /**
     * This method should call the separate recursive-backtracking method
     * that you will write, passing it the appropriate initial parameter(s).
     * It should return true if a solution is found, and false otherwise.
     */
    public boolean solve() {
        // calculate execution time
        long startTime = System.currentTimeMillis();
        Boolean solved =  findSolutions(0, 0, 0, tobeassigned);
        // calculate execution time
        long endTime   = System.currentTimeMillis();
        long totalTime = endTime - startTime;
        System.out.println("execution time: " + totalTime + " ms");
        
        return solved;
    }
    
    /**
     * Backtracking method
     * param: numbers which still have to be assigned
     * global_iteration:
     * 0-> we are on the ousite layer of the square:
     * x: where we are working
     * A: assigned
     * order4:
     * x x x x
     * 0 0 0 x
     * 0 0 0 x
     * 0 0 0 x
     * 1-> we go one step inside
     * A A A A
     * x x x A
     * 0 0 x A
     * 0 0 x A
     * etc....
     * we use "global_iteration" to efficiently parse the square:
     * first row -> last column -> remaining 2nd row -> remaining last-1 column, etc.
     */
    
    private boolean findSolutions(int row, int column, int global_iteration, int[] param){
    
        // base case, if reached we are done!
        if(global_iteration == order){
          return true;
        }
        
        // go through list to be assigned
        for(int i=0; i<param.length; i++){
            if(isSafe(row, column, global_iteration, param[i])){
              placeNumber(row +global_iteration, column, param[i]);
              // new parameter set, we remove the one which has been assigned
              // not efficient at all, better use a list?
              int[] new_param = new int[param.length -1];
              int pos = 0;
              for(int j=0; j<param.length; j++){
                if(param[j] != param[i]){
                  new_param[pos] = param[j];
                  ++pos;
                  }
              }
              Boolean solved;
              if(column < order - global_iteration - 1){
                solved = findSolutions(row, column+1, global_iteration, new_param);
                }
              else if(row < order - global_iteration - 1){
                solved = findSolutions(row+1, column, global_iteration, new_param);
              }
              else{
                solved = findSolutions(0, 0, global_iteration+1, new_param);
              }
            
              if(solved){
                return true;
              }
              else{
                removeNumber(row + global_iteration, column);
              }
            }
          }
        return false;
    }
    
    /**
     * When row or column is full, check if sum in columns and rows != magicSum
     */
    private boolean isSafe(int row, int col, int global_it, int value){
      if(col == order - global_it -1 && row == 0){
        int sum_row = 0;
        for(int i=0; i<order; i++){
          sum_row += values[row+global_it][i];
        }
        sum_row+=value;
      
        if(sum_row != magicSum)
          return false;
      }
      
      if(row == order - 1){
        int sum_col = 0;
        for(int i=0; i<order; i++){
          sum_col += values[i][col];
        }
        sum_col += value;
      
        if(sum_col != magicSum)
          return false;
      }
      
      return true;
    }
    
    /**
     * Add number to magic square
     */
    private void placeNumber(int row, int column, int value){
      // add value in array
      values[row][column] = value;
      }

    /**
     * Remove number from magic square
     */
    private void removeNumber(int row, int column){
        // set value to 0in array
        values[row][column] = 0;
        }
    
    /**
     * Displays the current state of the puzzle.
     * You should not change this method.
     */
    public void display() {
        for (int r = 0; r < order; r++) {
            printRowSeparator();
            for (int c = 0; c < order; c++) {
                System.out.print("|");
                if (values[r][c] == 0)
                    System.out.print("   ");
                else {
                    if (values[r][c] < 10) {
                        System.out.print(" ");
                    }
                    System.out.print(" " + values[r][c] + " ");
                }
            }
            System.out.println("|");
        }
        printRowSeparator();
    }

    // A private helper method used by display()
    // to print a line separating two rows of the puzzle.
    private void printRowSeparator() {
        for (int i = 0; i < order; i++)
            System.out.print("-----");
        System.out.println("-");
    }
    
    public static void main(String[] args) {
        /*******************************************************
          **** You should NOT change any code in this method ****
          ******************************************************/

        Scanner console = new Scanner(System.in);
        System.out.print("What order Magic Square would you like to solve? ");
        int order = console.nextInt();
        
        MagicSquare puzzle = new MagicSquare(order);
        if (puzzle.solve()) {
            System.out.println("Here's the solution:");
            puzzle.display();
        } else {
            System.out.println("No solution found.");
        }
    }
}