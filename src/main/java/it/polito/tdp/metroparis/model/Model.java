package it.polito.tdp.metroparis.model;

import java.util.*;
import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.Graphs;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleGraph;
import org.jgrapht.graph.SimpleWeightedGraph;
import org.jgrapht.traverse.BreadthFirstIterator;

import com.javadocmd.simplelatlng.LatLngTool;
import com.javadocmd.simplelatlng.util.LengthUnit;

import it.polito.tdp.metroparis.db.MetroDAO;

public class Model {

	private Graph<Fermata, DefaultWeightedEdge> grafo;
	private List<Fermata> fermate;
	private Map<Integer,Fermata> fermateIdMap;
	
	public void creaGrafo() {
		//crea l'oggetto grafo
		grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		
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
			double distanza = LatLngTool.distance(c.partenza.getCoords(), c.getArrivo().getCoords(), LengthUnit.METER);
			Graphs.addEdge(this.grafo, c.partenza, c.arrivo, distanza);
		}
//		tac = System.currentTimeMillis();
//		System.out.println("Elapsed time "+(tac-tic));
		
		System.out.println("Grafo creato con "+grafo.vertexSet().size()+" vertici e "+grafo.edgeSet().size()+" archi");
	}
	
	//determina il percorso minimo tra le 2 fermate
	public List<Fermata> percorso(Fermata partenza, Fermata arrivo) {
//		//visita il grafo partendo da "partenza"
//		BreadthFirstIterator<Fermata,DefaultWeightedEdge> visita = new BreadthFirstIterator<>(grafo,partenza);
//		List<Fermata> raggiungibili = new ArrayList<>();
//		while(visita.hasNext()) {
//			Fermata f = visita.next();
////			raggiungibili.add(f);
//		}
////		System.out.println(raggiungibili);
//		
//		//Trova il percorso sull'albero di visita
//		List<Fermata> percorso = new ArrayList<>();
//		Fermata corrente = arrivo;
//		percorso.add(arrivo);
//		DefaultWeightedEdge e = visita.getSpanningTreeEdge(corrente);
//		while(e!=null) {
//			Fermata precedente = Graphs.getOppositeVertex(grafo, e, corrente);
//			percorso.add(0,precedente);
//			corrente = precedente;
//			e = visita.getSpanningTreeEdge(corrente);
//		}
		DijkstraShortestPath<Fermata, DefaultWeightedEdge> sp = new DijkstraShortestPath<>(grafo);
		GraphPath<Fermata,DefaultWeightedEdge> gp = sp.getPath(partenza, arrivo);
		
		return gp.getVertexList();
	}
	
	public List<Fermata> getAllFermate(){
		MetroDAO dao = new MetroDAO();
		return dao.readFermate();
	}
	
	public boolean isGrafoLoaded() {
		return this.grafo.vertexSet().size()>0;
	}
}
