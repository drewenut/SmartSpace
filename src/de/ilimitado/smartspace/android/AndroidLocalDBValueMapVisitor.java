package de.ilimitado.smartspace.android;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.net.Uri;
import de.ilimitado.smartspace.persistance.ValueMap;
import de.ilimitado.smartspace.persistance.ValueMapContainer;
import de.ilimitado.smartspace.persistance.Visitor;

public class AndroidLocalDBValueMapVisitor implements Visitor{

	private final ContentResolver contentResolver;

	public AndroidLocalDBValueMapVisitor(ContentResolver contentResolver) {
		this.contentResolver = contentResolver;
	}

	@Override
	public void handleValueMap(ValueMap values, String valueMapContainerName) {
		
		String tableCreationString = "CREATE TABLE IF NOT EXISTS " + valueMapContainerName + "( ";
		for(String tableRowName: values.getDataTypeMap().keySet()){
			String tableRowDataType = values.getDataTypeMap().get(tableRowName);
			tableCreationString += tableRowName +" "+ tableRowDataType + ", ";
		}
		tableCreationString = tableCreationString.substring(0, tableCreationString.length()-2);
		tableCreationString += ");";
		
		ContentValues contentValues = new ContentValues();
		for (String key : values.getKeySet()) {
			if(values.getDataTypeMap().get(key).equals("String"))
				contentValues.put(key, values.getAsString(key));
			else if(values.getDataTypeMap().get(key).equals("Integer"))
				contentValues.put(key, values.getAsInteger(key)); 
		}
		
		contentValues.put("tableCreationString", tableCreationString);
		
		Uri uri = Uri.parse("content://de.ilimitado.smartspace.provider.LocationFingerprints/" + valueMapContainerName);
		contentResolver.insert(uri, contentValues);
	}

	@Override
	public void handleValueMapContainer(ValueMapContainer valueMapContainer, String valueMapContainerName) {
		for (String key : valueMapContainer.getKeySet()) {
			valueMapContainer.get(key).handleSaveRequest(this, valueMapContainerName);
		}
	}
	
}
