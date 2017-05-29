package com.example.manan.enhancedurdureader.Activities;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.example.manan.enhancedurdureader.ApplicationEntities.Article;
import com.example.manan.enhancedurdureader.ApplicationEntities.Magazine;
import com.example.manan.enhancedurdureader.DataStorage.LocalStorage;
import com.example.manan.enhancedurdureader.R;
import com.salesforce.androidsdk.app.SalesforceSDKManager;
import com.salesforce.androidsdk.rest.ApiVersionStrings;
import com.salesforce.androidsdk.rest.RestClient;
import com.salesforce.androidsdk.rest.RestRequest;
import com.salesforce.androidsdk.rest.RestResponse;
import com.salesforce.androidsdk.ui.SalesforceActivity;

import org.json.JSONArray;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

/**
 * Created by anas on 5/28/17.
 */

public class SalesforceDownloadActivity extends SalesforceActivity {

    RestClient client;
    ArrayList<String> MagazineList;
    LocalStorage db;

    public SalesforceDownloadActivity(){



    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setContentView(R.layout.salesforce_layout);

        Toast.makeText(this, "Salesforce Activity", Toast.LENGTH_LONG).show();
       // db = new LocalStorage(this, null, null, 1);
        //MagazineList = new ArrayList<String>();

    }

    @Override
    public void onResume(RestClient client) {
        // Keeping reference to rest client
        this.client = client;
        try {
            //getMagazines("Select Name From Magazine__c");
            getArticles("Select Name, Body__c From Articles__c");

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        // Show everything
       //findViewById(R.id.drawer_layout).setVisibility(View.VISIBLE);
    }

    public void getMagazines(String soql) throws UnsupportedEncodingException {

        RestRequest restRequest = RestRequest.getRequestForQuery(ApiVersionStrings.getVersionNumber(this), soql);

        client.sendAsync(restRequest, new RestClient.AsyncRequestCallback() {
            @Override
            public void onSuccess(RestRequest request, final RestResponse result) {
                result.consumeQuietly(); // consume before going back to main thread
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            //MagazineList = new ArrayList<String>();
                            JSONArray records = result.asJSONObject().getJSONArray("records");
                            for (int i = 0; i < records.length(); i++) {
                                //MagazineList.add(records.getJSONObject(i).getString("Name"));

                                Magazine newMagazine = new Magazine();
                                newMagazine.setTitleResId(records.getJSONObject(i).getString("Name"));
                                db = new LocalStorage(getApplicationContext(), null, null, 1);
                                db.insertMagzine(newMagazine);

                            }



                        } catch (Exception e) {
                            onError(e);
                        }
                    }
                });
            }

            @Override
            public void onError(final Exception exception) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(SalesforceDownloadActivity.this,
                                SalesforceDownloadActivity.this.getString(SalesforceSDKManager.getInstance().getSalesforceR().stringGenericError(), exception.toString()),
                                Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }

    public void getArticles(String soql) throws UnsupportedEncodingException {

        RestRequest restRequest = RestRequest.getRequestForQuery(ApiVersionStrings.getVersionNumber(this), soql);

        client.sendAsync(restRequest, new RestClient.AsyncRequestCallback() {
            @Override
            public void onSuccess(RestRequest request, final RestResponse result) {
                result.consumeQuietly(); // consume before going back to main thread
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            //MagazineList = new ArrayList<String>();
                            JSONArray records = result.asJSONObject().getJSONArray("records");
                            db = new LocalStorage(getApplicationContext(), null, null, 1);
                            for (int i = 0; i < records.length(); i++) {
                                //MagazineList.add(records.getJSONObject(i).getString("Name"));
                                boolean dbCheck = db.checkArticle(records.getJSONObject(i).getString("Name"));
                                if(dbCheck == false) {
                                    Article newArticle = new Article();
                                    newArticle.setArticleTitle(records.getJSONObject(i).getString("Name"));
                                    newArticle.setArticleBody(records.getJSONObject(i).getString("Body__c"));

                                    db.insertArticle(newArticle);
                                }
                            }

                            finish();

                        } catch (Exception e) {
                            onError(e);
                        }
                    }
                });
            }

            @Override
            public void onError(final Exception exception) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(SalesforceDownloadActivity.this,
                                SalesforceDownloadActivity.this.getString(SalesforceSDKManager.getInstance().getSalesforceR().stringGenericError(), exception.toString()),
                                Toast.LENGTH_LONG).show();
                        System.out.println(SalesforceDownloadActivity.this.getString(SalesforceSDKManager.getInstance().getSalesforceR().stringGenericError()));
                    }
                });
            }
        });
    }
}
