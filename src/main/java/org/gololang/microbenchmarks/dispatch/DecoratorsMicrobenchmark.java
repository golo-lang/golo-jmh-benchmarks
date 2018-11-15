/*
 * Copyright 2018 Julien Ponge.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.gololang.microbenchmarks.dispatch;

import org.gololang.microbenchmarks.support.CodeLoader;
import org.openjdk.jmh.annotations.*;

import java.lang.invoke.MethodHandle;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class DecoratorsMicrobenchmark {

  static long add(long v, long n) {
    return v + n;
  }

  static long times(long v, long n) {
    return v * n;
  }

  @State(Scope.Thread)
  static public class JavaState {
    long A = 123456;
    long B = 465;
    long C = 123;
  }

  @State(Scope.Thread)
  static public class GoloState {
    MethodHandle add;
    long A = 123456;
    long B = 465;
    // long C -> in the decorator call

    @Setup
    public void setup() {
      add = new CodeLoader().golo("decorators", "add", 2);
    }
  }

  @Benchmark
  public long baseline(JavaState state) {
    return times(add(state.A, state.B), state.C);
  }

  @Benchmark
  public Object golo(GoloState state) throws Throwable {
    return state.add.invoke(state.A, state.B);
  }
}
