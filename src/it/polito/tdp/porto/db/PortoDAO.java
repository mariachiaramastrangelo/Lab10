package it.polito.tdp.porto.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.polito.tdp.porto.model.Author;
import it.polito.tdp.porto.model.Coauthors;
import it.polito.tdp.porto.model.Paper;

public class PortoDAO {

	/*
	 * Dato l'id ottengo l'autore.
	 */
	
	public List<Author> getAllAuthors(Map<Integer, Author> aIdMap){
		List<Author> result= new ArrayList<>();
		
		Connection conn= DBConnect.getConnection();
		String sql= "SELECT id, lastname, firstname " + 
				"FROM `author` ";
		try {
			PreparedStatement st= conn.prepareStatement(sql);
			ResultSet rs= st.executeQuery();
			while(rs.next()) {
				Author a= new Author(rs.getInt("id"), rs.getString("lastname"), rs.getString("firstname"));
				
				if(!aIdMap.containsKey(a.getId())) {
					result.add(a);
					aIdMap.put(a.getId(), a);
				}else {
					result.add(a);
				}
			}
			conn.close();
			return result;
			
			
		}catch(SQLException e) {
			throw new RuntimeException("errore nella connessione con il database");
		}
	}
	public List<Coauthors> getAllCoauthors(){
		List<Coauthors> result= new ArrayList<>();
		
		Connection conn= DBConnect.getConnection();
		String sql= "SELECT DISTINCT(c1.`authorid`) as id1, c2.`authorid` as id2 " + 
				"FROM creator c1, creator c2 " + 
				"WHERE c1.`eprintid`=c2.`eprintid` AND c1.`authorid`<c2.`authorid` ";
		try {
			PreparedStatement st= conn.prepareStatement(sql);
			ResultSet rs= st.executeQuery();
			while(rs.next()) {
				result.add(new Coauthors(rs.getInt("id1"), rs.getInt("id2")));
				
			}
			conn.close();
			return result;
			
			
		}catch(SQLException e) {
			throw new RuntimeException("errore nella connessione con il database");
		}
	
	}
	public List<Author> getNotCoauthors(Author a){
		{
			List<Author> result= new ArrayList<>();
			
			Connection conn= DBConnect.getConnection();
			String sql= "SELECT * " + 
					"FROM author a " + 
					"WHERE a.id NOT IN(SELECT DISTINCT (c2.`authorid`) as id2  " + 
					"FROM creator c1, creator c2  " + 
					"WHERE c1.`eprintid`=c2.`eprintid` AND c1.`authorid`<c2.`authorid` AND c1.`authorid`=? )";
			try {
				PreparedStatement st= conn.prepareStatement(sql);
				st.setInt(1, a.getId());
				ResultSet rs= st.executeQuery();
				while(rs.next()) {
					Author author= new Author(rs.getInt("id"), rs.getString("lastname"), rs.getString("firstname"));
					result.add(author);
				}
				conn.close();
				return result;
				
				
			}catch(SQLException e) {
				throw new RuntimeException("errore nella connessione con il database");
			}
		}
		
	}
	public Author getAutore(int id) {

		final String sql = "SELECT * FROM author where id=?";

		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, id);

			ResultSet rs = st.executeQuery();

			if (rs.next()) {

				Author autore = new Author(rs.getInt("id"), rs.getString("lastname"), rs.getString("firstname"));
				return autore;
			}

			return null;

		} catch (SQLException e) {
			// e.printStackTrace();
			throw new RuntimeException("Errore Db");
		}
	}

	/*
	 * Dato l'id ottengo l'articolo.
	 */
	public Paper getArticolo(int eprintid) {

		final String sql = "SELECT * FROM paper where eprintid=?";

		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, eprintid);

			ResultSet rs = st.executeQuery();

			if (rs.next()) {
				Paper paper = new Paper(rs.getInt("eprintid"), rs.getString("title"), rs.getString("issn"),
						rs.getString("publication"), rs.getString("type"), rs.getString("types"));
				return paper;
			}

			return null;

		} catch (SQLException e) {
			 e.printStackTrace();
			throw new RuntimeException("Errore Db");
		}
	}
	public Paper getArticoloComune(Author a1, Author a2) {
		String sql="SELECT * " + 
				"FROM paper p " + 
				"WHERE p.`eprintid` IN( " + 
				"SELECT c1.`eprintid` " + 
				"FROM creator c1, creator c2 " + 
				"WHERE c1.`eprintid`=c2.`eprintid` AND c1.`authorid`=? AND c2.`authorid`=?) ";
				try {
					Connection conn = DBConnect.getConnection();
					PreparedStatement st = conn.prepareStatement(sql);
					st.setInt(1, a1.getId());
					st.setInt(2, a2.getId());

					ResultSet rs = st.executeQuery();

					if (rs.next()) {
						Paper paper = new Paper(rs.getInt("eprintid"), rs.getString("title"), rs.getString("issn"),
								rs.getString("publication"), rs.getString("type"), rs.getString("types"));
						return paper;
					}

					return null;

				} catch (SQLException e) {
					 e.printStackTrace();
					throw new RuntimeException("Errore Db");
				}
	}
}