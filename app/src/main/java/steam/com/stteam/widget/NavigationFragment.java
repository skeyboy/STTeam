package steam.com.stteam.widget;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import steam.com.stteam.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class NavigationFragment extends Fragment {
    LinearLayout rightLayout, leftLayout;
    TextView centerTitleView;
    Button defaultBackBtn;

    public NavigationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View convertView =
                inflater.inflate(R.layout.fragment_navigation, container, false);
        leftLayout = (LinearLayout) convertView.findViewById(R.id.navigation_left_layout);
        rightLayout = (LinearLayout) convertView.findViewById(R.id.navigation_right_layout);

        defaultBackBtn = (Button) leftLayout.findViewById(R.id.navigation_default_back);
        centerTitleView = (TextView) convertView.findViewById(R.id.navigation_center_title_view);
        initView();
        return convertView;
    }

    public void setNavigationTitle(String title) {
        centerTitleView.setVisibility(View.VISIBLE);
        centerTitleView.setText(title);
    }

    public void setNavigationTitleColor(int color) {
        centerTitleView.setTextColor(color);
    }

    private void initView() {
        setDefaultLeftFirstBarListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Activity activity = (Activity) getContext();
                activity.finish();
            }
        });
    }

    private void setDefaultLeftFirstBarListener(View.OnClickListener listener) {
        defaultBackBtn.setOnClickListener(listener);
    }

    public void addLeftBarItem(String title, View.OnClickListener clickListener) {
        Button button = new Button(getContext());
        button.setText(title);
        button.setOnClickListener(clickListener);
        addLeftBarItem(button);
    }

    public void addLeftBarItem(View view) {
        if (view == null) {
            return;
        }
        view.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));
        leftLayout.addView(view, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }

    public void setOnLeftBackItemListener(View.OnClickListener listener) {
        if (defaultBackBtn != null) {
            defaultBackBtn.setOnClickListener(listener);
        }
    }
}
