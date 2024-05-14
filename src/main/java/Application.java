import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

public class Application {
    private static Path home = Paths.get("/home/sully/Documents/DiginamicFormation/workspace-spring-tool-suite-4-4.22.0.RELEASE/jeu-de-la-vie-Conway/src/main/java/resources/");
    
    private static List<Path> configurations = new ArrayList<>(Arrays.asList(
        home.resolve("init.csv"),
        home.resolve("ship.csv"),
        home.resolve("oscillator.csv")
    ));

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

    public static void generationMenu(Game game, Scanner scanner){
        int choice = -1;

        do {
            game.printCurrentGeneration();
            System.out.println("  1. Génération suivante");
            System.out.println("  2. Génération automatique (ctrl + D pour quitter)");
            System.out.println("  3. Quitter");
            choice = scanner.nextInt();

            if(choice == 1){
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
                StringBuilder separator = new StringBuilder();
                int separatorLength = game.getCurrentGeneration().get(0).size() * 2 + 1;
                IntStream.range(0, separatorLength).forEach(i -> separator.append("_"));

                System.out.println(separator);
                
                game.printCurrentGeneration();
                game.calculateNextGeneration();
            }
        };
        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);
        executorService.scheduleAtFixedRate(generationrRunnable, 0, 500, TimeUnit.MILLISECONDS);
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Game game = new Game(beginMenu(scanner));

        generationMenu(game, scanner);
        scanner.close();
    }
}
