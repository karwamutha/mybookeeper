package com.example.mybookkeeper.fragmernts.expenses;


import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.mybookkeeper.R;
import com.example.mybookkeeper.data.UIDataStore;
import com.example.mybookkeeper.managers.RefreshableFragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ExpenseDetailAdapter<S> extends RecyclerView.Adapter<ExpenseDetailAdapter.ExpenseDataViewHolder>
        implements Filterable {

    private final RefreshableFragment refreshable;
    private EditText date, descr, amount;
    private ImageView editexp, deleteexp;
    private Context context;
    private List<ExpenseData> listExpenseDatas;
    private final UIDataStore mDatabase;
    int clientId;
    private AlertDialog alertDialog;
    private ProgressDialog progress;

    ExpenseDetailAdapter(Context context, RefreshableFragment refreshable, List<ExpenseData> listExpenseDatas, int clientId) {
        this.context = context;
        this.refreshable = (RefreshableFragment) refreshable;
        this.listExpenseDatas = listExpenseDatas;
        this.clientId = clientId;
        mDatabase = new UIDataStore(context);
    }

    @Override
    public ExpenseDataViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.expenses_detail_layout, parent, false);

        date = view.findViewById(R.id.eDate);
        amount = view.findViewById(R.id.eAmount);
        return new ExpenseDataViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ExpenseDataViewHolder holder, int position) {

        final ExpenseData expenseData = listExpenseDatas.get(position);
        holder.tvDate.setText(expenseData.getDate());
        holder.tvDescr.setText(expenseData.getDescr());
        holder.tvAmount.setText("" + expenseData.getAmount());
        holder.editExpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editTaskDialog(expenseData);
            }
        });
        holder.deleteExpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                alertDialogBuilder.setTitle("Confirm Delete");
                alertDialogBuilder.setIcon(R.drawable.delete);
                alertDialogBuilder.setMessage("Delete   " + "'" + expenseData.getExpID() + "'  ?");
                alertDialogBuilder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        showProgressDialog("Deleting...");
                        mDatabase.deleteExpense(expenseData.getExpID())
                                .observe(refreshable.getViewLifecycleOwner(), r -> closeProgressDialog());
                        refreshable.refresh();
                    }
                });
                alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
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
        View subView = inflater.inflate(R.layout.edit_expense, null);
        final EditText dateField = subView.findViewById(R.id.enterDate);
        final EditText expnoField = subView.findViewById(R.id.enterExpNo);
        final EditText mngIdField = subView.findViewById(R.id.enterMgid);
        final EditText accIdField = subView.findViewById(R.id.enteraccId);
        final EditText subIdField = subView.findViewById(R.id.enterSubId);
        final EditText cltIdField = subView.findViewById(R.id.enterCltId);
        final EditText cltNameField = subView.findViewById(R.id.enterCltName);
        final EditText descrField = subView.findViewById(R.id.eDescription);
        final EditText amtField = subView.findViewById(R.id.enterAmount);
        //dateField.setEnabled(false);
        expnoField.setEnabled(false);
        mngIdField.setEnabled(false);
        accIdField.setEnabled(false);
        subIdField.setEnabled(false);
        cltIdField.setEnabled(false);

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
            Toast.makeText(context, expense.getDescr(), Toast.LENGTH_SHORT).show();
            dateField.setText(String.valueOf(expense.getDate()));
            expnoField.setText("" + expense.getExpNo());
            mngIdField.setText("" + expense.getMgId());
            accIdField.setText("" + expense.getAccId());
            subIdField.setText("" + expense.getSubId());
            cltIdField.setText("" + expense.getClientId());
            cltNameField.setText(expense.getCltName());
            descrField.setText(expense.getDescr());
            amtField.setText("" + expense.getAmount());
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Edit expense");
        builder.setView(subView);
        alertDialog = builder.create();
        builder.setPositiveButton("EDIT Expense", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final String date = dateField.getText().toString();
                final int expNo = Integer.parseInt(expnoField.getText().toString());
                final int mngid = Integer.parseInt(mngIdField.getText().toString());
                final int accid = Integer.parseInt(accIdField.getText().toString());
                final int subid = Integer.parseInt(subIdField.getText().toString());
                final int clntid = Integer.parseInt(cltIdField.getText().toString());
                final String cltName = cltNameField.getText().toString();
                final String ddescr = descrField.getText().toString();
                final double amt = Double.parseDouble(amtField.getText().toString());

                Toast.makeText(context, ddescr, Toast.LENGTH_LONG).show();
                if (TextUtils.isEmpty(amt + "") || ddescr == null) {
                    Toast.makeText(context, "Something went wrong. Check your input values", Toast.LENGTH_SHORT).show();
                } else {
                    expense.setDate(date);
                    expense.setExpNo(expNo);
                    expense.setMgId(mngid);
                    expense.setAccId(accid);
                    expense.setSubId(subid);
                    expense.setClientId(clntid);
                    expense.setCltName(cltName);
                    expense.setDescr(ddescr);
                    expense.setAmount(amt);
                    showProgressDialog("Editing..");
                    mDatabase.updateExpense(expense)
                            .observe(refreshable.getViewLifecycleOwner(), r -> closeProgressDialog());
                    refreshable.refresh();
                }
            }
        });
        builder.show();
    }

    static class ExpenseDataViewHolder extends RecyclerView.ViewHolder {

        TextView tvDate;
        TextView tvDescr;
        TextView tvAmount;
        ImageView editExpense;
        ImageView deleteExpense;

        ExpenseDataViewHolder(View itemView) {
            super(itemView);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvDescr = itemView.findViewById(R.id.tvDescriptipn);
            tvAmount = itemView.findViewById(R.id.tvAmount);
            editExpense = itemView.findViewById(R.id.editExpense);
            deleteExpense = itemView.findViewById(R.id.deleteExpense);
        }
    }

    private void closeProgressDialog() {
        if (progress != null) {
            progress.dismiss();
            progress = null;
        }
    }

    private void showProgressDialog(String message) {
        if (progress == null) {
            progress = new ProgressDialog(context);

            progress.setMessage(message);
            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progress.setCancelable(false);
            progress.setIndeterminate(true); progress.show();
        }
    }
}