package gui;

import system.CAgent;
import system.CBase;
import system.CEnvironement;
import system.CNourriture;
import system.CZoneAEviter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Observable;
import java.util.Observer;
import java.util.Timer;
import java.util.TimerTask;

public class CMainPanel extends JPanel implements Observer, MouseListener {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final int BASE_COUNT = 1;
    private static final int AGENTS_COUNT = 30;
    private static final int NOURRITURE_COUNT = 2;
   // private static final int AGENT_WIDTH = 3;
   // private static final int AGENT_HEIGHT = 3;
   
    private static final int TIMER_DELAY = 0;
    private static final int TIMER_PERIOD = 10;
    public static final Color backgroundColor = new Color(128, 128, 128);
    int incrColor =0;
    public Color[] mArrayColor = {Color.BLUE,Color.CYAN,Color.GRAY,Color.GREEN,Color.LIGHT_GRAY,Color.MAGENTA,Color.ORANGE,Color.PINK,Color.RED,Color.WHITE,Color.YELLOW};
    
    private Timer mTimer;
    boolean mInProgress = false;
    private TimerTask mTask;
    private CEnvironement mEnv = null;


    /**
     * 
     */
    public CMainPanel() {
        // Fond gris.
    	this.setBackground(CMainPanel.backgroundColor);

    	// Gestion souris.
    	this.addMouseListener(this);
    }
    
    /**
     * Démarrage du panel
     */
    public void launch() {
        mEnv = CEnvironement.getInstance();
        mEnv.init(BASE_COUNT, AGENTS_COUNT, getWidth(), getHeight(),NOURRITURE_COUNT);
        mEnv.addObserver(this);
        
        mTimer = new Timer();
        mTask = new TimerTask()
        {
        	@Override
        	public void run() {mEnv.update();}
        };
        mTimer.scheduleAtFixedRate(mTask, TIMER_DELAY, TIMER_PERIOD);		
    }

    @Override
    public void update(Observable pObservable, Object pArg) 
    {
    	this.repaint();
    }

    @Override
    public void paintComponent(Graphics pG) {
        super.paintComponent(pG);
        if(mEnv != null) {
        	for(CBase b : mEnv.mBaseList)
            {
            	b.afficherBase(pG);
            	b.afficherAgents(pG);
            	
            	for (CAgent agent : b.fourmiz) {
            		// Pour chacun des agents on déssine ses phéromones
            		agent.drawPheromones(pG, b.getColor());
    			}
            }
            	for(CNourriture n : mEnv.mNourritureList)
                {
                	n.afficher(pG);
                } 
            
            for(CZoneAEviter z : mEnv.mZoneAEviterList)
            {
            	z.afficher(pG);
            }
        }
    }
    
    
    @Override
    public void mouseClicked(MouseEvent e)
    {    	
    	if (SwingUtilities.isLeftMouseButton(e)) {
    		leftClickAction(e);
    	} 
    	else if (SwingUtilities.isRightMouseButton(e))
    	{
    		rightClickAction(e);
    	}
    	else if(SwingUtilities.isMiddleMouseButton(e))
    	{
    		moletteClickAction(e);
    	}
    }
    
	public void moletteClickAction(MouseEvent e)
	{
		mEnv.mNourritureList.add(new CNourriture(e.getX(), e.getY(), Color.BLACK, 20));
	}
    public void leftClickAction(MouseEvent e)
    { 
    	if(incrColor > 10)
    		incrColor=0;
    	else
    	{
    		mEnv.mBaseList.add(new CBase(e.getX(), e.getY(), AGENTS_COUNT, mArrayColor[incrColor], 10));
    		incrColor++;
    	} 
    }

    public void rightClickAction(MouseEvent e) {
       mEnv.mZoneAEviterList.add(new CZoneAEviter(e.getX(), e.getY(), 20.0));
    }
    
    @Override
    public void mousePressed(MouseEvent e) {}
    
    @Override
    public void mouseReleased(MouseEvent e) {}

	@Override
	public void mouseEntered(MouseEvent e) {}

	@Override
	public void mouseExited(MouseEvent e) {}
}