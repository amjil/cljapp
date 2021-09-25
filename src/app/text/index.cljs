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

;; (def inner-size 0)
;; (def font (font/get-font :white))
;; (def word font/mstr)
;; (require '[cljs-bean.core :refer [bean ->clj ->js]])
;; (def glyphs     (-> font
;;                     (j/call :layout word)
;;                     (j/get :glyphs)
;;                     (->clj)))
;; (def glyph (first glyphs))
(comment
  (prn 'Hi)
  font/fonts
  (font/run :white 24 font/mstr)
  
  )
