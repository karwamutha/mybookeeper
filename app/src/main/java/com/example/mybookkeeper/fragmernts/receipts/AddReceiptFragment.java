package com.example.mybookkeeper.fragmernts.receipts;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.Editable;
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

public class AddReceiptFragment extends Fragment {

    private UIDataStore mDatabase;
    EditText eRctNo;
    EditText eDate;
    EditText eSubName;
    EditText eCltName;
    EditText eAmount;
    Button buttonEnter;
    View buttonClear;

    String clientNameFFromDialog;
    int mngIdFromFFromDialog;
    int acntIdFFromDialog;
    int subAccIdFFromDialog;
    int clientIDFFromDialog;
    private ProgressDialog progress;
    private String subAccNameFFromDialog;

    public static AddReceiptFragment getInstance(int clientID) {
        AddReceiptFragment r = new AddReceiptFragment();
        Bundle args = new Bundle();
        args.putInt("ClientID", clientID);
        r.setArguments(args);
        return r;
    }

    public AddReceiptFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_add_receipt, container, false);

        mDatabase = new UIDataStore(getActivity());
        eRctNo = view.findViewById(R.id.eRctNo);
        eCltName = view.findViewById(R.id.eClientName);
        eSubName = view.findViewById(R.id.eSubAccName);
        eDate = view.findViewById(R.id.eDate);
        eAmount = view.findViewById(R.id.eAmount);
        eAmount.requestFocus();
        buttonEnter = view.findViewById(R.id.btnInsert);
        buttonClear = view.findViewById(R.id.btnClear);

        if (getArguments() != null) {
            clientNameFFromDialog = getArguments().getString("clientNameFFromDialog");
            mngIdFromFFromDialog = getArguments().getInt("mngIdFromFFromDialog");
            acntIdFFromDialog = getArguments().getInt("acntIdFFromDialog");
            subAccIdFFromDialog = getArguments().getInt("subAccIdFFromDialog");
            subAccNameFFromDialog = getArguments().getString("subAccNameFromDialog");
            clientIDFFromDialog = getArguments().getInt("clientIDFFromDialog");
            eCltName.setText(clientNameFFromDialog);
            eSubName.setText(subAccNameFFromDialog);
            ((MainActivity) getActivity()).getSupportActionBar().setTitle("Receipt for ");
            ((MainActivity) getActivity()).getSupportActionBar().setSubtitle(clientNameFFromDialog);
        } else {
            ((MainActivity) getActivity()).getSupportActionBar().setTitle("NO RECEIPT SELECTED");
            ((MainActivity) getActivity()).getSupportActionBar().setSubtitle("SELECTED RECEIPT NOT FOUND");
        }
        eCltName.setText(clientNameFFromDialog);

        Date date = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");
        String formattedDate = df.format(date);
        eDate.setText(formattedDate);

        setReceiptNextID();
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
                final int rctNo = Integer.parseInt(eRctNo.getText().toString());
                final int rctMngId = mngIdFromFFromDialog;
                final int rctAccId = acntIdFFromDialog;
                final int rctSubaccId = subAccIdFFromDialog;
                final int rctClientid = clientIDFFromDialog;
                final String rctCltName = clientNameFFromDialog;
                final double amount = Double.parseDouble(eAmount.getText().toString());
                //Toast.makeText(getActivity(), amount+"", Toast.LENGTH_LONG).show();
                if (TextUtils.isEmpty("" + amount)) {
                    Toast.makeText(getActivity(), "Something went wrong. Check your input values", Toast.LENGTH_LONG).show();
                } else {

                    ReceiptData newReceipt = new ReceiptData(date, rctNo, rctMngId, rctAccId, rctSubaccId, rctClientid, rctCltName, amount);
                    showProgressDialog("Adding receipt");
                    mDatabase.addReceipt(newReceipt)
                            .observe(getViewLifecycleOwner(), r -> {
                                Toast.makeText(getActivity(), "Success: Receipt saved", Toast.LENGTH_SHORT).show();
                                setReceiptNextID();
                            });
                    eAmount.setText("");
                }
            }
        });
        return view;
    }

    private void closeProgressDialog() {
        if (progress != null) {
            progress.dismiss();
            progress = null;
        }
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

    private void setReceiptNextID() {
        mDatabase.getNextReceiptID()
                .observe(getViewLifecycleOwner(), r -> {
                    eRctNo.setText(r.getResult() + "");
                    closeProgressDialog();
                });
    }

}