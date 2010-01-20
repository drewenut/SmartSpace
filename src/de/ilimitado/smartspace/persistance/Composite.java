package de.ilimitado.smartspace.persistance;

public interface Composite extends Component {
	public void add(String key, Component value);
	public Component get(String key);
}
