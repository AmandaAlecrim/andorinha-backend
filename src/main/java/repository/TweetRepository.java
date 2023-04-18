package repository;

import java.util.Calendar;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;

import model.Tweet;

@Stateless
public class TweetRepository extends AbstractCrudRepository {

	public void inserir(Tweet tweet) {
		tweet.setData(Calendar.getInstance());
		super.em.persist(tweet);
	}

	public Tweet consultar(int id) {
		return super.em.find(Tweet.class, id);
	}

	public List<Tweet> listarTodos() {

		StringBuilder jpql = new StringBuilder();

		jpql.append("SELECT t FROM Tweet t ");
		jpql.append("INNER JOIN FETCH t.usuario ");

		// jpql.append("select t.id, t.conteudo, t.data_postagem, t.id_usuario, u.nome
		// as nome_usuario from tweet t ");
		// jpql.append("join usuario u on t.id_usuario = u.id ");

		Query query = super.em.createQuery(jpql.toString());

		return query.getResultList();
	}

	public void atualizar(Tweet tweet) {
		super.em.merge(tweet);
	}

	public void remover(int id) {
		Tweet tweet = this.consultar(id);
		super.em.remove(tweet);
	}
}
