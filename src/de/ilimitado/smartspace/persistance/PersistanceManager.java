package de.ilimitado.smartspace.persistance;

import android.content.Context;



public class PersistanceManager implements Persistance{

	public static final int GATEWAY_LFPT = 0x0001;
	public static final int GATEWAY_FILE_SYSTEM = 0x0002;
	
	private LFPTGateway lfptGW;
	private FileGateway fileGW;

	public PersistanceManager(Context ctx) {
		this.lfptGW = new LFPTGateway();
		this.fileGW = new FileGateway(ctx);
	}
	
	@Override
	public Object get(int gateway) {
		switch (gateway) {
		case GATEWAY_LFPT:
			return lfptGW.getRadioMap();
		case GATEWAY_FILE_SYSTEM:
			return fileGW;
		default:
			throw new PersitanceException("You tried to query some values from a non existing GW");
		}
	}

	@Override
	public void load(int gateway, String query) {
		switch (gateway) {
		case GATEWAY_LFPT:
			if(!lfptGW.isLoaded()){
				lfptGW.loadRadioMap();
			}
			break;
		case GATEWAY_FILE_SYSTEM:
			break;
		default:
			throw new PersitanceException("You tried to load some values from a non existing GW");
		}
	}

	@Override
	public void save(int gateway, Object value) {
		switch (gateway) {
		case GATEWAY_LFPT:
			lfptGW.save(value);
			break;
		case GATEWAY_FILE_SYSTEM:
			break;
		default:
			throw new PersitanceException("You tried to save some values on a non existing GW");
		}
	}
	
	@Override
	public void setStrategy(int gateway, Object strategy){
		switch (gateway) {
		case GATEWAY_LFPT:
			lfptGW.setStrategy(strategy);
			break;
		case GATEWAY_FILE_SYSTEM:
			fileGW.setStrategy(strategy);
			break;
		default:
			throw new PersitanceException("You tried to set a strategy on a non existing GW");
		}
	}

	public void startPersistance(int gateway) {
		switch (gateway) {
		case GATEWAY_LFPT:
			lfptGW.startGateway();
			break;
		case GATEWAY_FILE_SYSTEM:
			break;
		default:
			throw new PersitanceException("You tried to start a non existing GW");
		}	
	}
	
	public void stopPersistance(int gateway) {
		switch (gateway) {
		case GATEWAY_LFPT:
			lfptGW.stopGateway();
			break;
		case GATEWAY_FILE_SYSTEM:
			break;
		default:
			throw new PersitanceException("You tried to stop a non existing GW");
		}	
	}
}
