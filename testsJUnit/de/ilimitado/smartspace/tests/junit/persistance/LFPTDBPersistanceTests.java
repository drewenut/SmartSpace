package de.ilimitado.smartspace.tests.junit.persistance;

import java.util.ArrayList;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;

import de.ilimitado.smartspace.LFPT;
import de.ilimitado.smartspace.android.LFPTDBPersistance;
import de.ilimitado.smartspace.positioning.IGeoPoint;
import de.ilimitado.smartspace.tests.junit.config.MockConfigTranslator;

public class LFPTDBPersistanceTests extends TestCase{

	private LFPTDBPersistance dbPers;

	@Before
	public void setUp() {
		dbPers = new LFPTDBPersistance();
		MockConfigTranslator.getInstance().translate();
	}

	@Test
	public void testGetTableName() {
		assertEquals(LFPT.VALUE_MAP_DEFAULT_DATA, dbPers.getTableName());
	}

	@Test
	public void testGetSQLTableCreateQuery() {
		String tableCreateQuery = dbPers.getSQLTableCreateQuery();
		assertEquals("CREATE TABLE IF NOT EXISTS defaultData( ID STRING KEY, creationTime LONG, positionX INTEGER, positionY INTEGER, floor INTEGER, igp_name STRING, Orientation INTEGER )", tableCreateQuery);
	}

	@Test
	public void testGetSQLInsertQuery() {
		LFPT lfpt = new LFPT(123456789, 95, new IGeoPoint("name",3,7,1));
		ArrayList<String> insertQueryList = dbPers.getSQLInsertQuerys(lfpt);
		assertEquals(1, insertQueryList.size());
		String insertQuery = insertQueryList.get(0);
		assertEquals("INSERT INTO defaultData( ID, creationTime, positionX, positionY, floor, igp_name, Orientation  ) VALUES ( '3790', 123456789, 3, 7, 1, 'name', 90  )", insertQuery);
	}
	
	@Test
	public void testGetSQLUpdateQuery() {
		LFPT lfpt = new LFPT(123456789, 95, new IGeoPoint("name",3,7,1));
		ArrayList<String> updateQueryList = dbPers.getSQLUpdateQuerys(lfpt);
		assertEquals(1, updateQueryList.size());
		String updateQuery = updateQueryList.get(0);
		assertEquals("UPDATE defaultData SET creationTime=123456789, positionX=3, positionY=7, floor=1, igp_name='name', Orientation=90  WHERE ID='3790'", updateQuery);
	}

	@Test
	public void testGetSQLSelectQuery() {
		LFPT lfpt = new LFPT(123456789, 95, new IGeoPoint("name",3,7,1));
		String selectQuery = dbPers.getSQLSelectQuery(lfpt.getID());
		assertEquals("SELECT creationTime, positionX, positionY, floor, igp_name, Orientation  FROM defaultData WHERE ID='3790'", selectQuery);
	}

}
