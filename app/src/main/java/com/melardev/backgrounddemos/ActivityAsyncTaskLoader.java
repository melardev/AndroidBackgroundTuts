package com.melardev.backgrounddemos;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class ActivityAsyncTaskLoader extends AppCompatActivity implements LoaderManager.LoaderCallbacks<SharedPreferences> {

    private static final String KEY = "prefs";
    private TextView txtResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_async_task_loader);
        txtResult = (TextView) findViewById(R.id.txtAsyncTaskLoaderResult);
        getSupportLoaderManager().initLoader(0, null, this);
    }

    @Override
    public Loader<SharedPreferences> onCreateLoader(int id, Bundle args) {
        return new SharedPreferenceLoader(this);
    }

    @Override
    public void onLoadFinished(Loader<SharedPreferences> loader, SharedPreferences prefs) {
        int value = prefs.getInt(KEY, 0);
        value += 1;
        txtResult.setText(String.valueOf(value));
        // update value
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(KEY, value);
        SharedPreferenceLoader.persist(editor);
    }

    @Override
    public void onLoaderReset(Loader<SharedPreferences> loader) {

    }

    public static class SharedPreferenceLoader extends AsyncTaskLoader<SharedPreferences> implements SharedPreferences.OnSharedPreferenceChangeListener {
        private SharedPreferences prefs = null;

        public SharedPreferenceLoader(Context context) {
            super(context);
        }

        public static void persist(SharedPreferences.Editor editor) {
            editor.commit();
        }

        @Override
        protected void onStartLoading() {
            super.onStartLoading();
            if (prefs != null)
                deliverResult(prefs); //if Loader was already loaded, return directly?
            else if (isContentChanged() || prefs == null)
                forceLoad(); //note the difference between forceLoad() and startLoading(), after this, onLoadFinished will be called?
        }

        private boolean isContentChanged() {
            return false;
        }

        @Override
        public SharedPreferences loadInBackground() {
            //Called after startLoading() or forceLoad()
            prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
            prefs.registerOnSharedPreferenceChangeListener(this);
            return prefs; //will trigger the deliverResult() to be called
        }

        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            onContentChanged();
        }
    }
}
