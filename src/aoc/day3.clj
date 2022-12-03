(ns aoc.day3
  (:require [clojure.string]
            [aoc.utils]))


(def character->priority
  (reduce (fn [acc v] (assoc acc (first v) (second v))) {}
          (concat
           (for [i (range (int \a) (inc (int \z)))]
             (let [c (char i)
                   priority (- i (dec (int \a)))]
               [c priority]))

           (for [i (range (int \A) (inc (int \Z)))]
             (let [c (char i)
                   priority (+ 26 (- i (dec (int \A))))]
               [c priority])))))

(defn priority [character] (get character->priority character))

(defn prioritize-seq [s] (apply + (map priority s)))

(defn process-line-to-sack [line]
  (let [line-vec (vec line)
        len (count line-vec)
        left (vec (take (/ len 2) line-vec))
        right (vec (drop (/ len 2) line-vec))]
    (when (not= (count left) (count right)) (aoc.utils/raise (str "Uneven character count on line " line)))

    {:left left :right right}))

(defn duplicates-in-sack [sack]
  (let [left (:left sack)
        right (:right sack)
        right-contains (fn [left-char] (some #(= % left-char) right))]

    (->> left
         (filter right-contains)
         (distinct))))

(defn count-in-collection [item coll]
  (->> coll
       (filter #(= % item))
       (count)))

(defn count-across-collections [item colls]
  (->> colls
       (map (partial count-in-collection item))
       (apply +)))

(defn find-badge [group]
  (let [group-count (count group)
        distinct-group (map distinct group)
        potential-badges (distinct (first group))]

    (->> potential-badges
         (map (fn [p] [p (count-across-collections p distinct-group)]))
         (filter (fn [v] (= group-count (second v))))
         (first)
         (first))
    ))

(defn process-part-one [input]
  (->> input
       (clojure.string/split-lines)
       (map process-line-to-sack)
       (map duplicates-in-sack)
       (map prioritize-seq)
       (apply +)
       ))

(defn process-part-two [input]
  (->> input
       (clojure.string/split-lines)
       (map vec)
       (partition 3)
       (map find-badge)
       (map priority)
       (apply +)
       ))