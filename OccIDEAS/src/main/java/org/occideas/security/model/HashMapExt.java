package org.occideas.security.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

public class HashMapExt extends LinkedHashMap<Object, Object> {

	private static final long serialVersionUID = 1L;

	public List<?> getValues(Object key) {
		if (super.containsKey(key)) {
			return (List<?>) super.get(key);
		} else {
			return new ArrayList<>();
		}
	}

	@Override
	public Object get(Object key) {
		ArrayList<?> values = (ArrayList<?>) super.get(key);
		if (values != null && !values.isEmpty()) {
			return values.get(0);
		} else {
			return null;
		}
	}

	public List<?> getList(Object key) {
		ArrayList<?> values = (ArrayList<?>) super.get(key);
		if (values != null && !values.isEmpty()) {
			return values;
		} else {
			return null;
		}
	}

	@Override
	public boolean containsValue(Object value) {
		return values().contains(value);
	}

	@Override
	public int size() {
		int size = 0;
		Iterator<?> keyIterator = super.keySet().iterator();

		while (keyIterator.hasNext()) {
			ArrayList<?> values = (ArrayList<?>) super.get(keyIterator.next());
			size = size + values.size();
		}

		return size;
	}

	@Override
	public Object put(Object key, Object value) {
		ArrayList<Object> values = new ArrayList<>();

		if (super.containsKey(key)) {
			values = (ArrayList<Object>) super.get(key);
			values.add(value);

		} else {
			values.add(value);
		}
		super.put(key, values);
		return null;
	}
	
	@Override
	public Collection values() {
		List<?> values = new ArrayList<>();
		Iterator keyIterator = super.keySet().iterator();

		while (keyIterator.hasNext()) {
			List keyValues = (List) super.get(keyIterator.next());
			values.addAll(keyValues);
		}

		return values;
	}

}
