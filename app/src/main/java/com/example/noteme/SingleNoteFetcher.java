package com.example.noteme;


import android.content.Context;
import android.location.Location;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;


/*
This class should be able to take only one Note from server thure its Lon and Lat.
 */

public class SingleNoteFetcher {

    private RequestQueue _queue;
    private final static String REQUEST_URL = "http://10.0.2.2:8080/notes/getnote?";


    public SingleNoteFetcher(Context context) {
        _queue = Volley.newRequestQueue(context);
    }


    public class SingleNoteResponse {

        public boolean isError;
        public String id;
        public String head;
        public String body;
        public double lon;
        public double lat;
        public int numLikes;


        public SingleNoteResponse(boolean isError, String id, String head, String body, double lon, double lat, int numLikes) {
            this.isError = isError;
            this.id = id;
            this.head = head;
            this.body = body;
            this.lon = lon;
            this.lat = lat;
            this.numLikes = numLikes;
        }
    }

    public interface SingleNoteResponseListener {
        public void onResponse(SingleNoteFetcher.SingleNoteResponse response);

    }

    private SingleNoteFetcher.SingleNoteResponse createErrorResponse() {
        Log.e("In fetcher", "In Creat arror response");
        return new SingleNoteFetcher.SingleNoteResponse(true, null, null, null, 0, 0, 0);

    }

    public void dispatchRequest(final SingleNoteResponseListener listener) {
        Log.i("In fetcher", "In fetcher");
        //JSONObject response = new JSONObject();
        Location lastLocation = MapActivity.mLastKnownLocation;
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET,
                REQUEST_URL+"Lat="+ NoteActivity.givenLat + "&Lon=" + NoteActivity.givenLon,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
//                        Log.i("In fetcher", "On Response");
                        try {
                            //Creating the response
                            Log.i("In fetcher", "In Try");
                            SingleNoteFetcher.SingleNoteResponse res = new SingleNoteFetcher.SingleNoteResponse(false,
                                    response.getString("Id"),
                                    response.getString("Head"),
                                    response.getString("Body"),
                                    response.getDouble("Lon"),
                                    response.getDouble("Lat"),
                                    response.getInt("NumLikes"));
                            listener.onResponse(res);
                        }
                        catch (JSONException e) {
                            //Log for error
                            String error = "The error is : "+ e;
                            Log.i("onResponse exception" , error);
                            //create error response
                            listener.onResponse(createErrorResponse());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse (VolleyError error) {
                        Log.i("In fetcher", "In Error Response");
                        System.out.println(error);

                        listener.onResponse(createErrorResponse());
                    }
                });

        _queue.add(req);
    }
}

