package com.bwssystems.nest.protocol.status;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class DeviceDeserializer implements JsonDeserializer<Device> {
	
    @Override
    public Device deserialize(	final JsonElement json, 
    							final Type typeOfT, 
    							final JsonDeserializationContext ctx )
    {
        final JsonObject obj = json.getAsJsonObject();

    	final Map<String, DeviceDetail> devices;
        devices = new HashMap<String, DeviceDetail>();
        
        for( final Entry<String, JsonElement> entry:obj.entrySet() ){
        	
            final String strKey = entry.getKey();
			final JsonObject jsonElement = obj.getAsJsonObject(strKey);

			final Map<String,String> map = new HashMap<>();
			
			for ( final Entry<String, JsonElement> 
									property : jsonElement.entrySet() ) {
				map.put( property.getKey(), property.getValue().toString() );
			}

			final String strJSON = jsonElement.toString();
            final DeviceDetail device = new DeviceDetail( strJSON, map );


            final String strCurrentScheduleMode = 
            			jsonElement.get("current_schedule_mode").getAsString();
			device.setCurrentScheduleMode( strCurrentScheduleMode );
			
            final String strFanMode = jsonElement.get("fan_mode").getAsString();
			device.setFanMode( strFanMode );
            
            final JsonElement jsonWhereID = jsonElement.get("where_id");
			if(	jsonWhereID != null && !jsonWhereID.isJsonNull() ) {
            	device.setWhereId( jsonWhereID.getAsString() );
			}
			
            devices.put( strKey, device );
        } 
        return new Device( devices );
    }

}
