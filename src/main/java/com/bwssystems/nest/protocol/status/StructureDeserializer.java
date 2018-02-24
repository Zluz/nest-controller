package com.bwssystems.nest.protocol.status;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class StructureDeserializer implements JsonDeserializer<Structure> {
	

	@Override
	public Structure deserialize(	final JsonElement json, 
									final Type typeOfT,
									final JsonDeserializationContext ctx ) {
		final JsonObject obj = json.getAsJsonObject();

		final Map<String, StructureDetail> mapStructureDetails;
		mapStructureDetails = new HashMap<String, StructureDetail>();
		
		for (Entry<String, JsonElement> entry : obj.entrySet()) {
			
			final String strKey = entry.getKey();
			
			final StructureDetail element = new Gson().fromJson(
					obj.getAsJsonObject(strKey), StructureDetail.class);

			element.setOriginalJSON( element.toString() );

			mapStructureDetails.put( strKey, element );
		}
		return new Structure( mapStructureDetails );
	}

}
