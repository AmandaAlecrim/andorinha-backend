package repository;

import java.util.List;

import javax.ejb.Stateless;
import model.Comentario;
import model.dto.ComentarioDTO;
import model.selector.ComentarioSeletor;
import repository.base.AbstractCrudRepository;

@Stateless
public class ComentarioRepository extends AbstractCrudRepository<Comentario> {

	
	public List<Comentario> pesquisar(ComentarioSeletor seletor) {
		
		return super.createEntityQuery()
				.innerJoinFetch("usuario")
				.innerJoinFetch("tweet")
				.equal("id", seletor.getId())
				.like("conteudo", seletor.getComentario())
				.equal("data", seletor.getData())
				.equal("usuario.id", seletor.getIdUsuario())
				.equal("tweet.id", seletor.getIdUsuario())
				.setFirstResult(seletor.getOffset())
				.setMaxResults(seletor.getLimite())
				.list();
	}
	
	public List<ComentarioDTO> pesquisarDTO(ComentarioSeletor seletor) {

		return super.createTupleQuery().select("id", "tweet.id as idTweet", "usuario.id as idUsuario", "usuario.nome as nomeUsuario", "data", "conteudo")
				.join("usuario").join("tweet").equal("id", seletor.getId()).list(ComentarioDTO.class);
	}

	public Long contar(ComentarioSeletor seletor) {

		return super.createCountQuery()
				.innerJoinFetch("usuario")
				.innerJoinFetch("tweet")
				.equal("id", seletor.getId())
				.like("conteudo", seletor.getComentario())
				.equal("data", seletor.getData())
				.equal("usuario.id", seletor.getIdUsuario())
				.equal("tweet.id", seletor.getIdUsuario())
				.setFirstResult(seletor.getOffset())
				.setMaxResults(seletor.getLimite())
				.count();
	}
}
