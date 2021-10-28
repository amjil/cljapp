(ns app.ui.keyboard
  (:require
   [steroid.rn.core :as rn]
   [app.font.base :as font]
   [app.text.index :as text]
   [app.components.gesture :as gesture]
   [applied-science.js-interop :as j]
   [cljs-bean.core :as bean]
   [reagent.core :as reagent]
   [steroid.rn.components.list :as rn-list]
   [steroid.rn.components.ui :as ui]
   [steroid.rn.components.ui :as ui]
   [steroid.rn.components.touchable :as touchable]

   ["react-native-smooth-blink-view" :default blinkview]
   ["react-native-svg" :as svg]
   ["react-native-advanced-ripple" :as ripple]))

(defn text-editor [height text]
  (let [text-svgs (text/text-component {:width 32 :fill "gray" :color "black" :height height :font :white :font-size 24} text)
        runs (last (last text-svgs))
        x (reagent/atom nil)
        y (reagent/atom nil)
        blink-cursor? (reagent/atom true)
        touch-state (reagent/atom 0)]
    (reset! y (:y runs))
    (reset! x (+ 8 (* 32 (dec (count text-svgs)))))
    (fn []
      [gesture/tap-gesture-handler
       {:onHandlerStateChange #(if (gesture/tap-state-end (j/get % :nativeEvent))
                                   (let [[ex ey] (text/cursor-location (j/get % :nativeEvent) 32 text-svgs)]
                                     ; (js/console.log "x = " ex " y = " ey)
                                     (reset! blink-cursor? true)
                                     (reset! x ex)
                                     (reset! y ey)))}
       [gesture/pan-gesture-handler {:onGestureEvent #(let [[ex ey] (text/cursor-location (j/get % :nativeEvent) 32 text-svgs)]
                                                        ; (js/console.log "x = " ex " y = " ey)
                                                        (reset! blink-cursor? true)
                                                        (reset! x ex)
                                                        (reset! y ey))}
        [rn/view {:style {:height "100%" :width "100%"}}
         (when @blink-cursor?
            [:> blinkview {"useNativeDriver" false}
             [rn/view {:style {:position :absolute :top (or @y 0) :left (or @x 0)}}
              [:> svg/Svg {:width 32 :height 2}
               [:> svg/Rect {:x "0" :y "0" :width 32 :height 2 :fill "black"}]]]])
         ; [text/flat-list-text {:width 32 :fill "gray" :color "black" :height height :font :white :font-size 24} text-svgs]]]])))
         [rn-list/flat-list
          {:key-fn    identity
           :data      text-svgs
           ; :render-fn text-list-item
           :render-fn (partial text/flat-list-item {:width 32 :fill "gray" :color "black" :height height :font :white :font-size 24})
           :horizontal true
           ; :onScroll #(do
           ;              (js/console.log "on scroll >>>>"))
           :on-touch-start #(reset! blink-cursor? false)
           :on-touch-move #(do
                             (reset! blink-cursor? false)
                             (js/console.log "on touch move"))
           ; :onResponderMove #(js/console.log "on responder move >>>")
           ; :onMomentumScrollBegin #(do
           ;                           (js/console.log "onmomentum scroll start >>>"))
           :onMomentumScrollEnd #(do
                                    (swap! blink-cursor? not)
                                    (js/console.log "onmomentum scroll end >>>"))
           :onScrollBeginDrag #(do (reset! blink-cursor? false)
                                   (js/console.log "on scroll begin >>>"))
           :onScrollEndDrag #(do
                               (js/console.log "on scroll end >>>"))}]]]])))
(def key-style
  {
          :flex-direction "row"
          :flex 1
          :justifyContent "center"
          :alignItems "center"
          :backgroundColor "#FFF"
          :borderRightColor "#e8e8e8"
          :borderRightWidth 1
          :borderBottomColor "#e8e8e8"
          :borderBottomWidth 1})
          ; :height 38})

(def key-con-style
  {:backgroundColor "#FFF"
   :borderRightColor "#e8e8e8"
   :borderRightWidth 1
   :borderBottomColor "#e8e8e8"
   :borderBottomWidth 1
   :flex 1})

(def key-text-style
  {
    :fontWeight "400"
          :fontSize 25,
          :textAlign "center",
          :color "#222222"})
          ; :width 50})

(defn keyboard-view []
  (let [h (reagent/atom nil)]
    (fn []
      [rn/view {:style {:flex-direction "column" :width "100%" :height "100%"
                        :flex 1}}
       [rn/view {:style {:width "100%"
                         :flex 2}
                 :on-layout #(let [height (j/get-in % [:nativeEvent :layout :height])]
                               (reset! h height))}
        (when @h
          [text-editor @h font/mlongstr])]
       [rn/view {:style {:flex nil
                         :width "100%"
                         :flex-direction "column"
                         :justifyContent "flex-end"}}
        [rn/view {:style {:height 180
                          :backgroundColor "#FFF"
                           :alignItems "center"
                           :justifyContent "center"}}
                          ; :flex 1 :flex-direction "column"}}
         [rn/view {:style {:flex 1 :flex-direction "row"
                           :alignItems "center"
                           :justifyContent "center"}}
          [rn/view {:style key-con-style}
           [:> ripple {:rippleColor "#000" :style key-style}
            [rn/text {:style key-text-style} "x"]]]
          [rn/view {:style key-con-style}
           [:> ripple {:rippleColor "#000" :style key-style}
            [rn/text {:style key-text-style} "x"]]]
          [rn/view {:style key-con-style}
           [:> ripple {:rippleColor "#000" :style key-style}
            [rn/text {:style key-text-style} "x"]]]
          [rn/view {:style key-con-style}
           [:> ripple {:rippleColor "#000" :style key-style}
            [rn/text {:style key-text-style} "x"]]]
          [rn/view {:style key-con-style}
           [:> ripple {:rippleColor "#000" :style key-style}
            [rn/text {:style key-text-style} "x"]]]
          [rn/view {:style key-con-style}
           [:> ripple {:rippleColor "#000" :style key-style}
            [rn/text {:style key-text-style} "x"]]]
          [rn/view {:style key-con-style}
           [:> ripple {:rippleColor "#000" :style key-style}
            [rn/text {:style key-text-style} "x"]]]
          [rn/view {:style key-con-style}
           [:> ripple {:rippleColor "#000" :style key-style}
            [rn/text {:style key-text-style} "x"]]]]
         [rn/view {:style {:flex 1 :flex-direction "row"
                           :alignItems "center"
                           :justifyContent "center"}}
          [rn/view {:style key-con-style}
           [:> ripple {:rippleColor "#000" :style key-style}
            [rn/text {:style key-text-style} "x"]]]
          [rn/view {:style key-con-style}
           [:> ripple {:rippleColor "#000" :style key-style}
            [rn/text {:style key-text-style} "x"]]]
          [rn/view {:style key-con-style}
           [:> ripple {:rippleColor "#000" :style key-style}
            [rn/text {:style key-text-style} "x"]]]
          [rn/view {:style key-con-style}
           [:> ripple {:rippleColor "#000" :style key-style}
            [rn/text {:style key-text-style} "x"]]]
          [rn/view {:style key-con-style}
           [:> ripple {:rippleColor "#000" :style key-style}
            [rn/text {:style key-text-style} "x"]]]
          [rn/view {:style key-con-style}
           [:> ripple {:rippleColor "#000" :style key-style}
            [rn/text {:style key-text-style} "x"]]]
          [rn/view {:style key-con-style}
           [:> ripple {:rippleColor "#000" :style key-style}
            [rn/text {:style key-text-style} "x"]]]
          [rn/view {:style key-con-style}
           [:> ripple {:rippleColor "#000" :style key-style}
            [rn/text {:style key-text-style} "x"]]]]
         [rn/view {:style {:flex 1 :flex-direction "row"
                           :alignItems "center"
                           :justifyContent "center"}}
          [rn/view {:style key-con-style}
           [:> ripple {:rippleColor "#000" :style key-style}
            [rn/text {:style key-text-style} "x"]]]
          [rn/view {:style key-con-style}
           [:> ripple {:rippleColor "#000" :style key-style}
            [rn/text {:style key-text-style} "x"]]]
          [rn/view {:style key-con-style}
           [:> ripple {:rippleColor "#000" :style key-style}
            [rn/text {:style key-text-style} "x"]]]
          [rn/view {:style key-con-style}
           [:> ripple {:rippleColor "#000" :style key-style}
            [rn/text {:style key-text-style} "x"]]]
          [rn/view {:style key-con-style}
           [:> ripple {:rippleColor "#000" :style key-style}
            [rn/text {:style key-text-style} "x"]]]
          [rn/view {:style key-con-style}
           [:> ripple {:rippleColor "#000" :style key-style}
            [rn/text {:style key-text-style} "x"]]]
          [rn/view {:style key-con-style}
           [:> ripple {:rippleColor "#000" :style key-style}
            [rn/text {:style key-text-style} "x"]]]
          [rn/view {:style key-con-style}
           [:> ripple {:rippleColor "#000" :style key-style}
            [rn/text {:style key-text-style} "x"]]]]
         [rn/view {:style {:flex 1 :flex-direction "row"
                           :alignItems "center"
                           :justifyContent "center"}}
          [rn/view {:style key-con-style}
           [:> ripple {:rippleColor "#000" :style key-style}
            [rn/text {:style key-text-style} "x"]]]
          [rn/view {:style key-con-style}
           [:> ripple {:rippleColor "#000" :style key-style}
            [rn/text {:style key-text-style} "x"]]]
          [rn/view {:style key-con-style}
           [:> ripple {:rippleColor "#000" :style key-style}
            [rn/text {:style key-text-style} "x"]]]
          [rn/view {:style key-con-style}
           [:> ripple {:rippleColor "#000" :style key-style}
            [rn/text {:style key-text-style} "x"]]]
          [rn/view {:style key-con-style}
           [:> ripple {:rippleColor "#000" :style key-style}
            [rn/text {:style key-text-style} "x"]]]
          [rn/view {:style key-con-style}
           [:> ripple {:rippleColor "#000" :style key-style}
            [rn/text {:style key-text-style} "x"]]]
          [rn/view {:style key-con-style}
           [:> ripple {:rippleColor "#000" :style key-style}
            [rn/text {:style key-text-style} "x"]]]
          [rn/view {:style key-con-style}
           [:> ripple {:rippleColor "#000" :style key-style}
            [rn/text {:style key-text-style} "x"]]]]
         [rn/view {:style {:flex 1 :flex-direction "row"
                           :alignItems "center"
                           :justifyContent "center"}}
         ; [rn/view {:style key-style}
          [rn/view {:style key-con-style}
           [:> ripple {:rippleColor "#000" :style key-style}
            [rn/text {:style key-text-style} "x"]]]
          [rn/view {:style key-con-style}
           [:> ripple {:rippleColor "#000" :style key-style}
            [rn/text {:style key-text-style} "x"]]]
          [rn/view {:style (merge key-con-style {:flex 1.5})}
           [:> ripple {:rippleColor "#000" :style key-style}
            [rn/text {:style key-text-style} "x"]]]
          [rn/view {:style (merge key-con-style {:flex 1.5})}
           [:> ripple {:rippleColor "#000" :style key-style}
            [rn/text {:style key-text-style} "x"]]]
          [rn/view {:style (merge key-con-style {:flex 1.5})}
           [:> ripple {:rippleColor "#000" :style key-style}
            [rn/text {:style key-text-style} "x"]]]
          [rn/view {:style (merge key-con-style {:flex 1.5})}
           [:> ripple {:rippleColor "#000" :style key-style}
            [rn/text {:style key-text-style} "x"]]]]]]])))
