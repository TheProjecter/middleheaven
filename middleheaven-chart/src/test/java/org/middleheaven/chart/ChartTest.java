package org.middleheaven.chart;

import static org.junit.Assert.assertTrue;

import javax.swing.JFrame;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.junit.Test;
import org.middleheaven.chart.jfreechart.JFreeChartEngine;
import org.middleheaven.quantity.time.CalendarDateTime;
import org.middleheaven.quantity.time.Period;
import org.middleheaven.quantity.time.clocks.IntervalSchedule;
import org.middleheaven.tool.test.MiddleHeavenTestCase;
import org.middleheaven.work.Work;
import org.middleheaven.work.WorkContext;
import org.middleheaven.work.scheduled.AlarmClockScheduleWorkExecutionService;
import org.middleheaven.work.scheduled.ScheduleWorkExecutorService;


public class ChartTest extends MiddleHeavenTestCase{


	public static void main(String ... args){
		ChartTest r = new ChartTest();
		//r.testBidimensionalChart();
		r.testDynamicChart();
	}

	@Test
	public void testDynamicChart(){
		ChartEngine engine = new JFreeChartEngine();

		final BidimensionalSeries<Integer, Integer> hs = new BidimensionalSeries<Integer, Integer>("World Population (per year)");
		hs.add(1900, 1650);
		hs.add(1950, 2521);
		hs.add(1999, 5978);

		ChartLayout layout = ChartLayout.layout(ChartType.XY , "World Population (per year)");

		Chart chart = engine.createChart(hs, layout);

		assertTrue (chart.getChartObject()!=null);

		// show
		show(chart);

		ScheduleWorkExecutorService service = new AlarmClockScheduleWorkExecutionService();

		Work work = new Work(){
			int year = 2000;
			@Override
			public void execute(WorkContext context) {
				year+=50;
				hs.add(year, (int)(5978 + (Math.random() * 1000 - 500)));

			}

		};
		
		service.execute( work , IntervalSchedule.schedule(CalendarDateTime.now(), Period.miliseconds(500)));

		/*
		Timer timer = new Timer();
		timer.schedule(new TimerTask(){

			@Override
			public void run() {
				year+=50;
				hs.add(year, (int)(5978 + (Math.random() * 1000 - 500)));

			}

		}, 1000, 500);
		*/


	}
	@Test
	public void testBidimensionalChart(){

		ChartEngine engine = new JFreeChartEngine();


		BidimensionalSeries<Integer, Integer> hs = new BidimensionalSeries<Integer, Integer>("World Population (per year)");
		hs.add(1900, 1650);
		hs.add(1950, 2521);
		hs.add(1999, 5978);

		ChartLayout layout = ChartLayout.layout(ChartType.PIE , "World Population (per year)");

		Chart chart = engine.createChart(hs, layout);

		assertTrue (chart.getChartObject()!=null);

		// show
		show(chart);

		layout = ChartLayout.layout(ChartType.BARS , "World Population (per year)");

		chart = engine.createChart(hs, layout);

		assertTrue (chart.getChartObject()!=null);

		// show
		show(chart);

		layout = ChartLayout.layout(ChartType.XY , "World Population (per year)");

		chart = engine.createChart(hs, layout);

		assertTrue (chart.getChartObject()!=null);

		// show
		show(chart);
	}

	private static void show(final Chart chart){
		ChartTestFrame.forChart(chart).setVisible(true);
	}

	private static class ChartTestFrame extends JFrame{

		public static ChartTestFrame forChart(Chart chart){
			return new ChartTestFrame(chart);
		}

		public ChartTestFrame(Chart chart){
			this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			final ChartPanel chartPanel = new ChartPanel((JFreeChart)chart.getChartObject());
			chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
			setContentPane(chartPanel);
			this.pack();
		}
	}
}
