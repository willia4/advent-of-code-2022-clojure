(ns aoc.day5
  (:require [clojure.string]
            [aoc.utils]))

(defn split-input
  "Given the puzzle input as a string, returns a 2-vector.
   Each item in the vector is a vector of strings.
   The first item contains the lines for the crate setup.
   The second input contains the lines for the instructions."
  [input]
  (let [parts (clojure.string/split input #"\n\n")
        crate-part (get parts 0)
        instructions-part (get parts 1)]
    [(clojure.string/split-lines crate-part)
     (clojure.string/split-lines instructions-part)]))

(defn process-crate-line
  "Given a line of crates from the puzzle input, returns a vector of
   crate labels represented by that line. Each entry in the vector will
   contain a character (the label) or nil (if there was no crate in that spot)"
  [crate-line]
  (loop [ crates [] remaining-input crate-line ]
    (if (= 3 (count remaining-input))
      ;; the last crate doesn't have a space at the end so there are just three
      ;; characters remaining; thus, this is the last crate
      (let [crate-label (nth remaining-input 1)
            next-crate (if (= \space crate-label) nil crate-label)
            next-crates (into crates [next-crate])]
        next-crates)

      ;; there are more crates so we'll need to recur
      ;; since this is not the last crate, it takes up 4 spaces so
      ;; drop that many on the next iteration
      (let [crate-label (nth remaining-input 1)
            next-crate (if (= \space crate-label) nil crate-label)
            next-crates (into crates [next-crate])
            next-input (drop 4 remaining-input)]
        (recur next-crates next-input)))))

(defn crates-for-stack
  "Takes the crate rows from process-crate-line (in reverse order from
   the input order so the bottom crate is at position 0) and the
   1-based stack index. Returns a vector of crates in that stack."
  [reversed-crate-rows stack-index]
  (vec (for [row reversed-crate-rows
             :let [c (nth row (dec stack-index))]
             :when (not (nil? c))]
         c)))

(defn process-crate-input
  "Takes the crate part of the puzzle input and returns a map of
   stack numbers to vectors of crates"
  [crate-part]
  ;; some good news: there are no more than 9 crates so the
  ;; entries in the last line (containing the crate indexes)
  ;; are all the same width since they're all single digits
  ;;
  ;; more good news: the crate labels only go up to Z so they
  ;; are also all single-character.
  ;; That means that a single column cannot be more than 26 crates
  ;; high and the width cannot be more than 9 stacks wide
  ;; (it starts at 1, not 0)
  ;;
  ;; final bit of good news: there are spaces to represent each
  ;; crate so the lines are all the same length
  ;;
  ;; the puzle gods were kind

  (let [index-line (last crate-part)
        crate-lines (take (dec (count crate-part)) crate-part)
        max-index (as-> index-line v
                    (clojure.string/split v #"\s+")
                    (filter #(not= "" %) v)
                    (map aoc.utils/parse-int v)
                    (apply max v))
        crate-rows (->> crate-lines
                        (map process-crate-line))
        reversed-crate-rows (reverse crate-rows)]

    (into {}
          (for [stack-index (range 1 (inc max-index))
                :let [stack-crates (crates-for-stack reversed-crate-rows stack-index)]]
            {stack-index stack-crates}))))

(defn process-instructions-input
  "Takes the instructions part of the puzzle input and returns a vector of maps with
   each map representing a single move instruction with keys :from, :to, and :amount"
  [instructions-part]
  (->> instructions-part
       (map (partial re-matches #"move (\d+?) from (\d+?) to (\d+?)"))
       (map (fn [m] (drop 1 m)))
       (map (fn [m] (map aoc.utils/parse-int m)))
       (map (fn [m]
              {:amount (nth m 0)
               :from (nth m 1)
               :to (nth m 2)}))
       (vec)))

(defn move-crate
  "Takes a stacks-map (a map from ints (stack indexes) to vecs (of crates)) and the
   from and to indexes (whcih must be keys in stacks-map).
   Returns a new stacks-map with a single crate moved from from to to."
  [stacks-map move-count from to]
  (let [from-stack (stacks-map from)
        to-stack (stacks-map to)
        crates (vec (take-last move-count from-stack))
        new-from-stack (vec (drop-last move-count from-stack))
        new-to-stack (into to-stack crates)]
    (assoc stacks-map from new-from-stack to new-to-stack)))

(defn process-instruction-part-one
  [stacks-map instruction]

  (loop [stacks-map stacks-map
         remaining-moves (:amount instruction)]
    (if (= 0 remaining-moves)
      stacks-map
      (recur (move-crate stacks-map 1 (:from instruction) (:to instruction))
             (dec remaining-moves)))))

(defn process-instruction-part-two
  [stacks-map instruction]

  (move-crate stacks-map (:amount instruction) (:from instruction) (:to instruction)))

(defn process-instructions
  [stacks-map instructions processor]
  (loop [stacks-map stacks-map
         instructions instructions]
    (if (empty? instructions)
      stacks-map
      (recur
       (processor stacks-map (first instructions))
       (drop 1 instructions)))))

(defn tops-of-stacks
  [stacks-map]
  (loop [result ""
         remaining-indexes (sort (keys stacks-map))]
    (let [stack (stacks-map (first remaining-indexes))
          top-of-stack (last stack)]
      (if (empty? remaining-indexes)
        result
        (recur (str result top-of-stack)
               (rest remaining-indexes))))))

(defn process-part-one [input]
  (let [parts (split-input input)
        stack-map (process-crate-input (first parts))
        instructions (process-instructions-input (last parts))
        processed-stack-map (process-instructions stack-map instructions process-instruction-part-one)]

    (tops-of-stacks processed-stack-map)))

(defn process-part-two [input]
  (let [parts (split-input input)
        stack-map (process-crate-input (first parts))
        instructions (process-instructions-input (last parts))
        processed-stack-map (process-instructions stack-map instructions process-instruction-part-two)]

    (tops-of-stacks processed-stack-map)))