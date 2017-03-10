package com.juanpabloprado.countrypicker;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.inputmethod.InputMethodManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DataPicker extends DialogFragment {
  private DataAdapter mAdapter;
  private DataPickerListener mListener;

  static List<Data> dataList = new ArrayList<Data>();
  private List<String> userCountryCodes;
  private static final String DIALOG_TITLE_KEY = "dialogTitle";

    public static DataPicker newInstance(String dialogTitle) {
        DataPicker picker = new DataPicker();
        Bundle bundle = new Bundle();
        bundle.putString("dialogTitle", dialogTitle);
        picker.setArguments(bundle);
        return picker;
    }

  public static DataPicker getInstance(String dialogTitle, DataPickerListener listener) {
    DataPicker picker = getInstance(listener);
    Bundle bundle = new Bundle();
    bundle.putString(DIALOG_TITLE_KEY, dialogTitle);
    picker.setArguments(bundle);
    return picker;
  }

  public static DataPicker getInstance(DataPickerListener listener) {
    DataPicker picker = new DataPicker();
    picker.mListener = listener;
    return picker;
  }

  public static DataPicker getInstance(DataPickerListener listener,
                                       List<String> userCountryCodes) {
    DataPicker picker = getInstance(listener);
    picker.userCountryCodes = userCountryCodes;
    return picker;
  }

    public void setDataList(List<Data> userDataList){
        dataList = userDataList;
    }

    public void setListener(DataPickerListener listener) {
        this.mListener = listener;
    }

  /**
   * Create view
   */
  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.country_picker, container, false);

    // Set dialog title if show as dialog
    Bundle args = getArguments();
    if (args != null) {
      String dialogTitle = args.getString(DIALOG_TITLE_KEY);
      getDialog().setTitle(dialogTitle);

      int width = getResources().getDimensionPixelSize(R.dimen.cp_dialog_width);
      int height = getResources().getDimensionPixelSize(R.dimen.cp_dialog_height);
      getDialog().getWindow().setLayout(width, height);
    }

    EditText searchEditText = (EditText) view.findViewById(R.id.data_picker_search);
    RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.data_picker_recycler_view);

    // Sort the dataList based on country name
    Collections.sort(dataList);

    // setup recyclerView
    recyclerView.setLayoutManager(
        new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));
    mAdapter = new DataAdapter(this, mListener);
    recyclerView.setAdapter(mAdapter);
    recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
      @Override
      public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
          super.onScrollStateChanged(recyclerView, newState);
          hideKeyboard();
      }
    });

    // Search for which dataList matched user query
    searchEditText.addTextChangedListener(new TextWatcher() {

      @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
      }

      @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {
      }

      @Override public void afterTextChanged(Editable s) {
        mAdapter.getFilter().filter(s);
      }
    });

    return view;
  }
  
  /**
  * Method to hide keyboard if it's open
  */
  public void hideKeyboard() {
    try {
      Activity activity = getActivity();
      if (activity == null) {
        return;
      }
      InputMethodManager input
            = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
      if (input == null) {
        return;
      }
      View currentFocus = activity.getCurrentFocus();
      if (currentFocus == null || currentFocus.getWindowToken() == null) {
        return;
      }

      input.hideSoftInputFromWindow(currentFocus.getWindowToken(), 0);
    } catch (Exception e) {
        // ignoring any exceptions
    }
  }
}
