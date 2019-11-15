package com.example.aroundme;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EventInfo extends AppCompatActivity {

    //ArrayList<String> myList;
    List<ListItem> items = new ArrayList<>();
    List<ListItem> itemsFiltered = new ArrayList<>();
    private ListView mylist;
    private ListAdapter adapter;

    private ImageView imageView;
    private TextView title;
    private TextView description;

    private Button sortButton;

    private EditText textBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_info);

        mylist = findViewById(R.id.listViewdepreceded);
        imageView = findViewById(R.id.imageView);
        title = findViewById(R.id.title);
        description = findViewById(R.id.description);

        sortButton = findViewById(R.id.sortButton);
        sortButton.setOnClickListener(SortButtonClick);

        textBox = findViewById(R.id.editTextFilter);
        //textBox.setOnClickListener(TextBoxListener);


        imageView.setImageResource(getIntent().getIntExtra("ImageId", 0));
        title.setText(getIntent().getStringExtra("Title"));
        description.setText(getIntent().getStringExtra("Description"));


        items = (List<ListItem>) getIntent().getSerializableExtra("myItemList");
        //itemsFiltered = (List<ListItem>) getIntent().getSerializableExtra("myItemList");
        if (items != null)
            itemsFiltered.addAll(items);
        adapter = new ListAdapter(this, items);
        mylist.setAdapter((adapter));

        Log.i("info", "items size BEFORE:" + items.size());


        textBox.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                //adapter.getFilter().filter(cs);
                Log.i("info", "text:" + cs);

                if (!cs.toString().isEmpty()) {
                    Log.i("info", "itemsFiltered size:" + itemsFiltered.size());
                    itemsFiltered.clear();
                    Log.i("info", "items size:" + items.size());

                    for(int i = 0; i < items.size(); i++)
                    {
                        if(items.get(i).getTitle().contains(cs))
                        {
                            Log.i("info", "Added Title: " + items.get(i).getTitle());
                            itemsFiltered.add(items.get(i));
                        }
                    }

                    adapter = new ListAdapter(getApplicationContext(), itemsFiltered);
                    mylist.setAdapter(adapter);
                    /*adapter.clear();
                    adapter.addAll(itemsFiltered);
                    adapter.notifyDataSetChanged();*/
                } else {
                    Log.i("info", "Re added Full List");
                    adapter = new ListAdapter(getApplicationContext(), items);
                    mylist.setAdapter(adapter);
                    /*adapter.clear();
                    adapter.addAll(items);
                    adapter.notifyDataSetChanged();*/
                }
            }


            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) { }

            @Override
            public void afterTextChanged(Editable arg0) {}
        });
    }

    View.OnClickListener SortButtonClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            Collections.sort(items);
            adapter.notifyDataSetChanged();

            /*for(int i = 0; i < items.size()-1; i++)
            {
                Log.i("list item Sort INFO SORTED", "Title: "+items.get(i).getTitle());
            }*/
        }
    };


}
