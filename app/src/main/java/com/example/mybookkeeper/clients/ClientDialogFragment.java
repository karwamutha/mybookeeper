package com.example.mybookkeeper.clients;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.mybookkeeper.R;
import com.example.mybookkeeper.SqliteDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public  class ClientDialogFragment extends Fragment {

    private TextView tvClient,tvSubaccout;
    private SqliteDatabase mDatabase;
    //    String subAccNameFromClients;
//    String clientNameFromClients;
    int clientIDFromAcc;
    int subAccIdFromSubacc;
//    int subAccIdFromClients;
//    int clientIDFromClients;
    EditText dateFrom, dateTo;
    int mngIdFromDialog;
    Client client;
    int acntIdFromDialog;

    public ClientDialogFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_client_dialog, container, false);

        if (getArguments() != null) {
            subAccIdFromSubacc = getArguments().getInt("subAccIdFromSubacc");
//            subAccNameFromSubaccs = getArguments().getString("subAccNameFromSubaccs");
//            acntIdFromSubaccs = getArguments().getInt("acntIdFromSubaccs");
//            mngIdFromSubacc = getArguments().getInt("mngIdFromSubacc");
        }
        Button btnGetRctSmry = (Button) view.findViewById(R.id.btnRctDetails);
//        tvClient = view.findViewById(R.id.txtClient);
        dateTo = view.findViewById(R.id.edDateTo);

        Date date = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");
        String formattedDate = df.format(date);
        dateFrom = view.findViewById(R.id.edDateFrom);
        dateFrom.setText(formattedDate);
        dateFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v == dateFrom) {
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
                            dateFrom.setText(formattedDate);
                        }
                    }, mYear, mMonth, mDay);
                    datePickerDialog.show();
                }
            }
        });

        dateTo = view.findViewById(R.id.edDateTo);
        Date date1 = Calendar.getInstance().getTime();
        SimpleDateFormat df1 = new SimpleDateFormat("yyyy/MM/dd");
        String formattedDate1 = df1.format(date1);
        dateTo.setText(formattedDate1);
        dateTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v == dateTo) {
                    final Calendar calendar = Calendar.getInstance();
                    int mYear = calendar.get(Calendar.YEAR);
                    int mMonth = calendar.get(Calendar.MONTH);
                    int mDay = calendar.get(Calendar.DAY_OF_MONTH);

                    //show dialog
                    DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                            calendar.set(year, month, dayOfMonth);
                            SimpleDateFormat df1 = new SimpleDateFormat("yyyy/MM/dd");
                            String formattedDate1 = df1.format(calendar.getTime());
                            dateTo.setText(formattedDate1);
                        }
                    }, mYear, mMonth, mDay);
                    datePickerDialog.show();
                }
            }
        });

        btnGetRctSmry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle args = new Bundle();
                args.putInt("mngIdFromDialog", subAccIdFromSubacc);
//                args.putString("subAccNameFromGallety", subAccNameFromClients);
//                args.putInt("acntIdFFromGallety", acntIdFromClients);
//                args.putInt("subAccIdFFromGallety", subAccIdFromClients);
//                args.putString("clientNameFFromGallety", clientNameFromClients);
//                args.putInt("clientIDFFromGallety", clientIDFromClients);
                args.putString("startDate", dateFrom.getText().toString());
                args.putString("endDate", dateTo.getText().toString());
                NavHostFragment.findNavController(ClientDialogFragment.this)
                        .navigate(R.id.action_ClientReceiptFragment_to_TransactionsFragment, args);
            }
        });
        return view;
    }
}