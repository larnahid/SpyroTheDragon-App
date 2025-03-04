package dam.pmdm.spyrothedragon.adapters;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import dam.pmdm.spyrothedragon.R;
import dam.pmdm.spyrothedragon.models.Character;
import dam.pmdm.spyrothedragon.ui.FireAnimationView;

import java.util.List;

public class CharactersAdapter extends RecyclerView.Adapter<CharactersAdapter.CharactersViewHolder> {

    private List<Character> list;
    private FireAnimationView fireAnimationView;

    public CharactersAdapter(List<Character> charactersList, FireAnimationView fireView) {
        this.list = charactersList;
        this.fireAnimationView = fireView;
    }

    @Override
    public CharactersViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview, parent, false);
        return new CharactersViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CharactersViewHolder holder, int position) {
        Character character = list.get(position);
        holder.nameTextView.setText(character.getName());

        int imageResId = holder.itemView.getContext().getResources()
                .getIdentifier(character.getImage(), "drawable", holder.itemView.getContext().getPackageName());
        holder.imageImageView.setImageResource(imageResId);


        if ("Spyro".equals(character.getName())) {
            holder.itemView.setOnLongClickListener(v -> {
                if (fireAnimationView != null) {

                    fireAnimationView.setVisibility(View.VISIBLE);
                    fireAnimationView.setTranslationX(holder.imageImageView.getX() - holder.imageImageView.getWidth()/3);
                    fireAnimationView.setTranslationY(holder.imageImageView.getY() - holder.imageImageView.getHeight()*2);

                    fireAnimationView.setRotation(90);

                    fireAnimationView.startFireAnimation();
                    new Handler().postDelayed(() -> fireAnimationView.setVisibility(View.GONE), 1500);
                }
                return true;
            });
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class CharactersViewHolder extends RecyclerView.ViewHolder {

        TextView nameTextView;
        ImageView imageImageView;

        public CharactersViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.name);
            imageImageView = itemView.findViewById(R.id.image);
        }
    }
}
