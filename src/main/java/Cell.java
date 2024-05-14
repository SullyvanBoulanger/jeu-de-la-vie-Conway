import java.util.List;

public class Cell {
    private int x;
    private int y;
    private boolean isAlive;

    public Cell(int x, int y, boolean isAlive) {
        this.x = x;
        this.y = y;
        this.isAlive = isAlive;
    }

    public boolean isAlive() {
        return isAlive;
    }

    public Cell calculateNextGeneration(List<List<Cell>> cells){
        boolean isAliveNewGeneration = false;

        if(isAlive){
            isAliveNewGeneration = survive(cells);
        } else {
            isAliveNewGeneration = birth(cells);
        }
        return new Cell(x, y, isAliveNewGeneration);
    }

    private boolean survive(List<List<Cell>> cells){
        int aliveCellsAround = countAliveCellsAround(cells);
        
        if(aliveCellsAround == 2 || aliveCellsAround == 3)
            return true;

        return false;
    }

    private boolean birth(List<List<Cell>> cells){
        int aliveCellsAround = countAliveCellsAround(cells);
        
        if(aliveCellsAround == 3)
            return true;

        return false;
    }

    private int countAliveCellsAround(List<List<Cell>> cells){
        int aliveCellsAround = 0;

        for(int lineId = -1; lineId<2; lineId++){
            int searchedX = x + lineId; 
            
            if(searchedX < 0 || searchedX >= cells.size())
                continue;

            List<Cell> line = cells.get(searchedX);

            for (int columnId = -1; columnId < 2; columnId++) {
                int searchedY = y + columnId;
                
                if(x == searchedX && y == searchedY)
                    continue;
                
                if(searchedY < 0 || searchedY >= line.size())
                    continue;
                
                Cell cellAround = line.get(searchedY);
                
                if(cellAround.isAlive){
                    aliveCellsAround++;
                }
            }
        }
        
        return aliveCellsAround;
    }
}
