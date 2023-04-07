package repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import model.Usuario;
import model.exceptions.ErroAoConectarNaBaseException;
import model.exceptions.ErroAoConsultarBaseException;

public class UsuarioRepository extends AbstractCrudRepository{
	
	public void inserir(Usuario usuario) throws ErroAoConectarNaBaseException, ErroAoConsultarBaseException {
		
		//abrir uma conexão com o banco
		try ( Connection c = this.abrirConexao() ){
			
			//próximo valor da sequence
			int id = this.recuperarProximoValorDaSequence("seq_usuario");
			usuario.setId(id);
			
			//criar e executar o sql
			PreparedStatement ps = c.prepareStatement("insert into usuario (id, nome) values (?, ?)");
			ps.setInt(1, usuario.getId());
			ps.setString(2, usuario.getNome());
			ps.execute();			
			ps.close();
			
		} catch (SQLException e) {
			throw new ErroAoConsultarBaseException("Ocorreu um erro ao inserir usuário", e);
		}
	}
	public void atualizar(Usuario usuario) {
		
	}
	public void remover(int id) {
		
	}
	public Usuario consultar(int id) {
		return null;
	}
	public List<Usuario> listarTodos() {
		return null;
	}
}
