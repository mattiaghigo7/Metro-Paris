package it.polito.tdp.metroparis.model;

import java.util.*;
import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

import it.polito.tdp.metroparis.db.MetroDAO;

public class Model {

	private Graph<Fermata, DefaultEdge> grafo;
	private List<Fermata> fermate;
	private Map<Integer,Fermata> fermateIdMap;
	
	public void creaGrafo() {
		//crea l'oggetto grafo
		grafo = new SimpleGraph<>(DefaultEdge.class);
		
		//aggiungi i vertici
		MetroDAO dao = new MetroDAO();
		fermate = dao.readFermate();
		fermateIdMap = new HashMap<>();
		for(Fermata f : fermate) {
			this.fermateIdMap.put(f.getIdFermata(), f);
		}
		
		Graphs.addAllVertices(grafo, fermate);
		
		//aggiungi gli archi
		
		//metodo 1: considero tutti i potenziali archi, LENTO
//		long tic = System.currentTimeMillis();
//		for(Fermata partenza : grafo.vertexSet()) {
//			for(Fermata arrivo : grafo.vertexSet()) {
//				if(dao.isConnesse(partenza,arrivo)) {
//					grafo.addEdge(partenza, arrivo);
//				}
//			}
//		}
//		long tac = System.currentTimeMillis();
//		System.out.println("Elapsed time "+(tac-tic));
		
		//metodo 2: data una fermata, trova la lista di quelle adiacente, VELOCE
//		long tic = System.currentTimeMillis();
//		for(Fermata partenza : grafo.vertexSet()) {
//			List<Fermata> collegate = dao.trovaCollegate(partenza,fermateIdMap);
//			for(Fermata arrivo : collegate) {
//				grafo.addEdge(partenza, arrivo);
//			}
//		}
//		long tac = System.currentTimeMillis();
//		System.out.println("Elapsed time "+(tac-tic));
		
		//metodo 3: faccio una query per prendere tutti gli archi, PIU' VELOCE
//		tic = System.currentTimeMillis();
		List<coppieF> allCoppie = dao.getAllCoppie(fermateIdMap);
		for(coppieF c : allCoppie) {
			grafo.addEdge(c.partenza, c.arrivo);
		}
//		tac = System.currentTimeMillis();
//		System.out.println("Elapsed time "+(tac-tic));
		
		System.out.println("Grafo creato con "+grafo.vertexSet().size()+" vertici e "+grafo.edgeSet().size()+" archi");
	}
	
	public List<Fermata> getAllFermate(){
		MetroDAO dao = new MetroDAO();
		return dao.readFermate();
	}
	
	public boolean isGrafoLoaded() {
		return this.grafo.vertexSet().size()>0;
	}
}
