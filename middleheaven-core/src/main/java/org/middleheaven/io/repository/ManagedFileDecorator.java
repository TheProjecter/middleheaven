/**
 * 
 */
package org.middleheaven.io.repository;

import java.net.URI;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;

import org.middleheaven.io.ManagedIOException;
import org.middleheaven.io.repository.watch.WatchEvent.Kind;
import org.middleheaven.io.repository.watch.WatchEventChannel;
import org.middleheaven.io.repository.watch.WatchService;
import org.middleheaven.util.collections.Enumerable;
import org.middleheaven.util.collections.Pair;
import org.middleheaven.util.collections.PairEnumerable;
import org.middleheaven.util.function.BinaryOperator;
import org.middleheaven.util.function.Block;
import org.middleheaven.util.function.Mapper;
import org.middleheaven.util.function.Predicate;

/**
 * 
 */
class ManagedFileDecorator implements ManagedFile {

	private final ManagedFile original;

	public ManagedFileDecorator ( ManagedFile original){
		this.original = original;
	}

	/**
	 * {@inheritDoc}
	 */
	public void forEach(Block<ManagedFile> walker) {
		original.forEach(walker);
	}

	/**
	 * {@inheritDoc}
	 */
	public void forEachRecursive(Block<ManagedFile> walker) {
		original.forEachRecursive(walker);
	}

	/**
	 * {@inheritDoc}
	 */
	public void forEachParent(Block<ManagedFile> walker) {
		original.forEachParent(walker);
	}

	/**
	 * {@inheritDoc}
	 */
	public WatchEventChannel register(WatchService watchService, Kind... events) {
		return original.register(watchService, events);
	}

	/**
	 * {@inheritDoc}
	 */
	public ManagedFileRepository getRepository() {
		return original.getRepository();
	}

	/**
	 * {@inheritDoc}
	 */
	public ManagedFilePath getPath() {
		return original.getPath();
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean canRenameTo(String newBaseName) {
		return original.canRenameTo(newBaseName);
	}

	/**
	 * {@inheritDoc}
	 */
	public void renameTo(String newBaseName) throws ManagedIOException {
		original.renameTo(newBaseName);
	}

	/**
	 * {@inheritDoc}
	 */
	public long getSize() throws ManagedIOException {
		return original.getSize();
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean contains(ManagedFile other) {
		return original.contains(other);
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean exists() {
		return original.exists();
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isWatchable() {
		return original.isWatchable();
	}

	/**
	 * {@inheritDoc}
	 */
	public ManagedFile getParent() {
		return original.getParent();
	}

	/**
	 * {@inheritDoc}
	 */
	public ManagedFileType getType() {
		return original.getType();
	}

	/**
	 * {@inheritDoc}
	 */
	public URI getURI() {
		return original.getURI();
	}

	/**
	 * {@inheritDoc}
	 */
	public ManagedFile retrive(String path) throws ManagedIOException {
		return original.retrive(path);
	}

	/**
	 * {@inheritDoc}
	 */
	public ManagedFile retrive(ManagedFilePath path) throws ManagedIOException {
		return original.retrive(path);
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isReadable() {
		return original.isReadable();
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isWriteable() {
		return original.isWriteable();
	}

	/**
	 * {@inheritDoc}
	 */
	public ManagedFileContent getContent() {
		return original.getContent();
	}

	/**
	 * {@inheritDoc}
	 */
	public void copyTo(ManagedFile other) throws ManagedIOException {
		original.copyTo(other);
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean delete() {
		return original.delete();
	}

	/**
	 * {@inheritDoc}
	 */
	public ManagedFile createFile() throws UnsupportedOperationException {
		return original.createFile();
	}

	/**
	 * {@inheritDoc}
	 */
	public ManagedFile createFolder() throws UnsupportedOperationException {
		return original.createFolder();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void copyTo(ContentSource other) throws ManagedIOException {
		original.copyTo(other);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <L extends Collection<ManagedFile>> L into(L collection) {
		return original.into(collection);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void deleteTree() {
		original.deleteTree();
	}

	/**
	 * {@inheritDoc}
	 */
	public Iterator<ManagedFile> iterator() {
		return original.iterator();
	}

	/**
	 * {@inheritDoc}
	 */
	public <U> Enumerable<U> cast(Class<U> newType) {
		return original.cast(newType);
	}

	/**
	 * {@inheritDoc}
	 */
	public int size() {
		return original.size();
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isEmpty() {
		return original.isEmpty();
	}

	/**
	 * {@inheritDoc}
	 */
	public ManagedFile getFirst() {
		return original.getFirst();
	}

	/**
	 * {@inheritDoc}
	 */
	public Enumerable<ManagedFile> sort(Comparator<ManagedFile> comparable) {
		return original.sort(comparable);
	}

	/**
	 * {@inheritDoc}
	 */
	public Enumerable<ManagedFile> sort() {
		return original.sort();
	}

	/**
	 * {@inheritDoc}
	 */
	public Enumerable<ManagedFile> reject(Predicate<ManagedFile> predicate) {
		return original.reject(predicate);
	}

	/**
	 * {@inheritDoc}
	 */
	public int count(ManagedFile object) {
		return original.count(object);
	}

	/**
	 * {@inheritDoc}
	 */
	public int count(Predicate<ManagedFile> predicate) {
		return original.count(predicate);
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean every(Predicate<ManagedFile> predicate) {
		return original.every(predicate);
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean any(Predicate<ManagedFile> predicate) {
		return original.any(predicate);
	}

	/**
	 * {@inheritDoc}
	 */
	public ManagedFile find(Predicate<ManagedFile> predicate) {
		return original.find(predicate);
	}

	/**
	 * {@inheritDoc}
	 */
	public Enumerable<ManagedFile> filter(Predicate<ManagedFile> predicate) {
		return original.filter(predicate);
	}

	/**
	 * {@inheritDoc}
	 */
	public <C> Enumerable<C> map(Mapper<C, ManagedFile> mapper) {
		return original.map(mapper);
	}

	/**
	 * {@inheritDoc}
	 */
	public <C> Enumerable<C> mapAll(Mapper<Enumerable<C>, ManagedFile> mapper) {
		return original.mapAll(mapper);
	}

	/**
	 * {@inheritDoc}
	 */
	public ManagedFile reduce(ManagedFile seed,
			BinaryOperator<ManagedFile> operator) {
		return original.reduce(seed, operator);
	}

	/**
	 * {@inheritDoc}
	 */
	public <C> C mapReduce(C seed, Mapper<Enumerable<C>, ManagedFile> mapper,
			BinaryOperator<C> operator) {
		return original.mapReduce(seed, mapper, operator);
	}

	/**
	 * {@inheritDoc}
	 */
	public String join(String separator) {
		return original.join(separator);
	}

	@Override
	public <K, V, P extends Pair<K, V>> PairEnumerable<K, V> pairMap(
			Mapper<Pair<K, V>, ManagedFile> mapper) {
		return original.pairMap(mapper);
	}

	@Override
	public <C> PairEnumerable<C, Enumerable<ManagedFile>> groupBy(
			Mapper<C, ManagedFile> mapper) {
		return original.groupBy(mapper);
	}

	
	
}
