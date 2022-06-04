package com.example.mybookkeeper.fragmernts.receipts;

import android.app.AlertDialog;
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

public class ReceiptDetailAdapter<S> extends RecyclerView.Adapter<ReceiptDetailAdapter.ReceiptDataViewHolder>
        implements Filterable {

    private final RefreshableFragment refreshable;
    private EditText date, amount;
    private ImageView editexp, deleteexp;
    private Context context;
    private List<ReceiptData> listReceiptDatas;
    private UIDataStore mDatabase;
    int clientId;

    ReceiptDetailAdapter(Context context, RefreshableFragment refreshable, List<ReceiptData> listReceiptDatas, int clientId) {
        this.context = context;
        this.refreshable = (RefreshableFragment) refreshable;
        this.listReceiptDatas = listReceiptDatas;
        this.clientId = clientId;
        mDatabase = new UIDataStore(context);
    }

    @Override
    public ReceiptDataViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.receipt_detail_layout, parent, false);
        date = view.findViewById(R.id.eDate);
        amount = view.findViewById(R.id.eAmount);
        return new ReceiptDetailAdapter.ReceiptDataViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReceiptDetailAdapter.ReceiptDataViewHolder holder, int position) {
        final ReceiptData receiptData = listReceiptDatas.get(position);
        holder.tvDate.setText(receiptData.getDate());
        holder.tvAmount.setText("" + receiptData.getAmount());
        holder.editReceipt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editTaskDialog(receiptData);
            }
        });
        holder.deleteReceipt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                alertDialogBuilder.setTitle("Confirm Delete");
                alertDialogBuilder.setIcon(R.drawable.delete);
                alertDialogBuilder.setMessage("Delete   " + "'" + receiptData.getRctID() + "'  ?");
                alertDialogBuilder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        mDatabase.deleteReceipt(receiptData.getRctID());
                        refreshable.refresh();
                    }
                });
                alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                AlertDialog alertDialog = alertDialogBuilder.create();
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
                    listReceiptDatas = listReceiptDatas;
                } else {
                    ArrayList<ReceiptData> filteredList = new ArrayList<>();
                    for (ReceiptData Receipts : listReceiptDatas) {
//                        if (receipts.getPhone().toLowerCase().contains(charString)) {
//                            filteredList.add(receipts);
//                        }
                    }
                    listReceiptDatas = filteredList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = listReceiptDatas;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                listReceiptDatas = (ArrayList<ReceiptData>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    @Override
    public int getItemCount() {
        return listReceiptDatas.size();
    }

    private void editTaskDialog(final ReceiptData receipt) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View subView = inflater.inflate(R.layout.edit_receipt, null);
        final EditText dateField = subView.findViewById(R.id.enterDate);
        final EditText rctnoField = subView.findViewById(R.id.enterRctNo);
        final EditText mngField = subView.findViewById(R.id.enterMgid);
        final EditText accField = subView.findViewById(R.id.enteraccId);
        final EditText subaccField = subView.findViewById(R.id.enterSubId);
        final EditText cltIdField = subView.findViewById(R.id.enterCltId);
        final EditText cltNameField = subView.findViewById(R.id.clientName);
        final EditText amtField = subView.findViewById(R.id.eAmount);
        dateField.setEnabled(false);
        rctnoField.setEnabled(false);
        mngField.setEnabled(false);
        accField.setEnabled(false);
        subaccField.setEnabled(false);
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
                            dateField.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
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

        if (receipt != null) {
            dateField.setText(String.valueOf(receipt.getDate()));
            rctnoField.setText("" + receipt.getRctNo());
            mngField.setText("" + receipt.getMgId());
            accField.setText("" + receipt.getAccId());
            subaccField.setText("" + receipt.getSubId());
            cltIdField.setText("" + receipt.getClientId());
            cltNameField.setText(receipt.getCltName());
            amtField.setText("" + receipt.getAmount());
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Edit receipt");
        builder.setView(subView);
        AlertDialog alertDialog = builder.create();
        builder.setPositiveButton("EDIT receipt", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final String date = dateField.getText().toString();
                final int receiptno = Integer.parseInt(rctnoField.getText().toString());
                final int mngid = Integer.parseInt(mngField.getText().toString());
                final int accid = Integer.parseInt(accField.getText().toString());
                final int subaccid = Integer.parseInt(subaccField.getText().toString());
                final int clntid = Integer.parseInt(cltIdField.getText().toString());
                final String cltName = cltNameField.getText().toString();
                final double amt = Double.parseDouble(amtField.getText().toString());
                if (TextUtils.isEmpty(receiptno + "") || receiptno + "" == null) {
                    Toast.makeText(context, "Something went wrong. Check your input values", Toast.LENGTH_LONG).show();
                } else {
                    receipt.setDate(date);
                    receipt.setRctNo(receiptno);
                    receipt.setMgId(mngid);
                    receipt.setAccId(accid);
                    receipt.setSubId(subaccid);
                    receipt.setClientId(clntid);
                    receipt.setCltName(cltName);
                    receipt.setAmount(amt);
                    showDialog(alertDialog);
                    mDatabase.updateReceipts(receipt)
                            .observe(refreshable.getViewLifecycleOwner(), voidResult -> closeDialog(alertDialog));
                    refreshable.refresh();
                }
            }
        });
        builder.show();
    }

    static class ReceiptDataViewHolder extends RecyclerView.ViewHolder {

        TextView tvDate;
        TextView tvAmount;
        ImageView editReceipt;
        ImageView deleteReceipt;

        ReceiptDataViewHolder(View itemView) {
            super(itemView);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvAmount = itemView.findViewById(R.id.tvAmount);
            editReceipt = itemView.findViewById(R.id.editReceipt);
            deleteReceipt = itemView.findViewById(R.id.deleteReceipt);
        }
    }

    private void closeDialog(AlertDialog alertDialog) {
        alertDialog.dismiss();
    }

    private void showDialog(AlertDialog alertDialog) {
        ProgressBar progressBar = new ProgressBar(context);
        progressBar.setIndeterminate(true);
        alertDialog.setView(progressBar);
    }
}