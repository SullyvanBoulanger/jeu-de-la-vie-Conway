import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Game {
    private List<List<Cell>> currentGeneration;
    private int xHintForCreation = 0;
    private int yHintForCreation = 0;

    public Game(List<List<Cell>> cells) {
        this.currentGeneration = cells;
    }

    public Game(Path path) {
        this.currentGeneration = createGameFromCSV(path);
    }

    private List<List<Cell>> createGameFromCSV(Path path){
        try {
            List<String> lines = Files.readAllLines(path);
            List<List<Cell>> cells = new ArrayList<>();

            lines.forEach(line -> {
                List<String> splittedLine = new ArrayList<>(Arrays.asList(line.split("\\|")));
                splittedLine.remove(0);
                splittedLine.remove(splittedLine.size() - 1);

                List<Cell> cellsLine = new ArrayList<>();
                splittedLine.forEach(part -> {
                    cellsLine.add(new Cell(xHintForCreation, yHintForCreation, part.equalsIgnoreCase("X") ? true : false));
                    yHintForCreation++;
                });

                cells.add(cellsLine);
                xHintForCreation++;
                yHintForCreation = 0;
            });
            
            return cells;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public void calculateNextGeneration(){
        List<List<Cell>> newGeneration = currentGeneration.stream().map(line -> {
                List<Cell> newLine = line.stream()
                    .map(cell -> cell.calculateNextGeneration(currentGeneration))
                    .collect(Collectors.toList());
                return newLine;
            }).collect(Collectors.toList());
        
        currentGeneration = newGeneration;
    }

    private void printGeneration(List<List<Cell>> generation){
        generation.forEach(line -> {
            StringBuilder lineBuilder = new StringBuilder("|");

            line.forEach(cell -> {
                if(cell.isAlive()){
                    lineBuilder.append("X|");
                } else {
                    lineBuilder.append(" |");
                }
            });

            System.out.println(lineBuilder);
        });
    }

    public void printCurrentGeneration(){
        printGeneration(currentGeneration);
    }
}
