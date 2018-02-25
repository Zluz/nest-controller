package com.bwssystems.nest.controller;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bwssystems.nest.protocol.status.DeviceDetail;
import com.bwssystems.nest.protocol.status.SharedDetail;

public class Thermostat {

	final private Logger log = LoggerFactory.getLogger(Thermostat.class);
	final private String strDeviceName;
	final private NestSession session;
	
	private DeviceDetail deviceDetail;
	private SharedDetail sharedDetail;

	public Thermostat(	final NestSession aSession, 
						final String name, 
						final DeviceDetail detail,
						final SharedDetail shared ) {
		super();
		deviceDetail = detail;
		sharedDetail = shared;
		strDeviceName = name;
		session = aSession;
	}

	public void reinitialize(	final DeviceDetail device, 
								final SharedDetail shared ) {
		deviceDetail = device;
		sharedDetail = shared;
	}

	public void setTargetTemperature( final double dCelsius ) {
		if (dCelsius < 33.0 & dCelsius > 9.0) {
			String theUrl = session.getTransport_url() 
					+ "/v2/put/shared." + strDeviceName;
			HttpPost postRequest = new HttpPost(theUrl);
			String target = null;
			log.debug("current thermostat target type is: "
					+ deviceDetail.getCurrentScheduleMode());
			if (deviceDetail.getCurrentScheduleMode()
					.equalsIgnoreCase("range")) {
				log.debug("current thermostat temperature is: "
						+ Double.toString(sharedDetail.getTargetTemperature()));
				if (dCelsius < sharedDetail.getTargetTemperature())
					target = "target_temperature_low";
				else
					target = "target_temperature_high";
			} else
				target = "target_temperature";

			String requestString = "{\"target_change_pending\":true,\"" + target
					+ "\":" + String.format("%3.1f", dCelsius) + "}";
			StringEntity requestBody = new StringEntity(requestString,
					NestSession.parsedContentType);
			log.debug("setTargetTemperature for thermostat: " + theUrl
					+ " with body: " + requestString);
			postRequest.setEntity(requestBody);
			String theResponse = session.execute(postRequest);
			log.debug("setTargetTemperature response: " + theResponse);
		} else
			log.warn( "setTargetTemperature outside of Nest paramaters "
					+ "of 10C to 33C derees, not setting with this paramter: "
							+ Double.toString( dCelsius ) );
	}

	public void setTargetType( final ThermostatTargetType type ) {
		log.debug("current thermostat target type is: "
				+ deviceDetail.getCurrentScheduleMode());
		if ( null!=type ) {
			String theUrl = session.getTransport_url() + "/v2/put/shared."
					+ strDeviceName;
			HttpPost postRequest = new HttpPost(theUrl);
			String requestString = "{\"target_temperature_type\":\"" 
					+ type.name() + "\"}";
			StringEntity requestBody = new StringEntity(requestString,
					NestSession.parsedContentType);
			log.debug("setTargetType for thermostat: " + theUrl + " with body: "
					+ requestString);
			postRequest.setEntity(requestBody);
			String theResponse = session.execute(postRequest);
			log.debug("setTargetType response: " + theResponse);
		} else
			log.warn( "ThermostatTargetType is null "
					+ "in call to setTargetType()" );
	}
	
	

	public void setFanMode( final FanMode mode ) {
		log.debug( "current thermostat fan mode is: " 
									+ deviceDetail.getFanMode() );
		if ( null!=mode ) {
			String theUrl = session.getTransport_url() 
					+ "/v2/put/device." + strDeviceName;
			HttpPost postRequest = new HttpPost(theUrl);
			String requestString = "{\"fan_mode\":\"" + mode.name() + "\"}";
			StringEntity requestBody = new StringEntity(requestString,
					NestSession.parsedContentType);
			log.debug("setFanMode for thermostat: " + theUrl + " with body: "
					+ requestString);
			postRequest.setEntity(requestBody);
			String theResponse = session.execute(postRequest);
			log.debug("setFanMode response: " + theResponse);
		} else
			log.warn( "FanMode is null in call to setFanMode()" );
	}

	public DeviceDetail getDeviceDetail() {
		return deviceDetail;
	}

	public void setDeviceDetail( final DeviceDetail deviceDetail ) {
		this.deviceDetail = deviceDetail;
	}

	public SharedDetail getSharedDetail() {
		return sharedDetail;
	}

	public void setSharedDetail( final SharedDetail sharedDetail ) {
		this.sharedDetail = sharedDetail;
	}

	public String getDeviceName() {
		return strDeviceName;
	}
}
