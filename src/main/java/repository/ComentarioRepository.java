package repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import javax.ejb.Stateless;

import java.util.ArrayList;

import model.Comentario;
import model.Usuario;
import model.Tweet;
import model.exceptions.ErroAoConectarNaBaseException;
import model.exceptions.ErroAoConsultarBaseException;

@Stateless
public class ComentarioRepository extends AbstractCrudRepository{
	
public void inserir(Comentario comentario) throws ErroAoConectarNaBaseException, ErroAoConsultarBaseException {
	try (Connection c = super.ds.getConnection()) {

		int id = this.recuperarProximoValorDaSequence("seq_comentario");
		comentario.setId(id);
		
		Calendar hoje = Calendar.getInstance();
		
		PreparedStatement ps = c.prepareStatement("insert into comentario (id, conteudo, data_postagem, id_usuario, id_tweet) values (?, ?, ?, ?, ?)");
		ps.setInt(1, id);
		ps.setString(2, comentario.getConteudo());
		ps.setTimestamp(3, new Timestamp(hoje.getTimeInMillis()));
		ps.setInt(4, comentario.getUsuario().getId());
		ps.setInt(5, comentario.getTweet().getId());
		ps.execute();
		ps.close();
		
	} catch (SQLException e) {
		throw new ErroAoConsultarBaseException("Ocorreu um erro ao inserir o comentário", e);
	}
	}
	
	public Comentario consultar(int id) throws ErroAoConectarNaBaseException, ErroAoConsultarBaseException {
		try (Connection c = super.ds.getConnection()) {
			
			Comentario comentario = null;
			
			StringBuilder sql = new StringBuilder();
			sql.append("select c.id, c.conteudo, c.data_postagem, c.id_usuario, c.id_tweet, ");
			sql.append("u.nome as nome_usuario, t.conteudo as conteudo_tweet, t.data_postagem as data_postagem_tweet, ");
			sql.append("t.id_usuario as id_usuario_tweet, ut.nome as nome_usuario_tweet ");
			sql.append("from comentario c ");
			sql.append("join tweet t on c.id_tweet = t.id ");
			sql.append("join usuario u on c.id_usuario = u.id ");
			sql.append("join usuario ut on t.id_usuario = ut.id ");
			sql.append("where c.id = ? ");
			
			PreparedStatement ps = c.prepareStatement(sql.toString());
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();
			
			if (rs.next()) {
				Calendar dataTweet = new GregorianCalendar();
				dataTweet.setTime(rs.getTimestamp("data_postagem_tweet"));
				
				Calendar dataComentario = new GregorianCalendar();
				dataTweet.setTime(rs.getTimestamp("data_postagem"));
				
				Usuario userTweet = new Usuario();
				userTweet.setId(rs.getInt("id_usuario_tweet"));
				userTweet.setNome(rs.getString("nome_usuario_tweet"));
				
				Usuario userComentario = new Usuario();
				userComentario.setId(rs.getInt("id_usuario"));
				userComentario.setNome(rs.getString("nome_usuario"));
				
				Tweet tweet = new Tweet();
				tweet.setId(rs.getInt("id_tweet"));
				tweet.setTexto(rs.getString("conteudo_tweet"));
				tweet.setData(dataTweet);
				tweet.setUsuario(userTweet);
				
				comentario = new Comentario();
				comentario.setId(rs.getInt("id"));
				comentario.setConteudo(rs.getString("conteudo"));
				comentario.setData(dataComentario);
				comentario.setTweet(tweet);
				comentario.setUsuario(userComentario);				
			}
			rs.close();
			ps.close();
			
			return comentario;
		} catch (SQLException e) {
			throw new ErroAoConsultarBaseException("Ocorreu um erro ao consultar o comentário", e);
		}	
	}
	
	public List<Comentario> listarTodos() throws ErroAoConectarNaBaseException, ErroAoConsultarBaseException {
		try (Connection c = super.ds.getConnection()) {
			
			List<Comentario> comentarios = new ArrayList<>();
			
			StringBuilder sql = new StringBuilder();
			sql.append("select c.id, c.conteudo, c.data_postagem, c.id_usuario, c.id_tweet, ");
			sql.append("u.nome as nome_usuario, t.conteudo as conteudo_tweet, t.data_postagem as data_postagem_tweet, ");
			sql.append("t.id_usuario as id_usuario_tweet, ut.nome as nome_usuario_tweet ");
			sql.append("from comentario c ");
			sql.append("join tweet t on c.id_tweet = t.id ");
			sql.append("join usuario u on c.id_usuario = u.id ");
			sql.append("join usuario ut on t.id_usuario = ut.id ");
			
			PreparedStatement ps = c.prepareStatement(sql.toString());
			ResultSet rs = ps.executeQuery();
			
			while (rs.next()) {
				Calendar dataTweet = new GregorianCalendar();
				dataTweet.setTime(rs.getTimestamp("data_postagem_tweet"));
				
				Calendar dataComentario = new GregorianCalendar();
				dataComentario.setTime(rs.getTimestamp("data_postagem"));
				
				Usuario userTweet = new Usuario();
				userTweet.setId(rs.getInt("id_usuario_tweet"));
				userTweet.setNome(rs.getString("nome_usuario_tweet"));
				
				Usuario userComentario = new Usuario();
				userComentario.setId(rs.getInt("id_usuario"));
				userComentario.setNome(rs.getString("nome_usuario"));
				
				Tweet tweet = new Tweet();
				tweet.setId(rs.getInt("id_tweet"));
				tweet.setTexto(rs.getString("conteudo_tweet"));
				tweet.setData(dataTweet);
				tweet.setUsuario(userTweet);
				
				Comentario comentario = new Comentario();
				comentario.setId(rs.getInt("id"));
				comentario.setConteudo(rs.getString("conteudo"));
				comentario.setData(dataComentario);
				comentario.setTweet(tweet);
				comentario.setUsuario(userComentario);
				
				comentarios.add(comentario);
			}
			rs.close();
			ps.close();
			
			return comentarios;
		} catch (SQLException e) {
			throw new ErroAoConsultarBaseException("Ocorreu um erro ao listar todos os comentários", e);
		}		
	}
	
	public void atualizar (Comentario comentario) throws ErroAoConectarNaBaseException, ErroAoConsultarBaseException {
		try (Connection c = super.ds.getConnection()) {
			
			Calendar hoje = Calendar.getInstance();
			
			PreparedStatement ps = c.prepareStatement("update comentario set conteudo = ?, data_postagem = ? where id = ?");
			ps.setString(1, comentario.getConteudo());
			ps.setTimestamp(2, new Timestamp(hoje.getTimeInMillis()));
			ps.setInt(3, comentario.getId());
			ps.execute();
			ps.close();
			
		} catch (SQLException e) {
			throw new ErroAoConsultarBaseException("Ocorreu um erro ao atualizar o comentário", e);
		}
	}
	
	public void remover (int id) throws ErroAoConectarNaBaseException, ErroAoConsultarBaseException {
		try (Connection c = super.ds.getConnection()) {
			
			PreparedStatement ps = c.prepareStatement("delete from comentario where id = ?");
			ps.setInt(1, id);
			ps.execute();
			ps.close();
			
		} catch (SQLException e) {
			throw new ErroAoConsultarBaseException("Ocorreu um erro ao remover o comentário", e);
		}
	}
}
