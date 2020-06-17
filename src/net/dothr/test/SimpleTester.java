package net.dothr.test;

import net.utils.ClientRestUtily;


public class SimpleTester {
	
	
	public static void main(String[] args) {
		numerador(127, 504);
	}
	
	/**
	 * Simple numerador para lineas en archivo
	 * @param inicio
	 * @param fin
	 */
	public static void numerador(long inicio, long fin){
		for(;inicio<=fin;inicio++){
			System.out.println(inicio);
		}
	}
	
	

}
