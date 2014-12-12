package com.epickrram.experiment.lock.benchmark;

//////////////////////////////////////////////////////////////////////////////////
//   Copyright 2014   Mark Price     mark at epickrram.com                      //
//                                                                              //
//   Licensed under the Apache License, Version 2.0 (the "License");            //
//   you may not use this file except in compliance with the License.           //
//   You may obtain a copy of the License at                                    //
//                                                                              //
//       http://www.apache.org/licenses/LICENSE-2.0                             //
//                                                                              //
//   Unless required by applicable law or agreed to in writing, software        //
//   distributed under the License is distributed on an "AS IS" BASIS,          //
//   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.   //
//   See the License for the specific language governing permissions and        //
//   limitations under the License.                                             //
//////////////////////////////////////////////////////////////////////////////////

import com.epickrram.experiment.lock.biased.BiasedLock;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@State(Scope.Benchmark)
public class LockBenchmark
{
    private final Lock fairReentrantLock = new ReentrantLock(true);
    private final Lock unfairReentrantLock = new ReentrantLock(false);
    private final Object intrinsicLock = new Object();
    private final Lock biasedLock = new BiasedLock();

    private long counter = 0L;

    @Benchmark
    public long fairReentrantLock()
    {
        return testLock(fairReentrantLock);
    }

    @Benchmark
    public long unfairReentrantLock()
    {
        return testLock(unfairReentrantLock);
    }

    @Benchmark
    public long biasedLock()
    {
        return testLock(biasedLock);
    }

    @Benchmark
    public long intrinsicLock()
    {
        synchronized (intrinsicLock)
        {
            counter++;
            return counter;
        }
    }

    private long testLock(final Lock lock)
    {
        lock.lock();
        try
        {
            counter ++;
            return counter;
        }
        finally
        {
            lock.unlock();
        }
    }
}