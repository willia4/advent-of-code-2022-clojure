(ns advent-of-code.core
  (:gen-class)
  (:require
   [aoc.utils]
   [aoc.day0] [aoc.day1] [aoc.day2] [aoc.day3] [aoc.day4] [aoc.day5]))

(def all-days (vec (range 0 (inc 5))))

(defn run-test
  ([day]
   (run-test day :test-data)
   (run-test day :real-data))

  ([day run-type]
   (let [test-namespace-name (format "aoc.day%d" day)
         test-namespace (find-ns (symbol test-namespace-name))
         process-part-one (resolve (symbol (format "%s/process-part-one" test-namespace-name)))
         process-part-two (resolve (symbol (format "%s/process-part-two" test-namespace-name)))]

     (when (nil? test-namespace) (aoc.utils/raise (format "Could not find namespace %s" test-namespace-name)))
     (when (nil? process-part-one) (aoc.utils/raise (format "Could not find process-part-one in namespace %s" test-namespace-name)))

     (if (and (not (nil? process-part-one)) (not (nil? process-part-two)))
       (aoc.utils/run-day day run-type process-part-one process-part-two)
       (when (not (nil? process-part-one))
         (aoc.utils/run-day day run-type process-part-one))))))

(defn parse-arg
  [arg]
  (let [i (aoc.utils/try-parse-int arg)]
    (cond
      (= "test" arg) {:type :run-type :value :test-data}
      (= "real" arg) {:type :run-type :value :real-data}
      (= "both" arg) {:type :run-type :value :both}
      (or (= "last" arg) (= "latest" arg)) {:type :day :value (apply max all-days)}
      (some? i) {:type :day :value i}
      :else (aoc.utils/raise (str "Could not parse argument " arg)))))

(defn parse-args
  [args]
  (let [parsed (->> args
                    (map parse-arg)
                    (reduce (fn [acc v]
                              (cond
                                (= (:type v) :run-type) (assoc acc :run-type (:value v))
                                (= (:type v) :day)
                                (let [days (:days acc)
                                      new-days (conj days (:value v))]
                                  (assoc acc :days new-days))
                                :else (aoc.utils/raise (str "Could not handle argument " v)))) {:days [] :run-type :both}))]
    (if (empty? (:days parsed))
      (assoc parsed :days all-days)
      parsed)))

(defn -main
  "Runs the callenges"
  [& args]
  (let [parsed-args (parse-args args)
        run-type (:run-type parsed-args)]

    (loop [days (:days parsed-args)]
      (when (seq days)
        (let [day (first days)]
          (when (or (= run-type :test-data) (= run-type :both)) (run-test day :test-data))
          (when (or (= run-type :real-data) (= run-type :both)) (run-test day :real-data))
          (recur (rest days)))))))
