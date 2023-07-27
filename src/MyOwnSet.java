import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class MyOwnSet<E> extends AbstractSet<E> implements Serializable, Cloneable, Set<E> {
    private static final Object PRESENT = new Object();
    private transient HashMap<E, Object> map;

    public MyOwnSet() {
        map = new HashMap<>();
    }
    public MyOwnSet(Collection<? extends E> collection){
        this.map = new HashMap<>(Math.max((int) (collection.size() / .75f) + 1, 16));
        addAll(collection);
    }

    @Override
    public boolean add(E e) {
        return null == map.put(e, PRESENT);
    }

    @Override
    public Iterator<E> iterator() {
        return map.keySet().iterator();
    }

    @Override
    public void forEach(Consumer<? super E> action) {
        super.forEach(action);
    }

    @Override
    public boolean removeIf(Predicate<? super E> filter) {
        return super.removeIf(filter);
    }

    @Override
    public Spliterator<E> spliterator() {
        return super.spliterator();
    }

    @Override
    public Stream<E> stream() {
        return super.stream();
    }

    @Override
    public Stream<E> parallelStream() {
        return super.parallelStream();
    }

    @Override
    public int size() {
        return map.size();
    }

    public boolean isEmpty(){
        return map.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return map.containsKey(o);
    }

    @Override
    public void clear() {
        map.clear();
    }

    public boolean remove(Object o){
        map.remove(o);
        return true;
    }

    public Object clone(){
        try {
            MyOwnSet<E> newSet = (MyOwnSet<E>) super.clone();
            newSet.map = (HashMap<E, Object>) map.clone();
            return newSet;
        } catch (Exception e) {
            throw new InternalError();
        }
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        objectOutputStream.defaultWriteObject();
        objectOutputStream.writeInt(HashMapReflectionHelper.callHiddenMethod(map, "capacity"));
        objectOutputStream.writeFloat(HashMapReflectionHelper.callHiddenMethod(map, "loadFactor"));
        objectOutputStream.writeInt(map.size());
        for(E e : map.keySet()) {
            objectOutputStream.writeObject(e);
        }
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        objectInputStream.defaultReadObject();
        int capacity = objectInputStream.readInt();
        float loadFactor = objectInputStream.readFloat();
        int size = objectInputStream.readInt();
        map = new HashMap<>(capacity, loadFactor);
        for (int i = 0; i < size; i++) {
            map.put((E) objectInputStream.readObject(), PRESENT);
        }
    }
}
