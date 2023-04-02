package it.prova.gestionecompagnia.dao.impiegato;

import java.time.LocalDate;
import java.util.List;

import it.prova.gestionecompagnia.dao.IBaseDAO;
import it.prova.gestionecompagnia.model.Compagnia;
import it.prova.gestionecompagnia.model.Impiegato;

public interface ImpiegatoDAO extends IBaseDAO<Impiegato> {

	public List<Impiegato> findAllByCompagnia(Compagnia compagniaInput) throws Exception;
	public int countByDataFondazioneCompagniaGreaterThan(LocalDate dateInput) throws Exception;
	public List<Impiegato> findAllByCompagniaConFatturatoMaggioreDi(int fatturatoInput) throws Exception ;
	public List<Impiegato> findAllErroriAssunzione() throws Exception;
}
