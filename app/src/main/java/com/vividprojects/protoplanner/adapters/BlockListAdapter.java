package com.vividprojects.protoplanner.adapters;

import android.content.Context;

import com.vividprojects.protoplanner.R;
import com.vividprojects.protoplanner.bindingmodels.BlockItemListBindingModel;
import com.vividprojects.protoplanner.bindingmodels.RecordItemListBindingModel;
import com.vividprojects.protoplanner.coredata.Block;
import com.vividprojects.protoplanner.coredata.Record;
import com.vividprojects.protoplanner.utils.DeleteDialogHelper;
import com.vividprojects.protoplanner.utils.ItemActionsBlock;
import com.vividprojects.protoplanner.utils.ItemActionsRecord;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class BlockListAdapter extends DataBindingAdapter implements ItemActionsBlock {
    private List<Block.Plain> data = new ArrayList<>();
    private List<Block.Plain> filtered_data = new ArrayList<>();
    private List<BlockItemListBindingModel> models = new ArrayList<>();

    private WeakReference<Context> context;
    private ItemActionsBlock master;
    private String defaultImage;
    private String filter;

    public BlockListAdapter(Context context, String defaultImage) {
        this.context = new WeakReference<>(context);
        this.defaultImage = defaultImage;
    }

    public void setMaster(ItemActionsBlock master) {
        this.master = master;
    }

    @Override
    protected int getLayoutIdForPosition(int position) {
        return R.layout.block_item;
    }

    @Override
    public int getItemCount() {
        return filtered_data != null ? filtered_data.size() : 0;
    }

    @Override
    public Object getObjForPosition(int position) {
        return models.get(position);
    }

    public void setData(List<Block.Plain> data) {
        this.data.clear();
        this.data.addAll(data);

        this.filtered_data = data;
        createModels();
    }

    public void setFilter(String filter) {

/*
        String fLower;
        if (filter != null)
            fLower = filter.toLowerCase();
        else
            return;
        if (!fLower.equals(this.filter)) {
            this.filter = fLower;
            filtered_data = new ArrayList<>();
            for (Record.Plain r : data) {
                if (r.name.toLowerCase().contains(filter) || (r.mainVariant != null && r.mainVariant.title.toLowerCase().contains(filter)))
                    filtered_data.add(r);
            }
            createModels();
            notifyDataSetChanged();
        } else
            filtered_data = data;
*/

    }

    private void createModels() {
        models.clear();
        for (int i = 0; i < filtered_data.size(); i++) {

            Block.Plain v = filtered_data.get(i);
            BlockItemListBindingModel model = new BlockItemListBindingModel(context.get(),this, v, defaultImage);

            models.add(model);
        }
    }

    @Override
    public void itemBlockDelete(String id) {
        DeleteDialogHelper.show(context.get(), "Are you sure?", () -> {
            int pos;
            Block.Plain r = null;
            for (pos = 0; pos < filtered_data.size(); pos++)
                if (filtered_data.get(pos).id.equals(id)) {
                    r = filtered_data.get(pos);
                    break;
                }
            data.remove(r);
            filtered_data.remove(r);
            models.remove(pos);
            master.itemBlockDelete(id);
            notifyItemRemoved(pos);
        });
    }

    @Override
    public void itemBlockEdit(String id) {
        clearSelected();
        master.itemBlockEdit(id);
    }

    public void clearSelected() {
        for (BlockItemListBindingModel m : models)
            m.setSelected(false);
    }

    private void itemMove(int from, int to) {
        if (from > getItemCount() || to > getItemCount())
            return;

        filtered_data.add(to,filtered_data.remove(from));
        models.add(to,models.remove(from));

        notifyItemMoved(from,to);
        notifyItemChanged(to);
        notifyItemChanged(from);
    }

    public void refresh(Block.Plain block) {
        int posInsert = 0;
        for (Block.Plain b : this.filtered_data) {
            if (b.id.equals(block.id)) {
                int pos = filtered_data.indexOf(b);
                data.remove(b);
                filtered_data.remove(b);
                models.remove(pos);
                data.add(block);
                filtered_data.add(pos,block);
                models.add(pos,new BlockItemListBindingModel(context.get(),this, block, defaultImage));
                notifyItemChanged(pos);
                return;
            }
/*            if (record.mainVariant.primaryShop.price > record.mainVariant.primaryShop.price)
                posInsert++;*/
        }
        data.add(block);
        filtered_data.add(posInsert, block);
        models.add(posInsert,new BlockItemListBindingModel(context.get(),this, block, defaultImage));
        notifyItemInserted(posInsert);
    }

}
