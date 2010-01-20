package de.ilimitado.smartspace.persistance;

public interface Persistance {
	public void save(int pGateway, Object value);
	public void load(int pGateway, String query);
	public Object get(int pGateway);
	public void setStrategy(int gateway, Object strategy);
}
