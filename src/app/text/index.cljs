(ns app.text.index
  (:require
   [reagent.core :as reagent]
   [app.font.base :as font]
   [clojure.string :as str]
   [applied-science.js-interop :as j]))

(defn word-component [word]
  nil)

;; (.codePointAt % 0)
;; (char %)
(defn text-component [w h text]
  (let [x (reagent/atom 0)
        y (reagent/atom 0)
        
        text-list (str/split text #" ")
        glyphs (map #(get-glyphs :white %) text-list)
        widths (map (fn [x] (map #(width :white 24 %) x) glyhphs))]))

(comment
  (prn 'Hi)
  font/fonts
  (-> 
   (font/get-glyphs :white font/mstr)
   last
   (j/get :codPoints) )

)