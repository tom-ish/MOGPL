package robot;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.Random;

import javax.swing.JPanel;

public class World extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public World(Integer lignes, Integer colonnes,
			Integer lignesRW, Integer colonnesRW,
			Point[][] world, Point[][] robotWorld,
			Integer xi, Integer yi, Integer xf, Integer yf,
			Orientation o){
		World.M = lignes;
		World.N = colonnes;
		World.M_RW = lignesRW;
		World.N_RW = colonnesRW;
		this.world = world;
		this.robotWorld = robotWorld;
		this.x_i = xi;
		this.y_i = yi;
		this.x_f = xf;
		this.y_f = yf;
		this.orientation = o;

		createRobotWorld();
		placeRobot();
		setFinalCase();

		System.out.println(" ------------------------------- ");
		showMatrice(world);
		showMatrice(robotWorld);
		System.out.println(" ------------------------------- ");

	}
	
	public String id;

	// M = LIGNE, N = COLONNE
	public static Integer M;
	public static Integer N;
	private static Integer M_RW;
	private static Integer N_RW;
	private Point[][] world;
	private Point[][] robotWorld;
	private Integer x_i, y_i;
	private Integer x_f, y_f;
	private Integer current_x, current_y;
	private Orientation orientation;
	public final static String[] direction = {"droite","gauche"};
	public long executionTime;
	public Integer nbObstacles;

	public LinkedList<Point> cheminBFS = new LinkedList<Point>();

	public final Integer arrowSize = 20;
	public Integer width;
	public Integer height;

	public boolean bfsFound = false;


	public Point getPoint(int i, int j){ return this.robotWorld[i][j]; }
	public Point[][] getWorld(){ return this.world; }
	public Point[][] getRobotWorld(){ return this.robotWorld; }
	public Integer getX_i(){ return this.x_i; }
	public Integer getY_i(){ return this.y_i; }
	public Integer getCurrentX(){ return this.current_x; }
	public Integer getCurrentY(){ return this.current_y; }
	public Orientation getOrientation(){ return this.orientation; }
	public long getExecutionTime(){ return this.executionTime; }
	public Integer getNbObstacles(){ return this.nbObstacles; }
	
	public void setNbObstacles(Integer i){ this.nbObstacles = i; }
	
	public void setString(String s){ this.id = s; }
	public String getId(){	return this.id; }
	public World getWorldById(String s){
		if(s.equalsIgnoreCase(this.id)) return this;
		else return null;
	}


	public void showMatrice(Point[][] matrice){
		System.out.println("Matrice de " + matrice.length + " lignes, " + matrice[0].length + " colonnes");		
		System.out.println(" ----- WORLD -----");
		for(int i = 0; i < matrice.length; i++){
			for(int j = 0; j < matrice[0].length; j++){
				System.out.print(matrice[i][j].getKey() + " ");
			}
			System.out.println();
		}
		System.out.println(" ----- WORLD -----");
		System.out.println("Point de départ: [" + x_i + "," + y_i + "]");
		System.out.println("Point d'arrivée: [" + x_f + "," + y_f + "]");
		System.out.println("Orientation: " + orientation);
	}

	/* FONCTIONS DE MOUVEMENT DU ROBOT */
	public void placeRobot(){
		Random random = new Random();
		if(x_i == null && y_i == null){
			x_i = random.nextInt(robotWorld.length);
			y_i = random.nextInt(robotWorld[0].length);
			System.out.println("x_i = " + x_i + " y_i = " + y_i);
			while(robotWorld[x_i][y_i].getKey() == 1){
				x_i = random.nextInt(robotWorld.length);
				y_i = random.nextInt(robotWorld[0].length);
			}
		}
		else {
			current_x = x_i;
			current_y = y_i;
		}
		this.robotWorld[x_i][y_i] = new Point(5, true, x_i, y_i, orientation, null, 0);
	}

	public Point tourne(String o, Point p){
		Point rslt = p.clone();
		if(o.equalsIgnoreCase("droite")){
			switch(rslt.getOrientation()){
			case est:
				rslt.setOrientation(Orientation.sud);
				break;
			case nord:
				rslt.setOrientation(Orientation.est);
				break;
			case ouest:
				rslt.setOrientation(Orientation.nord);
				break;
			case sud:
				rslt.setOrientation(Orientation.ouest);
				break;
			default:
				break;
			}
		}
		else if(o.equalsIgnoreCase("gauche")){
			switch(rslt.getOrientation()){
			case est:
				rslt.setOrientation(Orientation.nord);
				break;
			case nord:
				rslt.setOrientation(Orientation.ouest);
				break;
			case ouest:
				rslt.setOrientation(Orientation.sud);
				break;
			case sud:
				rslt.setOrientation(Orientation.est);
				break;
			default:
				break;

			}
		}
		else {
			// On ne fait rien d'autre que tourner a droite ou a gauche
		}
		rslt.setParent(p);
		rslt.setCost(p.getCost()+1);
		return rslt;
	}
	
	
	/* FONCTIONS DE MOUVEMENT DU ROBOT */


	/* ALGORITHME DE PARCOURS EN LARGEUR */
	public LinkedList<Point> bfs(){
		Date uDate = new Date (System.currentTimeMillis ()); //Relever l'heure avant le debut du progamme (en milliseconde) 

		LinkedList<Point> visited = new LinkedList<Point>();
		ArrayList<Point> voisins = new ArrayList<Point>();
		ArrayList<Point> rslt = new ArrayList<Point>();
		Point start = getPoint(x_i,y_i);
		visited.add(start);

		boolean continuing = true;

		while(!visited.isEmpty() && continuing){
			Point current = visited.pop();
			rslt.add(current);
			try {
				voisins = getVoisins(current);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			for(Point p : voisins){
				if(p.getKey() == 9){
					continuing = false;
					bfsFound = true;
					System.out.println("Point final trouvé !");
					rslt.add(p);
					visited.add(p);
					break;
				}
				else if(!visited.contains(p) && !rslt.contains(p)){
					p.setVisited(true);
					visited.add(p);
				}
			}
		}


		/* CALCUL DU TEMPS D'EXECUTION */
		Date dateFin = new Date (System.currentTimeMillis()); //Relever l'heure a la fin du progamme (en milliseconde) 
		Date duree = new Date (System.currentTimeMillis()); //Pour calculer la différence
		duree.setTime (dateFin.getTime () - uDate.getTime ());  //Calcul de la différence
		executionTime = duree.getTime();
		/* CALCUL DU TEMPS D'EXECUTION */

		cheminBFS = getChemin(rslt);
		return cheminBFS;
	}
	/* ALGORITHME DE PARCOURS EN LARGEUR */


	public ArrayList<Point> getVoisins(Point point) throws Exception{
		ArrayList<Point> rslt = new ArrayList<Point>();


		Point tmp1 = point.clone();
		Point tmp2 = point.clone();
		Point currentPointDroit = tourne("droite", tmp1);
		Point currentPointGauche = tourne("gauche", tmp2);
		rslt.add(currentPointDroit);
		rslt.add(currentPointGauche);


		Integer i = 1;
		while(i <= 3){
			Point voisinPoint = null;
			switch(point.getOrientation()){
			case nord:
				if(point.getX()-i >= 0){
					if(isAccessible(point, i, Orientation.nord)){
						voisinPoint = getPoint(point.getX()-i, point.getY());
						if((!voisinPoint.isVisited()) && voisinPoint.getKey() != 1){
							voisinPoint.setOrientation(point.getOrientation());
							voisinPoint.setParent(point);
							rslt.add(voisinPoint);
						}
					}
				}
				break;
			case sud:
				if(point.getX()+i < robotWorld.length){
					if(isAccessible(point, i, Orientation.sud)){
						voisinPoint = getPoint(point.getX()+i, point.getY());
						if((!voisinPoint.isVisited()) && voisinPoint.getKey() != 1){
							voisinPoint.setOrientation(point.getOrientation());
							voisinPoint.setParent(point);
							rslt.add(voisinPoint);
						}
					}
				}
				break;
			case est:
				if(point.getY()+i <  robotWorld[0].length){
					if(isAccessible(point, i, Orientation.est)){
						voisinPoint = getPoint(point.getX(), point.getY()+i);
						if((!voisinPoint.isVisited()) && voisinPoint.getKey() != 1){
							voisinPoint.setOrientation(point.getOrientation());
							voisinPoint.setParent(point);
							rslt.add(voisinPoint);
						}
					}
				}
				break;
			case ouest:
				if(point.getY()-i >= 0){
					if(isAccessible(point, i, Orientation.ouest)){
						voisinPoint = getPoint(point.getX(), point.getY()-i);
						if((!voisinPoint.isVisited()) && voisinPoint.getKey() != 1){
							voisinPoint.setOrientation(point.getOrientation());
							voisinPoint.setParent(point);
							rslt.add(voisinPoint);
						}
					}
				}
				break;
			default:;
			}
			i++;
		}

		for(Point p_tmp : rslt){
			p_tmp.setCost(point.getCost() + 1);
		}

		return rslt;

	}

	public boolean isAccessible(Point p, Integer n, Orientation o) throws Exception{	// n <= 3
		if(n > 3)
			throw new Exception("Erreur, tentative d'acces a une case plus loin que 3");
		else{
			switch(o){
			case nord:
				for(int i = 1; i <= n; i++){
					Integer x1 = p.getX() - i;
					Integer y1 = p.getY();
					if(x1 != null && y1 != null && (x1 >= 0 && x1 < M_RW && y1 >= 0 && y1 < N_RW)){
						if(getPoint(x1,y1).getKey() != 0 && getPoint(x1,y1).getKey() != 5 && getPoint(x1,y1).getKey() != 9){
							return false;
						}
					}
				}
				break;
			case sud:
				for(int i = 1; i <= n; i++){
					Integer x1 = p.getX() + i;
					Integer y1 = p.getY();
					if(x1 != null && y1 != null){
						if(getPoint(x1,y1).getKey() != 0 && getPoint(x1,y1).getKey() != 5 && getPoint(x1,y1).getKey() != 9){
							return false;
						}
					}
				}
				break;
			case est:
				for(int i = 1; i <= n; i++){
					Integer x1 = p.getX();
					Integer y1 = p.getY() + i;
					if(x1 != null && y1 != null){
						if(getPoint(x1,y1).getKey() != 0 && getPoint(x1,y1).getKey() != 5 && getPoint(x1,y1).getKey() != 9){
							return false;
						}
					}
				}
				break;
			case ouest:
				for(int i = 1; i <= n; i++){
					Integer x1 = p.getX();
					Integer y1 = p.getY() - i;
					if(x1 != null && y1 != null){
						if(getPoint(x1,y1).getKey() != 0 && getPoint(x1,y1).getKey() != 5 && getPoint(x1,y1).getKey() != 9){
							return false;
						}
					}
				}
				break;

			}
		}
		return true;
	}

	public void createRobotWorld(){
		for(int x = 0; x < M_RW; x++){
			for(int y = 0; y < N_RW; y++){
				robotWorld[x][y] = new Point(0, false, x, y, null, null, 0);
			}
		}

		for(int i = 0; i < M; i++){
			for(int j = 0; j  < N; j++){
				if(world[i][j].getKey() == 1){
					robotWorld[i][j].setKey(1);
					robotWorld[i+1][j].setKey(1);
					robotWorld[i][j+1].setKey(1);
					robotWorld[i+1][j+1].setKey(1);
				}
			}
		}
	}

	public void setFinalCase(){
		Random random = new Random();
		if(x_f == null && y_f == null){
			x_f = random.nextInt(robotWorld.length);
			y_f = random.nextInt(robotWorld[0].length);
			System.out.println("x_f = " + x_f + " y_f = " + y_f);
			while(robotWorld[x_f][y_f].getKey() == 1){
				x_f = random.nextInt(robotWorld.length);
				y_f = random.nextInt(robotWorld[0].length);
			}
		}
		this.robotWorld[x_f][y_f] = new Point(9, false, x_f, y_f, null, null, 0);
	}
	/* FONCTIONS D'INITIALISATION DES MATRICES */

	/* FONCTION DE RECUPERATION DU CHEMIN TROUVE */
	public LinkedList<Point> getChemin(ArrayList<Point> list){
		LinkedList<Point> rslt = new LinkedList<Point>();
		if(bfsFound == false || list.isEmpty()){
			return rslt;
		}
		else {
			// On sait que le dernier element de la liste passee en parametre contient le point d'arrivee
			Point last = list.get(list.size()-1);
			Point parent = last.getParent();
			if(last != null && parent != null){
				while(parent != null){
					rslt.addFirst(last);
					last = parent;
					parent = last.getParent();
				}
			}
			return rslt;
		}
	}

	public Integer getBFSCost(){
		if(bfsFound) {
			return cheminBFS.getLast().getCost();
		}
		else
			return 0;
	}
	/* FONCTION DE RECUPERATION DU CHEMIN TROUVE */

	/* FONCTION D'ECRITURE EN FICHIER DE SORTIE */

	public String getOutputFormat() {
		Integer cost = getBFSCost();
		if(cost == 0){
			return "-1";
		}
		else {
			return this.printBFSformat();
		}
	}

	public String printBFSformat(){
		String rslt = "";
		if(bfsFound){
			rslt += this.cheminBFS.getLast().getCost().toString();
			for(Point current : this.cheminBFS){
				Point parent = current.getParent();
				if(parent.getX() == current.getX() && parent.getY() == current.getY()){
					switch(parent.getOrientation()){
					case nord:
						if(current.getOrientation() == Orientation.est)
							rslt += " D";
						else if(current.getOrientation() == Orientation.ouest)
							rslt += " G";
						else if(current.getOrientation() == Orientation.sud)
							rslt += " D";
						else
							rslt += " D";
						break;
					case sud:
						if(current.getOrientation() == Orientation.est)
							rslt += " G";
						else if(current.getOrientation() == Orientation.ouest)
							rslt += " D";
						else if(current.getOrientation() == Orientation.sud)
							rslt += " D";
						else
							rslt += " D";
						break;
					case est:
						if(current.getOrientation() == Orientation.nord)
							rslt += " G";
						else if(current.getOrientation() == Orientation.sud)
							rslt += " D";
						else if(current.getOrientation() == Orientation.sud)
							rslt += " D";
						else
							rslt += " D";
						break;
					case ouest:
						if(current.getOrientation() == Orientation.nord)
							rslt += " D";
						else if(current.getOrientation() == Orientation.sud)
							rslt += " G";
						else if(current.getOrientation() == Orientation.sud)
							rslt += " D";
						else
							rslt += " D";
						break;
					default:;
					}
				}
				if(parent.getX() == current.getX()+1 || parent.getX() == current.getX()-1
						|| parent.getY() == current.getY()+1 || parent.getY() == current.getY()-1){
					rslt += " a1";
				}
				if(parent.getX() == current.getX()+2 || parent.getX() == current.getX()-2
						|| parent.getY() == current.getY()+2 || parent.getY() == current.getY()-2){
					rslt += " a2";
				}
				if(parent.getX() == current.getX()+3 || parent.getX() == current.getX()-3
						|| parent.getY() == current.getY()+3 || parent.getY() == current.getY()-3){
					rslt += " a3";
				}
			}
		}
		else rslt = "-1 ";
		rslt += "\n";
		return rslt;
	}

	public String getWorldEntryFile(){
		String rslt = "";
		rslt += this.world.length + " " + this.world[0].length + "\n";
		for(int x = 0; x < this.world.length; x++){
			for(int y = 0; y < this.world[0].length; y++){
				if(this.world[x][y].getKey() != 5 && this.world[x][y].getKey() != 9){
					if(y == this.world[0].length-1)
						rslt += this.world[x][y].getKey() + "\n";
					else
						rslt += this.world[x][y].getKey() + " ";
				}
				else {
					if(y == this.world[0].length-1)
						rslt += "0\n";
					else
						rslt += "0 ";
				}
			}

		}
		rslt += x_i + " " + y_i + " " +  x_f + " " + y_f + " " + getOrientation() + "\n";
		return rslt;
	}

	/* FONCTION D'ECRITURE EN FICHIER DE SORTIE */ 

	public void paintComponent(Graphics g){
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;

		this.setSize(new Dimension(this.getParent().getWidth()-10,this.getParent().getHeight()-10));
		this.setBounds(50, 50,this.getWidth()-50,this.getHeight()-50);

		this.width = (int)((this.getWidth())/world[0].length-1);
		this.height = (int)((this.getHeight())/world.length-1);
		this.width = Math.min(this.width, this.height);
		this.height = this.width;

		for(int x = 0; x < world.length; x++){
			for(int y = 0; y < world[0].length; y++){
				if(this.world[x][y].getKey() == 0){
					g2.setColor(Color.white);
					g2.fillRect(y*width, x*height, width, height);
				}
				else if(this.world[x][y].getKey() == 1){
					g2.setColor(Color.cyan);
					g2.fillRect(y*width, x*height, width, height);
				}
				else {
					g2.setColor(Color.red);
					g2.fillRect(y*width, x*height, width, height);
				}
			}
		}

		g2.setColor(Color.black);

		// LIGNES
		for(int x = 0; x <= world.length; x++){
			g2.drawLine(0, x*height, world[0].length*width, x*height);
		}
		// COLONNES
		for(int y = 0; y <= world[0].length; y++){
			g2.drawLine(y*width, 0, y*width, world.length*height);
		}

		for(Point p : cheminBFS){
			g2.setColor(Color.green);
			g2.drawLine(p.getY()*width, p.getX()*height, p.getParent().getY()*width, p.getParent().getX()*height);
		}

		// Point de depart du robot:
		g2.setColor(Color.blue);
		g2.fillOval(y_i*width-(width/8), x_i*height-(height/8), width/4, height/4);

		// Orientation du robot:
		g2.setStroke(new BasicStroke(2));

		// Fleche
		switch(this.getOrientation()){
		case nord:
			drawArrow(g2, y_i*width, x_i*height, y_i*width, x_i*height-(height/2)-arrowSize);
			break;
		case sud:
			drawArrow(g2, y_i*width, x_i*height, y_i*width, x_i*height+(height/2)+arrowSize);
			break;
		case est:
			drawArrow(g2, y_i*width, x_i*height, y_i*width+(width/2)+arrowSize, x_i*height);
			break;
		case ouest:
			drawArrow(g2, y_i*width, x_i*height, y_i*width-(width/2)-arrowSize, x_i*height);
			break;
		default:;
		}

		// Point d'arrivee du robot:
		g2.setColor(Color.red);
		g2.fillOval(y_f*width-(width/8), x_f*height-(height/8), width/4, height/4);


	}

	private final int ARR_SIZE = 8;
	void drawArrow(Graphics g1, int x1, int y1, int x2, int y2) {
		Graphics2D g = (Graphics2D) g1.create();

		double dx = x2 - x1, dy = y2 - y1;
		double angle = Math.atan2(dy, dx);
		int len = (int) Math.sqrt(dx*dx + dy*dy);
		AffineTransform at = AffineTransform.getTranslateInstance(x1, y1);
		at.concatenate(AffineTransform.getRotateInstance(angle));
		g.transform(at);

		// Draw horizontal arrow starting in (0, 0)
		g.drawLine(0, 0, len, 0);
		g.fillPolygon(new int[] {len, len-ARR_SIZE, len-ARR_SIZE, len},
				new int[] {0, -ARR_SIZE, ARR_SIZE, 0}, 4);
	}


}
