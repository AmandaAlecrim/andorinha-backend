package repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import model.Usuario;
import model.exceptions.ErroAoConectarNaBaseException;
import model.exceptions.ErroAoConsultarBaseException;
import runner.AndorinhaTestRunner;

@RunWith(AndorinhaTestRunner.class)
public class TestUsuarioRepository {

	private UsuarioRepository usuarioRepository;

	@Before
	public void setUp() {
		this.usuarioRepository = new UsuarioRepository();
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

		int idConsulta = 6;
		Usuario user = this.usuarioRepository.consultar(idConsulta);

		assertThat(user).isNotNull();
		assertThat(user.getNome()).isEqualTo("Usuario do Teste de unidade");
		assertThat(user.getId()).isEqualTo(idConsulta);
	}

	@Test
	public void testa_alterar_usuario() throws ErroAoConectarNaBaseException, ErroAoConsultarBaseException {
		int idConsulta = 5;
		Usuario user = this.usuarioRepository.consultar(idConsulta);
		user.setNome("Nome alterado");
		this.usuarioRepository.atualizar(user);
		Usuario alterado = this.usuarioRepository.consultar(idConsulta);

		assertThat(alterado).isEqualToComparingFieldByField(user);
	}

	@Test
	public void testa_remover_usuario() throws ErroAoConectarNaBaseException, ErroAoConsultarBaseException {
		int idConsulta = 6;
		Usuario user = this.usuarioRepository.consultar(idConsulta);
		assertThat(user).isNotNull();

		this.usuarioRepository.remover(idConsulta);
		Usuario removido = this.usuarioRepository.consultar(idConsulta);
		assertThat(removido).isNull();

	}

	@Test
	public void testa_listar_todos_usuarios() throws ErroAoConectarNaBaseException, ErroAoConsultarBaseException {
		List<Usuario> usuarios = this.usuarioRepository.listarTodos();

		assertThat(usuarios).isNotNull().isNotEmpty();
	}

}
