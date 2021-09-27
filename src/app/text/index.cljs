(ns app.text.index
  (:require
   [reagent.core :as reagent]
   [app.font.base :as font]
   [clojure.string :as str]
   ["react-native-svg" :as svg]
   [applied-science.js-interop :as j]))

(defn in-current-line? [runs h y]
  (let [runs-length (apply + (map :width runs))]
    (if (>= (- h y) runs-length)
      true
      false)))

;; (.codePointAt % 0)
;; (char %)
;; var px = evt.nativeEvent.localtionX / PixelRatio.get();
(defn text-component [w h text]
  (let [x (reagent/atom 0)
        y (reagent/atom 0)

        [space-width line-width] (font/space-dimention :white 24)
        text-list (str/split text #" ")
        text-runs (map #(font/run :white 24 %) text-list)]
    (map #(let [[x y] (next-position)]
            vector :> svg/Path {:d (:svg %) :x x :y y}))))

;; (def inner-size 0)
(def font (font/get-font :white))
(def word font/mstr)
;; (require '[cljs-bean.core :refer [bean ->clj ->js]])
;; (def glyphs     (-> font
;;                     (j/call :layout word)
;;                     (j/get :glyphs)
;;                     (->clj)))
;; (def glyph (first glyphs))
(def runs (font/run :white 24 font/mstr))
(comment
  (prn 'Hi)
  font/fonts
  (js/console.log (:white @font/fonts))
  (font/run :white 24 font/mstr)
  (-> (font/run :white 24 " ") first :width)
  (next-position runs))
