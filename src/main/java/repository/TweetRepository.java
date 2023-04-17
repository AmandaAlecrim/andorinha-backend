package repository;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import model.Tweet;
import model.Usuario;
import model.exceptions.ErroAoConectarNaBaseException;
import model.exceptions.ErroAoConsultarBaseException;

@Stateless
public class TweetRepository extends AbstractCrudRepository {

	public void inserir(Tweet tweet) throws ErroAoConectarNaBaseException, ErroAoConsultarBaseException {
		tweet.setData(Calendar.getInstance());
		super.em.persist(tweet);
	}

	public Tweet consultar(int id) throws ErroAoConsultarBaseException, ErroAoConectarNaBaseException {
		return super.em.find(Tweet.class, id);
	}

	public List<Tweet> listarTodos() throws ErroAoConectarNaBaseException, ErroAoConsultarBaseException {
		try (Connection c = super.ds.getConnection()) {
			
			List<Tweet> tweets = new ArrayList<>();			
			StringBuilder sql = new StringBuilder();
			
			sql.append("select t.id, t.conteudo, t.data_postagem, t.id_usuario, u.nome as nome_usuario from tweet t ");
			sql.append("join usuario u on t.id_usuario = u.id ");
			
			PreparedStatement ps = c.prepareStatement(sql.toString());
			ResultSet rs = ps.executeQuery();
			
			while (rs.next()) {
				Tweet tweet = new Tweet();
				
				tweet.setId(rs.getInt("id"));
				tweet.setTexto(rs.getString("conteudo"));

				Calendar data = new GregorianCalendar();
				data.setTime( rs.getTimestamp("data_postagem") );
				tweet.setData(data);
				
				Usuario user = new Usuario();
				user.setId(rs.getInt("id_usuario"));
				user.setNome(rs.getString("nome_usuario"));
				tweet.setUsuario(user);
				
				tweets.add(tweet);				
			}
			rs.close();
			ps.close();
			
			return tweets;
		} catch (SQLException e) {
			throw new ErroAoConsultarBaseException("Ocorreu um erro ao consultar todos os tweets", e);
		}
	}

	public void atualizar(Tweet tweet) throws ErroAoConectarNaBaseException, ErroAoConsultarBaseException {
		super.em.merge(tweet);
	}

	public void remover(int id) throws ErroAoConectarNaBaseException, ErroAoConsultarBaseException {
		Tweet tweet = this.consultar(id);
		super.em.remove(tweet);
	}
}
