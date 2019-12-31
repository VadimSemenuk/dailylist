package com.mamindeveloper.dailylist;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;

public class ColorPickerFragment extends Fragment {
    View view;
    LinearLayout colorsListView;
    AppCompatButton selectedColorButton;

    ArrayList<ColorPickerItem> colors;
    ColorPickerItem selectedColor;

    public ColorPickerFragment() {
    }

    public static ColorPickerFragment newInstance(String param1, String param2) {
        ColorPickerFragment fragment = new ColorPickerFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_color_picker, container, false);

        colorsListView = view.findViewById(R.id.colors_list);

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public void setColors(ArrayList<ColorPickerItem> _colors) {
        colors = _colors;

        LayoutInflater ltInflater = getLayoutInflater();

        for (int i = 0; i < colors.size(); i++) {
            View colorView = ltInflater.inflate(R.layout.color_picker_item, colorsListView, false);

            final ColorPickerItem color = colors.get(i);

            AppCompatButton colorButton = colorView.findViewById(R.id.color);
            colorButton.getBackground().setColorFilter(Color.parseColor(color.value), PorterDuff.Mode.SRC_IN);

            String contrastColor = getContrastColor(color.value);
            colorButton.setTextColor(Color.parseColor(contrastColor));

            colorButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectColorItem(color, v);
                }
            });

            colorsListView.addView(colorView);
        }
    }

    public void selectColorItem(ColorPickerItem color, View colorView) {
        if (selectedColorButton != null) {
            selectedColorButton.setText(null);
        }
        selectedColor = color;
        selectedColorButton = (AppCompatButton) colorView;

        ((AppCompatButton) colorView.findViewById(R.id.color)).setText(Html.fromHtml("&#x2713;"));
    }

    private String getContrastColor(String color) {
        int r = Integer.valueOf(color.substring(1, 3), 16);
        int g = Integer.valueOf(color.substring(3, 5), 16);
        int b = Integer.valueOf(color.substring(5, 7), 16);
        int threshold = 200;

        if ((r * 0.299 + g * 0.587 + b * 0.114) > threshold) {
            return "#000000";
        } else {
            return "#ffffff";
        }
    }

    public ColorPickerItem getSelectedColor() {
        return selectedColor;
    }
}
