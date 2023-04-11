package repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;

import javax.ejb.EJB;

import org.dbunit.operation.DatabaseOperation;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import model.Usuario;
import model.exceptions.ErroAoConectarNaBaseException;
import model.exceptions.ErroAoConsultarBaseException;
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
	public void testa_se_usuario_foi_inserido() throws ErroAoConectarNaBaseException, ErroAoConsultarBaseException {
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
	public void testa_alterar_usuario() throws ErroAoConectarNaBaseException, ErroAoConsultarBaseException {

		Usuario user = this.usuarioRepository.consultar(idUsuarioConsulta);
		user.setNome("Nome alterado");
		this.usuarioRepository.atualizar(user);
		Usuario alterado = this.usuarioRepository.consultar(idUsuarioConsulta);

		assertThat(alterado).isEqualToComparingFieldByField(user);
	}

	@Test
	public void testa_remover_usuario() throws ErroAoConectarNaBaseException, ErroAoConsultarBaseException {

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
		}).isInstanceOf(ErroAoConsultarBaseException.class)
				.hasMessageContaining("Ocorreu um erro ao remover o usuário");
	}

	@Test
	public void testa_listar_todos_usuarios() throws ErroAoConectarNaBaseException, ErroAoConsultarBaseException {
		List<Usuario> usuarios = this.usuarioRepository.listarTodos();

		assertThat(usuarios).isNotNull().isNotEmpty();
	}

}
