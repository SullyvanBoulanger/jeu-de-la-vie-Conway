import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class Application {
    private static Path home = Paths.get("/home/sully/Documents/DiginamicFormation/workspace-spring-tool-suite-4-4.22.0.RELEASE/jeu-de-la-vie-Conway/src/main/java/resources/");
    
    private static List<Path> configurations = getConfigurations();
    
    private static List<Path> getConfigurations(){
        try {
            return Files.list(home)
            .filter(Files::isRegularFile)
            .sorted(Comparator.comparing(Path::getFileName))
            .collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public static Path beginMenu(Scanner scanner){
        System.out.println("Bienvenue sur le jeu de la vie de Conway");
        System.out.println("  1. Choisir une configuration");
        System.out.println("  2. Commencer");

        int choice = scanner.nextInt();
        int configurationId = 0;
        while (choice != 1 && choice != 2) {
            System.out.println("Invalide, veuillez sélectionner un choix du menu :");
            choice = scanner.nextInt();
        }
        switch (choice) {
            case 1:
                configurationId = choiceConfiguration(scanner);
                break;
            case 2:
                return configurations.get(configurationId);
            }
        return configurations.get(configurationId);
    }

    public static int choiceConfiguration(Scanner scanner){
        clearScreen();
        configurations.forEach(configuration -> {
            try {
                List<String> allLines = Files.readAllLines(configuration);
                System.out.println("  " + configurations.indexOf(configuration) + ". " + configuration.getFileName());
                allLines.forEach(line -> System.out.println(line));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        System.out.println("Votre choix :");
        int choice = scanner.nextInt();
        while (choice < 0 || choice > configurations.size() - 1) {
            System.out.println("Votre choix :");
            choice = scanner.nextInt();
        }
        return choice;
    }

    public static void clearScreen() {  
        System.out.print("\033[H\033[2J");  
        System.out.flush();  
    }  

    public static void generationMenu(Game game, Scanner scanner){
        int choice = -1;

        do {
            clearScreen();
            game.printCurrentGeneration();
            System.out.println("  1. Génération suivante");
            System.out.println("  2. Génération automatique (ctrl + D pour quitter)");
            System.out.println("  3. Quitter");
            choice = scanner.nextInt();

            if(choice == 1){
                clearScreen();
                game.calculateNextGeneration();
            } else if(choice == 2){
                runCalculateGeneration(game);
            }
        } while (choice != 3);
    }

    public static void runCalculateGeneration(Game game){
        Runnable generationrRunnable = new Runnable() {
            @Override
            public void run() {
                clearScreen();
                
                game.printCurrentGeneration();
                game.calculateNextGeneration();
            }
        };
        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);
        executorService.scheduleAtFixedRate(generationrRunnable, 0, 250, TimeUnit.MILLISECONDS);
    }

    public static void main(String[] args) {
        clearScreen();
        Scanner scanner = new Scanner(System.in);
        Game game = new Game(beginMenu(scanner));

        generationMenu(game, scanner);
        scanner.close();
    }
}
