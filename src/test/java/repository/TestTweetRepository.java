package repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import model.Usuario;
import model.Tweet;
import model.exceptions.ErroAoConectarNaBaseException;
import model.exceptions.ErroAoConsultarBaseException;
import runner.AndorinhaTestRunner;

@RunWith(AndorinhaTestRunner.class)
public class TestTweetRepository {

	private UsuarioRepository usuarioRepository;
	private TweetRepository tweetRepository;

	@Before
	public void setUp() {
		this.usuarioRepository = new UsuarioRepository();
		this.tweetRepository = new TweetRepository();
	}

	@Test
	public void test_consultar_tweet() throws ErroAoConsultarBaseException, ErroAoConectarNaBaseException {
		int idTweetConsulta = 2;
		Tweet tweet = this.tweetRepository.consultar(idTweetConsulta);

		assertThat(tweet).isNotNull();
		assertThat(tweet.getTexto()).isEqualTo("Minha postagem de teste 2");
		assertThat(tweet.getId()).isEqualTo(idTweetConsulta);
		assertThat(tweet.getUsuario()).isNotNull();
	}

	@Test
	public void testa_se_tweet_foi_inserido() throws ErroAoConectarNaBaseException, ErroAoConsultarBaseException {

		int idUsuarioConsulta = 2;

		Usuario user = this.usuarioRepository.consultar(idUsuarioConsulta);

		Tweet tweet = new Tweet();

		tweet.setTexto("Post de teste a");
		tweet.setUsuario(user);
		this.tweetRepository.inserir(tweet);

		assertThat(tweet.getId()).isGreaterThan(0);

		Tweet inserido = this.tweetRepository.consultar(tweet.getId());

		assertThat(inserido).isNotNull();
		assertThat(inserido.getTexto()).isEqualTo(tweet.getTexto());

	}
	
	@Test
	public void testa_listar_todos_tweets() throws ErroAoConectarNaBaseException, ErroAoConsultarBaseException {
		List<Tweet> tweets = this.tweetRepository.listarTodos();
		
		assertThat(tweets).isNotNull().isNotEmpty();
		
	}
	
	@Test
	public void testa_alterar_tweet() throws ErroAoConsultarBaseException, ErroAoConectarNaBaseException {
		int idConsulta = 12 ;
		
		Tweet tweet = this.tweetRepository.consultar(idConsulta);
		tweet.setTexto("Post alterado");
		
		this.tweetRepository.atualizar(tweet);
		
		Tweet alterado = this.tweetRepository.consultar(idConsulta);
		
		assertThat(alterado.getTexto()).isEqualTo(tweet.getTexto());
	}
	
	@Test
	public void testa_remover_tweet() throws ErroAoConsultarBaseException, ErroAoConectarNaBaseException {
		int idConsulta = 12 ;
		
		Tweet tweet = this.tweetRepository.consultar(idConsulta);
		assertThat(tweet).isNotNull();
		
		this.tweetRepository.remover(idConsulta);
		
		Tweet removido = this.tweetRepository.consultar(idConsulta);
		assertThat(removido).isNull();
	}
}
