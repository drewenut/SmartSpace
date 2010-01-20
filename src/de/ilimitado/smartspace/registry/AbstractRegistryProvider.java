package de.ilimitado.smartspace.registry;

import java.util.HashMap;

public abstract class AbstractRegistryProvider<K, V> extends HashMap<K, Class<? extends V>>{

	private static final long serialVersionUID = 1L;

	protected AbstractRegistryProvider() { }
	
	public boolean isRegistered(K uri) {
		return (this.containsKey(uri));
	}

	public DataCommandProvider putItem(K uri, Class<? extends V> clazz) {
		super.put(uri, clazz);
		return null;
	}

	public V getItem(K uri) throws RegistryProviderException {
		try {
			Class<? extends V> clazz;
			if(isRegistered(uri)){
				clazz = super.get(uri);
				return createInstance(uri, clazz);
			}
			else
				throw new RegistryProviderNotRegisterdException("Item is not registered, here is what i know: Item uri -> "+uri);
				
		} catch (Exception e) {
			throw new RegistryProviderException("Item could not be initiated, here is what i know:" + e);
		}
	}
	
	@Override
	public Class<? extends V> get(Object key) {
		throw new UnsupportedOperationException();
	}
	
	private V createInstance(K uri, Class<? extends V> clazz) throws InstantiationException, IllegalAccessException
    {
		V dC;
		try {
			dC = (V) clazz.newInstance();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			throw e;
		} catch (InstantiationException e) {
			e.printStackTrace();
			throw e;
		}
		return dC;
    }
}


