package robot;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;
import java.util.Scanner;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.border.EtchedBorder;

public class FrameWorld extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	static boolean chooseWorld = false;
	static boolean random = false;
	static boolean randomObstacle = false;

	static ArrayList<World> worlds = new ArrayList<World>();
	static Integer M;
	static Integer N;

	static Integer M_RW;
	static Integer N_RW;

	static Point[][] world = null;
	static Point[][] robotWorld = null;
	static Integer x_i, y_i, x_f, y_f;
	static Orientation orientation = null;

	static int nbMonde = 0;
	static int nbInstance = 10;
	
	private String[] worldStr;

	public FrameWorld(){
		super("Frame of the Robot World");
		setSize(1100,750);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);

		runAs();
		
		
		

		setWorldPanel2();
		setVisible(true);
	}
	
	private void runAs(){
		if(random){
			for(int i = 10; i <= 100; i+=10){
				try {
					generateRandomWorld(i, i);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			writeWorldEntryFile();
		}
		else {
			if(randomObstacle){
				for(int i = 10; i <= 50; i+=10){
					try {
						generateRandomObstaclesWorld(i);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			else if(!chooseWorld)
				initWorld();
			else {
				try {
					readDataFromIntegerTab(initUserWorld());
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	public void initWorld(){
		String strWorld = readFile(new File("//Users/Tomohiro/Documents/M1/Project/MOGPL/src/robot/instance.txt"));
		readDataFromString(strWorld);

	}

	private void setSettingsPanel(){
		JPanel settings = new JPanel();
		JPanel pointSettings = new JPanel(new SpringLayout());

		int SPHeight = (int) (Math.round(this.getHeight() * 0.40));
		settings.setPreferredSize(new Dimension(this.getWidth(), SPHeight));

		String[] labels = {"Nombre de lignes: ", "Nombre de colonnes: ", "Nombre d'obstacles: ", "Initial X: ", "Initial Y: ", "Final X: ", "Final Y: "};
		int numPairs = labels.length;

		for (int i = 0; i < numPairs; i++) {
			JLabel l = new JLabel(labels[i], JLabel.TRAILING);
			pointSettings.add(l);
			JTextField textField = new JTextField(1);
			l.setLabelFor(textField);
			pointSettings.add(textField);
		}

		//Lay out the panel.
		SpringUtilities.makeCompactGrid(pointSettings,
				numPairs, 2, //rows, cols
				6, 6,        //initX, initY
				6, 6);       //xPad, yPad

		settings.setLayout(new GridLayout(0,1,3,3));
		settings.add(pointSettings);
		settings.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
		this.add(settings, BorderLayout.PAGE_START);

	}

	private void setWorldPanel(){
		JPanel worldPanel = new JPanel(new BorderLayout());
		worldPanel.setPreferredSize(new Dimension(this.getWidth(), this.getHeight()));
		
		worldStr = new String[worlds.size()];
		for(int i = 0; i < worlds.size(); i++){
			worldStr[i] = "world n° " + i;
		}

		final JLayeredPane worldLayered = new JLayeredPane();
		
		for(int i = worlds.size()-1; i >= 0; i--)
			worldLayered.add(worlds.get(i), new Integer(i));
		
		
		final JComboBox<String> worldList = new JComboBox<String>(worldStr);
		worldList.setPreferredSize(new Dimension(100,20));
		/*
		worldList.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				@SuppressWarnings("unchecked")
				JComboBox<String> combo = (JComboBox<String>) e.getSource();
				Integer currentInt = (Integer) combo.getSelectedIndex();
				System.out.println(currentInt);
				worldLayered.moveToFront(worlds.get(currentInt));
			}
		});*/


		worldLayered.setOpaque(true);


		worldPanel.add(worldList,BorderLayout.PAGE_START);
		worldPanel.add(worldLayered, BorderLayout.CENTER);
		this.setContentPane(worldPanel);

	}

	private void setWorldPanel2(){
		final JPanel worldPanel = new JPanel(new BorderLayout());
		worldPanel.setPreferredSize(new Dimension(this.getWidth(), this.getHeight()));
		System.out.println(worldPanel.getWidth());

		String[] worldString = new String[worlds.size()];
		for(int i = 0; i < worlds.size(); i++)
			worldString[i] = "world n°" + i;
		
		JComboBox<String> worldList = new JComboBox<String>(worldString);
		worldList.setPreferredSize(new Dimension(100,20));
		


		worldPanel.add(worlds.get(0), BorderLayout.CENTER);
		worldPanel.add(worldList,BorderLayout.PAGE_START);
		this.getContentPane().add(worldPanel);
	}



	/* FONCTIONS D'INITIALISATION DES MATRICES */
	public static String readFile(File file){
		String chaine = "";

		// Lecture du fichier texte
		try{
			InputStream ips = new FileInputStream(file);
			InputStreamReader ipsr = new InputStreamReader(ips);
			BufferedReader br = new BufferedReader(ipsr);
			String ligne;
			while((ligne = br.readLine()) != null){
				chaine += ligne + "\n";
			}
			br.close();
		}
		catch (Exception e) {
			System.out.println(e.toString());
		}
		return chaine;
	}

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
				Integer i_nextSpace = chaine.indexOf(" ", i_space+1);
				x_i = Integer.parseInt(chaine.substring(0, i_space));
				System.out.println("i = "+i_space);
				System.out.println("i next = " + i_nextSpace);
				y_i = Integer.parseInt(chaine.substring(i_space+1, i_nextSpace));
				System.out.println("i " +i_space);
				System.out.println("i next = " + i_nextSpace);
				i_space = chaine.indexOf(" ", i_nextSpace+1);
				System.out.println("i " +i_space);
				System.out.println("i next = " + i_nextSpace);
				x_f = Integer.parseInt(chaine.substring(i_nextSpace+1, i_space));
				i_nextSpace = chaine.indexOf(" ", i_space+1);
				System.out.println(i_space);
				System.out.println("i next = " + i_nextSpace);
				y_f = Integer.parseInt(chaine.substring(i_space+1, i_nextSpace));

				System.out.println("[x_i,y_i] = [" + x_i + "," + y_i + "] [x_f,y_f] = [" + x_f + "," + y_f + "]");

				switch(chaine.substring(i_nextSpace+1, endLine)){
				case "nord":
					orientation = Orientation.nord;
					break;
				case "sud":
					orientation = Orientation.sud;
					break;
				case "est":
					orientation = Orientation.est;
					break;
				case "ouest":
					orientation = Orientation.ouest;
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
					worlds.add(new World(M, N, M+1, N+1, world, robotWorld, x_i, y_i, x_f, y_f, orientation));
					System.out.println("fin de l'instance n° " + nbMonde + " de monde");
					System.out.println("0 0 ATTEINT");
				}
				else {
					worlds.add(new World(M, N, M+1, N+1, world, robotWorld, x_i, y_i, x_f, y_f, orientation));
					System.out.println("fin de l'instance n° " + nbMonde + " de monde");
					nbMonde++;
					cpt = 0;
				}
			}
		}
	}

	public static void readDataFromIntegerTab(Integer[] tab){
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
		nbMonde = tab[8];

		world = new Point[M][N];
		robotWorld = new Point[M_RW][N_RW];

		for(int x = 0; x < M; x++){
			for(int y = 0; y < N; y++){
				world[x][y] = new Point(0, false, x, y, null, null, 0);
			}
		}
		for(int i = 9; i < nbObstacles*2 + 9; i = i+2){
			world[tab[i]][tab[i+1]].setKey(1);
		}

		World w = new World(M, N, M+1, N+1, world, robotWorld, x_i, y_f, x_f, y_f, orientation);

		w.showMatrice(world);

	}

	@SuppressWarnings("resource")
	public static Integer[] initUserWorld() throws Exception{
		Scanner scanner = new Scanner(System.in);

		System.out.println("Nous vous laissons la possibilité de définir votre propre monde !");
		Thread.sleep(1400);

		System.out.println("Determinez le nombre de lignes de votre monde :");
		int nbLignes = scanner.nextInt();

		System.out.println("Determinez le nombre de colonnes de votre monde :");
		int nbColonnes = scanner.nextInt();

		System.out.println("Determinez le nombre d'obstacles que vous voulez placer :");
		int nbObstacles = scanner.nextInt();

		System.out.println("Determinez la position initiale du robot :");
		Thread.sleep(1000);
		System.out.println("x_i :");
		int x_i = scanner.nextInt();

		System.out.println("y_i :");
		int y_i = scanner.nextInt();

		System.out.println("Determinez la position finale du robot :");
		Thread.sleep(1000);
		System.out.println("x_f :");
		int x_f = scanner.nextInt();

		System.out.println("y_f :");
		int y_f = scanner.nextInt();

		System.out.println("Determinez l'orientation initiale du robot parmi: nord, sud, est, ouest :");
		Thread.sleep(1000);
		System.out.println("orientation :");
		String orientation = scanner.next();

		int o = 0;
		if(orientation.compareToIgnoreCase("sud") == 0)
			o = 1;
		else if(orientation.compareToIgnoreCase("nord") == 0)
			o = 0;
		else if(orientation.compareToIgnoreCase("est") == 0)
			o = 2;
		else if(orientation.compareToIgnoreCase("ouest") == 0)
			o = 3;
		else
			throw new Exception("Erreur: l'orientation specifiee n'est pas definie !");

		Integer[] rslt = new Integer[8 + nbObstacles*2];
		rslt[0] = nbLignes;
		rslt[1] = nbColonnes;
		rslt[2] = nbObstacles;
		rslt[3] = x_i;
		rslt[4] = y_i;
		rslt[5] = x_f;
		rslt[6] = y_f;
		rslt[7] = o;

		for(int i = 0; i < nbObstacles*2; i = i +2){
			System.out.println("Choisir les coordonnées de l'obstacle " + ((i/2)+1));
			System.out.println("x :");
			int out_x = scanner.nextInt();
			System.out.println("y :");
			int out_y = scanner.nextInt();
			rslt[i+8] = out_x;
			rslt[i+1+8] = out_y;
		}

		scanner.close();

		return rslt;
	}

	public void generateRandomWorld(Integer n, Integer nbObstacles) throws Exception{
		if(nbObstacles > n*n){
			throw new Exception("Erreur: nombre d'obstacles plus grand que nombre de cases de la matrice !");
		}
		else {
			Random random = new Random();

			M = N = n;
			M_RW = N_RW = n+1;
			int x_obstacle;
			int y_obstacle;
			int o;

			for(int i = 0; i < nbInstance; i++){
				world = new Point[M][N];
				robotWorld = new Point[M_RW][N_RW];

				for(int x = 0; x < M; x++){
					for(int y = 0; y < N; y++){
						world[x][y] = new Point(0, false, x, y, null, null, 0);
					}
				}		
				for(int obstacle = 0; obstacle < nbObstacles; obstacle++){
					x_obstacle = random.nextInt(M);
					y_obstacle = random.nextInt(N);
					while(world[x_obstacle][y_obstacle].getKey() == 1){
						x_obstacle = random.nextInt(M);
						y_obstacle = random.nextInt(N);
					}
					world[x_obstacle][y_obstacle] = new Point(1, false, x_obstacle, y_obstacle, null, null, -1);
				}

				o = random.nextInt(4);
				
				World created = new World(N, N, M_RW, N_RW, world, robotWorld, x_i, y_i, x_f, y_f, Orientation.values()[o]);
				created.setNbObstacles(nbObstacles);
				worlds.add(created);
				nbMonde++;
			}
		}
		System.out.println(nbMonde+" mondes créé");
	}

	// Generation dans un monde de dimension 20 x 20 avec 10, 20, 30, 40, 50 obstacles
	public void generateRandomObstaclesWorld(int nbObstacles){
		try {
			generateRandomWorld(20,nbObstacles);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void doBFS(){
		ArrayList<LinkedList<Point>> BFSs = new ArrayList<LinkedList<Point>>();
		for(World w : worlds){
			LinkedList<Point> b = w.bfs();
			BFSs.add(b);
		}
		int b = 0;
		for(LinkedList<Point> bfs : BFSs){
			System.out.println("BFS n°" + b);
			if(bfs.isEmpty())
				System.out.println("BFS NON TROUVE");
			else {
				for(Point p : bfs){
					System.out.println("\t" + p.toString());
				}
			}
			b++;
		}
	}

	public static void writeFile(){
		FileOutputStream fos;
		try {
			fos = new FileOutputStream(new File("OutputFile.txt"));
			for(World w : worlds){
				fos.write(w.getOutputFormat().getBytes());
			}
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void writeWorldEntryFile(){
		FileOutputStream fos;
		try {
			fos = new FileOutputStream(new File("EntryFile_user.txt"));
			for(World w : worlds){
				fos.write(w.getWorldEntryFile().getBytes());
			}
			fos.write("0 0".getBytes());
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void writeExecutionTime(){
		FileOutputStream fos;
		String rslt = "";
		long somme = 0;
		long duree = 0;
		Integer lastM = null, lastN = null;
		
		try {
			fos = new FileOutputStream(new File("OutputExecutionFile.txt"));
			rslt = " M ||  N || Execution Time\n"
					+ "--------------------------\n";
			for(Integer i = 0; i < worlds.size()-1; i++){
				World w = worlds.get(i);
				
				if(w.getWorld().length == worlds.get(i+1).getWorld().length){
					duree += w.getExecutionTime();
					lastN = w.getWorld()[0].length;
					lastM = w.getWorld().length;
				}
				
				else {
					somme = duree / nbInstance;
					long sec = somme / 1000;
					long ms = somme % 1000;
					sec %= 60;
					rslt += w.getWorld().length + " || " + w.getWorld()[0].length + " || " + sec + " s " + ms + " ms\n";
					duree = 0;
				}
			}
			somme = duree / nbInstance;
			long sec = somme / 1000;
			long ms = somme % 1000;
			sec %= 60;
			rslt += lastM + " || " + lastN + " || " + sec + " s " + ms + " ms\n";
			fos.write(rslt.getBytes());
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void writeObstaclesExecutionTime(){
		FileOutputStream fos;
		String rslt = "";
		long somme = 0;
		long duree = 0;
		Integer last = null;
		
		try {
			fos = new FileOutputStream(new File("OutputExecutionFile.txt"));
			rslt = " nbObstacles || Execution Time\n"
				 + "------------------------------\n";
			for(Integer i = 0; i < worlds.size()-1; i++){
				World w = worlds.get(i);
				
				if(w.nbObstacles == worlds.get(i+1).nbObstacles){
					duree += w.getExecutionTime();
					last = w.nbObstacles;
				}
				
				else {
					somme = duree / nbInstance;
					long sec = somme / 1000;
					long ms = somme % 1000;
					sec %= 60;
					rslt += "    " + w.nbObstacles + "     || " + sec + " s " + ms + " ms\n";
					duree = 0;
				}
			}
			somme = duree / nbInstance;
			long sec = somme / 1000;
			long ms = somme % 1000;
			sec %= 60;
			rslt += "    " + last + "     || " + sec + " s " + ms + " ms\n";
			fos.write(rslt.getBytes());
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void main(String[] args){
		new FrameWorld();
		
		doBFS();
		writeFile();
		if(random)
			writeExecutionTime();
		else if(randomObstacle)
			writeObstaclesExecutionTime();
		
		for(World w : worlds){
			System.out.println(w.printBFSformat());

		}

	}

}
