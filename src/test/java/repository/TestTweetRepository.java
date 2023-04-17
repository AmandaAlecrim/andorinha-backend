package repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Calendar;
import java.util.List;

import javax.ejb.EJB;

import org.dbunit.operation.DatabaseOperation;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import model.Usuario;
import model.Tweet;
import model.exceptions.ErroAoConectarNaBaseException;
import model.exceptions.ErroAoConsultarBaseException;
import runner.AndorinhaTestRunner;
import runner.DatabaseHelper;

@RunWith(AndorinhaTestRunner.class)
public class TestTweetRepository {

	private static final int idTweetConsulta = 2;
	private static final int idUsuarioConsulta = 2;

	private static final long deltaMilis = 500;

	@EJB
	private UsuarioRepository usuarioRepository;

	@EJB
	private TweetRepository tweetRepository;

	@Before
	public void setUp() {
		// DatabaseHelper.getInstance("andorinhaDS").executeSqlScript("sql/prepare-database.sql");
		DatabaseHelper.getInstance("andorinhaDS").execute("dataset/andorinha.xml", DatabaseOperation.CLEAN_INSERT);
	}

	@Test
	public void test_consultar_tweet() throws ErroAoConsultarBaseException, ErroAoConectarNaBaseException {

		Tweet tweet = this.tweetRepository.consultar(idTweetConsulta);

		assertThat(tweet).isNotNull();
		assertThat(tweet.getTexto()).isEqualTo("Minha postagem de teste 2");
		assertThat(tweet.getId()).isEqualTo(idTweetConsulta);
		assertThat(tweet.getUsuario()).isNotNull();
	}

	@Test
	public void testa_se_tweet_foi_inserido() throws ErroAoConectarNaBaseException, ErroAoConsultarBaseException {

		Usuario user = this.usuarioRepository.consultar(idUsuarioConsulta);

		Tweet tweet = new Tweet();

		tweet.setTexto("Post de teste a");
		tweet.setUsuario(user);
		this.tweetRepository.inserir(tweet);

		assertThat(tweet.getId()).isGreaterThan(0);

		Tweet inserido = this.tweetRepository.consultar(tweet.getId());

		assertThat(inserido).isNotNull();
		assertThat(inserido.getTexto()).isEqualTo(tweet.getTexto());
		assertThat(Calendar.getInstance().getTime()).isCloseTo(inserido.getData().getTime(), deltaMilis);

	}

	@Test
	public void testa_listar_todos_tweets() throws ErroAoConectarNaBaseException, ErroAoConsultarBaseException {
		List<Tweet> tweets = this.tweetRepository.listarTodos();

		assertThat(tweets).isNotNull().isNotEmpty();

	}

	@Test
	public void testa_alterar_tweet() throws ErroAoConsultarBaseException, ErroAoConectarNaBaseException {

		Tweet tweet = this.tweetRepository.consultar(idTweetConsulta);
		tweet.setTexto("Post alterado");

		this.tweetRepository.atualizar(tweet);

		Tweet alterado = this.tweetRepository.consultar(idTweetConsulta);

		assertThat(alterado.getTexto()).isEqualTo(tweet.getTexto());
		assertThat(Calendar.getInstance().getTime());
		//.isCloseTo(alterado.getData().getTime(), deltaMilis); 
	}

	@Test
	public void testa_remover_tweet() throws ErroAoConsultarBaseException, ErroAoConectarNaBaseException {

		Tweet tweet = this.tweetRepository.consultar(idTweetConsulta);
		assertThat(tweet).isNotNull();

		this.tweetRepository.remover(idTweetConsulta);

		Tweet removido = this.tweetRepository.consultar(idTweetConsulta);
		assertThat(removido).isNull();
	}
}
