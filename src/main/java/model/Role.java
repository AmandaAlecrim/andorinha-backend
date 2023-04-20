package model;

import java.security.Principal;

public class Role implements Principal{
	
	private String nome;

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}
	
	@Override
	public String getName() {
		return null;
	}

}
