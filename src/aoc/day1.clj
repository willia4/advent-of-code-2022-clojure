(ns aoc.day1
  (:require [ clojure.string ]
            [ aoc.utils ]))


(defn process-part-one [input]
  (->> input
       (clojure.string/split-lines)
       (aoc.utils/chunk-lines-by-blank)
       (map (fn [chunk]
              (map aoc.utils/parse-int chunk)))
       (map (fn [chunk]
              (reduce + chunk)))
       (apply max)
       (str)))

(defn process-part-two [input]
  (->> input
       (clojure.string/split-lines)
       (aoc.utils/chunk-lines-by-blank)
       (map (fn [chunk]
              (map aoc.utils/parse-int chunk)))
       (map (fn [chunk]
              (reduce + chunk)))
       (sort)
       (reverse)
       (take 3)
       (reduce +)
       (str)))
