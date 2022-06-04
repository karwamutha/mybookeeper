package com.example.mybookkeeper.data.samis;

import android.content.Context;

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
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class OnlineDataStore implements BaseDataStore {

    private static final String URL = "http://ledger.sbitkenya.com/bookkeeper.php/";
    private static final Type RECEIPT_DATA_LIST_TYPE = new TypeToken<List<ReceiptData>>() {
    }.getType();
    private static final Type CLIENT_LIST_TYPE = new TypeToken<List<Client>>() {
    }.getType();;
    private final Context context;
    private final String url;
    private final SbitKenyaLedgerApi ledgerService;
    private final Gson gson;
    Type MANAGER_LIST_TYPE = new TypeToken<List<Manager>>() {
    }.getType();
    Type ACCOUNT_LIST_TYPE = new TypeToken<List<Account>>() {
    }.getType();
    Type SUB_ACCOUNT_LIST_TYPE = new TypeToken<List<SubAccount>>() {
    }.getType();


    public OnlineDataStore(Context context) {
        this(context, URL);
    }

    public OnlineDataStore(Context context, String url) {
        this.context = context;
        this.url = url;
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE).create();
        ledgerService = retrofit.create(SbitKenyaLedgerApi.class);
    }

    @Override
    public List<Manager> listManagers() throws IOException {
        Map<String, String> mp = Collections.singletonMap("item", "get_managers");

        Call<ResponseBody> managers = ledgerService.phpFunction(mp);
        Response<ResponseBody> response = managers.execute();
        if (response.isSuccessful() && response.body() != null) {
            String string = response.body().string();
            return gson.fromJson(string, MANAGER_LIST_TYPE);
        }
        return Collections.emptyList();
    }

    @Override
    public List<Account> listAccounts(int mgid) throws IOException {
        Map<String, String> mp = Collections.singletonMap("item", "get_managers");

        Call<ResponseBody> function = ledgerService.phpFunction(mp);
        Response<ResponseBody> response = function.execute();
        if (response.isSuccessful() && response.body() != null) {
            String string = response.body().string();
            return gson.fromJson(string, ACCOUNT_LIST_TYPE);
        }
        return Collections.emptyList();
    }

    @Override
    public List<SubAccount> listSubAccounts(int accId) throws IOException {

        Map<String, String> mp = new HashMap<>();
        mp.put("item", "get_subaccounts");
        mp.put(SqliteDatabase.SUB_AC_ID, String.valueOf(accId));

        Call<ResponseBody> function = ledgerService.phpFunction(mp);
        Response<ResponseBody> response = function.execute();
        if (response.isSuccessful() && response.body() != null) {
            String string = response.body().string();
            return gson.fromJson(string, SUB_ACCOUNT_LIST_TYPE);
        }
        return Collections.emptyList();
    }

    @Override
    public List<Client> listClients(int clSubId) throws IOException {
        Map<String, String> mp = new HashMap<>();
        mp.put("item", "get_clients");
        mp.put(SqliteDatabase.CLIENT_SUBAC_ID, String.valueOf(clSubId));

        Call<ResponseBody> function = ledgerService.phpFunction(mp);
        Response<ResponseBody> response = function.execute();
        if (response.isSuccessful() && response.body() != null) {
            String string = response.body().string();
            return gson.fromJson(string, CLIENT_LIST_TYPE);
        }
        return Collections.emptyList();
    }

    @Override
    public List<ReceiptData> listReceipts(int clientId) throws IOException {
        Map<String, String> mp = new HashMap<>();
        mp.put("item", "get_receipts");
        mp.put(SqliteDatabase.REC_CLIENT_ID, String.valueOf(clientId));

        Call<ResponseBody> function = ledgerService.phpFunction(mp);
        Response<ResponseBody> response = function.execute();
        if (response.isSuccessful() && response.body() != null) {
            String string = response.body().string();
            return gson.fromJson(string, RECEIPT_DATA_LIST_TYPE);
        }
        return Collections.emptyList();
    }

    @Override
    public List<ReceiptData> listReceipts(int clientId, String startDate, String endDate) throws IOException {
        Map<String, String> mp = new HashMap<>();
        mp.put("item", "get_receipts");
        mp.put(SqliteDatabase.REC_CLIENT_ID, String.valueOf(clientId));
        mp.put("start_date", startDate);
        mp.put("end_date", endDate);

        Call<ResponseBody> function = ledgerService.phpFunction(mp);
        Response<ResponseBody> response = function.execute();
        if (response.isSuccessful() && response.body() != null) {
            String string = response.body().string();
            return gson.fromJson(string, RECEIPT_DATA_LIST_TYPE);
        }
        return Collections.emptyList();
    }

    @Override
    public List<ReceiptData> listAllReceipts() throws IOException {
        Map<String, String> mp = new HashMap<>();
        mp.put("item", "get_receipts");

        Call<ResponseBody> function = ledgerService.phpFunction(mp);
        Response<ResponseBody> response = function.execute();
        if (response.isSuccessful() && response.body() != null) {
            String string = response.body().string();
            return gson.fromJson(string, RECEIPT_DATA_LIST_TYPE);
        }
        return Collections.emptyList();
    }

    @Override
    public List<ReceiptData> listManagerReceipts(int mngIDFromManager, String startDate, String endDate) {
        return null;
    }

    @Override
    public List<ManagerTotal> listMngrTotalReceipts() {
        return null;
    }

    @Override
    public List<AccountTotal> listAccTotalReceipts(String startDate, String endDate) {
        return null;
    }

    @Override
    public List<AccountTotal> listAccTotalReceipts(String startDate, String endDate, int managerID) {
        return null;
    }

    @Override
    public List<SubAccountTotal> listSubAccTotalReceipts(String startDate, String endDate) {
        return null;
    }

    @Override
    public List<SubAccountTotal> listSubAccTotalReceipts(String startDate, String endDate, int accountID) {
        return null;
    }

    @Override
    public List<ClientTotal> listClientTotalReceipts(String startDate, String endDate) {
        return null;
    }

    @Override
    public List<ClientTotal> listClientTotalReceipts(String startDate, String endDate, int cltSubId) {
        return null;
    }

    @Override
    public SubAccount searchSubAccountByAccId(int id) {
        return null;
    }

    @Override
    public List<ExpenseData> listExpenses(int clientId) {
        return null;
    }

    @Override
    public List<ExpenseData> listExpenses(int clientId, String startDate, String endDate) {
        return null;
    }

    @Override
    public void addManagers(Manager manager) {

    }

    @Override
    public void addAccounts(Account account) {

    }

    @Override
    public void addSubAccounts(SubAccount subaccount) {

    }

    @Override
    public void addClients(Client clients) {

    }

    @Override
    public void addReceipt(ReceiptData receipts) {

    }

    @Override
    public void addExpense(ExpenseData expenseData) {

    }

    @Override
    public void updateManagers(ManagerTotal managerTotal) {

    }

    @Override
    public void updateManagers(Manager managerTotal) {

    }

    @Override
    public void updateAccounts(AccountTotal accountTotal) {

    }

    @Override
    public void updateSubAccount(SubAccountTotal subAccountTotal) {

    }

    @Override
    public void updateClients(ClientTotal clientTotal) {

    }

    @Override
    public void updateReceipts(ReceiptData receiptData) {

    }

    @Override
    public void updateExpense(ExpenseData expense) {

    }

    @Override
    public Manager searchManagerByID(int mg_id) {
        return null;
    }

    @Override
    public Account searchAccountByAccId(int id) {
        return null;
    }

    @Override
    public int getNextReceiptID() {
        return 0;
    }

    @Override
    public int getNextExpenseID() {
        return 0;
    }

    @Override
    public int getNextManagerID() {
        return 0;
    }

    @Override
    public Account searchAccountByID(int id) {
        return null;
    }

    @Override
    public SubAccount searchSubAccountByID(String id) {
        return null;
    }

    @Override
    public Client searchClientByID(int id) {
        return null;
    }

    @Override
    public ReceiptData searchReceiptByID(int id) {
        return null;
    }

    @Override
    public Manager searchRctIdByManagerId(int mngIdFromDialog) {
        return null;
    }

    @Override
    public ExpenseData searchExpenseByID(int id) {
        return null;
    }

    @Override
    public void deleteManager(int managerId) {

    }

    @Override
    public void deleteAccount(int accountId) {

    }

    @Override
    public void deleteSubAccount(int subaccountsId) {

    }

    @Override
    public void deleteClient(int clientsId) {

    }

    @Override
    public void deleteReceipt(int receiptsID) {

    }

    @Override
    public void deleteExpense(int ExpensesID) {

    }

    @Override
    public Manager searchManagerByPassword(String password) {
        return null;
    }

    @Override
    public Manager searchManagerByPhone(String mg_phone, String password) {
        return null;
    }

    @Override
    public void close() {

    }
}
