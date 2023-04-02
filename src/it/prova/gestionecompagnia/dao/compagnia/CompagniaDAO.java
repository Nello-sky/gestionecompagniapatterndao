package it.prova.gestionecompagnia.dao.compagnia;

import java.time.LocalDate;
import java.util.List;

import it.prova.gestionecompagnia.dao.IBaseDAO;
import it.prova.gestionecompagnia.model.Compagnia;

public interface CompagniaDAO extends IBaseDAO<Compagnia> {
	
	public List<Compagnia> findAllByDataAssunzioneMaggioreDi(LocalDate dateMin) throws Exception;

	public List<Compagnia> findAllByRagioneSocialeContiene(String textInput) throws Exception;

	public List<Compagnia> findAllBYCodFisImpiegatoContiene(String textInput) throws Exception;
}
