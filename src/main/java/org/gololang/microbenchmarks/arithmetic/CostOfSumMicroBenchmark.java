package org.gololang.microbenchmarks.arithmetic;

import org.gololang.microbenchmarks.support.CodeLoader;
import org.openjdk.jmh.annotations.*;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import static java.lang.invoke.MethodType.genericMethodType;
import static java.lang.invoke.MethodType.methodType;

@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class CostOfSumMicroBenchmark {

  public static long sum(long x, long y) {
    return x + y;
  }

  public static Object boxed_sum(Object x, Object y) {
    return (Long) x + (Long) y;
  }

  public static Object boxed_sum_with_constant(Object x) {
    return (Long) x + 10L;
  }

  @State(Scope.Thread)
  static public class DataState {

    long x;
    long y;

    @Setup(Level.Iteration)
    public void setup() {
      Random rand = new Random();
      x = (long) rand.nextInt();
      y = (long) rand.nextInt();
    }
  }

  @State(Scope.Thread)
  static public class JavaState {

    MethodHandle sumHandle;
    MethodHandle boxedSumHandle;
    MethodHandle boxedSumWithConstantHandle;

    @Setup(Level.Trial)
    public void setup() {
      try {
        MethodHandles.Lookup lookup = MethodHandles.lookup();
        sumHandle = lookup.findStatic(CostOfSumMicroBenchmark.class, "sum", methodType(long.class, long.class, long.class));
        boxedSumHandle = lookup.findStatic(CostOfSumMicroBenchmark.class, "boxed_sum", genericMethodType(2));
        boxedSumWithConstantHandle = lookup.findStatic(CostOfSumMicroBenchmark.class, "boxed_sum_with_constant", genericMethodType(1));
      } catch (NoSuchMethodException | IllegalAccessException e) {
        throw new AssertionError(e);
      }
    }
  }

  @State(Scope.Thread)
  static public class GoloState {

    MethodHandle sumHandle;
    MethodHandle sumWithConstantHandle;
    MethodHandle sumOfConstantsHandle;

    @Setup(Level.Trial)
    public void setup() {
      sumHandle = new CodeLoader().golo("arithmetic", "sum", 2);
      sumWithConstantHandle = new CodeLoader().golo("arithmetic", "sum_with_constant", 1);
      sumOfConstantsHandle = new CodeLoader().golo("arithmetic", "sum_of_constants", 0);
    }
  }

  @State(Scope.Thread)
  static public class GroovyState {

    MethodHandle sumHandle;
    MethodHandle sumWithConstantHandle;
    MethodHandle sumOfConstantsHandle;
    MethodHandle fastSumHandle;
    MethodHandle fastestSumHandle;

    @Setup(Level.Trial)
    public void setup() {
      sumHandle = new CodeLoader().groovy("arithmetic", "sum", genericMethodType(2));
      sumWithConstantHandle = new CodeLoader().groovy("arithmetic", "sum_with_constant", genericMethodType(1));
      sumOfConstantsHandle = new CodeLoader().groovy("arithmetic", "sum_of_constants", genericMethodType(0));
      fastSumHandle = new CodeLoader().groovy("arithmetic", "fast_sum", methodType(long.class, long.class, long.class));
      fastestSumHandle = new CodeLoader().groovy("arithmetic", "fastest_sum", methodType(long.class, long.class, long.class));
    }
  }

  @State(Scope.Thread)
  static public class GroovyIndyState {

    MethodHandle sumHandle;
    MethodHandle sumWithConstantHandle;
    MethodHandle sumOfConstantsHandle;
    MethodHandle fastSumHandle;
    MethodHandle fastestSumHandle;

    @Setup(Level.Trial)
    public void setup() {
      sumWithConstantHandle = new CodeLoader().groovy("arithmetic", "sum_with_constant", genericMethodType(1));
      sumOfConstantsHandle = new CodeLoader().groovy("arithmetic", "sum_of_constants", genericMethodType(0));
      sumHandle = new CodeLoader().groovy_indy("arithmetic", "sum", genericMethodType(2));
      fastSumHandle = new CodeLoader().groovy_indy("arithmetic", "fast_sum", methodType(long.class, long.class, long.class));
      fastestSumHandle = new CodeLoader().groovy_indy("arithmetic", "fastest_sum", methodType(long.class, long.class, long.class));
    }
  }

  @Benchmark
  public Object baseline_java(DataState dataState, JavaState javaState) throws Throwable {
    return javaState.sumHandle.invoke(dataState.x, dataState.y);
  }

  @Benchmark
  public Object baseline_return_value(DataState dataState) {
    return dataState.x;
  }

  @Benchmark
  public Object baseline_java_boxed(DataState dataState, JavaState javaState) throws Throwable {
    return javaState.boxedSumHandle.invoke(dataState.x, dataState.y);
  }

  @Benchmark
  public Object baseline_java_boxed_with_constant(DataState dataState, JavaState javaState) throws Throwable {
    return javaState.boxedSumWithConstantHandle.invoke(dataState.x);
  }

  @Benchmark
  public Object golo_sum(DataState dataState, GoloState goloState) throws Throwable {
    return goloState.sumHandle.invoke(dataState.x, dataState.y);
  }

  @Benchmark
  public Object golo_sum_with_constant(DataState dataState, GoloState goloState) throws Throwable {
    return goloState.sumWithConstantHandle.invoke(dataState.x);
  }

  @Benchmark
  public Object golo_sum_of_constants(DataState dataState, GoloState goloState) throws Throwable {
    return goloState.sumOfConstantsHandle.invoke();
  }

  @Benchmark
  public Object groovy_sum(DataState dataState, GroovyState groovyState) throws Throwable {
    return groovyState.sumHandle.invoke(dataState.x, dataState.y);
  }

  @Benchmark
  public Object groovy_sum_with_constant(DataState dataState, GroovyState groovyState) throws Throwable {
    return groovyState.sumWithConstantHandle.invoke(dataState.x);
  }

  @Benchmark
  public Object groovy_sum_of_constants(GroovyState groovyState) throws Throwable {
    return groovyState.sumOfConstantsHandle.invoke();
  }

  @Benchmark
  public Object groovy_sum_fast(DataState dataState, GroovyState groovyState) throws Throwable {
    return groovyState.fastSumHandle.invoke(dataState.x, dataState.y);
  }

  @Benchmark
  public Object groovy_sum_fastest(DataState dataState, GroovyState groovyState) throws Throwable {
    return groovyState.fastestSumHandle.invoke(dataState.x, dataState.y);
  }

  @Benchmark
  public Object groovy_sum_indy(DataState dataState, GroovyIndyState groovyState) throws Throwable {
    return groovyState.sumHandle.invoke(dataState.x, dataState.y);
  }

  @Benchmark
  public Object groovy_sum_indy_sum_with_constant(DataState dataState, GroovyIndyState groovyState) throws Throwable {
    return groovyState.sumWithConstantHandle.invoke(dataState.x);
  }

  @Benchmark
  public Object groovy_sum_indy_sum_of_constants(GroovyIndyState groovyState) throws Throwable {
    return groovyState.sumOfConstantsHandle.invoke();
  }

  @Benchmark
  public Object groovy_sum_indy_fast(DataState dataState, GroovyIndyState groovyState) throws Throwable {
    return groovyState.fastSumHandle.invoke(dataState.x, dataState.y);
  }

  @Benchmark
  public Object groovy_sum_indy_fastest(DataState dataState, GroovyIndyState groovyState) throws Throwable {
    return groovyState.fastestSumHandle.invoke(dataState.x, dataState.y);
  }
}
