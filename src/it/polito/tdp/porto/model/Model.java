package it.polito.tdp.porto.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.GraphPath;
import org.jgrapht.Graphs;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

import it.polito.tdp.porto.db.PortoDAO;

public class Model {
	
	private SimpleGraph<Author, DefaultEdge> grafo;
	private Map<Integer, Author> aIdMap;
	private List<Author> authors;
	private PortoDAO dao;
	
	public Model() {
		grafo= new SimpleGraph<Author, DefaultEdge>(DefaultEdge.class);
		dao= new PortoDAO();
		aIdMap= new HashMap<Integer,Author>();
	}
	
	public void createGraph() {
		authors= dao.getAllAuthors(aIdMap);
		Graphs.addAllVertices(grafo, authors);
		List<Coauthors> coautori= dao.getAllCoauthors();
		for(Coauthors c: coautori) {
			Author a1= this.aIdMap.get(c.getId1());
			Author a2= this.aIdMap.get(c.getId2());
			
			grafo.addEdge(a1, a2);
		}
	}
	public List<Author> trovaCoautori(Author a) {
		//Author a= this.aIdMap.get(id);
		return Graphs.neighborListOf(grafo, a);
	}

	public List<Author> getAuthors() {
		return authors;
	}
	
	public List<Author> getNotCoauthors(Author a){
		return dao.getNotCoauthors(a);
	}
	
	public List<Paper> getArticoliConnessione(Author partenza, Author arrivo){
		DijkstraShortestPath<Author, DefaultEdge> sp= new DijkstraShortestPath<>(this.grafo);
		GraphPath<Author, DefaultEdge> path= sp.getPath(partenza, arrivo);
		List<Paper> result= new ArrayList<Paper>();
		List<DefaultEdge> archiAttraversati= path.getEdgeList();
		for(DefaultEdge de: archiAttraversati) {
			Author a1= grafo.getEdgeSource(de);
			Author a2= grafo.getEdgeTarget(de);
			Paper p= dao.getArticoloComune(a1, a2);
			result.add(p);
		}
		return result;
	}
}
