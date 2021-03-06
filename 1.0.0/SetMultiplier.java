import java.io.*;
import java.util.Arrays;

//Programa para generar conjuntos aleatorios para probar extensivamente CalPCS
class SetMultiplier {

	static MetodosAnalisis m;
	
	public static void main(String[] args) throws Exception{
		
		SetMultiplier();
		
	}
	
	public static void SetMultiplier() throws Exception{
	
		m = new MetodosAnalisis();
		
		InputStreamReader isr = new InputStreamReader(System.in);
		BufferedReader br = new BufferedReader(isr);

		System.out.println();
		System.out.println("////////////////////////////////////////////////");
		System.out.println("                PCS Multiplier");
		System.out.println("////////////////////////////////////////////////");		
		System.out.println();
		System.out.println("Ingrese el set a multiplicar:");
		System.out.println("Número 'espacio' Número...");
		System.out.println();
		String set;
		set = br.readLine();
		int[] setNotes;
		System.out.println();
		try	{
			setNotes = m.getPcsNotas(set);
			if (evaluarSetValido(setNotes) == true){
				multiply(setNotes);
			} else {
				System.out.println();
				System.out.println("'" + set + "' NO PUEDE PROCESARSE");
				System.out.println("Intentar nuevamente:");
				System.out.println();
				SetMultiplier();
			}
		} catch (Exception e){
				System.out.println();
				System.out.println("'" + set + "' NO PUEDE PROCESARSE");
				System.out.println("Intentar nuevamente:");
				System.out.println();
				SetMultiplier();
		}	
		
		System.out.println();
		exit();
	
	}
	
	public static void multiply(int[] y){
	
		int[] setx5 = new int[y.length];
		int[] setx7 = new int[y.length];
		int[] setx11 = new int[y.length];
		
		for (int i = 0; i < y.length; i++){
			setx5[i] = y[i]*5;
			setx7[i] = y[i]*7;
			setx11[i] = y[i]*11;			
		}
		
		m.getModuloSet(setx5);
		m.getModuloSet(setx7);
		m.getModuloSet(setx11);
		
		System.out.print(m.getPcsText(y));
		y = m.getSetOrdenado(y);
		System.out.println("	(" + m.getPcsText(y) + ")");
		System.out.println(getSetData(y));
		System.out.println();
		System.out.print(m.getPcsText(setx5));
		setx5 = m.getSetOrdenado(setx5);
		System.out.println("	(" + m.getPcsText(setx5) + ")");
		System.out.println(getSetData(setx5));
		System.out.println();
		System.out.print(m.getPcsText(setx7));
		setx7 = m.getSetOrdenado(setx7);
		System.out.println("	(" + m.getPcsText(setx7) + ")");
		System.out.println(getSetData(setx7));
		System.out.println();
		System.out.print(m.getPcsText(setx11));
		setx11 = m.getSetOrdenado(setx11);
		System.out.println("	(" + m.getPcsText(setx11) + ")");
		System.out.println(getSetData(setx11));
		System.out.println("------------------");
	
	}
	
	static String getSetData(int[] y){
	
		String data = new String();
		m.getSetData(y);
		data = m.setData;
		
		return data;
				
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
			SetMultiplier();
		} else {
			System.exit(0);
		}
	
	}

}