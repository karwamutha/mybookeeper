package com.example.mybookkeeper.fragmernts.receipts;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mybookkeeper.MainActivity;
import com.example.mybookkeeper.R;
import com.example.mybookkeeper.accounts.Account;
import com.example.mybookkeeper.accounts.AccountTotal;
import com.example.mybookkeeper.clients.ClientTotal;
import com.example.mybookkeeper.data.UIDataStore;
import com.example.mybookkeeper.managers.Manager;
import com.example.mybookkeeper.managers.ManagerTotal;
import com.example.mybookkeeper.managers.RefreshableFragment;
import com.example.mybookkeeper.subaccounts.SubAccount;
import com.example.mybookkeeper.subaccounts.SubAccountTotal;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ReceiptDetailFragment extends Fragment implements RefreshableFragment {

    private UIDataStore mDatabase;
    RecyclerView ReceiptView;
    EditText eExpNo, eAmount;

    String clientNameFFromDialog;
    int mngIdFromFFromDialog;
    int acntIdFFromDialog;
    int subAccIdFFromDialog;
    int clientIDFFromDialog;

    EditText dateFrom, dateTo;
    String startDate, endDate;
    private ProgressDialog progress;

    public static ReceiptDetailFragment getInstance(int clientID) {
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

        ReceiptData ReceiptData = null;
        Bundle args = getArguments();

        View v = inflater.inflate(R.layout.fragment_receipt_details, container, false);

        ReceiptView = v.findViewById(R.id.myReceiptList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        ReceiptView.setLayoutManager(linearLayoutManager);
        ReceiptView.setHasFixedSize(true);
        mDatabase = UIDataStore.getInstance();
        eExpNo = v.findViewById(R.id.eExpNo);
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
        if (getArguments() != null) {
            clientNameFFromDialog = getArguments().getString("clientNameFFromDialog");
            mngIdFromFFromDialog = getArguments().getInt("mngIdFromFFromDialog");
            acntIdFFromDialog = getArguments().getInt("acntIdFFromDialog");
            subAccIdFFromDialog = getArguments().getInt("subAccIdFFromDialog");
            ;
            clientIDFFromDialog = getArguments().getInt("clientIDFFromDialog");
            startDate = firstDayOfMonthStr;
            endDate = lastDayOfMonthStr;
            ((MainActivity) getActivity()).getSupportActionBar().setTitle("Receipt Details for ");
            ((MainActivity) getActivity()).getSupportActionBar().setSubtitle(clientNameFFromDialog);

        } else {
            ((MainActivity) getActivity()).getSupportActionBar().setTitle("NO ReceiptS SELECTED");
            ((MainActivity) getActivity()).getSupportActionBar().setSubtitle("SELECTED ReceiptS NOT FOUND");
        }
        refresh();
        dateFrom.setText(startDate);
        dateTo.setText(endDate);
        return v;
    }

    public void refresh() {
        showProgressDialog("Refreshing...");
        UIDataStore.UiData<List<ReceiptData>> listUiData = mDatabase.listReceipts(clientIDFFromDialog);
        listUiData.observe(getViewLifecycleOwner(), new Observer<UIDataStore.Result<List<ReceiptData>>>() {
            @Override
            public void onChanged(UIDataStore.Result<List<ReceiptData>> listResult) {
                List<ReceiptData> allReceipts = listResult.getResult();
                if (listResult.isFailure()){
                    displayNonBlockingError(getContext(), listResult.getException());
                } else if (allReceipts != null && allReceipts.size() > 0) {
                    ReceiptView.setVisibility(View.VISIBLE);
                    ReceiptDetailAdapter mAdapter = new ReceiptDetailAdapter(getActivity(), ReceiptDetailFragment.this, allReceipts, getArguments().getInt("clientIDFromDialog"));
                    ReceiptView.setAdapter(mAdapter);
                } else {
                    ReceiptView.setVisibility(View.GONE);
                    Toast.makeText(getActivity(), "There is no account in the database. Start adding now", Toast.LENGTH_LONG).show();
                }
                closeProgressDialog();
            }
        });
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

    //    private void addTaskDialog() {
//        LayoutInflater inflater = LayoutInflater.from(getActivity());
//        View subView = inflater.inflate(R.layout.expenses_list_layout, null);
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
//                final int expNo = Integer.parseInt(ReceiptData.getExpNo()+"");
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