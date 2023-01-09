
import java.util.Scanner;

public class Menu {
    public static void mainMenu(){
        Scanner sc = new Scanner(System.in);
        Task1 task1 = new Task1();
        for(;;){
            System.out.println("MAIN MENU");
            System.out.println("1. USER LOGIN");
            System.out.println("2. CREATE NEW USER");
            System.out.println("3. Exit");
            System.out.print("Enter Choice: ");
            int ch= sc.nextInt();
            switch (ch){
                case 1:
                    task1.userLogin();
                    break;
                case 2:
                    task1.createNewUser();
                    break;
                case 3:
                    System.out.println("Exiting application");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Enter valid choice:");
                    mainMenu();
            }
        }
    }

    public static void main(String[] args) {

        Menu.mainMenu();
    }
}
