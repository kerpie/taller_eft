package com.androidheroes.library;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity implements CategoriesFragment.OnCategorySelected, BookIndexFragment.OnBookSelected{

    BookIndexFragment book_index;

    FragmentManager fragment_manager;
    FragmentTransaction fragment_transaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragment_manager = getSupportFragmentManager();
        fragment_transaction = fragment_manager.beginTransaction();

        book_index = (BookIndexFragment) fragment_manager.findFragmentById(R.id.books_fragment);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCategorySelected(int category_id) {
        book_index.makeRequest(category_id);
    }

    @Override
    public void onBookSelected(int book_id) {
        Intent intent = new Intent(MainActivity.this, BookDetailActivity.class);

        intent.putExtra("book_id", book_id);

        startActivity(intent);
    }
}
