(ns app.test
  (:require
   ["react-native-reanimated" :default animated :refer [Easing useAnimatedStyle useSharedValue withRepeat withTiming]]
;;    ["react-native-redash" :as redash :refer [loop mix] :rename {loop rloop}]
   ["react-native-redash" :as redash]
   [reagent.core :as reagent]
   [applied-science.js-interop :as j]))

(comment
  (require '["react-native-sqlite-storage" :as sqlite])
  sqlite/openDatabase

  (def sqlite (js/require "react-native-sqlite-storage"))

  (js/require "react-native-sqlite-storage")

   ; [:> blinkview {"useNativeDriver" false}
   ;  [rn/view {:style {:position :absolute :top 40 :left 86}}
   ;   [:> svg/Svg {:width 24 :height 6}
   ;    [:> svg/Rect {:x "0" :y "0" :width 48 :height 2 :fill "black"}]]]]
   ; [rn/view {:style {:position :absolute :top 140 :left 81}}
   ;  [:> svg/Svg {:width 45 :height 20}
   ;   [:> svg/Circle {:cx "5" :cy "5" :r "5" :fill "black"}]
   ;   [:> svg/Rect {:x "10" :y "4.5" :width 25 :height 2 :fill "black"}]]]
   ; [rn/view {:style {:position :absolute :top 150 :left 81}}
   ;  [:> svg/Svg {:width 45 :height 20}
   ;   [:> svg/Circle {:cx "40" :cy "5" :r "5" :fill "black"}]
   ;   [:> svg/Rect {:x "10" :y "4.5" :width 25 :height 2 :fill "black"}]]]

   ; (text/flat-list-text {:width 26 :fill "black" :color "black" :height 500 :font :white :font-size 18} font/mlongstr)]])

   ; [text/text-area {:line-height 26 :fill "black" :color "black"}
   ;   (text/text-component {:width 26 :height 500 :font :white :font-size 18} font/mlongstr)]]])

   ; [rn/view {:flex 1 :height 400 :width 40}
   ;  [rn/scroll-view {:style {:height 400}
   ;                   :showsVerticalScrollIndicator true
   ;                   :showsHorizontalScrollIndicator true}
   ;                   ; :contentContainerStyle {:flex 1}}
   ;   [text/text-line {:height 5000 :width 26 :fill "black" :color "black" :flex 1}
   ;     (nth (text/text-component {:width 26 :height 5000 :font :white :font-size 18} font/mlongstr) 0)]]]]])
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
