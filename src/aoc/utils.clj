(ns aoc.utils
  (:require [clojure.java.io]))

(defn parse-int [ s ] (Integer/parseInt s))

(defn read-file-lines
  "Reads a seq of lines from the file at path and passes that seq to the function f"
  [ path f ]
  (with-open [rdr (clojure.java.io/reader path)]
    (doall (f (line-seq rdr)))))