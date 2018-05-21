package com.apptech.RecyclerviewSearch;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.SearchManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.Settings;
import android.provider.Telephony;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.apptech.CircularProgressBar.HoloCircularProgressBar;
import com.apptech.ComplexRecyclerView.Utils.Util;
import com.apptech.MainActivity;
import com.apptech.Retrofit.ApiInterface;
import com.apptech.Retrofit.RetrofitApiClient;
import com.apptech.apptechcomponents.R;
import com.bumptech.glide.Glide;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecyclerviewSearch extends AppCompatActivity implements SearchAdapter.ContactsAdapterListener {

    private static final String TAG = RecyclerviewSearch.class.getSimpleName();

    private ApiInterface apiInterface;
    private RecyclerView recyclerView;
    private List<RecyclerViewSearchModel> searchData;
    private SearchAdapter mAdapter;
    private SearchView searchView;
    private DBHelper dbHelper;
    // variable to track event time
    private long mLastClickTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recyclerview_search);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(getColor(R.color.white));
        setSupportActionBar(toolbar);

        // toolbar fancy stuff
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.toolbar_title);

        //Create an instance of Interface
        apiInterface = RetrofitApiClient.getClient().create(ApiInterface.class);

        dbHelper = new DBHelper(this);

        recyclerView = findViewById(R.id.recycler_view);
        searchData = new ArrayList<>();
        mAdapter = new SearchAdapter(this, searchData, this);

        // white background notification bar
        whiteNotificationBar(recyclerView);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new MyDividerItemDecoration(this, DividerItemDecoration.VERTICAL, 36));
        recyclerView.setAdapter(mAdapter);

        getSearchDataFromServer();

    }


    @Override
    public void onContactSelected(final RecyclerViewSearchModel contact, int pos, View view) {
        // Preventing multiple clicks, using threshold of 1 second
        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
            return;
        }
        mLastClickTime = SystemClock.elapsedRealtime();

        View parent = (View) view.getParent().getParent();
//        int position = recyclerView.getChildViewHolder(parent).getAdapterPosition();
//
//        RecyclerViewSearchModel item = mAdapter.getItem(position);
//        if (item == null) {
//            return;
//        }

        switch (view.getId()) {
            case R.id.download:
                RecyclerViewSearchModel data = dbHelper.getJsonDataObject(pos + 1);
                if (data != null && data.getStatus().equalsIgnoreCase("1")) {
                    deletePopup(RecyclerviewSearch.this, pos, getPath(contact.getImage()), contact.getName());
                } else {
                    displayPopup(contact.getImage(), pos, contact.getName(), parent);
                }

                break;

            case R.id.item_view:

                if (isExist(contact.getImage())) {
                    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(RecyclerviewSearch.this, R.style.MyAlertDialogTheme);
                    LayoutInflater inflater = RecyclerviewSearch.this.getLayoutInflater();
                    View dialogView = inflater.inflate(R.layout.popup_contact, null);
                    dialogBuilder.setView(dialogView);

                    TextView phone = dialogView.findViewById(R.id.pop_phone);
                    phone.setText(contact.getPhone());

                    final AlertDialog alertDialog = dialogBuilder.create();

                    ImageView iv = dialogView.findViewById(R.id.pop_image);
                    if (contact.getImage() == null) {
                        Glide.with(RecyclerviewSearch.this).load(R.drawable.blank_user_bg).into(iv);
                    } else {
                        Glide.with(RecyclerviewSearch.this).load(contact.getImage()).into(iv);
                    }

                    RelativeLayout call = dialogView.findViewById(R.id.call_layout);
                    call.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String number = contact.getPhone();
                            Intent callIntent = new Intent(Intent.ACTION_CALL);
                            callIntent.setData(Uri.parse("tel:" + number));
                            startActivity(callIntent);
                            alertDialog.dismiss();
                        }
                    });

                    RelativeLayout msg = dialogView.findViewById(R.id.msg_layout);
                    msg.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String number = contact.getPhone();  // The number on which you want to send SMS
//                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.fromParts("sms", number, null)));
                            sendSms(RecyclerviewSearch.this, null, number);
                            alertDialog.dismiss();
                        }
                    });

                    RelativeLayout info = dialogView.findViewById(R.id.info_layout);
                    info.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toast.makeText(RecyclerviewSearch.this, "User Info", Toast.LENGTH_SHORT).show();
                        }
                    });

                    //AlertDialog alertDialog = dialogBuilder.create();
                    alertDialog.show();

                } else {
                    Toast.makeText(RecyclerviewSearch.this, " Please Download First", Toast.LENGTH_SHORT).show();
                }

                break;


            default:
                break;
        }
    }

    public static boolean sendSms(Context context, String text, String number) {
        return sendSms(context, text, Collections.singletonList(number));
    }

    public static boolean sendSms(Context context, String text, List<String> numbers) {

        String numbersStr = TextUtils.join(",", numbers);

        Uri uri = Uri.parse("sms:" + numbersStr);

        Intent intent = new Intent();
        intent.setData(uri);
        intent.putExtra(Intent.EXTRA_TEXT, text);
        intent.putExtra("sms_body", text);
        //intent.putExtra("address", numbersStr);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            intent.setAction(Intent.ACTION_SENDTO);
            String defaultSmsPackageName = Telephony.Sms.getDefaultSmsPackage(context);
            if (defaultSmsPackageName != null && !defaultSmsPackageName.isEmpty()) {
                intent.setPackage(defaultSmsPackageName);
            }
        } else {
            intent.setAction(Intent.ACTION_VIEW);
            intent.setType("vnd.android-dir/mms-sms");
        }

        try {
            context.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public void displayPopup(final String url, final int pos, final String name, final View parent) {

        final Dialog dialog = Util.downloadDialog(this);

        TextView txtViewTitle = dialog.findViewById(R.id.txt_title);
        String styledText = "Do you want to download <font color='yellow'>" + name + "</font> " + "Movie?";
        txtViewTitle.setText(Html.fromHtml(styledText), TextView.BufferType.SPANNABLE);

        Button btnOk = dialog.findViewById(R.id.btn_ok);
        btnOk.setText(getString(R.string.button_YES));


        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

                new DownloadItem(pos, url, parent).execute();
            }
        });

        Button btnCancel = (Button) dialog.findViewById(R.id.btn_cancel);
        btnCancel.setText(getString(R.string.button_NO));

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    public void deletePopup(Context context, final int pos, final String path, final String name) {

        final Dialog dialog = Util.downloadDialog(context);

        TextView txtViewTitle = dialog.findViewById(R.id.txt_title);
        String htmlTitle = "Are you sure you want to delete <font color='#76FF03'><i>" + name + "</i></font> " + "from your device?";
        txtViewTitle.setText(Html.fromHtml(htmlTitle), TextView.BufferType.SPANNABLE);

        Button btnCancel = dialog.findViewById(R.id.btn_cancel);
        Button btnOk = dialog.findViewById(R.id.btn_ok);

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

                File f = new File(path);
                if (f != null && f.exists()) {
                    f.delete();
                }

                dbHelper.updateMovie(pos + 1, "0");
//                mAdapter.notifyItemRemoved(pos);// it will also remove recyclerView item view
                mAdapter.notifyItemChanged(pos); // it will just refresh current recyclerView item
            }
        });

        dialog.show();
    }

    public class DownloadItem extends AsyncTask<String, Integer, String> {

        HoloCircularProgressBar progressBar;
        int pos;
        String url;
        View view;

        public DownloadItem(int pos, String url, View view) {
            this.pos = pos;
            this.url = url;
            this.view = view;
        }

        @Override
        protected void onPreExecute() {
            progressBar = view.findViewById(R.id.progress_circuls);
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... voids) {
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

                    publishProgress((int) ((total * 100) / lenghtOfFile));
                    output.write(buffer, 0, bytesRead);
                    Thread.sleep(1000);
                    // Escape early if cancel() is called
                    if (isCancelled()) break;
                }

                output.close();
                input.close();

            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            // HoloCircularProgressBar always accept float value and 1.0 mean complete download that's why need to convert by 100f
            // if use native progressBar just using setProgress(Integer.parseInt(progress[0]));
            progressBar.setProgress(progress[0] / 100f);
        }

        @Override
        protected void onPostExecute(String aVoid) {
            progressBar.setVisibility(View.GONE);

            if (isExist(url)) {
//                dbHelper.updateAllMovieStatus("1"); // update all field in status column
                dbHelper.updateMovie(pos + 1, "1"); // update single item in status column
//                mAdapter.notifyDataSetChanged(); // refresh all item in adapter(recyclerView)
                mAdapter.notifyItemChanged(pos); // refresh for specific item
                Toast.makeText(getApplicationContext(), "Download completed", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), "Download Failed. Maybe need to grant permission", Toast.LENGTH_LONG).show();
            }

        }
    }

    private boolean isExist(String url) {
        String imageName = FilenameUtils.getName(url);
        String sdCardLink = Util.generateSDCardLink(Util.PATH, imageName);
        return new File(sdCardLink).exists();
    }

    private String getPath(String url) {
        String imageName = FilenameUtils.getName(url);
        String sdCardLink = Util.generateSDCardLink(Util.PATH, imageName);
        return sdCardLink;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);

        // listening to search query text change
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // filter recycler view when query submitted
                mAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                // filter recycler view when text is changed
                mAdapter.getFilter().filter(query);
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case android.R.id.home:
                // todo: goto back activity from here
                Intent intent = new Intent(RecyclerviewSearch.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
                return true;

            //noinspection SimplifiableIfStatement
            case R.id.action_search:
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        // close search view on back button pressed
        if (!searchView.isIconified()) {
            searchView.setIconified(true);
            return;
        }
        super.onBackPressed();
    }

    private void whiteNotificationBar(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int flags = view.getSystemUiVisibility();
            flags |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            view.setSystemUiVisibility(flags);
            getWindow().setStatusBarColor(Color.WHITE);
        }
    }

    public void getSearchDataFromServer() {

//        final ProgressDialog dialog;
//        dialog = new ProgressDialog(RecyclerviewSearch.this);
//        dialog.setMessage(getString(R.string.please_wait));
//        dialog.setCancelable(false);
//        dialog.show();

        final AlertDialog progressDialog;
        progressDialog = new SpotsDialog(this, R.style.CustomProgressDialog);
        progressDialog.show();

        ApiInterface apiService = RetrofitApiClient.getClient().create(ApiInterface.class);
        Call<List<RecyclerViewSearchModel>> call = apiService.getSearchData();
        call.enqueue(new Callback<List<RecyclerViewSearchModel>>() {
            @Override
            public void onResponse(Call<List<RecyclerViewSearchModel>> call, Response<List<RecyclerViewSearchModel>> response) {
                if (response.isSuccessful()) {

                    Log.d(TAG, "server contacted and has file");

//                    dialog.dismiss();
                    progressDialog.dismiss();

                    final List<RecyclerViewSearchModel> data = response.body();

                    searchData.clear();
                    searchData.addAll(data);

                    new AsyncTask<Void, Void, Void>() {
                        @Override
                        protected Void doInBackground(Void... voids) {
                            int id = 1;
                            for (RecyclerViewSearchModel model : data) {
                                model.setId(id);
                                dbHelper.insertJsonData(model);
                                id++;
                            }
                            return null;
                        }
                    }.execute();

                    // refreshing recycler view
                    mAdapter.notifyDataSetChanged();


                    // for storage runtime permission
                    requestStoragePermission();

                } else {
                    Log.d(TAG, "server contact failed");
                }
            }

            @Override
            public void onFailure(Call<List<RecyclerViewSearchModel>> call, Throwable t) {
                Log.e(TAG, "error");
            }
        });
    }

    public boolean isPermissionGrantNeeded() {
        //TODO need to sorted out how we will handle permission
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED || checkSelfPermission(Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED)) {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.INTERNET}, 1);

            return true;

        }
        return false;
    }


    /**
     * Requesting multiple permissions (storage and location) at once
     * This uses multiple permission model from dexter
     * On permanent denial opens settings dialog
     */
    private void requestStoragePermission() {
        Dexter.withActivity(this)
                .withPermissions(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {
                            Toast.makeText(getApplicationContext(), "All permissions are granted!", Toast.LENGTH_SHORT).show();
                        }

                        // check for permanent denial of any permission
                        if (report.isAnyPermissionPermanentlyDenied()) {
                            // show alert dialog navigating to Settings
                            showSettingsDialog();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).
                withErrorListener(new PermissionRequestErrorListener() {
                    @Override
                    public void onError(DexterError error) {
                        Toast.makeText(getApplicationContext(), "Error occurred! " + error.toString(), Toast.LENGTH_SHORT).show();
                    }
                })
                .onSameThread()
                .check();
    }

    /**
     * Showing Alert Dialog with Settings option
     * Navigates user to app settings
     * NOTE: Keep proper title and message depending on your app
     */
    private void showSettingsDialog() {
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(RecyclerviewSearch.this);
        builder.setTitle("Need Permissions");
        builder.setMessage("This app needs permission to use this feature. You can grant them in app settings.");
        builder.setPositiveButton("GOTO SETTINGS", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                openSettings();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();

    }

    // navigating user to app settings
    private void openSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 101);
    }
}
