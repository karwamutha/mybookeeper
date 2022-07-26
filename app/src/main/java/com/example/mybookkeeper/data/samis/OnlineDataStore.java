package com.example.mybookkeeper.data.samis;

import com.example.mybookkeeper.accounts.Account;
import com.example.mybookkeeper.accounts.AccountTotal;
import com.example.mybookkeeper.clients.Client;
import com.example.mybookkeeper.clients.ClientTotal;
import com.example.mybookkeeper.data.BaseDataStore;
import com.example.mybookkeeper.data.SqliteDatabase;
import com.example.mybookkeeper.fragmernts.expenses.ExpenseData;
import com.example.mybookkeeper.fragmernts.receipts.ReceiptData;
import com.example.mybookkeeper.managers.Manager;
import com.example.mybookkeeper.managers.ManagerTotal;
import com.example.mybookkeeper.subaccounts.SubAccount;
import com.example.mybookkeeper.subaccounts.SubAccountTotal;
import com.fasterxml.jackson.core.type.TypeReference;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class OnlineDataStore implements BaseDataStore {

    private static final String URL = "http://ledger.sbitkenya.com/bookkeeper.php/";
    private static final TypeReference<List<ReceiptData>> RECEIPT_DATA_LIST_TYPE = new TypeReference<List<ReceiptData>>() {
    };
    private static final TypeReference<List<Client>> CLIENT_LIST_TYPE = new TypeReference<List<Client>>() {
    };
    private static final TypeReference<List<Manager>> MANAGER_LIST_TYPE = new TypeReference<List<Manager>>() {
    };
    private static final TypeReference<List<Account>> ACCOUNT_LIST_TYPE = new TypeReference<List<Account>>() {
    };
    private static final TypeReference<List<SubAccount>> SUB_ACCOUNT_LIST_TYPE = new TypeReference<List<SubAccount>>() {
    };
    private static final TypeReference<List<ManagerTotal>> MANAGER_TOTAL_LIST_TYPE = new TypeReference<List<ManagerTotal>>() {
    };
    private static final TypeReference<List<AccountTotal>> ACCOUNT_TOTAL_LIST_TYPE = new TypeReference<List<AccountTotal>>() {
    };
    private static final TypeReference<List<SubAccountTotal>> SUB_ACCOUNT_TOTAL_LIST_TYPE = new TypeReference<List<SubAccountTotal>>() {
    };
    private static final TypeReference<List<ClientTotal>> CLIENT_TOTAL_LIST_TYPE = new TypeReference<List<ClientTotal>>() {
    };
    private static final TypeReference<List<ExpenseData>> EXPENSE_DATA_LIST_TYPE = new TypeReference<List<ExpenseData>>() {
    };
    private final SbitKenyaLedgerApi ledgerService;


    public OnlineDataStore() {
        this(URL);
    }

    public OnlineDataStore(String url) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(JacksonConverterFactory.create(SbitKenyaLedgerApi.mapper))
                .build();
        ledgerService = retrofit.create(SbitKenyaLedgerApi.class);
    }

    @Override
    public List<Manager> listManagers() throws IOException {
        Map<String, String> mp = Collections.singletonMap("item", "listManagers");

        return ledgerService.phpFunction(mp, MANAGER_LIST_TYPE);
    }

    @Override
    public List<Account> listAccounts(int mgid) throws IOException {
        Map<String, String> mp = new HashMap<>();
        mp.put("item", "listAccounts");

        return ledgerService.phpFunction(mp, ACCOUNT_LIST_TYPE);
    }

    @Override
    public List<SubAccount> listSubAccounts(int accId) throws IOException {
        Map<String, String> mp = new HashMap<>();
        mp.put("item", "listSubAccounts");
        mp.put(SqliteDatabase.SUB_AC_ID, String.valueOf(accId));

        return ledgerService.phpFunction(mp, SUB_ACCOUNT_LIST_TYPE);
    }

    @Override
    public List<Client> listClients(int clSubId) throws IOException {
        Map<String, String> mp = new HashMap<>();
        mp.put("item", "listClients");
        mp.put(SqliteDatabase.CLIENT_SUBAC_ID, String.valueOf(clSubId));

        return ledgerService.phpFunction(mp, CLIENT_LIST_TYPE);
    }

    @Override
    public List<ReceiptData> listReceipts(int clientId) throws IOException {
        Map<String, String> mp = new HashMap<>();
        mp.put("item", "listReceipts");
        mp.put(SqliteDatabase.REC_CLIENT_ID, String.valueOf(clientId));

        return ledgerService.phpFunction(mp, RECEIPT_DATA_LIST_TYPE);
    }

    @Override
    public List<ReceiptData> listReceipts(int clientId, String startDate, String endDate) throws IOException {
        Map<String, String> mp = new HashMap<>();
        mp.put("item", "listReceipts");
        mp.put(SqliteDatabase.REC_CLIENT_ID, String.valueOf(clientId));
        mp.put("startDate", startDate);
        mp.put("endDate", endDate);

        return ledgerService.phpFunction(mp, RECEIPT_DATA_LIST_TYPE);
    }

    @Override
    public List<ReceiptData> listAllReceipts() throws IOException {
        Map<String, String> mp = new HashMap<>();
        mp.put("item", "listAllReceipts");

        return ledgerService.phpFunction(mp, RECEIPT_DATA_LIST_TYPE);
    }

    @Override
    public List<ReceiptData> listManagerReceipts(int mngIDFromManager, String startDate, String endDate) throws IOException {
        Map<String, String> mp = new HashMap<>();
        mp.put("item", "listManagerReceipts");
        mp.put(SqliteDatabase.REC_MG_ID, String.valueOf(mngIDFromManager));
        mp.put("startDate", startDate);
        mp.put("endDate", endDate);

        return ledgerService.phpFunction(mp, RECEIPT_DATA_LIST_TYPE);
    }


    @Override
    public List<ManagerTotal> listMngrTotalReceipts() throws IOException {
        Map<String, String> mp = new HashMap<>();
        mp.put("item", "listMngrTotalReceipts");

        return ledgerService.phpFunction(mp, MANAGER_TOTAL_LIST_TYPE);

    }

    @Override
    public List<AccountTotal> listAccTotalReceipts(String startDate, String endDate) throws IOException {
        Map<String, String> mp = new HashMap<>();
        mp.put("item", "listAccTotalReceipts");
        mp.put("startDate", startDate);
        mp.put("endDate", endDate);

        return ledgerService.phpFunction(mp, ACCOUNT_TOTAL_LIST_TYPE);
    }

    @Override
    public List<AccountTotal> listAccTotalReceipts(String startDate, String endDate, int managerID) throws IOException {
        Map<String, String> mp = new HashMap<>();
        mp.put("item", "listAccTotalReceipts");
        mp.put(SqliteDatabase.AC_MNG_ID, String.valueOf(managerID));
        mp.put("startDate", startDate);
        mp.put("endDate", endDate);

        return ledgerService.phpFunction(mp, ACCOUNT_TOTAL_LIST_TYPE);
    }

    @Override
    public List<SubAccountTotal> listSubAccTotalReceipts(String startDate, String endDate) throws IOException {
        Map<String, String> mp = new HashMap<>();
        mp.put("item", "listSubAccTotalReceipts");
        mp.put("startDate", startDate);
        mp.put("endDate", endDate);

        return ledgerService.phpFunction(mp, SUB_ACCOUNT_TOTAL_LIST_TYPE);
    }

    @Override
    public List<SubAccountTotal> listSubAccTotalReceipts(String startDate, String endDate, int accountID) throws IOException {
        Map<String, String> mp = new HashMap<>();
        mp.put("item", "listSubAccTotalReceipts");
        mp.put(SqliteDatabase.SUB_AC_ID, String.valueOf(accountID));
        mp.put("startDate", startDate);
        mp.put("endDate", endDate);

        return ledgerService.phpFunction(mp, SUB_ACCOUNT_TOTAL_LIST_TYPE);
    }

    @Override
    public List<ClientTotal> listClientTotalReceipts(String startDate, String endDate) throws IOException {
        Map<String, String> mp = new HashMap<>();
        mp.put("item", "listClientTotalReceipts");
        mp.put("startDate", startDate);
        mp.put("endDate", endDate);

        return ledgerService.phpFunction(mp, CLIENT_TOTAL_LIST_TYPE);
    }

    @Override
    public List<ClientTotal> listClientTotalReceipts(String startDate, String endDate, int cltSubId) throws IOException {
        Map<String, String> mp = new HashMap<>();
        mp.put("item", "listClientTotalReceipts");
        mp.put(SqliteDatabase.CLIENT_SUBAC_ID, String.valueOf(cltSubId));
        mp.put("startDate", startDate);
        mp.put("endDate", endDate);

        return ledgerService.phpFunction(mp, CLIENT_TOTAL_LIST_TYPE);
    }

    @Override
    public SubAccount searchSubAccountByAccId(int id) throws IOException {
        Map<String, String> mp = new HashMap<>();
        mp.put("item", "searchSubAccountByAccId");
        mp.put(SqliteDatabase.SUBACCOUNT_ID, String.valueOf(id));

        return ledgerService.phpFunction(mp, SubAccount.class);
    }

    @Override
    public List<ExpenseData> listExpenses(int clientId) throws IOException {
        Map<String, String> mp = new HashMap<>();
        mp.put("item", "listExpenses");
        mp.put(SqliteDatabase.CLIENT_ID, String.valueOf(clientId));

        return ledgerService.phpFunction(mp, EXPENSE_DATA_LIST_TYPE);
    }

    @Override
    public List<ExpenseData> listExpenses(int clientId, String startDate, String endDate) throws IOException {
        Map<String, String> mp = new HashMap<>();
        mp.put("item", "listExpenses");
        mp.put(SqliteDatabase.CLIENT_ID, String.valueOf(clientId));
        mp.put("startDate", startDate);
        mp.put("endDate", endDate);

        return ledgerService.phpFunction(mp, EXPENSE_DATA_LIST_TYPE);
    }

    @Override
    public void addManagers(Manager manager) throws IOException {
        ledgerService.phpFunction("addManagers", manager);
    }

    @Override
    public void addAccounts(Account account) throws IOException {
        ledgerService.phpFunction("addAccounts", account);
    }

    @Override
    public void addSubAccounts(SubAccount subaccount) throws IOException {
        ledgerService.phpFunction("addSubAccounts", subaccount);
    }

    @Override
    public void addClients(Client clients) throws IOException {
        ledgerService.phpFunction("addClients", clients);
    }

    @Override
    public void addReceipt(ReceiptData receipts) throws IOException {
        ledgerService.phpFunction("addReceipt", receipts);
    }

    @Override
    public void addExpense(ExpenseData expenseData) throws IOException {
        ledgerService.phpFunction("addExpense", expenseData);
    }

    @Override
    public void updateManagerTotals(ManagerTotal managerTotal) throws IOException {
        ledgerService.phpFunction("updateManagerTotals", managerTotal);
    }

    @Override
    public void updateManagers(Manager managerTotal) throws IOException {
        ledgerService.phpFunction("updateManagers", managerTotal);
    }

    @Override
    public void updateAccounts(AccountTotal accountTotal) throws IOException {
        ledgerService.phpFunction("updateAccounts", accountTotal);
    }

    @Override
    public void updateSubAccount(SubAccountTotal subAccountTotal) throws IOException {
        ledgerService.phpFunction("updateSubAccount", subAccountTotal);
    }

    @Override
    public void updateClients(ClientTotal clientTotal) throws IOException {
        ledgerService.phpFunction("updateClients", clientTotal);
    }

    @Override
    public void updateReceipts(ReceiptData receiptData) throws IOException {
        ledgerService.phpFunction("updateReceipts", receiptData);
    }

    @Override
    public void updateExpense(ExpenseData expense) throws IOException {
        ledgerService.phpFunction("updateExpense", expense);
    }

    @Override
    public Manager searchManagerByID(int mg_id) throws IOException {
        Map<String, String> mp = new HashMap<>();
        mp.put("item", "searchManagerByID");
        mp.put(SqliteDatabase.MANAGER_ID, String.valueOf(mg_id));

        return ledgerService.phpFunction(mp, Manager.class);
    }

    @Override
    public Account searchAccountByAccId(int id) throws IOException {
        Map<String, String> mp = new HashMap<>();
        mp.put("item", "searchAccountByAccId");
        mp.put(SqliteDatabase.MANAGER_ID, String.valueOf(id));

        return ledgerService.phpFunction(mp, Account.class);
    }

    @Override
    public int getNextReceiptID() throws IOException {
        Map<String, String> mp = new HashMap<>();
        mp.put("item", "getNextReceiptID");

        return ledgerService.phpFunction(mp, Integer.class);
    }

    @Override
    public int getNextExpenseID() throws IOException {
        Map<String, String> mp = new HashMap<>();
        mp.put("item", "getNextExpenseID");

        return ledgerService.phpFunction(mp, Integer.class);
    }

    @Override
    public int getNextManagerID() throws IOException {
        Map<String, String> mp = new HashMap<>();
        mp.put("item", "getNextManagerID");

        return ledgerService.phpFunction(mp, Integer.class);
    }

    @Override
    public Account searchAccountByID(int id) throws IOException {
        Map<String, String> mp = new HashMap<>();
        mp.put("item", "searchAccountByID");
        mp.put(SqliteDatabase.ACCOUNT_ID, String.valueOf(id));

        return ledgerService.phpFunction(mp, Account.class);
    }

    @Override
    public SubAccount searchSubAccountByID(String id) throws IOException {
        Map<String, String> mp = new HashMap<>();
        mp.put("item", "searchSubAccountByID");
        mp.put(SqliteDatabase.SUBACCOUNT_ID, String.valueOf(id));

        return ledgerService.phpFunction(mp, SubAccount.class);
    }

    @Override
    public Client searchClientByID(int id) throws IOException {
        Map<String, String> mp = new HashMap<>();
        mp.put("item", "searchClientByID");
        mp.put(SqliteDatabase.CLIENT_ID, String.valueOf(id));

        return ledgerService.phpFunction(mp, Client.class);
    }

    @Override
    public ReceiptData searchReceiptByID(int id) throws IOException {
        Map<String, String> mp = new HashMap<>();
        mp.put("item", "searchReceiptByID");
        mp.put(SqliteDatabase.RECEIPT_ID, String.valueOf(id));

        return ledgerService.phpFunction(mp, ReceiptData.class);
    }

    @Override
    public Manager searchRctIdByManagerId(int mngIdFromDialog) throws IOException {
        Map<String, String> mp = new HashMap<>();
        mp.put("item", "searchRctIdByManagerId");
        mp.put(SqliteDatabase.REC_MG_ID, String.valueOf(mngIdFromDialog));

        return ledgerService.phpFunction(mp, Manager.class);
    }

    @Override
    public ExpenseData searchExpenseByID(int id) throws IOException {
        Map<String, String> mp = new HashMap<>();
        mp.put("item", "searchExpenseByID");
        mp.put(SqliteDatabase.EXPENSE_ID, String.valueOf(id));

        return ledgerService.phpFunction(mp, ExpenseData.class);
    }

    @Override
    public void deleteManager(int managerId) throws IOException {
        Map<String, String> map = new HashMap<>();
        map.put("item", "deleteManager");
        map.put(SqliteDatabase.MANAGER_ID, String.valueOf(managerId));

        ledgerService.phpFunctionNoBody(map);
    }

    @Override
    public void deleteAccount(int accountId) throws IOException {
        Map<String, String> map = new HashMap<>();
        map.put("item", "deleteAccount");
        map.put(SqliteDatabase.ACCOUNT_ID, String.valueOf(accountId));

        ledgerService.phpFunctionNoBody(map);
    }

    @Override
    public void deleteSubAccount(int subaccountsId) throws IOException {
        Map<String, String> map = new HashMap<>();
        map.put("item", "deleteSubAccount");
        map.put(SqliteDatabase.SUBACCOUNT_ID, String.valueOf(subaccountsId));

        ledgerService.phpFunctionNoBody(map);
    }

    @Override
    public void deleteClient(int clientsId) throws IOException {
        Map<String, String> map = new HashMap<>();
        map.put("item", "deleteClient");
        map.put(SqliteDatabase.CLIENT_ID, String.valueOf(clientsId));

        ledgerService.phpFunctionNoBody(map);
    }

    @Override
    public void deleteReceipt(int receiptsID) throws IOException {
        Map<String, String> map = new HashMap<>();
        map.put("item", "deleteReceipt");
        map.put(SqliteDatabase.RECEIPT_ID, String.valueOf(receiptsID));

        ledgerService.phpFunctionNoBody(map);
    }

    @Override
    public void deleteExpense(int ExpensesID) throws IOException {
        Map<String, String> map = new HashMap<>();
        map.put("item", "deleteExpense");
        map.put(SqliteDatabase.EXPENSE_ID, String.valueOf(ExpensesID));

        ledgerService.phpFunctionNoBody(map);
    }

    @Override
    public Manager searchManagerByPassword(String password) throws IOException {
        Map<String, String> mp = new HashMap<>();
        mp.put("item", "searchManagerByPassword");
        mp.put(SqliteDatabase.MANAGER_PASSWORD, password);

        return ledgerService.phpFunction(mp, Manager.class);
    }

    @Override
    public Manager searchManagerByPhone(String mg_phone, String password) throws IOException {
        Map<String, String> mp = new HashMap<>();
        mp.put("item", "searchManagerByPhone");
        mp.put(SqliteDatabase.MANAGER_PHONE, mg_phone);
        mp.put(SqliteDatabase.MANAGER_PASSWORD, password);

        return ledgerService.phpFunction(mp, Manager.class);
    }

    @Override
    public void close() {

    }
}
