(ns aoc.day0
  (:require [ clojure.string ]
            [ aoc.utils ]))

(defn process-part-one [input]
  (->> input
       (clojure.string/split-lines)
       (map aoc.utils/parse-int)
       (partition 2 1)
       (map (fn [[a b]]
              (if (> b a)
                :descending
                :not-descending)))
       (filter #(= :descending %))
       (count)
       (format "Total Descent Count: %d")
       (println)))

(defn process-part-two [input]
  (->> input
       (clojure.string/split-lines)
       (map aoc.utils/parse-int)
       (partition 3 1)
       (map (fn [[a b c]] (+ a b c)))
       (partition 2 1)
       (map (fn [[a b]]
              (if (> b a)
                :descending
                :not-descending)))
       (filter #(= :descending %))
       (count)
       (format "Total Window Descent Count: %d")
       (println)))
