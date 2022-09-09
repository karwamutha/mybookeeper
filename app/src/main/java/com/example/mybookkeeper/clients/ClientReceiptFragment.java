package com.example.mybookkeeper.clients;

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
import com.example.mybookkeeper.data.UIDataStore;
import com.example.mybookkeeper.fragmernts.receipts.ReceiptData;
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

public class ClientReceiptFragment extends Fragment implements RefreshableFragment {

    private UIDataStore mDatabase;
    RecyclerView ClientReceiptView;
    EditText eRctNo, eAmount;

    EditText dateFrom, dateTo;
    String startDate, endDate;

    String subAccNameFromSubaccs;
    int mngIdFromSubacc;
    int acntIdFromSubaccs;
    int subAccIdFromSubacc;
    private ProgressDialog progress;

    public static ClientReceiptFragment getInstance(int accId) {
        ClientReceiptFragment r = new ClientReceiptFragment();
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

        View view = inflater.inflate(R.layout.fragment_client_receipts, container, false);

        ClientReceiptView = view.findViewById(R.id.myClientReceiptList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        ClientReceiptView.setLayoutManager(linearLayoutManager);
        ClientReceiptView.setHasFixedSize(true);
        mDatabase = UIDataStore.getInstance();
        eRctNo = view.findViewById(R.id.eRctNo);
        eAmount = view.findViewById(R.id.eAmount);

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
        });//====================
        dateFrom = view.findViewById(R.id.edDateFrom);
        dateTo = view.findViewById(R.id.edDateTo);

        if (getArguments() != null) {
            subAccIdFromSubacc = getArguments().getInt("subAccIdFromSubacc");
            subAccNameFromSubaccs = getArguments().getString("subAccNameFromSubaccs");
            mngIdFromSubacc = getArguments().getInt("mngIdFromSubacc");
            acntIdFromSubaccs = getArguments().getInt("acntIdFromSubaccs");
            startDate = firstDayOfMonthStr;
            endDate = lastDayOfMonthStr;
            ((MainActivity) getActivity()).getSupportActionBar().setTitle("Client List for Subaccount ");
            ((MainActivity) getActivity()).getSupportActionBar().setSubtitle(subAccNameFromSubaccs);

        } else {
            ((MainActivity) getActivity()).getSupportActionBar().setTitle("NO Client SELECTED");
            ((MainActivity) getActivity()).getSupportActionBar().setSubtitle("SELECTED Client NOT FOUND");
        }

        refresh();

        dateFrom.setText(startDate);
        dateTo.setText(endDate);

        Button btnAdd = view.findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addTaskDialog();
            }
        });
        return view;
    }

    public void refresh() {
        showProgressDialog("Loading data...");
        //Toast.makeText(getActivity(), ""+mngIdFromDialog, Toast.LENGTH_LONG).show();
        UIDataStore.UiData<List<ClientTotal>> totalReceipts =
                mDatabase.listClientTotalReceipts(startDate, endDate, subAccIdFromSubacc);
        totalReceipts.observe(getViewLifecycleOwner(), new Observer<UIDataStore.Result<List<ClientTotal>>>() {
            @Override
            public void onChanged(UIDataStore.Result<List<ClientTotal>> listResult) {
                List<ClientTotal> allReceipts = listResult.getResult();
                if (listResult.isFailure()){
                    displayNonBlockingError(getContext(), listResult.getException());
                } else if (allReceipts != null && allReceipts.size() > 0) {
                    ClientReceiptView.setVisibility(View.VISIBLE);
                    ClientReceiptAdapter mAdapter = new ClientReceiptAdapter(getActivity(), ClientReceiptFragment.this, allReceipts, getArguments().getInt("cltSubIdFromSub"));
                    ClientReceiptView.setAdapter(mAdapter);

                } else {
                    ClientReceiptView.setVisibility(View.GONE);
                    Toast.makeText(getActivity(), "There is no Client in the database. Start adding now", Toast.LENGTH_LONG).show();
                }
                closeProgressDialog();
            }
        });
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
    public void navigateToClients(SubAccount subaccount) {

    }

    @Override
    public void navigateToClientsDialog(ClientTotal clientTotal) {
        Bundle args = new Bundle();
        args.putInt("clientIDFromClients", clientTotal.getClient().getId());
        args.putString("clientNameFromClients", clientTotal.getClient().getCltName());
        args.putInt("subAccIdFromClients", subAccIdFromSubacc);
        args.putString("subAccNameFromClients", subAccNameFromSubaccs);
        args.putInt("acntIdFromClients", acntIdFromSubaccs);
        args.putInt("mngIdFromFromClients", mngIdFromSubacc);
        NavHostFragment.findNavController(ClientReceiptFragment.this)
                .navigate(R.id.action_ClientReceiptFragment_to_TransactionsFragment, args);
    }

    private void addTaskDialog() {
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View subView = inflater.inflate(R.layout.add_clients, null);
        final EditText subaccnameField = subView.findViewById(R.id.enterCltName);
        final EditText mgidField = subView.findViewById(R.id.enterCltMgid);
        final EditText accField = subView.findViewById(R.id.enterCltAccId);
        final EditText subAccField = subView.findViewById(R.id.enterCltSubId);
        mgidField.setText(mngIdFromSubacc + "");
        accField.setText(acntIdFromSubaccs + "");
        subAccField.setText(subAccIdFromSubacc + "");
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Add new CLIENT");
        builder.setView(subView);
        builder.create();
        builder.setPositiveButton("ADD CLIENT", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final String subacNme = subaccnameField.getText().toString();
                final int mngid = mngIdFromSubacc;
                final int accid = acntIdFromSubaccs;
                final int suAaccid = subAccIdFromSubacc;
                if (TextUtils.isEmpty(subacNme)) {
                    Toast.makeText(getActivity(), "Something went wrong. Check your input values", Toast.LENGTH_LONG).show();
                } else {
                    Client newClient = new Client(subacNme, mngid, accid, suAaccid);
                    showProgressDialog("Adding Client..");
                    mDatabase.addClients(newClient).observe(getViewLifecycleOwner(), r -> closeProgressDialog());
                    refresh();
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
//import com.example.mybookkeeper.fragmernts.receipts.ReceiptData;
//import com.example.mybookkeeper.managers.Manager;
//import com.example.mybookkeeper.managers.ManagerTotal;
//import com.example.mybookkeeper.managers.RefreshableFragment;
//import com.example.mybookkeeper.subaccounts.SubAccount;
//import com.example.mybookkeeper.subaccounts.SubAccountTotal;
//
//import java.text.DateFormat;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Calendar;
//import java.util.Date;
//
//public class ClientReceiptFragment extends Fragment implements RefreshableFragment {
//
//    private SqliteDatabase mDatabase;
//    RecyclerView ClientReceiptView;
//    EditText eRctNo, eAmount;
//
//    EditText dateFrom, dateTo;
//    String startDate, endDate;
//    String subAccNameFromSubaccs;
//    int subAccIdFromSubacc;;
//    int acntIdFromSubaccs;
//    int mngIdFromSubacc;
//
//    public static ClientReceiptFragment getInstance(int accId){
//        ClientReceiptFragment r = new ClientReceiptFragment();
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
//        View view = inflater.inflate(R.layout.fragment_client_receipts, container, false);
//
//        ClientReceiptView = view.findViewById(R.id.myClientReceiptList);
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
//        ClientReceiptView.setLayoutManager(linearLayoutManager);
//        ClientReceiptView.setHasFixedSize(true);
//        mDatabase = new SqliteDatabase(context);
//        eRctNo = view.findViewById(R.id.eRctNo);
//        eAmount = view.findViewById(R.id.eAmount);
//
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
//
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
//        });//====================
//        dateFrom = view.findViewById(R.id.edDateFrom);
//        dateTo = view.findViewById(R.id.edDateTo);
//
//        if (getArguments() != null) {
//            subAccIdFromSubacc = getArguments().getInt("subAccIdFromSubacc");
//            subAccNameFromSubaccs = getArguments().getString("subAccNameFromSubaccs");
//            mngIdFromSubacc = getArguments().getInt("mngIdFromSubacc");
//            acntIdFromSubaccs = getArguments().getInt("acntIdFromSubaccs");
//
//            ((MainActivity) getActivity()).getSupportActionBar().setTitle("Client's List for ");
//            ((MainActivity) getActivity()).getSupportActionBar().setSubtitle("SUBACC: " + subAccNameFromSubaccs);
//        }else{
//            ((MainActivity) getActivity()).getSupportActionBar().setTitle("NO ACCOUNT SELECTED");
//            ((MainActivity) getActivity()).getSupportActionBar().setSubtitle("SELECTED ACCOUNT NOT FOUND");
//        }
//
//        refresh();
//
//        dateFrom.setText(startDate);
//        dateTo.setText(endDate);
//
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
//        ArrayList<ClientTotal> allReceipts = mDatabase.listClientTotalReceipts(startDate, endDate, subAccIdFromSubacc);
//        if (allReceipts.size() > 0) {
//            ClientReceiptView.setVisibility(View.VISIBLE);
//            ClientReceiptAdapter mAdapter = new ClientReceiptAdapter(getActivity(),  this, allReceipts, getArguments().getInt("cltSubIdFromSub"));
//            ClientReceiptView.setAdapter(mAdapter);
//
//        }
//        else {
//            ClientReceiptView.setVisibility(View.GONE);
//            Toast.makeText(getActivity(), "There is no Client in the database. Start adding now", Toast.LENGTH_LONG).show();
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
//    public void navigateToSubAccountTotal(AccountTotal accountTotal) {
//
//    }
//
//    @Override
//    public void navigateToClientTotal(SubAccountTotal accountTotal) {
//
//    }
//
//    @Override
//    public void navigateToCreaateAccount() {
//
//    }
//
//    @Override
//    public void navigateToClients(SubAccount subaccount) {
//
//    }
//
//    @Override
//    public void navigateToClientsDialog(ClientTotal clientTotal) {
//        Bundle args = new Bundle();
//        args.putInt("clientIDFromClients", clientTotal.getClient().getId());
//        args.putString("clientNameFromClients", clientTotal.getClient().getCltName());
//        args.putInt("subAccIdFromClients", subAccIdFromSubacc);
//        args.putString("subAccNameFromClients", subAccNameFromSubaccs);
//        args.putInt("acntIdFromClients", acntIdFromSubaccs);
//        args.putInt("mngIdFromFromClients", mngIdFromSubacc);
//        NavHostFragment.findNavController(ClientReceiptFragment.this)
//                .navigate(R.id.action_ClientReceiptFragment_to_TransactionsFragment, args);
//    }
//
//    private void addTaskDialog() {
//        LayoutInflater inflater = LayoutInflater.from(getActivity());
//        View subView = inflater.inflate(R.layout.add_clients, null);
//        final EditText clientNameField = subView.findViewById(R.id.enterCltName);
//        final EditText clientMgidField = subView.findViewById(R.id.enterCltMgid);
//        final EditText claccIdField = subView.findViewById(R.id.enterCltAccId);
//        final EditText clsubIdField = subView.findViewById(R.id.enterCltSubId);
//        clientNameField.setText("");
//        clientMgidField.setText(clientMgidField + "");
//        claccIdField.setText(claccIdField + "");
//        clsubIdField.setText(clsubIdField + "");
//        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//        builder.setTitle("Add new CLIENT");
//        builder.setView(subView);
//        builder.create();
//        builder.setPositiveButton("ADD CLIENT", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                final String cltName = clientNameField.getText().toString();
//                final int clientMgid = Integer.parseInt(clientMgidField.getText().toString());
//                final int cltaccId = Integer.parseInt(claccIdField.getText().toString());
//                final int cltsubId = Integer.parseInt(clsubIdField.getText().toString());
//                if (TextUtils.isEmpty(cltName)) {
//                    Toast.makeText(getActivity(), "Something went wrong. Check your input values", Toast.LENGTH_LONG).show();
//                }
//                else {
//                    Client newClient = new Client(cltName, clientMgid, cltaccId, cltsubId);
//                    mDatabase.addClients(newClient);
//                    refresh();
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
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        if (mDatabase != null) {
//            mDatabase.close();
//        }
//    }
//}