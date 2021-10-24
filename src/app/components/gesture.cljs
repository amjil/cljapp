(ns app.components.gesture
  (:require
    [reagent.core :as r]
    [steroid.rn.core :as rn]
    ["react-native-gesture-handler" :refer [PanGestureHandler TapGestureHandler State]]
    [app.font.base :as font]
    [app.text.index :as text]
    [applied-science.js-interop :as j]))

(def pan-gesture-handler (r/adapt-react-class PanGestureHandler))

(def tap-gesture-handler (r/adapt-react-class TapGestureHandler))

(def state State)

(defn tap-state-end [evt]
  (=  (j/get state :END)
      (j/get evt :state)))
; [text/text-area {:line-height 26 :fill "black" :color "black"}
;   (text/text-component {:width 26 :height 500 :font :white :font-size 18} font/mlongstr)]]])

(defn pan-gesture []
  [pan-gesture-handler {:onGestureEvent #(js/console.log (j/get % :nativeEvent))}
   [rn/view {}
    [rn/text {} "abc"]]])

(comment
  (j/get State :ACTIVE)
  State)
