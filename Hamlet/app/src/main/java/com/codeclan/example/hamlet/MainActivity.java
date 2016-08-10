package com.codeclan.example.hamlet;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
    ArrayList<String> taskList = null;
    //Presents an arraylist object into a list view
    ArrayAdapter<String> ladapter = null;
    ListView lview = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        taskList = getTaskValue(getApplicationContext());
        //Create empty arraylist
        //taskList = new ArrayList<>();
        //populate with some initial test data
        //taskList.addAll(Arrays.asList("Do this", "Do that"));
        //Create new list view adapter
        ladapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, taskList);
        lview = (ListView) findViewById(R.id.listView);
        lview.setAdapter(ladapter);

        lview.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            public void onItemClick(AdapterView parent, View view, final int position, long id){
                String selectedTask = ((TextView) view).getText().toString();
                if (selectedTask.equals(taskList.get(position))) {
                    deleteTask(selectedTask, position);
                }
                   else {
                        Toast.makeText(getApplicationContext(),"Error removing Task", Toast.LENGTH_LONG).show();
                    }

            }
        });

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

        if (id == R.id.action_addTask){
            //Open Add Task Dialog Box
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Add Task");
            //Enter Task Text
            final EditText input = new EditText(this);
            builder.setView(input);
            builder.setPositiveButton("Add", new DialogInterface.OnClickListener(){
                //Clicking "Add" adds task to list
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    taskList.add(capFirstLetter(input.getText().toString()));
                    storeTaskValue(taskList, getApplicationContext());
                    lview.setAdapter(ladapter);
                }
            });
            builder.setNegativeButton("Canx", new DialogInterface.OnClickListener(){
                //Clicking "Canx" closes dialog box
                @Override
                public void onClick(DialogInterface dialog, int which){
                    dialog.cancel();
                }
            });
            builder.show();
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    public static String capFirstLetter(String original){
        //Sets the first letter of word to Capital for niceness
        if (original.isEmpty())
            return original;
        return original.substring(0 ,1).toUpperCase() + original.substring(1).toLowerCase();
    }

    public static void storeTaskValue(ArrayList inArrayList, Context context){
        Set TaskListItem = new HashSet(inArrayList);
        SharedPreferences TaskItemPutPrefs = context.getSharedPreferences("taskArrayValues", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = TaskItemPutPrefs.edit();
        editor.putStringSet("taskArray", TaskListItem);
        editor.commit();
    }

    public static ArrayList getTaskValue(Context task){
        SharedPreferences TaskItemGetPrefs = task.getSharedPreferences("taskArrayValues", Activity.MODE_PRIVATE);
        Set taskSet = new HashSet();
        taskSet = TaskItemGetPrefs.getStringSet("taskArray", taskSet);
        return new ArrayList<>(taskSet);
    }

    public void deleteTask(String selectedTask, final int position){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete " + selectedTask + "?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which){
                taskList.remove(position);
                storeTaskValue(taskList, getApplicationContext());
                lview.setAdapter(ladapter);
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which){
                dialog.cancel();
            }
        });
        builder.show();
    }
}
