package com.example.mybookkeeper.fragmernts.expenses;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.mybookkeeper.MainActivity;
import com.example.mybookkeeper.R;
import com.example.mybookkeeper.data.UIDataStore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AddExpenseFragment extends Fragment {

    private UIDataStore mDatabase;
    EditText eExpNo, eDate, eSubName, eCltName, eDescr, eAmount;
    Button buttonEnter;
    View buttonClear;
    String clientNameFFromDialog;
    int mngIdFromFFromDialog;
    int acntIdFFromDialog;
    int subAccIdFFromDialog;
    int clientIDFFromDialog;
    private ProgressDialog progress;

    public static AddExpenseFragment getInstance(int clientID) {
        AddExpenseFragment r = new AddExpenseFragment();
        Bundle args = new Bundle();
        args.putInt("ClientID", clientID);
        r.setArguments(args);
        return r;
    }

    public AddExpenseFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_add_expense, container, false);

        mDatabase = UIDataStore.getInstance();
        eDate = view.findViewById(R.id.eDate);
        eExpNo = view.findViewById(R.id.eExpNo);
        eCltName = view.findViewById(R.id.eClientName);
        eDescr = view.findViewById(R.id.eDescription);
        eAmount = view.findViewById(R.id.eAmount);
        eAmount.requestFocus();
        buttonEnter = view.findViewById(R.id.btnInsert);
        buttonClear = view.findViewById(R.id.btnClear);

        if (getArguments() != null) {
            clientNameFFromDialog = getArguments().getString("clientNameFFromDialog");
            mngIdFromFFromDialog = getArguments().getInt("mngIdFromFFromDialog");
            acntIdFFromDialog = getArguments().getInt("acntIdFFromDialog");
            subAccIdFFromDialog = getArguments().getInt("subAccIdFFromDialog");
            ;
            clientIDFFromDialog = getArguments().getInt("clientIDFFromDialog");
            ((MainActivity) requireActivity()).getSupportActionBar().setTitle("Add Receipt for ");
            ((MainActivity) requireActivity()).getSupportActionBar().setSubtitle(clientNameFFromDialog);
        } else {
            ((MainActivity) getActivity()).getSupportActionBar().setTitle("NO Expense SELECTED");
            ((MainActivity) getActivity()).getSupportActionBar().setSubtitle("SELECTED Expense NOT FOUND");
        }
        eCltName.setText(clientNameFFromDialog);
//        eDescr.setText(nameFromDialog);


        Date date = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");
        String formattedDate = df.format(date);
        eDate.setText(formattedDate);

        showProgressDialog("Getting expense id...");
        setNextExpenseId();
        eDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v == eDate) {
                    final Calendar calendar = Calendar.getInstance();
                    int mYear = calendar.get(Calendar.YEAR);
                    int mMonth = calendar.get(Calendar.MONTH);
                    int mDay = calendar.get(Calendar.DAY_OF_MONTH);

                    //show dialog
                    DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                            calendar.set(year, month, dayOfMonth);
                            SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");
                            String formattedDate = df.format(calendar.getTime());
                            eDate.setText(formattedDate);
                        }
                    }, mYear, mMonth, mDay);
                    datePickerDialog.show();
                }
            }
        });

        buttonEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String date = eDate.getText().toString();
                final int expNo = Integer.parseInt(eExpNo.getText().toString());
                final int expMngId = mngIdFromFFromDialog;
                final int expAccId = acntIdFFromDialog;
                final int expSubaccId = subAccIdFFromDialog;
                final int expClientid = clientIDFFromDialog;
                final String expCltName = clientNameFFromDialog;
                final String descr = eDescr.getText().toString();
                final double amount = Double.parseDouble(eAmount.getText().toString());
                //Toast.makeText(getActivity(), amount+"", Toast.LENGTH_LONG).show();
                if (TextUtils.isEmpty("" + amount)) {
                    Toast.makeText(getActivity(), "Something went wrong. Check your input values", Toast.LENGTH_LONG).show();
                } else {

                    ExpenseData newExpense = new ExpenseData(date, expNo, expMngId, expAccId, expSubaccId, expClientid, expCltName, descr, amount);
                    showProgressDialog("Adding Expense...");
                    mDatabase.addExpense(newExpense)
                            .observe(getViewLifecycleOwner(), r -> setNextExpenseId());
                    Toast.makeText(getActivity(), "Success: Expense saved", Toast.LENGTH_SHORT).show();
                    eAmount.setText("");
                }
            }
        });
        return view;
    }

    private void setNextExpenseId() {
        mDatabase.getNextExpenseID()
                .observe(getViewLifecycleOwner(), r -> {
                    eExpNo.setText(r.getResult() + "");
                    closeProgressDialog();
                });
    }


    private void showProgressDialog(String message) {
        if (progress == null) {
            progress = new ProgressDialog(getContext());

            progress.setMessage(message);
            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progress.setCancelable(false);
            progress.setIndeterminate(true); progress.show();
        }
    }

    private void closeProgressDialog() {
        if (progress != null) {
            progress.dismiss();
            progress = null;
        }
    }
}