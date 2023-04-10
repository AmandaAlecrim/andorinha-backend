package repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import model.Comentario;
import model.Tweet;
import model.Usuario;
import model.exceptions.ErroAoConectarNaBaseException;
import model.exceptions.ErroAoConsultarBaseException;

public class TestComentarioRepository {

	private UsuarioRepository usuarioRepository;
	private TweetRepository tweetRepository;
	private ComentarioRepository comentarioRepository; 

	@Before
	public void setUp() {
		this.usuarioRepository = new UsuarioRepository();
		this.tweetRepository = new TweetRepository();
		this.comentarioRepository = new ComentarioRepository();
	}

	@Test
	public void test_consultar_comentario() throws ErroAoConsultarBaseException, ErroAoConectarNaBaseException {
		int idConsulta = 2;
		Comentario comentario = this.comentarioRepository.consultar(idConsulta);

		assertThat(comentario).isNotNull();
		assertThat(comentario.getConteudo()).isEqualTo("Comentário 2");
		assertThat(comentario.getId()).isEqualTo(idConsulta);
		assertThat(comentario.getUsuario()).isNotNull();
		assertThat(comentario.getTweet()).isNotNull();
		assertThat(comentario.getTweet().getUsuario()).isNotNull();
	}

	@Test
	public void testa_se_comentario_foi_inserido() throws ErroAoConectarNaBaseException, ErroAoConsultarBaseException {

		int idUsuarioConsulta = 2;
		int idTweetConsulta = 9;

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

	}
	
	@Test
	public void test_listar_todos_comentarios() throws ErroAoConectarNaBaseException, ErroAoConsultarBaseException {
		List<Comentario> comentarios = this.comentarioRepository.listarTodos();
		
		assertThat(comentarios).isNotNull().isNotEmpty();
	}
	
	@Test
	public void testa_alterar_comentario() throws ErroAoConectarNaBaseException, ErroAoConsultarBaseException {
		int idComentario = 11;
		
		Comentario c = this.comentarioRepository.consultar(idComentario);
		c.setConteudo("Comentário alterado");
		
		this.comentarioRepository.atualizar(c);
		
		Comentario alterado = this.comentarioRepository.consultar(idComentario);
		
		assertThat(alterado.getConteudo()).isEqualTo(c.getConteudo());
	}
	
	@Test
	public void testa_remover_comenatrio() throws ErroAoConectarNaBaseException, ErroAoConsultarBaseException {
		int idComentario = 11;
		
		Comentario c = this.comentarioRepository.consultar(idComentario);
		assertThat(c).isNotNull();
		
		this.comentarioRepository.remover(idComentario);
		
		Comentario removido = this.comentarioRepository.consultar(idComentario);
		assertThat(removido).isNull();
	}
	
}
