package eu.nikolaykopa.guesspic.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import eu.nikolaykopa.guesspic.activity.ChoiceActivity;
import eu.nikolaykopa.guesspic.activity.GameOverActivity;
import eu.nikolaykopa.guesspic.activity.Loading;
import eu.nikolaykopa.guesspic.model.Category;
import eu.nikolaykopa.guesspic.interf.ItemClickListener;
import eu.nikolaykopa.guesspic.R;
import eu.nikolaykopa.guesspic.activity.LivesGameActivity;

/**
 * MainAdapter.
 */

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {

    private List<Category> categoriesList;
    private Context context;

    public MainAdapter(List<Category> categoriesList, Context context) {
        this.categoriesList = categoriesList;
        this.context = context;
    }

    @Override
    public MainAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MainAdapter.ViewHolder holder, int position) {
        holder.bind(categoriesList.get(position));

        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                switch (position) {
                    case 0:
                        select("animals_L", "animals_T");
                        break;
                    case 1:
                        select("dogs_L", "dogs_T");
                        break;
                    case 2:
                        select("attractions_L", "attractions_T");
                        break;
                    case 3:
                        select("flags_L", "flags_T");
                        break;
                    case 4:
                        select("weapons_L", "weapons_T");
                        break;
                }
            }
        });
    }

    private boolean isNetworkConnected() {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();

        return networkInfo != null && networkInfo.isConnected();
    }

    public void select(final String catNameLives, final String catNameTime) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.alert_dialog, null);

        builder.setView(dialogView);

        final AlertDialog dialog = builder.create();

        LinearLayout gLivesBtn = (LinearLayout) dialogView.findViewById(R.id.gLivesBtn);
        LinearLayout gTimeBtn = (LinearLayout) dialogView.findViewById(R.id.gTimeBtn);

        gLivesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
                if (isNetworkConnected()) {
                    Intent gameForLives = new Intent(context, Loading.class);
                    gameForLives.putExtra("chosenGame", catNameLives);
                    context.startActivity(gameForLives);
                } else {
                    Toast.makeText(context, context.getResources().getString(R.string.internet_access), Toast.LENGTH_SHORT).show();
                }
            }
        });

        gTimeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();

                if (isNetworkConnected()) {
                    Intent gameForTime = new Intent(context, Loading.class);
                    gameForTime.putExtra("chosenGame", catNameTime);
                    context.startActivity(gameForTime);
                } else {
                    Toast.makeText(context, context.getResources().getString(R.string.internet_access), Toast.LENGTH_SHORT).show();
                }
            }
        });

        dialog.show();
    }

    @Override
    public int getItemCount() {
        return categoriesList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView image;
        private TextView name;
        private TextView scoreLives;
        private TextView scoreTime;
        private ItemClickListener itemClickListener;

        public ViewHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.category_image);
            name = (TextView) itemView.findViewById(R.id.category_name);
            scoreLives = (TextView) itemView.findViewById(R.id.category_score_lives);
            scoreTime = (TextView) itemView.findViewById(R.id.category_score_time);

            itemView.setOnClickListener(this);
        }

        public void setItemClickListener(ItemClickListener itemClickListener) {
            this.itemClickListener = itemClickListener;
        }

        public void bind(Category category) {
            image.setImageResource(category.getImageId());
            name.setText(category.getName());
            scoreLives.setText(String.valueOf(category.getScoreLives()));
            scoreTime.setText(String.valueOf(category.getScoreTime()));
        }

        @Override
        public void onClick(View v) {
            itemClickListener.onClick(v, getAdapterPosition(), false);
        }
    }
}
