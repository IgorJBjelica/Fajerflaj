package igor.firefly;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.List;

public class MenuFragment extends Fragment {
    private EventsHelper db = new EventsHelper(getContext());

    public MenuFragment() {
        // Required empty public constructor
    }

    private String getOrgan (int id){
        List<User> organ = db.searchUsers("", id);

        for (User u : organ) {
            if (u.getId() == id) return u.getName();
        }
        return "Greska pri ucitavanju organizatora.";
    }

    private String getTag (int id){
        List<Tag> tag = db.searchTags("", id);

        for (Tag t : tag) {
            if (t.getId() == id) return t.getName();
        }
        return "Greska pri ucitavanju tag-a.";
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_menu, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle bundle = this.getActivity().getIntent().getExtras();
        Event event = bundle.getParcelable("event");

        if (event != null) {
            TextView tvName = (TextView) view.findViewById(R.id.textViewName);
            tvName.setText(event.getName());

            TextView tvAdd = (TextView) view.findViewById(R.id.textViewAddress);
            tvAdd.setText(event.getAddress());

            TextView tvDesc = (TextView) view.findViewById(R.id.textViewDescription);
            tvDesc.setText(event.getDescription());

            TextView tvPrice = (TextView) view.findViewById(R.id.textViewPrice);
            String p = "" + event.getPrice();
            tvPrice.setText(p);

            TextView tvOrgan = (TextView) view.findViewById(R.id.textViewOrgan);
            tvOrgan.setText(getOrgan(event.getOrgan()));

            TextView tvType = (TextView) view.findViewById(R.id.textViewType);
            tvType.setText(getTag(event.getTag()));

            RatingBar rb = (RatingBar) view.findViewById(R.id.ratingBar);
            rb.setRating(event.getPopularity());
        }
    }
}
