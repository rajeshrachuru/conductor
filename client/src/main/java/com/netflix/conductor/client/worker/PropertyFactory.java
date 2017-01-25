/**
 * 
 */
package com.netflix.conductor.client.worker;

import java.util.concurrent.ConcurrentHashMap;

import com.netflix.config.DynamicProperty;

/**
 * @author Viren
 * Used to configure the Conductor workers using properties.
 * 
 */
public class PropertyFactory {
	
	private DynamicProperty global;
	
	private DynamicProperty local;
	
	private static final String propertyPrefix = "conductor.worker";

	private static ConcurrentHashMap<String, PropertyFactory> factories = new ConcurrentHashMap<>();
	
	private PropertyFactory(String prefix, String propName, String workerName) {
		this.global = DynamicProperty.getInstance(prefix + "." + propName);
		this.local = DynamicProperty.getInstance(prefix + "." + workerName + "." + propName);
	}
	
	/**
	 * 
	 * @param defaultValue Default Value
	 * @return Returns the value as integer.  If not value is set (either global or worker specific), then returns the default value. 
	 */
	public Integer getInteger(int defaultValue) {
		Integer value = local.getInteger();
		if(value == null) {
			value = global.getInteger(defaultValue);
		}
		return value;
	}
	
	/**
	 * 
	 * @param defaultValue Default Value
	 * @return Returns the value as String.  If not value is set (either global or worker specific), then returns the default value.
	 */
	public String getString(String defaultValue) {
		String value = local.getString();
		if(value == null) {
			value = global.getString(defaultValue);
		}
		return value;
	}
	
	/**
	 * 
	 * @param defaultValue Default Value
	 * @return Returns the value as Boolean.  If not value is set (either global or worker specific), then returns the default value.
	 */
	public Boolean getBoolean(Boolean defaultValue) {
		Boolean value = local.getBoolean();
		if(value == null) {
			value = global.getBoolean(defaultValue);
		}
		return value;
	}

	public static Integer getInteger(String workerName, String property, Integer defaultValue) {
		return get(workerName, property).getInteger(defaultValue);
	}
	
	public static Boolean getBoolean(String workerName, String property, Boolean defaultValue) {
		return get(workerName, property).getBoolean(defaultValue);
	}
	
	public static String getString(String workerName, String property, String defaultValue) {
		return get(workerName, property).getString(defaultValue);
	}

	private static PropertyFactory get(String workerName, String property) {
		String key = property + "." + workerName;
		PropertyFactory pf = factories.computeIfAbsent(key, t-> {
			return new PropertyFactory(propertyPrefix, property, workerName);
		});
		return pf;
	}
	
}