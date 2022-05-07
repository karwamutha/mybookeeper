package com.example.mybookkeeper.managers;

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

import java.util.ArrayList;

public class ManagerReceiptAdapter<S> extends RecyclerView.Adapter<ManagerReceiptAdapter.ReceiptDataViewHolder>
        implements Filterable {
    private final RefreshableFragment refreshable;
    private ArrayList<ManagerTotal> ManagerReceiptAdapter;
    private  TextView tvName, tvReceiptAmount, tvExpenseAmount, tvBalanceAmount;
    private Context context;
    private SqliteDatabase mDatabase;
    int mngId;
//    TextView tvName, tvRctAmount, tvExpAmount, tvExBalAmount;

    public ManagerReceiptAdapter(Context context, RefreshableFragment refreshable, ArrayList<ManagerTotal> ManagerReceiptAdapter, int mngId) {
        this.context = context;
        this.refreshable = (RefreshableFragment) refreshable;
        this.ManagerReceiptAdapter = ManagerReceiptAdapter;
        this.mngId = mngId;
        mDatabase = new SqliteDatabase(context);
    }

    @Override
    public ReceiptDataViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.manager_receipt_layout, parent, false);
        tvReceiptAmount = (TextView) view.findViewById(R.id.tvRctAmount);
        tvExpenseAmount = view.findViewById(R.id.tvExAmount);
        tvBalanceAmount = view.findViewById(R.id.tvBalAmount);
        return new ReceiptDataViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReceiptDataViewHolder holder, int position) {
        final ManagerTotal managerTotal = ManagerReceiptAdapter.get(position);
        holder.tvName.setText(managerTotal.getManager().getManagerName());
        holder.tvReceiptAmount.setText("" + managerTotal.getReceiptsTotal());
        holder.tvExpenseAmount.setText("" + managerTotal.getExpensesTotal());
        holder.tvBalanceAmount.setText("" + (managerTotal.getReceiptsTotal() - managerTotal.getExpensesTotal()));
        holder.viewManagerReceipt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // open fragment here
                refreshable.navigateToAccountTotal(managerTotal);
            }
        });
    }

    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    ManagerReceiptAdapter = ManagerReceiptAdapter;
                } else {
                    ArrayList<ManagerTotal> filteredList = new ArrayList<>();
                    for (ManagerTotal managerTotal : ManagerReceiptAdapter) {
//                        if (receipts.getPhone().toLowerCase().contains(charString)) {
                            filteredList.add(managerTotal);
//                        }
                    }
                    ManagerReceiptAdapter = filteredList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = ManagerReceiptAdapter;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                ManagerReceiptAdapter = (ArrayList<ManagerTotal>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    @Override
    public int getItemCount() {
        return ManagerReceiptAdapter.size();
    }

    static class ReceiptDataViewHolder extends RecyclerView.ViewHolder {

        View viewManagerReceipt;
        TextView tvName, tvReceiptAmount, tvExpenseAmount, tvBalanceAmount;

        ReceiptDataViewHolder(View itemView) {
            super(itemView);
            viewManagerReceipt = itemView.findViewById(R.id.viewManagerReceipt);
            tvName = itemView.findViewById(R.id.tvName);
            tvReceiptAmount = itemView.findViewById(R.id.tvRctAmount);
            tvExpenseAmount = itemView.findViewById(R.id.tvExAmount);
            tvBalanceAmount = itemView.findViewById(R.id.tvBalAmount);
        }
    }
}
