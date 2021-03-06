package com.kenfestoche.smartcoder.kenfestoche.model;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Environment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.kenfestoche.smartcoder.kenfestoche.ModuleSmartcoder;
import com.kenfestoche.smartcoder.kenfestoche.R;
import com.kenfestoche.smartcoder.kenfestoche.UserProfil;
import com.kenfestoche.smartcoder.kenfestoche.webservices.WebService;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.Object;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by smartcoder on 25/07/16.
 */
public class ImagesProfil extends BaseAdapter {


    int mGalleryItemBackground;
    private Context mContext;
    public final LayoutInflater mInflater;
    File[] images;
    File[] files;
    ImageView ID;
    JSONArray ListPhotos;
    Holder holder;
    Utilisateur User;
    JSONObject photo;
    private Activity parentActivity;


    public ImagesProfil(Context c, int folderID, Utilisateur user, Activity parent) {
        mContext = c;
        mInflater = LayoutInflater.from(c);
        User = user;

        WebService WS = new WebService(mContext,false);
        ListPhotos =  WS.GetListPhotos(user);
        parentActivity=parent;

    }

    @Override
    public int getCount() {
        return ListPhotos.length();
    }

    @Override
    public Object getItem(int i) {
        try {
            return ListPhotos.get(i);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    public class Holder
    {
        //TextView tv;
        ImageView img;
        ImageView imgdelete;
        TextView txID;
    }


    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ImageView imageView = null;


        View rowView;

        holder=new Holder();

        rowView = mInflater.inflate(R.layout.rowphotoprofil, null);
        holder.img=(ImageView) rowView.findViewById(R.id.imphotoprofil);

        holder.imgdelete=(ImageView) rowView.findViewById(R.id.imdeletephoto);
        holder.txID = (TextView) rowView.findViewById(R.id.txID);

        //JSONObject photo = null;
        try {
            photo = ListPhotos.getJSONObject(i);
            holder.txID.setText( photo.getString("ID"));

        } catch (JSONException e) {
            e.printStackTrace();
        }


        URL url = null;
        try {

            Picasso.with(rowView.getContext()).load("http://www.smartcoder-dev.fr/ZENAPP/webservice/" +  photo.getString("photo")).into(holder.img);
            //holder.img.setImageBitmap(image);
            holder.imgdelete.setId(photo.getInt("ID"));
            if(photo.getString("photo").equals("./imgprofil/ajoutphoto.png")){
                holder.img.setId(0);
                holder.imgdelete.setVisibility(View.INVISIBLE);
                holder.imgdelete.setId(photo.getInt("ID"));
            }
            //imageView.setImageBitmap(image);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        holder.imgdelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ID = (ImageView) view;
                AlertDialog.Builder builder1 = new AlertDialog.Builder(parentActivity);
                builder1.setMessage("Voulez vous supprimer la photo ?");
                builder1.setCancelable(true);

                builder1.setPositiveButton(
                        "Oui",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();


                                //ActivityProfil.RefreshAdapter();
                                parentActivity.runOnUiThread(new Runnable() {

                                    @Override
                                    public void run() {
                                        Integer TagID = ID.getId();
                                        WebService WS = new WebService(mContext,false);
                                        WS.DeletePhotoProfil(TagID);
                                        ListPhotos =  WS.GetListPhotos(User);
                                        notifyDataSetChanged();

                                    }
                                });
                                //notifyDataSetChanged();
                            }
                        });

                builder1.setNegativeButton(
                        "Non",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alert11 = builder1.create();
                alert11.show();

            }
        });



        //return imageView;

        return rowView;
    }

}
