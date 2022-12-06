(ns aoc.day6
  (:require [ aoc.utils ]
            [clojure.string ]))

(defn all-unique
  "Takes a sequence and returns truthy if all items in the sequence are unique"
  [s]
  (let [s' (seq s)
        s'' (set s)]
    (= (count s') (count s''))))

(defn find-marker
  [len input]
  (->> input
       (partition len 1)
       (keep-indexed (fn [idx v] (when (all-unique v) idx)))
       (first)
       (+ len)))

(defn process-part-one [input]
  (find-marker 4 input))

(defn process-part-two [input]
  (find-marker 14 input))