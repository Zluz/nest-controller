package com.bwssystems.nest.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.http.client.methods.HttpGet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bwssystems.nest.protocol.status.Device;
import com.bwssystems.nest.protocol.status.DeviceDeserializer;
import com.bwssystems.nest.protocol.status.DeviceDetail;
import com.bwssystems.nest.protocol.status.NestStatus;
import com.bwssystems.nest.protocol.status.Shared;
import com.bwssystems.nest.protocol.status.SharedDeserializer;
import com.bwssystems.nest.protocol.status.SharedDetail;
import com.bwssystems.nest.protocol.status.Structure;
import com.bwssystems.nest.protocol.status.StructureDeserializer;
import com.bwssystems.nest.protocol.status.StructureDetail;
import com.bwssystems.nest.protocol.status.Where;
import com.bwssystems.nest.protocol.status.WhereDeserializer;
import com.bwssystems.nest.protocol.status.WhereDetail;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Nest {
	
	private final Logger log = LoggerFactory.getLogger(Nest.class);
	private final Gson gson;
	
	private NestSession session;
	private NestStatus status;
	
	private final Map<String, Thermostat> mapThermostats;
	private final Map<String, Home> mapHomes;

	public Nest(NestSession aSession) {
		super();
		this.gson = new GsonBuilder()
				.registerTypeAdapter(Device.class, new DeviceDeserializer())
				.registerTypeAdapter(Structure.class,
						new StructureDeserializer())
				.registerTypeAdapter(Where.class, new WhereDeserializer())
				.registerTypeAdapter(Shared.class, new SharedDeserializer())
				.create();
		this.session = aSession;
		this.mapThermostats = new HashMap<String, Thermostat>();
		this.mapHomes = new HashMap<String, Home>();
		_getStatus();
	}

	private void _getStatus() {
		final String theUrl = session.getTransport_url() 
								+ "/v2/mobile/user." + session.getUserid();
		log.debug("getting status: " + theUrl);
		final HttpGet newRequest = new HttpGet(theUrl);
		final String response = session.execute(newRequest);
		log.debug("status response: " + response);
		
		status = gson.fromJson(response, NestStatus.class);
		
		final Map<String, DeviceDetail> 
								devices = status.getDevice().getDevices();
		for ( final String key : devices.keySet()) {
			
			final Map<String, SharedDetail> mapShared = 
								status.getShared().getSharedDetails();
			
			if (mapThermostats.get(key) == null) {
				
				final Thermostat thermostat = new Thermostat(
						session, key, devices.get(key), mapShared.get(key) );
				
				mapThermostats.put( key, thermostat );
			} else
				mapThermostats.get( key ).reinitialize( 
								devices.get(key), mapShared.get(key));
		}
		for (String key : status.getStructure().getStructureDetails()
				.keySet()) {
			final StructureDetail detail = 
						status.getStructure().getStructureDetails().get(key);
			
			if (mapHomes.get(key) == null) {
				final Home home = new Home(session, key, detail);
				mapHomes.put(key, home);
			} else
				mapHomes.get(key).reinitialize(detail);
		}
	}

	public Thermostat getThermostat(String aName) {
		if (session == null)
			return null;
		_getStatus();
		return mapThermostats.get(aName);
	}

	public Home getHome(String aName) {
		if (session == null)
			return null;
		_getStatus();
		return mapHomes.get(aName);
	}

	public Set<String> getThermostatNames() {
		if (session == null)
			return null;
		return mapThermostats.keySet();
	}

	public Set<String> getHomeNames() {
		if (session == null)
			return null;
		return mapHomes.keySet();
	}

	public WhereDetail getWhere(String aName) {
		if (session == null)
			return null;
		return status.getWhere().getWheres().get(aName);
	}

	public void endNestSession() {
		session.close();
		session = null;
	}
}