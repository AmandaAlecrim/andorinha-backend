package repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;

import javax.ejb.EJB;
import javax.transaction.RollbackException;

import org.dbunit.operation.DatabaseOperation;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import model.Usuario;
import model.exceptions.ErroAoConectarNaBaseException;
import model.exceptions.ErroAoConsultarBaseException;
import model.selector.UsuarioSeletor;
import runner.AndorinhaTestRunner;
import runner.DatabaseHelper;

@RunWith(AndorinhaTestRunner.class)
public class TestUsuarioRepository {

	private static final int idUsuarioConsulta = 1;
	private static final int idUsuarioSemTweet = 5;

	@EJB
	private UsuarioRepository usuarioRepository;

	@Before
	public void setUp() {
		// DatabaseHelper.getInstance("andorinhaDS").executeSqlScript("sql/prepare-database.sql");
		DatabaseHelper.getInstance("andorinhaDS").execute("dataset/andorinha.xml", DatabaseOperation.CLEAN_INSERT);
	}

	@Test
	public void testa_se_usuario_foi_inserido(){
		Usuario user = new Usuario();
		user.setNome("Usuario do Teste de unidade");
		this.usuarioRepository.inserir(user);

		Usuario inserido = this.usuarioRepository.consultar(user.getId());

		assertThat(user.getId()).isGreaterThan(0);

		assertThat(inserido).isNotNull();
		assertThat(inserido.getNome()).isEqualTo(user.getNome());
		assertThat(inserido.getId()).isEqualTo(user.getId());

	}

	@Test
	public void testa_consultar_usuario() throws ErroAoConsultarBaseException, ErroAoConectarNaBaseException {

		Usuario user = this.usuarioRepository.consultar(idUsuarioConsulta);

		assertThat(user).isNotNull();
		assertThat(user.getNome()).isEqualTo("Usuário 1");
		assertThat(user.getId()).isEqualTo(idUsuarioConsulta);
	}

	@Test
	public void testa_alterar_usuario(){

		Usuario user = this.usuarioRepository.consultar(idUsuarioConsulta);
		user.setNome("Nome alterado");
		this.usuarioRepository.atualizar(user);
		Usuario alterado = this.usuarioRepository.consultar(idUsuarioConsulta);

		assertThat(alterado).isEqualToComparingFieldByField(user);
	}

	@Test
	public void testa_remover_usuario(){

		Usuario user = this.usuarioRepository.consultar(idUsuarioSemTweet);
		assertThat(user).isNotNull();

		this.usuarioRepository.remover(idUsuarioSemTweet);
		Usuario removido = this.usuarioRepository.consultar(idUsuarioSemTweet);
		assertThat(removido).isNull();

	}

	@Test
	public void testa_remover_usuario_com_tweet() {
		assertThatThrownBy(() -> {
			this.usuarioRepository.remover(idUsuarioConsulta);
		}).hasCauseInstanceOf(RollbackException.class);
	}

	@Test
	public void testa_listar_todos_usuarios(){
		List<Usuario> usuarios = this.usuarioRepository.listarTodos();

		assertThat(usuarios).isNotNull().isNotEmpty();
	}

	@Test
	public void testa_pesquisar_usuarios_por_nome() throws ErroAoConsultarBaseException, ErroAoConectarNaBaseException {
		UsuarioSeletor seletor = new UsuarioSeletor();
		seletor.setNome("Us");
		List<Usuario> usuarios = this.usuarioRepository.pesquisar(seletor);

		assertThat(usuarios).isNotNull().isNotEmpty();
	}

	@Test
	public void testa_contar_usuarios_por_nome() throws ErroAoConsultarBaseException {
		UsuarioSeletor seletor = new UsuarioSeletor();
		seletor.setNome("Usuário");
		Long total = this.usuarioRepository.contar(seletor);

		assertThat(total).isNotNull().isEqualTo(5L);
	}

	@Test
	public void testa_pesquisar_usuarios_por_id() throws ErroAoConsultarBaseException, ErroAoConectarNaBaseException {
		UsuarioSeletor seletor = new UsuarioSeletor();
		seletor.setId(2);

		List<Usuario> usuarios = this.usuarioRepository.pesquisar(seletor);

		assertThat(usuarios).isNotNull().isNotEmpty().hasSize(1);
	}
	@Test
	public void testa_consultar_usuario_trazerndo_tweets() throws ErroAoConectarNaBaseException, ErroAoConsultarBaseException {
		Usuario user = this.usuarioRepository.consultar(idUsuarioConsulta);
		
		assertThat( user ).isNotNull();
		assertThat( user.getNome() ).isEqualTo("Usuário 1");
		assertThat( user.getId() ).isEqualTo(idUsuarioConsulta);
		
		//assertThat( user.getTweets() ).isNotNull().isNotEmpty();
	}

}
