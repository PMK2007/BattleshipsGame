
/**
* Battleships.java
* A Battleship game in a console program
* @author MK Pham
* Computing Science 10
* Copyright 2023, Centennial High School. All rights reserved.
*/

import java.util.Scanner;
import java.util.InputMismatchException;

/*
 * This project is a battleship game. It allows you to fight against a computer
 * which can attack you.
 */
public class Battleships {

	// Variables for grid
	public static int numRows = 10;// Rows of grid
	public static int numCols = 10;// Columns of grid
	public static String[][] grid = new String[numRows][numCols];
	// ships variables
	public static int playerShips;
	public static int computerShips;

	public static int missedGuesses = 0;// stores the number of times both computer and player misses
	public static double difficulty = 0;
	public static boolean play = true;

	// main method
	public static void main(String[] args) {

		Scanner input = new Scanner(System.in);

		while (Battleships.play) {

			// calls the methods to run the program
			welcome(input);

			generateMap();

			deployShips(input);

			do {
				battle(input);
			} while (Battleships.playerShips != 0 && Battleships.computerShips != 0);

			gameOver(input);
		}

		end();
		input.close();

	}// end main

	// welcome screen which has a help menu and user can pick difficulty
	public static void welcome(Scanner input) {

		System.out.println("Welcome to Battleship");
		System.out.println("You will be fighting against a computer in a battle to conquer the sea");
		System.out.println("    __|__ |___| |\\\r\n" + "    |o__| |___| | \\\r\n" + "    |___| |___| |o \\\r\n"
				+ "   _|___| |___| |__o\\\r\n" + "  /...\\_____|___|____\\_/\r\n" + "  \\   o * o * * o o  /\r\n"
				+ "~~~~~~~~~~~~~~~~~~~~~~~~~~");// boat ascii art
		System.out.println("Press Enter to Continue...");
		input.nextLine();
		System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n");

		boolean check = true;
		while (check) {

			System.out.println("Select a Difficulty or Enter \"help\" for Assistance:");
			String difficulty = input.nextLine();

			switch (difficulty) {

			case ("easy"):
				Battleships.difficulty = .25;
				break;
			case ("medium"):
				Battleships.difficulty = .15;
				break;
			case ("hard"):
				Battleships.difficulty = .07;
				break;
			case ("help"):
				System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
				System.out.println("Help Menu:\n");
				System.out.println("Difficulties:");
				System.out.println("easy, medium, hard");
				System.out.println("NOTE: Enter difficulty in all lowercase\n");

				System.out.println("Tiles:");
				System.out.println("Empty Tile =  Nothing");
				System.out.println("\"-\" = Missed Shots");
				System.out.println("\"@\" = Your Ships");
				System.out.println("\"x\" = Your Destroyed Ships");
				System.out.println("\"X\" = Computer's Destroyed Ships");

				System.out.println("Press Enter to Continue...");
				input.nextLine();

				System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
				continue;
			default:
				System.out.println("Invalid Response");
				continue;
			}// switch

			System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
			check = false;

		} // while loop
	}

	// generates or regenerates/clear the grid for the game
	public static void generateMap() {

		// top row
		System.out.print("  ");
		for (int r = 0; r < numCols; r++) {
			System.out.print(r);
		}
		System.out.println();

		// middle rows
		for (int r = 0; r < grid.length; r++) {
			for (int c = 0; c < grid[r].length; c++) {
				grid[r][c] = " ";
				if (c == 0)
					System.out.print(r + "|" + grid[r][c]);
				else if (c == grid[r].length - 1)
					System.out.print(grid[r][c] + "|" + r);
				else
					System.out.print(grid[r][c]);
			} // end for
			System.out.println();
		} // end for

		// bottom row
		System.out.print("  ");
		for (int r = 0; r < numCols; r++) {
			System.out.print(r);
		}
		System.out.println();
	}// end method

	// lets the player deploy ships
	public static void deployShips(Scanner input) {
		// variables and scanner
		int ships = 0;
		Battleships.playerShips = 5;
		Battleships.computerShips = 5;
		// a while loop that lets the player deploy 5 ships
		while (ships < Battleships.playerShips) {
			try {
				System.out.println("Enter the X coordinate of your ship");
				int x = input.nextInt();
				System.out.println("Enter the Y coordinate of your ship");
				int y = input.nextInt();

				if ((x >= 0 && x <= numRows) && (y >= 0 && y <= numCols) && (grid[x][y]) != ("@")) {
					grid[x][y] = ("@");
					ships++;
				} else if ((x >= 0 && x <= numRows) && (y >= 0 && y <= numCols) && grid[x][y] == ("@")) {
					System.out.println("You can't have 2 ships on the same location");
				} else if ((x < 0 || x >= numRows) || (y < 0 || y >= numCols)) {
					System.out.println("The coordinates you entered is out of the boundary");
				}

				printMap();// prints map after deploying a ship

			} catch (InputMismatchException e) {
				System.out.println("Please enter a valid choice");
				input.next();
			} // end catch
		} // end while

		return;

	}// end method

	// calls the methods for battling
	public static void battle(Scanner input) {

		playerTurn(input);

		computerTurn();

		printMap();

		System.out.println("You have " + Battleships.playerShips + " ships left");
		System.out.println("Computer have " + Battleships.computerShips + " ships left");

		return;

	}

	// allows the player to shoot, there is a chance to hit the enemy ship
	public static void playerTurn(Scanner input) {
		boolean turn = true;
		java.util.Random random = new java.util.Random();

		while (turn) {
			try {

				// enter coordinates
				System.out.println("Enter X coordinate of attack");
				int x = input.nextInt();
				System.out.println("Enter Y coordinate of attack");
				int y = input.nextInt();

				double hitChance = random.nextDouble();// chance to hit the enemy

				// if else checks tile and applies the appropriate action
				if ((x >= 0 && x <= numRows) && (y >= 0 && y <= numCols)) {// if the guess is valid
					if (grid[x][y] == "@") {
						System.out.println("Oops! You hit your own ship");
						grid[x][y] = ("x");
						Battleships.playerShips--;
					} else if (grid[x][y] == ("X")) {
						System.out.println("An enemy ship was already shot here, try again");
						printMap();
						continue;
					} else if (grid[x][y] == ("x")) {
						System.out.println("Your ship was already shot here, try again");
						printMap();
						continue;
					} else if (grid[x][y] == ("-")) {
						System.out.println("You already missed here, try again");
						printMap();
						continue;
					} else if (Battleships.computerShips != 0) {

						if (Battleships.missedGuesses == 94 && Battleships.computerShips == 1
								|| Battleships.missedGuesses == 93 && Battleships.computerShips == 2
								|| Battleships.missedGuesses == 92 && Battleships.computerShips == 3
								|| Battleships.missedGuesses == 91 && Battleships.computerShips == 4
								|| Battleships.missedGuesses == 90 && Battleships.computerShips == 5) {
							System.out.println("You sunk an enemy ship!");
							grid[x][y] = ("X");
							Battleships.computerShips--;
						} else if (hitChance < Battleships.difficulty) {
							System.out.println("You sunk an enemy ship!");
							grid[x][y] = ("X");
							Battleships.computerShips--;
						} else if (grid[x][y] != "-") {
							System.out.println("You missed!");
							grid[x][y] = ("-");
							Battleships.missedGuesses++;
						}
					} // end if

				} else {
					System.out.println("Please enter a valid guess");
					continue;
				}

				turn = false;

			} catch (InputMismatchException e) {
				System.out.println("Plese enter a valid coordinate");
				input.next();
				printMap();
				continue;
			} // end catch

		} // end while

		return;
	}// end method

	// generates a random coordinate on the map and fires at it
	public static void computerTurn() {

		boolean turn = true;
		java.util.Random random = new java.util.Random();

		double chance = random.nextDouble();
		while (turn) {

			double x = Math.random() * 10;
			int xRound = (int) x;

			double y = Math.random() * 10;
			int yRound = (int) y;

			if ((x >= 0 && x <= numRows) && (y >= 0 && y <= numCols)) {
				if (grid[xRound][yRound] == "@") {
					System.out.println("One of your ship was hit!");
					Battleships.playerShips--;
					turn = false;
					grid[xRound][yRound] = ("x");
				} else if (grid[xRound][yRound] == ("X")) {
					continue;// try again if location has an "X"
				} else if (grid[xRound][yRound] == ("-")) {
					continue;// try again if location has a "-"
				} else if (grid[xRound][yRound] == ("x")) {
					continue;// try again if location has a "x"
				} else {
					// chance to hit their own ship
					if (chance < 0.005) {
						System.out.println("The Computer hit their own ship!");
						grid[xRound][yRound] = ("X");
						Battleships.computerShips--;
						turn = false;
					} else {
						grid[xRound][yRound] = ("-");
						Battleships.missedGuesses++;
						turn = false;
					}
				}
			} else
				continue;
		} // end while
	}// end method

	// prints out the map
	public static void printMap() {
		System.out.println();
		// First section
		System.out.print("  ");
		for (int i = 0; i < numCols; i++)
			System.out.print(i);
		System.out.println();

		// Middle section
		for (int x = 0; x < grid.length; x++) {
			System.out.print(x + "|");

			for (int y = 0; y < grid[x].length; y++) {
				System.out.print(grid[x][y]);
			}

			System.out.println("|" + x);
		}

		// Last section
		System.out.print("  ");
		for (int i = 0; i < numCols; i++)
			System.out.print(i);
		System.out.println();
	}// end method

	// game ends, ask to play again
	public static void gameOver(Scanner input) {

		if (Battleships.computerShips == 0 && Battleships.playerShips != 0) {
			System.out.println("Congratulations, you won against the computer");
			System.out.println("__      ___      _                   \r\n"
					+ " \\ \\    / (_)    | |                  \r\n" + "  \\ \\  / / _  ___| |_ ___  _ __ _   _ \r\n"
					+ "   \\ \\/ / | |/ __| __/ _ \\| '__| | | |\r\n" + "    \\  /  | | (__| || (_) | |  | |_| |\r\n"
					+ "     \\/   |_|\\___|\\__\\___/|_|   \\__, |\r\n" + "                                 __/ |\r\n"
					+ "                                |___/ ");//victory ascii art
		} else if (Battleships.computerShips != 0 && Battleships.playerShips == 0) {
			System.out.println("You lost against the computer.");
			System.out.println("  _____        __           _   \r\n" + " |  __ \\      / _|         | |  \r\n"
					+ " | |  | | ___| |_ ___  __ _| |_ \r\n" + " | |  | |/ _ \\  _/ _ \\/ _` | __|\r\n"
					+ " | |__| |  __/ ||  __/ (_| | |_ \r\n" + " |_____/ \\___|_| \\___|\\__,_|\\__|\r\n"
					+ "                                \r\n" + "                                ");//defeat ascii art
		}

		boolean i = true;
		int skip = 0;

		while (i) {
			try {
				System.out.println("\nWould you like to play again?");
				if (skip == 0)
					input.nextLine();// skips \n character on first run of method
				String playAgain = input.nextLine();

				switch (playAgain) {

				case ("yes"):
					return;
				case ("no"):
					Battleships.play = false;
					System.out.println("\n\n\n\n\n\n\n\n\n");
					return;
				default:
					System.out.println("Invalid Choice");
					skip++;
					break;
				}// switch end

			} catch (Exception e) {
				System.out.println("Invalid Choice");
				skip++;
			}
		}
	}// end method

	// end screen
	public static void end() {

		System.out.println("\n\n\n\n\n\n\n\n\n\nThanks for Playing Battleship!");
		System.out.println("      I\\\r\n" + "      I \\\r\n" + "      I  \\\r\n" + "      I*--\\\r\n"
				+ "      I    \\\r\n" + "      I     \\\r\n" + "      I______\\\r\n" + " _____I__O______\r\n"
				+ "  \\     ( )     b  ^  ^\r\n" + "^^^^^^^^^^^^^^^^^    ^");// boat ascii art

	}

}// end class