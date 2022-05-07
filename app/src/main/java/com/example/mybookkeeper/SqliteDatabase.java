package com.example.mybookkeeper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.mybookkeeper.accounts.Account;
import com.example.mybookkeeper.accounts.AccountTotal;
import com.example.mybookkeeper.clients.Client;
import com.example.mybookkeeper.clients.ClientTotal;
import com.example.mybookkeeper.fragmernts.expenses.ExpenseData;
import com.example.mybookkeeper.fragmernts.receipts.ReceiptData;
import com.example.mybookkeeper.managers.Manager;
import com.example.mybookkeeper.managers.ManagerTotal;
import com.example.mybookkeeper.subaccounts.SubAccount;
import com.example.mybookkeeper.subaccounts.SubAccountTotal;

import java.util.ArrayList;

public class SqliteDatabase extends SQLiteOpenHelper {
    //DATABASE
    private static final int DATABASE_VERSION = 5;
    private static final String DATABASE_NAME = "my_manager.db";

    //TABLES
    private static final String MANAGER_TABLE = "managers";
    private static final String ACCOUNT_TABLE = "accounts";
    private static final String SUBACCOUNT_TABLE = "subaccounts";
    private static final String CLIENT_TABLE = "clients";
    private static final String RECEIPT_TABLE = "receipts";
    private static final String EXPENSE_TABLE = "expenses";

    //MANAGER_TABLE COLUMNS
    private static final String MANAGER_ID = "ManagerId";
    private static final String MANAGER_NAME = "ManagerName";
    private static final String MANAGER_PHONE = "ManagerPhone";
    private static final String MANAGER_PASSWORD = "ManagerPword";

    //ACCOUNT_TABLE COLUMNS
    private static final String ACCOUNT_ID = "AccountId";
    private static final String ACCOUNT_NAME = "Name";
    private static final String AC_MNG_ID = "Mg_Id";

    //SUBACCOUNT_TABLE COLUMNS
    private static final String SUBACCOUNT_ID = "SubAccountId";
    private static final String SUBACCOUNT_NAME = "Name";
    private static final String SUB_AC_ID = "Ac_Id";
    private static final String SUB_MG_ID = "Mg_ID";

    //CLIENT_TABLE COLUMNS
    private static final String CLIENT_ID = "ClientId";
    private static final String CLIENT_NAME = "Name";
    private static final String CLIENT_MG_ID = "Mg_ID";
    private static final String CLIENT_AC_ID = "Ac_Id";
    private static final String CLIENT_SUBAC_ID = "Subac_Id";

    //RECEIPTS_TABLE COLUMNS
    private static final String RECEIPT_ID = "ReceiptId";
    private static final String RECEIPT_DATE = "ReceiptDate";
    private static final String RECEIPT_NO = "RctNo";
    private static final String REC_SUB_NAME = "SubName";
    private static final String REC_MG_ID = "RctMgID";
    private static final String REC_ACC_ID = "AccID";
    private static final String REC_SUBAC_ID = "SubacID";
    private static final String REC_CLIENT_ID = "ClientId";
    private static final String RECEIPT_AMOUNT = "Amount";

    //EXPENSES_TABLE COLUMNS
    private static final String EXPENSE_ID = "ExpenseId";
    private static final String EXPENSE_DATE = "Expense_Date";
    private static final String EXPENSE_NO = "ExpNo";
    private static final String EXP_SUB_NAME = "SubName";
    private static final String EXP_MG_ID = "ExpMgID";
    private static final String EXP_ACC_ID = "AccID";
    private static final String EXP_SUBAC_ID = "SubacID";
    private static final String EXP_CLIENT_ID = "ClientId";
    private static final String EXPENSE_AMOUNT = "Amount";

    public SqliteDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //CREATE TABLES
    @Override
    public void onCreate(SQLiteDatabase db) {
        //CREATE MANAGER TABLE
        String CREATE_MANAGER_TABLE = "CREATE TABLE "
                + MANAGER_TABLE + "(" + MANAGER_ID
                + " INTEGER PRIMARY KEY,"
                + MANAGER_NAME + " TEXT,"
                + MANAGER_PHONE + " TEXT,"
                + MANAGER_PASSWORD + " TEXT" + ")";

        //CREATE ACCOUNT TABLE
        String CREATE_ACCOUNT_TABLE = "CREATE TABLE "
                + ACCOUNT_TABLE + "(" + ACCOUNT_ID
                + " INTEGER PRIMARY KEY,"
                + ACCOUNT_NAME + " TEXT,"
                + AC_MNG_ID + " TEXT" + ")";

        //CREATE SUBACCOUNT TABLE
        String CREATE_SUBACCOUNT_TABLE = "CREATE TABLE "
                + SUBACCOUNT_TABLE + "(" + SUBACCOUNT_ID
                + " INTEGER PRIMARY KEY,"
                + SUBACCOUNT_NAME + " TEXT,"
                + SUB_MG_ID + " TEXT,"
                + SUB_AC_ID  + " TEXT" + ")";

        //CREATE CLIENT_TABLE
        String CREATE_CLIENT_TABLE = "CREATE TABLE "
                + CLIENT_TABLE + "(" + CLIENT_ID
                + " INTEGER PRIMARY KEY,"
                + CLIENT_NAME + " TEXT,"
                + CLIENT_MG_ID + " TEXT,"
                + CLIENT_AC_ID + " TEXT,"
                + CLIENT_SUBAC_ID + " TEXT" + ")";

        //CREATE RECEIPT_TABLE
        String CREATE_RECEIPT_TABLE = "CREATE TABLE "
                + RECEIPT_TABLE + "(" + RECEIPT_ID
                + " INTEGER PRIMARY KEY,"
                + RECEIPT_DATE + " TEXT,"
                + RECEIPT_NO + " TEXT,"
                + REC_SUB_NAME + " TEXT,"
                + REC_MG_ID + " TEXT,"
                + REC_ACC_ID + " TEXT,"
                + REC_SUBAC_ID + " TEXT,"
                + REC_CLIENT_ID + " TEXT,"
                + RECEIPT_AMOUNT + " TEXT" + ")";

        //CREATE EXPENSE_TABLE
        String CREATE_EXPENSE_TABLE = "CREATE TABLE "
                + EXPENSE_TABLE + "(" + EXPENSE_ID
                + " INTEGER PRIMARY KEY,"
                + EXPENSE_DATE + " TEXT,"
                + EXPENSE_NO + " TEXT,"
                + EXP_SUB_NAME + " TEXT,"
                + EXP_MG_ID + " TEXT,"
                + EXP_ACC_ID + " TEXT,"
                + EXP_SUBAC_ID + " TEXT,"
                + EXP_CLIENT_ID + " TEXT,"
                + EXPENSE_AMOUNT + " TEXT" + ")";

        db.execSQL(CREATE_MANAGER_TABLE);
        db.execSQL(CREATE_ACCOUNT_TABLE);
        db.execSQL(CREATE_SUBACCOUNT_TABLE);
        db.execSQL(CREATE_CLIENT_TABLE);;
        db.execSQL(CREATE_RECEIPT_TABLE);
        db.execSQL(CREATE_EXPENSE_TABLE);
    }

    //DROP TABLE IF EXISTS
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + MANAGER_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + ACCOUNT_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + SUBACCOUNT_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + CLIENT_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + RECEIPT_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + EXPENSE_TABLE);
        onCreate(db);
    }

    //FETCHING ALL ITEMS============================================================
//FETCHING ALL MANAGERS
    public ArrayList<Manager> listManagers() {
        String sql = "select * from " + MANAGER_TABLE;
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Manager> storeManagers = new ArrayList<>();
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.moveToFirst()) {
            do {
                int id = Integer.parseInt(cursor.getString(0));
                String managerName = cursor.getString(1);
                String managerPhone = cursor.getString(2);
                String managerPassword = cursor.getString(3);
                storeManagers.add(new Manager(id, managerName, managerPhone, managerPassword));
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        return storeManagers;
    }

    //FETCHING ALL ACCOUNTS
    public ArrayList<Account> listAccounts(int mgid) {
        String sql = "select * from " + ACCOUNT_TABLE + " WHERE " + AC_MNG_ID + " like '" +
                mgid + "' ";
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Account> storeAccounts = new ArrayList<>();
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.moveToFirst()) {
            do {
                int id = Integer.parseInt(cursor.getString(0));
                String accName = cursor.getString(1);
                int acc_mg_ID = cursor.getInt(2);
                storeAccounts.add(new Account(id, accName, acc_mg_ID));
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        return storeAccounts;
    }

    //FETCHING ALL ACCOUNTS
    public ArrayList<Account> listAccountReceipts(int accIdFromDialog, String startDate, String endDate) {
        String sql = "select * from " + ACCOUNT_TABLE + " WHERE " + AC_MNG_ID + " like '" +
                accIdFromDialog + "' ";
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Account> storeAccounts = new ArrayList<>();
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.moveToFirst()) {
            do {
                int id = Integer.parseInt(cursor.getString(0));
                String accName = cursor.getString(1);
                int accIdF = Integer.parseInt(cursor.getString(2));
                storeAccounts.add(new Account(accName, accIdF));
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        return storeAccounts;
    }

    //FETCHING ALL SUBACCOUNTS
    public ArrayList<SubAccount> listSubAccounts(int accId) {
        String sql = "select * from " + SUBACCOUNT_TABLE + " WHERE " + SUB_AC_ID + " like '" +
                accId + "' ";
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<SubAccount> storeSubAccounts = new ArrayList<>();
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.moveToFirst()) {
            do {
                int subaccountID = Integer.parseInt(cursor.getString(cursor.getColumnIndex(SUBACCOUNT_ID)));
                String name = cursor.getString(cursor.getColumnIndex(SUBACCOUNT_NAME));
                int sub_mg_ID = Integer.parseInt(cursor.getString(cursor.getColumnIndex(SUB_MG_ID)));
                accId = Integer.parseInt(cursor.getString(cursor.getColumnIndex(SUB_AC_ID)));
                storeSubAccounts.add(new SubAccount(subaccountID, name, sub_mg_ID, accId));
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        return storeSubAccounts;
    }

    //FETCHING ALL CLIENTS
    public ArrayList<Client> listClients(int clSubId) {
        String sql = "select * from " + CLIENT_TABLE + " WHERE " + CLIENT_SUBAC_ID + " like '" +
                clSubId + "' ";
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Client> storeClients = new ArrayList<>();
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.moveToFirst()) {
            do {
                int id = Integer.parseInt(cursor.getString(cursor.getColumnIndex(CLIENT_ID)));
                String clientName = cursor.getString(1);
                int cl_mg_id = cursor.getInt(2);
                int cl_acc_id = cursor.getInt(3);
                clSubId = Integer.parseInt(String.valueOf(cursor.getInt(4)));
                storeClients.add(new Client(id, clientName, cl_mg_id, cl_acc_id, clSubId));
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        return storeClients;
    }

    //FETCHING ALL RECEIPTS
    public ArrayList<ReceiptData> listReceipts(int clientId) {
        String sql = "select * from " + RECEIPT_TABLE + " WHERE " + CLIENT_ID + " like '" +
                clientId + "' ";
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<ReceiptData> storeReceipts = new ArrayList<>();
        try (Cursor cursor = db.rawQuery(sql, null)) {
            if (cursor.moveToFirst()) {
                do {
                    int id = Integer.parseInt(cursor.getString(cursor.getColumnIndex(RECEIPT_ID)));
                    String rctDate = cursor.getString(cursor.getColumnIndex(RECEIPT_DATE));
                    int rctNo = Integer.parseInt(cursor.getString(cursor.getColumnIndex(RECEIPT_NO)));
                    String subName = cursor.getString(cursor.getColumnIndex(REC_SUB_NAME));
                    int rctMgid = cursor.getInt(cursor.getColumnIndex(REC_MG_ID));
                    int accId = Integer.parseInt(cursor.getString(cursor.getColumnIndex(REC_ACC_ID)));
                    int subaccId = Integer.parseInt(cursor.getString(cursor.getColumnIndex(REC_SUBAC_ID)));
                    clientId = Integer.parseInt(cursor.getString(cursor.getColumnIndex(REC_CLIENT_ID)));
                    double amount = Double.parseDouble(cursor.getString(cursor.getColumnIndex(RECEIPT_AMOUNT)));
                    storeReceipts.add(new ReceiptData(id, rctDate, rctNo, subName, rctMgid, accId, subaccId, clientId, amount));
                }
                while (cursor.moveToNext());
            }
            cursor.close();
        }
        return storeReceipts;
    }

    //FETCHING ALL RECEIPTS
    public ArrayList<ReceiptData> listReceipts(int clientId, String startDate, String endDate) {
        String sql = "select * from " + RECEIPT_TABLE + " WHERE " + CLIENT_ID + " like '" +
                clientId + "' AND " + RECEIPT_DATE + " >= \"" + startDate + "\" AND " +
                "" + RECEIPT_DATE + " <= \"" + endDate + "\" ";
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<ReceiptData> storeReceipts = new ArrayList<>();
        try (Cursor cursor = db.rawQuery(sql, null)) {
            if (cursor.moveToFirst()) {
                do {
                    int id = Integer.parseInt(cursor.getString(cursor.getColumnIndex(RECEIPT_ID)));
                    String rctDate = cursor.getString(cursor.getColumnIndex(RECEIPT_DATE));
                    int rctNo = Integer.parseInt(cursor.getString(cursor.getColumnIndex(RECEIPT_NO)));
                    String subName = cursor.getString(cursor.getColumnIndex(REC_SUB_NAME));
                    int rctMgid = cursor.getInt(cursor.getColumnIndex(REC_MG_ID));
                    int accId = Integer.parseInt(cursor.getString(cursor.getColumnIndex(REC_ACC_ID)));
                    int subaccId = Integer.parseInt(cursor.getString(cursor.getColumnIndex(REC_SUBAC_ID)));
                    clientId = Integer.parseInt(cursor.getString(cursor.getColumnIndex(REC_CLIENT_ID)));
                    double amount = Double.parseDouble(cursor.getString(cursor.getColumnIndex(RECEIPT_AMOUNT)));
                    storeReceipts.add(new ReceiptData(id, rctDate, rctNo, subName, rctMgid, accId, subaccId, clientId, amount));
                }
                while (cursor.moveToNext());
            }
            cursor.close();
        }
        return storeReceipts;
    }

    //FETCHING ALL MANAGER RECEIPTS
    public ArrayList<ReceiptData> listManagerReceipts(int mngIDFromManager, String startDate, String endDate) {
        String sql = "select * from " + RECEIPT_TABLE + " WHERE " + REC_MG_ID + " like '" +
                mngIDFromManager + "' AND " + RECEIPT_DATE + " >= \"" + startDate + "\" AND " +
                "" + RECEIPT_DATE + " <= \"" + endDate + "\" ";
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<ReceiptData> storeReceipts = new ArrayList<>();
        try (Cursor cursor = db.rawQuery(sql, null)) {
            if (cursor.moveToFirst()) {
                do {
                    int id = Integer.parseInt(cursor.getString(cursor.getColumnIndex(RECEIPT_ID)));
                    String rctDate = cursor.getString(cursor.getColumnIndex(RECEIPT_DATE));
                    int rctNo = Integer.parseInt(cursor.getString(cursor.getColumnIndex(RECEIPT_NO)));
                    String subName = cursor.getString(cursor.getColumnIndex(REC_SUB_NAME));
                    int rctMgid = cursor.getInt(cursor.getColumnIndex(REC_MG_ID));
                    int accId = Integer.parseInt(cursor.getString(cursor.getColumnIndex(REC_ACC_ID)));
                    int subaccId = Integer.parseInt(cursor.getString(cursor.getColumnIndex(REC_SUBAC_ID)));
                    mngIDFromManager = Integer.parseInt(cursor.getString(cursor.getColumnIndex(REC_CLIENT_ID)));
                    double amount = Double.parseDouble(cursor.getString(cursor.getColumnIndex(RECEIPT_AMOUNT)));
                    storeReceipts.add(new ReceiptData(id, rctDate, rctNo, subName, rctMgid, accId, subaccId, mngIDFromManager, amount));
                }
                while (cursor.moveToNext());
            }
            cursor.close();
        }
        return storeReceipts;
    }

    public ArrayList<ManagerTotal> listMngrTotalReceipts(String startDate, String endDate) {
        ArrayList<ManagerTotal> items = new ArrayList<>();
        String sql = "SELECT m.*, r.Total AS ReceiptsTotal, e.Total AS ExpensesTotal " +
                "FROM " + MANAGER_TABLE + " AS m " +
                "LEFT JOIN (select " + REC_MG_ID + ", SUM(" + RECEIPT_AMOUNT + ") AS Total from " + RECEIPT_TABLE
                + " WHERE " + RECEIPT_DATE + " >= \"" + startDate + "\" AND " + "" + RECEIPT_DATE
                + " <= \"" + endDate + "\" GROUP BY " + REC_MG_ID + ") AS r ON m." + MANAGER_ID + " = " +
                "r." + REC_MG_ID + " " +
                "LEFT JOIN (select " + EXP_MG_ID + ", SUM(" + EXPENSE_AMOUNT + ") AS Total from " + EXPENSE_TABLE
                + " WHERE " + EXPENSE_DATE + " >= \"" + startDate + "\" AND " + "" + EXPENSE_DATE
                + " <= \"" + endDate + "\" GROUP BY " + EXP_MG_ID + ") AS e ON m." + MANAGER_ID + " = " +
                "e." + EXP_MG_ID;
        SQLiteDatabase db = this.getReadableDatabase();
        try (Cursor cursor = db.rawQuery(sql, null)) {
            while (cursor.moveToNext()){

                int manager_id = Integer.parseInt(cursor.getString(cursor.getColumnIndex(MANAGER_ID)));
                String myName = cursor.getString(cursor.getColumnIndex(MANAGER_NAME));
                String myPhone = cursor.getString(cursor.getColumnIndex(MANAGER_PHONE));
                String myPassword = cursor.getString(cursor.getColumnIndex(MANAGER_PASSWORD));
                Manager manager = new Manager(manager_id, myName, myPhone, myPassword);
                ManagerTotal managerTotal = new ManagerTotal(
                        manager,
                        cursor.getDouble(cursor.getColumnIndex("ReceiptsTotal")),
                        cursor.getDouble(cursor.getColumnIndex("ExpensesTotal")));
                items.add(managerTotal);
            }
        } catch (Exception er){
            er.printStackTrace();
        }
        return items;
    }

    public ArrayList<AccountTotal> listAccTotalReceipts(String startDate, String endDate) {
        ArrayList<AccountTotal> items = new ArrayList<>();
        String sql = "SELECT m.*, r.Total AS ReceiptsTotal, e.Total AS ExpensesTotal " +
                "FROM " + ACCOUNT_TABLE + " AS m " +
                "LEFT JOIN (select " + REC_ACC_ID + ", SUM(" + RECEIPT_AMOUNT + ") AS Total from " + RECEIPT_TABLE
                + " WHERE " + RECEIPT_DATE + " >= \"" + startDate + "\" AND " + "" + RECEIPT_DATE
                + " <= \"" + endDate + "\" GROUP BY " + REC_ACC_ID + ") AS r ON m." + ACCOUNT_ID + " = " +
                "r." + REC_ACC_ID + " " +
                "LEFT JOIN (select " + EXP_ACC_ID + ", SUM(" + EXPENSE_AMOUNT + ") AS Total from " + EXPENSE_TABLE
                + " WHERE " + EXPENSE_DATE + " >= \"" + startDate + "\" AND " + "" + EXPENSE_DATE
                + " <= \"" + endDate + "\" GROUP BY " + REC_ACC_ID + ") AS e ON m." + ACCOUNT_ID + " = " +
                "e." + EXP_ACC_ID;
//        String sql = "select " + REC_ACC_ID + ", SUM(" + RECEIPT_AMOUNT + ") AS Total from " + RECEIPT_TABLE
//                + " WHERE " + RECEIPT_DATE + " >= \"" + startDate + "\" AND " + "" + RECEIPT_DATE
//                + " <= \"" + endDate + "\" GROUP BY " + REC_ACC_ID;
        SQLiteDatabase db = this.getReadableDatabase();
        try (Cursor cursor = db.rawQuery(sql, null)) {
            while (cursor.moveToNext()){

                int account_id = Integer.parseInt(cursor.getString(cursor.getColumnIndex(ACCOUNT_ID)));
                String myName = cursor.getString(cursor.getColumnIndex(ACCOUNT_NAME));
                int myAcMngId = Integer.parseInt(cursor.getString(cursor.getColumnIndex(AC_MNG_ID)));
                Account account = new Account(account_id, myName, myAcMngId);
                AccountTotal accountTotal = new AccountTotal(
                        account,
                        cursor.getDouble(cursor.getColumnIndex("ReceiptsTotal")),
                        cursor.getDouble(cursor.getColumnIndex("ExpensesTotal")));
                items.add(accountTotal);
            }
        } catch (Exception er){
            er.printStackTrace();
        }
        return items;
    }

    public ArrayList<SubAccountTotal> listSubAccTotalReceipts(String startDate, String endDate) {
        ArrayList<SubAccountTotal> items = new ArrayList<>();
        String sql = "SELECT m.*, r.Total AS ReceiptsTotal, e.Total AS ExpensesTotal " +
                "FROM " + SUBACCOUNT_TABLE + " AS m " +
                "LEFT JOIN (select " + REC_SUBAC_ID + ", SUM(" + RECEIPT_AMOUNT + ") AS Total from " + RECEIPT_TABLE
                + " WHERE " + RECEIPT_DATE + " >= \"" + startDate + "\" AND " + "" + RECEIPT_DATE
                + " <= \"" + endDate + "\" GROUP BY " + REC_SUBAC_ID + ") AS r ON m." + SUBACCOUNT_ID + " = " +
                "r." + REC_SUBAC_ID + " " +
                "LEFT JOIN (select " + EXP_SUBAC_ID + ", SUM(" + EXPENSE_AMOUNT + ") AS Total from " + EXPENSE_TABLE
                + " WHERE " + EXPENSE_DATE + " >= \"" + startDate + "\" AND " + "" + EXPENSE_DATE
                + " <= \"" + endDate + "\" GROUP BY " + REC_SUBAC_ID + ") AS e ON m." + SUBACCOUNT_ID + " = " +
                "e." + EXP_SUBAC_ID;
//        String sql = "select " + REC_ACC_ID + ", SUM(" + RECEIPT_AMOUNT + ") AS Total from " + RECEIPT_TABLE
//                + " WHERE " + RECEIPT_DATE + " >= \"" + startDate + "\" AND " + "" + RECEIPT_DATE
//                + " <= \"" + endDate + "\" GROUP BY " + REC_ACC_ID;
        SQLiteDatabase db = this.getReadableDatabase();
        try (Cursor cursor = db.rawQuery(sql, null)) {
            while (cursor.moveToNext()){

                int subaccount_id = Integer.parseInt(cursor.getString(cursor.getColumnIndex(SUBACCOUNT_ID)));
                String myName = cursor.getString(cursor.getColumnIndex(SUBACCOUNT_NAME));
                int mySubAcMngId = Integer.parseInt(cursor.getString(cursor.getColumnIndex(SUB_MG_ID)));
                int mySubAcId = Integer.parseInt(cursor.getString(cursor.getColumnIndex(SUB_AC_ID)));
                SubAccount subaccount = new SubAccount(subaccount_id, myName, mySubAcMngId, mySubAcId);
                SubAccountTotal subAccountTotal = new SubAccountTotal(
                        subaccount,
                        cursor.getDouble(cursor.getColumnIndex("ReceiptsTotal")),
                        cursor.getDouble(cursor.getColumnIndex("ExpensesTotal")));
                items.add(subAccountTotal);
            }
        } catch (Exception er){
            er.printStackTrace();
        }
        return items;
    }

    public ArrayList<ClientTotal> listClientTotalReceipts(String startDate, String endDate) {
        ArrayList<ClientTotal> items = new ArrayList<>();
        String sql = "SELECT m.*, r.Total AS ReceiptsTotal, e.Total AS ExpensesTotal " +
                "FROM " + CLIENT_TABLE + " AS m " +
                "LEFT JOIN (select " + REC_CLIENT_ID + ", SUM(" + RECEIPT_AMOUNT + ") AS Total from " + RECEIPT_TABLE
                + " WHERE " + RECEIPT_DATE + " >= \"" + startDate + "\" AND " + "" + RECEIPT_DATE
                + " <= \"" + endDate + "\" GROUP BY " + REC_CLIENT_ID + ") AS r ON m." + CLIENT_ID + " = " +
                "r." + REC_CLIENT_ID + " " +
                "LEFT JOIN (select " + EXP_CLIENT_ID + ", SUM(" + EXPENSE_AMOUNT + ") AS Total from " + EXPENSE_TABLE
                + " WHERE " + EXPENSE_DATE + " >= \"" + startDate + "\" AND " + "" + EXPENSE_DATE
                + " <= \"" + endDate + "\" GROUP BY " + EXP_CLIENT_ID + ") AS e ON m." + CLIENT_ID + " = " +
                "e." + EXP_CLIENT_ID;
//        String sql = "select " + REC_ACC_ID + ", SUM(" + RECEIPT_AMOUNT + ") AS Total from " + RECEIPT_TABLE
//                + " WHERE " + RECEIPT_DATE + " >= \"" + startDate + "\" AND " + "" + RECEIPT_DATE
//                + " <= \"" + endDate + "\" GROUP BY " + REC_ACC_ID;
        SQLiteDatabase db = this.getReadableDatabase();
        try (Cursor cursor = db.rawQuery(sql, null)) {
            while (cursor.moveToNext()){

                int client_id = Integer.parseInt(cursor.getString(cursor.getColumnIndex(CLIENT_ID)));
                String myName = cursor.getString(cursor.getColumnIndex(CLIENT_NAME));
                int myClientMngId = Integer.parseInt(cursor.getString(cursor.getColumnIndex(CLIENT_MG_ID)));
                int myClientAcId = Integer.parseInt(cursor.getString(cursor.getColumnIndex(CLIENT_AC_ID)));
                int myClientSubAcId = Integer.parseInt(cursor.getString(cursor.getColumnIndex(CLIENT_SUBAC_ID)));
                Client client = new Client(client_id, myName, myClientMngId, myClientAcId, myClientSubAcId);
                ClientTotal clientTotal = new ClientTotal(
                        client,
                        cursor.getDouble(cursor.getColumnIndex("ReceiptsTotal")),
                        cursor.getDouble(cursor.getColumnIndex("ExpensesTotal")));
                items.add(clientTotal);
            }
        } catch (Exception er){
            er.printStackTrace();
        }
        return items;
    }

    public SubAccount searchSubAccountByAccId(int id){
        String sql = "SELECT * FROM " + SUBACCOUNT_TABLE + " WHERE " + SUBACCOUNT_ID + " " +
                "like '" + id + "' ";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.moveToNext()){
            int subaccount_id = Integer.parseInt(cursor.getString(cursor.getColumnIndex(SUBACCOUNT_ID)));
            String mySubName = cursor.getString(cursor.getColumnIndex(SUBACCOUNT_NAME));
            int myMngId = Integer.parseInt(cursor.getString(cursor.getColumnIndex(SUB_MG_ID)));
            int subaccID = Integer.parseInt(cursor.getString(cursor.getColumnIndex(SUB_AC_ID)));
            return new SubAccount(subaccount_id, mySubName, myMngId, subaccID);
        }
        return null;
    }

    //FETCHING ALL RECEIPTS
    public ArrayList<ExpenseData> listExpenses(int clientId) {
        String sql = "select * from " + EXPENSE_TABLE + " WHERE " + CLIENT_ID + " like '" +
                clientId + "' ";
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<ExpenseData> storeExpenses = new ArrayList<>();
        try (Cursor cursor = db.rawQuery(sql, null)) {
            if (cursor.moveToFirst()) {
                do {
                    int id = Integer.parseInt(cursor.getString(cursor.getColumnIndex(EXPENSE_ID)));
                    String expDate = cursor.getString(cursor.getColumnIndex(EXPENSE_DATE));
                    int expNo = Integer.parseInt(cursor.getString(cursor.getColumnIndex(EXPENSE_NO)));
                    String subName = cursor.getString(cursor.getColumnIndex(EXP_SUB_NAME));
                    int expMgid = cursor.getInt(cursor.getColumnIndex(EXP_MG_ID));
                    int accId = Integer.parseInt(cursor.getString(cursor.getColumnIndex(EXP_ACC_ID)));
                    int subaccId = Integer.parseInt(cursor.getString(cursor.getColumnIndex(EXP_SUBAC_ID)));
                    clientId = Integer.parseInt(cursor.getString(cursor.getColumnIndex(EXP_CLIENT_ID)));
                    double amount = Double.parseDouble(cursor.getString(cursor.getColumnIndex(EXPENSE_AMOUNT)));

                    storeExpenses.add(new ExpenseData(id, expDate, expNo, subName, expMgid, accId, subaccId, clientId, amount));
                }
                while (cursor.moveToNext());
            }
            cursor.close();
        }
        return storeExpenses;
    }

    //FETCHING ALL RECEIPTS
    public ArrayList<ExpenseData> listExpenses(int clientId, String startDate, String endDate) {
        String sql = "select * from " + EXPENSE_TABLE + " WHERE " + CLIENT_ID + " like '" +
                clientId + "' AND " + EXPENSE_DATE + " >= \"" + startDate + "\" AND " +
                "" + EXPENSE_DATE + " <= \"" + endDate + "\" ";
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<ExpenseData> storeExpenses = new ArrayList<>();
        try (Cursor cursor = db.rawQuery(sql, null)) {
            if (cursor.moveToFirst()) {
                do {
                    int id = Integer.parseInt(cursor.getString(cursor.getColumnIndex(EXPENSE_ID)));
                    String expDate = cursor.getString(cursor.getColumnIndex(EXPENSE_DATE));
                    int expNo = Integer.parseInt(cursor.getString(cursor.getColumnIndex(EXPENSE_NO)));
                    String subName = cursor.getString(cursor.getColumnIndex(EXP_SUB_NAME));
                    int expMgid = cursor.getInt(cursor.getColumnIndex(EXP_MG_ID));
                    int accId = Integer.parseInt(cursor.getString(cursor.getColumnIndex(EXP_ACC_ID)));
                    int subaccId = Integer.parseInt(cursor.getString(cursor.getColumnIndex(EXP_SUBAC_ID)));
                    clientId = Integer.parseInt(cursor.getString(cursor.getColumnIndex(EXP_CLIENT_ID)));
                    double amount = Double.parseDouble(cursor.getString(cursor.getColumnIndex(EXPENSE_AMOUNT)));

                    storeExpenses.add(new ExpenseData(id, expDate, expNo, subName, expMgid, accId, subaccId, clientId, amount));
                }
                while (cursor.moveToNext());
            }
            cursor.close();
        }
        return storeExpenses;
    }

    //==================== ADD ITEM ========================================

    //ADD A MANAGER
    public void addManagers(Manager manager) {
        ContentValues values = new ContentValues();
        values.put(MANAGER_NAME, manager.getManagerName());
        values.put(MANAGER_PHONE, manager.getManagerPhone());
        values.put(MANAGER_PASSWORD, manager.getManagerPassword());
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(MANAGER_TABLE, null, values);
    }

    //ADD AN ACCOUNT
    public void addAccounts(Account accounts) {
        ContentValues values = new ContentValues();
        values.put(ACCOUNT_NAME, accounts.getAccName());
        values.put(AC_MNG_ID, accounts.getMgId());
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(ACCOUNT_TABLE, null, values);
    }

    //ADD AN SUBACCOUNT
    public void addSubAccounts(SubAccount subaccounts) {
        ContentValues values = new ContentValues();
        values.put(SUBACCOUNT_NAME, subaccounts.getSubAccName());
        values.put(SUB_MG_ID, subaccounts.getSubMgId());
        values.put(SUB_AC_ID, subaccounts.getAccId());
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(SUBACCOUNT_TABLE, null, values);
    }

    //ADD A CLIENT
    public void addClients(Client clients) {
        ContentValues values = new ContentValues();
        values.put(CLIENT_NAME, clients.getCltName());
        values.put(CLIENT_MG_ID, clients.getCltMgid());
        values.put(CLIENT_AC_ID, clients.getCltAccid());
        values.put(CLIENT_SUBAC_ID, clients.getCltSubId());
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(CLIENT_TABLE, null, values);
    }

    //ADD A RECEIPT
    public void addReceipt(ReceiptData receipts) {
        ContentValues values = new ContentValues();
        values.put(RECEIPT_DATE, receipts.getDate());
        values.put(RECEIPT_NO, receipts.getRctNo());
        values.put(REC_SUB_NAME, receipts.getSubname());
        values.put(REC_MG_ID, receipts.getMgId());
        values.put(REC_ACC_ID, receipts.getAccId());
        values.put(REC_SUBAC_ID, receipts.getSubId());
        values.put(REC_CLIENT_ID, receipts.getClientId());
        values.put(RECEIPT_AMOUNT, receipts.getAmount());
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(RECEIPT_TABLE, null, values);
    }

    //ADD A EXPENSE
    public void addExpense(ExpenseData expenseData) {
        ContentValues values = new ContentValues();
        values.put(EXPENSE_DATE, expenseData.getDate());
        values.put(EXPENSE_NO, expenseData.getExpNo());
        values.put(EXP_SUB_NAME, expenseData.getSubname());
        values.put(EXP_MG_ID, expenseData.getMgId());
        values.put(EXP_ACC_ID, expenseData.getAccId());
        values.put(EXP_SUBAC_ID, expenseData.getSubId());
        values.put(EXP_CLIENT_ID, expenseData.getClientId());
        values.put(EXPENSE_AMOUNT, expenseData.getAmount());
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(EXPENSE_TABLE, null, values);
    }

    //==================== UPDAATE ITEM ========================================
    //UPDAATE MANAGER
    public void updateManagers(Manager manager) {
        ContentValues values = new ContentValues();
        values.put(MANAGER_ID, manager.getManagerID());
        values.put(MANAGER_NAME, manager.getManagerName());
        values.put(MANAGER_PHONE, manager.getManagerPhone());
        values.put(MANAGER_PASSWORD, manager.getManagerPassword());
        SQLiteDatabase db = this.getWritableDatabase();
        db.update(MANAGER_TABLE, values, MANAGER_ID + " = ?", new String[]{String.valueOf(manager.getManagerID())});
    }

    //UPDATE ACCOUNT
    public void updateAccounts(Account account) {
        ContentValues values = new ContentValues();
        values.put(ACCOUNT_NAME, account.getAccName());
        values.put(AC_MNG_ID, account.getMgId());
        SQLiteDatabase db = this.getWritableDatabase();
        db.update(ACCOUNT_TABLE, values, ACCOUNT_ID + " = ?", new String[]{String.valueOf(account.getAccountId())});
    }

    //UPDATE SUBACCOUNT
    public void updateSubAccount(SubAccount subaccounts) {
        ContentValues values = new ContentValues();
        values.put(SUBACCOUNT_NAME, subaccounts.getSubAccName());
        values.put(SUB_MG_ID, subaccounts.getSubMgId());
        values.put(SUB_AC_ID, subaccounts.getAccId());
        SQLiteDatabase db = this.getWritableDatabase();
        db.update(SUBACCOUNT_TABLE, values, SUBACCOUNT_ID + " = ?", new String[]{String.valueOf(subaccounts.getSubAccountId())});
    }

    //UPDATE CLIENT

    public void updateClients(Client client) {
        ContentValues values = new ContentValues();
        values.put(CLIENT_NAME, client.getCltName());
        values.put(CLIENT_MG_ID, client.getCltMgid());
        values.put(CLIENT_AC_ID, client.getCltAccid());
        values.put(CLIENT_SUBAC_ID, client.getCltSubId());
        SQLiteDatabase db = this.getWritableDatabase();
        db.update(CLIENT_TABLE, values, CLIENT_ID + " = ?", new String[]{String.valueOf(client.getId())});
    }

    //UPDATE RECEIPT

    public void updateReceipts(ReceiptData receipt) {
        ContentValues values = new ContentValues();
        values.put(RECEIPT_DATE, receipt.getDate());
        values.put(RECEIPT_NO, receipt.getRctNo());
        values.put(REC_MG_ID, receipt.getMgId());
        values.put(REC_SUB_NAME, receipt.getSubname());
        values.put(REC_ACC_ID, receipt.getAccId());
        values.put(REC_SUBAC_ID, receipt.getSubId());
        values.put(REC_CLIENT_ID, receipt.getClientId());
        values.put(RECEIPT_AMOUNT, receipt.getAmount());
        SQLiteDatabase db = this.getWritableDatabase();
        db.update(RECEIPT_TABLE, values, RECEIPT_ID + " = ?", new String[]{String.valueOf(receipt.getRctID())});
    }

    //UPDATE EXPENSE

    public void updateExpenses(ExpenseData expense) {
        ContentValues values = new ContentValues();
        values.put(EXPENSE_DATE, expense.getDate());
        values.put(EXPENSE_NO, expense.getExpNo());
        values.put(EXP_MG_ID, expense.getMgId());
        values.put(EXP_SUB_NAME, expense.getSubname());
        values.put(EXP_ACC_ID, expense.getAccId());
        values.put(EXP_SUBAC_ID, expense.getSubId());
        values.put(EXP_CLIENT_ID, expense.getClientId());
        values.put(EXPENSE_AMOUNT, expense.getAmount());
        SQLiteDatabase db = this.getWritableDatabase();
        db.update(EXPENSE_TABLE, values, EXPENSE_ID + " = ?", new String[]{String.valueOf(expense.getExpID())});
    }
//======================== SEARCH BY ID =======================================

    public Manager searchManagerByID(int mg_id){
        String sql = "SELECT * FROM " + MANAGER_TABLE + " WHERE " + MANAGER_ID + " " +
                "like '" + mg_id + "' ";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.moveToNext()){
            int manager_id = Integer.parseInt(cursor.getString(cursor.getColumnIndex(MANAGER_ID)));
            String myName = cursor.getString(cursor.getColumnIndex(MANAGER_NAME));
            String myPhone = cursor.getString(cursor.getColumnIndex(MANAGER_PHONE));
            String myPassword = cursor.getString(cursor.getColumnIndex(MANAGER_PASSWORD));
            return new Manager(manager_id, myName, myPhone, myPassword);
        }
        return null;
    }

    public Account searchAccountByAccId(int id){
        String sql = "SELECT * FROM " + ACCOUNT_TABLE + " WHERE " + ACCOUNT_ID + " " +
                "like '" + id + "' ";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.moveToNext()){
            int account_id = Integer.parseInt(cursor.getString(cursor.getColumnIndex(ACCOUNT_ID)));
            String myName = cursor.getString(cursor.getColumnIndex(ACCOUNT_NAME));
            int accID = Integer.parseInt(cursor.getString(cursor.getColumnIndex(AC_MNG_ID)));
            return new Account(myName, accID);
        }
        return null;
    }

    public int getNextReceiptID(){
        String sql = "SELECT MAX(" + RECEIPT_ID + ") as Receipt FROM " + RECEIPT_TABLE + " ";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(sql, null);
        int max = 0;
        if (c.moveToNext()){
            max = c.getInt(c.getColumnIndex("Receipt"));
        }
        return max + 1;
    }


    public int getNextExpenseID(){
        String sql = "SELECT MAX(" + EXPENSE_ID + ") as expense FROM " + EXPENSE_TABLE + " ";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(sql, null);
        int max = 0;
        if (c.moveToNext()){
            max = c.getInt(c.getColumnIndex("expense"));
        }
        return max + 1;
    }

    public int getNextManagerID(){
        String sql = "SELECT MAX(" + MANAGER_ID + ") as Mg_ID FROM " + MANAGER_TABLE + " ";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(sql, null);
        int max = 0;
        if (c.moveToNext()){
            max = c.getInt(c.getColumnIndex("Mg_ID"));
        }
        return max + 1;
    }

    public Account searchAccountByID(int id){
        String sql = "SELECT * FROM " + ACCOUNT_TABLE + " WHERE " + ACCOUNT_ID + " " +
                "like '" + id + "' ";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.moveToNext()){
            int accountId = Integer.parseInt(cursor.getString(cursor.getColumnIndex(ACCOUNT_ID)));
            String accName = cursor.getString(cursor.getColumnIndex(ACCOUNT_NAME));
            int acc_mg_ID = Integer.parseInt(cursor.getString(cursor.getColumnIndex(AC_MNG_ID)));
            return new Account(accName, acc_mg_ID);
        }
        return null;
    }

    public SubAccount searchSubAccountByID(String id){
        String sql = "SELECT * FROM " + SUBACCOUNT_TABLE + " WHERE " + SUBACCOUNT_ID + " " +
                "like '" + id + "' ";
        System.out.println(sql);
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.moveToNext()){
            int subaccid = Integer.parseInt(cursor.getString(cursor.getColumnIndex(SUBACCOUNT_ID)));
            String name = cursor.getString(cursor.getColumnIndex(SUBACCOUNT_NAME));
            int sub_mg_id = Integer.parseInt(cursor.getString(cursor.getColumnIndex(SUB_MG_ID)));
            int accId = Integer.parseInt(cursor.getString(cursor.getColumnIndex(SUB_AC_ID)));
            return new SubAccount(subaccid, name, sub_mg_id, accId);
        }
        return null;
    }

    public Client searchClientByID(int id){
        String sql = "SELECT * FROM " + CLIENT_TABLE + " WHERE " + CLIENT_ID + " " +
                "like '" + id + "' ";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.moveToNext()){
            int client_id = Integer.parseInt(cursor.getString(cursor.getColumnIndex(CLIENT_ID)));
            String client_name = cursor.getString(cursor.getColumnIndex(CLIENT_NAME));
            int client_mg_id = Integer.parseInt(cursor.getString(cursor.getColumnIndex(CLIENT_MG_ID)));
            int client_acc_id = Integer.parseInt(cursor.getString(cursor.getColumnIndex(CLIENT_AC_ID)));
            int client_subacc_id = Integer.parseInt(cursor.getString(cursor.getColumnIndex(CLIENT_SUBAC_ID)));
            return new Client(client_id, client_name, client_mg_id, client_acc_id, client_subacc_id);
        }
        return null;
    }

    public ReceiptData searchReceiptByID(int id){
        String sql = "SELECT * FROM " + RECEIPT_TABLE + " WHERE " + RECEIPT_ID + " " +
                "like '" + id + "' ";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.moveToNext()){
            int receipt_id = Integer.parseInt(cursor.getString(cursor.getColumnIndex(RECEIPT_ID)));
            String date = cursor.getString(cursor.getColumnIndex(RECEIPT_DATE));
            int rctNo = Integer.parseInt(cursor.getString(cursor.getColumnIndex(RECEIPT_NO)));
            String subName = cursor.getString(cursor.getColumnIndex(REC_SUB_NAME));
            int rct_mg_id = cursor.getInt(cursor.getColumnIndex(REC_MG_ID));
            int accId = Integer.parseInt(cursor.getString(cursor.getColumnIndex(REC_ACC_ID)));
            int subaccId = Integer.parseInt(cursor.getString(cursor.getColumnIndex(REC_SUBAC_ID)));
            int clientId = Integer.parseInt(cursor.getString(cursor.getColumnIndex(REC_CLIENT_ID)));
            double amount = Double.parseDouble(cursor.getString(cursor.getColumnIndex(RECEIPT_AMOUNT)));
            return new ReceiptData(receipt_id, date, rctNo, subName, rct_mg_id, accId, subaccId, clientId, amount);
        }
        return null;
    }

    public Manager searchRctIdByManagerId(int mngIdFromDialog) {
        String sql = "SELECT * FROM " + RECEIPT_TABLE + " WHERE " + REC_MG_ID + " " +
                "like '" + mngIdFromDialog+ "' ";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.moveToNext()){
            int manager_id = Integer.parseInt(cursor.getString(cursor.getColumnIndex(MANAGER_ID)));
            String myName = cursor.getString(cursor.getColumnIndex(MANAGER_NAME));
            String myPhone = cursor.getString(cursor.getColumnIndex(MANAGER_PHONE));
            String myPassword = cursor.getString(cursor.getColumnIndex(MANAGER_PASSWORD));
            return new Manager(manager_id, myName, myPhone, myPassword);
        }
        return null;
    }

    public ExpenseData searchExpenseByID(int id){
        String sql = "SELECT * FROM " + EXPENSE_TABLE + " WHERE " + EXPENSE_ID + " " +
                "like '" + id + "' ";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.moveToNext()){
            int expense_id = Integer.parseInt(cursor.getString(cursor.getColumnIndex(EXPENSE_ID)));
            String date = cursor.getString(cursor.getColumnIndex(EXPENSE_DATE));
            int expNo = Integer.parseInt(cursor.getString(cursor.getColumnIndex(EXPENSE_NO)));
            String subName = cursor.getString(cursor.getColumnIndex(EXP_SUB_NAME));
            int exp_mg_id = cursor.getInt(cursor.getColumnIndex(EXP_MG_ID));
            int accId = Integer.parseInt(cursor.getString(cursor.getColumnIndex(EXP_ACC_ID)));
            int subaccId = Integer.parseInt(cursor.getString(cursor.getColumnIndex(EXP_SUBAC_ID)));
            int clientId = Integer.parseInt(cursor.getString(cursor.getColumnIndex(EXP_CLIENT_ID)));
            double amount = Double.parseDouble(cursor.getString(cursor.getColumnIndex(EXPENSE_AMOUNT)));
            return new ExpenseData(expense_id, date, expNo, subName, exp_mg_id, accId, subaccId, clientId, amount);
        }
        return null;
    }

    //==================== DELETE ITEM ========================================
    //DELETE MANAGER
    public void deleteManager(int managerId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(MANAGER_TABLE, MANAGER_ID + " = ?", new String[]{String.valueOf(managerId)});
    }

    //DELETE ACCOUNT
    public void deleteAccount(int accountId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(ACCOUNT_TABLE, ACCOUNT_ID + " = ?", new String[]{String.valueOf(accountId)});
    }

    //DELETE SUBACCOUNT
    public void deleteSubAccount(int subaccountsId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(SUBACCOUNT_TABLE, SUBACCOUNT_ID + " = ?", new String[]{String.valueOf(subaccountsId)});
    }

    //DELETE CLIENT
    public void deleteClient(int clientsId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(CLIENT_TABLE, CLIENT_ID + " = ?", new String[]{String.valueOf(clientsId)});
    }

    //DELETE CLIENT
    public void deleteReceipt(int receiptsID) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(RECEIPT_TABLE, RECEIPT_ID + " = ?", new String[]{String.valueOf(receiptsID)});
    }

    //DELETE CLIENT
    public void deleteExpense(int ExpensesID) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(EXPENSE_TABLE, EXPENSE_ID + " = ?", new String[]{String.valueOf(ExpensesID)});
    }

    public Manager searchManagerByPassword(String password) {
        String sql = "SELECT * FROM " + MANAGER_TABLE + " WHERE " + MANAGER_PASSWORD + " " +
                "like '" + password + "' ";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.moveToNext()){
            int manager_id = Integer.parseInt(cursor.getString(cursor.getColumnIndex(MANAGER_ID)));
            String myName = cursor.getString(cursor.getColumnIndex(MANAGER_NAME));
            String myPhone = cursor.getString(cursor.getColumnIndex(MANAGER_PHONE));
            String myPassword = cursor.getString(cursor.getColumnIndex(MANAGER_PASSWORD));
            return new Manager(manager_id, myName, myPhone, myPassword);
        }
        return null;
    }

    public Manager searchManagerByPhone(String mg_phone, String password) {
        String sql = "SELECT * FROM " + MANAGER_TABLE + " WHERE " + MANAGER_PHONE + " " +
                "like '" + mg_phone + "' AND " + MANAGER_PASSWORD + " like '" + password + "'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.moveToNext()){
            int manager_id = Integer.parseInt(cursor.getString(cursor.getColumnIndex(MANAGER_ID)));
            String myName = cursor.getString(cursor.getColumnIndex(MANAGER_NAME));
            String myPhone = cursor.getString(cursor.getColumnIndex(MANAGER_PHONE));
            String myPassword = cursor.getString(cursor.getColumnIndex(MANAGER_PASSWORD));
            return new Manager(manager_id, myName, myPhone, myPassword);
        }
        return null;
    }
}