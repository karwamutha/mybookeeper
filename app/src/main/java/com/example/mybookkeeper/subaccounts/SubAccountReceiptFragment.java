package com.example.mybookkeeper.subaccounts;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
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
import androidx.lifecycle.Observer;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mybookkeeper.MainActivity;
import com.example.mybookkeeper.R;
import com.example.mybookkeeper.accounts.Account;
import com.example.mybookkeeper.accounts.AccountTotal;
import com.example.mybookkeeper.clients.ClientTotal;
import com.example.mybookkeeper.data.UIDataStore;
import com.example.mybookkeeper.fragmernts.receipts.ReceiptData;
import com.example.mybookkeeper.managers.Manager;
import com.example.mybookkeeper.managers.ManagerTotal;
import com.example.mybookkeeper.managers.RefreshableFragment;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class SubAccountReceiptFragment extends Fragment implements RefreshableFragment {

    private UIDataStore mDatabase;
    RecyclerView SubAccountReceiptView;
    EditText eAmount;
    EditText dateFrom, dateTo;
    String startDate, endDate;

    String chooser;
    String accNameFromAccs;
    ;
    int mngIdFromAccs;
    int accIdFromAccs;
    Button buttonAdd;
    private ProgressDialog progress;

    public static SubAccountReceiptFragment getInstance(int accId) {
        SubAccountReceiptFragment r = new SubAccountReceiptFragment();
        Bundle args = new Bundle();
//        args.putInt("ClientID", clientID);
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

        View view = inflater.inflate(R.layout.fragment_subaccount_receipts, container, false);

        SubAccountReceiptView = view.findViewById(R.id.mySubAccountReceiptList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        SubAccountReceiptView.setLayoutManager(linearLayoutManager);
        SubAccountReceiptView.setHasFixedSize(true);
        mDatabase = UIDataStore.getInstance();
        eAmount = view.findViewById(R.id.eAmount);
        buttonAdd = view.findViewById(R.id.btnAdd);
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
        //=========================
        if (getArguments() != null) {
            chooser = getArguments().getString("originPage");
            if (chooser.equalsIgnoreCase("FromAccsAdmin")) {
                buttonAdd.setVisibility(View.VISIBLE);
            } else if (chooser.equalsIgnoreCase("FromAccsLgn")) {
                buttonAdd.setVisibility(View.GONE);
            } else if (chooser.equalsIgnoreCase("FromAccsPwd")) {
                buttonAdd.setVisibility(View.GONE);
            }
            accIdFromAccs = getArguments().getInt("accIdFromAccs");
            accNameFromAccs = getArguments().getString("accNameFromAccs");
            mngIdFromAccs = getArguments().getInt("mngIdFromAccs");
            startDate = firstDayOfMonthStr;
            endDate = lastDayOfMonthStr;
            ((MainActivity) getActivity()).getSupportActionBar().setTitle("SubAccount's List for ");
            ;
            ((MainActivity) getActivity()).getSupportActionBar().setSubtitle(accNameFromAccs);

        } else {
            ((MainActivity) getActivity()).getSupportActionBar().setTitle("NO ACCOUNT SELECTED");
            ((MainActivity) getActivity()).getSupportActionBar().setSubtitle("SELECTED ACCOUNT NOT FOUND");
        }
        dateFrom.setText(startDate);
        dateTo.setText(endDate);

        refresh();
        Button btnAdd = view.findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(view1 -> addTaskDialog());

        Button submitDateButton = view.findViewById(R.id.btnSubmit);
        submitDateButton.setOnClickListener(clicked -> refresh());
        return view;
    }

    public void refresh() {
        //Toast.makeText(getActivity(), ""+mngIdFromDialog, Toast.LENGTH_LONG).show();
        showProgressDialog("Refreshing...");
        UIDataStore.UiData<List<SubAccountTotal>> totalReceipts = mDatabase.listSubAccTotalReceipts(startDate, endDate, accIdFromAccs);
        totalReceipts.observe(getViewLifecycleOwner(), new Observer<UIDataStore.Result<List<SubAccountTotal>>>() {
            @Override
            public void onChanged(UIDataStore.Result<List<SubAccountTotal>> listResult) {
                List<SubAccountTotal> allReceipts = listResult.getResult();
                if (listResult.isFailure()){
                    displayNonBlockingError(getContext(), listResult.getException());
                } else if (allReceipts != null && allReceipts.size() > 0) {
                    SubAccountReceiptView.setVisibility(View.VISIBLE);
                    SubAccountReceiptAdapter mAdapter = new SubAccountReceiptAdapter(getActivity(), SubAccountReceiptFragment.this, allReceipts, getArguments().getInt("acntIdFromDialog"));
                    SubAccountReceiptView.setAdapter(mAdapter);
                } else {
                    SubAccountReceiptView.setVisibility(View.GONE);
                    Toast.makeText(getActivity(), "There is no subAccount in the database. Start adding now", Toast.LENGTH_LONG).show();
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
    public void navigateToCreaateAccount() {

    }

    @Override
    public void navigateToClients(SubAccount subaccounts) {

    }

    @Override
    public void navigateToClientsDialog(ClientTotal client) {

    }

    @Override
    public void navigateToSubAccountTotal(AccountTotal accountTotal) {

    }

    @Override
    public void navigateToClientTotal(SubAccountTotal subAccountTotal) {
        Bundle args = new Bundle();
        args.putInt("subAccIdFromSubacc", subAccountTotal.getSubAccount().getSubAccId());
        args.putString("subAccNameFromSubaccs", subAccountTotal.getSubAccount().getSubAccName());
        args.putInt("acntIdFromSubaccs", accIdFromAccs);
        args.putInt("mngIdFromSubacc", accIdFromAccs);
        args.putString("startDate", dateFrom.getText().toString());
        args.putString("endDate", dateTo.getText().toString());
        args.putString("startDate", dateFrom.getText().toString());
        args.putString("endDate", dateTo.getText().toString());
        NavHostFragment.findNavController(SubAccountReceiptFragment.this)
                .navigate(R.id.action_SubAccountReceiptFragment_to_ClientReceiptFragment, args);
    }

    private void addTaskDialog() {
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View subView = inflater.inflate(R.layout.add_subaccount, null);
        final EditText subaccnameField = subView.findViewById(R.id.entersacName);
        final EditText mgidField = subView.findViewById(R.id.entersacMgId);
        final EditText accField = subView.findViewById(R.id.entersacAccId);
        mgidField.setText(mngIdFromAccs + "");
        accField.setText(accIdFromAccs + "");
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Add new SUBACCOUNT");
        builder.setView(subView);
        builder.create();
        builder.setPositiveButton("ADD SUBACCOUNTF", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final String subacNme = subaccnameField.getText().toString();
                final int mngid = mngIdFromAccs;
                final int accid = accIdFromAccs;
                if (TextUtils.isEmpty(subacNme)) {
                    Toast.makeText(getActivity(), "Something went wrong. Check your input values", Toast.LENGTH_LONG).show();
                } else {
                    SubAccount newSubAccount = new SubAccount(subacNme, mngid, accid);
                    showProgressDialog("Adding sub-account...");
                    mDatabase.addSubAccounts(newSubAccount)
                            .observe(getViewLifecycleOwner(), voidResult -> {
                                closeProgressDialog();
                                refresh();
                            });
                }
            }
        }).setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getActivity(), "Task cancelled", Toast.LENGTH_LONG).show();
            }
        });
        builder.show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mDatabase != null) {
            mDatabase.close();
        }
    }
}

//import android.app.AlertDialog;
//import android.app.DatePickerDialog;
//import android.content.DialogInterface;
//import android.os.Bundle;
//import android.text.TextUtils;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//import android.widget.DatePicker;
//import android.widget.EditText;
//import android.widget.Toast;
//
//import androidx.fragment.app.Fragment;
//import androidx.navigation.fragment.NavHostFragment;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.example.mybookkeeper.MainActivity;
//import com.example.mybookkeeper.R;
//import com.example.mybookkeeper.data.UIDataStore;
//import com.example.mybookkeeper.accounts.Account;
//import com.example.mybookkeeper.accounts.AccountTotal;
//import com.example.mybookkeeper.clients.ClientTotal;
//import com.example.mybookkeeper.fragmernts.receipts.ReceiptData;
//import com.example.mybookkeeper.managers.Manager;
//import com.example.mybookkeeper.managers.ManagerTotal;
//import com.example.mybookkeeper.managers.RefreshableFragment;
//
//import java.text.DateFormat;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Calendar;
//import java.util.Date;
//
//public class SubAccountReceiptFragment extends Fragment implements RefreshableFragment {
//
//    private SqliteDatabase mDatabase;
//    RecyclerView SubAccountReceiptView;
//    EditText eAmount;
//    EditText dateFrom, dateTo;
//    String startDate, endDate;
//
//    String chooser;
//    String accNameFromAccs;;
//    int accMngIdFromAccs;
//    int accIdFromAccs;
//    int mngIdFromAccs;
//    Button buttonAdd;
//
//    public static SubAccountReceiptFragment getInstance(int accId){
//        SubAccountReceiptFragment r = new SubAccountReceiptFragment();
//        Bundle args = new Bundle();
////        args.putInt("ClientID", clientID);
//        r.setArguments(args);
//        return r;
//    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//
//        ReceiptData ReceiptData = null;
//        Bundle args = getArguments();
//
//        View view = inflater.inflate(R.layout.fragment_subaccount_receipts, container, false);
//
//        SubAccountReceiptView = view.findViewById(R.id.mySubAccountReceiptList);
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
//        SubAccountReceiptView.setLayoutManager(linearLayoutManager);
//        SubAccountReceiptView.setHasFixedSize(true);
//        mDatabase = new SqliteDatabase(context);
//        eAmount = view.findViewById(R.id.eAmount);
//        buttonAdd = view.findViewById(R.id.btnAdd);
//        dateFrom = view.findViewById(R.id.edDateFrom);
//        dateTo = view.findViewById(R.id.edDateTo);
//        Date date = Calendar.getInstance().getTime();
//
//        //========FIRST AND LAST DAY OD MONTH
//        Calendar calendar = Calendar.getInstance();
//        Date today = calendar.getTime();// Get the current date
//        calendar.set(Calendar.DAY_OF_MONTH, 1); // Setting the first day of month
//        Date firstDayOfMonth = calendar.getTime();
//        calendar.add(Calendar.MONTH, 1);  // Move to next month
//        calendar.set(Calendar.DAY_OF_MONTH, 1);    // setting the 1st day of the month
//        calendar.add(Calendar.DATE, -1); // Move a day back from the date
//        Date lastDayOfMonth = calendar.getTime();
//        DateFormat sdf = new SimpleDateFormat("yyyy/MM/dd"); // Formatting the date
//        String firstDayOfMonthStr = sdf.format(firstDayOfMonth); // String todayStr = sdf.format(today);
//        String lastDayOfMonthStr = sdf.format(lastDayOfMonth);
//        //====================
//        //=============CDATEPICKERS
//        SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");
//        String formattedDate = df.format(date);
//        dateFrom = view.findViewById(R.id.edDateFrom);
//        dateFrom.setText(formattedDate);
//        dateFrom.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (v == dateFrom) {
//                    final Calendar calendar = Calendar.getInstance();
//                    int mYear = calendar.get(Calendar.YEAR);
//                    int mMonth = calendar.get(Calendar.MONTH);
//                    int mDay = calendar.get(Calendar.DAY_OF_MONTH);
//
//                    //show dialog
//                    DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
//                        @Override
//                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
//                            calendar.set(year, month, dayOfMonth);
//                            SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");
//                            String formattedDate = df.format(calendar.getTime());
//                            dateFrom.setText(formattedDate);
//                            dateTo.setText(formattedDate);
//                        }
//                    }, mYear, mMonth, mDay);
//                    datePickerDialog.show();
//                }
//            }
//        });
//
//        dateTo = view.findViewById(R.id.edDateTo);
//        Date date1 = Calendar.getInstance().getTime();
//        SimpleDateFormat df1 = new SimpleDateFormat("yyyy/MM/dd");
//        String formattedDate1 = df1.format(date1);
//        dateTo.setText(formattedDate1);
//        dateTo.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (v == dateTo) {
//                    final Calendar calendar = Calendar.getInstance();
//                    int mYear = calendar.get(Calendar.YEAR);
//                    int mMonth = calendar.get(Calendar.MONTH);
//                    int mDay = calendar.get(Calendar.DAY_OF_MONTH);
//
//                    //show dialog
//                    DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
//                        @Override
//                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
//                            calendar.set(year, month, dayOfMonth);
//                            SimpleDateFormat df1 = new SimpleDateFormat("yyyy/MM/dd");
//                            String formattedDate1 = df1.format(calendar.getTime());
//                            dateTo.setText(formattedDate1);
//                        }
//                    }, mYear, mMonth, mDay);
//                    datePickerDialog.show();
//                }
//            }
//        });
//        //=========================
//        if (getArguments() != null) {
//            chooser = getArguments().getString("originPage");
//            if (chooser.equalsIgnoreCase("FromAccsAdmin")){
//                buttonAdd.setVisibility(View.VISIBLE);
//            }else if (chooser.equalsIgnoreCase("FromAccsLgn")){
//                buttonAdd.setVisibility(View.GONE);
//            }else if (chooser.equalsIgnoreCase("FromAccsPwd")){
//                buttonAdd.setVisibility(View.GONE);
//            }
//            accIdFromAccs = getArguments().getInt("accIdFromAccs");
//            accNameFromAccs = getArguments().getString("accNameFromAccs");
//            mngIdFromAccs = getArguments().getInt("mngIdFromAccs");
//            ((MainActivity) getActivity()).getSupportActionBar().setTitle("SubAccount's List for ");;
//            ((MainActivity) getActivity()).getSupportActionBar().setSubtitle("ACC:- " + accNameFromAccs);
//
//        }else{
//            ((MainActivity) getActivity()).getSupportActionBar().setTitle("NO ACCOUNT SELECTED");
//            ((MainActivity) getActivity()).getSupportActionBar().setSubtitle("SELECTED ACCOUNT NOT FOUND");
//        }
//        dateFrom.setText(startDate);
//        dateTo.setText(endDate);
//
//        refresh();
//        Button btnAdd = view.findViewById(R.id.btnAdd);
//        btnAdd.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                addTaskDialog();
//            }
//        });
//        return view;
//    }
//    public void refresh(){
//        //Toast.makeText(getActivity(), ""+mngIdFromDialog, Toast.LENGTH_LONG).show();
//        ArrayList<SubAccountTotal> allReceipts = mDatabase.listSubAccTotalReceipts(startDate, endDate, accMngIdFromAccs);
//        if (allReceipts.size() > 0) {
//            SubAccountReceiptView.setVisibility(View.VISIBLE);
//            SubAccountReceiptAdapter mAdapter = new SubAccountReceiptAdapter(getActivity(),  this, allReceipts, getArguments().getInt("acntIdFromDialog"));
//            SubAccountReceiptView.setAdapter(mAdapter);
//        }
//        else {
//            SubAccountReceiptView.setVisibility(View.GONE);
//            Toast.makeText(getActivity(), "There is no subAccount in the database. Start adding now", Toast.LENGTH_LONG).show();
//        }
//    }
//
//    @Override
//    public void navigateToManagers(Manager manager) {
//
//    }
//
//    @Override
//    public void navigateToAccounts(Account account) {
//
//    }
//
//    @Override
//    public void navigateToSubAccounts(SubAccount subaccount) {
//
//    }
//
//    @Override
//    public void navigateToAccountTotal(ManagerTotal managerTotal) {
//
//    }
//
//    @Override
//    public void navigateToCreaateAccount() {
//
//    }
//
//    @Override
//    public void navigateToClients(SubAccount subaccounts) {
//
//    }
//
//    @Override
//    public void navigateToClientsDialog(ClientTotal client) {
//
//    }
//
//    @Override
//    public void navigateToSubAccountTotal(AccountTotal accountTotal) {
//
//    }
//
//    @Override
//    public void navigateToClientTotal(SubAccountTotal subAccountTotal) {
//        Bundle args = new Bundle();
//        args.putInt("subAccIdFromSubacc", subAccountTotal.getSubAccount().getsubAccId());
//        args.putString("subAccNameFromSubaccs", subAccountTotal.getSubAccount().getSubAccName());
//        args.putInt("acntIdFromSubaccs", accIdFromAccs);
//        args.putInt("mngIdFromSubacc", accIdFromAccs);
//        args.putString("startDate", dateFrom.getText().toString());
//        args.putString("endDate", dateTo.getText().toString());
//        NavHostFragment.findNavController(SubAccountReceiptFragment.this)
//                .navigate(R.id.action_SubAccountReceiptFragment_to_ClientReceiptFragment, args);
//    }
//
//    private void addTaskDialog() {
//        LayoutInflater inflater = LayoutInflater.from(getActivity());
//        View subView = inflater.inflate(R.layout.add_subaccount, null);
//        final EditText subaccnameField = subView.findViewById(R.id.entersacName);
//        final EditText mgidField = subView.findViewById(R.id.entersacMgId);
//        final EditText accField = subView.findViewById(R.id.entersacAccId);
//        mgidField.setText(mngIdFromAccs+"");
//        accField.setText(accIdFromAccs+"");
//        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//        builder.setTitle("Add new SUBACCOUNT");
//        builder.setView(subView);
//        builder.create();
//        builder.setPositiveButton("ADD SUBACCOUNTF", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                final String subacNme = subaccnameField.getText().toString();
//                final int mngid = mngIdFromAccs;
//                final int accid = accIdFromAccs;
//                if (TextUtils.isEmpty(subacNme)) {
//                    Toast.makeText(getActivity(), "Something went wrong. Check your input values", Toast.LENGTH_LONG).show();
//                } else {
//                    SubAccountTotal newSubAccountTotalt = new SubAccountTotal(subacNme, mngid, accid);
//                    mDatabase.addSubAccounts(newSubAccountTotalt);
//                    refresh();
//                }
//            }
//        }).setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                Toast.makeText(getActivity(), "Task cancelled", Toast.LENGTH_LONG).show();
//            }
//        });
//        builder.show();
//    }

//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        if (mDatabase != null) {
//            mDatabase.close();
//        }
//    }
//}