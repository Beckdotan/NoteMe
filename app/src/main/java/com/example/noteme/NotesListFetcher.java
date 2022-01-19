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
The purpose of this class is to get to ask from our server to get to firebase and get a JSON with all the relevant notes.
for now, there is no "distance" logic here.

 */

public class NotesListFetcher {

    private RequestQueue _queue;
    private final static String REQUEST_URL = "http://10.0.2.2:8080/notes?";

    public NotesListFetcher(Context context) {
        _queue = Volley.newRequestQueue(context);
    }


    public class NotesListResponse {

        public boolean isError;
        public String id;
        public String head;
        public String body;
        public double lon;
        public double lat;
        public int numLikes;


        public NotesListResponse(boolean isError, String id, String head, String body, double lon, double lat, int numLikes) {
            this.isError = isError;
            this.id = id;
            this.head = head;
            this.body = body;
            this.lon = lon;
            this.lat = lat;
            this.numLikes = numLikes;
        }
    }

    public interface NotesListResponseListener {
        public void onResponse(NotesListResponse response);

    }

    private NotesListResponse createErrorResponse() {
        Log.e("In fetcher", "In Creat arror response");
        return new NotesListResponse(true, null, null, null, 0, 0, 0);

    }

    public void dispatchRequest(final NotesListResponseListener listener) {
        Log.i("In fetcher", "In fetcher");
        //JSONObject response = new JSONObject();
        Location lastLocation = MapActivity.mLastKnownLocation;
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, REQUEST_URL+"lon="+lastLocation.getLongitude()+"&lat="+lastLocation.getLatitude()  , null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("In fetcher", "On Response");
                        try {
                            //Creating the response
                            Log.i("In fetcher", "In Try");
                            NotesListResponse res = new NotesListResponse(false,
                                    response.getString("Id"),
                                    response.getString("Head"),
                                    response.getString("Body"),
                                    response.getDouble("Lon"),
                                    response.getDouble("Lat"),
                                    response.getInt("NumLikes")
                            );
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
                }, new Response.ErrorListener() {
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


