package it.polito.tdp.imdb.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.imdb.model.Actor;
import it.polito.tdp.imdb.model.Adj;
import it.polito.tdp.imdb.model.Director;
import it.polito.tdp.imdb.model.Movie;

public class ImdbDAO {
	
	public List<Actor> listAllActors(Map<Integer,Actor> idMap){
		String sql = "SELECT * FROM actors";
		List<Actor> result = new ArrayList<Actor>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				if(!idMap.containsKey(res.getInt("id"))) {
					Actor actor = new Actor(res.getInt("id"), res.getString("first_name"), res.getString("last_name"),
							res.getString("gender"));
					result.add(actor);
					idMap.put(actor.getId(), actor);
					
				}
						
				
				
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Movie> listAllMovies(){
		String sql = "SELECT * FROM movies";
		List<Movie> result = new ArrayList<Movie>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Movie movie = new Movie(res.getInt("id"), res.getString("name"), 
						res.getInt("year"), res.getDouble("rank"));
				
				result.add(movie);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	public List<Director> listAllDirectors(){
		String sql = "SELECT * FROM directors";
		List<Director> result = new ArrayList<Director>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Director director = new Director(res.getInt("id"), res.getString("first_name"), res.getString("last_name"));
				
				result.add(director);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<String> listGeneres(){
		String sql = "SELECT DISTINCT(mg.genre)\n" + 
				"FROM movies_genres mg\n" + 
				"ORDER BY mg.genre";
		List<String> result = new ArrayList<String>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {
				
				result.add(res.getString("mg.genre"));
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	public List<Actor> getVertici(String x,Map<Integer,Actor> idMap){
		String sql = "SELECT  Distinct(r.actor_id) as id  \n" + 
				"FROM movies_genres mg , roles r  \n" + 
				"WHERE mg.movie_id=r.movie_id\n" + 
				"AND mg.genre= ?\n" + 
				"ORDER BY id";
		List<Actor> result = new ArrayList<Actor>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, x);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				if(idMap.containsKey(res.getInt("id"))) {
					
					result.add(idMap.get(res.getInt("id")));
					
					
				}
						
				
				
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Adj> getArchi(String x,Map<Integer,Actor> idMap){
		String sql = "SELECT r1.actor_id AS id1, r2.actor_id AS id2 , COUNT((r1.movie_id)) AS weight\n" + 
				"FROM  movies_genres mg, roles r1, roles r2\n" + 
				"WHERE r1.actor_id> r2.actor_id\n" + 
				"AND mg.movie_id=r1.movie_id\n" + 
				"AND r1.movie_id= r2.movie_id\n" + 
				"AND mg.genre=?\n" + 
				"AND r1.movie_id=r2.movie_id\n" + 
				"GROUP BY id1, id2";
		List<Adj> result = new ArrayList<Adj>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, x);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				if(idMap.containsKey(res.getInt("id1")) && idMap.containsKey(res.getInt("id2"))) {
					Adj a = new Adj(idMap.get(res.getInt("id1")), idMap.get(res.getInt("id2")), res.getDouble("weight"));
					
					result.add(a );
					
					
				}
						
				
				
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	
}
