package com.bdevlin.apps.pandt;

import android.content.Context;
import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.SmallTest;
import android.util.Log;
import android.widget.ImageView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.bdevlin.apps.ui.activity.core.MailActivityEmail;
import com.bdevlin.apps.utils.VolleyController;

import junit.framework.Assert;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * Created by brian on 10/9/2014.
 */
public class HomeActivityTest  extends ActivityInstrumentationTestCase2<MailActivityEmail> {

    public static final String TAG = ApplicationTest.class.getSimpleName();
    private MailActivityEmail activity;
    private Context context;
    private ImageLoader mImageLoader;

    public HomeActivityTest() {
        super(MailActivityEmail.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();


        setActivityInitialTouchMode(false);
        activity = getActivity();
        context = activity.getActivityContext();
        mImageLoader = VolleyController.getInstance(context).getImageLoader();

    }

    @SmallTest
    public void testPreconditions() {

        assertNotNull("failed to get application", activity);
        assertNotNull("failed to get mImageLoader", mImageLoader);

    }

    @SmallTest
    public void testImageLoader2() throws Exception {
        final CountDownLatch signal = new CountDownLatch(1);
        final ImageView mImageView;
        mImageView = new ImageView(activity.getActivityContext());
        String url = "http://i.imgur.com/7spzG.png";

        activity.runOnUiThread(new Runnable() {

            public void run() {
                String url = "http://i.imgur.com/7spzG.png";
              //  MailActivityEmail activity = getActivity();
              // Context context =  activity.getActivityContext();
        //ImageLoader mImageLoader = VolleyController.getInstance(context).getImageLoader();
        ImageLoader.ImageListener listener = ImageLoader.getImageListener(mImageView, android.R.drawable.ic_menu_rotate, android.R.drawable.ic_delete);
        ImageLoader.ImageContainer imageContainer = mImageLoader.get(url, listener);

            }
        });

        signal.await(5000, TimeUnit.MILLISECONDS);

        getInstrumentation().waitForIdleSync();

        Assert.assertNotNull(mImageView);

    }

    @SmallTest
    public void testImageLoader() throws Exception {
        final CountDownLatch signal = new CountDownLatch(1);
        ImageLoader imageLoader;
        final ImageView mImageView;
        mImageView = new ImageView(activity.getActivityContext());
        String url = "http://i.imgur.com/7spzG.png";



//        Thread t = new Thread("Thread1") {
//           // @Override
//            public void run() {
//                // some code #2
//                Log.e(TAG, "thread running: " );



                activity.runOnUiThread(new Runnable() {

                    public void run() {
                        String url = "http://i.imgur.com/7spzG.png";
                       // MailActivityEmail activity = getActivity();

                      // Context context =  activity.getActivityContext();

                      //  ImageLoader imageLoader = VolleyController.getInstance(context).getImageLoader();
                        // If you are using normal ImageView
                        mImageLoader.get(url, new ImageLoader.ImageListener() {

                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.e(TAG, "Image Load Error: " + error.getMessage());
                                // note: setting image via a resource and not a bitmap
                                mImageView.setImageResource(R.drawable.person_image_empty);
                                signal.countDown();
                            }

                            @Override
                            public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                                if (response.getBitmap() != null) {
                                    // load image into imageview
                                    mImageView.setImageBitmap(response.getBitmap());
                                    signal.countDown();
                                } else {
                                    mImageView.setImageResource(R.drawable.person_image_empty);
                                }

                            }
                        });

                    }
                });

//            }
//        };
//        t.start();

        signal.await(5000, TimeUnit.MILLISECONDS);

        getInstrumentation().waitForIdleSync();

        Assert.assertNotNull(mImageView);
       // assertEquals(url, mImageLoader.);

    }


    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }
}
