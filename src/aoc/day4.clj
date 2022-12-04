(ns aoc.day4
  (:require [clojure.string]
            [aoc.utils]))

(defn parse-range [the-range]
  (let [m (re-matches #"(\d+?)-(\d+?)" the-range)
        start (aoc.utils/parse-int (get m 1))
        end (aoc.utils/parse-int (get m 2))]
    (when (< end start) (aoc.utils/raise (format "Out of order range %s" the-range)))
    {:start start :end end}))

(defn fully-contains
  "Returns truthy if a is fully contained by b"
  [a b]
  (and (>= (:start a) (:start b)) (<= (:end a) (:end b))))

(defn either-fully-contains
  "Takes a 2-count vector of pseudo-ranges.
   Returns true if either is fully contained by the other"
  [ranges]
  (or (fully-contains (first ranges) (second ranges))
      (fully-contains (second ranges) (first ranges))))

(defn overlaps
  "Returns truthy if any part of a is in b"
  [a b]
  (or (and (>= (:start a) (:start b))
           (<= (:start a) (:end b)))
      (and (>= (:end a) (:start b))
           (<= (:end a) (:end b)))))

(defn either-overlaps
  "Takes a 2-count vector of pseudo-ranges.
   Returns true if either is partially contained by the other"
  [ranges]
  (or (overlaps (first ranges) (second ranges))
      (overlaps (second ranges) (first ranges))))

(defn process-line [line]
  (->> (clojure.string/split line #",")
       (map parse-range)
       vec)
  )
(defn process-part-one [input]
  (->> input
       (clojure.string/split-lines)
       (map process-line)
       (filter either-fully-contains)
       count))

(defn process-part-two [input]
  (->> input
       (clojure.string/split-lines)
       (map process-line)
       (filter either-overlaps)
       count))