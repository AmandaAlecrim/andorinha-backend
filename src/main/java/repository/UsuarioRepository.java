package repository;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;

import model.Usuario;
import model.selector.UsuarioSeletor;

@Stateless
public class UsuarioRepository extends AbstractCrudRepository {

	public void inserir(Usuario usuario) {
		super.em.persist(usuario);
	}

	public void atualizar(Usuario usuario) {
		super.em.merge(usuario);
	}

	public void remover(int id) {
		Usuario user = this.consultar(id);
		super.em.remove(user);
	}

	public Usuario consultar(int id) {
		return super.em.find(Usuario.class, id);
	}

	public void criarFiltro(StringBuilder jpql, UsuarioSeletor seletor) {
		if (seletor.possuiFiltro()) {
			jpql.append("WHERE ");
			boolean primeiro = false;

			if (seletor.getId() != null) {
				jpql.append("u.id = :id ");
				primeiro = true;
			}
			if (seletor.getNome() != null && !seletor.getNome().trim().isEmpty()) {
				if (primeiro) {
					jpql.append("and ");
				}
				jpql.append("u.nome LIKE :nome ");

			}
		}
	}

	public void adicionarParametro(Query query, UsuarioSeletor seletor) {

		if (seletor.possuiFiltro()) {
			if (seletor.getId() != null) {
				query.setParameter("id", seletor.getId());
			}
			if (seletor.getNome() != null && !seletor.getNome().trim().isEmpty()) {
				query.setParameter("nome", String.format("%%%s%%", seletor.getNome()));
			}
		}
	}

	public List<Usuario> pesquisar(UsuarioSeletor seletor) {
		StringBuilder jpql = new StringBuilder();
		jpql.append("SELECT u FROM Usuario u ");

		this.criarFiltro(jpql, seletor);

		Query query = super.em.createQuery(jpql.toString());

		this.adicionarParametro(query, seletor);

		return query.getResultList();
	}

	public Long contar(UsuarioSeletor seletor) {
		StringBuilder jpql = new StringBuilder();
		jpql.append("SELECT COUNT(u) FROM Usuario u ");

		this.criarFiltro(jpql, seletor);

		Query query = super.em.createQuery(jpql.toString());

		this.adicionarParametro(query, seletor);

		return (Long) query.getSingleResult();
	}

	public List<Usuario> listarTodos() {
		return this.pesquisar(new UsuarioSeletor());
	}
}
