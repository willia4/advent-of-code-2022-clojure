(ns aoc.utils
  (:require [clojure.java.io]))

(defn parse-int [ s ] (Integer/parseInt s))

(defn input-path
  "Determines the path for a specific piece of test input"
  [day run-type]
  (let [run-type-component (case run-type
                             :test-data "test"
                             :real-data "real")
        day-component (str day)
        path (format "aoc-input/day%s.%s.txt" day-component run-type-component)]
    path))

(defn read-input
  "Reads input for day and test-type"
  [day run-type]
  (let [path (input-path day run-type)]
    (slurp path)))


(defn run-day
  ([day run-type part1] (run-day day run-type part1 nil))
  ([day run-type part1 part2]
  (let [path (input-path day run-type)
        data (read-input day run-type)]
    (when part1

      (println (format "### Day %d - Part 1 - %s" day path))
      (part1 data)
      (println ""))

    (when part2
      (println (format "### Day %d - Part 2 - %s" day path))
      (part2 data)
      (println ""))
    )))