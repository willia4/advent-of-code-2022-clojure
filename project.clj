(defproject advent-of-code "0.1.0-SNAPSHOT"
  :description "Clojure implementation of some of the exercises for Advent of Code 2022"
  :url "https://github.com/willia4/advent-of-code-2022-clojure"
  :dependencies [[org.clojure/clojure "1.10.1"]]
  :main ^:skip-aot advent-of-code.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
