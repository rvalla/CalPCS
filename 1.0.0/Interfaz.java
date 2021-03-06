/*/////////////////////////////////////////////////////////////////////////////////////
CalPCS analiza cualquier conjunto de notas musicales indicando a qué conjunto de
grados cromáticos pertenece. Muestra también toda la información relevante de dicho
conjunto (forma prima, vector interválico, par Z, etc).
Desarrollado por Gustavo García Novo y Rodrigo Valla.
/////////////////////////////////////////////////////////////////////////////////////*/
import javax.swing.JComponent;
import javax.swing.Box;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLayeredPane;
import javax.swing.JButton;
import javax.swing.ImageIcon;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Synthesizer;
import javax.sound.midi.MidiChannel;
import javax.sound.midi.Soundbank;
import javax.sound.midi.MidiUnavailableException;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.net.URL;

/*/////////////////////////////////////////////////////////////////////////////////////
La clase interfaz construye la ventana del programa y contiene todos los métodos que
permiten su funcionamiento (ingreso de datos y producción de sonido). Los métodos para
el análisis de los datos ingresados se encuentran en la clase MetodosAnalisis.
/////////////////////////////////////////////////////////////////////////////////////*/
class Interfaz implements ActionListener {

	//Variables globales
	Color negro = new Color(45, 45, 45);
	Color blanco = new Color(255, 255, 255);
	Color rojo = new Color(190, 40, 40);
	Color azul = new Color(90, 140, 190);
	JTextField visorTxt = new JTextField("");
	JTextField resultado = new JTextField("");
	JTextField parZ = new JTextField("");
	JButton bNotas[] = new JButton[12];
	JButton ejecutar = new JButton();
	JButton borrar = new JButton();
	JButton verMemorias = new JButton();
	String sNotas = new String("");
	String pcsHistorial[] = new String [12];
	String [] visorTxtHistorial = new String [12];
	String [] resultadoHistorial = new String [12];
	String [] parZHistorial = new String [12];
	String idioma[] = new String[6];
	int posicionMemoria = 0;
	int posicionMemoriaVer = 0;
	int contadorEjecutar = 0;
	boolean hayInstrumentos = true;
	Font classInfoFont;
	Font dataObtenidaFont;
	Synthesizer sint;
	MidiChannel canal;
	MetodosAnalisis m;
	
	/*//////////
	Constructor
	/////////*/
	Interfaz(){
		
		//Construcción del sintetizador
		try {
			sint = MidiSystem.getSynthesizer();
			sint.open();
         	//Confirmación de la existencia de un banco de sonidos.
         	if (sint.getDefaultSoundbank().getInstruments() == null){
         		hayInstrumentos = false;
         	} else {         		
				canal = sint.getChannels()[0];
         	}
      	} catch (MidiUnavailableException e) {}

		m = new MetodosAnalisis();
		
		getIdioma(System.getProperty("user.language"));
		
		construirVentana(System.getProperty("os.name"));
      	
	}
	
	
	/*///////////////////////////////////////////////////////
	Construcción y métodos de gestión de la interfaz gráfica.
	///////////////////////////////////////////////////////*/
	
	//Construcción de la ventana, agregado del panel principal.
	void construirVentana(String os) {
		
		JFrame v = new JFrame("CalPCS");
		v.setDefaultCloseOperation(3);
		v.setSize(460, 520);
		v.setResizable(false);
		v.setLocationRelativeTo(null);
		
		//Definiendo formatos según sistema operativo
		if (os.startsWith("Windows")){
			classInfoFont = new Font("sansserif", Font.PLAIN, 12);
			dataObtenidaFont = new Font("monospace", Font.BOLD, 14);
		} else {
			classInfoFont = new Font("sansserif", Font.PLAIN, 12);
			dataObtenidaFont = new Font("monospace", Font.PLAIN, 14);
		}
        
        //Icono para windows y linux
        if (os.startsWith("Windows") || os.startsWith("Linux")){
	        URL iconoUrl = getClass().getResource("Icono.png");
			if (iconoUrl != null){
				ImageIcon icono = new ImageIcon(iconoUrl);
				v.setIconImage(icono.getImage());
			}
		}
		
		/*Inicialización de los botones del teclado para poder ejecutar
		los métodos que los configuran.*/	
		for (int i = 0; i < bNotas.length; i++){
			bNotas[i] = new JButton();
		} 
				
		v.add(construirPanel());
		v.setVisible(true);

	}
	
	//Construcción del panel principal.
	JPanel construirPanel(){
	
		JPanel p = new JPanel();
		p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));		
		p.add(Box.createRigidArea(new Dimension (0, 10)));
		p.add(construirpTeclado());
		p.add(Box.createRigidArea(new Dimension (0, 10)));
		p.add(construirpResultados());
		p.add(Box.createRigidArea(new Dimension (0, 10)));
		
		return p;

	}
	
	//Construcción del panel que aloja el teclado.
	JPanel construirpTeclado(){
	
		JPanel p = new JPanel();
		p.setLayout(new BoxLayout(p, BoxLayout.X_AXIS));
		p.setPreferredSize(new Dimension(440, 240));
		p.add(Box.createRigidArea(new Dimension(10, 0)));
		
		JLayeredPane lp = new JLayeredPane();
		lp.setOpaque(true);
        lp.setBorder(BorderFactory.createTitledBorder(idioma[0]));
        
		construirTeclado();
		
        for (int i = 0; i < bNotas.length; i++){
        
        	if (esTeclaNegra(i) == true){
        		lp.add(bNotas[i], new Integer(2));
        	} else {
        		lp.add(bNotas[i], new Integer(1));
        	}
        
        }
        
		p.add(lp);
		p.add(Box.createRigidArea(new Dimension(10, 0)));
	
		return p;
		
	}
	
	//Construcción del panel que mostrará los resultados.
	JPanel construirpResultados(){
	
		JPanel p = new JPanel();
		p.setLayout(new BoxLayout(p, BoxLayout.X_AXIS));
		p.add(Box.createRigidArea(new Dimension(10, 0)));	
		p.add(panelR());
		p.add(Box.createRigidArea(new Dimension(10, 0)));
	
		return p;
		
	}
	
	//Construcción de los paneles que integran el panel Resultados.
	JPanel panelR(){
	
		JPanel p = new JPanel();
		Dimension d = new Dimension(0, 5);
		p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
		p.setBorder(BorderFactory.createTitledBorder(idioma[1]));
		p.add(Box.createRigidArea(d));
		p.add(panelTxt());
		p.add(Box.createRigidArea(d));
		p.add(panelRes());
		p.add(Box.createRigidArea(d));
		p.add(panelZ());
		p.add(Box.createRigidArea(d));
		p.add(panelB());
		p.add(Box.createRigidArea(new Dimension(0, 3)));
		p.add(panelCI());
		p.add(Box.createRigidArea(d));
			
		return p;
	
	}
	
	JPanel panelTxt(){
	
		JPanel p = new JPanel();
		p.setOpaque(true);
		p.setLayout(new BoxLayout(p, BoxLayout.X_AXIS));
		p.setMaximumSize(new Dimension(400, 40));		
		
		visorTxt.setVisible(true);
		visorTxt.setEditable(false);
		visorTxt.setSize(310, 30);
		visorTxt.setHorizontalAlignment(JTextField.CENTER);
		visorTxt.setBackground(blanco);
		visorTxt.setFont(dataObtenidaFont);
	
		p.add(visorTxt);
	
		return p;
	
	}
	
	JPanel panelRes(){
	
		JPanel p = new JPanel();
		p.setOpaque(true);
		p.setLayout(new BoxLayout(p, BoxLayout.X_AXIS));
		p.setMaximumSize(new Dimension(400, 40));		
		
		resultado.setVisible(true);
		resultado.setEditable(false);
		resultado.setSize(310, 30);
		resultado.setHorizontalAlignment(JTextField.CENTER);
		resultado.setBackground(blanco);
		resultado.setFont(dataObtenidaFont);
	
		p.add(resultado);
	
		return p;
	
	}
	
	JPanel panelZ(){
		
		JPanel p = new JPanel();
		p.setOpaque(true);
		p.setMaximumSize(new Dimension(400, 40));
		p.setLayout(new BoxLayout(p, BoxLayout.X_AXIS));
		
		JLabel parZlabel = new JLabel(idioma[2], JLabel.CENTER);
		parZlabel.setVisible(true);
		parZlabel.setSize(60, 30);
		
		parZ.setVisible(true);
		parZ.setEditable(false);
		parZ.setBackground(blanco);
		parZ.setHorizontalAlignment(JTextField.CENTER);
		parZ.setFont(dataObtenidaFont);
		
		p.add(parZlabel);
		p.add(Box.createRigidArea(new Dimension(2,0)));
		p.add(parZ);
	
		return p;
	
	}
	
	JPanel panelB(){
	
		JPanel p = new JPanel();
		p.setOpaque(true);
		p.setPreferredSize(new Dimension(360, 40));
		
		ejecutar.setText(idioma[3]);
		ejecutar.setVisible(true);
		ejecutar.setSize(130, 40);
		ejecutar.addActionListener(this);

		verMemorias.setText(idioma[4]);
		verMemorias.setVisible(true);
		verMemorias.setSize(50, 10);
		verMemorias.addActionListener(this);

		borrar.setText(idioma[5]);
		borrar.setVisible(true);
		borrar.setSize(130, 40);
		borrar.addActionListener(this);

		
		p.add(ejecutar);
		p.add(verMemorias);
		p.add(borrar);
	
		return p;
	
	}
	
	JPanel panelCI(){
	
		JPanel p = new JPanel();
		p.setOpaque(true);
		p.setLayout(new BoxLayout(p, BoxLayout.X_AXIS));
		p.setMaximumSize(new Dimension(314, 50));		
		
		JLabel classInfo = new JLabel("<html><div align='center'>http://sourceforge.net/projects/calpcs<br>"
									+ "Rodrigo Valla - Gustavo García Novo</div><html>", JLabel.CENTER);
	
		p.add(classInfo);
	
		return p;
	
	}
	
	void getIdioma(String s){
	
		if (s.equals("es")){
			idioma[0] = "Teclado";
			idioma[1] = "Resultados";
			idioma[2] = "Par Z:";
			idioma[3] = "Ejecutar";
			idioma[4] = "Historial";
			idioma[5] = "Borrar";
		} else {
			idioma[0] = "Keyboard";
			idioma[1] = "Results";
			idioma[2] = "Z rel.:";
			idioma[3] = "Run";
			idioma[4] = "History";
			idioma[5] = "Erase";
		}	
		
	}
	
	/*/////////////////////////////////////////////////
	Métodos para la construcción y gestión del teclado.
	/////////////////////////////////////////////////*/
	void construirTeclado(){
	
		for (int i = 0; i < bNotas.length; i++){
			
       		bNotas[i].setOpaque(true);
   			bNotas[i].setBorder(null);
			bNotas[i].setBorderPainted(false);
			bNotas[i].addActionListener(this);
			
			//Diferenciación del tamaño de las teclas.
			if (esTeclaNegra(i) == true){			
				bNotas[i].setSize(28, 124);
			} else {
				bNotas[i].setSize(56, 200);
			}
			
	        pintarTeclado();
			posicionarTeclado();
	        bNotas[i].setVisible(true);
        	
		} 
		
	}
	
	//Método para pintar el teclado a blanco y negro.
	void pintarTeclado(){
	
		for (int i = 0; i < bNotas.length; i++){
			
			if (esTeclaNegra(i) == true){			
				bNotas[i].setBackground(negro);
			} else {
				bNotas[i].setBackground(blanco);
			}
		
		}
	
	}
	
	/* Método que posiciona las teclas (pnicial corresponde al margen con respecto al
	borde del panel, ajuste permite corregir el error que se produce cuando aparecen
	dos teclas blancas consecutivas y ajusteNegras permite centrar las teclas negras
	con respecto a las blancas.*/
	void posicionarTeclado(){
	
		int pinicial = 16;
		int ajuste = 0;
		int ajusteNegras = 14;
		
		for (int i = 0; i < bNotas.length; i++){
			
			if (i >= 5){
				ajuste = 29;
			}
			
			if (esTeclaNegra(i) == true){			
				bNotas[i].setLocation(pinicial + (29 * i) + ajuste + ajusteNegras, 28);
			} else {
				bNotas[i].setLocation(pinicial + (29 * i) + ajuste, 28);
			}
		
		}
	
	}
	
	//Métodos para activar y desactivar el teclado.
	void activarTeclado(){
		for (int i=0; i < bNotas.length; i++){
			bNotas[i].setEnabled(true);
		}
	}

	void desactivarTeclado(){
		for (int i=0; i < bNotas.length; i++){
			bNotas[i].setEnabled(false);
		}
	}

	//Método para decidir si el botón corresponde a una tecla blanca o negra.
	boolean esTeclaNegra(int i){
	
		boolean esTN = false;
		if (i == 1 || i == 3 || i == 6 || i == 8 || i == 10){
			esTN = true;
		}
		return esTN;
	
	}
	
	/*//////////////////////////////////////////
	Métodos para el funcionamiento del programa
	//////////////////////////////////////////*/
	public void actionPerformed (ActionEvent ae) {
	
		for (int i=0; i < bNotas.length; i++ ){
			
			if(ae.getSource() == bNotas[i]){
			
				sNotas += String.valueOf(i + " ");
				visorTxt.setText(sNotas);
				
				if (esTeclaNegra(i) == true){			
					bNotas[i].setBackground(azul);
				} else {
				bNotas[i].setBackground(rojo);
				}
				
				bNotas[i].setEnabled(false);
				
				if (hayInstrumentos == true){
					playN(i + 60);
				}
						
			}
			
		}
		
		if(ae.getSource() == ejecutar) {
			
			if(sNotas == ""){
				resultado.setText("No hay nada");
			} else {
				procesarSet();
				desactivarTeclado();
				posicionMemoriaVer = contadorEjecutar - 1;
			}
			
		}		
		
		if(ae.getSource() == borrar) {
			
			sNotas = "";
			visorTxt.setText("");
			resultado.setText("");
			parZ.setText("");
			m.borrar();
			pintarTeclado();
			activarTeclado();
			ejecutar.setEnabled(true);
			
		}
		
		if(ae.getSource() == verMemorias){
			
			if (0 < contadorEjecutar && contadorEjecutar < 12) {
				
				pintarTeclado();
				desactivarTeclado();
				ejecutar.setEnabled(false);
				
				if(0 < posicionMemoriaVer){
					mostrarMemoria(posicionMemoriaVer);
					posicionMemoriaVer = posicionMemoriaVer - 1;
				} else {
					mostrarMemoria(posicionMemoriaVer);
					posicionMemoriaVer = contadorEjecutar - 1;
				}

			} else if (11 < contadorEjecutar){

				pintarTeclado();
				desactivarTeclado();
				ejecutar.setEnabled(false);

				if (posicionMemoriaVer < 12){
				posicionMemoriaVer = posicionMemoriaVer + 12;
				}
			
				mostrarMemoria(posicionMemoriaVer - 12);
				posicionMemoriaVer = posicionMemoriaVer - 1;

			} else {
				
				resultado.setText("¡Historial vacío!");
			
			}

		}
	
	}
	
	/*///////////////////////////////////
	Métodos para al producción de sonido.
	///////////////////////////////////*/
	
	//Método para hacer sonar las teclas.
	void playN(int n){
	
		canal.noteOn(n, 100);
       	try {
       		Thread.sleep(10);
           	} catch (InterruptedException e) {
          
           	} finally {
           	canal.noteOff(n);
           	}
        
    }
    
    //Método para hacer sonar el set.
    void playA(String s){
	
		int[] a = m.getPcsNotas(s);
		
		for(int i = 0; i < a.length; i++){
			canal.noteOn(a[i] + 60, 50);
		}
		
       	try {
       		Thread.sleep(200);
           	} catch (InterruptedException e) {
          
           	} finally {
           	canal.allNotesOff();
        }
        
    }
    
    /*////////////////////////////////
	Gestión del historial de análisis
	////////////////////////////////*/
	void mostrarMemoria(int x){

		getPosicionMemoriaData(x);
		getPosicionMemoriaTeclas(x);
		if (hayInstrumentos == true){
			playA(pcsHistorial[x]);
		}

	}
	
	void getPosicionMemoriaData(int x){

		visorTxt.setText(visorTxtHistorial[x]);
		resultado.setText(resultadoHistorial[x]);
		parZ.setText(parZHistorial[x]);
	
	}

	void getPosicionMemoriaTeclas(int x){

		int[] y = m.getPcsNotas(pcsHistorial[x]);

		for (int i=0; i < y.length; i++){
			if (esTeclaNegra(y[i]) == true){			
					bNotas[y[i]].setBackground(azul);
				} else {
				bNotas[y[i]].setBackground(rojo);
			}
		}
	
	}

	void guardarSetData(int x){

		pcsHistorial[x] = sNotas;
		visorTxtHistorial[x] = visorTxt.getText();
		resultadoHistorial[x] = resultado.getText();
		parZHistorial[x] = parZ.getText();

	}

	void gestionarPosicionMemoria(){
		
		if (contadorEjecutar == 24){
			contadorEjecutar = contadorEjecutar - 11;
		} else {
			contadorEjecutar = contadorEjecutar + 1;
		}
		
		if (posicionMemoria == 11){
				posicionMemoria = 0;
			} else {
				posicionMemoria = posicionMemoria + 1;
		}
			
	}
	
	/*///////////////////
	Gestión del análisis
	///////////////////*/
	void procesarSet(){
	
		int set[] = m.getPcsNotas(visorTxt.getText());
		if (set.length == 12){
			m.getAlturasOrdenadas(set);
			visorTxt.setText(visorTxt.getText() + " (" + m.getPcsText(set) + ")");
			m.getSetData(set);
			resultado.setText(m.setData);
		} else if (set.length < 12 && set.length > 1){
			m.getSetData(set);
			visorTxt.setText(visorTxt.getText() + " (" + m.setOrdenado + ")");
			resultado.setText(m.setData);
			parZ.setText(m.parZData);
		} else if (set.length == 1){
			visorTxt.setText(visorTxt.getText() + " (" + m.getPcsText(set) + ")");
			m.getSetData(set);
			resultado.setText(m.setData);
			parZ.setText(m.parZData);
		}
		
		if (hayInstrumentos == true){
			playA(sNotas);
		}
		
		guardarSetData(posicionMemoria);
		gestionarPosicionMemoria();
		
	}

}