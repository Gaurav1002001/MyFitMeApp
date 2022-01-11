package com.example.myfitmeapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.example.myfitmeapp.CoronaData.AffectedCountries;
import com.example.myfitmeapp.Model.CountryModel;
import com.example.myfitmeapp.R;

import java.util.ArrayList;
import java.util.List;

public class CoronaAdapter extends ArrayAdapter<CountryModel> {
    private Context context;
    private List<CountryModel> countryModelsList;
    private List<CountryModel> countryModelsListFiltered;

    public CoronaAdapter(Context context2, List<CountryModel> countryModelsList2) {
        super(context2, R.layout.list_custom_item, countryModelsList2);
        this.context = context2;
        this.countryModelsList = countryModelsList2;
        this.countryModelsListFiltered = countryModelsList2;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_custom_item, (ViewGroup) null, true);
        ((TextView) view.findViewById(R.id.tvCountryName)).setText(this.countryModelsListFiltered.get(position).getCountry());
        Glide.with(this.context).load(this.countryModelsListFiltered.get(position).getFlag()).into((ImageView) view.findViewById(R.id.imageFlag));
        return view;
    }

    public int getCount() {
        return this.countryModelsListFiltered.size();
    }

    @Override
    public CountryModel getItem(int position) {
        return this.countryModelsListFiltered.get(position);
    }

    public long getItemId(int position) {
        return (long) position;
    }

    public Filter getFilter() {
        return new Filter() {

            public FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                if (constraint == null || constraint.length() == 0) {
                    filterResults.count = CoronaAdapter.this.countryModelsList.size();
                    filterResults.values = CoronaAdapter.this.countryModelsList;
                } else {
                    List<CountryModel> resultsModel = new ArrayList<>();
                    String searchStr = constraint.toString().toLowerCase();
                    for (CountryModel itemsModel : CoronaAdapter.this.countryModelsList) {
                        if (itemsModel.getCountry().toLowerCase().contains(searchStr)) {
                            resultsModel.add(itemsModel);
                        }
                        filterResults.count = resultsModel.size();
                        filterResults.values = resultsModel;
                    }
                }
                return filterResults;
            }

            public void publishResults(CharSequence constraint, FilterResults results) {
                CoronaAdapter.this.countryModelsListFiltered = (List) results.values;
                AffectedCountries.countryModelsList = (List) results.values;
                CoronaAdapter.this.notifyDataSetChanged();
            }
        };
    }
}
