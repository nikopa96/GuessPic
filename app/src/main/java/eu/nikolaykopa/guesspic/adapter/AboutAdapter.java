package eu.nikolaykopa.guesspic.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import eu.nikolaykopa.guesspic.R;
import eu.nikolaykopa.guesspic.model.AboutCard;

public class AboutAdapter extends RecyclerView.Adapter<AboutAdapter.ViewHolder> {

    private List<AboutCard> aboutCardList;

    public AboutAdapter(List<AboutCard> aboutCardList) {
        this.aboutCardList = aboutCardList;
    }

    @Override
    public AboutAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.about_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AboutAdapter.ViewHolder holder, int position) {
        holder.bind(aboutCardList.get(position));
    }

    @Override
    public int getItemCount() {
        return aboutCardList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView aboutTitle;
        private TextView aboutLicense;
        private TextView aboutText;

        public ViewHolder(View view) {
            super(view);
            aboutTitle = (TextView) view.findViewById(R.id.about_title);
            aboutLicense = (TextView) view.findViewById(R.id.about_license);
            aboutText = (TextView) view.findViewById(R.id.about_text);
        }

        public void bind(AboutCard aboutCard) {
            aboutTitle.setText(aboutCard.getAboutTitle());
            aboutLicense.setText(aboutCard.getAboutLicense());
            aboutText.setText(aboutCard.getAboutText());
        }
    }
}
