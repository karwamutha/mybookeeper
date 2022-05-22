package com.example.mybookkeeper.fragmernts.receipts;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mybookkeeper.MainActivity;
import com.example.mybookkeeper.R;
import com.example.mybookkeeper.SqliteDatabase;
import com.example.mybookkeeper.accounts.Account;
import com.example.mybookkeeper.accounts.AccountTotal;
import com.example.mybookkeeper.clients.ClientTotal;
import com.example.mybookkeeper.managers.Manager;
import com.example.mybookkeeper.managers.ManagerTotal;
import com.example.mybookkeeper.managers.RefreshableFragment;
import com.example.mybookkeeper.subaccounts.SubAccount;
import com.example.mybookkeeper.subaccounts.SubAccountTotal;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class ReceiptDetailFragment extends Fragment implements RefreshableFragment {

    private SqliteDatabase mDatabase;
    RecyclerView ReceiptView;
    ReceiptData ReceiptData;
    EditText eRctNo, eAmount;
    String nameFromDialog;
    int mngIdFromDialog;
    int acntIdFromDialog;
    int subAccIdFromDialog;
    int clientIDFromDialog;
    EditText dateFrom, dateTo;
    String startDate, endDate;

    public static ReceiptDetailFragment getInstance(int clientID){
        ReceiptDetailFragment r = new ReceiptDetailFragment();
        Bundle args = new Bundle();
        args.putInt("ClientID", clientID);
        r.setArguments(args);
        return r;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ReceiptData receiptData = null;
        Bundle args = getArguments();

        View v = inflater.inflate(R.layout.fragment_receipt_details, container, false);

        ReceiptView = v.findViewById(R.id.myReceiptList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        ReceiptView.setLayoutManager(linearLayoutManager);
        ReceiptView.setHasFixedSize(true);
        mDatabase = new SqliteDatabase(getActivity());
        eRctNo = v.findViewById(R.id.eRctNo);
        eAmount = v.findViewById(R.id.eAmount);
        dateFrom = v.findViewById(R.id.edDateFrom);
        dateTo = v.findViewById(R.id.edDateTo);
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

        dateTo = v.findViewById(R.id.edDateTo);
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
        //=========================
        if (getArguments() != null) {
            nameFromDialog = getArguments().getString("nameFromDialog");
            mngIdFromDialog = getArguments().getInt("mngIdFromDialog");
            acntIdFromDialog = getArguments().getInt("acntIdFromDialog");
            subAccIdFromDialog = getArguments().getInt("subAccIdFromDialog");;
            clientIDFromDialog = getArguments().getInt("clientIDFromDialog");
            startDate = firstDayOfMonthStr;
            endDate = lastDayOfMonthStr;
            ((MainActivity) getActivity()).getSupportActionBar().setTitle("Receipt Details for ");
            ((MainActivity) getActivity()).getSupportActionBar().setSubtitle(nameFromDialog);

        }else{
            ((MainActivity) getActivity()).getSupportActionBar().setTitle("NO ReceiptS SELECTED");
            ((MainActivity) getActivity()).getSupportActionBar().setSubtitle("SELECTED ReceiptS NOT FOUND");
        }
        dateFrom.setText(startDate);
        dateTo.setText(endDate);
        refresh();
        return v;
    }
    public void refresh(){

        ArrayList<ReceiptData> allReceipts = mDatabase.listReceipts(startDate, endDate, clientIDFromDialog);
        if (allReceipts.size() > 0) {
            ReceiptView.setVisibility(View.VISIBLE);
            ReceiptDetailAdapter mAdapter = new ReceiptDetailAdapter(getActivity(),  this, allReceipts, getArguments().getInt("clientIDFromDialog"));
            ReceiptView.setAdapter(mAdapter);
        }
        else {
            ReceiptView.setVisibility(View.GONE);
            Toast.makeText(getActivity(), "There is no account in the database. Start adding now", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void navigateToManagers(Manager manager) {

    }

    @Override
    public void navigateToAccounts(Account account) {

    }

    @Override
    public void navigateToSubAccounts(SubAccount subaccount) {

    }

    @Override
    public void navigateToAccountTotal(ManagerTotal managerTotal) {

    }

    @Override
    public void navigateToSubAccountTotal(AccountTotal accountTotal) {

    }

    @Override
    public void navigateToClientTotal(SubAccountTotal accountTotal) {

    }

    @Override
    public void navigateToCreaateAccount() {

    }

    @Override
    public void navigateToClients(SubAccount subaccounts) {

    }

    @Override
    public void navigateToClientsDialog(ClientTotal client) {

    }

//        private void addTaskDialog() {
//        LayoutInflater inflater = LayoutInflater.from(getActivity());
//        View subView = inflater.inflate(R.layout.receipt_list_layout, null);
//        final EditText dateField = subView.findViewById(R.id.eDate);
//        final EditText rctnoField = subView.findViewById(R.id.eRctNo);
////        final EditText subField = subView.findViewById(R.id.eSubName);
////        final EditText mngField = subView.findViewById(R.id.eMgclid);
////        final EditText accField = subView.findViewById(R.id.eAccId);
////        final EditText subaccField = subView.findViewById(R.id.eSubaccId);
////        final EditText clientField = subView.findViewById(R.id.eClient);
//        final EditText amountField = subView.findViewById(R.id.eAmount);
//        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//        builder.setTitle("Add new Receipt");
//        builder.setView(subView);
//        builder.create();
//        builder.setPositiveButton("ADD Receipt", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                ReceiptData = mDatabase.searchReceiptByID(Integer.parseInt(mngField.getText().toString()));
//                final String date = dateField.getText().toString();
//                finReceiptexpNo = Integer.parseInt(ReceiptData.getExpNo()+"");
//                final String subname = subField.getText().toString();
//                final int mngId = Integer.parseInt(ReceiptData.getMgId()+"");
//                final int accId = Integer.parseInt(ReceiptData.getAccId()+"");
//                final int subaccId = Integer.parseInt(ReceiptData.getSubId()+"");
//                final int clientId = Integer.parseInt(ReceiptData.getClientId()+"");
//                final double amount = Double.parseDouble(amountField.getText().toString());
//
//                if (!TextUtils.isEmpty("" + amount)) {
//                    ReceiptData newReceipt = new ReceiptData(date, expNo, subname, mngId, accId, subaccId, clientId, amount);
//                    mDatabase.addReceipt(newReceipt);
//                    refresh();
//                } else {
//                    Toast.makeText(getActivity(), "Something went wrong. Check your input values", Toast.LENGTH_LONG).show();
//                }
//            }
//        });
//        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                Toast.makeText(getActivity(), "Task cancelled", Toast.LENGTH_LONG).show();
//            }
//        });
//        builder.show();
//    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mDatabase != null) {
            mDatabase.close();
        }
    }
}