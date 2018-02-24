package com.bwssystems.nest.controller;

import java.util.Date;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bwssystems.nest.protocol.status.StructureDetail;

public class Home {
	
	private final Logger log = LoggerFactory.getLogger(Nest.class);
	private final NestSession session;
	private final String strName;
	private StructureDetail theDetail;

	public Home(NestSession aSession, String aName, StructureDetail aDetail) {
		super();
		session = aSession;
		strName = aName;
		theDetail = aDetail;
	}

	public void reinitialize(StructureDetail aDetail) {
		theDetail = aDetail;
	}

	public void setAway(Boolean isAway) {
		final String theUrl = session.getTransport_url() 
				+ "/v2/put/structure." + strName;
		final HttpPost postRequest = new HttpPost(theUrl);
		final String requestString = "{\"away_timestamp\":"
				+ Long.toString(new Date().getTime()) + ",\"away\":"
				+ isAway.toString() + ",\"away_setter\":0}";
		final StringEntity requestBody = new StringEntity(requestString,
				NestSession.parsedContentType);
		log.debug( "setAway for home: " + theUrl 
						+ " with body: " + requestString);
		postRequest.setEntity(requestBody);

		final String theResponse = session.execute(postRequest);
		log.debug("setAway post request response: " + theResponse);
	}

	public StructureDetail getDetail() {
		return theDetail;
	}

	public void setDetail(StructureDetail theDetail) {
		this.theDetail = theDetail;
	}

	public String getName() {
		return strName;
	}
}
