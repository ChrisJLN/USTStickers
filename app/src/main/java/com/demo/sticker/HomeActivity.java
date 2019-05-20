package com.demo.sticker;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.GridView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ImageButton;
import android.content.Intent;
import android.net.Uri;
import java.io.File;
import android.graphics.Bitmap;
import java.io.FileOutputStream;
import android.support.v4.content.FileProvider;
import java.io.IOException;
import android.graphics.BitmapFactory;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.Toast;

import com.demo.sticker.BuildConfig;

public class HomeActivity extends AppCompatActivity {

    public static Integer currentSticker = 0;
    ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        if(!prefs.getBoolean("firstTime", false)) {
            // run your one time code
            AppIndexingService.enqueueWork(HomeActivity.this);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("firstTime", true);
            editor.commit();
        }

        GridView gridView = (GridView)findViewById(R.id.gridview);
        final StickersAdapter stickersAdapter = new StickersAdapter(this, stickers);
        gridView.setAdapter(stickersAdapter);

        image = (ImageView)findViewById(R.id.mainImageView);

        ImageButton ShareStickerBtn = findViewById(R.id.shareSticker);
        ShareStickerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Bitmap icon = BitmapFactory.decodeResource(HomeActivity.this.getResources(),stickers[currentSticker].getImageResource());
                shareImage(icon);

            }
        });

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View view, int position, long id) {
                Sticker sticker = stickers[position];

                image.setImageResource(sticker.getImageResource());
                currentSticker = position;

                // This tells the GridView to redraw itself
                // in turn calling StickerAdapter's getView method again for each cell
                stickersAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        // set back the currently selected sticker we're viewing
        image.setImageResource(stickers[currentSticker].getImageResource());

    }

    private void shareImage(Bitmap bitmap){
        // save bitmap to cache directory
        try {
            File cachePath = new File(this.getCacheDir(), "images");
            cachePath.mkdirs(); // don't forget to make the directory
            FileOutputStream stream = new FileOutputStream(cachePath + "/image.png"); // overwrites this image every time
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            stream.close();

        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Failed to copy sticker", Toast.LENGTH_SHORT)
                    .show();
        }
        File imagePath = new File(this.getCacheDir(), "images");
        File newFile = new File(imagePath, "image.png");
        Uri contentUri = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID + ".fileprovider", newFile);

        if (contentUri != null) {
            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); // temp permission for receiving app to read this file
            shareIntent.setDataAndType(contentUri, getContentResolver().getType(contentUri));
            shareIntent.putExtra(Intent.EXTRA_STREAM, contentUri);
            shareIntent.setType("image/png");
            startActivity(Intent.createChooser(shareIntent, "Choose an app"));
        }
    }

    //Sticker Listing
    //ADD NEW STICKERS HERE
    //Please give name,drawable folder,url and keywords
    public static Sticker[] stickers = {
        new Sticker("chris evans", R.drawable.chris_evans,
                "https://firebasestorage.googleapis.com/v0/b/uststickers.appspot.com/o/chris_evans.png?alt=media&token=3101330e-a394-4b75-a442-f45224993cfc", new String [] {"chris","evans","chrisevans"}),
        new Sticker("dr ludwick", R.drawable.dr_ludwick,
                    "https://firebasestorage.googleapis.com/v0/b/uststickers.appspot.com/o/dr_ludwick.png?alt=media&token=146fa04e-e48d-44d5-9c53-fee7b7d7413f", new String [] {"dr","ludwick","drludwick"}),
        new Sticker("dr ludwick golfcart", R.drawable.dr_ludwick_golfcart,
                    "https://firebasestorage.googleapis.com/v0/b/uststickers.appspot.com/o/dr_ludwick_golfcart.png?alt=media&token=df5a0c8d-12bc-4c33-9071-406d023a1957", new String [] {"golfcart","drludwick","ludwickgolfcart"}),
        new Sticker("dr luckwick graduation", R.drawable.dr_ludwick_graduation,
                    "https://firebasestorage.googleapis.com/v0/b/uststickers.appspot.com/o/dr_ludwick_graduation.png?alt=media&token=7877d10c-0193-45c3-ae00-9f2d2c1b95c4", new String [] {"graduation","ludwickgraduation","drgraduation","congrats"}),
        new Sticker("dr ludwick plane", R.drawable.dr_ludwick_plane,
                    "https://firebasestorage.googleapis.com/v0/b/uststickers.appspot.com/o/dr_ludwick_plane.png?alt=media&token=1adb200f-b391-44a7-9eff-686cff285d36", new String [] {"plane","ludwickplane","drplane"}),
        new Sticker("dr ludwick slaying dragon", R.drawable.dr_ludwick_slayingdragon,
                    "https://firebasestorage.googleapis.com/v0/b/uststickers.appspot.com/o/dr_ludwick_slayingdragon.png?alt=media&token=efc12dfb-53eb-45dc-81ef-0e31d6cfec22", new String [] {"slayingdragon","drdragon","ludwickdragon"}),
        new Sticker("hoUSTon", R.drawable.houston,
                    "https://firebasestorage.googleapis.com/v0/b/uststickers.appspot.com/o/hoUSTon.png?alt=media&token=1fd80d91-f15a-459d-8f96-14ee498033a2", new String [] {"houston","ust","UST"}),
        new Sticker("lenny graduation 1", R.drawable.lenny_graduation1,
                    "https://firebasestorage.googleapis.com/v0/b/uststickers.appspot.com/o/lenny_graduation1.png?alt=media&token=2f95c3cb-50be-4002-a011-2894a46edf87", new String [] {"lennygraduation","lenny","graduation1"}),
        new Sticker("lenny graduation 2", R.drawable.lenny_graduation2,
                    "https://firebasestorage.googleapis.com/v0/b/uststickers.appspot.com/o/lenny_graduation2.png?alt=media&token=e508e7ad-79c5-4a18-8bbb-901ba8a65a93", new String [] {"lennygraduation","lenny","graduation2"}),
        new Sticker("maximilion kolbe haha", R.drawable.maximilion_kolbe_haha,
                    "https://firebasestorage.googleapis.com/v0/b/uststickers.appspot.com/o/maximilion_kolbe_haha.png?alt=media&token=2a0cc080-9f08-420e-8e5c-329492204e6e", new String [] {"haha","maximilion","kolbe","lol"}),
        new Sticker("maximilion kolbe normal", R.drawable.maximilion_kolbe_normal,
                    "https://firebasestorage.googleapis.com/v0/b/uststickers.appspot.com/o/maximilion_kolbe_normal.png?alt=media&token=41dfa1a5-63ab-48f1-ae90-0dda74c8ca0f", new String [] {"kolbe","maximilion","normal"}),
        new Sticker("maximilion kolbe music", R.drawable.maximilion_kolbe_music,
                    "https://firebasestorage.googleapis.com/v0/b/uststickers.appspot.com/o/maximilion_kolbe_music.png?alt=media&token=32e85dad-4e37-44f1-b24b-e7559f91cfdf", new String [] {"kolbe","maximilion","music","sound"}),
        new Sticker("maximilion kolbe thumbsup", R.drawable.maximilion_kolbe_thumbsup,
                    "https://firebasestorage.googleapis.com/v0/b/uststickers.appspot.com/o/maximilion_kolbe_thumbsup.png?alt=media&token=68c140b9-36be-47cb-851f-29ea7dd1f836", new String [] {"thumbsup","ok","good","maximilion","kolbe"}),
        new Sticker("st thomas aquinas chalkboard", R.drawable.st_thomas_aquinas_chalkboard,
                    "https://firebasestorage.googleapis.com/v0/b/uststickers.appspot.com/o/st_thomas_aquinas_chalkboard.png?alt=media&token=33253d88-a665-4773-b052-14a153590d79", new String [] {"st thomas","thomas","chalkboard","teacher","stthomas"}),
        new Sticker("st thomas aquinas goals", R.drawable.st_thomas_aquinas_goals,
                    "https://firebasestorage.googleapis.com/v0/b/uststickers.appspot.com/o/st_thomas_aquinas_goals.png?alt=media&token=26b6e586-9e65-4c4a-8489-0b9a32cfe9dc", new String [] {"goals","goal","stthomas","thomasgoals"}),
        new Sticker("st thomas aquinas normal", R.drawable.st_thomas_aquinas_normal,
                    "https://firebasestorage.googleapis.com/v0/b/uststickers.appspot.com/o/st_thomas_aquinas_normal.png?alt=media&token=b1f1609d-3d41-4305-84f7-40837a94fb2c", new String [] {"stthomas","normalthomas","st thomas","aquinas"}),
        new Sticker("st thomas aquinas sleeping", R.drawable.st_thomas_aquinas_sleeping,
                    "https://firebasestorage.googleapis.com/v0/b/uststickers.appspot.com/o/st_thomas_aquinas_sleeping.png?alt=media&token=53b29d99-6814-4cb9-a922-ad526d58efaa", new String [] {"sleeping","aquinas","stthomas","zzz"}),
        new Sticker("st thomas aquinas whataburger", R.drawable.st_thomas_aquinas_whatburger,
                    "https://firebasestorage.googleapis.com/v0/b/uststickers.appspot.com/o/st_thomas_aquinas_whatburger.png?alt=media&token=dc7f7847-7559-4b2c-838e-0c7276f9bf0f", new String [] {"whataburger","stthomas","aquinasburger","burger","eat"}),
        new Sticker("taking it to the max", R.drawable.takingittothemax,
                    "https://firebasestorage.googleapis.com/v0/b/uststickers.appspot.com/o/takingittothemax.png?alt=media&token=48708f33-f8be-4d53-bddf-0d8b36bed699", new String [] {"tothemax","100","takingit","max","takingittothemax"}),
    };
}