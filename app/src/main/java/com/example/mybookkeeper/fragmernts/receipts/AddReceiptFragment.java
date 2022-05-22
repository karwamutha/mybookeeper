package com.example.mybookkeeper.fragmernts.receipts;

import android.app.DatePickerDialog;
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
import com.example.mybookkeeper.SqliteDatabase;
import com.example.mybookkeeper.clients.Client;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AddReceiptFragment extends Fragment {

    private SqliteDatabase mDatabase;
    EditText eRctNo, eDate, eSubName, eCltName, eMgclid, eAccId, eSubaccId, eClientId, eAmount;
    Button btnInsert, btnClear;
    String NameHolder, NumberHolder, SQLiteDataBaseQueryHolder;
    Button buttonEnter;
    View buttonClear;

    String nameFromDialog;
    int mngIdFromDialog;
    int subAccIdFromDialog;
    int acntIdFromDialog;
    int clientIDFromDialog;
    Client client = null;

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

        mDatabase = new SqliteDatabase(getActivity());
        eRctNo = view.findViewById(R.id.eRctNo);
        eCltName = view.findViewById(R.id.eClientName);
        eSubName = view.findViewById(R.id.eSubAccName);
        eDate = view.findViewById(R.id.eDate);
        eAmount = view.findViewById(R.id.eAmount);
        eAmount.requestFocus();
        buttonEnter = view.findViewById(R.id.btnInsert);
        buttonClear = view.findViewById(R.id.btnClear);

        if (getArguments() != null) {
            nameFromDialog = getArguments().getString("nameFromDialog");
            mngIdFromDialog = getArguments().getInt("mngIdFromDialog");
            acntIdFromDialog = getArguments().getInt("acntIdFromDialog");
            subAccIdFromDialog = getArguments().getInt("subAccIdFromDialog");;
            clientIDFromDialog = getArguments().getInt("clientIDFromDialog");
            ((MainActivity) getActivity()).getSupportActionBar().setTitle("Add Receipt for ");
            ((MainActivity) getActivity()).getSupportActionBar().setSubtitle(nameFromDialog);
        }else{
            ((MainActivity) getActivity()).getSupportActionBar().setTitle("NO RECEIPT SELECTED");
            ((MainActivity) getActivity()).getSupportActionBar().setSubtitle("SELECTED RECEIPT NOT FOUND");
        }
        eCltName.setText(nameFromDialog);
        eSubName.setText(nameFromDialog);


        Date date = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");
        String formattedDate = df.format(date);
        eDate.setText(formattedDate);

        eRctNo.setText(mDatabase.getNextReceiptID() + "");
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
                nameFromDialog = getArguments().getString("nameFromDialog");
                mngIdFromDialog = getArguments().getInt("mngIdFromDialog");
                acntIdFromDialog = getArguments().getInt("acntIdFromDialog");
                subAccIdFromDialog = getArguments().getInt("subAccIdFromDialog");;
                clientIDFromDialog = getArguments().getInt("clientIDFromDialog");
                final String cltName = nameFromDialog;
                final int rctMngId= mngIdFromDialog;
                final int rctAccId= acntIdFromDialog;
                final int rctSubaccId= subAccIdFromDialog;
                final int rctClientid = clientIDFromDialog;
                final double amount = Double.parseDouble(eAmount.getText().toString());
                //Toast.makeText(getActivity(), amount+"", Toast.LENGTH_LONG).show();
                if (TextUtils.isEmpty("" + amount)) {
                    Toast.makeText(getActivity(), "Something went wrong. Check your input values", Toast.LENGTH_LONG).show();
                } else {

                    ReceiptData newReceipt = new ReceiptData(date, rctNo, rctMngId, rctAccId, rctSubaccId, rctClientid, cltName, amount);
                    mDatabase.addReceipt(newReceipt);
                    Toast.makeText(getActivity(), "Success: Receipt saved", Toast.LENGTH_SHORT).show();
                    eRctNo.setText(mDatabase.getNextReceiptID() + "");
                    eAmount.setText("");
                }
            }
        });
        return view;
    }
}