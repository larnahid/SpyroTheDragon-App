package dam.pmdm.spyrothedragon.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import dam.pmdm.spyrothedragon.R;
import dam.pmdm.spyrothedragon.models.Collectible;
import dam.pmdm.spyrothedragon.VideoActivity;

public class CollectiblesAdapter extends RecyclerView.Adapter<CollectiblesAdapter.CollectiblesViewHolder> {

    private List<Collectible> list;
    private int gemClickCount = 0;
    private static final int CLICK_THRESHOLD = 4;
    private static final long CLICK_RESET_TIME = 2000;

    public CollectiblesAdapter(List<Collectible> collectibleList) {
        this.list = collectibleList;
    }

    @Override
    public CollectiblesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview, parent, false);
        return new CollectiblesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CollectiblesViewHolder holder, int position) {
        Collectible collectible = list.get(position);
        holder.nameTextView.setText(collectible.getName());

        // Cargar la imagen
        int imageResId = holder.itemView.getContext().getResources().getIdentifier(collectible.getImage(), "drawable", holder.itemView.getContext().getPackageName());
        holder.imageImageView.setImageResource(imageResId);

        if (collectible.getName().equalsIgnoreCase("Gemas")) {
            holder.itemView.setOnClickListener(v -> {
                gemClickCount++;

                if (gemClickCount == CLICK_THRESHOLD) {

                    gemClickCount = 0;


                    Context context = v.getContext();
                    Intent intent = new Intent(context, VideoActivity.class);
                    context.startActivity(intent);
                }


                new Handler().postDelayed(() -> gemClickCount = 0, CLICK_RESET_TIME);
            });
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class CollectiblesViewHolder extends RecyclerView.ViewHolder {

        TextView nameTextView;
        ImageView imageImageView;

        public CollectiblesViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.name);
            imageImageView = itemView.findViewById(R.id.image);
        }
    }
}
