package system;

import java.awt.Color;
import java.io.Serializable;
import java.util.*;

public class CEnvironement extends Observable implements Serializable {
	public static CEnvironement sInstance = null;
    protected Random mRandomGen;
    public double mWidth;
    public double mHeight;
    public ArrayList<CBase> mBaseList;
    public Vector<CNourriture> mNourritureList;
    //public ArrayList<CNourriture> mNourritureList;
    public ArrayList<CZoneAEviter> mZoneAEviterList;
    
    //protected int mIterCounts = 0;

    private CEnvironement() {
    	mBaseList = new ArrayList<CBase>();
    	mNourritureList = new Vector<CNourriture>();
    	mZoneAEviterList = new ArrayList<CZoneAEviter>();
    
        // Création du générateur aléatoire.
        mRandomGen = new Random();
    }
    
    private CEnvironement(CEnvironement env) {
    	mBaseList = env.mBaseList;
    	mNourritureList = env.mNourritureList;
    	mZoneAEviterList = env.mZoneAEviterList;
    
        // Création du générateur aléatoire.
        mRandomGen = new Random();
    }

    /**
     * Singleton
     * @return
     */
    public static CEnvironement getInstance() {
		if(sInstance == null) {
			sInstance = new CEnvironement();
		}
		return sInstance;
	}
    
    public static void ImportEnvironement(CEnvironement env) {
    	sInstance = new CEnvironement(env);
    }
    
    
        
    /**
     * 
     * @param pNourrite
     * @return
     */
    public CNourriture catchNourriture(CNourriture pNourrite) {
        	pNourrite.decreaseSize();
        	return pNourrite;
    }
    
    /**
     * Initialisation de l'environnement de base
     * @param _nbBase
     * @param _nbAgents
     * @param x
     * @param y
     * @param _nbNourriture
     */
    public void init(int _nbBase, int _nbAgents, int x, int y, int _nbNourriture) 
    {  	
    	mWidth = x;
    	mHeight = y;
    	mBaseList.clear();
    	// Ajout du nombre de bases
    	for(int i = 0; i < _nbBase; i++)
    	{
    		mBaseList.add(new CBase(x/10.0, y/10.0, _nbAgents, Color.RED, 10));
    	}
    		
    	mNourritureList.clear();
    	// Ajout de la nourriture sur le canvas
    	for(int i = 0; i < _nbNourriture; i++)
    	{
    		double _x  = mRandomGen.nextDouble() * mWidth;
    		double _y  = mRandomGen.nextDouble() * mHeight;
    		mNourritureList.add(new CNourriture(_x, _y, Color.BLACK, 20));
    	}
    	// Clean des obstacles
    	mZoneAEviterList.clear();
    }
    /**
     * 
     */
    public void update()
    {
    	for(CBase b : mBaseList)
    	{
    		b.bougerAgents();
    	}
    	combatAgent();
    	setChanged();
    	notifyObservers();
    }
    
    public void combatAgent() {
    	for(CBase b : mBaseList) {
    		for(CAgent agent : b.fourmiz) {
    			// chaque agent d'une base regarde si il rentre en contact avaec un agent 
    			searchAgentColision(b,agent);
    		}
    	}
    }
    
    /**
     * Gestion de la collision entre agents
     * @param base
     * @param mAgent
     */
    public void searchAgentColision(CBase base, CAgent mAgent) {
    	for(CBase b : mBaseList) {
    		if(base!= b) {
    			for(CAgent agent : b.fourmiz) {
        			if((mAgent.posX == agent.posX) && (mAgent.posY == agent.posY)) {
        				if(mAgent.mCombat > agent.mCombat) {
        					// si notre agent est plus faible, il perd le combat
        					b.killAgents(agent,mAgent.mCombat);
        				}
        				else if(mAgent.mCombat < agent.mCombat) {
        					// si notre agent est plus fort, il gagne le combat
        					base.killAgents(mAgent,agent.mCombat);
        				}
        				else {
        					// si notre agent est de meme attaque, les deux perdent des points 
        					base.killAgents(mAgent,agent.mCombat);
        					b.killAgents(agent,mAgent.mCombat);
        				}
        			}
        		}		
			}
    	}
    }
    
    public static void updateEnvironement(CEnvironement envDistant) {
    	sInstance = envDistant;
    }
    
    
}