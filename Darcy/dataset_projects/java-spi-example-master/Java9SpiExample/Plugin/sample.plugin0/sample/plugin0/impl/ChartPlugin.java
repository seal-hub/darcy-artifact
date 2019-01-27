package sample.plugin0.impl;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Tab;
import sample.plugin.Plugin;

/**
 * 饼图插件
 *
 * @author 李佳明 https://github.com/pkpk1234
 * @date 2017-12-2017/12/4
 */
public class ChartPlugin implements Plugin {
	private String name;

	public ChartPlugin() {
		this.name = "ChartPlugin";
	}

	@Override
	public String getPluginName() {
		return this.name;
	}

	@Override
	public Tab getPluginTab() {
		Tab tab = new Tab();
		tab.setText(getPluginName());

		ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(
				new PieChart.Data("Grapefruit", 13), new PieChart.Data("Oranges", 25),
				new PieChart.Data("Plums", 10), new PieChart.Data("Pears", 22),
				new PieChart.Data("Apples", 30));
		final PieChart chart = new PieChart(pieChartData);
		chart.setTitle("Imported Fruits");
		tab.setContent(chart);

		return tab;
	}
}
