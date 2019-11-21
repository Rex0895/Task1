//import com.tsc.Department;
//import com.tsc.Reader;
//
//import java.io.IOException;
//import java.util.*;
//
//public class Test {
//
//    private static List<List<String>> powerSet(List<String> listA,List<String>listB, int index) {
//        if (index == listA.size()) {
//            return new ArrayList<>();
//        }
//        double avg1=calcAvgSalary(listA,listA.size());
//        double avg2=calcAvgSalary(listB,listB.size());
//
//        int val = listA.get(index);
//        List<List<Integer>> subset = powerSet(listA, listB,index + 1);
//        List<List<Integer>> returnList = new ArrayList<>();
//        returnList.add(Arrays.asList(val));
//        returnList.addAll(subset);
////        for (List<Integer> subsetValues : subset) {
////            for (Integer subsetValue : subsetValues) {
////                returnList.add(Arrays.asList(val,subsetValue));
////            }
////        }
//        return returnList;
//    }
//    private static double calcAvgSalary(List<String> list,int divisor) {
//        double avg = 0;
//        for (String emp : list) {
//            avg +=emp;
//        }
//        avg = avg/divisor;
//        return avg;
//    }
//
//    public static void main(String[] args) {
//        List<Integer>listA=new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5));
//        List<Integer>listB=new ArrayList<>(Arrays.asList(6, 7, 8, 9, 10));
//        List<List<String>> allSubsets = powerSet(listA,listB, 0);
//
//    }
//}
//public class Powerset {
//
//    public static void main(String[] args) {
//        final List<List<String>> allSubsets = powerSet(Arrays.asList(1, 2, 3, 4), 0);
//        for (List<String> subsets : allSubsets) {
//            System.out.println(subsets);
//        }
//    }
//
//    private static List<List<String>> powerSet(final List<Integer> values,
//                                               int index) {
//        if (index == values.size()) {
//            return new ArrayList<>();
//        }
//        int val = values.get(index);
//        List<List<String>> subset = powerSet(values, index + 1);
//        List<List<String>> returnList = new ArrayList<>();
//        returnList.add(Arrays.asList(String.valueOf(val)));
//        returnList.addAll(subset);
//        for (final List<String> subsetValues : subset) {
//            for (final String subsetValue : subsetValues) {
//                returnList.add(Arrays.asList(val + "," + subsetValue));
//            }
//        }
//        return returnList;
//    }
//}