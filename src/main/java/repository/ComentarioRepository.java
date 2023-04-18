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
import javax.persistence.Query;

import java.util.ArrayList;

import model.Comentario;
import model.Usuario;
import model.Tweet;
import model.exceptions.ErroAoConectarNaBaseException;
import model.exceptions.ErroAoConsultarBaseException;
import model.selector.ComentarioSeletor;
import model.selector.UsuarioSeletor;

@Stateless
public class ComentarioRepository extends AbstractCrudRepository {

	public void inserir(Comentario comentario) {
		super.em.persist(comentario);
	}

	public Comentario consultar(int id) {
		return super.em.find(Comentario.class, id);
	}

	public void criarFiltro(StringBuilder sql, ComentarioSeletor seletor) {
		if (seletor.possuiFiltro()) {
			sql.append("where ");
			boolean primeiro = false;

			if (seletor.getId() != null) {
				sql.append("id = :id ");
				primeiro = true;
			}
			if (seletor.getIdUsuario() != null) {
				if (primeiro) {
					sql.append("and ");
				}
				sql.append("c.id_usuario = :id_usuario ");
			}
			if (seletor.getIdTweet() != null) {
				if (primeiro) {
					sql.append("and ");
				}
				sql.append("id_tweet = :id_tweet ");
			}
			if (seletor.getComentario() != null && !seletor.getComentario().trim().isEmpty()) {
				if (primeiro) {
					sql.append("and ");
				}
				sql.append("conteudo like :conteudo");
			}
			if (seletor.getData() != null) {
				if (primeiro) {
					sql.append("and ");
				}
				sql.append("data_postagem = :data_postagem ");
			}
		}
	}

	public void adicionarParametro(Query query, ComentarioSeletor seletor) {

		if (seletor.possuiFiltro()) {
			if (seletor.getId() != null) {
				query.setParameter("id", seletor.getId());
			}
			if (seletor.getIdUsuario() != null) {
				query.setParameter("id_usuario", seletor.getIdUsuario());
			}
			if (seletor.getIdTweet() != null) {
				query.setParameter("id_tweet", seletor.getIdTweet());
			}
			if (seletor.getComentario() != null && !seletor.getComentario().trim().isEmpty()) {
				query.setParameter("conteudo", String.format("%%%s%%", seletor.getComentario()));
			}
			if (seletor.getData() != null) {
				query.setParameter("data_postagem", new Timestamp(seletor.getData().getTimeInMillis()));
			}
		}
	}

	public List<Comentario> pesquisar(ComentarioSeletor seletor) {
		StringBuilder jpql = new StringBuilder();
		
		jpql.append("SELECT c FROM Comentario c ");
		jpql.append("INNER JOIN FETCH c.tweet t ");
		jpql.append("INNER JOIN FETCH c.usuario ");
		jpql.append("INNER JOIN FETCH t.usuario ");

		this.criarFiltro(jpql, seletor);

		Query query = super.em.createQuery(jpql.toString());

		this.adicionarParametro(query, seletor);

		return query.getResultList();
	}

	public Long contar(ComentarioSeletor seletor) throws ErroAoConsultarBaseException {

		StringBuilder jpql = new StringBuilder();
		jpql.append("SELECT COUNT(c) FROM Comentario c ");

		this.criarFiltro(jpql, seletor);

		Query query = super.em.createQuery(jpql.toString());

		this.adicionarParametro(query, seletor);

		return (Long) query.getSingleResult();
	}

	public List<Comentario> listarTodos() throws ErroAoConectarNaBaseException, ErroAoConsultarBaseException {
		return this.pesquisar(new ComentarioSeletor());
	}

	public void atualizar(Comentario comentario) throws ErroAoConectarNaBaseException, ErroAoConsultarBaseException {
		super.em.merge(comentario);
	}

	public void remover(int id) throws ErroAoConectarNaBaseException, ErroAoConsultarBaseException {
		Comentario comentario = this.consultar(id);
		super.em.remove(comentario);
	}
}
