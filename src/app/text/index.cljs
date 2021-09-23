(ns app.text.index
  (:require
   [reagent.core :as reagent]
   [app.font.base :as font]
   [clojure.string :as str]))

(defn word-component [word]
  nil)

;; (.codePointAt % 0)
;; (char %)
(defn text-component [line-height text]
  (let [x (reagent/atom 0)
        y (reagent/atom 0)
        text-list (str/split text #" ")
        glyphs (map #(get-glyphs :white %) text-list)]))
