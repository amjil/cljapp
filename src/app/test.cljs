(ns app.test
  (:require
   ["react-native-reanimated" :default animated :refer [Easing useAnimatedStyle useSharedValue withRepeat withTiming]]
;;    ["react-native-redash" :as redash :refer [loop mix] :rename {loop rloop}]
   ["react-native-redash" :as redash]
   [reagent.core :as reagent]
   [applied-science.js-interop :as j]))



(comment

  (reagent/adapt-react-class (.-View animated))



 (def animated-view (.-View animated))
 animated-view

 (useSharedValue 0)

 Easing
 loop

 withRepeat
 withTiming
 useAnimatedStyle
 useSharedValue
 mix
 rloop
 loop
 redash/mix
 redash
 (def easing-opacity (withRepeat
                      (withTiming 1 #js {:duration 1000, :easing (.-ease Easing)})
                      (- 1)
                      true))

 (useAnimatedStyle (fn [] #js {:opacity easing-opacity}))

 (def style (useAnimatedStyle (fn [] #js {:opacity easing-opacity}) #js [])))
