package com.bonmanager.ui.dashboard;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.bonmanager.R;
import com.bonmanager.Receipt;
import com.bonmanager.databinding.FragmentDashboardBinding;
import com.bonmanager.ui.home.HomeFragment;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TimeZone;

/**
 * DashboardFragment class
 * Used for statistics
 */
public class DashboardFragment extends Fragment {

    private DashboardViewModel dashboardViewModel;
    private FragmentDashboardBinding binding;
    private PieChart pieChart;
    private Pair<Long, Long> timeRange;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel = new ViewModelProvider(this).get(DashboardViewModel.class);

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        pieChart = root.findViewById(R.id.activity_main_piechart);
        setupPieChart();
        loadPieChartData();

        Button filters_btn = root.findViewById(R.id.filters_button);
        Button date_range_btn = root.findViewById(R.id.date_range_button);

        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        calendar.clear();

        long today = MaterialDatePicker.todayInUtcMilliseconds();
        calendar.setTimeInMillis(today);

        CalendarConstraints.Builder constraintBuilder = new CalendarConstraints.Builder();

        //MaterialDatePicker
        final MaterialDatePicker<Pair<Long,Long>> dataPicker = MaterialDatePicker.Builder.dateRangePicker()
                .setTitleText("Select dates")
                .setSelection(new Pair<>(
                        MaterialDatePicker.todayInUtcMilliseconds(),
                        MaterialDatePicker.todayInUtcMilliseconds()))
                .setCalendarConstraints(constraintBuilder.build())
                .build();

        dataPicker.addOnPositiveButtonClickListener(selection -> {
            timeRange = selection;
            System.out.println(selection);
        });

        date_range_btn.setOnClickListener(v -> {
            dataPicker.show(getChildFragmentManager(), "DATE_PICKER");
        });

        filters_btn.setOnClickListener((view) -> {

        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    /**
     * initialize pie chart
     */
    private void setupPieChart() {
        pieChart.setDrawHoleEnabled(true);
        pieChart.setUsePercentValues(true);
        pieChart.setEntryLabelTextSize(12);
        pieChart.setEntryLabelColor(Color.BLACK);
        //pieChart.setCenterText("Expenses");
        pieChart.setCenterTextSize(24f);
        pieChart.getDescription().setEnabled(false);

        Legend leg = pieChart.getLegend();
        leg.setTextSize(14f);
        leg.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        leg.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        leg.setOrientation(Legend.LegendOrientation.VERTICAL);
        leg.setDrawInside(false);
        leg.setEnabled(true);
    }

    /**
     * load pie chart data
     */
    @SuppressLint("DefaultLocale")
    private void loadPieChartData() {
        ArrayList<PieEntry> entries = new ArrayList<PieEntry>();

        final Set<String> carne = new HashSet<String>(Arrays.asList(
                "pui",
                "porc",
                "vita",
                "miel",
                "oaie",
                "cal",
                "pasare",
                "curcan",
                "ton",
                "peste",
                "pastrav",
                "mici",
                "carnati",
                "friptura",
                "ceafa",
                "piept"));

        final Set<String> legume = new HashSet<String>(Arrays.asList(
                "spanac",
                "morcov",
                "rosii",
                "rosie",
                "ardei",
                "telina",
                "varza",
                "brocoli",
                "conopida",
                "patrunjel",
                "castravete",
                "mazare",
                "fasole",
                "vanata",
                "vinete",
                "telina",
                "usturoi",
                "praz",
                "ridichie",
                "ceapa",
                "sfecla"));

        final Set<String> sucuri = new HashSet<String>(Arrays.asList(
                "pepsi",
                "cola",
                "fanta",
                "sprite",
                "suc",
                "juice",
                "redbull",
                "monster",
                "energizant",
                "mountain dew",
                "seven up",
                "7up",
                "7 up",
                "nestea",
                "frutti",
                "fuzetea",
                "tea",
                "tedi",
                "ciao",
                "giusto",
                "santal",
                "figo",
                "prigat",
                "coke",
                "bere",
                "apa"));

        final Set<String> lactate = new HashSet<String>(Arrays.asList(
                "cascaval",
                "lapte",
                "branza",
                "urda",
                "smantana",
                "sana",
                "chefir",
                "zer",
                "iaurt",
                "cas",
                "mozzarella",
                "unt"));

        final Set<String> paine = new HashSet<String>(Arrays.asList(
                "paine",
                "bagheta",
                "lipie",
                "chifla",
                "painica"));

        final Set<String> combustibil = new HashSet<String>(Arrays.asList(
                "benzina",
                "motorina",
                "gpl"));

        float totalAll = 0.0f;
        float totalCarne = 0.0f;
        float totalLegume = 0.0f;
        float totalSucuri = 0.0f;
        float totalLactate = 0.0f;
        float totalPaine = 0.0f;
        float totalCombustibil = 0.0f;
        float totalOthers = 0.0f;
        for (Receipt bon: HomeFragment.getReceipts()) {
            totalAll += Float.parseFloat(bon.getTotalWithoutCurrency());
            int cnt = 0;
            for (String produs: bon.getProduse()) {
                StringTokenizer defaultTokenizer = new StringTokenizer(produs);
                boolean ok = false;
                while (defaultTokenizer.hasMoreTokens()) {
                    String word = defaultTokenizer.nextToken().toLowerCase();
                    if (carne.contains(word)) {
                        totalCarne += Float.parseFloat(bon.getPreturi()[cnt]);
                        ok = true;
                        break;
                    } else if (legume.contains(word)) {
                        totalLegume += Float.parseFloat(bon.getPreturi()[cnt]);
                        ok = true;
                        break;
                    } else if (sucuri.contains(word)) {
                        totalSucuri += Float.parseFloat(bon.getPreturi()[cnt]);
                        ok = true;
                        break;
                    } else if (lactate.contains(word)) {
                        totalLactate += Float.parseFloat(bon.getPreturi()[cnt]);
                        ok = true;
                        break;
                    } else if (paine.contains(word)) {
                        totalPaine += Float.parseFloat(bon.getPreturi()[cnt]);
                        ok = true;
                        break;
                    } else if (combustibil.contains(word)) {
                        totalCombustibil += Float.parseFloat(bon.getPreturi()[cnt]);
                        ok = true;
                        break;
                    }
                }
                if (!ok) {
                    totalOthers += Float.parseFloat(bon.getPreturi()[cnt]);
                }
                ++cnt;
            }
        }

        if (totalAll == 0.0f) {
            return;
        }
        if (totalCarne != 0.0f) {
            entries.add(new PieEntry(totalCarne, "Meat"));
        }
        if (totalLegume != 0.0f) {
            entries.add(new PieEntry(totalLegume, "Vegetables"));
        }
        if (totalSucuri != 0.0f) {
            entries.add(new PieEntry(totalSucuri, "Beverages"));
        }
        if (totalLactate != 0.0f) {
            entries.add(new PieEntry(totalLactate, "Dairy products"));
        }
        if (totalPaine != 0.0f) {
            entries.add(new PieEntry(totalPaine, "Breads"));
        }
        if (totalCombustibil != 0.0f) {
            entries.add(new PieEntry(totalCombustibil, "Gas"));
        }
        if (totalOthers != 0.0f) {
            entries.add(new PieEntry(totalOthers, "Others"));
        }

        pieChart.setCenterText(String.format("%.2f", totalAll) + " RON");

        ArrayList<Integer> colors = new ArrayList<>();
        for (int color: ColorTemplate.MATERIAL_COLORS) {
            colors.add(color);
        }
        for (int color: ColorTemplate.VORDIPLOM_COLORS) {
            colors.add(color);
        }

        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setColors(colors);

        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setDrawValues(true);
        data.setValueFormatter(new PercentFormatter(pieChart));
        data.setValueTextSize(16f);
        data.setValueTextColor(Color.BLACK);

        pieChart.setData(data);
        pieChart.invalidate();

        pieChart.animateY(1400, Easing.EaseInOutQuad);
    }
}