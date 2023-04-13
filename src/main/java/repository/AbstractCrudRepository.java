package repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;

import model.exceptions.ErroAoConectarNaBaseException;
import model.exceptions.ErroAoConsultarBaseException;

public abstract class AbstractCrudRepository {
	
	@Resource(name = "andorinhaDS")
	protected DataSource ds;
	
	@PersistenceContext
	protected EntityManager em;
	
	protected int recuperarProximoValorDaSequence (String nomeSequence) throws ErroAoConectarNaBaseException, ErroAoConsultarBaseException {
		//abrir uma conexão com o banco
		try ( Connection c = this.ds.getConnection() ){

			//recuperar próximo valor do id
			PreparedStatement ps = c.prepareStatement("select nextval(?)");
			ps.setString(1, nomeSequence);
			ResultSet rs = ps.executeQuery();
			if(rs.next()) {
				return rs.getInt(1);
			}
			
			throw new ErroAoConsultarBaseException("Erro ao recuperar próximo valor da sequence" + nomeSequence, null);
			
		} catch (SQLException e) {
			throw new ErroAoConectarNaBaseException("Ocorreu um erro ao acesar a base de dados", e);
		}
	}
}
