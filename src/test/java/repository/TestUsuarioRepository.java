package repository;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import model.Usuario;
import runner.AndorinhaTestRunner;

@RunWith(AndorinhaTestRunner.class)
public class TestUsuarioRepository {
	
	private UsuarioRepository usuarioRepository;
	
	@Before
	public void setUp() {
		this.usuarioRepository = new UsuarioRepository();
	}
	
	@Test
	public void testa_se_usuario_foi_inserido() {
		Usuario user = new Usuario();
		user.setNome("Usuario do Teste de unidade");
		this.usuarioRepository.inserir(user);

		assertThat(user.getId() ).isGreaterThan(0);
	}
	@Test
	public void testa_consultar_usuario() {
		Usuario user = this.usuarioRepository.consultar(1);
		assertThat(user).isNotNull();
	}

}
