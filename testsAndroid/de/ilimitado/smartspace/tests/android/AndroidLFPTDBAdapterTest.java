package de.ilimitado.smartspace.tests.android;

import android.content.ContentValues;
import android.database.Cursor;
import android.test.AndroidTestCase;
import de.ilimitado.smartspace.android.AndroidLFPTDBAdapter;

public class AndroidLFPTDBAdapterTest extends AndroidTestCase{

	private AndroidLFPTDBAdapter adapter;

	public void setUp() throws Exception {
		String creationString = "CREATE TABLE IF NOT EXISTS TestTable(intRow int, strRow varchar(100));";
		adapter = new AndroidLFPTDBAdapter(getContext(), "TestCases", "TestTable", creationString);
		adapter.open();
	}
	
	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
		adapter.close();
		getContext().deleteDatabase("TestCases"); 
	}

	public void testExecSQL() {
		
		Cursor cursor = adapter.execSQL("INSERT INTO TestTable (intRow, strRow) VALUES (5, 'testString')");		
		assertNotNull(cursor);
		
		cursor = adapter.execSQL("SELECT * FROM TestTable");
		
		assertNotNull(cursor);
		assertEquals(2, cursor.getColumnCount());
		assertEquals(1, cursor.getCount());
		
		if (cursor.moveToFirst()) {
			assertEquals("testString", cursor.getString(cursor.getColumnIndex("strRow")));
			assertEquals(5, cursor.getInt(cursor.getColumnIndex("intRow")));
		} else {
			fail("Cursor should contain some values!!");
		}
		
	}
	
	public void testExecSQLAndSelect() {
		
		adapter.execSQL("INSERT INTO TestTable (intRow, strRow) VALUES (5, 'testString')");		
		
		String[] columns = {};
		String selection = null;
		String[] selectionArgs = null;
		String groubBy = null;
		String having = null;
		String orderBy = null;
		String limit = null;
		Cursor cursor = adapter.select(columns, selection, selectionArgs, groubBy, having, orderBy, limit);
		
		
		assertNotNull(cursor);
		assertEquals(2, cursor.getColumnCount());
		assertEquals(1, cursor.getCount());
		
		if (cursor.moveToFirst()) {
			while (cursor.moveToNext()) {
				assertEquals("testString", cursor.getString(cursor.getColumnIndex("strRow")));
				assertEquals(5, cursor.getInt(cursor.getColumnIndex("intRow")));
			}
		} else {
			fail("Cursor should contain some values!!");
		}
		
	}
	
	public void testInsertAndSelect() {
		
		
		ContentValues values = new ContentValues();
		values.put("intRow", 5);
		values.put("strRow", "testString");
		adapter.insert(values);		
		
		String[] columns = {"intRow", "strRow"};
		String selection = null;
		String[] selectionArgs = null;
		String groubBy = null;
		String having = null;
		String orderBy = null;
		String limit = null;
		Cursor cursor = adapter.select(columns, selection, selectionArgs, groubBy, having, orderBy, limit);
		
		assertNotNull(cursor);
		assertEquals(2, cursor.getColumnCount());
		assertEquals(1, cursor.getCount());
		
		if (cursor.moveToFirst()) {
			while (cursor.moveToNext()) {
				assertEquals("testString", cursor.getString(cursor.getColumnIndex("strRow")));
				assertEquals(5, cursor.getInt(cursor.getColumnIndex("intRow")));
			}
		} else {
			fail("Cursor should contain some values!!");
		}
		
	}
	
}
