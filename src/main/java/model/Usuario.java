package model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "usuario")
public class Usuario {

	@Id
	@SequenceGenerator(name = "seq_usuario", sequenceName = "seq_usuario", initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_usuario")
	@Column(name = "id")
	private int id;

	@Column(name = "nome")
	private String nome;

	// OneToMany pode ser utilizado mas não é necessário e nem recomendado devido a
	// grande quantidade de itens que são mostrados nas queries do JPA
	@OneToMany(mappedBy = "usuario", fetch = FetchType.EAGER)
	List<Tweet> tweets;

	@Override
	public String toString() {
		return "Usuário [id=" + id + ", nome =" + nome + "]";
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public List<Tweet> getTweets() {
		return tweets;
	}

	public void setTweets(List<Tweet> tweets) {
		this.tweets = tweets;
	}

}
