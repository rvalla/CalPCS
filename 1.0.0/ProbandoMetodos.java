import java.io.*;
import java.util.Random;
import java.util.Arrays;

//Programa para generar conjuntos aleatorios para probar extensivamente CalPCS
class ProbandoMetodos {

	static Random r;
	static MetodosAnalisis m;
	
	public static void main(String[] args) throws Exception{
		
		ProbandoMetodos();
		
	}
	
	public static void ProbandoMetodos() throws Exception{
	
		r = new Random();
		m = new MetodosAnalisis();
		
		InputStreamReader isr = new InputStreamReader(System.in);
		BufferedReader br = new BufferedReader(isr);

		System.out.println();
		System.out.println("////////////////////////////////////////////////");
		System.out.println("PUESTA A PRUEBA DE METODOS DE ANALISIS DE CalPCS");
		System.out.println("////////////////////////////////////////////////");		
		System.out.println();
		System.out.println("Ingrese el numero de conjuntos aleatorios a generar:");
		String cantidad;
		cantidad = br.readLine();
		int c = 1;
		try	{c = (Integer.parseInt(cantidad));
		} catch (NumberFormatException e){
				System.out.println();
				System.out.println("'" + cantidad + "' NO PUEDE PROCESARSE");
				System.out.println("Intentar nuevamente:");
				System.out.println();
				ProbandoMetodos();
		}	
		
		System.out.println();
		probarMetodos(c);
		exit();
	
	}
	
	public static void probarMetodos(int y){
	
		for (int i = 0; i < y; i++){
		
			int t = r.nextInt(11);
			int set[] = new int[t + 1];
			
			for (int o = 0; o < set.length; o++){				
				set[o] = r.nextInt(12);
			}
			
			m.getModuloSet(set);
			if (evaluarSetValido(set) == true){
				m.getSetData(set);
				System.out.println(m.setData);
			}
			
		}
	
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
			ProbandoMetodos();
		} else {
			System.exit(0);
		}
	
	}

}