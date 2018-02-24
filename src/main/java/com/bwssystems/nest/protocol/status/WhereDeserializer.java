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

public class WhereDeserializer implements JsonDeserializer<Where> {

	@Override
	public Where deserialize(	final JsonElement json, 
								final Type typeOfT,
								final JsonDeserializationContext ctx ) {
		
		final JsonObject obj = json.getAsJsonObject();

		final Map<String, WhereDetail> mapWhereDetails;
		mapWhereDetails = new HashMap<String, WhereDetail>();
		
		for ( final Entry<String, JsonElement> entry : obj.entrySet() ) {
			final String strKey = entry.getKey();
			
			final JsonObject jsonObject = obj.getAsJsonObject(strKey);
			final WhereDetail where = new Gson().fromJson(
					jsonObject, WhereDetail.class );
			where.setOriginalJSON( jsonObject.toString() );
			
			mapWhereDetails.put( strKey, where );
		}
		return new Where( mapWhereDetails );
	}

}
