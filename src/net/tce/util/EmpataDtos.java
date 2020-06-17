package net.tce.util;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import com.google.gson.Gson;

import net.tce.dto.AreaPersonaDto;

public class EmpataDtos {

	protected static Gson gson;
	
	public static void main(String[] args) {

		testMergeAreaPersona();
	}
	
	
	
	
	
	private static void testMergeAreaPersona (){
		System.out.println("<testMergeAreaPersona>");
		List<AreaPersonaDto> lsNuevos = new ArrayList<AreaPersonaDto>()
				, lsModelo = new ArrayList<AreaPersonaDto>()
				, lsFinal;
		try{
			gson = new Gson();
			/*  NUEVAS AREAS */
			lsNuevos.add(new AreaPersonaDto(null, new Long(8), null, null, null, null) ); //("8", null) );
			lsNuevos.add(new AreaPersonaDto(null, new Long(12), null, null, null, null) ); //("12", "true") );
			lsNuevos.add(new AreaPersonaDto(null, new Long(2), null, null, null, null) );  //("2", null) );
			
			System.out.println(">>  lsNuevos [" + lsNuevos.size() + "] ");  //> 8,12*,1
			if(!lsNuevos.isEmpty()){
				for (AreaPersonaDto apDto1 : lsNuevos)
				{
					System.out.print(gson.toJson(apDto1)+" ");
				}
			}
			
			
			/* AREAS EN BD  */
			//{"idAreaPersona":"5","idArea":"1","personal":true}
//			lsModelo.add(new AreaPersonaDto(new Long(5), new Long(1), null, null, null, false) );
//			//{"idAreaPersona":"6","idArea":"2","personal":true, "principal":true}
//			lsModelo.add(new AreaPersonaDto(new Long(6), new Long(2), null, true, null, false) );
//			//{"idAreaPersona":"8","idArea":"3","personal":false}
//			lsModelo.add(new AreaPersonaDto(new Long(8), new Long(3), null, null, null, false) ); //* true/false 
//			//{"idAreaPersona":"9","idArea":"4","personal":true}
//			lsModelo.add(new AreaPersonaDto(new Long(9), new Long(12), null, null, null, true) );
			
			System.out.println("\n  lsModelo [" + lsModelo.size() + "] ");  //> 1,2*
			if(!lsModelo.isEmpty()){
				for (AreaPersonaDto apDto : lsModelo)
				{
					System.out.print(gson.toJson(apDto)+" ");
				}
			}
			
			System.out.println("\n \n");
			
			lsFinal= mergedList(lsNuevos, lsModelo);
			System.out.println("\n ************** LISTA FINAL  ****************\n");
			AreaPersonaDto dto;
			ListIterator<AreaPersonaDto> itFinal = lsFinal.listIterator();
			
			while(itFinal.hasNext()){
				dto = itFinal.next();
				System.out.print(gson.toJson(dto) + (itFinal.hasNext()?", ":"") );
			}
		}catch (Exception e){
			e.printStackTrace();
		}
	}
	
	
	protected static List<AreaPersonaDto> mergedList(List<AreaPersonaDto> lsNuevos, 
			List<AreaPersonaDto> lsModelo){
		List<AreaPersonaDto> lsFinal;
		ListIterator<AreaPersonaDto> itNuevos;
		AreaPersonaDto dtoA;
		System.out.println("<mergedList> lsNuevos.size(): " + lsNuevos.size() + " lsModelo.size(): " + lsModelo.size());
		if((lsNuevos!=null && lsNuevos.size()>0) && (lsModelo!=null && lsModelo.size()>0) ){
//			System.out.println("<mergedList> lsNuevos.size(): " + lsNuevos.size());				
				ListIterator<AreaPersonaDto> itMod;
				AreaPersonaDto dtoB;
//				System.out.println("lsModelo.size(): " + lsModelo.size());
				
				lsFinal = new ArrayList<AreaPersonaDto>();
				//Iterar para empatar datos ya existentes
				itNuevos = lsNuevos.listIterator();
				while(itNuevos.hasNext()){
					dtoA = itNuevos.next();
					itMod = lsModelo.listIterator();
//					System.out.println("A: " +gson.toJson(dtoA));
					while(itMod.hasNext() && dtoA.getIdAreaPersona()==null){
						dtoB = itMod.next();
						if(dtoA.getIdArea().equals(dtoB.getIdArea())){
							//El area ya esta relacionada con usuario, se copia ID
							dtoA.setIdAreaPersona( dtoB.getIdAreaPersona() );
							//valida si esta ya esta confirmada
							if(dtoB.getConfirmada()!=null && dtoB.getConfirmada()){ //UNBOXING
								dtoB.setConfirmada(true); //AutoBoxing
							}							
							//SobreEscribe si es Personal ?
							//dtoA.setPersonal(true);
							dtoA.setPersonal( dtoB.getPersonal()!=null?dtoB.getPersonal():true ); //Si existe se copia, sino se escribe personal
							lsFinal.add(dtoA);
							itNuevos.remove();
							itMod.remove();
						}
					}
				}
				
				//Se iteran los restantes de BD para obtener los ID's disponibles
				if(!lsModelo.isEmpty() && !lsNuevos.isEmpty()){
					itNuevos = lsNuevos.listIterator();
					while(itNuevos.hasNext()){
						dtoA = itNuevos.next();
						itMod = lsModelo.listIterator();
//						System.out.println("A: " +gson.toJson(dtoA));

						while(itMod.hasNext() && dtoA.getIdAreaPersona()==null){
							dtoB = itMod.next();
							if(dtoB.getPersonal()==null || dtoB.getPersonal() ){ //Si NO tiene valor o el valor es True (personal) se puede sobreescribir 
								dtoA.setIdAreaPersona( dtoB.getIdAreaPersona() );
								dtoA.setPersonal(true);
								lsFinal.add(dtoA);
								itNuevos.remove();
								itMod.remove();
							}
						}
					}	
				}
				
				//Si aún hay elementos nuevos se agregan al listado final (sin ID se hará create con personal = true)
				if(!lsNuevos.isEmpty()){
					/*/>>Temporal para pruebas:
					itNuevos = lsNuevos.listIterator();
					while(itNuevos.hasNext()){
						dtoA = itNuevos.next();
						dtoA.setPersonal(true);
					}
					//<<< */
					lsFinal.addAll(lsNuevos);
				}
				
				//Si hay elementos en BD que no se utilizaron y SON personales, se coloca bandera de borrar
				if(!lsModelo.isEmpty()){
					itMod = lsModelo.listIterator();
					while(itMod.hasNext()){
						dtoB = itMod.next();
						if(dtoB.getPersonal()!=null && dtoB.getPersonal() ){
							dtoB.setPrincipal(null); //Opcional
							dtoB.setPersonal(null); //Opcional 
							dtoB.setToDelete(true);
							lsFinal.add(dtoB);
						}
					}
				}
				
				//temp
				//lsFinal = lsNuevos;
			
		}
		else
			lsFinal = lsNuevos;
		
		//TODO validar si existe un principal? 
		itNuevos = lsFinal.listIterator();
		boolean hayPrincipal = false;
		while(itNuevos.hasNext() && !hayPrincipal){
			dtoA = itNuevos.next();
			if(dtoA.getPrincipal()!=null && dtoA.getPrincipal()){
				hayPrincipal = true;
			}
		}
		if(!hayPrincipal){
			lsFinal.get(0).setPrincipal(true);
		}
		return lsFinal;
	}

}
/*
0) 3 Nuevos vs 0 en BD 
==>lsFinal: {"idArea":"8"} {"idArea":"12","principal":true} {"idArea":"2"} 

1) 3 nuevos vs 2 en BD (1 coincidente) Todos personales
lsNuevos [3] 
{"idArea":"8"} {"idArea":"12","principal":true} {"idArea":"2"} 

lsModelo [2] 
{"idAreaPersona":"5","idArea":"1"} {"idAreaPersona":"6","idArea":"2","principal":true} 


++++++++++++++++ cuando son Mas nuevos que BD ++++++++++++++ (algunos con id, y ninguno para borrar)
==>lsFinal: 3 areas (2 con idAp)

{"idAreaPersona":"6", "idArea":"2", "personal":true} {"idAreaPersona":"5", "idArea":"8", "personal":true} {"idArea":"12", "personal":true, "principal":true} 


2) 3 nuevos vs 1 en BD (1 coincidente) Todos personales
lsNuevos [3] : 
{"idArea":"8"} {"idArea":"12","principal":true} {"idArea":"2"} 

lsModelo [1] 
{"idAreaPersona":"6","idArea":"2","principal":true} 

==> {"idAreaPersona":"6","idArea":"2","personal":true}, {"idArea":"8","personal":true}, {"idArea":"12","principal":true,"personal":true}
    

3) 3 Nuevos vs 1 BD (Sin coincidencia) TP
lsNuevos [3] : 
{"idArea":"8"} {"idArea":"12","principal":true} {"idArea":"2"} 

lsModelo [1] 
{"idAreaPersona":"5","idArea":"1"}


==> {"idAreaPersona":"5","idArea":"8","personal":true}, {"idArea":"12","principal":true}, {"idArea":"2"}


++++++++++++++++ cuando son Menos nuevos que BD ++++++++++++++ (todos con id, algunos para borrar)
4) 2 Nuevos vs 3 en BD (1 coincidente [2]) TP
lsNuevos [2] 
{"idArea":"12","principal":true} {"idArea":"2"} 

lsModelo [3] 
{"idAreaPersona":"5","idArea":"1", "personal":true} {"idAreaPersona":"6","idArea":"2","principal":true, "personal":true}, {"idAreaPersona":"8","idArea":"3", "personal":true}



==> {"idAreaPersona":"6","idArea":"2","personal":true}, {"idAreaPersona":"5","idArea":"12","principal":true,"personal":true}, {"idAreaPersona":"8","idArea":"3","toDelete":true}

5) 2 Nuevos vs 4 en BD (Ningun coincidente) TP
lsNuevos [2] : 
{"idArea":"8"} {"idArea":"12","principal":true}

lsModelo [4] 
{"idAreaPersona":"5","idArea":"1", "personal":true} {"idAreaPersona":"6","idArea":"2","principal":true, "personal":true}, {"idAreaPersona":"8","idArea":"3", "personal":true} {"idAreaPersona":"9","idArea":"4", "personal":true}

==> {"idAreaPersona":"5","idArea":"8"} {"idAreaPersona":"6","idArea":"12","principal":true} {"idAreaPersona":"8","idArea":"3","toDelete":true} {"idAreaPersona":"9","idArea":"4", "toDelete":true}

{"idAreaPersona":"5","idArea":"8","personal":true}, {"idAreaPersona":"6","idArea":"12","principal":true,"personal":true}, {"idAreaPersona":"8","idArea":"3","toDelete":true}, {"idAreaPersona":"9","idArea":"4","toDelete":true}




+++++++++++++++ Pruebas con DB No personales NP


6) 3 nuevos vs 4 en BD (1 coincidente) 3 P, 1 NP
lsNuevos [3] 
{"idArea":"8"} {"idArea":"12","principal":true} {"idArea":"2"}

lsModelo [4] 
{"idAreaPersona":"5","idArea":"1", "personal":true} {"idAreaPersona":"6","idArea":"2","principal":true, "personal":true}, {"idAreaPersona":"8","idArea":"3", "personal":false} {"idAreaPersona":"9","idArea":"4", "personal":true}

==> {"idAreaPersona":"6","idArea":"2","personal":true}, {"idAreaPersona":"5","idArea":"8","personal":true}, {"idAreaPersona":"9","idArea":"12","principal":true,"personal":true}


7) 1 nuevo vs 4 en BD (Sin coincidencias) 3 P, 1 NP
lsNuevos [1] 
{"idArea":"12","principal":true}

lsModelo [4] 
{"idAreaPersona":"5","idArea":"1", "personal":true} {"idAreaPersona":"6","idArea":"2","principal":true, "personal":true}, {"idAreaPersona":"8","idArea":"3", "personal":false} {"idAreaPersona":"9","idArea":"4", "personal":true}

==> {"idAreaPersona":"5","idArea":"12","principal":true,"personal":true}, {"idAreaPersona":"6","idArea":"2","toDelete":true}, {"idAreaPersona":"9","idArea":"4","toDelete":true}


8) 3 nuevos vs 4 en BD (1 coincidente) 1 P, 3 NP
{"idArea":"8"} {"idArea":"12","principal":true} {"idArea":"2"}

lsModelo [4] 
{"idAreaPersona":"5","idArea":"1", "personal":false} {"idAreaPersona":"6","idArea":"2","principal":true, "personal":false}, {"idAreaPersona":"8","idArea":"3", "personal":false} {"idAreaPersona":"9","idArea":"4", "personal":true}



{"idAreaPersona":"6","idArea":"2","personal":false}, {"idAreaPersona":"9","idArea":"8","personal":true}, {"idArea":"12","principal":true}

8) 3 nuevos vs 4 en BD (1 coincidente y es el principal) 1 P, 3 NP
{"idArea":"8"} {"idArea":"12","principal":true} {"idArea":"2"}

lsModelo [4] 
{"idAreaPersona":"5","idArea":"1", "personal":false} {"idAreaPersona":"6","idArea":"2","principal":true,"personal":false}, {"idAreaPersona":"8","idArea":"3", "personal":false} {"idAreaPersona":"9","idArea":"12", "personal":true}

==> {"idAreaPersona":"9","idArea":"12","principal":true,"personal":true}, {"idAreaPersona":"6","idArea":"2","personal":false}, {"idArea":"8"}




9) 3 nuevos sin principal y NInguno en BD
{"idArea":"8"} {"idArea":"12"} {"idArea":"2"}


{"idArea":"8"}, {"idArea":"12"}, {"idArea":"2"}




*/