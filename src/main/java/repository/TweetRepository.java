package repository;

import java.util.Calendar;
import java.util.List;

import javax.ejb.Stateless;
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
		return this.em.createQuery("SELECT t from Tweet t").getResultList();
	}

	public void atualizar(Tweet tweet) {
		tweet.setData(Calendar.getInstance());
		super.em.merge(tweet);
	}

	public void remover(int id) {
		Tweet tweet = this.consultar(id);
		super.em.remove(tweet);
	}
}
