(ns aoc.day0
  (:require [ clojure.string ]
            [ aoc.utils ]))

(defn process-part-one [input]
  (->> (identity input)
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
  (->> (identity input)
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

(defn run
  [run-type]
  (let [day-name "day0"
        input-name (case run-type :test-data "test" :real-data "real")
        input-path (format "aoc-input/%s.%s.txt" day-name input-name)
        input (slurp input-path)]
    (println (format "### Day 0 - Part 1 - %s" input-path))
    (process-part-one input)
    (println "")
    (process-part-two input)
    (println "")))