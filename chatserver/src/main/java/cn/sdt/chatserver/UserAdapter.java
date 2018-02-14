package cn.sdt.chatserver;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by SDT13411 on 2018/2/9.
 */

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    private Context mContext;
    private List<String> userList;

    public UserAdapter(Context mContext, List<String> userList) {
        this.mContext = mContext;
        this.userList = userList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        TextView tvUser = new TextView(mContext);
        ViewHolder viewHolder = new ViewHolder(tvUser);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.tvUser.setText(userList.get(position));
    }

    @Override
    public int getItemCount() {
        return userList != null ? userList.size() : 0;
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvUser;

        public ViewHolder(View itemView) {
            super(itemView);
            tvUser = (TextView) itemView;
        }
    }
}
