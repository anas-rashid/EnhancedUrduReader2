package com.example.manan.enhancedurdureader.Activities;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.manan.enhancedurdureader.ApplicationEntities.Magazine;
import com.example.manan.enhancedurdureader.CustomViews.NastaleeqEditText;
import com.example.manan.enhancedurdureader.DataStorage.LocalStorage;
import com.example.manan.enhancedurdureader.R;

import java.io.File;

public class NewMagazineActivity extends AppCompatActivity {

    NastaleeqEditText magNameView;
    ImageView magCoverView;
    Button magAddBtn;
    Button magDiscardBtn;

    LocalStorage db;
    Magazine newMagzine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_magazine);

        newMagzine = new Magazine();
        db = new LocalStorage(this, null, null, 1);

        magNameView = (NastaleeqEditText) findViewById(R.id.magzine_name);
        magCoverView = (ImageView) findViewById(R.id.magzine_cover);
        magAddBtn = (Button) findViewById(R.id.add_magzine_btn);
        magDiscardBtn = (Button) findViewById(R.id.discard_magzine_btn);

        magCoverView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, 1);

            }
        });

        magAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                saveMagzine();

            }
        });

        magDiscardBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewMagazineActivity.this.finish();
            }
        });
    }

    private void saveMagzine(){
        newMagzine.setEditorId(1);
        newMagzine.setTitleResId(magNameView.getText().toString());

        try {

            db.insertMagzine(newMagzine);
            Toast.makeText(this, "Magazine Saved Successfully", Toast.LENGTH_SHORT).show();
        } catch (SQLiteException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }


    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                Uri selectedImage = data.getData();
                String[] filePathColumn = { MediaStore.Images.Media.DATA };
                Cursor cursor = getContentResolver().query(selectedImage,filePathColumn, null, null, null);
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                newMagzine.setImageResId( cursor.getString(columnIndex));
                cursor.close();

                if(newMagzine.getImageResId()!=null) {
                    /*BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inSampleSize = 3;

                    magCoverView.setImageBitmap(BitmapFactory.decodeFile(newMagzine.getImageResId(),options));
                    */
                    Glide.with(this).load(new File(newMagzine.getImageResId())).into(magCoverView);
                }
            }
        }
    }
}
