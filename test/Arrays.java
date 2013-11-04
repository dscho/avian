public class Arrays {
  private static void expect(boolean v) {
    if (! v) throw new RuntimeException();
  }

  private static <T extends Comparable<T>> void mergeSort(T[] array) {
//System.err.println(java.util.Arrays.toString(array));
     for (int stride = 1; stride < array.length; stride <<= 1) {
//System.err.println("=== stride: " + stride);
      for (int begin = 0; begin < array.length; ) {
        int i1 = begin, i2 = begin + stride, end = i2 + stride;
        if (end >= array.length) {
          if (i2 >= array.length) {
            break;
          }
          end = array.length;
        }

        int i;
        for (i = begin; i1 < i2 && i2 < end; ++i) {
          T a = array[i1], b = array[i2];
System.err.print("                 ");
for (int j = i1; j < i2; ++j) {
  System.err.print(", " + array[j]);
}
for (int j = i; j < i1; ++j) {
  System.err.print(", " + array[j]);
}
System.err.print("    -----    ");
for (int j = i2; j < end; ++j) {
  System.err.print(", " + array[j]);
}
System.err.println();
if (i1 == 0 && i2 == 2) {
  System.err.println("Hello");
}
expect(i <= i1);
expect(i1 < i2);
expect (i == begin || array[i].compareTo(array[i - 1]) >= 0);
expect(i1 + 1 < i2 ? array[i1].compareTo(array[i1 + 1]) <= 0 : (i1 == i || array[i1].compareTo(array[i]) <= 0));
expect(i2 + 1 >= end || array[i2].compareTo(array[i2 + 1]) <= 0);
System.err.println("i: " + i + ", i1: " + i1 + ", i2: " + i2 + ", end: " + end);
System.err.println("" + a + " <= " + b + ": " + a.compareTo(b));
          if (a.compareTo(b) <= 0) {
            if (i1 == i) {
              ++i1;
            } else {
              array[i1] = array[i];
              array[i] = a;
              if (i1 + 1 < i2) {
                if (++i1 == i2) {
                  i1 = i + 1;
                }
              }
            }
          } else {
            array[i2++] = array[i];
            array[i] = b;
            if (i == i1) {
              i1 = i2 - 1;
            }
          }
System.err.println(java.util.Arrays.toString(array));
        }
        if (i1 < i2) {
          while (i < i1) {
            T a = array[i1];
            array[i1] = array[i];
            array[i++] = a;
            if (i1 + 1 < i2) {
              i1++;
            }
          }
        }
        begin = end;
      }
    }
  }

  private static <T extends Comparable<T>> void expectSorted(T[] array) {
    for (int i = 1; i < array.length; ++i) {
      expect(array[i - 1].compareTo(array[i]) <= 0);
    }
  }

  private static int pseudoRandom(int seed) {
    return 3170425 * seed + 132102;
  }

  private static <T extends Comparable<T>> int shuffle(T[] array, int seed) {
    for (int i = array.length; i > 1; --i) {
      int i2 = (seed < 0 ? -seed : seed) % i;
      T value = array[i - 1];
      array[i - 1] = array[i2];
      array[i2] = value;
      seed = pseudoRandom(seed);
    }
    return seed;
  }

  public static void testSort() {
    Integer[] array = new Integer[64];
    for (int i = 0; i < array.length; ++i) {
      array[i] = Integer.valueOf(i + 1);
    }
    ;
    int random = 12345;
    for (int i = 0; i < 32; ++i) {
      random = shuffle(array, random);
      java.util.Arrays.sort(array);
      expectSorted(array);
    }
  }

  public static void main(String[] args) {
if (!true) {
  int count = 8; // 60000;
  int trials = 999;
  Integer[] array = new Integer[count];
  int random = 12345;

  long best = Long.MAX_VALUE, worst = -Long.MAX_VALUE, average = 0;
  for (int k = 0; k < trials; ++k) {
System.err.println("k: " + k);
    for (int i = 0; i < array.length; ++i) {
      array[i] = random % 20;
      random = pseudoRandom(random);
    }
System.err.println(java.util.Arrays.toString(array));
    long start = System.nanoTime();
    mergeSort(array);
    long end = System.nanoTime();
    long total = end - start;
    if (best > total) {
      best = total;
    }
    if (worst < total) {
      worst = total;
    }
    average += total;
System.err.println(java.util.Arrays.toString(array));
    expectSorted(array);
  }
  average /= trials;

  random = 12345;
  long best2 = Long.MAX_VALUE, worst2 = 0, average2 = 0;
  for (int k = 0; k < trials; ++k) {
    for (int i = 0; i < array.length; ++i) {
      array[i] = random;
      random = pseudoRandom(random);
    }
    long start = System.nanoTime();
    java.util.Arrays.sort(array);
    long end = System.nanoTime();
    long total = end - start;
    if (best2 > total) {
      best2 = total;
    }
    if (worst2 < total) {
      worst2 = total;
    }
    average2 += total;
    expectSorted(array);
  }
  average2 /= trials;

  System.err.println("" + best2 + " - " + best + ": " + (best2 - best));
  System.err.println("" + worst2 + " - " + worst + ": " + (worst2 - worst));
  System.err.println("" + average2 + " - " + average + ": " + (average2 - average));
  return;
}
if (true) {
  Integer[] array = new Integer[] { 6, 5, 3, 1, 8, 7, 2, 4 };
array = new Integer[] { -2, 0, 1, 3, -1, -3, 4, 2 };
array = new Integer[] { 5, 11, 9, -17, -19, -1, 1, 3 };
array = new Integer[] { 0, -2, 1, 2, -3, -1, -4, 3 };
  mergeSort(array);
  expectSorted(array);
  return;
}
    { int[] array = new int[0];
      Exception exception = null;
      try {
        int x = array[0];
      } catch (ArrayIndexOutOfBoundsException e) {
        exception = e;
      }

      expect(exception != null);
    }

    { int[] array = new int[0];
      Exception exception = null;
      try {
        int x = array[-1];
      } catch (ArrayIndexOutOfBoundsException e) {
        exception = e;
      }

      expect(exception != null);
    }

    { int[] array = new int[3];
      int i = 0;
      array[i++] = 1;
      array[i++] = 2;
      array[i++] = 3;

      expect(array[--i] == 3);
      expect(array[--i] == 2);
      expect(array[--i] == 1);
    }

    { Object[][] array = new Object[1][1];
      expect(array.length == 1);
      expect(array[0].length == 1);
    }

    { Object[][] array = new Object[2][3];
      expect(array.length == 2);
      expect(array[0].length == 3);
    }

    { int j = 0;
      byte[] decodeTable = new byte[256];
      for (int i = 'A'; i <= 'Z'; ++i) decodeTable[i] = (byte) j++;
      for (int i = 'a'; i <= 'z'; ++i) decodeTable[i] = (byte) j++;
      for (int i = '0'; i <= '9'; ++i) decodeTable[i] = (byte) j++;
      decodeTable['+'] = (byte) j++;
      decodeTable['/'] = (byte) j++;
      decodeTable['='] = 0;

      expect(decodeTable['a'] != 0);
    }

    { boolean p = true;
      int[] array = new int[] { 1, 2 };
      expect(array[0] == array[p ? 0 : 1]);
      p = false;
      expect(array[1] == array[p ? 0 : 1]);
    }

    { int[] array = new int[1024];
      array[1023] = -1;
      expect(array[1023] == -1);
      expect(array[1022] == 0);
    }

    { Integer[] array = (Integer[])
        java.lang.reflect.Array.newInstance(Integer.class, 1);
      array[0] = Integer.valueOf(42);
      expect(array[0].intValue() == 42);
    }

    { Object[] a = new Object[3];
      Object[] b = new Object[3];

      expect(java.util.Arrays.equals(a, b));
      a[0] = new Object();
      expect(! java.util.Arrays.equals(a, b));
      expect(! java.util.Arrays.equals(b, new Object[4]));
      expect(! java.util.Arrays.equals(a, null));
      expect(! java.util.Arrays.equals(null, b));
      expect(java.util.Arrays.equals((Object[])null, (Object[])null));
      b[0] = a[0];
      expect(java.util.Arrays.equals(a, b));

      java.util.Arrays.hashCode(a);
      java.util.Arrays.hashCode((Object[])null);
    }

    testSort();
  }
}
