package com.ione.iseller;


/**
 * To do: implement onclicklistener on done button.
 */








import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class CategoryChooser extends AppCompatActivity implements AdapterView.OnItemClickListener,
        CatChooserPopUp.Communicator, View.OnClickListener {

    static boolean[] checkedStatus;
    final static int  NUMBER_OF_CATEGORY = 37;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_chooser);

        Resources res = getResources();
        String[] catName = res.getStringArray(R.array.catName);
        String[] catExamples = res.getStringArray(R.array.examples);
        int[] catImageId = {R.drawable.cat1, R.drawable.cat2, R.drawable.cat3, R.drawable.cat4,
                R.drawable.cat5, R.drawable.cat6, R.drawable.cat7, R.drawable.cat8,
                R.drawable.cat9, R.drawable.cat10, R.drawable.cat11, R.drawable.cat12,
                R.drawable.cat13, R.drawable.cat14, R.drawable.cat15, R.drawable.cat16,
                R.drawable.cat17, R.drawable.cat18, R.drawable.cat19, R.drawable.cat20,
                R.drawable.cat21, R.drawable.cat22, R.drawable.cat23, R.drawable.cat24,
                R.drawable.cat25, R.drawable.cat26, R.drawable.cat27, R.drawable.cat28,
                R.drawable.cat29, R.drawable.cat30, R.drawable.cat31, R.drawable.cat32,
                R.drawable.cat33, R.drawable.cat34, R.drawable.cat35, R.drawable.cat36,
                R.drawable.cat37};

        ShivCategoryAdapter adapter = new ShivCategoryAdapter(this, catName, catExamples,
                catImageId);
        ListView list = (ListView) findViewById(R.id.listView);
        list.setAdapter(adapter);

        ImageView done_symbol = (ImageView) findViewById(R.id.done_symbol);
        done_symbol.setOnClickListener(this);


        checkedStatus = new boolean[NUMBER_OF_CATEGORY];
        for (int i = 0; i < NUMBER_OF_CATEGORY; i++) {
            checkedStatus[i] = false;
        }

        list.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        CheckBox checkBox = (CheckBox) view.findViewById(R.id.cat_checker);
        boolean checked = checkedStatus[position];
        if (!checked) {
            checkedStatus[position] = true;
            checkBox.setChecked(true);
        } else {
            checkedStatus[position] = false;
            checkBox.setChecked(false);
        }
    }

    @Override
    public void catChooserPopUpMessage(int status) {
        if (status == 1) {
            Intent intent = getIntent();
            intent.putExtra("CategoryChoose", checkedStatus);
            setResult(RESULT_OK, intent);
            finish();
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.done_symbol) {
            CatChooserPopUp catChooserPopUp = new CatChooserPopUp();
            catChooserPopUp.show(getFragmentManager(), "CatChooser");
        }
    }
}

class ShivCategoryAdapter extends BaseAdapter {

    private class SingleRowHolder {
        String catName, catExamples;
        int catImageId;

        SingleRowHolder(String catName, String catExamples, int catImageId) {
            this.catName = catName;
            this.catExamples = catExamples;
            this.catImageId = catImageId;
        }
    }

    private Context context;
    private ArrayList<SingleRowHolder> arrayList;

    ShivCategoryAdapter(Context context, String[] catName, String[] catExamples,
                        int[] catImageId) {

        this.context = context;
        arrayList = new ArrayList<>();

        // Define number of iteration for the for loop
        int iteration = CategoryChooser.NUMBER_OF_CATEGORY;
        for (int i = 0 ; i < iteration; i++) {
            arrayList.add(new SingleRowHolder(catName[i], catExamples[i], catImageId[i]));
        }
    }

    private class ViewHolder {
        ImageView catImage;
        TextView catName, catExamples;

        ViewHolder(View row) {
            catImage = (ImageView) row.findViewById(R.id.cat_pic);
            catName = (TextView) row.findViewById(R.id.cat_name);
            catExamples = (TextView) row.findViewById(R.id.cat_examples);
        }
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View row = convertView;
        ViewHolder holder;
        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.
                    LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.single_category_row, parent, false);
            holder = new ViewHolder(row);
            row.setTag(holder);
        } else {
            row = convertView;
            CheckBox checkBox = (CheckBox) row.findViewById(R.id.cat_checker);
            boolean checked = CategoryChooser.checkedStatus[position];
            if (!checked) {
                checkBox.setChecked(false);
            } else {
                checkBox.setChecked(true);
            }
            holder = (ViewHolder) row.getTag();
        }

        SingleRowHolder singleRowHolder = arrayList.get(position);
        holder.catName.setText(singleRowHolder.catName);
        holder.catExamples.setText(singleRowHolder.catExamples);
        holder.catImage.setImageResource(singleRowHolder.catImageId);

        return row;
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Object getItem(int position) {
        return arrayList.get(position);
    }
}