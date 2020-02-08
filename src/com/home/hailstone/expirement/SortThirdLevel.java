package com.home.hailstone.expirement;

public class SortThirdLevel {
    public void someOldCode() {
//        List<Item> thirdLevel = calculateLevel(3, limit);
//            println("thirdLevel", thirdLevel.stream().map(Item::getValue).collect(toList()));
//
//            List<Integer> firstIndexes = toIndexList(thirdLevel);
//            println("first indexes" + firstIndexes);
//
//            List<Integer> secondIndexes = thirdLevel.stream()
//                    .map(Item::getSecondIndex)
//                    .collect(toList());
//            println("second indexes" + secondIndexes);
//
//            List<Integer> secondSortedIndexes = thirdLevel.stream()
//                    .map(it -> it.getSortedIndex(0))
//                    .collect(toList());
//            println("sorted indexes from second level" + secondSortedIndexes);
//
//            List<Integer> thirdIndexes = thirdLevel.stream()
//                    .map(it -> it.getIndex(2))
//                    .collect(toList());
//            println("third indexes" + thirdIndexes);
//
//            Map<Integer, List<Integer>> groupOfSecondSortedIndexes = groupByValues(secondSortedIndexes);
//            println("Group of secondSortedIndexes:" + groupOfSecondSortedIndexes);
//            FunctionAnalyzer analyzer = new FunctionAnalyzer();
//
//            for (int v = 0; v < 10; v++) {
//                println("T = 18, v = " + v + ", " + analyzer.analyze(groupOfSecondSortedIndexes.get(v), 18));
//            }
//
//            Map<Integer, List<Integer>> groupOfThirdIndexes = groupByValues(thirdIndexes);
//            println(analyzer.analyze(groupOfThirdIndexes.get(0), 18)); // doesn't work :(
//
//            {
//                List<Item> grouped = thirdLevel.stream()
//                        .sorted((it1, it2) -> {
//                            int compareFirstIndex = Integer.compare(it1.getFirstIndex(), it2.getFirstIndex());
//                            if (compareFirstIndex == 0) {
//                                int compareSecondIndex = Integer.compare(it1.getSecondIndex(), it2.getSecondIndex());
//                                if (compareSecondIndex == 0) {
//                                    return Integer.compare(it1.getIndex(2), it2.getIndex(2));
//                                }
//                                return compareSecondIndex;
//                            }
//                            return compareFirstIndex;
//                        })
//                        .collect(toList());
//                println(grouped.stream().map(Item::getFirstIndex).collect(toList()));
//                println(grouped.stream().map(Item::getSecondIndex).collect(toList()));
//                println(grouped.stream().map(it -> it.getIndex(2)).collect(toList()));
//                println(grouped.stream().map(it -> it.getSortedIndex(1)).collect(toList()));
//            }

//            {
//                List<Integer> data = groupOfFirstIndexes.get(0);
//                for (int T = 1000; T <= 1128; T++) {
//                    List<Function> functions = analyzer.analyze(data, T);
//                    println("first index, value = " + T + ": " + functions);
//                }
//
//            }

//            functionsByValues = new ArrayList<>();
//            for (int v = 0; v < groupOfFirstIndexes.size(); v++) {
//                List<Integer> data = groupOfFirstIndexes.get(v);
//                if (data.size() < 18) {
//                    continue;
//                }
//                List<Function> functions = analyzer.analyze(data, 18);
//                functionsByValues.add(functions);
//                println("first index, value = " + v + ": " + functions);
//            }
    }
}
