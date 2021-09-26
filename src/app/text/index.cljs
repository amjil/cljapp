(ns app.text.index
  (:require
   [reagent.core :as reagent]
   [app.font.base :as font]
   [clojure.string :as str]
   ["react-native-svg" :as svg]
   [applied-science.js-interop :as j]))

(defn next-position [runs line-width h x y space]
  (let [runs-length (apply + (mapp :width runs))]
    (if (>= (- h y) runs-length)
      [x (+ y runs-length space)]
      [(+ x line-width) (+ runs-length space)])))

;; (.codePointAt % 0)
;; (char %)
(defn text-component [w h text]
  (let [x (reagent/atom 0)
        y (reagent/atom 0)

        space-length (-> (font/run :white 24 " ") first :width)
        text-list (str/split text #" ")
        text-runs (map #(font/run :white 24 %) text-list)]
    (map #(let [[x y] (next-position )]
            vector :> svg/Path {:d (:svg %) :x x :y y}))))

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
  (font/run :white 24 font/mstr))
