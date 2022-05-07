package com.example.mybookkeeper.clients;

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
import com.example.mybookkeeper.fragmernts.receipts.ReceiptData;
import com.example.mybookkeeper.managers.Manager;
import com.example.mybookkeeper.managers.ManagerTotal;
import com.example.mybookkeeper.managers.RefreshableFragment;
import com.example.mybookkeeper.subaccounts.SubAccount;
import com.example.mybookkeeper.subaccounts.SubAccountTotal;

import java.util.ArrayList;

public class ClientReceiptFragment extends Fragment implements RefreshableFragment {

    private SqliteDatabase mDatabase;
    RecyclerView ClientReceiptView;
    ReceiptData ReceiptData;
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
    int acntIdFromMnggrs;
    String subAccNameFromSubaccs;
    int subAccIdFromSubacc;
    int acntIdFromSubaccs;
    int mngIdFromSubacc;

    public static ClientReceiptFragment getInstance(int accId){
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
            subAccIdFromSubacc = getArguments().getInt("subAccIdFromSubacc");
//            subAccNameFromSubaccs = getArguments().getString("subAccNameFromSubaccs");
//            acntIdFromSubaccs = getArguments().getInt("acntIdFromSubaccs");
//            mngIdFromSubacc = getArguments().getInt("mngIdFromSubacc");
            startDate = getArguments().getString("startDate");
            endDate = getArguments().getString("endDate");
            ((MainActivity) getActivity()).getSupportActionBar().setTitle("CLIENTS LIST ");

        }else{
            ((MainActivity) getActivity()).getSupportActionBar().setTitle("NO Client SELECTED");
            ((MainActivity) getActivity()).getSupportActionBar().setSubtitle("SELECTED Client NOT FOUND");
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
        ArrayList<ClientTotal> allReceipts = mDatabase.listClientTotalReceipts(startDate, endDate);
        if (allReceipts.size() > 0) {
            ClientReceiptView.setVisibility(View.VISIBLE);
            ClientReceiptAdapter mAdapter = new ClientReceiptAdapter(getActivity(),  this, allReceipts, getArguments().getInt("acntIdFromDialog"));
            ClientReceiptView.setAdapter(mAdapter);
        }
        else {
            ClientReceiptView.setVisibility(View.GONE);
            Toast.makeText(getActivity(), "There is no Client in the database. Start adding now", Toast.LENGTH_LONG).show();
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
    public void navigateToClients(SubAccount subaccount) {

    }

    @Override
    public void navigateToClientsDialog(ClientTotal client) {
        Bundle args = new Bundle();
        args.putString("clientIDFromClients", subAccIdFromSubacc+"");
//        args.putString("clientNameFromClients", client.getClient().getCltName());
//        args.putInt("subAccIdFromClients", subAccIdFromSubacc);
//        args.putString("subAccNameFromClients", subAccNameFromSubaccs);
//        args.putInt("acntIdFromClients", acntIdFromSubaccs);
//        args.putInt("mngIdFromFromClients", mngIdFromSubacc);
        NavHostFragment.findNavController(ClientReceiptFragment.this)
                .navigate(R.id.action_ClientReceiptFragment_to_TransactionsFragment, args);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mDatabase != null) {
            mDatabase.close();
        }
    }
}