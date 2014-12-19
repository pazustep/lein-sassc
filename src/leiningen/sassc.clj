(ns leiningen.sassc
    (:import [java.nio.file Files FileSystems]
             [java.nio.file.attribute FileAttribute])
    (:require [leiningen.core.main :as main]))

(defn- binary [options]
  [(get options :executable "sassc")])

(defn- style [options]
  (if (:compressed options) ["--style" "compressed"]))

(defn- line-numbers [options]
  (if (:line-numbers options) ["--line-numbers"]))

(defn- load-path [options]
  (mapcat (fn [path] ["--load-path" path]) (:source-paths options)))

(defn- source-map [options]
  (if (:source-map options) ["--sourcemap"]))

(defn- omit-map-comment [options]
  (if (:omit-map-comment options) ["--omit-map-comment"]))

(defn- precision [options]
  (let [p (:precision options)]
    (if p ["--precision" p])))

(def ^:private empty-string-array
  (make-array String 0))

(def ^:private empty-attributes-array
  (make-array FileAttribute 0))

(defn- create-output-dir [^String output-to]
  (let [dir (-> (FileSystems/getDefault)
                (.getPath output-to empty-string-array)
                (.getParent))]
    (Files/createDirectories dir empty-attributes-array)))

(def ^:private command-line-fns
  [binary style line-numbers load-path source-map omit-map-comment precision])

(defn- build-command-line
  "Build the sassc command line"
  [options]
  (let [opts (mapcat #(% options) command-line-fns)
        input (:src options)
        output (:output-to options)]
    (concat opts [input output])))

(defn- run-sassc
  [command-parts]
  (apply println command-parts)
  (-> (ProcessBuilder. command-parts)
      (.start)
      (.waitFor)))

(defn- abort [message]
  (println message)
  (main/abort))

(defn- validate-options [options]
  (or (every? (partial contains? options) [:src :output-to])
    (abort ":src and :output-to keys under the :sass key are required.")))

(defn- validate [project]
  (when-let [options (:sass project)]
    (cond (map? options) (and (validate-options options) [options])
          (coll? options) (and (every? validate-options options) options))))

(defn sassc
  "I don't do a lot."
  [project & args]
  (let [configs (validate project)]
    (doseq [config configs]
      (create-output-dir (:output-to config))
      (-> config
         build-command-line
         run-sassc))))
