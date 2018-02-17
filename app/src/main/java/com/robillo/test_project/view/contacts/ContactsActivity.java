package com.robillo.test_project.view.contacts;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.opencsv.CSVWriter;
import com.robillo.test_project.R;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class ContactsActivity extends AppCompatActivity implements ContactsActivityMvpView, LoaderManager.LoaderCallbacks<Cursor>,
        AdapterView.OnItemClickListener {

    @SuppressWarnings("FieldCanBeLocal")
    private Button mExtractContacts, mExportCsv;
    List<String[]> mData = new ArrayList<>();

    @SuppressLint({"InlinedApi", "ObsoleteSdkInt"})
    private static final String[] PROJECTION =
            {
                    ContactsContract.Contacts._ID,
                    ContactsContract.Contacts.LOOKUP_KEY,
                    Build.VERSION.SDK_INT
                            >= Build.VERSION_CODES.HONEYCOMB ?
                            ContactsContract.Contacts.DISPLAY_NAME_PRIMARY :
                            ContactsContract.Contacts.DISPLAY_NAME

            };

    // The column index for the _ID column
    private static final int CONTACT_ID_INDEX = 0;
    // The column index for the LOOKUP_KEY column
    private static final int LOOKUP_KEY_INDEX = 1;

    // Defines the text expression
    @SuppressLint({"InlinedApi", "ObsoleteSdkInt"})
    private static final String SELECTION =
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB ?
                    ContactsContract.Contacts.DISPLAY_NAME_PRIMARY + " LIKE ?" :
                    ContactsContract.Contacts.DISPLAY_NAME + " LIKE ?";
    // Defines a variable for the search string
    private String mSearchString = "Robin";
    // Defines the array to hold values that replace the ?
    @SuppressWarnings("MismatchedReadAndWriteOfArray")
    private String[] mSelectionArgs = { mSearchString };

    /*
    * Defines an array that contains column names to move from
     * the Cursor to the ListView.
    */
    @SuppressLint({"InlinedApi", "ObsoleteSdkInt"})
    private final static String[] FROM_COLUMNS = {
            Build.VERSION.SDK_INT
                    >= Build.VERSION_CODES.HONEYCOMB ?
                    ContactsContract.Contacts.DISPLAY_NAME_PRIMARY :
                    ContactsContract.Contacts.DISPLAY_NAME
    };
    /*
     * Defines an array that contains resource ids for the layout views
     * that get the Cursor column contents. The id is pre-defined in
     * the Android framework, so it is prefaced with "android.R.id"
     */
    private final static int[] TO_IDS = {
            android.R.id.text1
    };
    // Define global mutable variables
    // Define a ListView object
    ListView mContactsList;
    // Define variables for the contact the user selects
    // The contact's _ID value
    long mContactId;
    // The contact's LOOKUP_KEY
    String mContactKey;
    // A content URI for the selected contact
    Uri mContactUri;
    // An adapter that binds the result Cursor to the ListView
    private SimpleCursorAdapter mCursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);

        setUp();
    }

    @Override
    public void setUp() {
        mExportCsv = findViewById(R.id.export_csv);
        mExtractContacts = findViewById(R.id.extract_contacts);
        mExtractContacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(ContactsActivity.this, Manifest.permission.READ_CONTACTS)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(ContactsActivity.this,
                            new String[] { Manifest.permission.READ_CONTACTS },
                            100);
                } else {
                    extractContacts();
                }
            }
        });
        mExportCsv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(ContactsActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(ContactsActivity.this,
                            new String[] { Manifest.permission.WRITE_EXTERNAL_STORAGE },
                            101);
                } else {
                    exportToCsv();
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 100 :
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    extractContacts();
                } else {
                    if(ActivityCompat.shouldShowRequestPermissionRationale(this,
                            Manifest.permission.READ_CONTACTS)) {
                        new AlertDialog.Builder(this).
                                setTitle("Read Contacts permission").
                                setMessage("You need to grant read contacts permission to use read" +
                                        " contacts feature. Retry and grant it !").show();
                    } else {
                        new AlertDialog.Builder(this).
                                setTitle("Read Contacts permission denied").
                                setMessage("You denied read contacts permission." +
                                        " So, the feature will be disabled. To enable it" +
                                        ", go on settings and " +
                                        "grant read contacts for the application").show();
                    }
                }
                break;
            case 101 :
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    exportToCsv();
                } else {
                    if(ActivityCompat.shouldShowRequestPermissionRationale(this,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                        new AlertDialog.Builder(this).
                                setTitle("Write External Storage permission").
                                setMessage("You need to grant permission to store" +
                                        " csv feature. Retry and grant it !").show();
                    } else {
                        new AlertDialog.Builder(this).
                                setTitle("Write permission denied").
                                setMessage("You denied write permission." +
                                        " So, the feature will be disabled. To enable it" +
                                        ", go on settings and " +
                                        "grant storage permission for the application").show();
                    }
                }
                break;
        }
    }

    @Override
    public void extractContacts() {
        // Initializes the loader
        getSupportLoaderManager().restartLoader(0, null, ContactsActivity.this);

        // Gets the ListView from the View list of the parent activity
        mContactsList =
                (ListView) findViewById(R.id.contacts_list);
        // Set the item click listener to be the current fragment.
        mContactsList.setOnItemClickListener(this);
        // Gets a CursorAdapter
        mCursorAdapter = new SimpleCursorAdapter(
                this,
                R.layout.contacts_list_item,
                null,
                FROM_COLUMNS, TO_IDS,
                0);
        // Sets the adapter for the ListView
        mContactsList.setAdapter(mCursorAdapter);
    }

    @Override
    public void exportToCsv() {
        if(mData.size()>0){
            Observable.fromCallable(() -> {
                try {
                    String baseDir = android.os.Environment.getExternalStorageDirectory().getAbsolutePath();
                    String fileName = "AnalysisData.csv";
                    String filePath = baseDir + File.separator + fileName;
                    File f = new File(filePath);

                    CSVWriter writer;
                    FileWriter mFileWriter;
                    if(f.exists() && !f.isDirectory()){
                        mFileWriter = new FileWriter(filePath , true);
                        writer = new CSVWriter(mFileWriter);
                    }
                    else {
                        writer = new CSVWriter(new FileWriter(filePath));
                    }
                    writer.writeAll(mData);
                    writer.close();
                } catch (IOException e) {
                }
                return false;
            })
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe((result) -> {
                        //Use result for something
                        Snackbar.make(findViewById(R.id.parent), "Export successful for the contacts CSV", Snackbar.LENGTH_LONG).show();
                    });
        }
        else {
            Toast.makeText(ContactsActivity.this, R.string.export_click, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        /*
         * Makes search string into pattern and
         * stores it in the selection array
         */
        mSelectionArgs[0] = "%" + mSearchString + "%";
        // Starts the query
        return new CursorLoader(
                this,
                ContactsContract.Contacts.CONTENT_URI,
                PROJECTION,
                null,
                null,
                null
        );
    }

    @SuppressLint("ObsoleteSdkInt")
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        // Put the result Cursor in the adapter for the ListView
        mCursorAdapter.swapCursor(data);
        if (data.moveToFirst()){
            do{
                mData.add(new String[]{data.getString(data.getColumnIndex(ContactsContract.Contacts._ID)),
                        data.getString(data.getColumnIndex(ContactsContract.Contacts.LOOKUP_KEY)),
                        data.getString(data.getColumnIndex(Build.VERSION.SDK_INT
                                >= Build.VERSION_CODES.HONEYCOMB ?
                                ContactsContract.Contacts.DISPLAY_NAME_PRIMARY :
                                ContactsContract.Contacts.DISPLAY_NAME))});
                // do what ever you want here
            }while(data.moveToNext());
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        // Delete the reference to the existing Cursor
        mCursorAdapter.swapCursor(null);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View item, int position, long rowID) {
        //not required
    }
}
