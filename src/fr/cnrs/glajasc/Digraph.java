package fr.cnrs.glajasc;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Digraph<V>
{
	private final Map<V, Set<V>> m = new HashMap<>();

	public Set<V> getNodes()
	{
		return m.keySet();
	}

	public Set<V> getFollowers(V u)
	{
		return m.get(u);
	}

	
	public void addNode(V e)
	{
		m.put(e, new HashSet<>());
	}

	public void addArc(V u, V v)
	{
		m.get(u).add(v);
	}

	public void removeArc(V u, V v)
	{
		m.get(u).remove(v);
	}

	public boolean isArc(V u, V v)
	{
		return m.get(u).contains(v);
	}
}
