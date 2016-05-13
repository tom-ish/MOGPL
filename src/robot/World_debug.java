package robot;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;

public class World_debug {

	public World_debug(String chaine){
		readDataFromString(chaine);
		createRobotWorld();
		placeRobot();
		setFinalCase();

		System.out.println(" ------------------------------- ");
		showMatrice(robotWorld);
		System.out.println(" ------------------------------- ");
	}

	public World_debug(Integer[] tab){
		readDataFromIntegerTab(tab);
		createRobotWorld();
		placeRobot();
		setFinalCase();

		System.out.println(" ------------------------------- ");
		showMatrice(robotWorld);
		System.out.println(" ------------------------------- ");
	}

	// M = LIGNE, N = COLONNE
	private static Integer M;
	private static Integer N;
	private static Integer M_RW;
	private static Integer N_RW;
	private Point[][] world;
	private Point[][] robotWorld;
	private Integer x_i, y_i;
	private Integer x_f, y_f;
	private Integer current_x, current_y;
	private Orientation orientation;
	public final static String[] direction = {"droite","gauche"};


	public Point getPoint(int i, int j){ return this.robotWorld[i][j]; }
	public Point[][] getWorld(){ return this.world; }
	public Point[][] getRobotWorld(){ return this.robotWorld; }
	public Integer getX_i(){ return this.x_i; }
	public Integer getY_i(){ return this.y_i; }
	public Integer getCurrentX(){ return this.current_x; }
	public Integer getCurrentY(){ return this.current_y; }
	public Orientation getOrientation(){ return this.orientation; }


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
		current_x = x_i;
		current_y = y_i;
		this.robotWorld[x_i][y_i].setKey(5);
		this.robotWorld[x_i][y_i].setVisited(true);
		this.robotWorld[x_i][y_i].setOrientation(orientation);
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
		rslt.add(getPoint(x_i, y_i));

		int iteration = 0;
		boolean continuing = true;

		while(!rslt.isEmpty() && continuing){
			Point current = rslt.pop();
			visitedPoint.add(current);

			/* UNIQUEMENT POUR L'AFFICHAGE */
			System.out.println("\n ****************************** ITERATION N°" + iteration + " ******************************");
			System.out.println("\n ---------- Etat de la liste : -----------");
			for(Point p : rslt){
				System.out.println(" ------ " + p.toString() +" ------");
			}
			System.out.println("\n1er element de la liste: " + current.toString());
			/* UNIQUEMENT POUR L'AFFICHAGE */

			try {
				voisins = getVoisins(current);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			int nbAjouts = 0;
			for(Point p : voisins){
				if(!rslt.contains(p) && !visitedPoint.contains(p)){
					rslt.add(p);
					nbAjouts++;
					System.out.println(" |___ " + p.toString() + " ajouté !");
				}
				else{
					System.out.println(" |___ " + p.toString() + " non ajouté : deja visité !");
				}
				if(p.getKey() == 9){
					continuing = false;
					System.out.println("Point final trouvé !");
					visitedPoint.add(p);
				}
			}
			System.out.println(nbAjouts + " elements ajoutés\n");
			iteration++;
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
		System.out.println("\n        Recherche dans le voisinage de p = " + p.toString());
		while(i <= 3){
			System.out.println(" - à une distance de " + i + " par rapport a la position de p");
			Point voisinPoint = null;
			switch(p.getOrientation()){
			case nord:
				if(p.getX()-i >= 0)
					if(isAccessible(p, i, Orientation.nord)){
						voisinPoint = getPoint(p.getX()-i, p.getY());
						voisinPoint.setOrientation(p.getOrientation());
						System.out.println("voisinPoint : " + voisinPoint.toString());
						if((!voisinPoint.isVisited()) && voisinPoint.getKey() != 1){
							rslt.add(voisinPoint);
							voisinPoint.setVisited(true);
						}
					}
				break;
			case sud:
				if(p.getX()+i < M_RW)
					if(isAccessible(p, i, Orientation.sud)){
						voisinPoint = getPoint(p.getX()+i, p.getY());
						voisinPoint.setOrientation(p.getOrientation());
						System.out.println("voisinPoint : " + voisinPoint.toString());
						if((!voisinPoint.isVisited()) && voisinPoint.getKey() != 1){
							rslt.add(voisinPoint);
							voisinPoint.setVisited(true);
						}
					}
				break;
			case est:
				if(p.getY()+i < N_RW)
					if(isAccessible(p, i, Orientation.est)){
						voisinPoint = getPoint(p.getX(), p.getY()+i);
						voisinPoint.setOrientation(p.getOrientation());
						System.out.println("voisinPoint : " + voisinPoint.toString());
						if((!voisinPoint.isVisited()) && voisinPoint.getKey() != 1){
							rslt.add(voisinPoint);
							voisinPoint.setVisited(true);
						}
					}
				break;
			case ouest:
				if(p.getY()-i >= 0)
					if(isAccessible(p, i, Orientation.ouest)){
						voisinPoint = getPoint(p.getX(), p.getY()-i);
						voisinPoint.setOrientation(p.getOrientation());
						System.out.println("voisinPoint : " + voisinPoint.toString());
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

		System.out.println("        Recherche a droite/a gauche de la position de p");

		Point tmp_1 = p.clone();
		Point tmp_2 = p.clone();

		Point currentPointDroit = tourne("droite", tmp_1);
		System.out.println(" ---> currentPointDroit : " + currentPointDroit.toString());
		Point currentPointGauche = tourne("gauche", tmp_2);
		tmp_1.setVisited(true);
		tmp_2.setVisited(true);
		System.out.println(" ---> currentPointGauche : " + currentPointGauche.toString());
		rslt.add(currentPointDroit);
		rslt.add(currentPointGauche);

		System.out.println(" ++++++++ liste des voisins de " + p.toString() + " ++++++++"); 
		int a = 1;
		for(Point p_tmp : rslt){
			p_tmp.setParent(p);
			p_tmp.setCost(p.getCost() + 1);
			System.out.println("   -> voisin n°" + a + " : " + p_tmp.toString());
			a++;
		}
		System.out.println(" ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++\n");

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
					if(getPoint(x1,y1).getKey() != 0 && getPoint(x1,y1).getKey() != 5 && getPoint(x1,y1).getKey() != 9){
						return false;
					}
					System.out.println(getPoint(x1,y1).toString() + " accessible !");
				}
				break;
			case sud:
				for(int i = 1; i <= n; i++){
					Integer x1 = p.getX() + i;
					Integer y1 = p.getY();
					if(getPoint(x1,y1).getKey() != 0 && getPoint(x1,y1).getKey() != 5 && getPoint(x1,y1).getKey() != 9){
						System.out.println("false");
						return false;
					}
					System.out.println(getPoint(x1,y1).toString() + " accessible !");
				}
				break;
			case est:
				for(int i = 1; i <= n; i++){
					Integer x1 = p.getX();
					Integer y1 = p.getY() + i;
					if(getPoint(x1,y1).getKey() != 0 && getPoint(x1,y1).getKey() != 5 && getPoint(x1,y1).getKey() != 9){
						return false;
					}
					System.out.println(getPoint(x1,y1).toString() + " accessible !");
				}
				break;
			case ouest:
				for(int i = 1; i <= n; i++){
					Integer x1 = p.getX();
					Integer y1 = p.getY() - i;
					if(getPoint(x1,y1).getKey() != 0 && getPoint(x1,y1).getKey() != 5 && getPoint(x1,y1).getKey() != 9){
						return false;
					}
					System.out.println(getPoint(x1,y1).toString() + " accessible !");
				}
				break;

			}
		}
		return true;
	}



	/* FONCTIONS D'INITIALISATION DES MATRICES */
	public void readDataFromString(String chaine){
		int cpt = 0;
		while(!chaine.isEmpty()){
			Integer ind = chaine.indexOf(" ");
			Integer endLine = chaine.indexOf("\n");
			
			// On stocke le nombre de ligne et de colonne de la matrice
			if(cpt == 0){
				M = Integer.parseInt(chaine.substring(0, ind));
				N = Integer.parseInt(chaine.substring(ind+1, endLine));
				M_RW = M+1;
				N_RW = N+1;
				world = new Point[M][N];
				robotWorld = new Point[M_RW][N_RW];
			}
			
			// Si on est a la fin d'un bloc mais pas a la fin du fichier
			else if(cpt == M+2){
				if(!chaine.substring(0, endLine).equalsIgnoreCase("0 0")){
					cpt = 0;
				}
			}
			
			// On stocke la matrice
			else if(cpt <= M){
				Integer i_start = 0;
				Integer i_space = chaine.indexOf(" ");
				if(i_space < endLine){
					for(int colonne = 0; colonne < N; colonne++){
						world[cpt-1][colonne] = new Point(Integer.parseInt(chaine.substring(i_start, i_space)), false, cpt-1, colonne, null, null, 0);
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
				x_i = Integer.parseInt(chaine.substring(0, i_space));
				y_i = Integer.parseInt(chaine.substring(++i_space, chaine.indexOf(" ", ++i_space)));
				x_f = Integer.parseInt(chaine.substring(++i_space, chaine.indexOf(" ", ++i_space)));
				y_f = Integer.parseInt(chaine.substring(++i_space, chaine.indexOf(" ", ++i_space)));

				switch(chaine.substring(i_space+1, endLine)){
				case "nord":
					this.orientation = Orientation.nord;
					break;
				case "sud":
					this.orientation = Orientation.sud;
					break;
				case "est":
					this.orientation = Orientation.est;
					break;
				case "ouest":
					this.orientation = Orientation.ouest;
				default:
					break;
				}
			}

			// Permet de soustraire la ligne courante à la chaine pour passer a la ligne suivante
			chaine = chaine.substring(endLine+1, chaine.length());
			cpt++;
		}

		showMatrice(world);
	}

	public void readDataFromIntegerTab(Integer[] tab){
		M = tab[0];
		N = tab[1];
		M_RW = M+1;
		N_RW = N+1;
		Integer nbObstacles = tab[2];
		x_i = tab[3];
		y_i = tab[4];
		x_f = tab[5];
		y_f = tab[6];
		orientation = Orientation.values()[tab[7]];

		world = new Point[M][N];
		robotWorld = new Point[M_RW][N_RW];

		for(int x = 0; x < M; x++){
			for(int y = 0; y < N; y++){
				this.world[x][y] = new Point(0, false, x, y, null, null, 0);
			}
		}
		for(int i = 9; i < nbObstacles*2 + 9; i = i+2){
			this.world[tab[i]][tab[i+1]].setKey(1);
		}
		showMatrice(world);

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
		this.robotWorld[x_f][y_f].setKey(9);
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
}
