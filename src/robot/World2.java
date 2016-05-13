package robot;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;

import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class World2 extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	public World2(String chaine){
		super();
		this.setSize(getMaximumSize());
		this.setVisible(true);
		this.setBorder(new EmptyBorder(300, 300, 300, 300));

		readDataFromString(chaine);
		createRobotWorld();
		placeRobot();
		setFinalCase();

		this.width = (int)(this.getWidth()%N*(0.4*N));
		this.height = (int)(this.getHeight()%M*(0.4*M));


		System.out.println("this.getWidth() = " + this.getWidth());
		System.out.println("this.getHeight() = " + this.getHeight());
		System.out.println("w = " + this.width);
		System.out.println("h = " + this.height);

		System.out.println(" ------------------------------- ");
		showMatrice(robotWorld);
		System.out.println(" ------------------------------- ");


	}

	public World2(Integer[] tab){
		super();
		this.setSize(600,600);
		this.setVisible(true);
		this.setBorder(new EmptyBorder(300, 300, 300, 300));

		readDataFromIntegerTab(tab);
		createRobotWorld();
		placeRobot();
		setFinalCase();

		this.width = this.getWidth()%N;
		this.height = this.getHeight()%M;

		System.out.println("w = " + this.width);
		System.out.println("h = " + this.height);

		System.out.println(" ------------------------------- ");
		showMatrice(robotWorld);
		System.out.println(" ------------------------------- ");
	}

	// M = LIGNE, N = COLONNE
	public static Integer M;
	public static Integer N;
	private static Integer M_RW;
	private static Integer N_RW;
	private Point[][][] world;
	private Point[][][] robotWorld;
	private Integer[] x_i, y_i;
	private Integer[] x_f, y_f;
	private Integer[] current_x, current_y;
	private Orientation[] orientation;
	public final static String[] direction = {"droite","gauche"};

	public final Integer arrowSize = 20;
	public Integer width;
	public Integer height;
	public static int nbWorld = 0;

	public ArrayList<World2> worlds = new ArrayList<World2>();

	public Point getPoint(int n, int i, int j){ return this.robotWorld[n][i][j]; }
	public Point[][] getWorld(int i){ return this.world[i]; }
	public Point[][] getRobotWorld(int i){ return this.robotWorld[i]; }
	public Integer getX_i(int n){ return this.x_i[n]; }
	public Integer getY_i(int n){ return this.y_i[n]; }
	public Integer getCurrentX(int n){ return this.current_x[n]; }
	public Integer getCurrentY(int n){ return this.current_y[n]; }
	public Orientation getOrientation(int n){ return this.orientation[n]; }


	public void showMatrice(Point[][][] matrice){
		System.out.println("Matrice de " + matrice.length + " lignes, " + matrice[0].length + " colonnes");		
		System.out.println(" ----- WORLD -----");
		for(int i = 0; i < matrice.length; i++){
			for(int j = 0; j < matrice[0].length; j++){
				System.out.print(matrice[nbWorld][i][j].getKey() + " ");
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
		current_x = x_i;
		current_y = y_i;
		this.robotWorld[nbWorld][x_i[nbWorld]][y_i[nbWorld]].setKey(5);
		this.robotWorld[nbWorld][x_i[nbWorld]][y_i[nbWorld]].setVisited(true);
		this.robotWorld[nbWorld][x_i[nbWorld]][y_i[nbWorld]].setOrientation(orientation[nbWorld]);
	}
	/*
	// le robot avance de n cases selon l'orientation definie
	public Integer avance(Integer n){
		Integer possible_x = current_x, possible_y = current_y;
		Integer rslt = 3;
		this.robotWorld[current_x][current_y].setKey(0);
		switch(this.robotWorld[current_x][current_y].getOrientation()){
		case est:
			possible_y += n;
			break;
		case nord:
			possible_x -= n;
			break;
		case ouest:
			possible_y -= n;
			break;
		case sud:
			possible_x += n;
			break;
		default:
			break;
		}
		if(this.robotWorld[possible_x][possible_y].getKey() == 0 || this.robotWorld[possible_x][possible_y].getKey() == 9){
			rslt = this.robotWorld[possible_x][possible_y].getKey();
			current_x = possible_x;
			current_y = possible_y;
			this.robotWorld[current_x][current_y].setKey(5);
			this.robotWorld[current_x][current_y].setOrientation(orientation);
			return rslt;
		}
		else {
			this.robotWorld[current_x][current_y].setKey(5);
			this.robotWorld[current_x][current_y].setOrientation(orientation);
			rslt = this.robotWorld[possible_x][possible_y].getKey();
			return rslt;
		}
	}
	 */

	public Point tourne(String o, Point p){
		if(o.equalsIgnoreCase("droite")){
			switch(p.getOrientation()){
			case est:
				p.setOrientation(Orientation.sud);
				break;
			case nord:
				p.setOrientation(Orientation.est);
				break;
			case ouest:
				p.setOrientation(Orientation.nord);
				break;
			case sud:
				p.setOrientation(Orientation.ouest);
				break;
			default:
				break;
			}
		}
		else if(o.equalsIgnoreCase("gauche")){
			switch(p.getOrientation()){
			case est:
				p.setOrientation(Orientation.nord);
				break;
			case nord:
				p.setOrientation(Orientation.ouest);
				break;
			case ouest:
				p.setOrientation(Orientation.sud);
				break;
			case sud:
				p.setOrientation(Orientation.est);
				break;
			default:
				break;

			}
		}
		else {
			// On ne fait rien d'autre que tourner a droite ou a gauche
		}
		p.setCost(p.getCost()+1);
		return p;
	}
	/* FONCTIONS DE MOUVEMENT DU ROBOT */


	/* ALGORITHME DE PARCOURS EN LARGEUR */

	public void bfs(){
		Date uDate = new Date (System.currentTimeMillis ()); //Relever l'heure avant le debut du progamme (en milliseconde) 

		System.out.println(" ===== BFS =====");
		LinkedList<Point> rslt = new LinkedList<Point>();
		ArrayList<Point> voisins = new ArrayList<Point>();
		ArrayList<Point> visitedPoint = new ArrayList<Point>();
		rslt.add(getPoint(nbWorld, x_i[nbWorld], y_i[nbWorld]));

		boolean continuing = true;

		while(!rslt.isEmpty() && continuing){
			Point current = rslt.pop();
			visitedPoint.add(current);

			try {
				voisins = getVoisins(current);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			for(Point p : voisins){
				if(!rslt.contains(p) && !visitedPoint.contains(p)){
					rslt.add(p);
				}
				else{
				}
				if(p.getKey() == 9){
					continuing = false;
					System.out.println("Point final trouvé !");
					visitedPoint.add(p);
				}
			}
		}

		System.out.println(" ===== BFS =====");

		/* CALCUL DU TEMPS D'EXECUTION */
		Date dateFin = new Date (System.currentTimeMillis()); //Relever l'heure a la fin du progamme (en milliseconde) 
		Date duree = new Date (System.currentTimeMillis()); //Pour calculer la différence
		duree.setTime (dateFin.getTime () - uDate.getTime ());  //Calcul de la différence
		long secondes = duree.getTime () / 1000;
		long min = secondes / 60;
		long heures = min / 60;
		long mili = duree.getTime () % 1000;
		secondes %= 60;
		System.out.println ("Temps passé durant le traitement : \nHeures : " + heures + "\nMinutes : " + min + "\nSecondes : " + secondes + "\nMilisecondes : " + mili + "\n");
		/* CALCUL DU TEMPS D'EXECUTION */

		System.out.println(" LISTE FINALE ");
		for(Point p : visitedPoint){
			System.out.println(p.toString());
		}

		LinkedList<Point> chemin = getChemin(visitedPoint);
	}
	/* ALGORITHME DE PARCOURS EN LARGEUR */


	public ArrayList<Point> getVoisins(Point p) throws Exception{
		ArrayList<Point> rslt = new ArrayList<Point>();
		Integer i = 1;
		while(i <= 3){
			Point voisinPoint = null;
			switch(p.getOrientation()){
			case nord:
				if(p.getX()-i >= 0)
					if(isAccessible(p, i, Orientation.nord)){
						voisinPoint = getPoint(nbWorld, p.getX()-i, p.getY());
						voisinPoint.setOrientation(p.getOrientation());
						if((!voisinPoint.isVisited()) && voisinPoint.getKey() != 1){
							rslt.add(voisinPoint);
							voisinPoint.setVisited(true);
						}
					}
				break;
			case sud:
				if(p.getX()+i < M_RW)
					if(isAccessible(p, i, Orientation.sud)){
						voisinPoint = getPoint(nbWorld, p.getX()+i, p.getY());
						voisinPoint.setOrientation(p.getOrientation());
						if((!voisinPoint.isVisited()) && voisinPoint.getKey() != 1){
							rslt.add(voisinPoint);
							voisinPoint.setVisited(true);
						}
					}
				break;
			case est:
				if(p.getY()+i < N_RW)
					if(isAccessible(p, i, Orientation.est)){
						voisinPoint = getPoint(nbWorld, p.getX(), p.getY()+i);
						voisinPoint.setOrientation(p.getOrientation());
						if((!voisinPoint.isVisited()) && voisinPoint.getKey() != 1){
							rslt.add(voisinPoint);
							voisinPoint.setVisited(true);
						}
					}
				break;
			case ouest:
				if(p.getY()-i >= 0)
					if(isAccessible(p, i, Orientation.ouest)){
						voisinPoint = getPoint(nbWorld, p.getX(), p.getY()-i);
						voisinPoint.setOrientation(p.getOrientation());
						if((!voisinPoint.isVisited()) && voisinPoint.getKey() != 1){
							rslt.add(voisinPoint);
							voisinPoint.setVisited(true);
						}
					}
				break;
			default:;
			}
			i++;
		}

		Point tmp_1 = p.clone();
		Point tmp_2 = p.clone();

		Point currentPointDroit = tourne("droite", tmp_1);
		Point currentPointGauche = tourne("gauche", tmp_2);
		tmp_1.setVisited(true);
		tmp_2.setVisited(true);
		rslt.add(currentPointDroit);
		rslt.add(currentPointGauche);

		for(Point p_tmp : rslt){
			p_tmp.setParent(p);
			p_tmp.setCost(p.getCost() + 1);
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
					if(getPoint(nbWorld, x1,y1).getKey() != 0 && getPoint(nbWorld, x1,y1).getKey() != 5 && getPoint(nbWorld, x1,y1).getKey() != 9){
						return false;
					}
				}
				break;
			case sud:
				for(int i = 1; i <= n; i++){
					Integer x1 = p.getX() + i;
					Integer y1 = p.getY();
					if(getPoint(nbWorld, x1,y1).getKey() != 0 && getPoint(nbWorld, x1,y1).getKey() != 5 && getPoint(nbWorld, x1,y1).getKey() != 9){
						return false;
					}
				}
				break;
			case est:
				for(int i = 1; i <= n; i++){
					Integer x1 = p.getX();
					Integer y1 = p.getY() + i;
					if(getPoint(nbWorld, x1,y1).getKey() != 0 && getPoint(nbWorld, x1,y1).getKey() != 5 && getPoint(nbWorld, x1,y1).getKey() != 9){
						return false;
					}
				}
				break;
			case ouest:
				for(int i = 1; i <= n; i++){
					Integer x1 = p.getX();
					Integer y1 = p.getY() - i;
					if(getPoint(nbWorld, x1,y1).getKey() != 0 && getPoint(nbWorld, x1,y1).getKey() != 5 && getPoint(nbWorld, x1,y1).getKey() != 9){
						return false;
					}
				}
				break;

			}
		}
		return true;
	}



	/* FONCTIONS D'INITIALISATION DES MATRICES */
	public void readDataFromString(String chaine){
		int cpt = 0;
		int ok = -1;
		while(!chaine.isEmpty()){
			Integer ind = chaine.indexOf(" ");
			Integer endLine = chaine.indexOf("\n");
			System.out.println("[nbWorld] = " + nbWorld);

			// On definit le nombre d'instance de World a creer
			if(ok < 0){
				int tmpM = 0;
				if(cpt == 0)
					tmpM = Integer.parseInt(chaine.substring(0, ind));
				else if(cpt <= tmpM+1)
					cpt++;
				chaine = chaine.substring(endLine+1, chaine.length());

				// Si on atteint la fin d'un bloc
				if(cpt == tmpM+2){
					endLine = chaine.indexOf("\n");
					Integer i_space = chaine.indexOf(" ");
					if(chaine.substring(0, i_space).equals("0") && chaine.substring(i_space+1,endLine).equals("0"))
						ok = 1;
					else {
						nbWorld++;
						cpt = 0;
						System.out.println("fin de la premiere instance de monde");
					}
				}
			}
			else {

				// On stocke le nombre de ligne et de colonne de la matrice
				if(cpt == 0){
					M = Integer.parseInt(chaine.substring(0, ind));
					N = Integer.parseInt(chaine.substring(ind+1, endLine));
					M_RW = M+1;
					N_RW = N+1;
					world = new Point[nbWorld][M][N];
					robotWorld = new Point[nbWorld][M_RW][N_RW];
				}

				// On stocke la matrice
				else if(cpt <= M){
					Integer i_start = 0;
					Integer i_space = chaine.indexOf(" ");
					if(i_space < endLine){
						for(int colonne = 0; colonne < N; colonne++){
							world[nbWorld][cpt-1][colonne] = new Point(Integer.parseInt(chaine.substring(i_start, i_space)), false, cpt-1, colonne, null, null, 0);
							i_start = i_space+1;
							i_space = chaine.indexOf(" ", i_space+1);
							if(i_space > endLine)
								i_space = i_space-2;
						}
					}
				}

				// On stocke le point de départ et le point d'arrivée
				else if(cpt == M+1){
					endLine = chaine.indexOf("\n");
					Integer i_space = chaine.indexOf(" ");
					x_i[nbWorld] = Integer.parseInt(chaine.substring(0, i_space));
					y_i[nbWorld] = Integer.parseInt(chaine.substring(++i_space, chaine.indexOf(" ", ++i_space)));
					x_f[nbWorld] = Integer.parseInt(chaine.substring(++i_space, chaine.indexOf(" ", ++i_space)));
					y_f[nbWorld] = Integer.parseInt(chaine.substring(++i_space, chaine.indexOf(" ", ++i_space)));

					switch(chaine.substring(i_space+1, endLine)){
					case "nord":
						this.orientation[nbWorld] = Orientation.nord;
						break;
					case "sud":
						this.orientation[nbWorld] = Orientation.sud;
						break;
					case "est":
						this.orientation[nbWorld] = Orientation.est;
						break;
					case "ouest":
						this.orientation[nbWorld] = Orientation.ouest;
					default:
						break;
					}
				}

				// Permet de soustraire la ligne courante à la chaine pour passer a la ligne suivante
				chaine = chaine.substring(endLine+1, chaine.length());
				cpt++;

				// Si on atteint la fin d'un bloc
				if(cpt == M+2){
					endLine = chaine.indexOf("\n");
					Integer i_space = chaine.indexOf(" ");
					if(chaine.substring(0, i_space).equals("0") && chaine.substring(i_space+1,endLine).equals("0")){
						System.out.println("0 0 ATTEINT");
					}
					else {
						cpt = 0;
						nbWorld++;
						System.out.println("fin de la premiere instance de monde");
						worlds.add(this);
					}
				}
			}
		}
		showMatrice(world);
	}

	public void readDataFromIntegerTab(Integer[] tab){
		M = tab[0];
		N = tab[1];
		M_RW = M+1;
		N_RW = N+1;
		Integer nbObstacles = tab[2];
		x_i[nbWorld] = tab[3];
		y_i[nbWorld] = tab[4];
		x_f[nbWorld] = tab[5];
		y_f[nbWorld] = tab[6];
		orientation[nbWorld] = Orientation.values()[tab[7]];

		world = new Point[10][M][N];
		robotWorld = new Point[10][M_RW][N_RW];

		for(int x = 0; x < M; x++){
			for(int y = 0; y < N; y++){
				this.world[nbWorld][x][y] = new Point(0, false, x, y, null, null, 0);
			}
		}
		for(int i = 8; i < nbObstacles*2 + 8; i = i+2){
			this.world[nbWorld][tab[i]][tab[i+1]].setKey(1);
		}
		showMatrice(world);

	}

	public void createRobotWorld(){
		for(int x = 0; x < M_RW; x++){
			for(int y = 0; y < N_RW; y++){
				robotWorld[nbWorld][x][y] = new Point(0, false, x, y, null, null, 0);
			}
		}

		for(int i = 0; i < M; i++){
			for(int j = 0; j  < N; j++){
				if(world[nbWorld][i][j].getKey() == 1){
					robotWorld[nbWorld][i][j].setKey(1);
					robotWorld[nbWorld][i+1][j].setKey(1);
					robotWorld[nbWorld][i][j+1].setKey(1);
					robotWorld[nbWorld][i+1][j+1].setKey(1);
				}
			}
		}
	}

	public void setFinalCase(){
		this.robotWorld[nbWorld][x_f[nbWorld]][y_f[nbWorld]].setKey(9);
	}
	/* FONCTIONS D'INITIALISATION DES MATRICES */

	/* FONCTION DE RECUPERATION DU CHEMIN TROUVE */
	public LinkedList<Point> getChemin(ArrayList<Point> list){
		System.out.println("\n ############ CHEMIN ############");
		LinkedList<Point> rslt = new LinkedList<Point>();
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
		System.out.println("chemin = ");
		for(Point p : rslt){
			System.out.println("\t" + p.toString());
		}
		System.out.println("\n ################################");
		return rslt;
	}
	/* FONCTION DE RECUPERATION DU CHEMIN TROUVE */







	public void paintComponent(Graphics g){
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;


		for(int x = 0; x < M; x++){
			for(int y = 0; y < N; y++){
				if(this.world[nbWorld][x][y].getKey() == 0){
					g2.setColor(Color.white);
					g2.fillRect(y*width, x*height, width, height);
				}
				else if(this.world[nbWorld][x][y].getKey() == 1){
					g2.setColor(Color.cyan);
					g2.fillRect(y*width, x*height, width, height);
				}
			}
		}

		g2.setColor(Color.black);

		// LIGNES
		for(int x = 0; x <= M; x++){
			g2.drawLine(0, x*height, N*width, x*height);
		}
		// COLONNES
		for(int y = 0; y <= N; y++){
			g2.drawLine(y*width, 0, y*width, M*height);
		}

		// Point de depart du robot:
		g2.setColor(Color.black);
		g2.fillOval(y_i[nbWorld]*width-(width/8), x_i[nbWorld]*height-(height/8), width/4, height/4);

		// Orientation du robot:
		g2.setStroke(new BasicStroke(2));

		// Fleche
		switch(this.getOrientation(nbWorld)){
		case nord:
			drawArrow(g2,y_i[nbWorld]*width, x_i[nbWorld]*height, y_i[nbWorld]*width-(width/2), x_i[nbWorld]*height-(height/2)-arrowSize);
			//g2.drawLine(y_i[nbWorld]*width, x_i[nbWorld]*height, y_i[nbWorld]*width-(width/2), x_i[nbWorld]*height-(height/2)-arrowSize);
			break;
		case sud:
			drawArrow(g2,y_i[nbWorld]*width, x_i[nbWorld]*height, y_i[nbWorld]*width, x_i[nbWorld]*height+arrowSize);
			g2.drawLine(y_i[nbWorld]*width, x_i[nbWorld]*height, y_i[nbWorld]*width, x_i[nbWorld]*height+arrowSize);
			break;
		case est:
			g2.drawLine(y_i[nbWorld]*width-(width/2), x_i[nbWorld]*height-(height/2), y_i[nbWorld]*width-(width/2)+arrowSize, x_i[nbWorld]*height-(height/2)+arrowSize);
			break;
		case ouest:
			g2.drawLine(y_i[nbWorld]*width-(width/2), x_i[nbWorld]*height-(height/2), y_i[nbWorld]*width-(width/2)-arrowSize, x_i[nbWorld]*height-(height/2)+arrowSize);
			break;
		default:;
		}

		// Point d'arrivee du robot:
		g2.fillOval(y_f[nbWorld]*width-(width/8), x_f[nbWorld]*height-(height/8), width/4, height/4);

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
