package com.apptech.ComplexRecyclerView.Activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.apptech.ComplexRecyclerView.Adapter.ComplexAdapter;
import com.apptech.ComplexRecyclerView.Database.DBHelper;
import com.apptech.ComplexRecyclerView.Model.Model;
import com.apptech.ComplexRecyclerView.Utils.MovieApp;
import com.apptech.ComplexRecyclerView.Utils.PreferenceUtil;
import com.apptech.ComplexRecyclerView.Utils.Util;
import com.apptech.Retrofit.ApiInterface;
import com.apptech.Retrofit.RetrofitApiClient;
import com.apptech.apptechcomponents.R;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ComplexRecyclerView extends AppCompatActivity {

    private static String TAG = ComplexRecyclerView.class.getSimpleName();
    private PreferenceUtil preferenceUtil;
    private RecyclerView complexRecyclerView;
    private ApiInterface apiInterface;
    public ComplexAdapter adapter;
    public DBHelper dbHelper;
    private int selectedCarIndex = 0;
    private Model obj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complex_recycler_view);

        dbHelper = new DBHelper(this);
        complexRecyclerView = (RecyclerView) findViewById(R.id.complex_recycler_view);
        preferenceUtil = new PreferenceUtil(this);

        //Create an instance of Interface
        apiInterface = RetrofitApiClient.getClient().create(ApiInterface.class);

    }

    @Override
    protected void onResume() {
        if (preferenceUtil.getIsFirstTime()) {
            if (new File(Util.PATH).exists()) {
                try {
                    FileUtils.deleteDirectory(new File(Util.PATH));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        new LoadDataBase().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);

        super.onResume();
    }

    public class LoadDataBase extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            if (preferenceUtil.getIsDatabaseEmpty()) {
                getDataFromServer();
                preferenceUtil.setIsDatabaseEmpty(false);
            } else {

            }

            return "executed";
        }

        @Override
        protected void onPostExecute(String s) {
            MovieApp.getInstance().setCarList(dbHelper.getAllOderMovieList());

            for (int i = 0; i < MovieApp.getInstance().getCarList().size(); i++) {
                Model info = (Model) MovieApp.getInstance().getCarList().get(i);
                if (("1".equalsIgnoreCase(info.getStatus()))) {
                    if (!MovieApp.getInstance().getCarList().contains("Downloaded Car")) {
                        MovieApp.getInstance().getCarList().add(i, "Downloaded Car");
                    }
                } else if (("0".equalsIgnoreCase(info.getStatus()))){
                    if (!MovieApp.getInstance().getCarList().contains("Available for Download")) {
                        MovieApp.getInstance().getCarList().add(i, "Available for Download");
                    }
                } else if (("2".equalsIgnoreCase(info.getStatus()))){
                    if (!MovieApp.getInstance().getCarList().contains("Previous Car")) {
                        MovieApp.getInstance().getCarList().add(i, "Previous Car");
                    }
                }
            }

            if (MovieApp.getInstance().getCarList() != null) {
                adapter = new ComplexAdapter(ComplexRecyclerView.this, MovieApp.getInstance().getCarList(), clickListener);
                complexRecyclerView.setLayoutManager(new LinearLayoutManager(ComplexRecyclerView.this));
                complexRecyclerView.setAdapter(adapter);
            } else {
                Toast.makeText(getApplicationContext(), "Empty", Toast.LENGTH_SHORT).show();
            }

            int lastId = dbHelper.getLastID();
            TextView t = (TextView)findViewById(R.id.no_data_available);
            if (lastId == 0) {
                t.setVisibility(View.VISIBLE);
            } else {
                t.setVisibility(View.GONE);
            }

        }
    }

    // get json data from server
    public void getDataFromServer() {
        ApiInterface apiService = RetrofitApiClient.getClient().create(ApiInterface.class);

        Call<List<Model>> call = apiService.getInbox();
        call.enqueue(new Callback<List<Model>>() {
            @Override
            public void onResponse(Call<List<Model>> call, Response<List<Model>> response) {

                if (response.isSuccessful()) {
                    int id = 1;

                    for (Model model : response.body()) {
                        model.setId(id);

                        if (model.getId() == 5 || model.getId() == 10 || model.getId() == 15) {
                            model.setStatus("2");
                        } else {
                            model.setStatus("0");
                        }

                        dbHelper.insertJsonData(model);

                        id++;
                    }

                } else {
                    // if response not success
                }
            }

            @Override
            public void onFailure(Call<List<Model>> call, Throwable t) {
                Log.e(TAG, t.toString());
            }
        });

    }

    private ComplexAdapter.ClickListener clickListener = new ComplexAdapter.ClickListener() {
        @Override
        public void clickListener(View view, int pos, Object o) {
            selectedCarIndex = pos;
            obj = (Model) o;
            String imageName = FilenameUtils.getName(((Model) MovieApp.getInstance().getCarList().get(pos)).getImage());
            String sdCardLink = Util.generateSDCardLink(Util.PATH, imageName);

            switch (view.getId()) {
                case R.id.img_btn_delete_or_download:

                    if (!new File(sdCardLink).exists()) {
                        if (Util.checkInternetConnection(ComplexRecyclerView.this)) {
                            if (obj.getId() == 5 || obj.getId() == 10) {
                                Toast.makeText(getApplicationContext(), "working...", Toast.LENGTH_SHORT).show();
                            } else {
                                downloadPopup(pos);
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), getResources().getString(R.string.internet_connect), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        deletePopup(ComplexRecyclerView.this, pos, sdCardLink);
//                        dbHelper.deleteContact(obj.getId());
                        if (obj.getId() == 5 || obj.getId() == 10 || obj.getId() == 15) {
                            dbHelper.updateMovie(obj.getId(), "2");
                        } else {
                            dbHelper.updateMovie(obj.getId(), "0");
                        }

                    }

                    break;
                case R.id.item_view:

                    if (new File(sdCardLink).exists()) {
                        Toast.makeText(getApplicationContext(), "position " + pos, Toast.LENGTH_SHORT).show();

/*                        Intent intent = new Intent();
                        intent.setAction(android.content.Intent.ACTION_VIEW);
                        Uri uri = Uri.parse("file://" + new File(sdCardLink));
                        intent.setDataAndType(uri,"image");
                        startActivity(intent);*/

                    } else {
                        Toast.makeText(getApplicationContext(), "Download First " + pos, Toast.LENGTH_SHORT).show();
                    }

                    break;
            }
        }
    };

    public void deletePopup(Context context, final int pos, final String path) {

        final Dialog dialog = Util.downloadDialog(context);

        TextView txtViewTitle = (TextView) dialog.findViewById(R.id.txt_title);
        txtViewTitle.setText(context.getString(R.string.delete_confirmation));

        Button btnCancel = (Button) dialog.findViewById(R.id.btn_cancel);
        Button btnOk = (Button) dialog.findViewById(R.id.btn_ok);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                adapter.removeItem(pos, path);
                onResume();
            }
        });

        dialog.show();
    }

    private void downloadPopup(final int pos) {
        final Dialog dialog = Util.downloadDialog(ComplexRecyclerView.this);

        TextView txtViewTitle = (TextView) dialog.findViewById(R.id.txt_title);
        txtViewTitle.setText(getResources().getString(R.string.alert_msg));

        Button btnCancel = (Button) dialog.findViewById(R.id.btn_cancel);
        Button btnOk = (Button) dialog.findViewById(R.id.btn_ok);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                saveImageOnSDCard(pos);
            }
        });

        dialog.show();
    }

    private void saveImageOnSDCard(int pos) {

        if (MovieApp.getInstance().getCarList().size() > 0) {

            String url = ((Model) MovieApp.getInstance().getCarList().get(pos)).getImage();

            String imageName = FilenameUtils.getName(url);
            String sdCardLink = Util.generateSDCardLink(Util.PATH, imageName);

            if (!new File(sdCardLink).exists()) {
                new DownloadImage(url, pos).execute();
            } else {
                Toast.makeText(getApplicationContext(), "Already downloaded", Toast.LENGTH_SHORT).show();
            }
        }
    }

    class DownloadImage extends AsyncTask<String, String, String> {

        ProgressDialog pDialog;
        String url;
        int pos;

        public DownloadImage(String url, int pos) {
            this.url = url;
            this.pos = pos;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(ComplexRecyclerView.this);
            pDialog.setMessage("Downloading file. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setMax(100);
            pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                String movieName = FilenameUtils.getName(url);
                URL link = new URL(url);
                InputStream input = link.openStream();

                URLConnection conection = link.openConnection();
                conection.connect();
                // getting file length
                int lenghtOfFile = conection.getContentLength();

                String storagePath = Util.generateSDCardLink(Util.PATH, movieName);
                OutputStream output = new FileOutputStream(new File(storagePath));

                byte[] buffer = new byte[2048];
                int bytesRead = 0;

                long total = 0;

                while ((bytesRead = input.read(buffer, 0, buffer.length)) >= 0) {
                    total += bytesRead;

                    publishProgress("" + (int) ((total * 100) / lenghtOfFile));
                    output.write(buffer, 0, bytesRead);
                }

                output.close();
                input.close();

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(String... progress) {
//            pDialog.setProgress(Integer.parseInt(progress[0]));
            pDialog.setProgress(Integer.parseInt(progress[0]));
        }

        @Override
        protected void onPostExecute(String aVoid) {
            pDialog.cancel();

            dbHelper.updateMovie(obj.getId(), "1");
            adapter.notifyDataSetChanged();

            onResume();
            Toast.makeText(getApplicationContext(), "Download Finished", Toast.LENGTH_SHORT).show();
        }
    }


}
