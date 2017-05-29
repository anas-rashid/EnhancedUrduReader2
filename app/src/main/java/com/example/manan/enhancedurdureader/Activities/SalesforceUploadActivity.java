package com.example.manan.enhancedurdureader.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.example.manan.enhancedurdureader.ApplicationEntities.Article;
import com.example.manan.enhancedurdureader.DataStorage.LocalStorage;
import com.example.manan.enhancedurdureader.R;
import com.salesforce.androidsdk.app.SalesforceSDKManager;
import com.salesforce.androidsdk.rest.ApiVersionStrings;
import com.salesforce.androidsdk.rest.RestClient;
import com.salesforce.androidsdk.rest.RestRequest;
import com.salesforce.androidsdk.rest.RestResponse;
import com.salesforce.androidsdk.ui.SalesforceActivity;

import org.json.JSONArray;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by anas on 5/28/17.
 */

public class SalesforceUploadActivity extends SalesforceActivity {

    RestClient client;
    ArrayList<String> MagazineList;
    LocalStorage db;

    Article article = new Article();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setContentView(R.layout.salesforce_layout);

        Toast.makeText(this, "Salesforce Activity", Toast.LENGTH_LONG).show();
        Intent in = getIntent();
        article.set_id(in.getIntExtra("id", 1));
        article.setArticleTitle(in.getStringExtra("name"));
        article.setArticleBody(in.getStringExtra("body"));
       // article.set_id(in.getIntExtra("id", 1));
        // db = new LocalStorage(this, null, null, 1);
        //MagazineList = new ArrayList<String>();

    }

    @Override
    public void onResume(RestClient client) {
        // Keeping reference to rest client
        this.client = client;
        try {

            Intent in = getIntent();
            article.set_id(in.getIntExtra("id", 1));
            article.setArticleTitle(in.getStringExtra("name"));
            article.setArticleBody(in.getStringExtra("body"));

            sendRequest(article);
            //getArticles("Select Name, Body__c, Magazine__c From Articles__c");

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        // Show everything
        //findViewById(R.id.drawer_layout).setVisibility(View.VISIBLE);
    }

    public void sendRequest(Article article) throws UnsupportedEncodingException {

        Map<String,Object> fields = new HashMap<String,Object>();
        fields.put("Name", article.getArticleTitle());
        fields.put("Body__c", article.getArticleBody());

        RestRequest restRequest = null;
        try {
            restRequest = RestRequest.getRequestForUpsert(ApiVersionStrings.getVersionNumber(this), "Articles__c", "App_Id__c", String.valueOf(article.get_id()), fields);
        } catch (IOException e) {
            e.printStackTrace();
        }

        client.sendAsync(restRequest, new RestClient.AsyncRequestCallback() {
            @Override
            public void onSuccess(RestRequest request, final RestResponse result) {
                try {
                    Log.d("Result from Upload", result.asString().toString());// consume before going back to main thread
                    finish();
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.v("Exception", e.getMessage());
                }
               /* runOnUiThread(new Runnable() {
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
                });*/
            }

            @Override
            public void onError(final Exception exception) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(SalesforceUploadActivity.this,
                                SalesforceUploadActivity.this.getString(SalesforceSDKManager.getInstance().getSalesforceR().stringGenericError(), exception.toString()),
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
                            for (int i = 0; i < records.length(); i++) {
                                //MagazineList.add(records.getJSONObject(i).getString("Name"));

                                Article newArticle = new Article();
                                newArticle.setArticleTitle(records.getJSONObject(i).getString("Name"));
                                newArticle.setArticleBody(records.getJSONObject(i).getString("Body__c"));
                                db = new LocalStorage(getApplicationContext(), null, null, 1);
                                db.insertArticle(newArticle);
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
                        Toast.makeText(SalesforceUploadActivity.this,
                                SalesforceUploadActivity.this.getString(SalesforceSDKManager.getInstance().getSalesforceR().stringGenericError(), exception.toString()),
                                Toast.LENGTH_LONG).show();
                        System.out.println(SalesforceUploadActivity.this.getString(SalesforceSDKManager.getInstance().getSalesforceR().stringGenericError()));
                    }
                });
            }
        });
    }
}
