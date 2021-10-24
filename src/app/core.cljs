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
   [app.components.gesture :as gesture]
   ["react-native-smooth-blink-view" :default blinkview]

   [app.ui.views :as views]
   app.events
   app.subs))

(defn root-comp []
  (let [text-svgs (text/text-component {:width 32 :fill "gray" :color "black" :height 200 :font :white :font-size 24} font/mlongstr)
        runs (last (last text-svgs))
        y (reagent/atom (:y runs))
        x (reagent/atom (+ 8 (* 32 (dec (count text-svgs)))))]
    [safe-area/safe-area-view
     [gesture/tap-gesture-handler
      {:onHandlerStateChange #(if (gesture/tap-state-end (j/get % :nativeEvent))
                                  (let [[ex ey] (text/cursor-location (j/get % :nativeEvent) 32 text-svgs)]
                                    (js/console.log "x = " ex " y = " ey)))}
      [gesture/pan-gesture-handler {:onGestureEvent #(let [[ex ey] (text/cursor-location (j/get % :nativeEvent) 32 text-svgs)]
                                                       (js/console.log "x = " ex " y = " ey))}
       [rn/view {:style {:flexDirection "column"
                         :height "100%"
                         :width "100%"}}
                 ; :on-press #(let [[ex ey] (text/cursor-location (j/get % :nativeEvent) 32 text-svgs)]
                 ;               (js/console.log "x = " ex " y = " ey))}
        ; [rn/text {:style {:backgroundColor "green"}} "Hello CLojure! from CLJS "]
        [:> blinkview {"useNativeDriver" false}
         [rn/view {:style {:position :absolute :top (if @y @y 0) :left @x}}
          [:> svg/Svg {:width 32 :height 2}
           [:> svg/Rect {:x "0" :y "0" :width 32 :height 2 :fill "black"}]]]]
        ; [rn/view {:style {:position :absolute :top 140 :left 81}}
        ;  [:> svg/Svg {:width 45 :height 20}
        ;   [:> svg/Circle {:cx "5" :cy "5" :r "5" :fill "black"}]
        ;   [:> svg/Rect {:x "10" :y "4.5" :width 25 :height 2 :fill "black"}]]]
        ; [rn/view {:style {:position :absolute :top 150 :left 81}}
        ;  [:> svg/Svg {:width 45 :height 20}
        ;   [:> svg/Circle {:cx "40" :cy "5" :r "5" :fill "black"}]
        ;   [:> svg/Rect {:x "10" :y "4.5" :width 25 :height 2 :fill "black"}]]]

        (text/flat-list-text {:width 32 :fill "gray" :color "black" :height 200 :font :white :font-size 24} text-svgs)]]]]))





(defn init []
  (font/init)

  (rn/register-comp "cljapp" views/root-stack))



(comment
  (require '[promesa.core :as p])
  @font/fonts
  (js/console.log "hahah"))
