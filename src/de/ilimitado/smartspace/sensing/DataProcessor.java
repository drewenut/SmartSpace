package de.ilimitado.smartspace.sensing;

import java.util.Collection;

public class DataProcessor<E> implements Runnable {

	private DataCommandChain<Collection<?>, E> dataCommandChain;
	private Class<E> clazz;
	private E scanSample = null;

	public DataProcessor(Class<E> clazz, DataCommandChain<Collection<?>, E> dataCmdChain) {
		this.dataCommandChain = dataCmdChain;
		this.clazz = clazz;
	}

	public void readyProcessor(Collection<?> data) {
		dataCommandChain.setStdIn(data);
		createSampleInstance();
		dataCommandChain.setStdOut(scanSample);
	}
	
	public void processData(){
		dataCommandChain.executeCommands();
	}

	public E getProcessedData() {
		return scanSample;
	}

	@Override
	public void run() {
		processData();
	}
	
	private void createSampleInstance()
    {
        try {
        	scanSample = clazz.newInstance();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} catch (InstantiationException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
    }
}
