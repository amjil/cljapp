(ns app.core
  (:require
   [steroid.rn.core :as rn]
   [steroid.rn.navigation.safe-area :as safe-area]
   [reagent.core :as reagent]
   ["react-native-svg" :as svg]
   [applied-science.js-interop :as j]
   [cljs-bean.core :refer [->clj ->js]]
   [goog.crypt.base64 :as b64]
   [app.font.base :as font]
   [app.text.index :as text]
   ["react-native-smooth-blink-view" :default blinkview]))

(defn root-comp []
  (let [w (reagent/atom 200)]
    [safe-area/safe-area-view
     [rn/view {:style {:flexDirection "column"
                       :height "100%"
                       :width "100%"}
               :on-layout #(do (js/console.log "on layout !!!!")
                               (reset! w (-> %
                                           (j/get :nativeEvent)
                                           (j/get :layout)
                                           (j/get :height)))
                               (js/console.log
                                 (-> %
                                   (j/get  :nativeEvent)
                                   (j/get :layout)
                                   (j/get :height))))}
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
      ; (text/text-area (text/text-component 0 500 :white 24 font/mlongstr) {:line-height 40 :fill "blue" :color "black" :top 0 :left 0})]])
      ; (text/flat-list-text {:width 26 :fill "black" :color "black"} (text/text-component 0 @w :white 18 font/mlongstr))
      (text/flat-list-text {:width 26 :fill "black" :color "black" :height 500 :font :white :font-size 18} font/mlongstr)]]))



(defn init []
  (font/init)

  (rn/register-comp "cljapp" root-comp))



(comment
  (require '[promesa.core :as p])
  @font/fonts
  (js/console.log "hahah"))
