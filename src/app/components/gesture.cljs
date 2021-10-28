(ns app.components.gesture
  (:require
    [reagent.core :as r]
    [steroid.rn.core :as rn]
    ["react-native-gesture-handler" :refer [PanGestureHandler TapGestureHandler FlingGestureHandler State]]
    [app.font.base :as font]
    [app.text.index :as text]
    [applied-science.js-interop :as j]))

(def pan-gesture-handler (r/adapt-react-class PanGestureHandler))

(def tap-gesture-handler (r/adapt-react-class TapGestureHandler))

(def fling-gesture-handler (r/adapt-react-class FlingGestureHandler))

(def state State)

(defn tap-state-end [evt]
  (=  (j/get state :END)
      (j/get evt :state)))

(defn pan-gesture []
  [pan-gesture-handler {:onGestureEvent #(js/console.log (j/get % :nativeEvent))}
   [rn/view {}
    [rn/text {} "abc"]]])

(comment
  (j/get State :ACTIVE)
  State)
