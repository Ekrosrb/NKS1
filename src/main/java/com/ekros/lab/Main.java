package com.ekros.lab;

import java.text.DecimalFormat;
import java.util.Arrays;

public class Main {

  private static final double[] VALUES = {
      1325, 977, 243, 3, 145, 997, 27, 67, 30, 934,
      1039, 240, 371, 86, 164, 96, 156, 145, 280,
      444, 887, 726, 41, 503, 174, 1809, 349, 532,
      1541, 148, 489, 198, 4, 761, 389, 37, 317,
      1128, 514, 426, 23, 184, 365, 153, 624, 31,
      49, 1216, 61, 189, 286, 1269, 365, 1085,
      279, 228, 95, 391, 683, 39, 7, 486, 715, 204,
      1553, 736, 1622, 1892, 448, 23, 135, 555,
      252, 569, 8, 491, 724, 331, 1243, 567, 788,
      729, 62, 636, 227, 227, 245, 153, 151, 217,
      1009, 143, 301, 342, 48, 493, 117, 78, 113,
      67};


  private static final double Y = 0.74;
  private static final double TIME1 = 1586;
  private static final double TIME2 = 1798;

//  private static final double[] VALUES = {644, 1216, 2352, 1386, 1280, 903, 607, 2068,
//      4467, 835, 313, 555, 307, 508, 1386, 2895, 583,
//      292, 5159, 1107, 181, 18, 1247, 125, 1452, 4211,
//      890, 659, 1602, 2425, 214, 68, 21, 1762, 1118,
//      45, 1803, 1187, 2154, 19, 1122, 278, 1622, 702,
//      1396, 694, 45, 1739, 3483, 1334, 1852, 96, 173,
//      7443, 901, 2222, 4465, 18, 1968, 1426, 1424,
//      1146, 435, 1390, 246, 578, 281, 455, 609, 854,
//      436, 1762, 444, 466, 1934, 681, 4539, 164, 295,
//      1644, 711, 245, 740, 18, 474, 623, 462, 605, 187,
//      106, 793, 92, 296, 226, 63, 246, 446, 2234, 2491,
//      315};

//  private static final double Y = 0.9;
//  private static final double TIME1 = 2000;
//  private static final double TIME2 = 2000;

  public static void main(String[] args) {

    printArray(VALUES, "Input values:\n");

    double avg = avgT();
    Arrays.sort(VALUES);
    double max = VALUES[VALUES.length - 1];
    double intSize = max / 10.0;

    printValue(avg, "Tcp:", "####.##");
    printArray(VALUES, "Input values:");
    printValue(max, "Max value:", "####.##");
    printValue(intSize, "Interval len:", "####.##");

    double[] intervals = calcInterval(intSize, max);
    double[] staticSolid = staticSolid(intervals, intSize);
    double[] pArray = pt(staticSolid, intervals, intSize);
    int[] scope = getDScope(pArray);
    double Ty = calcTy(pArray, intSize, scope[0], scope[1]);
    double pTime = pTime(staticSolid, intervals, intSize, TIME1);
    double pTimeInt = pTime(staticSolid, intervals, intSize, TIME2);
    int index2 = getIndex(intervals, TIME2, intSize);
    double yTime = yTime(staticSolid[index2], pTimeInt);

    printValue(Ty, "T" + Y + ":", "####.########");
    printValue(pTime, "P(" + TIME1 + "):", "####.########");
    printValue(pTimeInt, "P(" + TIME2 + "):", "####.########");
    printValue(yTime, "Y(" + TIME2 + "):", "####.########");
  }

  private static int[] getDScope(double[] pArray) {
    int[] scope = new int[2];
    for (int i = 0; i < pArray.length - 1; i++) {
      double from = pArray[i];
      double to = pArray[i + 1];
      if (Y <= from && Y > to) {
        scope[0] = i;
        scope[1] = i + 1;
      }
    }
    return scope;
  }

  private static double avgT() {
    int avg = 0;
    for (double i : VALUES) {
      avg += i;
    }
    return (double) avg / VALUES.length;
  }

  private static double[] calcInterval(double intSize, double max) {
    double[] intervals = new double[10];
    DecimalFormat df = new DecimalFormat("####.##");
    System.out.println("---------------------------");
    int j = 1;
    for (double i = 0.0; i < max; i += intSize, j++) {
      System.out.println(j + "-й інтервал від " + df.format(i) + " до " + df.format(i + intSize));
      intervals[j - 1] = i;
    }
    System.out.println("---------------------------");
    return intervals;
  }

  private static double[] staticSolid(double[] intervals, double intSize) {
    DecimalFormat df = new DecimalFormat("####.################");
    double[] staticSolid = new double[intervals.length];
    for (int i = 0; i < intervals.length; i++) {
      staticSolid[i] = intervalValuesCount(intervals[i], intervals[i] + intSize);
      staticSolid[i] = staticSolid[i] / (VALUES.length * intSize);
      System.out.println(
          "для " + (i + 1) + "-го інтервалу f" + (i + 1) + " = " + df.format(staticSolid[i]));
    }
    System.out.println("---------------------------");
    return staticSolid;
  }

  private static int intervalValuesCount(double from, double to) {
    int count = 0;
    for (double i : VALUES) {
      if (i > from && i <= to) {
        count++;
      }

    }
    return count;
  }

  private static double[] pt(double[] staticSolid, double[] intervals, double intSize) {
    double[] pArray = new double[staticSolid.length + 1];
    pArray[0] = 1.0;
    DecimalFormat df = new DecimalFormat("####.####");
    System.out.println("P(0) = " + pArray[0]);
    double p = 0;
    for (int i = 0; i < staticSolid.length; i++) {
      p += staticSolid[i] * intSize;
      pArray[i + 1] = 1.0 - p;
      System.out.println(
          "для " + (i + 1) + "-го інтервалу P(" + new DecimalFormat("####.#").format(
              intervals[i] + intSize)
              + ") = " + df.format(pArray[i + 1]));
    }
    System.out.println("---------------------------");
    return pArray;
  }


  private static double calcTy(double[] pArray, double intSize, int from, int to) {
    double p1 = pArray[from];
    double p2 = pArray[to];

    double d = (p2 - Y) / (p2 - p1);
    printValue(d, "d(" + from + ", " + to + ") = ", "####.###########");
    return intSize - intSize * d;
  }

  private static double pTime(double[] staticSolid, double[] intervals, double intSize,
      double time) {

    int index = getIndex(intervals, time, intSize);
    double pTime = 0;

    for (int i = 0; i < index; i++) {
      pTime += staticSolid[i] * intSize;
    }

    pTime += staticSolid[index] * (time - intervals[index]);
    return 1.0 - pTime;
  }

  private static double yTime(double f, double pTimeInt) {
    return f / pTimeInt;
  }

  private static int getIndex(double[] intervals, double time, double intSize) {
    int index = 0;
    for (int i = 0; i < intervals.length; i++) {
      if (time >= intervals[i] && time <= intervals[i] + intSize) {
        index = i;
        break;
      }
    }
    return index;
  }

  private static void printArray(double[] array, String message) {
    System.out.println(message);
    System.out.println(Arrays.toString(array));
  }

  private static void printValue(double value, String message, String format) {
    System.out.println(
        message + " " + ((format == null) ? value : new DecimalFormat(format).format(value)));
  }
}
