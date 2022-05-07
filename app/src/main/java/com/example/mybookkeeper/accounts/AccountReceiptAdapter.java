package com.example.mybookkeeper.accounts;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.mybookkeeper.R;
import com.example.mybookkeeper.SqliteDatabase;
import com.example.mybookkeeper.managers.RefreshableFragment;

import java.util.ArrayList;

public class AccountReceiptAdapter<S> extends RecyclerView.Adapter<AccountReceiptAdapter.ReceiptDataViewHolder>
        implements Filterable {
    private final RefreshableFragment refreshable;
    private ArrayList<AccountTotal> AccountReceiptAdapter;
    private  TextView tvName, tvReceiptAmount, tvExpenseAmount, tvBalanceAmount;
    private Context context;
    private SqliteDatabase mDatabase;
    int mngId;
//    TextView tvName, tvRctAmount, tvExpAmount, tvExBalAmount;

    public AccountReceiptAdapter(Context context, RefreshableFragment refreshable, ArrayList<AccountTotal> AccountReceiptAdapter, int mngId) {
        this.context = context;
        this.refreshable = (RefreshableFragment) refreshable;
        this.AccountReceiptAdapter = AccountReceiptAdapter;
        this.mngId = mngId;
        mDatabase = new SqliteDatabase(context);
    }

    @Override
    public ReceiptDataViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.account_receipt_layout, parent, false);
        tvReceiptAmount = (TextView) view.findViewById(R.id.tvRctAmount);
        tvExpenseAmount = view.findViewById(R.id.tvExAmount);
        tvBalanceAmount = view.findViewById(R.id.tvBalAmount);
        return new ReceiptDataViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReceiptDataViewHolder holder, int position) {
        final AccountTotal accountTotal = AccountReceiptAdapter.get(position);
        holder.tvName.setText(accountTotal.getAccount().getAccName());
        holder.tvReceiptAmount.setText("" + accountTotal.getReceiptsTotal());
        holder.tvExpenseAmount.setText("" + accountTotal.getExpensesTotal());
        holder.tvBalanceAmount.setText("" + (accountTotal.getReceiptsTotal() - accountTotal.getExpensesTotal()));
        holder.viewAccountReceipt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // open fragment here
                refreshable.navigateToSubAccountTotal(accountTotal);
            }
        });
    }

    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    AccountReceiptAdapter = AccountReceiptAdapter;
                } else {
                    ArrayList<AccountTotal> filteredList = new ArrayList<>();
                    for (AccountTotal accountTotal : AccountReceiptAdapter) {
//                        if (receipts.getPhone().toLowerCase().contains(charString)) {
                        filteredList.add(accountTotal);
//                        }
                    }
                    AccountReceiptAdapter = filteredList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = AccountReceiptAdapter;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                AccountReceiptAdapter = (ArrayList<AccountTotal>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    @Override
    public int getItemCount() {
        return AccountReceiptAdapter.size();
    }

    static class ReceiptDataViewHolder extends RecyclerView.ViewHolder {
        View viewAccountReceipt;
        TextView tvName, tvReceiptAmount, tvExpenseAmount, tvBalanceAmount;

        ReceiptDataViewHolder(View itemView) {
            super(itemView);
            viewAccountReceipt = itemView.findViewById(R.id.viewAccountReceipt);
            tvName = itemView.findViewById(R.id.tvName);
            tvReceiptAmount = itemView.findViewById(R.id.tvRctAmount);
            tvExpenseAmount = itemView.findViewById(R.id.tvExAmount);
            tvBalanceAmount = itemView.findViewById(R.id.tvBalAmount);
        }
    }
}

