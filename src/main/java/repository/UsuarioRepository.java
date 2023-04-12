package repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;

import model.Usuario;
import model.exceptions.ErroAoConectarNaBaseException;
import model.exceptions.ErroAoConsultarBaseException;
import model.selector.UsuarioSeletor;

@Stateless
public class UsuarioRepository extends AbstractCrudRepository {

	public void inserir(Usuario usuario) throws ErroAoConectarNaBaseException, ErroAoConsultarBaseException {

		// abrir uma conexão com o banco
		try (Connection c = super.ds.getConnection()) {

			// próximo valor da sequence
			int id = this.recuperarProximoValorDaSequence("seq_usuario");
			usuario.setId(id);

			// criar e executar o sql
			PreparedStatement ps = c.prepareStatement("insert into usuario (id, nome) values (?, ?)");
			ps.setInt(1, usuario.getId());
			ps.setString(2, usuario.getNome());
			ps.execute();
			ps.close();

		} catch (SQLException e) {
			throw new ErroAoConsultarBaseException("Ocorreu um erro ao inserir usuário", e);
		}
	}

	public void atualizar(Usuario usuario) throws ErroAoConectarNaBaseException, ErroAoConsultarBaseException {
		try (Connection c = super.ds.getConnection()) {
			PreparedStatement ps = c.prepareStatement("update usuario set nome = ? where id = ?");
			ps.setString(1, usuario.getNome());
			ps.setInt(2, usuario.getId());
			ps.execute();
			ps.close();
		} catch (SQLException e) {
			throw new ErroAoConsultarBaseException("Ocorreu um erro ao atualizar usuário", e);
		}
	}

	public void remover(int id) throws ErroAoConectarNaBaseException, ErroAoConsultarBaseException {
		try (Connection c = super.ds.getConnection()) {

			PreparedStatement ps = c.prepareStatement("delete from usuario where id = ?");
			ps.setInt(1, id);
			ps.execute();
			ps.close();

		} catch (SQLException e) {
			throw new ErroAoConsultarBaseException("Ocorreu um erro ao remover o usuário", e);
		}
	}

	public Usuario consultar(int id) throws ErroAoConectarNaBaseException, ErroAoConsultarBaseException {
		// abrir uma conexão com o banco
		try (Connection c = super.ds.getConnection()) {

			Usuario user = null;

			// criar e executar o sql
			PreparedStatement ps = c.prepareStatement("select id, nome from usuario where id = ?");
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				user = new Usuario();
				user.setId(rs.getInt("id"));
				user.setNome(rs.getString("nome"));
			}
			rs.close();
			ps.close();

			return user;

		} catch (SQLException e) {
			throw new ErroAoConsultarBaseException("Ocorreu um erro ao consultar usuário", e);
		}
	}

	public void criarFiltro(StringBuilder sql, UsuarioSeletor seletor) {
		if (seletor.possuiFiltro()) {
			sql.append("where ");
			boolean primeiro = false;

			if (seletor.getId() != null) {
				sql.append("id = ? ");
				primeiro = true;
			}
			if (seletor.getNome() != null && !seletor.getNome().trim().isEmpty()) {
				if (primeiro) {
					sql.append("and ");
				}
				sql.append("nome like ?");

			}
		}
	}

	public void adicionarParametro(PreparedStatement ps, UsuarioSeletor seletor) throws SQLException {
		int i = 1;

		if (seletor.possuiFiltro()) {
			if (seletor.getId() != null) {
				ps.setInt(i++, seletor.getId());
			}
			if (seletor.getNome() != null && !seletor.getNome().trim().isEmpty()) {
				ps.setString(i++, String.format("%%%s%%", seletor.getNome()));
			}
		}
	}

	public List<Usuario> pesquisar(UsuarioSeletor seletor) throws ErroAoConsultarBaseException, ErroAoConectarNaBaseException {
		try (Connection c = super.ds.getConnection()) {

			List<Usuario> users = new ArrayList<>();

			StringBuilder sql = new StringBuilder();
			sql.append("select id, nome from usuario ");
			
			this.criarFiltro(sql, seletor);
			
			PreparedStatement ps = c.prepareStatement( sql.toString() );
			
			this.adicionarParametro(ps, seletor);
			
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				users.add( this.criarModel(rs) );
			}
			rs.close();
			ps.close();

			return users;

		} catch (SQLException e) {
			throw new ErroAoConsultarBaseException("Ocorreu um erro ao listar todos os usuários", e);
		}
	}

	public Long contar(UsuarioSeletor seletor) throws ErroAoConsultarBaseException {
		try (Connection c = super.ds.getConnection()) {

			Long id = 0L;

			StringBuilder sql = new StringBuilder();

			sql.append("select count(id) as total from usuario ");
			
			this.criarFiltro(sql, seletor);

			PreparedStatement ps = c.prepareStatement(sql.toString());

			this.adicionarParametro(ps, seletor);

			ResultSet rs = ps.executeQuery();

			if (rs.next()) {
				id = rs.getLong("total");
			}
			rs.close();
			ps.close();

			return id;
		} catch (SQLException e) {
			throw new ErroAoConsultarBaseException("Ocorreu um erro ao listar os usuários", e);
		}
	}

	public List<Usuario> listarTodos() throws ErroAoConectarNaBaseException, ErroAoConsultarBaseException {
		return this.pesquisar(new UsuarioSeletor());
	}
	
	private Usuario criarModel(ResultSet rs) throws SQLException {
		Usuario user = new Usuario();
		user.setId(rs.getInt("id"));
		user.setNome(rs.getString("nome"));
		return user;
	}
}
