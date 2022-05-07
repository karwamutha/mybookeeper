package com.example.mybookkeeper.fragmernts.expenses;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mybookkeeper.R;
import com.example.mybookkeeper.SqliteDatabase;
import com.example.mybookkeeper.managers.RefreshableFragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class ExpenseAdapter<S> extends RecyclerView.Adapter<ExpenseAdapter.ExpenseDataViewHolder>
        implements Filterable {

    private final RefreshableFragment refreshable;
    private  EditText date, amount;
    private ImageView editexp, deleteexp;
    private Context context;
    private ArrayList<ExpenseData> listExpenseDatas;
    private SqliteDatabase mDatabase;
    int clientId;

    ExpenseAdapter(Context context, RefreshableFragment refreshable, ArrayList<ExpenseData> listExpenseDatas, int clientId) {
        this.context = context;
        this.refreshable = (RefreshableFragment) refreshable;
        this.listExpenseDatas = listExpenseDatas;
        this.clientId = clientId;
        mDatabase = new SqliteDatabase(context);
    }

    @Override
    public ExpenseDataViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.expenses_list_layout, parent, false);

        date = view.findViewById(R.id.eDate);
        amount = view.findViewById(R.id.eAmount);
        editexp = view.findViewById(R.id.editExpense);
        deleteexp = view.findViewById(R.id.deleteExpense);
        return new ExpenseDataViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ExpenseDataViewHolder holder, int position) {

        final ExpenseData expenseData = listExpenseDatas.get(position);
        holder.tvDate.setText(expenseData.getDate());
        holder.tvAmount.setText("" + expenseData.getAmount());
        holder.imgEdit.setOnClickListener(view -> editTaskDialog(expenseData));
        holder.imgDelete.setOnClickListener(view -> {
            Toast.makeText(context, "Heloo. delete.", Toast.LENGTH_SHORT).show();
            mDatabase.deleteExpense(expenseData.getExpNo());
            refreshable.refresh();
        });
    }

    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    listExpenseDatas = listExpenseDatas;
                } else {
                    ArrayList<ExpenseData> filteredList = new ArrayList<>();
                    for (ExpenseData expenses : listExpenseDatas) {
//                        if (expenses.getPhone().toLowerCase().contains(charString)) {
//                            filteredList.add(expenses);
//                        }
                    }
                    listExpenseDatas = filteredList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = listExpenseDatas;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                listExpenseDatas = (ArrayList<ExpenseData>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    @Override
    public int getItemCount() {
        return listExpenseDatas.size();
    }

    private void editTaskDialog(final ExpenseData expense) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View subView = inflater.inflate(R.layout.add_expense, null);
        final EditText dateField = subView.findViewById(R.id.enterDate);
        final EditText expnoField = subView.findViewById(R.id.enterExpNo);
        final EditText subNameField = subView.findViewById(R.id.enterSubName);
        final EditText mngField = subView.findViewById(R.id.enterMgid);
        final EditText accField = subView.findViewById(R.id.enteraccId);
        final EditText subaccField = subView.findViewById(R.id.enterSubId);
        final EditText clientField = subView.findViewById(R.id.enterCltId);
        final EditText amtField = subView.findViewById(R.id.enterAmount);
        //dateField.setEnabled(false);
        expnoField.setEnabled(false);
        subNameField.setEnabled(false);
        mngField.setEnabled(false);
        accField.setEnabled(false);
        subaccField.setEnabled(false);
        clientField.setEnabled(false);

        Date date = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd/MMM/yyyy");
        String formattedDate = df.format(date);
        dateField.setText(formattedDate);
        dateField.setFocusable(false);
        dateField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v == dateField) {
                    final Calendar calendar = Calendar.getInstance();
                    int mYear = calendar.get(Calendar.YEAR);
                    int mMonth = calendar.get(Calendar.MONTH);
                    int mDay = calendar.get(Calendar.DAY_OF_MONTH);

                    //show dialog
                    DatePickerDialog datePickerDialog = new DatePickerDialog(subView.getContext(), new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
//                                dateField.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                            calendar.set(year, month, dayOfMonth);
                            SimpleDateFormat df = new SimpleDateFormat("dd/MMM/yyyy");
                            String formattedDate = df.format(calendar.getTime());
                            dateField.setText(formattedDate);
                        }
                    }, mYear, mMonth, mDay);
                    datePickerDialog.show();
                }
            }
        });

        if (expense != null) {
            dateField.setText(String.valueOf(expense.getDate()));
            expnoField.setText("" + expense.getExpNo());
            subNameField.setText("" + expense.getSubname());
            mngField.setText("" + expense.getMgId());
            accField.setText("" + expense.getAccId());
            subaccField.setText("" + expense.getSubId());
            clientField.setText("" + expense.getClientId());
            amtField.setText("" + expense.getAmount());
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Edit expense");
        builder.setView(subView);
        builder.create();
        builder.setPositiveButton("EDIT Expense", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final String date = dateField.getText().toString();
                final int expenseno = Integer.parseInt(expnoField.getText().toString());
                final String subname = subNameField.getText().toString();
                final int mngid = Integer.parseInt(mngField.getText().toString());
                final int accid = Integer.parseInt(accField.getText().toString());
                final  int subaccid = Integer.parseInt(subaccField.getText().toString());
                final  int clntid = Integer.parseInt(clientField.getText().toString());
                final double amt = Double.parseDouble(amtField.getText().toString());
                if (TextUtils.isEmpty(expenseno+"") || expenseno+"" == null) {
                    Toast.makeText(context, "Something went wrong. Check your input values", Toast.LENGTH_LONG).show();
                } else {
                    expense.setDate(date);
                    expense.setExpNo(expenseno);
                    expense.setSubname(subname);
                    expense.setMgId(mngid);
                    expense.setAccId(accid);
                    expense.setSubId(subaccid);
                    expense.setClientId(clntid);
                    expense.setSubname(subname);
                    expense.setAmount(amt);
                    mDatabase.updateExpenses(expense);
                    refreshable.refresh();
                }
            }
        });
        builder.show();
    }

    static class ExpenseDataViewHolder extends RecyclerView.ViewHolder {

        TextView tvDate;
        TextView tvAmount;
        ImageView imgEdit;
        ImageView imgDelete;

        ExpenseDataViewHolder(View itemView) {
            super(itemView);
            tvDate = itemView.findViewById(R.id.tvDate);
            imgEdit = itemView.findViewById(R.id.editExpense);
            imgDelete = itemView.findViewById(R.id.deleteExpense);
            tvAmount = itemView.findViewById(R.id.tvAmount);
        }
    }
}
