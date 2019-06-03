package system;

public class CObject implements ISendable {
	public double posX;
	public double posY;
	
	public CObject() {};
	public CObject(double _x, double _y) {
		posX = _x;
		posY = _y;
	}
	
	/**
	 * Retourne la distance entre l'object courant et un deuxième object
	 * @param o
	 * @return
	 */
    public double distance(CObject o) {
        return Math.sqrt((o.posX - posX) * (o.posX - posX) + (o.posY - posY) * (o.posY - posY));
    }
    
    /**
     * Retourne la distance au carré entre l'object courant et un deuxième object
     * @param o
     * @return
     */
    public double DistanceCarre(CObject o) {
        return (o.posX - posX) * (o.posX - posX) + (o.posY - posY) * (o.posY - posY);
    }
}
