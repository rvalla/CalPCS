/*/////////////////////////////////////////////////////////////////////////////////////
CalPCS analiza cualquier conjunto de notas musicales indicando a qué conjunto de
grados cromáticos pertenece. Muestra también toda la información relevante de dicho
conjunto (forma prima, vector interválico, par Z, etc).
Desarrollado por Gustavo García Novo y Rodrigo Valla.
/////////////////////////////////////////////////////////////////////////////////////*/
import java.util.Arrays;

/*/////////////////////////////////////////////////////////////////////////////////////
La clase MetodosAnalisis contiene todos los métodos necesarios para analizar los
conjuntos y leer los datos alojados en BaseDatosSets.
/////////////////////////////////////////////////////////////////////////////////////*/
class MetodosAnalisis {
	
	//Se instancia la base de datos.
	BaseDatosSets b = new BaseDatosSets();
	//Variables globales
	boolean esInversion = false;
	boolean esZ = false;
	String setOrdenado = "";
	String setData = "";
	String parZData = "";
	
	/*Método que recibe un String con las notas representadas con números separados
	por espacios y devuelve un arreglo de enteros que contiene los elementos del
	conjunto.*/
	int[] getPcsNotas (String s){

		String conjuntoAlturas[] = s.split(" ");
		int [] valoresConjunto = new int[conjuntoAlturas.length];

		for (int i=0; i < valoresConjunto.length; i++){
			valoresConjunto[i] = Integer.parseInt(conjuntoAlturas[i]); 
		}

		return valoresConjunto;

	}
	
	/*Método que recibe un arreglo de enteros y devuelve sus valores separados
	por espacios.*/
	String getPcsText(int[] x){
		
		String conjuntoAlturas = new String();

		for (int i=0; i < x.length ; i++){	
			conjuntoAlturas += String.valueOf(x[i]);
			if (i < x.length - 1){
				conjuntoAlturas += " ";
			}
		}
		
		return conjuntoAlturas;
	
	}
	
	//Método que ordena las alturas de menor a mayor.
	void getAlturasOrdenadas (int[] set){
	
		Arrays.sort(set);
	
	}
	
	/*Método que recibe un conjunto de alturas como arreglo de enteros y devuelve
	un arreglo que corresponde a su orden normal según el algoritmo propuesto por
	Allen Forte. Para usar el algoritmo de Rahn sólo es necesario descartar el
	condicional de la línea 106, reemplazar la linea 113 por 'p = p-1' y adaptar
	la condición de la línea 142.*/
	int[] getSetOrdenado(int[] x){
	
		getAlturasOrdenadas(x);
		int m[][] = new int[x.length][x.length];
		m[0] = x;
		
		for (int i = 1; i < m.length; i++){
			
			for (int o = 0; o < m.length; o++){
				m[i][o] = m[i-1][o];
			}
			m[i][0] = m[i][0] + 12;
			Arrays.sort(m[i]);

		}
	
		int p = x.length - 1;
		int v = 12;
		int orden = 0;
		int cantidadIguales = 0;
		String filasIguales = new String("");
		boolean hayIgualdad = false;
		
		for (int i = 0; i < m.length; i++){
			if (m[i][p] - m[i][0] < v){
				v = m[i][p] - m[i][0];
				hayIgualdad = false;
				cantidadIguales = 1;
				orden = i;
				filasIguales = String.valueOf(i);
			} else if (m[i][p] - m[i][0] == v){
				hayIgualdad = true;
				cantidadIguales = cantidadIguales + 1;
				filasIguales += " " + String.valueOf(i);
			}
		}
		
		if (hayIgualdad == true){
			p = 0;
		}
				
		while (hayIgualdad == true){
		
			v = 12;
			p = p + 1;
			String fIguales[] = filasIguales.split(" ");
			int iguales[] = new int[fIguales.length];
			for (int i = 0; i < fIguales.length; i++){
				iguales[i] = Integer.parseInt(fIguales[i]);
			}
			
			int mm[][] = new int[iguales.length][x.length];
			
			for (int i = 0; i < mm.length; i++){
				for (int o = 0; o < mm[i].length; o++){
					mm[i][o] = m[iguales[i]][o];
				}
			}
		
			for (int i = 0; i < mm.length; i++){
				if (mm[i][p] - mm[i][0] < v){
					v = mm[i][p] - mm[i][0];
					hayIgualdad = false;
					cantidadIguales = 1;
					orden = iguales[i];
					filasIguales = String.valueOf(iguales[i]);
				} else if (mm[i][p] - mm[i][0] == v){
					hayIgualdad = true;
					cantidadIguales = cantidadIguales + 1;
					filasIguales += " " + String.valueOf(iguales[i]);
				}
			}
			
			if (p == x.length - 1){
				fIguales = filasIguales.split(" ");
				iguales[0] = Integer.parseInt(fIguales[0]);
				orden = iguales[0];
				break;			
			}
			
		}
		
		getModuloSet(m[orden]);
		return m[orden];
		
	}
	
	//Método que recibe un conjunto y devuelve su inversión en orden normal.
	int[] getSetInvertido(int[] x){
	
		int i[] = new int[x.length];
		for (int o = 0; o < i.length; o++){
			i[o] = 12 - x[o];
		}
		Arrays.sort(i);
		int setInvertido[] = getSetOrdenado(i);
		
		return setInvertido;
	
	}
	
	//Método que recibe un conjunto en orden normal y lo lleva a t0.
	void getTransporteCero(int[] x){
	
		int aCero = x[0];
		
		for (int i = 0; i < x.length; i++){
			if (x[i] < aCero){
				x[i] = x[i] + 12;
			}
			x[i] = x[i] - aCero;
		}
	
	}
	
	//Método que recibe un conjunto en orden normal y devuelve su transporte.
	int getTransporte(int[] set){
	
		int t;
		if (set[0] < 12){
			t = set[0];
		} else {
			t = set[0] - 12;
		}
		
		return t;
	
	}
	
	/*Método que recibe un conjunto de alturas y escribe las cadenas de texto que
	contienen toda la información del conjunto: "Cantidad de elementos - clasificación
	- transporte - vector interválico - limitación del conjunto.*/
	void getSetData(int [] x){
	
		
		int t = x.length;
		int set[] = getSetOrdenado(x);
		int tt = getTransporte(set);
		setOrdenado = getPcsText(set);
		getTransporteCero(set);
		
		if (t < 12){
			if (t == 1){
				setData = "1.1 t" + String.valueOf(tt) + "  (0)  [000000]  |12|";
			} else if (t > 1 && t < 12){
				int[] c = getSet(set);
				setData = String.valueOf(c[0]) + ".";
				if (esZ == true){
					setData += "Z";
					parZData = getZ(c);
				}
				setData +=  String.valueOf(c[1]) + " ";
				setData += "t" + String.valueOf(tt);
				if (esInversion == true){
					setData += "i";
				}
				setData   += "  (" + getPcsText(getFormaPrima(c)) + ")  ";
				setData  += "[" + getVector(set) + "]";
				setData += "  |" + String.valueOf(getLimitacion(c)) + "|";
			}
		} else if (t == 12){
			setData = "12.1 t0  (0 1 2 3 4 5 6 7 8 9 10 11)  [12121212126]  |1|";
		}
	
	}
	
	/*Método que recibe la clasificación de un conjunto con par Z y devuelve toda
	la información de dicho conjunto relacionado.*/
	String getZ(int[] clasificacion){
	
		String data = new String("");
		data = String.valueOf(clasificacion[0]) + ".Z";
		int s;
	
		if (clasificacion[0] == 4){
			s = b.set4Data[clasificacion[1] - 1][0] - 1;
			data += String.valueOf(s + 1);
			int set[] = b.set4[s];
			data += " t0  (" + getPcsText(set) + ")  ";
			data += "[" + getVector(set) + "]  |" + String.valueOf(b.set4Data[s][1]) + "|";
		} else if (clasificacion[0] == 5){
			s = b.set5Data[clasificacion[1] - 1][0] - 1;
			data += String.valueOf(s + 1);
			int set[] = b.set5[s];
			data += " t0  (" + getPcsText(set) + ")  ";
			data += "[" + getVector(set) + "]  |" + String.valueOf(b.set5Data[s][1]) + "|";
		} else if (clasificacion[0] == 6){
			s = b.set6Data[clasificacion[1] - 1][0] - 1;
			data += String.valueOf(s + 1);
			int set[] = b.set6[s];
			data += " t0  (" + getPcsText(set) + ")  ";
			data += "[" + getVector(set) + "]  |" + String.valueOf(b.set6Data[s][1]) + "|";
		} else if (clasificacion[0] == 7){
			s = b.set7Data[clasificacion[1] - 1][0] - 1;
			data += String.valueOf(s + 1);
			int set[] = b.set7[s];
			data += " t0  (" + getPcsText(set) + ")  ";
			data += "[" + getVector(set) + "]  |" + String.valueOf(b.set7Data[s][1]) + "|";
		} else if (clasificacion[0] == 8){
			s = b.set8Data[clasificacion[1] - 1][0] - 1;
			data += String.valueOf(s + 1);
			int set[] = b.set8[s];
			data += " t0  (" + getPcsText(set) + ")  ";
			data += "[" + getVector(set) + "]  |" + String.valueOf(b.set8Data[s][1]) + "|";
		}
	
		return data;
	
	}
	
	//Indica el número de conjuntos distintos posibles para una clasificación.
	int getLimitacion(int[] clasificacion){
	
		int l = 24;
		
		if (clasificacion[0] == 2){
			l = b.set2Data[clasificacion[1] - 1][1];
		} else if (clasificacion[0] == 3){
			l = b.set3Data[clasificacion[1] - 1][1];
		} else if (clasificacion[0] == 4){
			l = b.set4Data[clasificacion[1] - 1][1];
		} else if (clasificacion[0] == 5){
			l = b.set5Data[clasificacion[1] - 1][1];
		} else if (clasificacion[0] == 6){
			l = b.set6Data[clasificacion[1] - 1][1];
		} else if (clasificacion[0] == 7){
			l = b.set7Data[clasificacion[1] - 1][1];
		} else if (clasificacion[0] == 8){
			l = b.set8Data[clasificacion[1] - 1][1];
		} else if (clasificacion[0] == 9){
			l = b.set9Data[clasificacion[1] - 1][1];
		} else if (clasificacion[0] == 10){
			l = b.set10Data[clasificacion[1] - 1][1];
		} else if (clasificacion[0] == 11){
			l = b.set11Data[clasificacion[1] - 1][1];
		}
		
		return l;
	
	}
	
	/*Método que recibe una clasificación como arreglo de enteros y devuelve la
	forma prima de dicho conjunto.*/
	int[] getFormaPrima(int[] clasificacion){
	
		int s[] = new int[clasificacion[0]];
		
		if (clasificacion[0] == 2){
			s = b.set2[clasificacion[1] - 1];
		} else if (clasificacion[0] == 3){
			s = b.set3[clasificacion[1] - 1];
		} else if (clasificacion[0] == 4){
			s = b.set4[clasificacion[1] - 1];
		} else if (clasificacion[0] == 5){
			s = b.set5[clasificacion[1] - 1];
		} else if (clasificacion[0] == 6){
			s = b.set6[clasificacion[1] - 1];
		} else if (clasificacion[0] == 7){
			s = b.set7[clasificacion[1] - 1];
		} else if (clasificacion[0] == 8){
			s = b.set8[clasificacion[1] - 1];
		} else if (clasificacion[0] == 9){
			s = b.set9[clasificacion[1] - 1];
		} else if (clasificacion[0] == 10){
			s = b.set10[clasificacion[1] - 1];
		} else if (clasificacion[0] == 11){
			s = b.set11[clasificacion[1] - 1];
		}
		
		return s;
		
	}
	
	//Método que recibe el conjunto en forma prima y busca su clasificación.
	int[] getSet(int[] set){
	
		esInversion = false;
		esZ = false;
		int n[] = new int[2];
		n[0] = set.length;
		
		if (set.length == 2){
			n[1] = buscarSet(set, b.set2);
		} else if (set.length == 3){
			n[1] = buscarSet(set, b.set3);
		} else if (set.length == 4){
			n[1] = buscarSet(set, b.set4);
			if (b.set4Data[n[1] - 1][0] != 0){
				esZ = true;
			}
		} else if (set.length == 5){
			n[1] = buscarSet(set, b.set5);
			if (b.set5Data[n[1] - 1][0] != 0){
				esZ = true;
			}
		} else if (set.length == 6){
			n[1] = buscarSet(set, b.set6);
			if (b.set6Data[n[1] - 1][0] != 0){
				esZ = true;
			}
		} else if (set.length == 7){
			n[1] = buscarSet(set, b.set7);
			if (b.set7Data[n[1] - 1][0] != 0){
				esZ = true;
			}
		} else if (set.length == 8){
			n[1] = buscarSet(set, b.set8);
			if (b.set8Data[n[1] - 1][0] != 0){
				esZ = true;
			}
		} else if (set.length == 9){
			n[1] = buscarSet(set, b.set9);
		} else if (set.length == 10){
			n[1] = buscarSet(set, b.set10);
		} else if (set.length == 11){
			n[1] = buscarSet(set, b.set11);
		}
		
		return n; 
	
	}
	
	/*Método que recibe el conjunto a analizar y la matriz que aloja las formas primas
	de conjuntos de la misma cantidad de elementos. Devuelve su clasificación.*/
	int buscarSet(int[] set, int[][] datos){
	
		int s = 0;
		for (int i = 0; i < datos.length; i++){
			if (Arrays.equals(set, datos[i])){
				s = i + 1;
				break;
			}
		}
		
		if (s == 0){
			int ss[] = getSetInvertido(set);
			getTransporteCero(ss);
			s = buscarSet(ss, datos);
			esInversion = true;
		}
		
		return s;
	
	}
	
	/*Método que recibe un conjunto y devuelve su vector interválico como cade
	de texto.*/
	String getVector (int[] set){
	
		int[] vector = new int [6];
				
		for (int i=0; i < vector.length ; i++){
			vector[i]=0;				
		}
		
		int Intervalo = 0;
		
		for (int i=set.length - 1; i >= 0; i=i-1){
		
			for (int e=i - 1; e >= 0; e = e -1){
			
			 Intervalo = set[i] - set[e];
			 
			 if (Intervalo > 6){
			 
			 	Intervalo = 12 - Intervalo;
			 
			 }
			 
			 vector[Intervalo - 1] = vector[Intervalo -1] + 1;				
			
			}
		
		} 
		
		String v = new String("");
		for (int i = 0; i < vector.length; i++){
			v += String.valueOf(vector[i]);
		}
		
		return v;
	
	}
	
	/*Método que recibe un set y se asegura que sus elementos sean representados por
	números del 1 al 11.*/
	void getModuloSet(int[] set){
		
		for (int i = 0; i < set.length; i++){
			while (set[i] > 11){ 
				set[i] = set[i] - 12;
			}
		}
	
	}
	
	/*Método para reiniciar las cadenas de texto que contienen la información del
	conjunto.*/
	void borrar(){
	
		setOrdenado = "";
		setData = "";
		parZData = "";
	
	}
	
	
}