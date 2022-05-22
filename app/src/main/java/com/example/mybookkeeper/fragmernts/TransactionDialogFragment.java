package com.example.mybookkeeper.fragmernts;

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

import com.example.mybookkeeper.MainActivity;
import com.example.mybookkeeper.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TransactionDialogFragment extends Fragment {

    private TextView tvClient,tvSubaccout;

    EditText dateFrom, dateTo;
    String startDate, endDate;
    Button buttonAddRct, buttonAddExp, buttonRctDtl,
            buttonExpDtl, buttonAllRct, buttonAllExp;
    int mngIdFromFromClients;
    int acntIdFromClients;
    int subAccIdFromClients;
    int clientIDFromClients;
    String subAccNameFromClients;
    String clientNameFromClients;

    public TransactionDialogFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_transactions, container, false);
        buttonAddRct = (Button) view.findViewById(R.id.btnAddRct);
        buttonAddExp = (Button) view.findViewById(R.id.btnAddExp);
        buttonRctDtl = (Button) view.findViewById(R.id.btnRctDetails);
        buttonExpDtl = (Button) view.findViewById(R.id.btnExpDetails);
        buttonAllRct = (Button) view.findViewById(R.id.btnAllReceipts);
        buttonAllExp = (Button) view.findViewById(R.id.btnAllExpenses);
        dateFrom = view.findViewById(R.id.edDateFrom);
        dateTo = view.findViewById(R.id.edDateTo);
        Date date = Calendar.getInstance().getTime();

        //========FIRST AND LAST DAY OD MONTH
        Calendar calendar = Calendar.getInstance();
        Date today = calendar.getTime();// Get the current date
        calendar.set(Calendar.DAY_OF_MONTH, 1); // Setting the first day of month
        Date firstDayOfMonth = calendar.getTime();
        calendar.add(Calendar.MONTH, 1);  // Move to next month
        calendar.set(Calendar.DAY_OF_MONTH, 1);    // setting the 1st day of the month
        calendar.add(Calendar.DATE, -1); // Move a day back from the date
        Date lastDayOfMonth = calendar.getTime();
        DateFormat sdf = new SimpleDateFormat("yyyy/MM/dd"); // Formatting the date
        String firstDayOfMonthStr = sdf.format(firstDayOfMonth); // String todayStr = sdf.format(today);
        String lastDayOfMonthStr = sdf.format(lastDayOfMonth);
        //====================

        //=============CDATEPICKERS
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
                            dateTo.setText(formattedDate);
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
        if (getArguments() != null) {
            subAccNameFromClients = getArguments().getString("subAccNameFromClients");
            mngIdFromFromClients = getArguments().getInt("mngIdFromFromClients");
            acntIdFromClients = getArguments().getInt("acntIdFromClients");
            subAccIdFromClients = getArguments().getInt("subAccIdFromClients");
            clientNameFromClients = getArguments().getString("clientNameFromClients");
            //tvClient.setText(clientNameFromClients);
            clientIDFromClients = getArguments().getInt("clientIDFromClients");
            ((MainActivity) getActivity()).getSupportActionBar().setTitle("SUBACC: " + subAccNameFromClients);
            ((MainActivity) getActivity()).getSupportActionBar().setSubtitle("CLIENT: " + clientNameFromClients);
        }else{
            ((MainActivity) getActivity()).getSupportActionBar().setTitle("NO ACTIVITY SELECTED");
        }
            startDate = firstDayOfMonthStr;
            endDate = lastDayOfMonthStr;
//            ((MainActivity) getActivity()).getSupportActionBar().setTitle("Transactions for: ");
//            ((MainActivity) getActivity()).getSupportActionBar().setSubtitle(nameFromClients);

        dateFrom.setText(startDate);
        dateTo.setText(endDate);
        buttonRctDtl.setText("For: " + clientNameFromClients);
        buttonExpDtl.setText("For: " + clientNameFromClients);
        final int[] page = {3};

        buttonAddRct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle args = new Bundle();
                args.putString("subAccNameFromGallety", subAccNameFromClients);
                args.putInt("mngIdFromFFromGallety", mngIdFromFromClients);
                args.putInt("acntIdFFromGallety", acntIdFromClients);
                args.putInt("subAccIdFFromGallety", subAccIdFromClients);
                args.putString("clientNameFFromGallety", clientNameFromClients);
                args.putInt("clientIDFFromGallety", clientIDFromClients);                NavHostFragment.findNavController(TransactionDialogFragment.this)
                        .navigate(R.id.action_TransactionsFragment_to_AddReceiptFeagment, args);
            }
        });
        buttonAddExp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle args = new Bundle();
                args.putString("subAccNameFromGallety", subAccNameFromClients);
                args.putInt("mngIdFromFFromGallety", mngIdFromFromClients);
                args.putInt("acntIdFFromGallety", acntIdFromClients);
                args.putInt("subAccIdFFromGallety", subAccIdFromClients);
                args.putString("clientNameFFromGallety", clientNameFromClients);
                args.putInt("clientIDFFromGallety", clientIDFromClients);
                args.putString("startDate", dateFrom.getText().toString());
                args.putString("endDate", dateTo.getText().toString());
                NavHostFragment.findNavController(TransactionDialogFragment.this)
                        .navigate(R.id.action_TransactionsFragment_to_AddExpenseFragment, args);
            }
        });
        buttonRctDtl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle args = new Bundle();
                args.putString("subAccNameFromGallety", subAccNameFromClients);
                args.putInt("mngIdFromFFromGallety", mngIdFromFromClients);
                args.putInt("acntIdFFromGallety", acntIdFromClients);
                args.putInt("subAccIdFFromGallety", subAccIdFromClients);
                args.putString("clientNameFFromGallety", clientNameFromClients);
                args.putInt("clientIDFFromGallety", clientIDFromClients);
                args.putString("startDate", dateFrom.getText().toString());
                args.putString("endDate", dateTo.getText().toString());
                NavHostFragment.findNavController(TransactionDialogFragment.this)
                        .navigate(R.id.action_TransactionsFragment_to_ReceiptDetailFragment, args);
            }
        });
        buttonExpDtl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle args = new Bundle();
                args.putString("subAccNameFromGallety", subAccNameFromClients);
                args.putInt("mngIdFromFFromGallety", mngIdFromFromClients);
                args.putInt("acntIdFFromGallety", acntIdFromClients);
                args.putInt("subAccIdFFromGallety", subAccIdFromClients);
                args.putString("clientNameFFromGallety", clientNameFromClients);
                args.putInt("clientIDFFromGallety", clientIDFromClients);
                args.putString("startDate", dateFrom.getText().toString());
                args.putString("endDate", dateTo.getText().toString());
                NavHostFragment.findNavController(TransactionDialogFragment.this)
                        .navigate(R.id.action_TransactionsFragment_to_ExpensesDetailsragment, args);
            }
        });
        buttonAllRct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Bundle args = new Bundle();
//                args.putString("nameFromDialog", nameFromClients);
//                args.putInt("mngIdFromDialog", mngIdFromClients);
//                args.putInt("acntIdFromDialog", acntIdFromClients);
//                args.putInt("subAccIdFromDialog", subAccIdFromClients);
//                args.putInt("clientIDFromDialog", clientIDFromClients);
//                NavHostFragment.findNavController(TransactionDialogFragment.this)
//                        .navigate(R.id.action_TransactionsFragment_to_AllReceiptFeagment, args);
            }
        });
        buttonAllExp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Bundle args = new Bundle();
//                args.putString("nameFromDialog", nameFromClients);
//                args.putInt("mngIdFromDialog", mngIdFromClients);
//                args.putInt("acntIdFromDialog", acntIdFromClients);
//                args.putInt("subAccIdFromDialog", subAccIdFromClients);
//                args.putInt("clientIDFromDialog", clientIDFromClients);
//                NavHostFragment.findNavController(TransactionDialogFragment.this)
//                        .navigate(R.id.action_TransactionsFragment_to_AllExpenseFeagment, args);
            }
        });
        return view;
    }
}