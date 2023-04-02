package it.prova.gestionecompagnia.test;

import java.sql.Connection;
import java.time.LocalDate;
import java.util.List;

import it.prova.gestionecompagnia.connection.MyConnection;
import it.prova.gestionecompagnia.dao.Constants;
import it.prova.gestionecompagnia.dao.compagnia.CompagniaDAO;
import it.prova.gestionecompagnia.dao.compagnia.CompagniaDAOImpl;
import it.prova.gestionecompagnia.dao.impiegato.ImpiegatoDAO;
import it.prova.gestionecompagnia.dao.impiegato.ImpiegatoDAOImpl;
import it.prova.gestionecompagnia.model.Compagnia;
import it.prova.gestionecompagnia.model.Impiegato;

public class TestCompagnia {

	public static void main(String[] args) {

		CompagniaDAO compagniaDAOInstance = null;
		ImpiegatoDAO impiegatoDAOInstance = null;

		// ##############################################################################################################
		try (Connection connection = MyConnection.getConnection(Constants.DRIVER_NAME, Constants.CONNECTION_URL)) {
			// ecco chi 'inietta' la connection: il chiamante
			compagniaDAOInstance = new CompagniaDAOImpl(connection);
			impiegatoDAOInstance = new ImpiegatoDAOImpl(connection);

			///// test CRUD base compagnia
//			System.out.println("In tabella compagnia ci sono " + compagniaDAOInstance.list().size() + " elementi.");
//			System.out.println(compagniaDAOInstance.list().toString());

//			System.out.println(compagniaDAOInstance.list().get(0).toString());

//			int compagnieAggiornate = compagniaDAOInstance.update(new Compagnia(1L,"bar",10,LocalDate.now(),null));
//			System.out.println(compagnieAggiornate);

//			testInsertCompagnia(compagniaDAOInstance);
//			System.out.println("In tabella compagnia ci sono " + compagniaDAOInstance.list().size() + " elementi.");

			// attenzione
//			testDeleteCompagnia(compagniaDAOInstance);
//			System.out.println("In tabella compagnia ci sono " + compagniaDAOInstance.list().size() + " elementi.");

///////////// test CRUD base impiegato  /////////////////////////////////////////////////////////////////////////////
//			System.out.println("In tabella compagnia ci sono " + impiegatoDAOInstance.list().size() + " elementi.");
//			System.out.println(impiegatoDAOInstance.list().toString());			

//			System.out.println(impiegatoDAOInstance.list().get(0).toString());

//			Compagnia compagniaForUp = new Compagnia();
//			compagniaForUp.setId(1L);
//			Impiegato impiegatoUp = new Impiegato(1L,"ale","mori","er888p",LocalDate.now(),LocalDate.now(), compagniaForUp);
//			int impiegatiAggiornati = impiegatoDAOInstance.update(impiegatoUp);
//			System.out.println(impiegatiAggiornati);

//			testInsertImpiegato(impiegatoDAOInstance);
			
			//eseguo la delete su db stando attento a non creare erroriok)
			/////impiegatoDAOInstance.delete(impiegatoDAOInstance.list().get(0));
			
			

			///// test metodi aggiuntivi compagnia
			
//			System.out.println(compagniaDAOInstance.findAllByDataAssunzioneMaggioreDi(LocalDate.parse("2001-02-02")));
			
			
//			System.out.println(compagniaDAOInstance.findAllByRagioneSocialeContiene("risto"));
			
//			System.out.println(compagniaDAOInstance.findAllBYCodFisImpiegatoContiene("150"));
			
			
			///// test metodi aggiuntivi impiegato
			
			
			
			//System.out.println(impiegatoDAOInstance.findAllByCompagnia(compagniaDAOInstance.list().get(0)));
			
			//System.out.println(impiegatoDAOInstance.countByDataFondazioneCompagniaGreaterThan(LocalDate.parse("2003-01-01")));
			
			//System.out.println(impiegatoDAOInstance.findAllByCompagniaConFatturatoMaggioreDi(10));
			
			//System.out.println(impiegatoDAOInstance.findAllErroriAssunzione());

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	////////// compagnia  //////////////////////////////////////////////////////////////////////////////
	////////// base//////////////////////////////////////////////////////////////////////////////////////////

//	
//	private static void testInsertCompagnia(CompagniaDAO compagniaDAOInstance) throws Exception {
//		System.out.println(".......testInsertCompagnia inizio.............");
//		int quantiElementiInseriti = compagniaDAOInstance
//				.insert(new Compagnia("edicola",50,LocalDate.now()));
//		if (quantiElementiInseriti < 1)
//			throw new RuntimeException("testInsertCompagnia : FAILED");
//
//		System.out.println(".......testInsertCompagnia fine: PASSED.............");
//	}
//	
//	private static void testDeleteCompagnia(CompagniaDAO compagniaDAOInstance) throws Exception {
//		System.out.println(".......testDeleteUser inizio.............");
//		// me ne creo uno al volo da mettere e togliere cosi il test è protetto
//		int quantiElementiInseriti = compagniaDAOInstance
//				.insert(new Compagnia("edicola",50,LocalDate.now()));
//		if (quantiElementiInseriti < 1)
//			throw new RuntimeException("testDeleteUser : FAILED, compagnia da rimuovere non inserita");
//
//		List<Compagnia> elencoVociPresenti = compagniaDAOInstance.list();
//		int numeroElementiPresentiPrimaDellaRimozione = elencoVociPresenti.size();
//		if (numeroElementiPresentiPrimaDellaRimozione < 1)
//			throw new RuntimeException("testDeleteCompagnia : FAILED, non ci sono voci sul DB");
//
//		Compagnia ultimoDellaLista = elencoVociPresenti.get(numeroElementiPresentiPrimaDellaRimozione - 1);
//		compagniaDAOInstance.delete(ultimoDellaLista);
//
//		// ricarico per vedere se sono scalati di una unità
//		int numeroElementiPresentiDopoDellaRimozione = compagniaDAOInstance.list().size();
//		if (numeroElementiPresentiDopoDellaRimozione != numeroElementiPresentiPrimaDellaRimozione - 1)
//			throw new RuntimeException("testDeleteCompagnia : FAILED, la rimozione non è avvenuta");
//
//		System.out.println(".......testDeleteCompagnia fine: PASSED.............");
//	}

	/////// impiegato
	/////// base////////////////////////////////////////////////////////////////////////
	private static void testInsertImpiegato(ImpiegatoDAO impiegatoDAOInstance) throws Exception {
		System.out.println(".......testInsertImpiegato inizio.............");
		Compagnia compagniaForInsert = new Compagnia();
		compagniaForInsert.setId(1L);
		int quantiElementiInseriti = impiegatoDAOInstance.insert(
				new Impiegato(1L, "milo", "rosa", "mr111o", LocalDate.now(), LocalDate.now(), compagniaForInsert));
		if (quantiElementiInseriti < 1)
			throw new RuntimeException("testInsertImpiegato : FAILED");
		System.out.println(".......testInsertImpiegato fine: PASSED.............");
	}

}
