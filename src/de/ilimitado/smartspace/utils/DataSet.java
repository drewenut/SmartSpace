package de.ilimitado.smartspace.utils;

import java.util.ArrayList;

public class DataSet<E> extends ArrayList<E> {
	
	private static final long serialVersionUID = 1L;

	@Override
	public boolean add(E entry) {
		if(!contains(entry))
			return super.add(entry);
		return false;
	}
}
