import acm.program.*;
import java.awt.*;
import java.util.Queue;
import java.util.Random;
import java.util.Stack;
import acm.graphics.*;

public class mainClass extends GraphicsProgram {
    //This is the pixel length of a cell .
    private int w = 40;

    //This is the width of the canvas .
    private int width = 400;

    //This is the height of the canvas .
    private int height = 400;

    //This is the number of columns .
    private int cols = width/w;

    //This is the number of rows .
    private int rows = height/w;

    //This is the array of Cell object .
    private Cell[][] arr = new Cell[rows][cols];

    //The current cell .
    private Cell current;

    //The next cell .
    private Cell next;

    //Random number Generator
    private Random rand = new Random();

    //Stack for backtracking .
    private Stack<Cell> stack = new Stack<>();

    //Stack for transferring contents .
    private Stack<Cell> revStack = new Stack<>();





    public void run() {
        setSize(new Dimension(width+20,height+20));
        for(int i=0;i<rows;i++)
            for(int j=0;j<cols;j++)
            {
                Cell temp = new Cell(i,j);
                arr[i][j] = temp;
            }
        current = arr[0][0];
        setupIni();
        loop();
        for(int i=0;i<rows;i++)
            for(int j=0;j<cols;j++)
            {

                remove(arr[i][j].rect1);
                arr[i][j].visited = false;
            }

        solve();

    }



    private void setupIni() {

        for(int i=0;i<rows;i++)
            for(int j=0;j<cols;j++)
                arr[i][j].ini();
    }

    private void loop() {
        try {
            Thread.sleep(5000);
        }
        catch(InterruptedException ex)
        {
            Thread.currentThread().interrupt();
        }
        do {



            current.visited = true;
            current.highlight();
            next = current.checkNeighbours();
            if (next != null) {
                //Step 1
                next.visited = true;
                next.printDifferent();

                //Step 2
                stack.push(current);


                //Step 3
                removeWalls(current, next);

                //Step 4
                current = next;
            } else if (!stack.empty()) current = stack.pop();

            current.printDifferent();

            for (int i = 0; i < rows; i++)
                for (int j = 0; j < cols; j++)
                    arr[i][j].show();
            try {
                Thread.sleep(100);
            }
            catch(InterruptedException ex)
            {
                Thread.currentThread().interrupt();
            }

        }while(next!=null || !stack.empty());

    }

    private void solve() {
        current = arr[0][0];
        do {
            current.visited = true;



            next = current.checkNeighboursForSolve();
            if(next != null) {
                //Step 1
                next.visited = true;


                next.printDifferent();

                //Step 2
                stack.push(current);

                //Step 3
                current = next;
            } else if (!stack.empty()) {

                current = stack.pop();
            }

            if(current == arr[rows-1][cols-1]) break;


        }while(next!=null || !stack.empty());

        while(!stack.empty()) {
            revStack.push(stack.pop());
        }

        while(!revStack.empty()) {
            current = revStack.pop();
            current.turnRed();
            try {
                Thread.sleep(100);
            }
            catch(InterruptedException ex)
            {
                Thread.currentThread().interrupt();
            }
            for (int i = 0; i < rows; i++)
                for (int j = 0; j < cols; j++)
                    arr[i][j].show();
            current.turnGreen();
        }

        arr[rows-1][cols-1].turnRed();

    }

    public class Cell {
        int i;
        int j;
        boolean visited = false;
        GLine top,right,bottom,left;
        GRect rect1,rect2;
        boolean[] walls = new boolean[4];
        Cell(int i,int j) {
            this.i = i;
            this.j = j;
            for(int a=0;a<4;a++) {
                walls[a] = true;
            }
        }

        private void ini() {
            int x = j*w;
            int y = i*w;

            top = new GLine(x,y,x+w,y);
            right = new GLine(x+w,y,x+w,y+w);
            bottom = new GLine(x+w,y+w,x,y+w);
            left = new GLine(x,y+w,x,y);
            add(top);
            top.setVisible(true);
            add(right);
            right.setVisible(true);
            add(bottom);
            bottom.setVisible(true);
            add(left);
            left.setVisible(true);
        }

        private Cell checkNeighbours() {
            Cell topCell = index(this.i-1,this.j);
            Cell rightCell = index(this.i,this.j+1);
            Cell bottomCell = index(this.i+1,this.j);
            Cell leftCell = index(this.i,this.j-1);
            Cell[] temp = new Cell[4];
            int count = 0;
            if(topCell != null && !topCell.visited) {
                temp[count] = topCell;
                count++;
            }
            if(rightCell != null && !rightCell.visited) {
                temp[count] = rightCell;
                count++;

            }
            if(bottomCell != null && !bottomCell.visited) {
                temp[count] = bottomCell;
                count++;
            }
            if(leftCell != null && !leftCell.visited) {
                temp[count] = leftCell;
                count++;
            }
            if(count>0) {
                int r = rand.nextInt(count);

                return temp[r];
            }

            else {return null;}
        }

        private Cell checkNeighboursForSolve() {
            Cell topCell = index(this.i-1,this.j);
            Cell rightCell = index(this.i,this.j+1);
            Cell bottomCell = index(this.i+1,this.j);
            Cell leftCell = index(this.i,this.j-1);
            Cell[] temp = new Cell[4];
            int count = 0;
            if(!this.top.isVisible()  && topCell != null && !topCell.visited) {
                temp[count] = topCell;
                count++;
            }
            if( !this.right.isVisible() && rightCell != null && !rightCell.visited) {
                temp[count] = rightCell;
                count++;

            }
            if(!this.bottom.isVisible() && bottomCell != null && !bottomCell.visited) {
                temp[count] = bottomCell;
                count++;
            }
            if(!this.left.isVisible() && leftCell != null && !leftCell.visited) {
                temp[count] = leftCell;
                count++;
            }
            if(count>0) {
                int r = rand.nextInt(count);

                return temp[r];
            }

            else {return null;}
        }



        private void highlight() {
            int x = this.j*w;
            int y = this.i*w;
            if(this.visited) {
                rect1 = new GRect(x, y, w, w);
                rect1.setFilled(true);
                Color col = new Color(255, 249, 26);
                rect1.setColor(col);
                add(rect1);
            }
        }

        private void printDifferent() {
            int x = this.j*w;
            int y = this.i*w;
            if(this.visited) {
                rect2 = new GRect(x, y, w , w );
                rect2.setFilled(true);

                rect2.setColor(Color.cyan);
                add(rect2);
            }
        }

        private void show() {
            if (this.walls[0]) top.sendToFront();
            else {remove(top); top.setVisible(false);}

            if (this.walls[1]) right.sendToFront();
            else {remove(right);right.setVisible(false);}

            if (this.walls[2]) bottom.sendToFront();
            else {remove(bottom);bottom.setVisible(false);}

            if (this.walls[3]) left.sendToFront();
            else {remove(left);left.setVisible(false);}
        }

        private void turnRed() {
            int x = this.j*w;
            int y = this.i*w;
            rect1 = new GRect(x, y, w, w);
            rect1.setFilled(true);
            Color col = new Color(36, 43, 222);
            rect1.setColor(col);
            add(rect1);
        }

        private void turnGreen() {
            int x = this.j*w;
            int y = this.i*w;
            rect1 = new GRect(x, y, w, w);
            rect1.setFilled(true);

            Color col = new Color(0x57FF4F);
            rect1.setColor(col);
            add(rect1);
        }


    }

    private Cell index(int i,int j) {
        if(i<0 || j<0 || i>rows-1 || j>cols-1) return null;
        else return arr[i][j];
    }

    private void removeWalls(Cell current , Cell next) {
        int x = current.i - next.i;
        if(x == 1) {
            current.walls[0] = false;
            next.walls[2] = false;
        }
        else if(x == -1){
            current.walls[2] = false;
            next.walls[0] = false;
        }

        int y = current.j - next.j;
        if(y == 1) {
            current.walls[3] = false;
            next.walls[1] = false;
        }
        else if(y== -1){
            current.walls[1] = false;
            next.walls[3] = false;
        }
    }
}