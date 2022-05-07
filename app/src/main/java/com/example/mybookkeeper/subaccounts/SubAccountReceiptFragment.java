package com.example.mybookkeeper.subaccounts;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mybookkeeper.MainActivity;
import com.example.mybookkeeper.R;
import com.example.mybookkeeper.SqliteDatabase;
import com.example.mybookkeeper.accounts.Account;
import com.example.mybookkeeper.accounts.AccountTotal;
import com.example.mybookkeeper.clients.ClientTotal;
import com.example.mybookkeeper.fragmernts.receipts.ReceiptData;
import com.example.mybookkeeper.managers.Manager;
import com.example.mybookkeeper.managers.ManagerTotal;
import com.example.mybookkeeper.managers.RefreshableFragment;

import java.util.ArrayList;

//import com.example.mybookkeeper.transactions.TransactionActivity;
//import com.example.mybookkeeper.transactions.TransactionsActivity;

public class SubAccountReceiptFragment extends Fragment implements RefreshableFragment {

    private SqliteDatabase mDatabase;
    RecyclerView SubAccountReceiptView;
    ReceiptData ReceiptData;
    SubAccount subAccount;
    EditText eRctNo, eDate, eSubName, eMgclid,
            eAccId, eSubaccId, eClientId, eAmount;
    EditText dateFrom, dateTo;
    int accId;
    String fromAcc;
    String clientName;

    String subAccNameFromGallety;;
    int mngIdFromFFromGallety;
    int acntIdFFromGallety;
    int subAccIdFFromGallety;
    String clientNameFFromGallety;
    int clientIDFFromGallety;
    String startDate, endDate;

    int mngIdFromDialog;
    int acntIdFromDialog;
    int acntIdFromMnggrs;
    int accIdFromAccs;
    String accNameFromAccs;
    int mngIdFromAccs;

    public static SubAccountReceiptFragment getInstance(int accId){
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
        mDatabase = new SqliteDatabase(getActivity());
        eRctNo = view.findViewById(R.id.eRctNo);
        eSubName = view.findViewById(R.id.eSubName);
        eMgclid = view.findViewById(R.id.eMgclid);
        eDate = view.findViewById(R.id.eDate);
        eAccId = view.findViewById(R.id.eAccId);
        eSubaccId = view.findViewById(R.id.eSubaccId);
        eClientId = view.findViewById(R.id.eClient);
        eAmount = view.findViewById(R.id.eAmount);

        dateFrom = view.findViewById(R.id.edDateFrom);
        dateTo = view.findViewById(R.id.edDateTo);

        if (getArguments() != null) {
//           String chooser = getArguments().getString("originPage");
//            accIdFromAccs = getArguments().getInt("accIdFromAccs");
//            accNameFromAccs = getArguments().getString("accNameFromAccs");
//            mngIdFromAccs = getArguments().getInt("mngIdFromAccs");
            startDate = getArguments().getString("startDate");
            endDate = getArguments().getString("endDate");
            ((MainActivity) getActivity()).getSupportActionBar().setTitle("SUB_ACCOUNTS LIST ");

        }else{
            ((MainActivity) getActivity()).getSupportActionBar().setTitle("NO SUBACCOUNT SELECTED");
            ((MainActivity) getActivity()).getSupportActionBar().setSubtitle("SELECTED SUBACCOUNT NOT FOUND");
        }
        dateFrom.setText(startDate);
        dateTo.setText(endDate);

        refresh();
//        Button btnAdd = view.findViewById(R.id.btnAdd);
//        btnAdd.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                addTaskDialog();
//            }
//        });
        return view;
    }
    public void refresh(){
        //Toast.makeText(getActivity(), ""+mngIdFromDialog, Toast.LENGTH_LONG).show();
        ArrayList<SubAccountTotal> allReceipts = mDatabase.listSubAccTotalReceipts(startDate, endDate);
        if (allReceipts.size() > 0) {
            SubAccountReceiptView.setVisibility(View.VISIBLE);
            SubAccountReceiptAdapter mAdapter = new SubAccountReceiptAdapter(getActivity(),  this, allReceipts, getArguments().getInt("acntIdFromDialog"));
            SubAccountReceiptView.setAdapter(mAdapter);
        }
        else {
            SubAccountReceiptView.setVisibility(View.GONE);
            Toast.makeText(getActivity(), "There is no subAccount in the database. Start adding now", Toast.LENGTH_LONG).show();
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
    public void navigateToClientTotal(SubAccountTotal accountTotal) {
        Bundle args = new Bundle();
//        args.putInt("subAccIdFromSubacc", subAccount.getSubAccountId());
//        args.putString("subAccNameFromSubaccs", subAccount.getSubAccName());
//        args.putInt("acntIdFromSubaccs", accIdFromAccs);
//        args.putInt("mngIdFromSubacc", mngIdFromAccs);
        args.putString("startDate", dateFrom.getText().toString());
        args.putString("endDate", dateTo.getText().toString());
        NavHostFragment.findNavController(SubAccountReceiptFragment.this)
                .navigate(R.id.action_SubAccountReceiptFragment_to_ClientReceiptFragment, args);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mDatabase != null) {
            mDatabase.close();
        }
    }
}