package com.cyberlightning.realvirtualsensorsimulator.staticresources;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.cyberlightning.realvirtualsensorsimulator.MainActivity;
import com.cyberlightning.realvirtualsensorsimulator.SensorListener;
import com.cyberlightning.realvirtualsensorsimulator.SensorListener.SensorEventObject;

import android.annotation.SuppressLint;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.location.Location;

public abstract class JsonParser {
	
	public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
	
	/**
	 * Encodes a sensor events in to a JSON type of RESTful data format documented in FI-WARE RealVirtualInteraction open specifications.
	 * @param _sensorEvents
	 * @param _location
	 * @param _contextualLocation
	 * @return Returns a String in JSON format.
	 */
	public static String createFromSensorEvent(ArrayList<SensorListener.SensorEventObject> _sensorEvents, Location _location, String _contextualLocation) {
		
		JSONObject wrapper = new JSONObject();
		JSONObject device = new JSONObject();
		JSONObject sensorWraper = new JSONObject();
		JSONObject attributes = new JSONObject();
		JSONArray sensors = new JSONArray();
		
		
		try {
			
			if (_location != null) {
				JSONArray location = new JSONArray();
				location.put(_location.getLatitude());
				location.put(_location.getLongitude());
				attributes.put("gps", location);
			} if (_contextualLocation != null) {
				attributes.put("location", _contextualLocation);
			}
			
			attributes.put("name", MainActivity.deviceName);
			
			for(SensorEventObject o: _sensorEvents) {
				SensorEvent event = o.event;
				JSONObject sensorAttrs = new JSONObject();
				JSONObject sensor = new JSONObject();
				JSONObject sensorParams = new JSONObject();
				JSONObject value = new JSONObject();
				
				sensorAttrs.put("type", o.type);
				sensorAttrs.put("vendor", event.sensor.getVendor());
				sensorAttrs.put("power", event.sensor.getPower());
				sensorAttrs.put("name", event.sensor.getName());
				sensor.put("attributes", sensorAttrs);
				
				sensorParams.put("toggleable", "boolean");
				sensorParams.put("interval", "ms");
				sensor.put("parameters", sensorParams);
				
				value.put("values", resolveValues(event.values,event.sensor.getType()));
				value.put("time", getTimeStamp());
				value.put("primitive",resolvePrimitive(event.sensor.getType()));
				value.put("unit", resolveSensorUnitById(event.sensor.getType())); //TODO implement a more accurate and dynamic way
				
				sensor.put("value", value);
				sensors.put(sensor);
			}

			sensorWraper.put("sensors", sensors);
			sensorWraper.put("attributes", attributes);
			device.put(MainActivity.deviceId, sensorWraper);
			wrapper.put(MainActivity.deviceId, device);
			
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return wrapper.toString();
	}
	
	private static Object resolveValues (float[] _values, int _id) {
		String prim = resolvePrimitive(_id);
		if (prim.contentEquals("double")){
			return _values[0];
		} else {
			JSONArray values = new JSONArray();
			for(int i = 0; i < _values.length; i++) {
				try {
					values.put(_values[i]);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return values;
		}
	}
	
	
	@SuppressWarnings("deprecation")
	private static String resolvePrimitive(int _id) {
		String primitive = null;
		
		switch(_id){
			case Sensor.TYPE_ACCELEROMETER: primitive = "3DPoint";
				break;
			case Sensor.TYPE_AMBIENT_TEMPERATURE:primitive = "double";
				break;
			case Sensor.TYPE_ROTATION_VECTOR:primitive = "array";
				break;
			case Sensor.TYPE_GRAVITY:primitive = "3DPoint";
				break;
			case Sensor.TYPE_GYROSCOPE:primitive = "3DPoint";
				break;
			case Sensor.TYPE_LIGHT:primitive = "double";
				break;
			case Sensor.TYPE_LINEAR_ACCELERATION:primitive = "3DPoint";
				break;
			case Sensor.TYPE_MAGNETIC_FIELD:primitive = "3DPoint";
				break;
			case Sensor.TYPE_PRESSURE:primitive = "double";
				break;
			case Sensor.TYPE_PROXIMITY:primitive = "double";
				break;
			case Sensor.TYPE_ORIENTATION:primitive ="3DPoint";
				break;
			case Sensor.TYPE_RELATIVE_HUMIDITY:primitive = "double";
				break;
			
			
			}
		return primitive;
	}
	
	@SuppressLint("SimpleDateFormat")
	public static String getTimeStamp() {
		return  new SimpleDateFormat(DATE_FORMAT).format(new Date(System.currentTimeMillis()));
	}
	
	@SuppressWarnings("deprecation")
	public static String resolveSensorUnitById(int _id) {
		String unit = null;
		switch(_id){
			case Sensor.TYPE_ACCELEROMETER: unit = "m/s2";
				break;
			case Sensor.TYPE_AMBIENT_TEMPERATURE:unit = "celcius";
				break;
			case Sensor.TYPE_ROTATION_VECTOR:unit = "array";
				break;
			case Sensor.TYPE_GRAVITY:unit = "m/s2";
				break;
			case Sensor.TYPE_GYROSCOPE:unit = "rad/s";
				break;
			case Sensor.TYPE_LIGHT:unit = "lx";
				break;
			case Sensor.TYPE_LINEAR_ACCELERATION:unit = "m/s2";
				break;
			case Sensor.TYPE_MAGNETIC_FIELD:unit = "uT";
				break;
			case Sensor.TYPE_PRESSURE:unit = "hPa";
				break;
			case Sensor.TYPE_PROXIMITY:unit = "cm";
				break;	
			case Sensor.TYPE_ORIENTATION:unit = "orientation";
				break;
			case Sensor.TYPE_RELATIVE_HUMIDITY:unit = "percent";
			break;
		
			}
			
			return unit;
		}
	
	@SuppressWarnings("deprecation")
	public static String resolveSensorTypeById(int _id) {
		String name = null;
		switch(_id){
			case Sensor.TYPE_ACCELEROMETER: name = "accelerometer";
				break;
			case Sensor.TYPE_AMBIENT_TEMPERATURE:name = "temperature";
				break;
			case Sensor.TYPE_ROTATION_VECTOR:name = "rotationvector";
				break;
			case Sensor.TYPE_GRAVITY:name = "gravity";
				break;
			case Sensor.TYPE_GYROSCOPE:name = "gyroscope";
				break;
			case Sensor.TYPE_LIGHT:name = "light";
				break;
			case Sensor.TYPE_LINEAR_ACCELERATION:name = "linearacceleration";
				break;
			case Sensor.TYPE_MAGNETIC_FIELD:name = "magneticfield";
				break;
			case Sensor.TYPE_PRESSURE:name = "pressure";
				break;
			case Sensor.TYPE_PROXIMITY:name = "proximity";
				break;
			case Sensor.TYPE_ORIENTATION:name = "orientation";
				break;
			case Sensor.TYPE_RELATIVE_HUMIDITY: name = "relativehumidity";
			}
			return name;
		}

}
