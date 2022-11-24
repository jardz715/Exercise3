package org.exist;

import java.util.Scanner;

public class MainClass {

    public MainClass() {
        Scanner input = new Scanner(System.in);
        boolean loop = true;
        int choice;
        while(loop){
            System.out.println("Main Menu");
            System.out.println("[1] Person");
            System.out.println("[2] Contact");
            System.out.println("[3] Role");
            System.out.println("[4] Exit");
            System.out.println("Enter input: ");
            choice = input.nextInt();

            switch(choice){
                case 1:
                    System.out.println(" -- Person -- ");
                    new PersonMenu();
                    break;
                case 2:
                    System.out.println(" -- Contact -- ");
                    new ContactMenu();
                    break;
                case 3:
                    System.out.println(" -- Role -- ");
                    new RoleMenu();
                    break;
                case 4:
                    System.out.println("Thank you for using the program!");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid Input!");
            }
        }
    }

    public static void main(String[] args) {
        MainClass mc = new MainClass();
    }
    
}
