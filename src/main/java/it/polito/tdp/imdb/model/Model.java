package it.polito.tdp.imdb.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.alg.connectivity.ConnectivityInspector;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;



import it.polito.tdp.imdb.db.ImdbDAO;

public class Model {
	
	ImdbDAO DAO;
	Map<Integer,Actor> idMap;
	Graph<Actor, DefaultWeightedEdge> graph;
	List <Actor> attori;
	
	
	public Model() {
		DAO= new ImdbDAO();
	}

	public List<String>getGenere() {
		return DAO.listGeneres();
	}
	
	public void creaGrafo(String genere) {
		//Inizializzare mappa e grafo
		idMap = new HashMap<Integer, Actor>();
		
		graph = new SimpleWeightedGraph<Actor, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		//RIempire la lista
		DAO.listAllActors(idMap);
		//Aggiungere Vertici
		attori= DAO.getVertici(genere, idMap);
		Graphs.addAllVertices(graph,attori);
		//Aggiungere arhci
		for (Adj a  : DAO.getArchi(genere,idMap)) {
			if(graph.containsVertex(a.getA1()) && graph.containsVertex(a.getA2()) ) {
				Graphs.addEdgeWithVertices(graph, a.getA1(), a.getA2(), a.getWeight());
			}			
		}
		System.out.println("Grafo creato!");
		System.out.println("# VERTICI: " +  this.graph.vertexSet().size());
		System.out.println("# ARCHI: " +  this.graph.edgeSet().size());
	
	
}
	
	public  int getNumVertici() {
		return graph.vertexSet().size();
	}
	public int getArchi() {
		return graph.edgeSet().size();
	}
	
	public Set<Actor> listActor(){
		return  graph.vertexSet();
	}

	public List<Actor> getAttoriSimili(Actor a ) {
		ConnectivityInspector<Actor, DefaultWeightedEdge> ci= new ConnectivityInspector<Actor, DefaultWeightedEdge>(graph);
		List <Actor> actor= new ArrayList<Actor>(ci.connectedSetOf(a));
		actor.remove(a);
		Collections.sort(actor);
		return actor;
	}
	
}
