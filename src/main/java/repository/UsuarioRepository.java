package repository;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.NoResultException;

import model.Usuario;
import model.selector.UsuarioSeletor;
import repository.base.AbstractCrudRepository;

@Stateless
public class UsuarioRepository extends AbstractCrudRepository<Usuario> {

	public Usuario login (String usuario, String senha) {
		try {
			return super.em.createQuery("SELECT u FROM Usuario u WHERE u.login LIKE :login AND u.senha LIKE :senha", Usuario.class)
			.setParameter("login", usuario)
			.setParameter("senha", senha)
			.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}
	
	public List<Usuario> pesquisar(UsuarioSeletor seletor) {
		return super.createEntityQuery()
				.equal("id", seletor.getId())
				.like("nome", seletor.getNome())
				.setFirstResult(seletor.getOffset())
				.setMaxResults(seletor.getLimite())
				.list();
	}

	public Long contar(UsuarioSeletor seletor) {
		return super.createCountQuery()
				.equal("id", seletor.getId())
				.like("nome", seletor.getNome())
				.count();
	}
}
