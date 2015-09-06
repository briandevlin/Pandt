package com.bdevlin.apps.pandt;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.test.ApplicationTestCase;
import android.test.suitebuilder.annotation.SmallTest;
import android.util.Log;
import android.widget.ImageView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.bdevlin.apps.utils.VolleyController;
//import com.bdevlin.apps.utils.MyVolley;

import junit.framework.Assert;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {

   // private application mApp;
   // private static application sAppInstance;
   public static final String TAG = ApplicationTest.class.getSimpleName();
   private static final String ACCT_USER_NAME = "username";
    private static final String ACCT_PASSWORD = "password";
    private static final String urlAuth ="http://pmosnotifierserver.appspot.com/Auth";


    // constructor
    public ApplicationTest() { super(Application.class);}

    @Override
    public Context getSystemContext() {
        return super.getSystemContext();
    }

    @Override
    public Application getApplication() {
        return super.getApplication();
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        createApplication();
        //mApplication =
        getApplication();



       //  MyVolley.init(getSystemContext());
    }

    @SmallTest
    public void testPostRequest() throws Exception {
        final CountDownLatch signal = new CountDownLatch(1);
        final StringBuilder strBuilder = new StringBuilder();
        //String url ="http://pmosnotifierserver.appspot.com/Auth";

        StringRequest sr = new StringRequest(Request.Method.POST,urlAuth, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
               // mPostCommentResponse.requestCompleted();
                Log.d("testing","Response: " + response.toString());
                strBuilder.append(response);
                signal.countDown();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //mPostCommentResponse.requestEndedWithError(error);
                Log.d("testing","Response: FAILED" );
                strBuilder.append("FAILED");
            }
        }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
               params.put(ACCT_USER_NAME,"Brian");
               params.put(ACCT_PASSWORD,"Brian");

                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("Content-Type","application/x-www-form-urlencoded; charset=utf-8");
                params.put("User-agent", "My useragent");
                return params;
            }
        };

        VolleyController.getInstance(this.getSystemContext()).addToRequestQueue(sr);

        try {
            signal.await(10, TimeUnit.SECONDS); // wait for callback
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Assert.assertEquals("authToken\n", strBuilder.toString());

    }

    @SmallTest
    public void testJSONRequest() throws Exception {
        final CountDownLatch signal = new CountDownLatch(1);
        final StringBuilder strBuilder = new StringBuilder();
        String url = "http://pmos-restseerver.appspot.com/rest/m/saymore/";

    /*    JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, null,
                        new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("testing","Response: " + response.toString());
                        strBuilder.append(response);
                        signal.countDown();
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("testing","Response: FAILED" );
                        strBuilder.append("FAILED");
                    }
                });*/

// Access the RequestQueue through your singleton class.
      /*  VolleyController.getInstance(this.getSystemContext()).addToRequestQueue(jsObjRequest);
        try {
            signal.await(10, TimeUnit.SECONDS); // wait for callback
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Log.d("testing","Response: " + strBuilder.toString());

        Assert.assertEquals("{\"data", strBuilder.substring(0,6));*/



    }
        @SmallTest
    public void testSimpleRequest() throws Exception {
        final CountDownLatch signal = new CountDownLatch(1);


        // Get a RequestQueue
//        RequestQueue queue = MySingleton.getInstance(this.getSystemContext()).
//                getRequestQueue();

       // String url ="http://pmosnotifierserver.appspot.com/Auth";
        final StringBuilder strBuilder = new StringBuilder();


// Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(
                Request.Method.GET,
                urlAuth,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("testing", "Response is: " + response);
                        strBuilder.append(response);
                        signal.countDown();

                    }
                },
                new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("testing","That didn't work!");
            }
        });
// Add the request to the RequestQueue.
       // queue.add(stringRequest);
        VolleyController.getInstance(this.getSystemContext()).addToRequestQueue(stringRequest);
        try {
            signal.await(10, TimeUnit.SECONDS); // wait for callback
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Assert.assertEquals("authenticaton service\n", strBuilder.toString());
    }

    @SmallTest
    public void testImageRequest() throws Exception {
        final CountDownLatch signal = new CountDownLatch(1);
        final ImageView mImageView;
        mImageView = new ImageView(this.getSystemContext());
        String url = "http://i.imgur.com/7spzG.png";

// Retrieves an image specified by the URL, displays it in the UI.
        ImageRequest request = new ImageRequest(url,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap bitmap) {
                        mImageView.setImageBitmap(bitmap);
                        signal.countDown();
                    }
                }, 0, 0, null,
                new Response.ErrorListener() {
                    public void onErrorResponse(VolleyError error) {
                        mImageView.setImageResource(R.drawable.default_cover);
                    }
                });
// Access the RequestQueue through your singleton class.
        VolleyController.getInstance(this.getSystemContext()).addToRequestQueue(request);

        try {
            signal.await(10, TimeUnit.SECONDS); // wait for callback
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Assert.assertNotNull(mImageView);
    }

//    @SmallTest
//    public void testImageLoader() throws Exception {
//        final CountDownLatch signal = new CountDownLatch(1);
//        final ImageView mImageView;
//        mImageView = new ImageView(this.getSystemContext());
//        String url = "http://i.imgur.com/7spzG.png";
//
//        ImageLoader imageLoader = VolleyController.getInstance(this.getSystemContext()).getImageLoader();
//        // If you are using normal ImageView
//        imageLoader.get(url, new ImageLoader.ImageListener() {
//
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Log.e(TAG, "Image Load Error: " + error.getMessage());
//            }
//
//            @Override
//            public void onResponse(ImageLoader.ImageContainer response, boolean arg1) {
//                if (response.getBitmap() != null) {
//                    // load image into imageview
//                    mImageView.setImageBitmap(response.getBitmap());
//                }
//            }
//        });
//
//
//    }

//    private void runThread(){
//        Thread t = new Thread("Thread1") {
//            @Override
//            public void run() {
//                // some code #2
//                this.runOnUiThread(new Runnable() {
//                    public void run() {
//                        // some code #3 (that needs to be ran in UI thread)
//
//                    }
//                });
//
//            }
//        };
//        t.start();
//    }


    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }
}