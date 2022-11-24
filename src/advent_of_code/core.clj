(ns advent-of-code.core
  (:gen-class)
  (:require [aoc.day0]
            [aoc.day1]
            [aoc.day2]))

(defn -main
  "Runs the callenges"
  [& args]
  (aoc.day0/run :test-data)
  (aoc.day0/run :real-data)
  ;; (aoc.day1/run :real-data)
  ;; (aoc.day2/run :test-data)
)
