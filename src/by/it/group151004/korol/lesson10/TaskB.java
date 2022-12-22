package by.it.group151004.korol.lesson10;

import java.util.*;

public class TaskB<E extends Comparable<E>>  implements NavigableSet<E> {


    private class Node<T extends Comparable<T>> {
        public T value;
        public Node nodeR;
        public Node nodeL;

        public Node() {

        }
    }
    //Создайте БЕЗ использования других классов (включая абстрактные)
    //аналог дерева TreeSet

    private Node m_node;
    private int m_size;

    //Обязательные к реализации методы и конструкторы
    public TaskB() { m_node = null; m_size = 0; }

    @Override
    public boolean add(E e) {
        if (e == null)
            return false;
        Node node = new Node();
        node.value = e;
        node.nodeL = null;
        node.nodeR = null;
        if(m_node == null){
            m_node = node;
            ++m_size;
        } else {
            Node current = m_node;
            Node prev = null;
            while (true){
                if (current.value.equals(e))
                    return false;
                prev = current;
                if(e.compareTo((E) prev.value) < 0){
                    current = current.nodeL;
                    if(current==null){
                        prev.nodeL = node;
                        ++m_size;
                        return true;
                    }
                }else{
                    current = current.nodeR;
                    if(current==null){
                        prev.nodeR = node;
                        ++m_size;
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private Node getMinimumKey(Node node) {
        while (node.nodeL != null) {
            node = node.nodeL;
        }
        return node;
    }

    private boolean delete(Node node, E value) {
        Node parent = null;
        Node current = m_node;

        //Find node, which contains value, we have to remove
        while (current != null && !current.value.equals(value))
        {
            parent = current;
            if (value.compareTo((E) current.value) < 0) {
                current = current.nodeL;
            }
            else {
                current = current.nodeR;
            }
        }

        //Return false if there is no such value in tree
        if (current == null) {
            return false;
        }

        //If node is leaf
        if (current.nodeL == null && current.nodeR == null) {
            //If this leaf isn't tree root
            if (!current.equals(m_node))
            {
                if (parent.nodeL == current) {
                    parent.nodeL = null;
                }
                else {
                    parent.nodeR = null;
                }
            }

            //If it's root we can just make it null
            else {
                m_node = null;
            }
        }
//
        //Deleted node has 2 offsprings
        else if (current.nodeL != null && current.nodeR != null) {
            //Find the most left subtree element of right offspring
            Node successor = getMinimumKey(current.nodeR);

            //Remember value of that element
            E val = (E) successor.value;

            //Delete the most left element of right offspring(notice: it obviously has less than 2 offsprings)
            delete(m_node, (E)successor.value);

            //Copy value to our node
            current.value = val;
        }

        //Deleted node has 1 offspring
        else {
            Node child = (current.nodeL != null) ? current.nodeL : current.nodeR;

            //If deleted node isn't tree root
            if (!current.equals(m_node))
            {
                if (current == parent.nodeL) {
                    parent.nodeL = child;
                }
                else {
                    parent.nodeR = child;
                }
            }

            //If deleted node is root we can just assign it to child
            else {
                m_node = child;
            }
        }
        return true; //Case where we can't delete node and return false is above
    }

    @Override
    public boolean remove(Object o) { --m_size; return delete(m_node, (E) o); } //Calls recursive function

    @Override
    public boolean contains(Object o) {
        Node parent = null;
        Node current = m_node;

        //Find node, which contains value, we have to remove
        while (current != null && !current.value.equals((E) o))
        {
            parent = current;
            if (current.value.compareTo((E) o) < 0) {
                current = current.nodeR;
            }
            else {
                current = current.nodeL;
            }
        }

        return current != null;
    }

    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {

            private int current = -1;

            @Override
            public boolean hasNext() {
                return current + 1 < m_size;
            }

            private E traversal(Node node, int iterator, E returnValue) {
                if (node == null)
                    return null;
                traversal(node.nodeL, iterator, returnValue);
                ++iterator;
                if (iterator == current + 1)
                    returnValue = (E)node.value;
                traversal(node.nodeR, iterator, returnValue);
                return returnValue;
            }

            @Override
            public E next() {
                E returnValue = null;
                return traversal(m_node, 0, returnValue);
            }

            private void del(Node node, int iterator) {
                if (node == null)
                    return;
                del(node.nodeL, iterator);
                ++iterator;
                if (iterator == current)
                    delete(m_node, (E)node.value);
                del(node.nodeR, iterator);
            }

            @Override
            public void remove() {
                del(m_node, 0);
                --m_size;
            }
        };
    }

    @Override
    public void clear() { m_node = null; m_size = 0; }

    @Override
    public boolean isEmpty() { return m_node == null; }

    @Override
    public int size() {
        return m_size;
    }

    @Override
    public E first() {
        //First element is the most left
        if (m_node == null)
            return null;
        Node parent = null;
        Node current = m_node;
        while (current != null) {
            parent = current;
            current = current.nodeL;
        }

        return (E)parent.value;
    }

    @Override
    public E last() {
        //Last element is the most right
        if (m_node == null)
            return null;
        Node parent = null;
        Node current = m_node;
        while (current != null) {
            parent = current;
            current = current.nodeR;
        }

        return (E)parent.value;
    }

    private String str(Node node, StringJoiner txt) {
        if (node == null)
            return txt.toString();
        str(node.nodeL, txt);
        txt.add(node.value.toString());
        str(node.nodeR, txt);
        return txt.toString();
    }

    @Override
    public String toString() { //Calls recursive function
        StringJoiner txt = new StringJoiner(", ", "[", "]");;
        return str(m_node, txt);
    }
    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    ////////         Эти методы реализовывать необязательно      ////////////
    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    @Override
    public E lower(E e) {
        return null;
    }

    @Override
    public E floor(E e) {
        return null;
    }

    @Override
    public E ceiling(E e) {
        return null;
    }

    @Override
    public E higher(E e) {
        return null;
    }

    @Override
    public E pollFirst() {
        return null;
    }

    @Override
    public E pollLast() {
        return null;
    }


    @Override
    public Object[] toArray() {
        return new Object[0];
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return null;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        return false;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return false;
    }

    @Override
    public NavigableSet<E> descendingSet() {
        return null;
    }

    @Override
    public Iterator<E> descendingIterator() {
        return null;
    }

    @Override
    public NavigableSet<E> subSet(E fromElement, boolean fromInclusive, E toElement, boolean toInclusive) {
        return null;
    }

    @Override
    public NavigableSet<E> headSet(E toElement, boolean inclusive) {
        return null;
    }

    @Override
    public NavigableSet<E> tailSet(E fromElement, boolean inclusive) {
        return null;
    }

    @Override
    public Comparator<? super E> comparator() {
        return null;
    }

    @Override
    public SortedSet<E> subSet(E fromElement, E toElement) {
        return null;
    }

    @Override
    public SortedSet<E> headSet(E toElement) {
        return null;
    }

    @Override
    public SortedSet<E> tailSet(E fromElement) {
        return null;
    }


}
