package com.example.mybookkeeper.fragmernts.expenses;

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

public class ExpenseDetailFragment extends Fragment implements RefreshableFragment {

    private SqliteDatabase mDatabase;
    RecyclerView ExpenseView;
    EditText eExpNo, eAmount;

    String clientNameFFromDialog;
    int mngIdFromFFromDialog;
    int acntIdFFromDialog;
    int subAccIdFFromDialog;
    int clientIDFFromDialog;

    EditText dateFrom, dateTo;
    String startDate, endDate;
    public static ExpenseDetailFragment getInstance(int clientID){
        ExpenseDetailFragment r = new ExpenseDetailFragment();
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

        ExpenseData ExpenseData = null;
        Bundle args = getArguments();

        View v = inflater.inflate(R.layout.fragment_expense_detail, container, false);

        ExpenseView = v.findViewById(R.id.myExpenseList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        ExpenseView.setLayoutManager(linearLayoutManager);
        ExpenseView.setHasFixedSize(true);
        mDatabase = new SqliteDatabase(getActivity());
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
            subAccIdFFromDialog = getArguments().getInt("subAccIdFFromDialog");;
            clientIDFFromDialog = getArguments().getInt("clientIDFFromDialog");
            startDate = firstDayOfMonthStr;
            endDate = lastDayOfMonthStr;
            ((MainActivity) getActivity()).getSupportActionBar().setTitle("Expense Details for ");
            ((MainActivity) getActivity()).getSupportActionBar().setSubtitle(clientNameFFromDialog);

        }else{
            ((MainActivity) getActivity()).getSupportActionBar().setTitle("NO EXPENSES SELECTED");
            ((MainActivity) getActivity()).getSupportActionBar().setSubtitle("SELECTED EXPENSES NOT FOUND");
        }
        refresh();
        dateFrom.setText(startDate);
        dateTo.setText(endDate);
        return v;
    }
    public void refresh(){
        ArrayList<ExpenseData> allExpenses = mDatabase.listExpenses(clientIDFFromDialog);
        if (allExpenses.size() > 0) {
            ExpenseView.setVisibility(View.VISIBLE);
            ExpenseDetailAdapter mAdapter = new ExpenseDetailAdapter(getActivity(),  this, allExpenses, getArguments().getInt("clientIDFromDialog"));
            ExpenseView.setAdapter(mAdapter);
        }
        else {
            ExpenseView.setVisibility(View.GONE);
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
//        builder.setTitle("Add new Expense");
//        builder.setView(subView);
//        builder.create();
//        builder.setPositiveButton("ADD Expense", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                ExpenseData = mDatabase.searchExpenseByID(Integer.parseInt(mngField.getText().toString()));
//                final String date = dateField.getText().toString();
//                final int expNo = Integer.parseInt(ExpenseData.getExpNo()+"");
//                final String subname = subField.getText().toString();
//                final int mngId = Integer.parseInt(ExpenseData.getMgId()+"");
//                final int accId = Integer.parseInt(ExpenseData.getAccId()+"");
//                final int subaccId = Integer.parseInt(ExpenseData.getSubId()+"");
//                final int clientId = Integer.parseInt(ExpenseData.getClientId()+"");
//                final double amount = Double.parseDouble(amountField.getText().toString());
//
//                if (!TextUtils.isEmpty("" + amount)) {
//                    ExpenseData newExpense = new ExpenseData(date, expNo, subname, mngId, accId, subaccId, clientId, amount);
//                    mDatabase.addExpense(newExpense);
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