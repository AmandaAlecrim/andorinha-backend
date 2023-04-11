package model.selector;

import java.util.Calendar;

public class ComentarioSeletor {

	private Integer id;
	private String comentario;
	private Calendar data;
	private Integer idUsuario;
	private Integer idTweet;
	
	private Integer limite; //10
	private Integer pagina; //3
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getComentario() {
		return comentario;
	}
	public void setComentario(String comentario) {
		this.comentario = comentario;
	}
	public Calendar getData() {
		return data;
	}
	public void setData(Calendar data) {
		this.data = data;
	}
	public Integer getIdUsuario() {
		return idUsuario;
	}
	public void setIdUsuario(Integer idUsuario) {
		this.idUsuario = idUsuario;
	}
	public Integer getIdTweet() {
		return idTweet;
	}
	public void setIdTweet(Integer idTweet) {
		this.idTweet = idTweet;
	}
	public Integer getLimite() {
		return limite;
	}
	public void setLimite(Integer limite) {
		this.limite = limite;
	}
	public Integer getPagina() {
		return pagina;
	}
	public void setPagina(Integer pagina) {
		this.pagina = pagina;
	}
		
}
