package system;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import java.awt.Color;
import java.awt.Graphics;
import java.io.Serializable;

//Etat de combat -> recuperation de nourriture

public class CAgent extends CObject implements Serializable{
	protected final static double STEP = 4;
	protected final static double CHANGING_DIRECTION_PROB = 0.05;
	public static final double DISTANCE_MIN = 5;
	public static final double SIZE = 5;
	public static final int RAYON_EPOQUE = 2;
	public static int SIZE_EPOQUE = 6;

	private int indexPheromones = 0;

	protected CNourriture mLoading;
	protected double mSpeedX;
	protected double mSpeedY;
	public List<CPheromone> pheromones;

	protected int mCombat;
	public int PointdeVie;
	public double Energizer = 100;
	public static final int maxCombat = 5;
	public static final int minCombat = 0;
	public ArrayList<double[]> epoque; 
	
	public boolean mBusy = false;
	private int saveAlpha = 1;
	private int saveIndexPhero;

	protected void normalize() {
		double lLenght = Math.sqrt(mSpeedX * mSpeedX + mSpeedY * mSpeedY);
		mSpeedX /= lLenght;
		mSpeedY /= lLenght;
	}

	public CAgent(double pPosX, double pPosY) {
		this.pheromones = new ArrayList<CPheromone>();
		posX = pPosX;
		posY = pPosY;

		epoque = new ArrayList<double[]>();
		this.pheromones.add(new CPheromone((int) posX, (int) posY));

		mSpeedX = CEnvironement.getInstance().mRandomGen.nextDouble() - 0.5;
		mSpeedY = CEnvironement.getInstance().mRandomGen.nextDouble() - 0.5;
		normalize();
	}

	public boolean isLoaded() {
		saveIndexPhero = pheromones.size() - 1;
		return mBusy = true;
	}

	public boolean unLoad() {
		saveIndexPhero = 0;
		return mBusy = false;
	}

	protected void MiseAJourPosition() {
		if (CEnvironement.getInstance().mRandomGen.nextDouble() < CHANGING_DIRECTION_PROB) {
			mSpeedX = CEnvironement.getInstance().mRandomGen.nextDouble() - 0.5;
			mSpeedY = CEnvironement.getInstance().mRandomGen.nextDouble() - 0.5;
		}
		if (mBusy) {
			calculDistancePheromone();
			normalize();
			saveIndexPhero = (saveIndexPhero - 1 >= 0) ? saveIndexPhero - 1 : 0;
		}
		posX += STEP * mSpeedX;
		posY += STEP * mSpeedY;
		
		if (indexPheromones == 2) {
			indexPheromones = 0;
			if(mBusy) {
				this.pheromones.add(new CPheromone((int)posX, (int)posY, saveAlpha));
				saveAlpha = (saveAlpha - 1 >= 20)?saveAlpha - 1:20;
			}else {
				this.pheromones.add(new CPheromone((int)posX, (int)posY));
			}
		}
		indexPheromones++;
	}
	
	public void backToHome() {
		calculDistancePheromone();
		saveIndexPhero = (saveIndexPhero - 1 >= 0) ? saveIndexPhero - 1 : 0;
	}
	
	public void calculDistancePheromone()
	{
		CPheromone unePheromone = pheromones.get(saveIndexPhero);
		double distanceCarre = DistanceCarre(unePheromone);
		double distance = Math.sqrt(distanceCarre);
		double diffX = (unePheromone.getPosX() - posX) / distance;
		double diffY = (unePheromone.getPosY() - posY) / distance;
		mSpeedX = diffX / 2;
		mSpeedY = diffY / 2;
		normalize();
	}

	int index = 0;

	public void decreaseEnergizer() {
		if (index == 20) {
			index = 0;
			if (this.Energizer > 0) {
				double n = (double) (Math.random() * 3);
				this.Energizer = this.Energizer - n;
			}
		}
		index++;
	}


	public boolean EviterMurs() {

		double lWidth = CEnvironement.getInstance().mWidth;
		double lHeight = CEnvironement.getInstance().mHeight;
		double minWidth = 0.0;
		double minHeight = 0.0;

		if (posX < minWidth) {
			posX = minWidth;
		} else if (posY < minHeight) {
			posY = minHeight;
		} else if (posX > lWidth) {
			posX = lWidth;
		} else if (posY > lHeight) {
			posY = lHeight;
		}

		double min = Math.min(posX - minWidth, posY - minHeight);
		min = Math.min(min, lWidth - posX);
		min = Math.min(min, lHeight - posY);

		if (min < DISTANCE_MIN) {
			if (min == (posX - minWidth)) {
				mSpeedX += 0.3;
			} else if (min == (posY - minHeight)) {
				mSpeedY += 0.3;
			} else if (min == (lWidth - posX)) {
				mSpeedX -= 0.3;
			} else if (min == (lHeight - posY)) {
				mSpeedY -= 0.3;
			}
			normalize();
			return true;
		}
		return false;
	}
	
    protected void updateDirection(List<CNourriture> pNourritureList) {
        // o� aller ?
        List<CNourriture> lInZone = new ArrayList<CNourriture>();
        lInZone.addAll(pNourritureList);
        lInZone.removeIf(d -> (distance(d) > d.rayon));
        Collections.sort(lInZone, (CNourriture g1, CNourriture g2) -> (distance(g1) < distance(g2) ? -1: 1));
        CNourriture lGoal = null;
        if (!lInZone.isEmpty()) {
            lGoal = lInZone.get(0);
        }

        // Avons-nous un but ?
        if (lGoal == null || mBusy) {
            if (CEnvironement.getInstance().mRandomGen.nextDouble() < CHANGING_DIRECTION_PROB) {
                mSpeedX = CEnvironement.getInstance().mRandomGen.nextDouble() - 0.5;
                mSpeedY = CEnvironement.getInstance().mRandomGen.nextDouble() - 0.5;
            }
            if (mBusy && lGoal == null) {
                mBusy = false;
            }
        }
        else {
            // Aller au but
            mSpeedX = lGoal.posX - posX;
            mSpeedY = lGoal.posY - posY;
            // But atteint ?
            if (distance(lGoal) < STEP) {
                if (mLoading == null) {
                        mLoading = CEnvironement.getInstance().catchNourriture(lGoal);
                }
                else {
                    mLoading = null;
                }
                mBusy = Boolean.TRUE;
            }
        }
        normalize();
    }
    
    /**
     * Afficher les phéromones sur le canvas
     * @param pG Le Canvas
     * @param baseColor la couleur de base 
     */
    public void drawPheromones(Graphics pG, Color baseColor) {
    		
    	if(pheromones.size() > 0 ) {
    		for (int i = 0; i < pheromones.size(); i++) {
    			if(pheromones.get(i).getTransparence() > 15) {
    				// On se sert de la couleur de la base pour y appliquer une transparence définie par l'alpha de la phéromone
    				Color myColour = new Color(baseColor.getRed(),
    						baseColor.getGreen(),
    						baseColor.getBlue(),
    						pheromones.get(i).getTransparence());
    				pG.setColor(myColour);
    				pG.fillOval(pheromones.get(i).getPosX(),
    						pheromones.get(i).getPosY(),
    						pheromones.get(i).PHEROMONE_HEIGHT,
    						pheromones.get(i).PHEROMONE_WIDTH
        				);
    			}			
    		}
    	}
    }
    /**
     *  Permet d'éviter les obstacles sur le canvas
     * @return Boolean
     */

	protected boolean EviterObstacles() {
		ArrayList<CZoneAEviter> obstacles = CEnvironement.getInstance().mZoneAEviterList;
		if (!obstacles.isEmpty()) {
			// Recherche de l'obstacle le plus proche
			CZoneAEviter obstacleProche = obstacles.get(0);
			double distanceCarre = DistanceCarre(obstacleProche);
			for (CZoneAEviter o : obstacles) {
				if (DistanceCarre(o) < distanceCarre) {
					obstacleProche = o;
					distanceCarre = DistanceCarre(o);
				}
			}

			if (distanceCarre < (obstacleProche.rayon * obstacleProche.rayon)) {
				// Si collision, calcul du vecteur diff
				double distance = Math.sqrt(distanceCarre);
				double diffX = (obstacleProche.posX - posX) / distance;
				double diffY = (obstacleProche.posY - posY) / distance;
				mSpeedX = mSpeedX - diffX / 2;
				mSpeedY = mSpeedY - diffY / 2;
				normalize();
				return true;
			}
		}
		return false;
	}

	/**
	 *  Retourne vrai ou faux si la nourriture a été trouvée sur le chemin
	 * @return Boolean
	 */
	public boolean nourritureFind() {
		if (this.mBusy == false) {
			for (CNourriture mNourriture : CEnvironement.getInstance().mNourritureList) {
				double mRayon = mNourriture.getRayon();
				if ((this.posX >= (mNourriture.getPosX() - mRayon) && (this.posX) <= (mNourriture.getPosX() + mRayon))
						&& ((this.posY >= (mNourriture.getPosY() - mRayon))
								&& (this.posY <= (mNourriture.getPosY() + mRayon)))) {
					// On réduit la taille de la nourriture
					mNourriture.decreaseSize();
					this.isLoaded();
					saveAlpha = 80;
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * permet de voir si un agent est bloque en fonction des �poques 
	 */
    public void statusEpoque() {
    	getEpoque();
    	boolean bloque[];
    	boolean fullBloque;
    	if(epoque.size() == SIZE_EPOQUE) {
    		bloque = new boolean[SIZE_EPOQUE-1];
    		double[] positionCercle = epoque.get(SIZE_EPOQUE-1);
    		for (double[] eq: epoque) {
    			int i = 0;
    			// on regarde si elle sont dans la zone de l'epoque 
    			if ( Math.sqrt((eq[0]-positionCercle[0])*(eq[0]-positionCercle[0])+(positionCercle[1] - eq[1])*(positionCercle[1] - eq[1])) < RAYON_EPOQUE) {
    				bloque[i] = true;
    			} else {
    				bloque[i] = false;
    			}
    		}	
    		fullBloque = true;
    		for(boolean e : bloque) {
    			if(!e) {
    				fullBloque = false ; 
    			}
    		}
    		if(fullBloque) {
        		posX = epoque.get(3)[0] - positionCercle[0]  ;
        		posX = epoque.get(3)[1] - positionCercle[1]  ;
        		EpoqueZone();
        	}
    	}
    }
    
    public void getEpoque() {
    	double[] posCurrent = {posX,posY};
		epoque.add(posCurrent);
		if(epoque.size() == SIZE_EPOQUE+1) {
			epoque.remove(SIZE_EPOQUE);
		};
    }
    
    public void EpoqueZone() {
    	ArrayList<CZoneAEviter> obstacles = CEnvironement.getInstance().mZoneAEviterList;
    	for (CZoneAEviter ob : obstacles) {
			if((posX == ob.posX)&&(posY==ob.posY)) {
				this.posX=this.posX/2;
				this.posY=this.posY/2;
				EpoqueZone();
			}
		}
    }
    
    /**
     * 
     * @return Boolean
     */
	public boolean hitHome() {

		for (CBase mBase : CEnvironement.getInstance().mBaseList) {
			double mRayon = mBase.getRayon();
			if ((this.posX >= (mBase.getPosX() - mRayon) && (this.posX) <= (mBase.getPosX() + mRayon))
					&& ((this.posY >= (mBase.getPosY() - mRayon)) && (this.posY <= (mBase.getPosY() + mRayon)))) {
				if (this.mBusy == true) {
					this.mBusy = false;
				}
				this.Energizer = 100;
				return true;
			}
		}
		return false;
	}

	protected void MiseAJour() {
		EviterObstacles();
		EviterMurs();
		nourritureFind();
		hitHome();
		decreaseEnergizer();
		MiseAJourPosition();
		statusEpoque();
	}

	/**
	 * Gestion du combat d'un agent 
	 */
	protected void combat() {
		// attaque et point de vie d'un agent
		PointdeVie = 10;
		mCombat = (int) (Math.random() * (maxCombat - minCombat));
	}
}