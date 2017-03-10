package com.juanpabloprado.datapicker;

import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

public class DataAdapter extends RecyclerView.Adapter<DataAdapter.CountryHolder>
    implements Filterable {

  private static final Pattern ACCENTS_PATTERN = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");

  private DataPicker mDataPicker;
  private LayoutInflater mInflater;
  private DataPickerListener mListener;
  private List<Data> mFilteredCountries;

  public DataAdapter(DataPicker dataPicker, DataPickerListener listener) {
    mDataPicker = dataPicker;
    mInflater = LayoutInflater.from(dataPicker.getActivity());
    mListener = listener;
    mFilteredCountries = new ArrayList<Data>(DataPicker.dataList);
  }

  @Override public CountryHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    return new CountryHolder(mInflater.inflate(R.layout.row, parent, false));
  }

  @Override public void onBindViewHolder(CountryHolder holder, int position) {
    final Data data = mFilteredCountries.get(position);

    holder.textView.setText(data.name);

    String drawableName = "flag_" + data.id.toLowerCase(Locale.ENGLISH);
    int drawableId = mDataPicker.getResources()
        .getIdentifier(drawableName, "drawable", mDataPicker.getActivity().getPackageName());
    if (drawableId != 0) {
      holder.imageView.setImageDrawable(
          ContextCompat.getDrawable(mDataPicker.getActivity(), drawableId));
    }
    holder.itemView.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        mListener.onSelectCountry(data.name, data.id);
      }
    });
  }

  @Override public int getItemCount() {
    return mFilteredCountries.size();
  }

  public class CountryHolder extends RecyclerView.ViewHolder {
    public TextView textView;
    public ImageView imageView;

    public CountryHolder(View itemView) {
      super(itemView);
      textView = (TextView) itemView.findViewById(R.id.row_title);
      imageView = (ImageView) itemView.findViewById(R.id.row_icon);
    }
  }

  public void refill(List<Data> countries) {
    mFilteredCountries.clear();
    mFilteredCountries.addAll(countries);
    notifyDataSetChanged();
  }

  @Override public Filter getFilter() {
    return new Filter() {
      @Override protected FilterResults performFiltering(CharSequence constraint) {
        FilterResults results = new FilterResults();
        if (constraint != null) {

          List<Data> filteredCountries = new ArrayList<Data>();
          for (Data data : DataPicker.dataList) {

            if (containsIgnoreCaseAndAccents(data.name, (String) constraint)) {
              filteredCountries.add(data);
            }
          }
          results.values = filteredCountries;
          results.count = filteredCountries.size();
        }
        return results;
      }

      @SuppressWarnings("unchecked") @Override
      protected void publishResults(CharSequence constraint, FilterResults results) {
        if (results != null) {
          refill((List<Data>) results.values);
        }
      }
    };
  }

  private boolean containsIgnoreCaseAndAccents(String name, String constraint) {
    return removeAccents(name).toLowerCase().contains(removeAccents(constraint).toLowerCase());
  }

  private String removeAccents(String string) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
      return ACCENTS_PATTERN.matcher(Normalizer.normalize(string, Normalizer.Form.NFD)).replaceAll("");
    } else {
      return string;
    }
  }
}