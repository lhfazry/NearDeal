package com.fazrilabs.neardeal;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.fazrilabs.neardeal.adapter.StoreItemAdapter;
import com.fazrilabs.neardeal.cache.StoreCache;
import com.fazrilabs.neardeal.model.Store;

public class ListActivity extends AppCompatActivity implements AdapterView.OnItemClickListener{
    private StoreItemAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ListView listView = (ListView) findViewById(R.id.listView);
        adapter = new StoreItemAdapter(this, StoreCache.cache);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();

        if(itemId == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Store store = adapter.getItem(position);
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra("store_id", store.id);
        startActivity(intent);
    }
}
