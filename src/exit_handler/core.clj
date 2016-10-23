(ns exit-handler.core
  (:gen-class)
  (:require [clojure.tools.cli :refer [cli]]))
  

(defn read-from-stdin
  []
  ;; @see http://stackoverflow.com/a/2034175
  (doseq [ln (line-seq (java.io.BufferedReader. *in*))]
    (println ln)))


;; @see http://stackoverflow.com/a/11709814
;; @see http://stackoverflow.com/a/10863953 - use lein trampoline run
(defn setup-hook
  []
  (.addShutdownHook (Runtime/getRuntime) (Thread. (fn [] (println "Shutting down..."))))
)

(defn exit-function
  []
  (println "\n\nInside `exit-function` before final exit.\n\n"))


(defn shutdown-hook
  [fn]
  (let [shutdown-thread (new Thread fn)]
    (.. Runtime (getRuntime) (addShutdownHook shutdown-thread))))


(defn -main 
  [& args]
  ;; setup `exit-function` to be called during program termination
  ;;
  ;; Note: if testing with lein use `lein trampoline run` otherwise lein will trap the Ctrl-C
  (shutdown-hook exit-function)

  ;; explain to the user how to exit
  (println "Enter miscellaneous text. Each line will be repeated.")
  (println "Exit program with Ctrl-C or Ctrl-D")
  
  ;; enter read from stdin routine
  (read-from-stdin)
)
