//Programa para analizar PCS en línea de comandos
import java.io.*;
import java.util.Arrays;

//Programa para generar conjuntos aleatorios para probar extensivamente CalPCS
class CalPCS_cp {

	static MetodosAnalisis m;
	
	public static void main(String[] args) throws Exception{
		
		CalPCS_cp();
		
	}
	
	public static void CalPCS_cp() throws Exception{
	

		m = new MetodosAnalisis();
		
		InputStreamReader isr = new InputStreamReader(System.in);
		BufferedReader br = new BufferedReader(isr);

	System.out.println();
		System.out.println("////////////");
		System.out.println("CalPCS 1.0.0");
		System.out.println("////////////");		
		System.out.println();
		System.out.println("Ingrese el conjunto a analizar:");
		String set;
		set = br.readLine();
		int notas[];
		
		try	{
			notas = m.getPcsNotas(set);
			Arrays.sort(notas);
			m.getModuloSet(notas);
			if (evaluarSetValido(notas) == true){
				getSetData(notas);
			} else {
				System.out.println();
				System.out.println("'" + set + "' NO ES UN CONJUNTO VALIDO");
				System.out.println("Intentar nuevamente:");
				System.out.println();
				CalPCS_cp();
			}
		} catch (Exception e){
				System.out.println();
				System.out.println("'" + set + "' NO PUEDE PROCESARSE");
				System.out.println("Intentar nuevamente:");
				System.out.println();
				CalPCS_cp();
		}	
		
		exit();
	
	}
	
	public static void getSetData(int y[]){
	
			m.getSetData(y);
			System.out.println();
			System.out.println("Info:");
			System.out.println("Orden normal: " + m.setOrdenado);
			System.out.println(m.setData);
			System.out.println(m.parZData);
			
	}
	
	//Metodo para asegurar que el conjunto es valido
	static boolean evaluarSetValido(int set[]){
		
		boolean valido = true;
		
		for (int i = 0; i < set.length; i++){
			for (int o = 0; o < set.length - 1; o++){
				if (i != o){
					if (set[i] == set[o]){
						valido = false;
					}
				}
			}
		}

		return valido;
	
	}
	
	public static void exit () throws Exception{
		
		InputStreamReader isr2 = new InputStreamReader(System.in);
		BufferedReader br2 = new BufferedReader(isr2);

		System.out.println();
		System.out.println("Para correr el programa de nuevo tipear 'n'");
		String ex;
		ex = br2.readLine();
		if (ex.equals("n")) {
			System.out.println(); 
			System.out.println(); 			
			CalPCS_cp();
		} else {
			System.exit(0);
		}
	
	}

}