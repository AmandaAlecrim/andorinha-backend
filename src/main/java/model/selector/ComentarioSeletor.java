package model.selector;

import java.util.Calendar;

public class ComentarioSeletor extends AbstractBaseSeletor{

	private Integer id;
	private String comentario;
	private Calendar data;
	private Integer idUsuario;
	private Integer idTweet;

	public boolean possuiFiltro() {
		return this.id != null || this.data != null
				|| this.idUsuario != null || this.idTweet != null || (this.comentario != null && !this.comentario.trim().isEmpty());
	}

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

}
