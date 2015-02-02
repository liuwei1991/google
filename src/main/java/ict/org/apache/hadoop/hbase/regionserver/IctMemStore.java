/**
*
* Licensed to the Apache Software Foundation (ASF) under one
* or more contributor license agreements.  See the NOTICE file
* distributed with this work for additional information
* regarding copyright ownership.  The ASF licenses this file
* to you under the Apache License, Version 2.0 (the
* "License"); you may not use this file except in compliance
* with the License.  You may obtain a copy of the License at
*
*     http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/
package ict.org.apache.hadoop.hbase.regionserver;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.rmi.UnexpectedException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.NavigableSet;
import java.util.SortedSet;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.hbase.classification.InterfaceAudience;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HConstants;
import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.KeyValueUtil;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.io.HeapSize;
import org.apache.hadoop.hbase.regionserver.KeyValueSkipListSet;
import org.apache.hadoop.hbase.regionserver.MemStoreChunkPool;
import org.apache.hadoop.hbase.regionserver.MemStoreLAB;
import org.apache.hadoop.hbase.regionserver.MemStoreLAB.Allocation;
import org.apache.hadoop.hbase.regionserver.TimeRangeTracker;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.hbase.util.ClassSize;
import org.apache.hadoop.hbase.util.EnvironmentEdgeManager;

/**
* The MemStore holds in-memory modifications to the Store.  Modifications
* are {@link KeyValue}s.  When asked to flush, current memstore is moved
* to snapshot and is cleared.  We continue to serve edits out of new memstore
* and backing snapshot until flusher reports in that the flush succeeded. At
* this point we let the snapshot go.
*  <p>
* The MemStore functions should not be called in parallel. Callers should hold
*  write and read locks. This is done in {@link HStore}.
*  </p>
*
* TODO: Adjust size of the memstore when we remove items because they have
* been deleted.
* TODO: With new KVSLS, need to make sure we update HeapSize with difference
* in KV size.
*/
@InterfaceAudience.Private
public class IctMemStore implements HeapSize {
 private static final Log LOG = LogFactory.getLog(IctMemStore.class);

 static final String USEMSLAB_KEY =
   "hbase.hregion.memstore.mslab.enabled";
 private static final boolean USEMSLAB_DEFAULT = true;

 private Configuration conf;

 // MemStore.  Use a KeyValueSkipListSet rather than SkipListSet because of the
 // better semantics.  The Map will overwrite if passed a key it already had
 // whereas the Set will not add new KV if key is same though value might be
 // different.  Value is not important -- just make sure always same
 // reference passed.
 volatile KeyValueSkipListSet kvset;

 // Snapshot of memstore.  Made for flusher.
 volatile KeyValueSkipListSet snapshot;

 final KeyValue.KVComparator comparator;

 // Used to track own heapSize
 final AtomicLong size;
 private volatile long snapshotSize;

 // Used to track when to flush
 volatile long timeOfOldestEdit = Long.MAX_VALUE;

 TimeRangeTracker timeRangeTracker;
 TimeRangeTracker snapshotTimeRangeTracker;

 MemStoreChunkPool chunkPool;
 volatile MemStoreLAB allocator;
 volatile MemStoreLAB snapshotAllocator;

 /**
  * Default constructor. Used for tests.
  */
 public IctMemStore() {
   this(HBaseConfiguration.create(), KeyValue.COMPARATOR);
 }

 /**
  * Constructor.
  * @param c Comparator
  */
 public IctMemStore(final Configuration conf, final KeyValue.KVComparator c) {
	 
 }

 void dump() {
	 
 }

 /**
  * Creates a snapshot of the current memstore.
  * Snapshot must be cleared by call to {@link #clearSnapshot(SortedSet)}
  * To get the snapshot made by this method, use {@link #getSnapshot()}
  */
 void snapshot() {

 }

 /**
  * Return the current snapshot.
  * Called by flusher to get current snapshot made by a previous
  * call to {@link #snapshot()}
  * @return Return snapshot.
  * @see #snapshot()
  * @see #clearSnapshot(SortedSet)
  */
 KeyValueSkipListSet getSnapshot() {

 }

 /**
  * On flush, how much memory we will clear.
  * Flush will first clear out the data in snapshot if any (It will take a second flush
  * invocation to clear the current Cell set). If snapshot is empty, current
  * Cell set will be flushed.
  *
  * @return size of data that is going to be flushed
  */
 long getFlushableSize() {

 }

 /**
  * The passed snapshot was successfully persisted; it can be let go.
  * @param ss The snapshot to clean out.
  * @throws UnexpectedException
  * @see #snapshot()
  */
 void clearSnapshot(final SortedSet<KeyValue> ss)
 throws UnexpectedException {

 }

 /**
  * Write an update
  * @param kv
  * @return approximate size of the passed key and value.
  */
 long add(final KeyValue kv) {

 }

 long timeOfOldestEdit() {

 }

 private boolean addToKVSet(KeyValue e) {

 }

 private boolean removeFromKVSet(KeyValue e) {

 }

 void setOldestEditTimeToNow() {

 }

 /**
  * Internal version of add() that doesn't clone KVs with the
  * allocator, and doesn't take the lock.
  *
  * Callers should ensure they already have the read lock taken
  */
 private long internalAdd(final KeyValue toAdd) {

 }

 private KeyValue maybeCloneWithAllocator(KeyValue kv) {

 }

 /**
  * Remove n key from the memstore. Only kvs that have the same key and the
  * same memstoreTS are removed.  It is ok to not update timeRangeTracker
  * in this call. It is possible that we can optimize this method by using
  * tailMap/iterator, but since this method is called rarely (only for
  * error recovery), we can leave those optimization for the future.
  * @param kv
  */
 void rollback(final KeyValue kv) {

 }

 /**
  * Write a delete
  * @param delete
  * @return approximate size of the passed key and value.
  */
 long delete(final KeyValue delete) {

 }

 /**
  * @param kv Find the row that comes after this one.  If null, we return the
  * first.
  * @return Next row or null if none found.
  */
 KeyValue getNextRow(final KeyValue kv) {

 }

 /*
  * @param a
  * @param b
  * @return Return lowest of a or b or null if both a and b are null
  */
 private KeyValue getLowest(final KeyValue a, final KeyValue b) {

 }

 /*
  * @param key Find row that follows this one.  If null, return first.
  * @param map Set to look in for a row beyond <code>row</code>.
  * @return Next row or null if none found.  If one found, will be a new
  * KeyValue -- can be destroyed by subsequent calls to this method.
  */
 private KeyValue getNextRow(final KeyValue key,
     final NavigableSet<KeyValue> set) {

 }

 /**
  * @param state column/delete tracking state
  */
 void getRowKeyAtOrBefore(final GetClosestRowBeforeTracker state) {

 }

 /*
  * @param set
  * @param state Accumulates deletes and candidates.
  */
 private void getRowKeyAtOrBefore(final NavigableSet<KeyValue> set,
     final GetClosestRowBeforeTracker state) {

 }

 /*
  * Walk forward in a row from <code>firstOnRow</code>.  Presumption is that
  * we have been passed the first possible key on a row.  As we walk forward
  * we accumulate deletes until we hit a candidate on the row at which point
  * we return.
  * @param set
  * @param firstOnRow First possible key on this row.
  * @param state
  * @return True if we found a candidate walking this row.
  */
 private boolean walkForwardInSingleRow(final SortedSet<KeyValue> set,
     final KeyValue firstOnRow, final GetClosestRowBeforeTracker state) {

 }

 /*
  * Walk backwards through the passed set a row at a time until we run out of
  * set or until we get a candidate.
  * @param set
  * @param state
  */
 private void getRowKeyBefore(NavigableSet<KeyValue> set,   final GetClosestRowBeforeTracker state) {

 }

 /**
  * Only used by tests. TODO: Remove
  *
  * Given the specs of a column, update it, first by inserting a new record,
  * then removing the old one.  Since there is only 1 KeyValue involved, the memstoreTS
  * will be set to 0, thus ensuring that they instantly appear to anyone. The underlying
  * store will ensure that the insert/delete each are atomic. A scanner/reader will either
  * get the new value, or the old value and all readers will eventually only see the new
  * value after the old was removed.
  *
  * @param row
  * @param family
  * @param qualifier
  * @param newValue
  * @param now
  * @return  Timestamp
  */
 long updateColumnValue(byte[] row, byte[] family, byte[] qualifier, long newValue, long now) {

 }

 /**
  * Update or insert the specified KeyValues.
  * <p>
  * For each KeyValue, insert into MemStore.  This will atomically upsert the
  * value for that row/family/qualifier.  If a KeyValue did already exist,
  * it will then be removed.
  * <p>
  * Currently the memstoreTS is kept at 0 so as each insert happens, it will
  * be immediately visible.  May want to change this so it is atomic across
  * all KeyValues.
  * <p>
  * This is called under row lock, so Get operations will still see updates
  * atomically.  Scans will only see each KeyValue update as atomic.
  *
  * @param cells
  * @param readpoint readpoint below which we can safely remove duplicate KVs 
  * @return change in memstore size
  */
 public long upsert(Iterable<Cell> cells, long readpoint) {
   long size = 0;
   
   return size;
 }

 /**
  * Inserts the specified KeyValue into MemStore and deletes any existing
  * versions of the same row/family/qualifier as the specified KeyValue.
  * <p>
  * First, the specified KeyValue is inserted into the Memstore.
  * <p>
  * If there are any existing KeyValues in this MemStore with the same row,
  * family, and qualifier, they are removed.
  * <p>
  * Callers must hold the read lock.
  *
  * @param cell
  * @return change in size of MemStore
  */
 private long upsert(Cell cell, long readpoint) {
   
   return 0;
 }

 /*
  * Immutable data structure to hold member found in set and the set it was
  * found in.  Include set because it is carrying context.
  */
 private static class Member {
	 
 }

 /*
  * @param set Set to walk back in.  Pass a first in row or we'll return
  * same row (loop).
  * @param state Utility and context.
  * @param firstOnRow First item on the row after the one we want to find a
  * member in.
  * @return Null or member of row previous to <code>firstOnRow</code>
  */
 private Member memberOfPreviousRow(NavigableSet<KeyValue> set,
     final GetClosestRowBeforeTracker state, final KeyValue firstOnRow) {
	 
 }

 /**
  * @return scanner on memstore and snapshot in this order.
  */
 List<KeyValueScanner> getScanners(long readPt) {
	 
 }

 /**
  * Check if this memstore may contain the required keys
  * @param scan
  * @return False if the key definitely does not exist in this Memstore
  */
 public boolean shouldSeek(Scan scan, long oldestUnexpiredTS) {
	 
 }

 public TimeRangeTracker getSnapshotTimeRangeTracker() {
	 
 }

 /*
  * MemStoreScanner implements the KeyValueScanner.
  * It lets the caller scan the contents of a memstore -- both current
  * map and snapshot.
  * This behaves as if it were a real scanner but does not maintain position.
  */
 protected class MemStoreScanner extends NonLazyKeyValueScanner {
	 
 }

 public final static long FIXED_OVERHEAD = ClassSize.align(
		 ClassSize.OBJECT + (10 * ClassSize.REFERENCE) + (2 * Bytes.SIZEOF_LONG));

 public final static long DEEP_OVERHEAD = ClassSize.align(FIXED_OVERHEAD +
     ClassSize.ATOMIC_LONG + (2 * ClassSize.TIMERANGE_TRACKER) +
     (2 * ClassSize.KEYVALUE_SKIPLIST_SET) + (2 * ClassSize.CONCURRENT_SKIPLISTMAP));

 /*
  * Calculate how the MemStore size has changed.  Includes overhead of the
  * backing Map.
  * @param kv
  * @param notpresent True if the kv was NOT present in the set.
  * @return Size
  */
 static long heapSizeChange(final KeyValue kv, final boolean notpresent) {
	 return 0;
 }

 /**
  * Get the entire heap usage for this MemStore not including keys in the
  * snapshot.
  */
 @Override
 public long heapSize() {
	 return 0;
 }

 /**
  * Get the heap usage of KVs in this MemStore.
  */
 public long keySize() {
	 return 0;
 }

 /**
  * Code to help figure if our approximation of object heap sizes is close
  * enough.  See hbase-900.  Fills memstores then waits so user can heap
  * dump and bring up resultant hprof in something like jprofiler which
  * allows you get 'deep size' on objects.
  * @param args main args
  */
 public static void main(String [] args) {
	 
 }
}
