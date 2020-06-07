package com.home.hailstone.expirement;

import com.home.hailstone.math.FunctionAnalyzer;
import com.home.hailstone.math.PalindromeFunction;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.knowm.xchart.QuickChart;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYSeries;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.function.Function;
import java.util.stream.IntStream;

import static com.home.hailstone.math.BigIntegerUtil.*;
import static java.math.BigInteger.*;
import static java.util.stream.Collectors.toList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BaseResearch {
    private static final Logger log = LogManager.getLogger(BaseResearch.class);
    private static Random generator;
    private static Map<Integer, Integer> g;
    private static Map<Integer, Integer> G;

    private static final int[][] I = new int[][]{
            {2, 3, 4, 1, 2, 1},
            {1, 2, 3, 0, 3, 2}
    };

    @BeforeAll
    static void init() {
        generator = new Random();
        g = new HashMap<>();
        g.put(0, 2);
        G = new HashMap<>();
        G.put(0, 1);
    }

    @Test
    public void testUnchangedRestOfParentsWhenShiftOnPeriods() {
        int n = generateInt(1, 10);
        log.info("n = {}", n);

        log.info("size of periods:");
        IntStream.range(0, n)
                .forEach(i ->
                        log.info("{}) {}", i + 1, calculateSizeOfPeriod(n, i))
                );

        List<Integer> baseCoordinates = randomBaseCoordinates(n);
        log.info("base coordinates: {}", baseCoordinates);

        LinkedList<BigInteger> basePathToOne = stepBackFromOne(baseCoordinates);
        log.info("base path to one:");
        logCollection(basePathToOne);

        List<Integer> amountsOfPeriods = IntStream.generate(() -> generateInt(0, 3))
                .limit(n)
                .boxed()
                .collect(toList());
        log.info("amounts of periods: {}", amountsOfPeriods);

        List<Integer> shiftedCoordinates = new ArrayList<>(n);
        for (int i = 0; i < n; i++) {
            Integer baseCoordinate = baseCoordinates.get(i);
            Integer amountOfPeriod = amountsOfPeriods.get(i);
            shiftedCoordinates.add(baseCoordinate + amountOfPeriod * calculateSizeOfPeriod(n, i));
        }
        log.info("shifted coordinates: {}", shiftedCoordinates);

        LinkedList<BigInteger> shiftedPathToOne = stepBackFromOne(shiftedCoordinates);
        log.info("shifted path to one:");
        logCollection(shiftedPathToOne);

        log.info("checking of same parent's rest:");
        Function<BigInteger, BigInteger> calculateParentRest = x -> R(x).mod(valueOf(6));
        for (int i = 0; i < n; i++) {
            BigInteger base = basePathToOne.get(i);
            BigInteger shifted = shiftedPathToOne.get(i);

            BigInteger baseParentRest = calculateParentRest.apply(base);
            BigInteger shiftedParentRest = calculateParentRest.apply(shifted);

            log.info("checking {} == {}", baseParentRest, shiftedParentRest);
            assertEquals(baseParentRest, shiftedParentRest);
        }
    }

    @Test
    public void testDecompositionOnBaseNodes() {
        int n = generateInt(1, 10);
        log.info("n = {}", n);

        log.info("size of periods:");
        IntStream.range(0, n)
                .forEach(i ->
                        log.info("{}) {}", i + 1, calculateSizeOfPeriod(n, i))
                );

        List<Integer> baseCoordinates = randomBaseCoordinates(n);
        log.info("base coordinates: {}", baseCoordinates);

        LinkedList<BigInteger> basePathToOne = stepBackFromOne(baseCoordinates);
        log.info("base path to one:");
        logCollection(basePathToOne);

        List<Integer> amountsOfPeriods = IntStream.generate(() -> generateInt(0, 3))
                .limit(n)
                .boxed()
                .collect(toList());
        log.info("amounts of periods: {}", amountsOfPeriods);

        List<Integer> shiftedCoordinates = new ArrayList<>(n);
        for (int i = 0; i < n; i++) {
            Integer baseCoordinate = baseCoordinates.get(i);
            Integer amountOfPeriod = amountsOfPeriods.get(i);
            shiftedCoordinates.add(baseCoordinate + amountOfPeriod * calculateSizeOfPeriod(n, i));
        }
        log.info("shifted coordinates: {}", shiftedCoordinates);

        LinkedList<BigInteger> shiftedPathToOne = stepBackFromOne(shiftedCoordinates);
        log.info("shifted path to one:");
        logCollection(shiftedPathToOne);

        BigInteger actualLastNode = basePathToOne.getLast();
        for (int l = 1; l <= n; l++) {
            int sumOfBaseI = 0;
            for (int k = l; k <= n; k++) {
                sumOfBaseI += i(basePathToOne.get(k - 1), baseCoordinates.get(k - 1));
            }
            assertTrue(sumOfBaseI >= 0);

            int sumOfPeriodsSizes = 0;
            for (int k = l + 1; k <= n; k++) {
                sumOfPeriodsSizes += calculateSizeOfPeriod(n, k - 1) * amountsOfPeriods.get(k - 1) * 3;
            }
            assertTrue(sumOfPeriodsSizes >= 0);

            BigInteger denominator = THREE.pow(n + 1 - l);
            BigInteger numerator = TWO.pow(3 * amountsOfPeriods.get(l - 1) * calculateSizeOfPeriod(n, l - 1))
                    .subtract(ONE);
            assertEquals(ZERO, numerator.mod(denominator), "should be dividable by pow of three");
            BigInteger divide = numerator.divide(denominator);

            actualLastNode = actualLastNode.add(
                    basePathToOne.get(l - 1)
                            .multiply(TWO.pow(sumOfBaseI))
                            .multiply(TWO.pow(sumOfPeriodsSizes))
                            .multiply(divide)
            );
        }

        log.info("should be equal {} and {}", shiftedPathToOne.getLast(), actualLastNode);
        assertEquals(shiftedPathToOne.getLast(), actualLastNode);
    }

    @Test
    public void testDecompositionOnBaseNodes2() throws InterruptedException, IOException {
//        int n = generateInt(1, 10);
        int n = 3;
        log.info("n = {}", n);

        log.info("size of periods:");
        IntStream.range(0, n)
                .forEach(i ->
                        log.info("{}) {}", i + 1, calculateSizeOfPeriod(n, i))
                );

        int baseSize = G(n);
        List<BigInteger> base = IntStream.range(0, baseSize)
                .boxed()
                .map(l -> {
                    List<Integer> coordinates = calculateCoordinates(n, l);
                    return stepBackFromOne(coordinates).getLast();
                })
                .collect(toList());
        log.info(base);

        class Item {
            int index;
            BigInteger value;

            public Item(int index, BigInteger value) {
                this.index = index;
                this.value = value;
            }

            @Override
            public String toString() {
                return "(" + index + ", " + value + ")";
            }
        }

        List<Item> baseItems = new ArrayList<>(baseSize);
        for (int i = 0; i < baseSize; i++) {
            baseItems.add(new Item(i, base.get(i)));
        }
        log.info("base items: {}", baseItems);

        List<Item> sortedItems = baseItems.stream()
                .sorted(Comparator.comparing(item -> item.value))
                .collect(toList());
        log.info("sorted items: {}", sortedItems);

        List<Integer> sortedIndexes = sortedItems.stream()
                .map(item -> item.index)
                .collect(toList());
        log.info("sorted indexes: {}", sortedIndexes);

        FunctionAnalyzer analyzer = new FunctionAnalyzer();
        log.info(analyzer.analyze("x", sortedIndexes, 36));

//        plot(sortedIndexes);
//        System.in.read();

//        List<Integer> baseCoordinates = randomBaseCoordinates(n);
//        log.info("base coordinates: {}", baseCoordinates);
//
//        LinkedList<BigInteger> basePathToOne = stepBackFromOne(baseCoordinates);
//        log.info("base path to one:");
//        logCollection(basePathToOne);
//
//        List<Integer> amountsOfPeriods = IntStream.generate(() -> generateInt(0, 3))
//                .limit(n)
//                .boxed()
//                .collect(toList());
//        log.info("amounts of periods: {}", amountsOfPeriods);
//
//        List<Integer> shiftedCoordinates = new ArrayList<>(n);
//        for (int i = 0; i < n; i++) {
//            Integer baseCoordinate = baseCoordinates.get(i);
//            Integer amountOfPeriod = amountsOfPeriods.get(i);
//            shiftedCoordinates.add(baseCoordinate + amountOfPeriod * calculateSizeOfPeriod(n, i));
//        }
//        log.info("shifted coordinates: {}", shiftedCoordinates);
//
//        LinkedList<BigInteger> shiftedPathToOne = stepBackFromOne(shiftedCoordinates);
//        log.info("shifted path to one:");
//        logCollection(shiftedPathToOne);
//
//        BigInteger actualLastNode = basePathToOne.getLast();
//        for (int l = 1; l <= n; l++) {
//            int sumOfBaseI = 0;
//            for (int k = l; k <= n; k++) {
//                sumOfBaseI += i(basePathToOne.get(k - 1), baseCoordinates.get(k - 1));
//            }
//            assertTrue(sumOfBaseI >= 0);
//
//            int sumOfPeriodsSizes = 0;
//            for (int k = l + 1; k <= n; k++) {
//                sumOfPeriodsSizes += calculateSizeOfPeriod(n, k - 1) * amountsOfPeriods.get(k - 1) * 3;
//            }
//            assertTrue(sumOfPeriodsSizes >= 0);
//
//            BigInteger denominator = THREE.pow(n + 1 - l);
//            BigInteger numerator = TWO.pow(3 * amountsOfPeriods.get(l - 1) * calculateSizeOfPeriod(n, l - 1))
//                    .subtract(ONE);
//            assertEquals(ZERO, numerator.mod(denominator), "should be dividable by pow of three");
//            BigInteger divide = numerator.divide(denominator);
//
//            actualLastNode = actualLastNode.add(
//                    basePathToOne.get(l - 1)
//                            .multiply(TWO.pow(sumOfBaseI))
//                            .multiply(TWO.pow(sumOfPeriodsSizes))
//                            .multiply(divide)
//            );
//        }
//
//        log.info("should be equal {} and {}", shiftedPathToOne.getLast(), actualLastNode);
//        assertEquals(shiftedPathToOne.getLast(), actualLastNode);
    }

    private void plot(List<Integer> list) {
        List<Integer> x = IntStream.range(0, list.size())
                .boxed()
                .collect(toList());
        XYChart chart = QuickChart.getChart("Sample Chart", "X", "Y", "y(x)", x, list);
        chart.getStyler().setDefaultSeriesRenderStyle(XYSeries.XYSeriesRenderStyle.Scatter);
        chart.getStyler().setMarkerSize(3);

        chart.addSeries("s", x, list);
//        XYSeries series = chart.addSeries("Gaussian Blob 2", getGaussian(1000, 1, 10), getGaussian(1000, 0, 5));
//        series.setMarker(SeriesMarkers.DIAMOND);

        new SwingWrapper(chart).displayChart();

// Customize Chart
//        chart.getStyler().setChartTitleVisible(false);
//        chart.getStyler().setLegendPosition(Styler.LegendPosition.InsideSW);

// Series

//        new SwingWrapper(chart).displayChart();
    }

    private List<Integer> calculateCoordinates(int baseN, int l) {
        return IntStream.rangeClosed(1, baseN)
                .map(n -> calculateCoordinate(baseN, n, l))
                .boxed()
                .collect(toList());
    }

    private int calculateCoordinate(int baseN, int currentN, int l) {
        int i = baseN - currentN;
        return (l / G(i)) % G(i + 1);
    }

    private static int calculateSizeOfPeriod(int n, int i) {
        return g(n - 1 - i);
    }

    private LinkedList<BigInteger> stepBackFromOne(List<Integer> coordinates) {
        LinkedList<BigInteger> pathToOne = new LinkedList<>();
        pathToOne.add(ONE);
        IntStream.range(0, coordinates.size())
                .forEach(i -> {
                    BigInteger parent = pathToOne.getLast();
                    Integer coordinate = coordinates.get(i);
                    BigInteger child = C(parent, coordinate);
                    pathToOne.addLast(child);
                });
        return pathToOne;
    }

    private void logCollection(LinkedList<BigInteger> pathToOne) {
        IntStream.range(0, pathToOne.size())
                .forEach(i ->
                        log.info("{}) {}", i + 1, pathToOne.get(i))
                );
    }

    private List<Integer> randomBaseCoordinates(int n) {
        return IntStream.range(0, n)
                .map(i -> generateInt(0, calculateSizeOfPeriod(n, i)))
                .boxed()
                .collect(toList());
    }

    private int generateInt(int a, int b) {
        assertTrue(b >= a);
        return a + generator.nextInt(b - a);
    }

    private static int g(int i) {
        Integer cacheValue = g.get(i);
        if (cacheValue != null) {
            return cacheValue;
        }
        int calculatedValue = g(i - 1) * 3;
        g.put(i, calculatedValue);
        return calculatedValue;
    }

    private static int G(int i) {
        Integer cacheValue = G.get(i);
        if (cacheValue != null) {
            return cacheValue;
        }
        int calculatedValue = G(i - 1) * g(i - 1);
        G.put(i, calculatedValue);
        return calculatedValue;
    }

    private List<BigInteger> getBigIntegers(int numberOfLevels) {
        int sizeOfSubset = 2 * (int) (Math.pow(3, numberOfLevels - 1));
        List<BigInteger> list = new ArrayList<>(1);
        list.add(ONE);
        for (int level = 1; level <= numberOfLevels; level++) {
            List<BigInteger> collector = new ArrayList<>();
            for (BigInteger value : list) {
                for (int j = 0; j < sizeOfSubset; j++) {
                    collector.add(C(value, j));
                }
            }
            list = collector;
            sizeOfSubset /= 3;
        }
        list.sort(null);
        return list;
    }

    private BigInteger C(BigInteger P, int j) {
        return P.multiply(powOf2(i(P, j)))
                .subtract(ONE)
                .divide(THREE);
    }

    private int i(BigInteger P, int j) {
        return 3 * j + cycle(j, R(P), I);
    }
}
