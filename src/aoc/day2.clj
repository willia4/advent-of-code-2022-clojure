(ns aoc.day2 
  (:require [clojure.string]
            [aoc.utils]))

(defn score-throw [move-throw]
  (case move-throw
    :rock 1
    :paper 2
    :scissors 3
    (aoc.utils/raise (str "Invalid move-throw " move-throw))))

(def score-loss 0)
(def score-draw 3)
(def score-win 6)

(defn score-move [move]
  (let [p1 (:p1 move)
        p2 (:p2 move)]
    (cond
      (= p1 p2) score-draw
      (and (= p1 :rock) (= p2 :paper)) score-win
      (and (= p1 :rock) (= p2 :scissors)) score-loss

      (and (= p1 :paper) (= p2 :rock)) score-loss
      (and (= p1 :paper) (= p2 :scissors)) score-win

      (and (= p1 :scissors) (= p2 :rock)) score-win
      (and (= p1 :scissors) (= p2 :paper)) score-loss
      :else (aoc.utils/raise (str "Unexpected scenario " p1 " " p2)))))

(defn score-round [move]
  (+ (score-throw (:p2 move)) (score-move move)))

(defn process-p2-move-part1 [p1move p2symbol]
  (case p2symbol
    "X" :rock
    "Y" :paper
    "Z" :scissors
    (aoc.utils/raise (str "Could not determine player 2 move from " p2symbol))))

(defn pick-winning-move [p1move]
  (case p1move
    :rock :paper
    :paper :scissors
    :scissors :rock))

(defn pick-losing-move [p1move]
  (case p1move
    :rock :scissors
    :paper :rock
    :scissors :paper))

(defn pick-drawing-move [p1move] p1move)

(defn process-p2-move-part2 [p1move p2symbol]
  (case p2symbol
    "X" (pick-losing-move p1move)
    "Y" (pick-drawing-move p1move)
    "Z" (pick-winning-move p1move)))

(defn process-line [p2-move-processor line]
  (let [splits (clojure.string/split line #" ")
        p1 (first splits)
        p2 (second splits)]
    (when (not= (count splits) 2) (aoc.utils/raise (str "Could not split line " line)))
    (when (nil? p1) (aoc.utils/raise (str "Could not determine first part of line " line)))
    (when (nil? p2) (aoc.utils/raise (str "Could not determine second part of line " line)))

    (let [p1-move (case p1
                    "A" :rock
                    "B" :paper
                    "C" :scissors
                    (aoc.utils/raise (str "Could not determine player 1 move from " p1)))]

      { :p1 p1-move :p2 (p2-move-processor p1-move p2)})
    ))

(defn process-part-one [input]
  (->> input
       (clojure.string/split-lines)
       (map (partial process-line process-p2-move-part1))
       (map score-round)
       (reduce +)))

(defn process-part-two [input]
  (->> input
       (clojure.string/split-lines)
       (map (partial process-line process-p2-move-part2))
       (map score-round)
       (reduce +)))