package com.example.cheatsheets.cheatsheets.collections

import java.util.ArrayList
import java.util.LinkedList
import java.util.PriorityQueue
import java.util.Vector
import java.util.HashSet
import java.util.TreeMap
import java.util.TreeSet

/**
 * Collection is a set of interfaces and classes within the java.util package
 * designed to represent and manipulate groups of objects
 *
 * Collection -> List, Set, Queue and separate Map
 */


/**
 * 1. List -> ArrayList, LinkedList, Vector
 *
 * List is interface and collection.
 * It represents an ordered collection of elements, meaning that the elements
 * are stored and retrieved in a specific sequence, typically the order in which they were added.
 */
// Add (end) - O(1) if it is not full, O(n) if it is (*1,5 + 1 new array)
// Add (start/middle) - O(n) for shifting the elements
// Remove (end) - O(1) ( in java it does not copy array in java they will check weather the array index is end.)
// Remove (start/middle) - O(n) for shifting the elements
// Get - O(1)
//
// So the power is Get and Disadvantage is Add/Remove operations
val arrayList = ArrayList<Int>() // Initial 10 elements with Nulls

// It implements both the List and Deque interfaces, making it a versatile data structure.
// It is based on a doubly-linked list data structure. This means each element,
// referred to as a "node," stores its own data and holds references (or pointers)
// to both the next node and the previous node in the sequence.
//
// Add/Remove (start/end) - O(1)
// Add/Remove (middle) - O(n)
// Get (start/end) - O(1)
// Get (middle) - O(n)
//
// So the power is Get/Add/Remove at the end and start
// and Disadvantage is any other operations - O(n)
val linkedList = LinkedList<Int>()

// In Java, a Vector is a legacy class within the java.util package that implements
// the List interface. It represents a dynamic array, meaning it can grow or shrink
// in size as elements are added or removed, unlike a fixed-size array.
val vector = Vector<Int>()

/**
 * 2. Queue -> Deque -> LinkedList, ArrayDeque
 *          -> PriorityQueue
 *
 *  represents a collection designed for holding elements prior to processing.
 *  It adheres to the First-In, First-Out (FIFO) principle, meaning that elements are
 *  retrieved from the queue in the same order they were added.
 *
 *  boolean offer(E obj): добавляет элемент obj в конец очереди. Если элемент удачно добавлен, возвращает true, иначе - false
 *  E poll(): возвращает с удалением элемент из начала очереди. Если очередь пуста, возвращает значение null
 */
val arrayDeque = ArrayDeque<Int>()
val priorityQueue = PriorityQueue<Int>()

/**
 * 3. Set -> HashSet, LinkedHashSet, TreeSet
 *
 * Set – это интерфейс, представляющий собой коллекцию уникальных элементов.
 * Он не допускает дубликатов и не имеет индексов. Используется, когда нужно быстро проверять наличие элемента.
 */

// HashSet: основан на HashMap. Не гарантирует порядок.
// Add/Remove/Contains – O(1) в среднем, но O(n) в худшем при коллизиях
// Уникальность обеспечивается через hashCode() и equals()
// Порядок не сохраняется
val hashSet = HashSet<Int>()

// LinkedHashSet: основан на HashMap + двусвязный список.
// Сохраняет порядок добавления элементов
// Add/Remove/Contains – O(1) в среднем
val linkedHashSet = LinkedHashSet<Int>()

// TreeSet: основан на TreeMap, использует красно-чёрное дерево
// Элементы хранятся в отсортированном порядке
// Add/Remove/Contains – O(log n)
// Требует Comparable или Comparator
val treeSet = TreeSet<Int>()

/**
 * 4. Map -> HashMap, LinkedHashMap, TreeMap
 *
 * Map – это интерфейс коллекции, сопоставляющий ключи и значения (key-value).
 * Каждый ключ уникален. Часто используется для быстрого поиска по ключу.
 */

// HashMap: основа – массив + цепочки (или дерево при коллизиях).
// Быстрый доступ по ключу
// Put/Get/Remove – O(1) в среднем, O(n) в худшем случае (например, при плохой hash-функции)
// Порядок хранения не гарантируется
val hashMap = HashMap<String, Int>()

// LinkedHashMap: HashMap + двусвязный список
// Сохраняет порядок вставки (или порядок доступа, если включен accessOrder)
// Put/Get/Remove – O(1)
val linkedHashMap = LinkedHashMap<String, Int>()

// TreeMap: основан на красно-чёрном дереве
// Хранит пары в отсортированном порядке по ключу
// Put/Get/Remove – O(log n)
// Требует Comparable или Comparator для ключей
val treeMap = TreeMap<String, Int>()

/**
 * Kotlin collections
 *
 * 1. Иммутабельные коллекции это просто рилонли интерфейс
 * List, MutableList и ArrayList — это всё java.util.ArrayList
 *
 * Различие только в интерфейсе и compile-time проверке:
 *
 * List в Kotlin = readonly interface
 *
 * MutableList = interface с mutator-функциями (add, remove)
 *
 * 2. Из пункта 1 следует, что List по настоящему не иммутабельный и в обход проверки типов
 * мы можем изменять список. Чтобы по-настоящему надо использовать kotlinx.collections.immutable -
 * persistentListOf()
 */