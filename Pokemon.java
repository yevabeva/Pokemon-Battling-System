//The Pokemon class is used to build Pokemon, which organizes the essential information of these Pokemon so they can be used accordingly
import java.util.*;
public class Pokemon {
	private String name;
	public int pokeHP;
	public int newHP; //newHP is the HP that is going to change when Pokemon are attacked or healed
	public String type; //the type that the Pokemon is
	private String resist; //the type that the Pokemon resists
	private String weakness; //the type that the Pokemon is weak to
	public int energy; //the amount of energy they have
	public Attack [] attacks; //an array of the attacks each pokemon has
	public Attack [] disabled; //an array of attacks that have less damage due to the disable effect
	public boolean fainted; //flag for whether a Pokemon faints (dies technically)
	public boolean stunned; //flag for the stun special                                  //too many flags probably..
	//public boolean disable; //flag for switching up the attack arrays (also probably unnecessary)
	//public boolean alreadyDisabled; //the flag that indicates a pokemon has already been disabled before
    public Pokemon(String stats){
    	String []items = stats.split(",");
    	name = items[0];
        pokeHP = Integer.parseInt(items[1]); //changes items[1] to an integer
        newHP = pokeHP; //pokeHP doesn't change and acts as a limiter for the Pokemon's newHP
        fainted = false; //no Pokemon starts off unconscious
        type = items[2];
        resist = items[3];
        weakness = items[4];
    	int numA = Integer.parseInt(items[5]);
    	attacks = new Attack[numA];
   	    for(int i=0; i< numA; i++){ //adds the attacks and their info to the arrays
   			attacks[i] = new Attack(items[6+i*4],items[7+i*4],items[8+i*4],items[9+i*4]);
   			//disabled[i] = new Attack(items[6+i*4],items[7+i*4],items[8+i*4],items[9+i*4]);
        }
        energy = 50; //their starting energy
        
        /*for(int i = 0;i<disabled.length;i++){
        	if(disabled[i].damage-10<0){ //if disabled causes the damage to go below zero, keep it at zero minimum
        		disabled[i].damage = 0;
        	}
        	else{
        		disabled[i].damage-=10;
        	}
        }*/
        
    }
    public ArrayList<Integer> getNums(){ //an arraylist of valid attacks
    	ArrayList<Integer>nums = new ArrayList<Integer>();
    	for(int i=0; i<attacks.length; i++){
    	//	if(disable==false){
    		if(attacks[i].getCost()<= energy){
    				nums.add(i);
    		}
    		//else{
    		//	if(disabled[i].getCost()<= energy){
    		//		nums.add(i);
    		//	}
    	//	}
    	}
    	return nums;
    }
    //NOTE: I DECIDED TO COPY THE SPECIALS INTO THE RESIST AND WEAKNESS IF STATEMENTS
    //BECAUSE IF THEIR ATTACK IS A SPECIAL ATTACK /AND/ THE TYPES AFFECT ONE ANOTHER, IT HAS TO BE ADDRESSED DIFFERENTLY (though I might be overthinking)
    //I ONLY DID SO FOR THE SPECIALS THAT DID DAMAGE
    public void attack(Pokemon target, int pos){ 
    	Attack move = attacks[pos]; //the move is in the attacks array at the given position
    	energy -= move.getCost(); //subtract the cost of the move from energy
    	if(energy<0){ //they should not have negative energy
    		energy = 0;
    	}
  /*  	if(move.getSpecial().equals("disable")){
    		if(target.alreadyDisabled==true){
    			System.out.println("This pokemon cannot be disabled again."); //incomplete feature
    		}
    		else{
    			disable = true;
    		}
    	}*/
    	if(move.getSpecial().equals("recharge")){//this special heals. If it heals over their maximum HP, bring it back down
    		newHP+=20;
    		if (newHP>pokeHP){
    			newHP = pokeHP; //as in set it to be its full health
    		}
    	}
    	if(target.resist.equals(type)){ //if the target is resistant to the attacking Pokemon's type
    		if(move.getSpecial().equals("wild card")){ //if their special is wild card, it has a 50% chance of hitting but will only deal half damage
    			System.out.println("The attack missed.");
    			if(Math.random()>.5){
    				System.out.println("It was not very effective...");
    				target.newHP -= move.getDamage()/2;
    			}    		
    		}
    		else if(move.getSpecial().equals("stun")){ //has a 50% chance of stunning the enemy (they skip their next turn)
    			System.out.println("It was not very effective...");
    			target.newHP -= move.getDamage()/2;
    			if(Math.random()>.5){
    				stunned = true;
    			}
    		}
    		else if(move.getSpecial().equals("wild storm")){ //wild storm attack can hit endlessly on a 50% chance
    			System.out.println("The attack missed.");
    			while(Math.random()>.5){
    				System.out.println("A wild storm blew in!");
    				System.out.println("It was not very effective...");
    				target.newHP -= move.getDamage()/2;
    			}    		
    		}
    		else{
    			System.out.println("It was not very effective...");
    			target.newHP -= move.getDamage()/2; //does half as much damage to the target than usual
    		}
    	}
    	else if(target.weakness.equals(type)){//if the target's weakness is the attacking Pokemon's type
    		if(move.getSpecial().equals("wild card")){ //if their special is wild card, it has a 50% chance of hitting and will do twice as much damage
    			System.out.println("The attack missed.");
    			if(Math.random()>.5){
    				System.out.println("It was super effective!");
    				target.newHP -= move.getDamage()*2;
    			}    		
    		}
    		else if(move.getSpecial().equals("stun")){//has a 50% chance of stunning the enemy (they skip their next turn)
    			System.out.println("It was super effective!");
    			target.newHP -= move.getDamage()*2;
    			if(Math.random()>.5){
    				target.stunned = true;
    			}
    		}
    		else if(move.getSpecial().equals("wild storm")){//wild storm attack can hit endlessly on a 50% chance
    			System.out.println("The attack missed.");
    			while(Math.random()>.5){
    				System.out.println("A wild storm blew in!" );
    				System.out.println("It was super effective!");
    				target.newHP -= move.getDamage()*2;
    			}    		
    		}
    		else{
    			System.out.println("It was super effective!");
    			target.newHP -= move.getDamage()*2; //does twice as much damage to the target than usual
    		}	
    	}
    	else{
    		if(move.getSpecial().equals("wild card")){ //if their special is wild card, it has a 50% chance of hitting
    			System.out.println("The attack missed.");
    			if(Math.random()>.5){
    				target.newHP -= move.getDamage();
    			}    		
    		}
    		else if(move.getSpecial().equals("stun")){//has a 50% chance of stunning the enemy (they skip their next turn)
    			target.newHP -= move.getDamage();
    			if(Math.random()>.5){
    				stunned = true;
    			}
    		}
    		else if(move.getSpecial().equals("wild storm")){//wild storm attack can hit endlessly on a 50% chance
    			System.out.println("The attack missed.");
    			while(Math.random()>.5){
    				System.out.println("A wild storm blew in!" );
    				target.newHP -= move.getDamage();
    			}    		
    		}
    		else{
    			target.newHP -= move.getDamage(); //deals the regular amount of damage
    		}
    	}
    	if (target.newHP<=0){ //if they have no health left, they have fainted 
        	target.fainted = true;
        }
    }
	public String toString(){
    	return name;
    }
    public int getEnergy(){ //returns the Pokemon's energy
    	return energy;
    }
    public int getHP(){ //returns the Pokemon's health 
    	return newHP;
    }
}