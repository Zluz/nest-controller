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

public class SharedDeserializer implements JsonDeserializer<Shared> {
	
	@Override
	public Shared deserialize(	final JsonElement jsonInput, 
								final Type typeOfT,
								final JsonDeserializationContext ctx ) {

		final JsonObject obj = jsonInput.getAsJsonObject();

		final Map<String, SharedDetail> 
					mapDetails = new HashMap<String, SharedDetail>();

		for (Entry<String, JsonElement> entry : obj.entrySet()) {

			final String strKey = entry.getKey();
			final JsonObject jsonElement = obj.getAsJsonObject( strKey );
			final String strJSON = jsonElement.toString();
			
			final Map<String,String> map = new HashMap<>();
			
			for ( final Entry<String, JsonElement> 
									property : jsonElement.entrySet() ) {
				map.put( property.getKey(), property.getValue().toString() );
			}

			final SharedDetail detail = 
					new Gson().fromJson( jsonElement, SharedDetail.class );
			detail.setOriginalJSON( strJSON );
			detail.setOriginalMap( map );
			
			mapDetails.put( strKey, detail );
		}
		return new Shared(mapDetails);
	}

}
