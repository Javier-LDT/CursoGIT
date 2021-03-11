package com.bolsadeideas.springboot.form.app.services;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;

import com.bolsadeideas.springboot.form.app.models.domain.Pais;

@Service
public class PaisServiceImpl implements PaisService {

	private List<Pais> lista;
	
	public PaisServiceImpl() {
		this.lista = Arrays.asList(
					new Pais(1, "ES", "España"),
					new Pais(2, "MX","México"),
					new Pais(3, "CL","Chile"),
					new Pais(4, "BR","Brasil"),
					new Pais(5, "PR","Portugal"),
					new Pais(6, "CL","Colombia"),
					new Pais(7, "VN","Venezuela"));
		}

	@Override
	public List<Pais> Listar() {
		// TODO Auto-generated method stub
		return lista;
	}

	@Override
	public Pais obtenerPorId(Integer id) {
		Pais resultado = null;
		for(Pais pais:this.lista) {
			if(id == pais.getId()) {
				resultado= pais;
				break;
			}
		}
		return resultado;
	}

	



}
