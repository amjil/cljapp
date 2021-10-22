(ns app.core
  (:require
   [steroid.rn.core :as rn]
   [steroid.rn.navigation.safe-area :as safe-area]
   [steroid.rn.components.touchable :as touchable]
   [steroid.rn.components.ui :as rn-ui]
   [reagent.core :as reagent]
   ["react-native-svg" :as svg]
   [applied-science.js-interop :as j]
   [cljs-bean.core :refer [->clj ->js]]
   [goog.crypt.base64 :as b64]
   [app.font.base :as font]
   [app.text.index :as text]
   [clojure.string :as str]
   ["react-native-smooth-blink-view" :default blinkview]))

(defn root-comp []
  [safe-area/safe-area-view
   [rn/view {:style {:flexDirection "column"
                     :height "100%"
                     :width "100%"}}
    [rn/text {:style {:backgroundColor "green"}} "Hello CLojure! from CLJS "]
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

    [rn/view {:flex 1 :height 400 :width 40}
     [rn/scroll-view {:style {:height 400}
                      :showsVerticalScrollIndicator true
                      :showsHorizontalScrollIndicator true
                      :contentContainerStyle {:flex 1}}
      [rn/view {:flex 1}
       [text/text-line {:width 26 :fill "black" :color "black" :flex 1}
         (nth (text/text-component {:width 26 :height 5000 :font :white :font-size 18} font/mlongstr) 0)]]]]]])




(defn init []
  (font/init)

  (rn/register-comp "cljapp" root-comp))



(comment
  (require '[promesa.core :as p])
  @font/fonts
  (js/console.log "hahah"))
