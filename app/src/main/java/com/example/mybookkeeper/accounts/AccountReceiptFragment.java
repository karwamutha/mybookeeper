package com.example.mybookkeeper.accounts;

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
import com.example.mybookkeeper.clients.ClientTotal;
import com.example.mybookkeeper.fragmernts.receipts.ReceiptData;
import com.example.mybookkeeper.managers.Manager;
import com.example.mybookkeeper.managers.ManagerTotal;
import com.example.mybookkeeper.managers.RefreshableFragment;
import com.example.mybookkeeper.subaccounts.SubAccount;
import com.example.mybookkeeper.subaccounts.SubAccountTotal;

import java.util.ArrayList;

public class AccountReceiptFragment extends Fragment implements RefreshableFragment {

    private SqliteDatabase mDatabase;
    RecyclerView AccountReceiptView;
    ReceiptData ReceiptData;
    Account account;
    EditText eRctNo, eDate, eSubName, eMgclid, eAccId, eSubaccId, eClientId, eAmount;
    int accId;
    String fromAcc;
    String clientName;

    EditText dateFrom, dateTo;

    String subAccNameFromGallety;;
    int mngIdFromFFromGallety;
    int acntIdFFromGallety;
    int subAccIdFFromGallety;
    String clientNameFFromGallety;
    int clientIDFFromGallety;
    String startDate, endDate;

    int mngIdFromDialog;
    int acntIdFromDialog;
    int mngIdFromMngs;
    String mngNameFromMngs;
    int acntIdFromMnggrs;
    String mngPhoneFromMngs;

    public static AccountReceiptFragment getInstance(int accId){
        AccountReceiptFragment r = new AccountReceiptFragment();
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

        View view = inflater.inflate(R.layout.fragment_account_receipts, container, false);

        AccountReceiptView = view.findViewById(R.id.myAccountReceiptList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        AccountReceiptView.setLayoutManager(linearLayoutManager);
        AccountReceiptView.setHasFixedSize(true);
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
//            mngIdFromMngs = getArguments().getInt("mngIdFromMngs");
//            mngNameFromMngs = getArguments().getString("mngNameFromMngs");
//            mngPhoneFromMngs = getArguments().getString("mngPhoneFromMngs");
            startDate = getArguments().getString("startDate");
            endDate = getArguments().getString("endDate");
            ((MainActivity) getActivity()).getSupportActionBar().setTitle("ACCOUNTS LIST ");

        }else{
            ((MainActivity) getActivity()).getSupportActionBar().setTitle("NO ACCOUNT SELECTED");
            ((MainActivity) getActivity()).getSupportActionBar().setSubtitle("SELECTED ACCOUNT NOT FOUND");
        }
        refresh();

        dateFrom.setText(startDate);
        dateTo.setText(endDate);
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
        ArrayList<AccountTotal> allReceipts = mDatabase.listAccTotalReceipts(startDate, endDate);
        if (allReceipts.size() > 0) {
            AccountReceiptView.setVisibility(View.VISIBLE);
            AccountReceiptAdapter mAdapter = new AccountReceiptAdapter(getActivity(),  this, allReceipts, getArguments().getInt("acntIdFromDialog"));
            AccountReceiptView.setAdapter(mAdapter);
        }
        else {
            AccountReceiptView.setVisibility(View.GONE);
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
    public void navigateToCreaateAccount() {

    }

    @Override
    public void navigateToClients(SubAccount subaccounts) {

    }

    @Override
    public void navigateToClientsDialog(ClientTotal client) {

    }

    @Override
    public void navigateToSubAccountTotal(AccountTotal ccountTotal) {
        Bundle args = new Bundle();
//        args.putInt("accIdFromAccs", account.getAccountId());
//        args.putString("accNameFromAccs", account.getAccName());
//        args.putInt("mngIdFromAccs", mngIdFromMngs);
//        args.putString("btnState", "showeButton");
//        args.putString("originPage", "FromAccsAdmin");

        args.putString("startDate", dateFrom.getText().toString());
        args.putString("endDate", dateTo.getText().toString());
        NavHostFragment.findNavController(AccountReceiptFragment.this)
                .navigate(R.id.action_AccountReceiptsFragment_to_SubAccountReceiptFragment, args);
    }

    @Override
    public void navigateToClientTotal(SubAccountTotal accountTotal) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mDatabase != null) {
            mDatabase.close();
        }
    }
}