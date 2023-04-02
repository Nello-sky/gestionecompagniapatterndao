package it.prova.gestionecompagnia.dao.impiegato;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import it.prova.gestionecompagnia.dao.AbstractMySQLDAO;
import it.prova.gestionecompagnia.model.Compagnia;
import it.prova.gestionecompagnia.model.Impiegato;

public class ImpiegatoDAOImpl extends AbstractMySQLDAO implements ImpiegatoDAO {

	// la connection stavolta fa parte del this, quindi deve essere 'iniettata'
	// dall'esterno
	public ImpiegatoDAOImpl(Connection connection) {
		super(connection);
	}

	//// metodi base
	public List<Impiegato> list() throws Exception {

		// prima di tutto cerchiamo di capire se possiamo effettuare le operazioni
		if (isNotActive())
			throw new Exception("Connessione non attiva. Impossibile effettuare operazioni DAO.");

		ArrayList<Impiegato> result = new ArrayList<Impiegato>();

		try (Statement ps = connection.createStatement();
				ResultSet rs = ps
						.executeQuery("select * from impiegato i inner join compagnia c on c.id=i.id_compagnia; ")) {

			while (rs.next()) {
				Compagnia compagniaTemp = new Compagnia();
				compagniaTemp.setId(rs.getLong("c.id"));
				compagniaTemp.setRagioneSociale(rs.getString("ragionesociale"));
				compagniaTemp.setFatturatoAnnuo(rs.getInt("fatturatoannuo"));
				compagniaTemp.setDataFondazione(
						rs.getDate("datafondazione") != null ? rs.getDate("datafondazione").toLocalDate() : null);

				Impiegato impiegatoTemp = new Impiegato();
				impiegatoTemp.setId(rs.getLong("i.id"));
				impiegatoTemp.setNome(rs.getString("nome"));
				impiegatoTemp.setCognome(rs.getString("cognome"));
				impiegatoTemp.setCodiceFiscale(rs.getString("codicefiscale"));
				impiegatoTemp.setDataNascita(
						rs.getDate("datanascita") != null ? rs.getDate("datanascita").toLocalDate() : null);
				impiegatoTemp.setDataAssunzione(
						rs.getDate("dataassunzione") != null ? rs.getDate("dataassunzione").toLocalDate() : null);
				impiegatoTemp.setCompagnia(compagniaTemp);

				result.add(impiegatoTemp);
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return result;

	}

	@Override
	public Impiegato get(Long idInput) throws Exception {
		// prima di tutto cerchiamo di capire se possiamo effettuare le operazioni
		if (isNotActive())
			throw new Exception("Connessione non attiva. Impossibile effettuare operazioni DAO.");

		if (idInput == null || idInput < 1)
			throw new Exception("Valore di input non ammesso.");

		Impiegato result = null;
		try (PreparedStatement ps = connection.prepareStatement("select * from impiegato where id=?")) {

			ps.setLong(1, idInput);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					result = new Impiegato();
					result.setId(rs.getLong("i.id"));
					result.setNome(rs.getString("nome"));
					result.setCognome(rs.getString("cognome"));
					result.setCodiceFiscale(rs.getString("codicefiscale"));
					result.setDataNascita(
							rs.getDate("datanascita") != null ? rs.getDate("datanascita").toLocalDate() : null);
					result.setDataAssunzione(
							rs.getDate("dataassunzione") != null ? rs.getDate("dataassunzione").toLocalDate() : null);
				} else {
					result = null;
				}
			} // niente catch qui

		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return result;
	}

//	public Impiegato getEager(Long idInput) throws Exception {
//		// prima di tutto cerchiamo di capire se possiamo effettuare le operazioni
//		if (isNotActive())
//			throw new Exception("Connessione non attiva. Impossibile effettuare operazioni DAO.");
//
//		if (idInput == null || idInput < 1)
//			throw new Exception("Valore di input non ammesso.");
//
//		Impiegato result = null;
//		try (PreparedStatement ps = connection.prepareStatement("select * from impiegato i inner join compagnia c on c.id=i.id_compagnia; ")) {
//
//			ps.setLong(1, idInput);
//			try (ResultSet rs = ps.executeQuery()) {
//				if (rs.next()) {
//					Compagnia compagniaTemp = new Compagnia();
//					compagniaTemp.setId(rs.getLong("c.id"));
//					compagniaTemp.setRagioneSociale(rs.getString("ragionesociale"));       
//					compagniaTemp.setFatturatoAnnuo(rs.getInt("fatturatoannuo"));
//					compagniaTemp.setDataFondazione(
//							rs.getDate("datafondazione") != null ? rs.getDate("datafondazione").toLocalDate() : null);
//					
//					result = new Impiegato();
//					result.setId(rs.getLong("i.id"));
//					result.setNome(rs.getString("nome"));    
//					result.setCognome(rs.getString("cognome"));  
//					result.setCodiceFiscale(rs.getString("codicefiscale"));  
//					result.setDataNascita(
//							rs.getDate("datanascita") != null ? rs.getDate("datanascita").toLocalDate() : null);
//					result.setDataAssunzione(
//							rs.getDate("dataassunzione") != null ? rs.getDate("dataassunzione").toLocalDate() : null);
//					result.setCompagnia(compagniaTemp);
//				} else {
//					result = null;
//				}
//			} // niente catch qui
//
//		} catch (Exception e) {
//			e.printStackTrace();
//			throw e;
//		}
//		return result;
//	}

	@Override
	public int update(Impiegato impiegatoInput) throws Exception {
		// prima di tutto cerchiamo di capire se possiamo effettuare le operazioni
		if (isNotActive())
			throw new Exception("Connessione non attiva. Impossibile effettuare operazioni DAO.");

		if (impiegatoInput == null || impiegatoInput.getId() == null || impiegatoInput.getId() < 1)
			throw new Exception("Valore di input non ammesso.");

		int result = 0;
		try (PreparedStatement ps = connection.prepareStatement(
				"UPDATE impiegato SET nome=?, cognome=?, codicefiscale=?, datanascita=?, dataassunzione=?, id_compagnia=? where id=?;")) {
			ps.setString(1, impiegatoInput.getNome());
			ps.setString(2, impiegatoInput.getCognome());
			ps.setString(3, impiegatoInput.getCodiceFiscale());
			ps.setDate(4, java.sql.Date.valueOf(impiegatoInput.getDataNascita()));
			ps.setDate(5, java.sql.Date.valueOf(impiegatoInput.getDataAssunzione()));
			ps.setLong(6, impiegatoInput.getCompagnia().getId());
			ps.setLong(7, impiegatoInput.getId());
			result = ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return result;
	}

	@Override
	public int insert(Impiegato impiegatoInput) throws Exception {
		// prima di tutto cerchiamo di capire se possiamo effettuare le operazioni
		if (isNotActive())
			throw new Exception("Connessione non attiva. Impossibile effettuare operazioni DAO.");

		if (impiegatoInput == null || impiegatoInput.getId() == null || impiegatoInput.getId() < 1)
			throw new Exception("Valore di input non ammesso.");

		int result = 0;
		try (PreparedStatement ps = connection.prepareStatement(
				"INSERT INTO impiegato (nome, cognome, codicefiscale, datanascita, dataassunzione, id_compagnia) VALUES (?, ?, ?, ?, ?, ?); ")) {
			ps.setString(1, impiegatoInput.getNome());
			ps.setString(2, impiegatoInput.getCognome());
			ps.setString(3, impiegatoInput.getCodiceFiscale());
			ps.setDate(4, java.sql.Date.valueOf(impiegatoInput.getDataNascita()));
			ps.setDate(5, java.sql.Date.valueOf(impiegatoInput.getDataAssunzione()));
			ps.setLong(6, impiegatoInput.getCompagnia().getId());

			result = ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return result;
	}

	@Override
	public int delete(Impiegato impiegatoInput) throws Exception {
		// prima di tutto cerchiamo di capire se possiamo effettuare le operazioni
		if (isNotActive())
			throw new Exception("Connessione non attiva. Impossibile effettuare operazioni DAO.");

		if (impiegatoInput == null || impiegatoInput.getId() == null || impiegatoInput.getId() < 1)
			throw new Exception("Valore di input non ammesso.");

		int result = 0;
		try (PreparedStatement ps = connection.prepareStatement("DELETE FROM impiegato WHERE ID=?")) {
			ps.setLong(1, impiegatoInput.getId());
			result = ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return result;
	}

//	findAllByCompagnia(Compagnia)

	public List<Impiegato> findAllByCompagnia(Compagnia compagniaInput) throws Exception {
		// prima di tutto cerchiamo di capire se possiamo effettuare le operazioni
		if (isNotActive())
			throw new Exception("Connessione non attiva. Impossibile effettuare operazioni DAO.");

		if (compagniaInput == null || compagniaInput.getId() == null || compagniaInput.getId() < 1)
			throw new Exception("Valore di input non ammesso.");

		List<Impiegato> result = new ArrayList<Impiegato>();
		try (PreparedStatement ps = connection
				.prepareStatement("select * from impiegato i where i.id_compagnia = ?; ")) {

			ps.setLong(1, compagniaInput.getId());
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					Impiegato temp = new Impiegato();
					temp.setId(rs.getLong("id"));
					temp.setNome(rs.getString("nome"));
					temp.setCognome(rs.getString("cognome"));
					temp.setCodiceFiscale(rs.getString("codicefiscale"));
					temp.setDataNascita(
							rs.getDate("datanascita") != null ? rs.getDate("datanascita").toLocalDate() : null);
					temp.setDataAssunzione(
							rs.getDate("dataassunzione") != null ? rs.getDate("dataassunzione").toLocalDate() : null);
					temp.setCompagnia(compagniaInput);
					result.add(temp);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			// rilancio in modo tale da avvertire il chiamante
			throw new RuntimeException(e);
		}
		return result;
	}

//	countByDataFondazioneCompagniaGreaterThan(data)
	public int countByDataFondazioneCompagniaGreaterThan(LocalDate dateInput) throws Exception {
		// prima di tutto cerchiamo di capire se possiamo effettuare le operazioni
		if (isNotActive())
			throw new Exception("Connessione non attiva. Impossibile effettuare operazioni DAO.");

		if (dateInput == null)
			throw new Exception("Valore di input non ammesso.");

		int result = 0;
		try (PreparedStatement ps = connection.prepareStatement(
				"SELECT \r\n" + "    count(i.id)\r\n" + "FROM\r\n" + "    impiegato i\r\n" + "        INNER JOIN\r\n"
						+ "    compagnia c ON c.id = i.id_compagnia\r\n" + "    where c.datafondazione > ?; ")) {

			ps.setDate(1, java.sql.Date.valueOf(dateInput));
			
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					result = rs.getInt(1);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return result;
	}
	
//	findAllByCompagniaConFatturatoMaggioreDi(int fatturatoInput)
	
	public List<Impiegato> findAllByCompagniaConFatturatoMaggioreDi(int fatturatoInput) throws Exception {
		// prima di tutto cerchiamo di capire se possiamo effettuare le operazioni
		if (isNotActive())
			throw new Exception("Connessione non attiva. Impossibile effettuare operazioni DAO.");



		List<Impiegato> result = new ArrayList<Impiegato>();
		try (PreparedStatement ps = connection
				.prepareStatement(" select  \r\n"
						+ "    i.*\r\n"
						+ "FROM\r\n"
						+ "    compagnia c\r\n"
						+ "        INNER JOIN\r\n"
						+ "    impiegato i ON c.id = i.id_compagnia\r\n"
						+ "    where c.fatturatoannuo > ?; ")) {

			ps.setInt(1, fatturatoInput);
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					Impiegato temp = new Impiegato();
					temp.setId(rs.getLong("id"));
					temp.setNome(rs.getString("nome"));
					temp.setCognome(rs.getString("cognome"));
					temp.setCodiceFiscale(rs.getString("codicefiscale"));
					temp.setDataNascita(
							rs.getDate("datanascita") != null ? rs.getDate("datanascita").toLocalDate() : null);
					temp.setDataAssunzione(
							rs.getDate("dataassunzione") != null ? rs.getDate("dataassunzione").toLocalDate() : null);
					
					result.add(temp);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			// rilancio in modo tale da avvertire il chiamante
			throw new RuntimeException(e);
		}
		return result;
	}
	
	//findAllErroriAssunzione()
	public List<Impiegato> findAllErroriAssunzione() throws Exception {

		// prima di tutto cerchiamo di capire se possiamo effettuare le operazioni
		if (isNotActive())
			throw new Exception("Connessione non attiva. Impossibile effettuare operazioni DAO.");

		ArrayList<Impiegato> result = new ArrayList<Impiegato>();

		try (Statement ps = connection.createStatement();
				ResultSet rs = ps
						.executeQuery("select  \r\n"
								+ "    i.*\r\n"
								+ "FROM\r\n"
								+ "    compagnia c\r\n"
								+ "        INNER JOIN\r\n"
								+ "    impiegato i ON c.id = i.id_compagnia\r\n"
								+ "    where i.datanascita > c.datafondazione or i.datanascita > i.dataassunzione or i.datanascita is null; ")) {

			while (rs.next()) {
				Impiegato impiegatoTemp = new Impiegato();
				impiegatoTemp.setId(rs.getLong("i.id"));
				impiegatoTemp.setNome(rs.getString("nome"));
				impiegatoTemp.setCognome(rs.getString("cognome"));
				impiegatoTemp.setCodiceFiscale(rs.getString("codicefiscale"));
				impiegatoTemp.setDataNascita(
						rs.getDate("datanascita") != null ? rs.getDate("datanascita").toLocalDate() : null);
				impiegatoTemp.setDataAssunzione(
						rs.getDate("dataassunzione") != null ? rs.getDate("dataassunzione").toLocalDate() : null);

				result.add(impiegatoTemp);
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return result;

	}
	
	
}
