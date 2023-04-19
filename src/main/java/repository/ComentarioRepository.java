package repository;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import model.Comentario;
import model.dto.ComentarioDTO;
import model.selector.ComentarioSeletor;
import repository.base.AbstractCrudRepository;

@Stateless
public class ComentarioRepository extends AbstractCrudRepository<Comentario> {

	public Comentario consultar(int id) {
		return super.em.find(Comentario.class, id);
	}

	public void criarFiltro(StringBuilder jpql, ComentarioSeletor seletor) {
		if (seletor.possuiFiltro()) {
			jpql.append("where ");
			boolean primeiro = false;

			if (seletor.getId() != null) {
				jpql.append("id = :id ");
				primeiro = true;
			}
			if (seletor.getIdUsuario() != null) {
				if (primeiro) {
					jpql.append("and ");
				}
				jpql.append("c.id_usuario = :id_usuario ");
			}
			if (seletor.getIdTweet() != null) {
				if (primeiro) {
					jpql.append("and ");
				}
				jpql.append("id_tweet = :id_tweet ");
			}
			if (seletor.getComentario() != null && !seletor.getComentario().trim().isEmpty()) {
				if (primeiro) {
					jpql.append("and ");
				}
				jpql.append("conteudo like :conteudo");
			}
			if (seletor.getData() != null) {
				if (primeiro) {
					jpql.append("and ");
				}
				jpql.append("data_postagem = :data_postagem ");
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
	
	public List<ComentarioDTO> pesquisarDTO(ComentarioSeletor seletor) {

		return super.createTupleQuery().select("id", "tweet.id as idTweet", "usuario.id as idUsuario", "usuario.nome as nomeUsuario", "data", "conteudo")
				.join("usuario").join("tweet").equal("id", seletor.getId()).list(ComentarioDTO.class);
	}
	
	public List<ComentarioDTO> pesquisarDTOold(ComentarioSeletor seletor) {
		StringBuilder jpql = new StringBuilder("SELECT new model.dto.ComentarioDTO( c.id, t.id, u.id, u.nome, c.data, c.conteudo ) ");
		
		jpql.append("FROM Comentario c ");
		jpql.append("INNER JOIN c.tweet t ");
		jpql.append("INNER JOIN c.usuario u ");
		jpql.append("INNER JOIN t.usuario ");
		this.criarFiltro(jpql, seletor);

		TypedQuery<ComentarioDTO> query = super.em.createQuery(jpql.toString(), ComentarioDTO.class);

		this.adicionarParametro(query, seletor);

		return query.getResultList();
	}

	public Long contar(ComentarioSeletor seletor) {

		StringBuilder jpql = new StringBuilder();
		jpql.append("SELECT COUNT(c) FROM Comentario c ");

		this.criarFiltro(jpql, seletor);

		Query query = super.em.createQuery(jpql.toString());

		this.adicionarParametro(query, seletor);

		return (Long) query.getSingleResult();
	}
}
