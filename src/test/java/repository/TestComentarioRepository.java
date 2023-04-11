package repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Calendar;
import java.util.List;

import javax.ejb.EJB;

import org.dbunit.operation.DatabaseOperation;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import model.Comentario;
import model.Tweet;
import model.Usuario;
import model.exceptions.ErroAoConectarNaBaseException;
import model.exceptions.ErroAoConsultarBaseException;
import runner.AndorinhaTestRunner;
import runner.DatabaseHelper;

@RunWith(AndorinhaTestRunner.class)
public class TestComentarioRepository {

	private static final int idTweetConsulta = 1;
	private static final int idComentarioConsulta = 1;
	private static final int idUsuarioConsulta = 1;

	private static final long deltaMilis = 500;

	@EJB
	private UsuarioRepository usuarioRepository;

	@EJB
	private TweetRepository tweetRepository;

	@EJB
	private ComentarioRepository comentarioRepository;

	@Before
	public void setUp() {
		// DatabaseHelper.getInstance("andorinhaDS").executeSqlScript("sql/prepare-database.sql");
		DatabaseHelper.getInstance("andorinhaDS").execute("dataset/andorinha.xml", DatabaseOperation.CLEAN_INSERT);
	}

	@Test
	public void testa_consultar_comentario() throws ErroAoConsultarBaseException, ErroAoConectarNaBaseException {

		Comentario comentario = this.comentarioRepository.consultar(idComentarioConsulta);

		assertThat(comentario).isNotNull();
		assertThat(comentario.getConteudo()).isEqualTo("Comentário 1");
		assertThat(comentario.getId()).isEqualTo(idComentarioConsulta);
		assertThat(comentario.getUsuario()).isNotNull();
		assertThat(comentario.getTweet()).isNotNull();
		assertThat(comentario.getTweet().getUsuario()).isNotNull();
	}

	@Test
	public void testa_se_comentario_foi_inserido() throws ErroAoConectarNaBaseException, ErroAoConsultarBaseException {

		Usuario user = this.usuarioRepository.consultar(idUsuarioConsulta);
		Tweet tweet = this.tweetRepository.consultar(idTweetConsulta);

		Comentario comentario = new Comentario();

		comentario.setConteudo("Comentario teste");
		comentario.setUsuario(user);
		comentario.setTweet(tweet);

		this.comentarioRepository.inserir(comentario);

		assertThat(comentario.getId()).isGreaterThan(0);

		Comentario inserido = this.comentarioRepository.consultar(comentario.getId());

		assertThat(inserido).isNotNull();
		assertThat(inserido.getConteudo()).isEqualTo(comentario.getConteudo());
		assertThat(Calendar.getInstance().getTime()).isCloseTo(inserido.getData().getTime(), deltaMilis);

	}

	@Test
	public void test_listar_todos_comentarios() throws ErroAoConectarNaBaseException, ErroAoConsultarBaseException {
		List<Comentario> comentarios = this.comentarioRepository.listarTodos();

		assertThat(comentarios).isNotNull().isNotEmpty();
	}

	@Test
	public void testa_alterar_comentario() throws ErroAoConectarNaBaseException, ErroAoConsultarBaseException {

		Comentario c = this.comentarioRepository.consultar(idComentarioConsulta);
		c.setConteudo("Comentário alterado");

		this.comentarioRepository.atualizar(c);

		Comentario alterado = this.comentarioRepository.consultar(idComentarioConsulta);

		assertThat(alterado.getConteudo()).isEqualTo(c.getConteudo());
		assertThat(Calendar.getInstance().getTime()).isCloseTo(alterado.getData().getTime(), deltaMilis);
	}

	@Test
	public void testa_remover_comenatrio() throws ErroAoConectarNaBaseException, ErroAoConsultarBaseException {

		Comentario c = this.comentarioRepository.consultar(idComentarioConsulta);
		assertThat(c).isNotNull();

		this.comentarioRepository.remover(idComentarioConsulta);

		Comentario removido = this.comentarioRepository.consultar(idComentarioConsulta);
		assertThat(removido).isNull();
	}

}
