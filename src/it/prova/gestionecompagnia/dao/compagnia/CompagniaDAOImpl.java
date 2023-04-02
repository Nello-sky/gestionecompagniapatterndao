package it.prova.gestionecompagnia.dao.compagnia;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import it.prova.gestionecompagnia.dao.AbstractMySQLDAO;
import it.prova.gestionecompagnia.model.Compagnia;

public class CompagniaDAOImpl extends AbstractMySQLDAO implements CompagniaDAO {

	// la connection stavolta fa parte del this, quindi deve essere 'iniettata'
	// dall'esterno
	public CompagniaDAOImpl(Connection connection) {
		super(connection);
	}

	//// metodi base
	public List<Compagnia> list() throws Exception {

		// prima di tutto cerchiamo di capire se possiamo effettuare le operazioni
		if (isNotActive())
			throw new Exception("Connessione non attiva. Impossibile effettuare operazioni DAO.");

		ArrayList<Compagnia> result = new ArrayList<Compagnia>();

		try (Statement ps = connection.createStatement(); ResultSet rs = ps.executeQuery("select * from compagnia")) {

			while (rs.next()) {
				Compagnia compagniaTemp = new Compagnia();
				compagniaTemp.setId(rs.getLong("id"));
				compagniaTemp.setRagioneSociale(rs.getString("ragionesociale"));
				compagniaTemp.setFatturatoAnnuo(rs.getInt("fatturatoannuo"));
				compagniaTemp.setDataFondazione(
						rs.getDate("datafondazione") != null ? rs.getDate("datafondazione").toLocalDate() : null);
				result.add(compagniaTemp);
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return result;

	}

	@Override
	public Compagnia get(Long idInput) throws Exception {
		// prima di tutto cerchiamo di capire se possiamo effettuare le operazioni
		if (isNotActive())
			throw new Exception("Connessione non attiva. Impossibile effettuare operazioni DAO.");

		if (idInput == null || idInput < 1)
			throw new Exception("Valore di input non ammesso.");

		Compagnia result = null;
		try (PreparedStatement ps = connection.prepareStatement("select * from user where id=?")) {

			ps.setLong(1, idInput);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					result = new Compagnia();
					result.setId(rs.getLong("id"));
					result.setRagioneSociale(rs.getString("ragionesociale"));
					result.setFatturatoAnnuo(rs.getInt("fatturatoannuo"));
					result.setDataFondazione(
							rs.getDate("datafondazione") != null ? rs.getDate("datafondazione").toLocalDate() : null);
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

	@Override
	public int update(Compagnia compagniaInput) throws Exception {
		// prima di tutto cerchiamo di capire se possiamo effettuare le operazioni
		if (isNotActive())
			throw new Exception("Connessione non attiva. Impossibile effettuare operazioni DAO.");

		if (compagniaInput == null || compagniaInput.getId() == null || compagniaInput.getId() < 1)
			throw new Exception("Valore di input non ammesso.");

		int result = 0;
		try (PreparedStatement ps = connection.prepareStatement(
				"UPDATE compagnia SET ragionesociale=?, fatturatoannuo=?, datafondazione=? where id=?;")) {
			ps.setString(1, compagniaInput.getRagioneSociale());
			ps.setInt(2, compagniaInput.getFatturatoAnnuo());
			ps.setDate(3, java.sql.Date.valueOf(compagniaInput.getDataFondazione()));
			ps.setLong(4, compagniaInput.getId());
			result = ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return result;
	}

	@Override
	public int insert(Compagnia compagniaInput) throws Exception {
		// prima di tutto cerchiamo di capire se possiamo effettuare le operazioni
		if (isNotActive())
			throw new Exception("Connessione non attiva. Impossibile effettuare operazioni DAO.");

		if (compagniaInput == null)
			throw new Exception("Valore di input non ammesso.");

		int result = 0;
		try (PreparedStatement ps = connection.prepareStatement(
				"INSERT INTO compagnia (ragionesociale, fatturatoannuo, datafondazione) VALUES (?, ?, ?);")) {
			ps.setString(1, compagniaInput.getRagioneSociale());
			ps.setInt(2, compagniaInput.getFatturatoAnnuo());
			ps.setDate(3, java.sql.Date.valueOf(compagniaInput.getDataFondazione()));
			result = ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return result;
	}

	@Override
	public int delete(Compagnia compagniaInput) throws Exception {
		// prima di tutto cerchiamo di capire se possiamo effettuare le operazioni
		if (isNotActive())
			throw new Exception("Connessione non attiva. Impossibile effettuare operazioni DAO.");

		if (compagniaInput == null || compagniaInput.getId() == null || compagniaInput.getId() < 1)
			throw new Exception("Valore di input non ammesso.");

		int result = 0;
		try (PreparedStatement ps = connection.prepareStatement("DELETE FROM compagnia WHERE ID=?")) {
			ps.setLong(1, compagniaInput.getId());
			result = ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return result;
	}

	@Override
	public List<Compagnia> findAllByDataAssunzioneMaggioreDi(LocalDate dateMin) throws Exception {

		// prima di tutto cerchiamo di capire se possiamo effettuare le operazioni
		if (isNotActive())
			throw new Exception("Connessione non attiva. Impossibile effettuare operazioni DAO.");

		if (dateMin == null)
			throw new Exception("Valore di data input non ammesso.");

		ArrayList<Compagnia> result = new ArrayList<Compagnia>();
		try (PreparedStatement ps = connection.prepareStatement(" select  \r\n"
				+ "    c.id as id_c,c.ragionesociale,c.fatturatoannuo,c.datafondazione\r\n" + "FROM\r\n"
				+ "    compagnia c\r\n" + "        INNER JOIN\r\n" + "    impiegato i ON c.id = i.id_compagnia\r\n"
				+ "    where i.dataassunzione > ? group by id_c; ")) {
			// attenzione
			ps.setDate(1, java.sql.Date.valueOf(dateMin));

			try (ResultSet rs = ps.executeQuery();) {
				while (rs.next()) {
					Compagnia compagniaTemp = new Compagnia();
					compagniaTemp.setId(rs.getLong("id_c"));
					compagniaTemp.setRagioneSociale(rs.getString("ragionesociale"));
					compagniaTemp.setFatturatoAnnuo(rs.getInt("fatturatoannuo"));
					compagniaTemp.setDataFondazione(
							rs.getDate("datafondazione") != null ? rs.getDate("datafondazione").toLocalDate() : null);
					result.add(compagniaTemp);
				}
			} // niente catch qui

		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return result;

	}

	@Override
	public List<Compagnia> findAllByRagioneSocialeContiene(String textInput) throws Exception {

		// prima di tutto cerchiamo di capire se possiamo effettuare le operazioni
		if (isNotActive())
			throw new Exception("Connessione non attiva. Impossibile effettuare operazioni DAO.");

		if (textInput == null || textInput.isBlank()) {
			throw new RuntimeException("non hai inserito nessun testo");
		}

		ArrayList<Compagnia> result = new ArrayList<Compagnia>();

		try (PreparedStatement ps = connection
				.prepareStatement("select *from compagnia c where c.ragionesociale like ?;")) {

			ps.setString(1, "%" + textInput + "%");

			try (ResultSet rs = ps.executeQuery();) {
				while (rs.next()) {
					Compagnia compagniaTemp = new Compagnia();
					compagniaTemp.setId(rs.getLong("id"));
					compagniaTemp.setRagioneSociale(rs.getString("ragionesociale"));
					compagniaTemp.setFatturatoAnnuo(rs.getInt("fatturatoannuo"));
					compagniaTemp.setDataFondazione(
							rs.getDate("datafondazione") != null ? rs.getDate("datafondazione").toLocalDate() : null);
					result.add(compagniaTemp);
				}
			} // niente catch qui

		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return result;

	}

	// findAllBYCodFisImpiegatoContiene
	public List<Compagnia> findAllBYCodFisImpiegatoContiene(String textInput) throws Exception {

		// prima di tutto cerchiamo di capire se possiamo effettuare le operazioni
		if (isNotActive())
			throw new Exception("Connessione non attiva. Impossibile effettuare operazioni DAO.");

		if (textInput == null || textInput.isBlank()) {
			throw new RuntimeException("non hai inserito nessun testo");
		}

		ArrayList<Compagnia> result = new ArrayList<Compagnia>();

		try (PreparedStatement ps = connection.prepareStatement(" select  \r\n"
				+ "    c.id as id_c, c.ragionesociale, c.fatturatoannuo, c.datafondazione\r\n" + "    FROM\r\n"
				+ "    compagnia c\r\n" + "        INNER JOIN\r\n" + "    impiegato i ON c.id = i.id_compagnia\r\n"
				+ "    where i.codicefiscale like ? group by id_c; ")) {

			ps.setString(1, "%" + textInput + "%");

			try (ResultSet rs = ps.executeQuery();) {
				while (rs.next()) {
					Compagnia compagniaTemp = new Compagnia();
					compagniaTemp.setId(rs.getLong("id_c"));
					compagniaTemp.setRagioneSociale(rs.getString("ragionesociale"));
					compagniaTemp.setFatturatoAnnuo(rs.getInt("fatturatoannuo"));
					compagniaTemp.setDataFondazione(
							rs.getDate("datafondazione") != null ? rs.getDate("datafondazione").toLocalDate() : null);
					result.add(compagniaTemp);
				}
			} // niente catch qui

		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return result;

	}

}
