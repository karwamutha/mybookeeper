package com.example.mybookkeeper.subaccounts;

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

public class SubAccountReceiptAdapter<S> extends RecyclerView.Adapter<SubAccountReceiptAdapter.ReceiptDataViewHolder>
        implements Filterable {
    private final RefreshableFragment refreshable;
    private ArrayList<SubAccountTotal> SubAccountReceiptAdapter;
    private  TextView tvName, tvReceiptAmount, tvExpenseAmount, tvBalanceAmount;
    private Context context;
    private SqliteDatabase mDatabase;
    int mngId;
//    TextView tvName, tvRctAmount, tvExpAmount, tvExBalAmount;

    public SubAccountReceiptAdapter(Context context, RefreshableFragment refreshable, ArrayList<SubAccountTotal> SubAccountReceiptAdapter, int mngId) {
        this.context = context;
        this.refreshable = (RefreshableFragment) refreshable;
        this.SubAccountReceiptAdapter = SubAccountReceiptAdapter;
        this.mngId = mngId;
        mDatabase = new SqliteDatabase(context);
    }

    @Override
    public SubAccountReceiptAdapter.ReceiptDataViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.subaccount_receipt_layout, parent, false);
        tvReceiptAmount = (TextView) view.findViewById(R.id.tvRctAmount);
        tvExpenseAmount = view.findViewById(R.id.tvExAmount);
        tvBalanceAmount = view.findViewById(R.id.tvBalAmount);
        return new SubAccountReceiptAdapter.ReceiptDataViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SubAccountReceiptAdapter.ReceiptDataViewHolder holder, int position) {
        final SubAccountTotal subAccountTotal = SubAccountReceiptAdapter.get(position);
        holder.tvName.setText(subAccountTotal.getSubAccount().getSubAccName());
        holder.tvReceiptAmount.setText("" + subAccountTotal.getReceiptsTotal());
        holder.tvExpenseAmount.setText("" + subAccountTotal.getExpensesTotal());
        holder.tvBalanceAmount.setText("" + (subAccountTotal.getReceiptsTotal() - subAccountTotal.getExpensesTotal()));
        holder.viewSubAccountReceipt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // open fragment here
                refreshable.navigateToClientTotal(subAccountTotal);
            }
        });
    }

    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    SubAccountReceiptAdapter = SubAccountReceiptAdapter;
                } else {
                    ArrayList<SubAccountTotal> filteredList = new ArrayList<>();
                    for (SubAccountTotal subAccountTotal : SubAccountReceiptAdapter) {
//                        if (receipts.getPhone().toLowerCase().contains(charString)) {
                        filteredList.add(subAccountTotal);
//                        }
                    }
                    SubAccountReceiptAdapter = filteredList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = SubAccountReceiptAdapter;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                SubAccountReceiptAdapter = (ArrayList<SubAccountTotal>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    @Override
    public int getItemCount() {
        return SubAccountReceiptAdapter.size();
    }

    static class ReceiptDataViewHolder extends RecyclerView.ViewHolder {
        View viewSubAccountReceipt;
        TextView tvName, tvReceiptAmount, tvExpenseAmount, tvBalanceAmount;

        ReceiptDataViewHolder(View itemView) {
            super(itemView);
            viewSubAccountReceipt = itemView.findViewById(R.id.viewSubAccountReceipt);
            tvName = itemView.findViewById(R.id.tvName);
            tvReceiptAmount = itemView.findViewById(R.id.tvRctAmount);
            tvExpenseAmount = itemView.findViewById(R.id.tvExAmount);
            tvBalanceAmount = itemView.findViewById(R.id.tvBalAmount);
        }
    }
}