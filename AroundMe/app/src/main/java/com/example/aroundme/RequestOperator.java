package com.example.aroundme;
import android.location.Location;
import android.util.Base64;
import android.util.Log;

import com.example.aroundme.ui.dashboard.DashboardFragment;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class RequestOperator extends Thread
{
    public interface RequestOperatorListener
    {
        void success (ModelPost[] publication);
        void failed (int responseCode);
    }

    private RequestOperatorListener listener;
    private int responseCode;

    public void setListener (RequestOperatorListener listener){ this.listener = listener; }

    @Override
    public void run()
    {
        super.run();
        try{
            ModelPost[] publications = request(DashboardFragment.GetCurrentLocation(), DashboardFragment.GetEventDistance());
            Log.e("Response Code: ", "testas request run");
            if(publications != null)
            {
                success(publications);
                Log.e("Response Code: ", "testas success");
            }
            else
            {
                failed(responseCode);
                Log.e("Response Code: ", "testas failed");
            }
        }
        catch(IOException e)
        {
            failed(-1);
        }
        catch(JSONException e)
        {
            failed(-2);
        }
    }


    private ModelPost[] request(Location location, int distanceKM) throws IOException, JSONException {

        //client secret: D4TRkN9l_iS9DIcy0fvDy6RtoPiIwVL6OnQbxWIjiWQb-gy-QLtvxw
        //auth key: 1xXBb6WyCUkXHtta-DGMAk2KCoh35XCd8-bSA402
        //url address

        //String locationString = String.format("%.6f", location.getLatitude())+"%2C"+String.format("%.6f", location.getLongitude());//54.898521%2C23.903597
        String locationString = location.getLatitude()+"%2C"+location.getLongitude();//54.9877946%2C23.9478463

        URL obj = new URL("https://api.predicthq.com/v1/events/?end.origin=2019-12-20&within="+distanceKM+"km%40"+locationString+"&offset=10&start_around.origin=2019-11-14");//
        URL obj2 = new URL("https://api.predicthq.com/v1/events/?offset=10&end.origin=2019-12-20&within="+distanceKM+"km%40"+locationString+"&offset=10&start_around.origin=2019-11-14");//

        //executor
         HttpURLConnection con = (HttpURLConnection) obj.openConnection();


        Log.i("Request: ", "request URL:" + con.getURL());

        //determined what method will be used (GET, POST, PUT, DELETE)
        con.setRequestMethod("GET");

        //determine the content type. in this case it is a JSON variable.


        //con.setRequestProperty("AuthToken", token);

        con.setRequestProperty ("Authorization", "Bearer 1xXBb6WyCUkXHtta-DGMAk2KCoh35XCd8-bSA402");
        con.setRequestProperty("Content-Type", "application/json");


        //make request and receive response
        responseCode = con.getResponseCode();

        Log.i("Request: ", "response Code:" + con.getResponseCode());

        InputStreamReader streamReader;
        if(responseCode == 200 )
        {
            streamReader = new InputStreamReader((con.getInputStream()));
        }
        else
        {
            streamReader = new InputStreamReader(con.getErrorStream());
        }



        BufferedReader in = new BufferedReader((streamReader));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null)
        {
            response.append((inputLine));
        }
        in.close();

        //print result
        //System.out.println(response.toString());

        if(responseCode == 200) {
            return parsingJsonObject(response.toString());
        }
        else
            return null;
    }


    public  ModelPost[] parsingJsonObject(String response) throws JSONException {
        //attempts to createa json object of acheiving a response


        JSONObject objects = new JSONObject(response);


        int count = objects.optInt("count", 0);
        boolean overflow = objects.optBoolean("overflow", false);
        String next = objects.optString("next", "");


        JSONArray jArray = objects.getJSONArray("results");

        ModelPost[] posts = new ModelPost[jArray.length()]; // nededam 'count' nes negrazina mums visu atsakymu o tik max 10.

        //object.a
        Log.e("Ilgis:", " array: "+jArray.length()+" count: "+count);

        //Log.i("Cycle "+0, "object: "+jArray.getJSONObject(0).toString());

        for (int i=0; i < jArray.length(); i++)
        {
            try
            {
                posts[i] = new ModelPost();

                JSONObject oneObject = jArray.getJSONObject(i);


                //because we will not need ID and user ID, the do not necessarily
                //get from a server in the JSON object
                posts[i].setId(oneObject.optString("id", "null"));
                posts[i].setRelevance(oneObject.optDouble("relevance", 0));
                posts[i].setTitle(oneObject.optString("title", "Missing Title"));
                posts[i].setDescription(oneObject.optString("description", "Missing Description"));
                posts[i].setCategory(oneObject.optString("category", "None"));
                posts[i].setLabels(oneObject.optString("labels", ""));
                posts[i].setRank(oneObject.optInt("rank", 0));
                posts[i].setLocalRank(oneObject.optInt("local_rank", 0));

                //entity
                JSONArray jArray2 = oneObject.getJSONArray("entities");
                JSONObject entityObject = jArray2.getJSONObject(0);/*
                Log.i("Entity "+i, "object0: "+entityObject.getString("formatted_address"));
                Log.i("Entity "+i, "object1: "+entityObject.getString("entity_id"));
                Log.i("Entity "+i, "object3: "+entityObject.getString("type"));
                Log.i("Entity "+i, "object4: "+entityObject.getString("name"));*/
                posts[i].setFormatted_address(entityObject.optString("formatted_address", "Missing Address"));//formatted_address
                posts[i].setEntity_id(entityObject.optString("entity_id", ""));//entity_id
                posts[i].setType(entityObject.optString("type", "Missing type"));//type
                posts[i].setName(entityObject.optString("name", "Missing name"));//name

                posts[i].setDuration(oneObject.optInt("duration", 0));
                posts[i].setStart(oneObject.optString("start", ""));
                posts[i].setEnd(oneObject.optString("end", ""));
                posts[i].setUpdated(oneObject.optString("updated", ""));

                //location
                JSONArray jArrayLocation = oneObject.getJSONArray("location");

                double[] location = new double[]{jArrayLocation.getDouble(0), jArrayLocation.getDouble(1)};
                posts[i].setLocation(location);


                /*
                Log.i("Post"+i, "Id:"+posts[i].getId());
                Log.i("Post"+i, "Relevance:"+posts[i].getRelevance());
                Log.i("Post"+i, "title:"+posts[i].getTitle());
                Log.i("Post"+i, "description:"+posts[i].getDescription());
                Log.i("Post"+i, "category:"+posts[i].getCategory());
                Log.i("Post"+i, "labels:"+posts[i].getLabels());
                Log.i("Post"+i, "rank:"+posts[i].getRank());
                Log.i("Post"+i, "localRank:"+posts[i].getLocalRank());
                Log.i("Post"+i, "formatted_address:"+posts[i].getFormatted_address());
                Log.i("Post"+i, "entity_id:"+posts[i].getEntity_id());
                Log.i("Post"+i, "type:"+posts[i].getType());
                Log.i("Post"+i, "name:"+posts[i].getName());
                Log.i("Post"+i, "duration:"+posts[i].getDuration());
                Log.i("Post"+i, "start:"+posts[i].getStart());
                Log.i("Post"+i, "end:"+posts[i].getEnd());
                Log.i("Post"+i, "updated:"+posts[i].getUpdated());
                Log.i("LOCATION", "location1: "+jArrayLocation.getDouble(0)+"  location2: "+jArrayLocation.getDouble(1));*/


                /*

                {"relevance":0.7062564,
                "id":"fV2ZEwYPGz7o8RFmBe",
                "title":"Tarp Dviejų Aušrų: D.Tiffany",
                "description":"",
                "category":"concerts",
                "labels":["concert","music"],
                "rank":38,
                "local_rank":55,
                "entities":[{"formatted_address":"Kaunas\nLithuania","entity_id":"cq4MNr6jGeSj69ScD6when","type":"venue","name":"Lizdas"}],
                "duration":28800,
                "start":"2019-11-08T21:00:00Z",
                "end":"2019-11-09T05:00:00Z",
                "updated":"2019-11-12T09:16:35Z",
                "first_seen":"2019-11-12T09:11:49Z",
                "timezone":"Europe\/Vilnius",
                "location":[23.920506,54.89642],
                "scope":"locality",
                "country":"LT",
                "place_hierarchies":[["6295630","6255148","597427","864477","598318","598316"]],
                "state":"active"}

                 */

            } catch (JSONException e)
            {
                Log.e("JSONException", e.toString());
            }
        }




        /*
        private int[] location = new int[2]; //    "location": [25.277869,54.684158]
         */
        return posts;
    }


    /*
    Get your JSON:

    Assume you have a json string

    String result = "{\"someKey\":\"someValue\"}";
    Create a JSONObject:

    JSONObject jObject = new JSONObject(result);
    If your json string is an array, e.g.:

    String result = "[{\"someKey\":\"someValue\"}]"
    then you should use JSONArray as demonstrated below and not JSONObject

    To get a specific string

    String aJsonString = jObject.getString("STRINGNAME");
    To get a specific boolean

    boolean aJsonBoolean = jObject.getBoolean("BOOLEANNAME");
    To get a specific integer

    int aJsonInteger = jObject.getInt("INTEGERNAME");
    To get a specific long

    long aJsonLong = jObject.getLong("LONGNAME");
    To get a specific double

    double aJsonDouble = jObject.getDouble("DOUBLENAME");
    To get a specific JSONArray:

    JSONArray jArray = jObject.getJSONArray("ARRAYNAME");
    To get the items from the array

    for (int i=0; i < jArray.length(); i++)
    {
        try {
            JSONObject oneObject = jArray.getJSONObject(i);
            // Pulling items from the array
            String oneObjectsItem = oneObject.getString("STRINGNAMEinTHEarray");
            String oneObjectsItem2 = oneObject.getString("anotherSTRINGNAMEINtheARRAY");
        } catch (JSONException e) {
            // Oops
        }
    }


    */

    private void failed(int code){
        if(listener != null) {
            listener.failed(code);
        }
    }

    private void success(ModelPost[] publications){
        if(listener != null) {
            listener.success(publications);
        }
    }
}
