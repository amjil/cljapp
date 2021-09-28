(ns app.text.index
  (:require
   [reagent.core :as reagent]
   [app.font.base :as font]
   [clojure.string :as str]
   ["react-native-svg" :as svg]
   [cljs-bean.core :refer [bean ->clj ->js]]
   [applied-science.js-interop :as j]))

(defn in-current-line? [runs h y space-length]
  (let [runs-length (apply + (map :width runs))]
    (>= (- h y) (+ space-length runs-length))))

(defn into-line [runs line num y]
  "line y is atom"
  (for [run runs]
    (let [width (:width run)
          run-y (assoc run :y @y :line @num)]
      (swap! line conj run-y)
      (reset! y (+ @y width)))))
;; (.codePointAt % 0)
;; (char %)
;; var px = evt.nativeEvent.localtionX / PixelRatio.get();
;; if one word longer then height
(defn text-component [w h text]
  (let [line                     (reagent/atom [])
        line-num (reagent/atom 0)
        y                        (reagent/atom 0)
        [space-width line-width] (font/space-dimention :white 24)
        space-run                {:svg "" :width space-width :code-poinst [32]}
        text-list                (str/split text #" ")
        text-runs                (map #(font/run :white 24 %) text-list)]
    (loop [lines    []
           run-runs text-runs]
      (if (empty? run-runs)
        (conj lines @line)
        (let [runs (first run-runs)]
          (if (true? (in-current-line? runs h @y space-width))
            (do
              (doall (into-line (concat runs [space-run]) line line-num y))
              (recur lines (rest run-runs)))
            (let [item @line]
              (reset! line [])
              (reset! y 0)
              (recur (conj lines (flatten item))
                     run-runs))
            ))))
    ))

    ; (map #(let [[x y] (next-position)]
    ;         vector :> svg/Path {:d (:svg %)
    ;                             :x x
    ;                             :y y}))))

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
  (next-position runs)
  (in-current-line? (font/run :white 24 font/mstr) 500 490 6)
  (text-component 0 500 font/mlongstr)
  (font/space-dimention :white 24)
  
  (-> (str/split font/mlongstr #" ") m
       (map #(font/run :white 24 %) m)
       (nth m 16))     
  )
