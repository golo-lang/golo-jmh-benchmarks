class Arithmetic {

  static def gcd(x, y) {
    def a = x
    def b = y
    while (a != b) {
      if (a > b) {
        a = a - b
      } else {
        b = b - a
      }
    }
    return a
  }

  static long fast_gcd(long x, long y) {
    long a = x
    long b = y
    while (a != b) {
      if (a > b) {
        a = a - b
      } else {
        b = b - a
      }
    }
    return a
  }

  @groovy.transform.CompileStatic
  static long fastest_gcd(long x, long y) {
    long a = x
    long b = y
    while (a != b) {
      if (a > b) {
        a = a - b
      } else {
        b = b - a
      }
    }
    return a
  }

  static def sum(x, y) {
    return x + y
  }

  static long fast_sum(long x, long y) {
    return x + y
  }

  @groovy.transform.CompileStatic
  static long fastest_sum(long x, long y) {
    return x + y
  }
}
