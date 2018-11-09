//The Attack class builds attacks so that they can be used in the Pokemon class to deal damage according to their properties
public class Attack{
	private String name;
	public int cost;
	public int damage;
	private String special;

    public Attack(String n, String c, String d, String s) {
    	name = n; //name of the attack
    	cost = Integer.parseInt(c); //how much it costs
    	damage = Integer.parseInt(d); //how much damage it deals
    	special = s; //whether or not it has a special
    }
    public String getMove(){
    	return name;
    }
    public int getDamage(){
    	return damage;
    }
    public int getCost(){
    	return cost;
    }
    public String getSpecial(){
    	return special;
    }
}