/** Copyright 2014 Robin Stumm (serverkorken@gmail.com, http://dermetfan.net)
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License. */

package de.fhkoeln.game.utils;

/** @param <K> the type of the key
 *  @param <V> the type of the value
 *  @author dermetfan */
public class Pair<K, V> {

	/** the key */
	protected K key;

	/** the value */
	protected V value;

	/** creates an empty pair ({@link #key} and {@link #value} are {@code null}) */
	public Pair() {}

	/** @param key the {@link #key}
	 *  @param value the {@link #value} */
	public Pair(K key, V value) {
		this.key = key;
		this.value = value;
	}

	/** @param pair the {@link de.fhkoeln.game.utils.Pair} to copy */
	public Pair(Pair<K, V> pair) {
		key = pair.key;
		value = pair.value;
	}

	/** sets {@link #key} and {@link #value} to null */
	public void clear() {
		key = null;
		value = null;
	}

	/** @return if {@link #key} or {@link #value} is not {@code null} */
	public boolean isEmpty() {
		return key != null || value != null;
	}

	/** @return if {@link #key} and {@link #value} are both not {@code null} */
	public boolean isFull() {
		return key != null && value != null;
	}

	/** @return if {@link #key} is not null */
	public boolean hasKey() {
		return key != null;
	}

	/** @return if {@link #value} is not null */
	public boolean hasValue() {
		return value != null;
	}

	/** swaps key and value
	 *  @throws IllegalStateException if the classes of {@link #key} and {@link #value} are not {@link Class#isAssignableFrom(Class) assignable} from each other */
	@SuppressWarnings("unchecked")
	public void swap() throws IllegalStateException {
		if(!key.getClass().isAssignableFrom(value.getClass()) || !value.getClass().isAssignableFrom(key.getClass()))
			throw new IllegalStateException("the types of key and value are not assignable from each other: " + key.getClass().getName() + " - " + value.getClass().getName());
		V oldValue = value;
		value = (V) key;
		key = (K) oldValue;
	}

	/** @param pair the Pair which {@link #key} and {@link #value} to use
	 *  @return this Pair for chaining */
	public Pair<K, V> set(Pair<K, V> pair) {
		key = pair.key;
		value = pair.value;
		return this;
	}

	/** @param key the {@link #key} to set
	 *  @param value the {@link #value} to set
	 *  @return this Pair for chaining */
	public Pair<K, V> set(K key, V value) {
		this.key = key;
		this.value = value;
		return this;
	}

	/** @return the {@link #key} */
	public K key() {
		return key;
	}

	/** @return the old {@link #key}
	 *  @see #setKey(Object)
	 *  @see #getKey() */
	public K key(K key) {
		K oldKey = this.key;
		this.key = key;
		return oldKey;
	}

	/** @return the {@link #value}*/
	public V value() {
		return value;
	}

	/** @return the old {@link #value}
	 *  @see #setValue(Object)
	 *  @see #getKey() */
	public V value(V value) {
		V oldValue = this.value;
		this.value = value;
		return oldValue;
	}

	/** @return the {@link #key} */
	public K getKey() {
		return key;
	}

	/** @param key the {@link #key} to set */
	public void setKey(K key) {
		this.key = key;
	}

	/** @return the {@link #value} */
	public V getValue() {
		return value;
	}

	/** @param value the {@link #value} to set */
	public void setValue(V value) {
		this.value = value;
	}

	/** if the given object is a {@link de.fhkoeln.game.utils.Pair} instance, {@link Object#equals(Object) equals} comparison will be used on key and value */
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Pair) {
			Pair<?, ?> pair = (Pair<?, ?>) obj;
			return key.equals(pair.key) && value.equals(pair.value);
		}
		return super.equals(obj);
	}

	/** @return [{@link #key} &amp; {@link #value}] */
	@Override
	public String toString() {
		return "[" + key + " & " + value + "]";
	}

}
