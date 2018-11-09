//Eva Yeverovich
//This is the basic structure for a Pokemon fighting game in which the user can choose four pokemon and fight a barrage of several others
//in order to be crowned the "Trainer Supreme" and win the game. It's basic function allow for users to attack, skip their turn, as well as switch out pokemon.

/*...I understand that you expected it to be late, and it might have been smarter to take the extension, but I just decided to hand it in...
The only things that don't function properly are disable and wild storm.
*/
import java.util.*;
import java.io.*;
public class PokemonArena{
	private static ArrayList<Pokemon>chosen= new ArrayList<Pokemon>(); //chosen is the arraylist of the user's party pokemon
	private static ArrayList<Pokemon>enemies= new ArrayList<Pokemon>(); //these are all the pokemon the user will be fighting
	private static ArrayList<Pokemon>pokes; //all of the available pokemon in the game
	private static ArrayList<Integer>NPCnum= new ArrayList<Integer>(); 
	private static final int USER=0,CPU=1;
	private static int turn;
	private static int energyAfter;
	private static boolean cannotAttack = false; //If this variable is true, then the computer's attacks can't be done (lack of energy)
	private static Pokemon MyFighter; //the user's current pokemon
	private static Pokemon enemy; //the current fighting enemy
	
	public static void main(String[]args) throws IOException{
		Scanner kb = new Scanner(System.in);
		pokes = new ArrayList<Pokemon>();
		Scanner inFile = new Scanner(new BufferedReader(new FileReader("pokemon.txt")));
		int num = Integer.parseInt(inFile.nextLine());

		for(int i= 0; i<num;i++){
			String word = inFile.nextLine();
			pokes.add(new Pokemon(word)); 
		}
		intro();
		next(); //next initializes the switch of enemy pokemon as well as reselcting whose turn it is
		while(enemies.size()>0){ //as long as there are still enemies to fight
			if (turn == USER){ //If it's the user's turn, then open up their menu options
				battleStats();
				menu();
			}
			else{ //otherwise, let the enemy take their turn
				theirmenu();
			}
			turn = USER + CPU - turn; //swaps the turns
			if (MyFighter.fainted==true){ //if my current pokemon faints
				chosen.remove(MyFighter); //remove it from the party
				if(chosen.size()!=0){
					System.out.println("Oh no...! "+MyFighter+" has fainted!\n Choose your next pokemon.");
					for (int j = 0; j<chosen.size();j++){ //pick a new one (not including the one that fainted obviously)
						System.out.print("| "+(j+1)+"."+chosen.get(j)+" ");
					}
					System.out.print("|\n>>");
        			int chooseNum = kb.nextInt();
        			MyFighter = chosen.get(chooseNum-1);
        			System.out.println(MyFighter+"! I CHOOSE YOU!");

				}
				else{ //if you've gotten to this point, then you've lost the game
					System.out.println("Your Pokemon have all fainted! You've been bested... Better luck next time!");
					break;
				}
			}
			if (enemy.fainted==true){ //if the current enemy faints
				System.out.println(enemy+" has fainted!");
				enemies.remove(enemy);
				System.out.println("Your pokemon have been healed."); //the current battle ends, so you can heal
				System.out.println("------------------------------");
				for(int i=0; i<chosen.size();i++){ //Restores 20 HP to the good guys that are still alive
					chosen.get(i).newHP+=20;
					if (chosen.get(i).getHP()>chosen.get(i).pokeHP){ //also yes im aware of how gross and messy this part looks, sorry
						chosen.get(i).newHP = chosen.get(i).pokeHP; //if it's greater than what it can be, it returns it to the max value that Pokemon can have
					}
				}
				next(); //reselects an enemy as well as whose turn it is
			}
			if (energyAfter == turn){ //decides whose turn to refill the energy after (since a full round has to be done before their energy can refill)
				refill();
			}
		}
		if (enemies.size()==0){ //end game!!
			System.out.println("Amazing! You've beaten all the enemies! You have earned the title 'TRAINER SUPREME'!\n Why don't you play again?");
		}
	}
	public static void intro()throws IOException{
		Scanner kb = new Scanner(System.in);
		Scanner copyPaste = new Scanner(new BufferedReader(new FileReader("intro.txt")));
		while(copyPaste.hasNextLine()){
			String title = copyPaste.nextLine();
			System.out.println(title); //just a fancy title thing
		}
		System.out.println(":*~*:._.:*~*:._.:*~*:._.:*~*:._.:*~*:._.:*~*:._.:*~*:._.:*~*:._.:*~*:\n");
      	System.out.println("Welcome to Pokemon Arena! Choose four Pokemon! (Just enter their number)");
        for (int i = 0; i<pokes.size();i++){
        	System.out.println((i+1)+". "+pokes.get(i));
        }
        while(chosen.size()!= 4){
        	System.out.print(">>");
         	int chooseNum = kb.nextInt();
         	Pokemon selected = pokes.get(chooseNum-1);
         	if (chosen.contains(selected)){ //if the pokemon is already in the user's party, don't let them add it again
         		System.out.print("This pokemon is already in your party. \n");
         	}
         	else{
         		chosen.add(selected);
         	}
        }
        for (int i = 0; i<pokes.size();i++){
        	if(chosen.contains(pokes.get(i))==false){ //as long as the Pokemon isn't already in the user's party
         		enemies.add(pokes.get(i)); //then they become an enemy to you. It's kill or be killed (though it's "faint", really).
         	}
        }
        System.out.println("Who will you start with? \n");
        System.out.print("| 1."+chosen.get(0)+" | 2."+chosen.get(1)+" | 3."+chosen.get(2)+" | 4."+chosen.get(3)+" | \n>>");
        int chooseNum = kb.nextInt(); //chooseNum is used throughout the program as the user's input
        MyFighter = chosen.get(chooseNum-1); //this selects which pokemon they wish to choose as their starter in the fight
        System.out.println(MyFighter+"! I CHOOSE YOU!");
        System.out.println("------------------------------");
    } 
    private static void next(){ //selects an enemy to fight, as well as whose turn will be first
    	enemyselect();
    	System.out.println("...");
    	turnselect();
    }
    private static void enemyselect(){ //from the list of enemies, it selects a random one to fight the user
    	Random randomizer = new Random();
		enemy = enemies.get(randomizer.nextInt(enemies.size()));
		System.out.println(enemy+" wants to fight!");
    }   
   	private static void turnselect(){
   		Random turnchoice = new Random();
		int turn = turnchoice.nextInt(2);
		energyAfter = USER + CPU - turn; //whoever's turn is second, energyAfter occurs after they finish their move
		if(turn == USER){
			System.out.println("You get to go first!");
		}
		if(turn == CPU){
			System.out.println("They get to go first!");
		}
   	}
   	private static void refill(){ //adds back ten energy points to all Pokemon once a round is completed
   		if(MyFighter.energy!=50){
   			MyFighter.energy += 10; //these also ensure that if the Pokemon already have full energy, there's no need to heal them
   		}
   		if (enemy.energy!=50){
   			enemy.energy += 10;
   		}
   	}
   	private static void menu(){ //the user's menu
   		if(MyFighter.stunned == true){
   			System.out.println(MyFighter+" has been stunned for this turn!");
   			pass();
   			MyFighter.stunned = false;
   		}
   		System.out.println("| 1. ATTACK | 2. PASS | 3. RETREAT |");
   		Scanner kb = new Scanner(System.in);
   		System.out.print(">>");
        int chooseNum = kb.nextInt();
        if (chooseNum == 1){ //depending on which number they select, they have three options
        	attack();		 //pretty self explanatory 
        }
        if (chooseNum == 2){
        	pass();
        }
        if (chooseNum == 3){
        	retreat();
        }
      //  MyFighter.disable = false; //the disabled turn ends
	  //	MyFighter.alreadyDisabled = true; //they can't be disabled for this turn anymore
   	}
   	private static void theirmenu(){ //the enemy can only attack or pass, so it's easier to resolve in the attack method what happens
   		if(enemy.stunned == true){
   			System.out.println(enemy+" has been stunned for this turn!");
   			pass();
   			enemy.stunned = false;
   		}
   		attack();
   //		enemy.disable = false;
   //		enemy.alreadyDisabled = true;
   	}
   	private static void attack(){
   		if(turn == USER){ //the attack method for the USER 
   		//	if(MyFighter.disable==false){//for regular attacks
	   			System.out.println("What will you do?");
	   			for (int i = 0; i<MyFighter.attacks.length;i++){ //this will print out all the moves that the user has to choose from
	   				System.out.print("| "+(i+1)+". "+MyFighter.attacks[i].getMove()+" ");
	   			}
	   			System.out.print("| "+(MyFighter.attacks.length+1)+". BACK |\n"); //the option to return to the user's battle menu
	   			Scanner kb = new Scanner(System.in);                       
	        	int chooseNum = kb.nextInt();
	        	if (chooseNum == (MyFighter.attacks.length+1)){ //If the program can't attack (like if they don't have enough energy)
	        		menu();                                 //They have the option to go back and select pass or retreat 
	        	}
	        	else{
	        		if(MyFighter.attacks[chooseNum-1].cost>MyFighter.energy){ //if the cost of the attack is greater than the energy I have
		        		System.out.println("You don't have enough energy to use this attack!"); //then you can't use it
		        		attack(); //So you can try to use another attack
	        		}
	        		else{
		        		Attack pickMove = MyFighter.attacks[chooseNum-1]; //if everything is fine, it takes the attack
		        		System.out.println(MyFighter+" uses "+pickMove.getMove()); //and uses it
		        		MyFighter.attack(enemy,chooseNum-1);
	        		}
	        	}
        	}
       /* 	else{
        		System.out.println("You've been disabled for this turn!");
	   			for (int i = 0; i<MyFighter.disabled.length;i++){ //this will print out all the moves that the user has to choose from
	   				System.out.print("| "+(i+1)+". "+MyFighter.disabled[i].getMove()+" ");
	   			}
	   			System.out.print("| "+(MyFighter.disabled.length+1)+". BACK |\n"); //the option to return to the user's battle menu
	   			Scanner kb = new Scanner(System.in);                       
	        	int chooseNum = kb.nextInt();
	        	if (chooseNum == (MyFighter.disabled.length+1)){ //If the program can't attack (like if they don't have enough energy)
	        		menu();                                 //They have the option to go back and select pass or retreat 
	        	}
	        	else{
	        		if(MyFighter.attacks[chooseNum-1].cost>MyFighter.energy){ //if the cost of the attack is greater than the energy I have
		        		System.out.println("You don't have enough energy to use this attack!"); //then you can't use it
		        		attack(); //So you can try to use another attack
	        		}
	        		else{
		        		Attack pickMove = MyFighter.disabled[chooseNum-1]; //if everything is fine, it takes the attack
		        		System.out.println(MyFighter+" uses "+pickMove.getMove()); //and uses it
		        		MyFighter.attack(enemy,chooseNum-1);
	        		}
	        	}
        	}*/
   		if(turn == CPU){ //the computer has a separate attack method just because it was easier for me to structure it this way
   			ArrayList<Integer>validAttacks = enemy.getNums();
   			if(validAttacks.size()!=0){ //as long as they have a valid move, let the computer select one at random
   				randomMove();
   			}
   			else{ //if there are no valid attacks with their current energy, then they must pass their turn
   				pass();
   			}   			
   		}
   	}
   	private static void randomMove(){
   		Random rand = new Random(); //selects a random attack from the list of valid ones the enemy can use
   		int randNum = rand.nextInt(enemy.getNums().size());
   		/*if(enemy.disable==true){
   			Attack pickMove = enemy.disabled[randNum]; //this attack has been selected
   			System.out.println(enemy+" uses "+pickMove.getMove());
   			enemy.attack(MyFighter, randNum); 
   		}*/
   			Attack pickMove = enemy.attacks[randNum]; //this attack has been selected
   			System.out.println(enemy+" uses "+pickMove.getMove());
   			enemy.attack(MyFighter, randNum); 
   	}
   	private static void pass(){ //they do nothing for the current turn
   		if (turn == USER){
   			System.out.println(MyFighter+" passed this turn.");
   		}
   		else{
   			System.out.println(enemy+" passed their turn.");
   		}
   	}
   	private static void retreat(){
   		System.out.println("Who will you switch in?");
   		for (int j = 0; j<chosen.size();j++){ //displays their party again
			System.out.print(" | "+(j+1)+"."+chosen.get(j));
		}
		System.out.print(" |\n>>");
   		Scanner kb = new Scanner(System.in);                       
        int chooseNum = kb.nextInt();
        if (chosen.get(chooseNum-1).equals(MyFighter)==false){ //as long as they do not switch in their current pokemon for itself, they can select a new Pokemon
        	MyFighter = chosen.get(chooseNum-1);              
        	System.out.println(MyFighter+"! I CHOOSE YOU!");
        }                      
        else{
			System.out.println("This pokemon is already in battle.");
			retreat();  //so that their turn doesn't finish without them keeping the same pokemon
        }
   	}
   	private static void battleStats(){ //displays the basic battle stats that the user needs to see throughout the fight
   		System.out.println(MyFighter+"("+MyFighter.type+" type): "+MyFighter.getHP()+" HP, "+MyFighter.getEnergy()+" energy remaining");
   		System.out.println(enemy+"("+enemy.type+" type): "+enemy.getHP()+" HP, "+enemy.getEnergy()+" energy remaining");
   	}
}