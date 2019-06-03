package system;
import java.awt.Color;
import java.awt.Graphics;
import java.io.Serializable;
import java.util.ArrayList;

public class CBase extends CObject implements Serializable{
	
	public int rayon = 10;
	public int nbAgents = 0;
	public ArrayList<CAgent> fourmiz;
	private Color color;
	
	/**
	 * 
	 * @param _x
	 * @param _y
	 * @param _nbAgents
	 * @param _color
	 * @param _rayon
	 */
	public CBase(double _x, double _y, int _nbAgents, Color _color, int _rayon)
	{
		this.posX = _x;
		this.posY = _y;
		this.nbAgents = _nbAgents; 
		this.fourmiz = new ArrayList<CAgent>();
		this.color = _color;
		this.rayon = _rayon;
		
		for(int i = 0; i < this.nbAgents;i++)
		{
			this.fourmiz.add(new CAgent(this.posX, this.posY));
		}
	}
	
	
	public Color getColor() {
		return this.color;
	}
	/**
	 * 
	 */
	public void bougerAgents()
	{	
		for(int cpt = 0; cpt < this.nbAgents; cpt++)
		{
			this.fourmiz.get(cpt).MiseAJour();
		}
	}
	
	/**
	 * Dessine le canas de base
	 * @param pG
	 */
	public void afficherBase(Graphics pG)
	{
		pG.setColor(color);
		pG.fillOval((int)this.posX, (int)this.posY, rayon, rayon);
	}
	
	/**
	 * Dessine les différents agents sur le canvas
	 * @param pG Canvas instancié
	 */
	public void afficherAgents(Graphics pG)
	{
		
		
		for(int cpt = 0; cpt < this.nbAgents; cpt++)
		{
			if(this.fourmiz.get(cpt).Energizer <= 0) {
				pG.setColor(Color.BLACK);
				this.fourmiz.get(cpt).backToHome();			}
			else {
				pG.setColor(color);
			}
			this.fourmiz.get(cpt);
			this.fourmiz.get(cpt);
			pG.fillOval((int)this.fourmiz.get(cpt).posX,
					(int)this.fourmiz.get(cpt).posY,
					(int)CAgent.SIZE,
					(int)CAgent.SIZE);
		}
	}
	

	/**
	 * Réduction des pdv d'un agent et retrait d'un agent s'il meurt
	 * @param agent
	 * @param combat
	 */
	public void killAgents(CAgent agent, int combat) {
		// enleve un agent si il meurt, en fonction des point de vie 
		agent.PointdeVie = agent.PointdeVie-combat;
		if(agent.PointdeVie <= 0) {
			this.fourmiz.remove(agent);
		}
	}

	public double getRayon() {
		return this.rayon;
	}

	public double getPosX() {
		return this.posX;
	}
	
	public double getPosY() {
		return this.posY;
	}
	
}
