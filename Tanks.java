import java.util.Scanner;

public class Tanks {

    static Scanner scanner = new Scanner(System.in);

    static char[][][] levels = {
            {
                    {'#','#','#','#','#','#','#','#','#','#','#','#','#','#'},
                    {'#','T',' ',' ',' ','#',' ',' ',' ',' ',' ','E',' ','#'},
                    {'#',' ','#','#',' ','#',' ','#','#','#',' ',' ',' ','#'},
                    {'#',' ',' ',' ',' ',' ',' ',' ',' ','#',' ','#',' ','#'},
                    {'#',' ','#','#','#','#',' ','#',' ','#',' ','#',' ','#'},
                    {'#',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ','#'},
                    {'#','#','#','#','#','#','#','#','#','#','#','#','#','#'}
            },
            {
                    {'#','#','#','#','#','#','#','#','#','#','#','#','#','#'},
                    {'#','T',' ',' ',' ',' ',' ','#',' ',' ',' ','E',' ','#'},
                    {'#',' ','#','#','#','#',' ','#',' ','#','#','#',' ','#'},
                    {'#',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ','#'},
                    {'#',' ','#','#','#','#','#','#','#','#','#','#',' ','#'},
                    {'#',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ','E',' ','#'},
                    {'#','#','#','#','#','#','#','#','#','#','#','#','#','#'}
            },
            {
                    {'#','#','#','#','#','#','#','#','#','#','#','#','#','#'},
                    {'#','T',' ',' ',' ',' ',' ',' ',' ',' ',' ','E',' ','#'},
                    {'#',' ','#','#','#','#',' ','#','#','#','#','#',' ','#'},
                    {'#',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ','#'},
                    {'#',' ','#','#','#','#','#','#','#','#','#','#',' ','#'},
                    {'#',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ','E',' ','#'},
                    {'#','#','#','#','#','#','#','#','#','#','#','#','#','#'}
            }
    };

    static char[][] field;
    static int tankX, tankY;
    static int lives = 3;
    static int level = 0;

    static int bulletX, bulletY;
    static boolean bulletActive;

    public static void main(String[] args) {
        while (true) {
            System.out.println("\n=== ТАНЧИКИ ===");
            System.out.println("1. Начать игру");
            System.out.println("2. Выход");
            System.out.print("Выбор: ");

            String c = scanner.nextLine();
            if (c.equals("1")) startGame();
            if (c.equals("2")) break;
        }
    }

    static void startGame() {
        lives = 3;
        level = 0;

        while (level < levels.length) {
            loadLevel(level);

            while (true) {
                drawField();
                System.out.println("Жизни: " + lives + " | Уровень: " + (level + 1));
                System.out.print("Команда: ");

                String cmd = scanner.nextLine().toLowerCase();
                if (cmd.equals("q")) return;

                moveTank(cmd);
                moveBullet();
                enemyAttack();

                if (lives <= 0) {
                    System.out.println("\n💀 ИГРА ОКОНЧЕНА");
                    scanner.nextLine();
                    return;
                }

                if (noEnemies()) {
                    level++;
                    break;
                }
            }
        }

        System.out.println("\n🏆 ТЫ ПРОШЁЛ ВСЕ УРОВНИ!");
        scanner.nextLine();
    }

    static void loadLevel(int n) {
        field = new char[levels[n].length][levels[n][0].length];

        for (int y = 0; y < field.length; y++) {
            for (int x = 0; x < field[y].length; x++) {
                field[y][x] = levels[n][y][x];
                if (field[y][x] == 'T') {
                    tankX = x;
                    tankY = y;
                    field[y][x] = ' ';
                }
            }
        }
        bulletActive = false;
    }

    static void drawField() {
        System.out.println("\n");
        for (int y = 0; y < field.length; y++) {
            for (int x = 0; x < field[y].length; x++) {
                if (x == tankX && y == tankY)
                    System.out.print('T');
                else if (bulletActive && x == bulletX && y == bulletY)
                    System.out.print('*');
                else
                    System.out.print(field[y][x]);
            }
            System.out.println();
        }
    }

    static void moveTank(String cmd) {
        int nx = tankX;
        int ny = tankY;

        if (cmd.equals("w")) ny--;
        if (cmd.equals("s")) ny++;
        if (cmd.equals("a")) nx--;
        if (cmd.equals("d")) nx++;

        if (cmd.equals("f") && !bulletActive) {
            bulletX = tankX + 1;
            bulletY = tankY;
            bulletActive = true;
            return;
        }

        if (field[ny][nx] == 'E') {
            lives--;
            System.out.println("⚠ Враг атаковал! -1 жизнь");
            return;
        }

        if (field[ny][nx] != '#') {
            tankX = nx;
            tankY = ny;
        }
    }

    static void moveBullet() {
        if (!bulletActive) return;

        bulletX++;

        if (field[bulletY][bulletX] == '#') bulletActive = false;

        if (field[bulletY][bulletX] == 'E') {
            field[bulletY][bulletX] = ' ';
            bulletActive = false;
        }
    }

    static void enemyAttack() {
        for (int y = 0; y < field.length; y++) {
            for (int x = 0; x < field[y].length; x++) {
                if (field[y][x] == 'E') {
                    if (Math.abs(x - tankX) + Math.abs(y - tankY) == 1) {
                        lives--;
                        System.out.println("⚠ Враг рядом! -1 жизнь");
                        return;
                    }
                }
            }
        }
    }

    static boolean noEnemies() {
        for (int y = 0; y < field.length; y++)
            for (int x = 0; x < field[y].length; x++)
                if (field[y][x] == 'E') return false;
        return true;
    }
}
